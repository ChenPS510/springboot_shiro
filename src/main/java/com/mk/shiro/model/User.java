package com.mk.shiro.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author mk.chen
 * @since 2020-01-09
 */
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 手机号
     */
    private Long phone;
    /**
     * 盐
     */
    private String salt;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否是超级管理员
     */
    private Boolean isAdmin;
    /**
     * 账号是否有效
     */
    private Boolean isEnable;
    private String createId;
    private String createName;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    public User() {
    }

    public User(Integer id, String name, Long phone, String salt, String password, Boolean isAdmin, Boolean isEnable, String createId, String createName, LocalDateTime createTime) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.salt = salt;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isEnable = isEnable;
        this.createId = createId;
        this.createName = createName;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                ", name=" + name +
                ", phone=" + phone +
                ", salt=" + salt +
                ", password=" + password +
                ", isAdmin=" + isAdmin +
                ", isEnable=" + isEnable +
                ", createId=" + createId +
                ", createName=" + createName +
                ", createTime=" + createTime +
                "}";
    }
}
