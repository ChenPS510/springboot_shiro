package com.mk.shiro.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author mk.chen
 * @since 2020-01-09
 */
@TableName("role_power")
public class RolePower implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer roleId;
    private Integer powerId;

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

    public Integer getPowerId() {
        return powerId;
    }

    public void setPowerId(Integer powerId) {
        this.powerId = powerId;
    }

    @Override
    public String toString() {
        return "RolePower{" +
        ", roleId=" + roleId +
        ", powerId=" + powerId +
        "}";
    }
}
