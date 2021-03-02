package com.mk.shiro.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.shiro.model.User;
import com.mk.shiro.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 机构用户表 服务实现类
 * </p>
 *
 * @author mk.chen
 * @since 2020-01-09
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * 根据手机号找用户
     *
     * @param phone
     * @return
     */
    public User findByPhone(long phone) {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("phone", phone);
        return baseMapper.selectOne(ew);
    }

    /**
     * 根据id找用户
     *
     * @param userId
     * @return
     */
    public Optional<User> findById(Integer userId) {
        User user = baseMapper.selectById(userId);
        return Optional.ofNullable(user);
    }
}
