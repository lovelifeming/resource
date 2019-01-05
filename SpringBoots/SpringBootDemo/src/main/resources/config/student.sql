/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : test_db

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-04-11 17:32:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_no` varchar(20) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `user_sex` varchar(20) NOT NULL,
  `user_birthday` datetime DEFAULT NULL,
  `user_class` varchar(20) DEFAULT NULL,
  `createtime` datetime NOT NULL,
  `updatetime` datetime NOT NULL,
  PRIMARY KEY (`id`,`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1', '108', '曾华', '1313445', '男', '1977-09-01 00:00:00', '95033', '2017-09-17 18:09:51', '2017-09-17 18:09:51');
INSERT INTO `student` VALUES ('2', '105', '匡明', 'dad363', '男', '1975-10-02 00:00:00', '95031', '2017-09-17 18:10:29', '2017-09-17 18:10:29');
INSERT INTO `student` VALUES ('3', '107', '王丽', '1d4515', '女', '1976-01-23 00:00:00', '95033', '2017-09-17 18:11:03', '2017-09-17 18:11:03');
INSERT INTO `student` VALUES ('4', '109', '王芳', '146464', '女', '1975-02-10 00:00:00', '95031', '2017-09-17 18:11:40', '2017-09-17 18:11:40');
INSERT INTO `student` VALUES ('5', '101', '李军', '8761367', '男', '1976-02-20 00:00:00', '95033', '2017-09-17 18:12:19', '2017-09-17 18:12:19');
INSERT INTO `student` VALUES ('6', '103', '陆君', 'c1336c1', '男', '1974-06-03 00:00:00', '95031', '2017-09-17 18:12:50', '2017-09-17 18:12:50');
INSERT INTO `student` VALUES ('7', '110', '李晓明', '313deq', '男', '1979-06-03 00:00:00', '95035', '2017-09-17 18:12:50', '2017-09-17 18:12:50');
INSERT INTO `student` VALUES ('8', '113', '杜玉萍', '6498fr', '女', '1980-06-03 00:00:00', '95035', '2017-09-17 18:12:50', '2017-09-17 18:12:50');
