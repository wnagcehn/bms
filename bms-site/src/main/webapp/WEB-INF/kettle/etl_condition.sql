/*
Navicat MySQL Data Transfer

Source Server         : 114.55.0.68[PRD]
Source Server Version : 50711
Source Host           : 114.55.0.68:3306
Source Database       : bmssite_prd

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2017-07-24 17:53:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `etl_condition`
-- ----------------------------
DROP TABLE IF EXISTS `etl_condition`;
CREATE TABLE `etl_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pull_type` varchar(24) DEFAULT NULL COMMENT '业务数据类型',
  `last_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次执行时间',
  `curr_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `takes_time` int(11) DEFAULT NULL COMMENT '耗时',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_condition
-- ----------------------------
INSERT INTO `etl_condition` VALUES ('1', 'dispatchBill', '2017-05-31 23:00:00', '2017-07-24 17:40:27', null);
INSERT INTO `etl_condition` VALUES ('2', 'instockMaster', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('3', 'instockDetail', '2017-05-31 23:00:00', '2017-07-24 17:40:28', null);
INSERT INTO `etl_condition` VALUES ('4', 'outStockMaster', '2017-05-31 23:00:00', '2017-07-24 17:40:28', null);
INSERT INTO `etl_condition` VALUES ('5', 'outStockDetail', '2017-05-31 23:00:00', '2017-07-24 17:40:28', null);
INSERT INTO `etl_condition` VALUES ('6', 'outStockPackMaterial', '2017-05-31 23:00:00', '2017-07-24 17:40:31', null);
INSERT INTO `etl_condition` VALUES ('7', 'packStorage', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('8', 'productStorage01', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('9', 'ganXianRoadBill', '2017-05-31 23:00:00', '2017-07-24 17:40:29', null);
INSERT INTO `etl_condition` VALUES ('10', 'ganXianWayBill', '2017-05-31 23:00:00', '2017-07-24 17:40:29', null);
INSERT INTO `etl_condition` VALUES ('11', 'feeAbnormal', '2017-05-31 23:00:00', '2017-07-24 17:40:28', null);
INSERT INTO `etl_condition` VALUES ('12', 'cusComplaintReason', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('13', 'productStorage02', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('14', 'productStorage03', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('15', 'productStorage04', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('16', 'productStorage05', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('17', 'productStorage06', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('18', 'productStorage07', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('19', 'productStorage08', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('20', 'productStorage09', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('21', 'productStorage10', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('22', 'productStorage11', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('23', 'productStorage27', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('24', 'productStorage31', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('25', 'productStorage32', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('26', 'productStorage33', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('27', 'productStorage34', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('28', 'productStorage35', '2017-05-31 23:00:00', '2017-07-24 17:25:56', null);
INSERT INTO `etl_condition` VALUES ('29', 'dispatchPayBill', '2017-05-31 23:00:00', '2017-07-24 17:40:28', null);
