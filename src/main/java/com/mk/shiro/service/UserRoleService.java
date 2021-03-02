package com.mk.shiro.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.shiro.exception.BusinessException;
import com.mk.shiro.exception.ResultCode;
import com.mk.shiro.model.Role;
import com.mk.shiro.model.UserRole;
import com.mk.shiro.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mk.chen
 * @since 2020-01-09
 */
@Service
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole> implements IService<UserRole> {
    @Autowired
    private RoleService roleService;

    /**
     * 根据用户id查用户角色
     *
     * @param userId
     * @return
     */
    public Optional<List<UserRole>> getRoleListByUserId(Integer userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return Optional.ofNullable(baseMapper.selectList(queryWrapper));
    }

    /**
     * 获取用户的角色信息
     *
     * @param userId
     * @return
     */
    public Optional<Role> getRoleInfoByUserId(Integer userId) {
        List<UserRole> userRoleList = getRoleListByUserId(userId).orElseThrow(() -> new BusinessException("该用户暂无角色"));
        if (CollectionUtils.isEmpty(userRoleList)) {
            throw new BusinessException("该用户暂无角色");
        }
        return Optional.ofNullable(roleService.getById(userRoleList.get(0).getRoleId()));
    }
}
