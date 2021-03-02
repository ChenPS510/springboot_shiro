package com.mk.shiro.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.shiro.model.RolePower;
import com.mk.shiro.mapper.RolePowerMapper;
import org.springframework.stereotype.Service;

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
public class RolePowerService extends ServiceImpl<RolePowerMapper, RolePower> implements IService<RolePower> {

    /**
     * 根据角色id获取用户的角色权限关系
     *
     * @param roleId
     * @return
     */
    public Optional<List<RolePower>> getPowerIdsByRoleId(Integer roleId) {
        QueryWrapper<RolePower> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<RolePower> rolePowers = baseMapper.selectList(queryWrapper);

        return Optional.ofNullable(rolePowers);
    }
}
