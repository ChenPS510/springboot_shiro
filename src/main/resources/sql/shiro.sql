/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50642
Source Host           : localhost:3306
Source Database       : shiro

Target Server Type    : MYSQL
Target Server Version : 50642
File Encoding         : 65001

Date: 2020-01-10 17:40:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for power
-- ----------------------------
DROP TABLE IF EXISTS `power`;
CREATE TABLE `power` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL COMMENT '权限名称',
  `value` varchar(36) NOT NULL COMMENT '权限字符串（a:b、a:b:c）',
  `parent_id` varchar(36) NOT NULL COMMENT '父权限id',
  `power_type` varchar(16) NOT NULL COMMENT '权限类型（MENU/BUTTON）',
  `create_id` varchar(36) NOT NULL,
  `create_name` varchar(16) NOT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of power
-- ----------------------------
INSERT INTO `power` VALUES ('1', '平台管理', 'platform', '0', 'MENU', '1', 'admin', '2020-01-10 15:11:56');
INSERT INTO `power` VALUES ('2', '用户管理', 'platform:employee', '1', 'MENU', '1', 'admin', '2020-01-10 15:12:53');
INSERT INTO `power` VALUES ('3', '用户增加', 'platform:employee:create', '2', 'BUTTON', '1', 'admin', '2020-01-10 15:13:56');
INSERT INTO `power` VALUES ('4', '用户删除', 'platform:employee:delete', '2', 'BUTTON', '1', 'admin', '2020-01-10 15:13:56');
INSERT INTO `power` VALUES ('5', '用户修改', 'platform:employee:update', '2', 'BUTTON', '1', 'admin', '2020-01-10 15:13:56');
INSERT INTO `power` VALUES ('6', '用户查看', 'platform:employee:select', '2', 'BUTTON', '1', 'admin', '2020-01-10 15:13:56');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(36) NOT NULL COMMENT '角色名称',
  `create_id` int(11) DEFAULT NULL,
  `create_name` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '普通角色001', '1', 'admin', '2020-01-10 15:10:32');

-- ----------------------------
-- Table structure for role_power
-- ----------------------------
DROP TABLE IF EXISTS `role_power`;
CREATE TABLE `role_power` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `power_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of role_power
-- ----------------------------
INSERT INTO `role_power` VALUES ('1', '1', '1');
INSERT INTO `role_power` VALUES ('2', '1', '2');
INSERT INTO `role_power` VALUES ('3', '2', '3');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(36) NOT NULL AUTO_INCREMENT COMMENT '主键，uuid',
  `name` varchar(16) NOT NULL COMMENT '用户名称',
  `phone` bigint(20) NOT NULL COMMENT '手机号',
  `salt` varchar(255) NOT NULL COMMENT '盐',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `is_admin` tinyint(1) NOT NULL COMMENT '是否是超级管理员',
  `is_enable` tinyint(1) NOT NULL COMMENT '账号是否有效',
  `create_id` varchar(36) DEFAULT NULL,
  `create_name` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='机构用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '11111111111', '123', 'f8b91be4905b14798437bf333d0d0ed5', '1', '1', '1', 'admin', '2020-01-09 20:41:38');
INSERT INTO `user` VALUES ('2', 'user001', '22222222222', '123', 'f8b91be4905b14798437bf333d0d0ed5', '0', '1', '1', 'admin', '2020-01-09 20:41:38');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1', '2');
