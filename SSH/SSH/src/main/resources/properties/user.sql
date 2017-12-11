/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : personal_test

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-12-11 11:40:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(255) NOT NULL COMMENT '用户昵称',
  `password` varchar(255) NOT NULL COMMENT '用户密码',
  `role` varchar(255) NOT NULL COMMENT '用户身份',
  `status` int(1) NOT NULL COMMENT '用户状态',
  `email` varchar(255) NOT NULL COMMENT '用户邮箱',
  `regtime` datetime NOT NULL COMMENT '注册时间',
  `regip` varchar(255) NOT NULL COMMENT '注册IP',
  PRIMARY KEY (`id`,`username`),
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '123456', '1', '2', '1124554@qq.com', '2017-11-01 17:46:59', '123.43.13.55');
INSERT INTO `user` VALUES ('2', 'zhangmazi', '764312', '2', '0', '456464@qq.com', '2017-11-06 10:33:43', '133.14.134.1');
INSERT INTO `user` VALUES ('3', 'xianming', 'acc113', '3', '0', 'dq313qr@qq.com', '2017-12-08 15:11:32', '13.134.45.6');
INSERT INTO `user` VALUES ('4', 'zhangsan', '31caa4', '3', '1', 'd31dad@qq.com', '2017-12-08 15:12:24', '13.134.45.61');
INSERT INTO `user` VALUES ('5', 'dagou', 'g12413', '3', '1', '425cfa@qq.com', '2017-12-08 15:12:57', '13.134.45.61');
