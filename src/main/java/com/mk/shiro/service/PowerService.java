package com.mk.shiro.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.shiro.exception.BusinessException;
import com.mk.shiro.model.*;
import com.mk.shiro.mapper.PowerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mk.chen
 * @since 2020-01-09
 */
@Service
public class PowerService extends ServiceImpl<PowerMapper, Power> implements IService<Power> {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePowerService rolePowerService;

    /**
     * 根据用户id获取用户权限信息
     *
     * @param userId
     * @return
     */
    public List<Power> getUserPowers(Integer userId) {
        User user = userService.findById(userId).orElseThrow(() -> new BusinessException("用户不存在！"));
        // 如果是超管，返回所有权限
        if (user.getAdmin()) {
            return baseMapper.selectList(new QueryWrapper<>());
        }
        // 找出该用户的角色
        List<UserRole> userRoleList = userRoleService.getRoleListByUserId(user.getId()).orElseThrow(() -> new BusinessException("获取用户角色信息失败！"));
        if (CollectionUtils.isEmpty(userRoleList)) {
            throw new BusinessException("该用户未被分配角色！");
        }
        List<RolePower> powerList = rolePowerService.getPowerIdsByRoleId(userRoleList.get(0).getRoleId()).orElseThrow(() -> new BusinessException("获取角色权限信息失败！"));
        // 找出该角色的权限信息
        if (CollectionUtils.isEmpty(powerList)) {
            return Collections.emptyList();
        }
        List<Integer> powerIds = powerList.stream().map(RolePower::getPowerId).collect(Collectors.toList());
        return baseMapper.selectBatchIds(powerIds);
    }
}
