package com.mk.shiro.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author mk.chen
 * @since 2020-01-09
 */
@TableName("user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer roleId;
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                ", roleId=" + roleId +
                ", userId=" + userId +
                "}";
    }
}
