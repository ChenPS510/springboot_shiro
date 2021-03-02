package com.mk.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.shiro.model.Role;
import com.mk.shiro.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mk.chen
 * @since 2020-01-09
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> implements IService<Role> {


}
