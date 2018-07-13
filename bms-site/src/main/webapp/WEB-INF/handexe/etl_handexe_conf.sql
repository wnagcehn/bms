/*
Navicat MySQL Data Transfer

Source Server         : 114.55.0.68[PRD]
Source Server Version : 50711
Source Host           : 114.55.0.68:3306
Source Database       : bmssite_prd

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2017-08-15 17:15:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `etl_handexe_conf`
-- ----------------------------
DROP TABLE IF EXISTS `etl_handexe_conf`;
CREATE TABLE `etl_handexe_conf` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(50) DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_handexe_conf
-- ----------------------------
INSERT INTO `etl_handexe_conf` VALUES ('1', '1100000619', '2017-07-01 00:00:00', '2017-08-01 00:00:00');
