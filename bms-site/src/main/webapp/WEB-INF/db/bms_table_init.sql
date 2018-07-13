/*
Navicat MySQL Data Transfer

Source Server         : 118.178.139.146[UAT]
Source Server Version : 50625
Source Host           : 118.178.139.146:3306
Source Database       : bmsuat

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2017-07-24 17:42:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bdf2_blob_store`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_blob_store`;
CREATE TABLE `bdf2_blob_store` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`CONTENT_`  longblob NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_clob_store`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_clob_store`;
CREATE TABLE `bdf2_clob_store` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`CONTENT_`  longtext CHARACTER SET utf8 COLLATE utf8_bin NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_company`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_company`;
CREATE TABLE `bdf2_company` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`DESC_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_component`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_component`;
CREATE TABLE `bdf2_component` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`COMPONENT_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`DESC_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_dept`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_dept`;
CREATE TABLE `bdf2_dept` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`COMPANY_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`CREATE_DATE_`  datetime NULL DEFAULT NULL ,
`DESC_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`PARENT_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_group`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_group`;
CREATE TABLE `bdf2_group` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`COMPANY_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`CREATE_DATE_`  datetime NULL DEFAULT NULL ,
`DESC_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`PARENT_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_group_member`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_group_member`;
CREATE TABLE `bdf2_group_member` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`DEPT_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`GROUP_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`POSITION_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`USERNAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_message`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_message`;
CREATE TABLE `bdf2_message` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`CONTENT_`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`READ_`  bit(1) NOT NULL ,
`RECEIVER_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`REPLY_`  bit(1) NULL DEFAULT NULL ,
`SEND_DATE_`  datetime NULL DEFAULT NULL ,
`SENDER_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`TAGS_`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`TITLE_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_message_template`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_message_template`;
CREATE TABLE `bdf2_message_template` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`COMPANY_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`CONTENT_`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`TYPE_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_position`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_position`;
CREATE TABLE `bdf2_position` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`COMPANY_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`CREATE_DATE_`  datetime NULL DEFAULT NULL ,
`DESC_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_report_definition`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_report_definition`;
CREATE TABLE `bdf2_report_definition` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`CREATE_DATE_`  datetime NULL DEFAULT NULL ,
`DATA_SOURCE_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`DATA_SOURCE_TYPE_`  varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`REPORT_FILE_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_report_parameter`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_report_parameter`;
CREATE TABLE `bdf2_report_parameter` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`REPORT_DEFINITION_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`VALUE_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_report_resource`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_report_resource`;
CREATE TABLE `bdf2_report_resource` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`REPORT_DEFINITION_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`RESOURCE_FILE_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_role`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_role`;
CREATE TABLE `bdf2_role` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`COMPANY_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`CREATE_DATE_`  datetime NULL DEFAULT NULL ,
`CREATE_USER_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`DESC_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`ENABLED_`  bit(1) NULL DEFAULT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`PARENT_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`TYPE_`  varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_role_member`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_role_member`;
CREATE TABLE `bdf2_role_member` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`CREATE_DATE_`  datetime NULL DEFAULT NULL ,
`DEPT_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`GRANTED_`  bit(1) NULL DEFAULT NULL ,
`POSITION_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`ROLE_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`USERNAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`GROUP_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`),
FOREIGN KEY (`GROUP_ID_`) REFERENCES `bdf2_group` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK79BEE4326C254D1D` (`GROUP_ID_`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_role_resource`;
CREATE TABLE `bdf2_role_resource` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`PACKAGE_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`ROLE_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`URL_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_upload_definition`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_upload_definition`;
CREATE TABLE `bdf2_upload_definition` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`CREATE_DATE_`  datetime NULL DEFAULT NULL ,
`CREATE_USER_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`FILE_NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`SIZE_`  bigint(20) NULL DEFAULT NULL ,
`UPLOAD_PROCESSOR_KEY_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_url`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_url`;
CREATE TABLE `bdf2_url` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`COMPANY_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`DESC_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`FOR_NAVIGATION_`  bit(1) NOT NULL ,
`ICON_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`ORDER_`  int(11) NULL DEFAULT NULL ,
`PARENT_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`SYSTEM_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`URL_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`oldurl`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_url_bak`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_url_bak`;
CREATE TABLE `bdf2_url_bak` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`COMPANY_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`DESC_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`FOR_NAVIGATION_`  bit(1) NOT NULL ,
`ICON_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`NAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`ORDER_`  int(11) NULL DEFAULT NULL ,
`PARENT_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`SYSTEM_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`URL_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`oldurl`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_url_component`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_url_component`;
CREATE TABLE `bdf2_url_component` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`AUTHORITY_TYPE_`  varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`ROLE_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`URL_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`COMPONENT_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
PRIMARY KEY (`ID_`),
FOREIGN KEY (`COMPONENT_ID_`) REFERENCES `bdf2_component` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FKFCFBDBDCD4C56CC` (`COMPONENT_ID_`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_user`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_user`;
CREATE TABLE `bdf2_user` (
`USERNAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`ADDRESS_`  varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`ADMINISTRATOR_`  bit(1) NOT NULL ,
`BIRTHDAY_`  datetime NULL DEFAULT NULL ,
`CNAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`COMPANY_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`CREATE_DATE_`  datetime NULL DEFAULT NULL ,
`EMAIL_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`ENABLED_`  bit(1) NOT NULL ,
`ENAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`MALE_`  bit(1) NOT NULL ,
`MOBILE_`  varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`PASSWORD_`  varchar(70) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`SALT_`  varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
PRIMARY KEY (`USERNAME_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_user_dept`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_user_dept`;
CREATE TABLE `bdf2_user_dept` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`DEPT_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`USERNAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bdf2_user_position`
-- ----------------------------
DROP TABLE IF EXISTS `bdf2_user_position`;
CREATE TABLE `bdf2_user_position` (
`ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`POSITION_ID_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`USERNAME_`  varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
PRIMARY KEY (`ID_`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `bill_dispatch_distinct`
-- ----------------------------
DROP TABLE IF EXISTS `bill_dispatch_distinct`;
CREATE TABLE `bill_dispatch_distinct` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`deliveryid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编号' ,
`bill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单编号' ,
`head_weight`  double NULL DEFAULT 0 COMMENT '首重重量' ,
`head_price`  double NULL DEFAULT 0 COMMENT '首重价格' ,
`continued_weight`  double NULL DEFAULT 0 COMMENT '续重重量' ,
`continued_price`  double NULL DEFAULT 0 COMMENT '续重价格' ,
`charged_weight`  double NULL DEFAULT 0 COMMENT '计费重量' ,
`total_weight`  double NULL DEFAULT 0 COMMENT '总重量' ,
`weight_limit`  double NULL DEFAULT 0 COMMENT '重量界限' ,
`unit_price`  double NULL DEFAULT 0 COMMENT '单价' ,
`amount`  double NULL DEFAULT 0 COMMENT '金额' ,
`diff_amount`  double NULL DEFAULT 0 COMMENT '差额' ,
`status`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `waybillno_index` (`waybill_no`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='宅配应收账单对账差异表'
AUTO_INCREMENT=23862

;

-- ----------------------------
-- Table structure for `bill_rule_pay`
-- ----------------------------
DROP TABLE IF EXISTS `bill_rule_pay`;
CREATE TABLE `bill_rule_pay` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`quotationNo`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`quotationName`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价名称' ,
`biz_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型' ,
`subject_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`remark`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`rule`  varchar(6400) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=30

;

-- ----------------------------
-- Table structure for `bill_rule_receive`
-- ----------------------------
DROP TABLE IF EXISTS `bill_rule_receive`;
CREATE TABLE `bill_rule_receive` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`quotationNo`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`quotationName`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价名称' ,
`biz_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型' ,
`subject_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`remark`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`rule`  varchar(6400) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=30

;

-- ----------------------------
-- Table structure for `biz_consumer_material_detail`
-- ----------------------------
DROP TABLE IF EXISTS `biz_consumer_material_detail`;
CREATE TABLE `biz_consumer_material_detail` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`outstock_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库单号' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`consumer_material_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '耗材编码' ,
`consumer_material_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '耗材名称' ,
`shopper`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家' ,
`num`  double NULL DEFAULT NULL COMMENT '数量' ,
`adjust_num`  double NULL DEFAULT NULL COMMENT '调整数量' ,
`fee_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编号' ,
`status`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2

;

-- ----------------------------
-- Table structure for `biz_dispatch_bill`
-- ----------------------------
DROP TABLE IF EXISTS `biz_dispatch_bill`;
CREATE TABLE `biz_dispatch_bill` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`outstock_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库单号' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`waybill_num`  double NULL DEFAULT NULL COMMENT '运单数量' ,
`waybill_list`  longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '运单列表' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编号' ,
`warehouse_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库id' ,
`warehouse_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家id' ,
`customer_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`carrier_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流商ID' ,
`carrier_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流商名称' ,
`deliverid`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商id' ,
`deliver_name`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商名称' ,
`is_calculated`  varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他' ,
`total_weight`  double NULL DEFAULT NULL COMMENT '重量' ,
`adjust_weight`  double NULL DEFAULT NULL COMMENT '调整重量' ,
`total_volume`  double NULL DEFAULT NULL COMMENT '体积' ,
`service_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务类型' ,
`dispatch_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配送类型' ,
`collect_money`  double NULL DEFAULT NULL COMMENT '代收金额' ,
`month_fee_count`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '月结账号' ,
`send_name`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人' ,
`send_province_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人省份' ,
`send_city_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人城市' ,
`send_district_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人地区' ,
`send_street`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人街道' ,
`send_detail_address`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人详细地址' ,
`receive_name`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人' ,
`receive_province_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人省份' ,
`receive_city_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人城市' ,
`receive_district_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人地区' ,
`receive_street`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人街道' ,
`receive_detail_address`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人详细地址' ,
`account_state`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算状态' ,
`external_no`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部单号' ,
`sign_time`  timestamp NULL DEFAULT NULL COMMENT '签收时间' ,
`accept_time`  timestamp NULL DEFAULT NULL COMMENT '揽收时间' ,
`receive_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '收件时间' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '单据创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
`calculate_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
`del_flag`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `idx_id` (`id`) USING BTREE ,
INDEX `idx_outstock_no` (`outstock_no`) USING BTREE ,
INDEX `idx_waybill_no` (`waybill_no`) USING BTREE ,
INDEX `idx_is_calculated` (`is_calculated`) USING BTREE ,
INDEX `idx_create_time` (`create_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=392525

;

-- ----------------------------
-- Table structure for `biz_dispatch_pay_bill`
-- ----------------------------
DROP TABLE IF EXISTS `biz_dispatch_pay_bill`;
CREATE TABLE `biz_dispatch_pay_bill` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`outstock_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库单号' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`waybill_num`  double NULL DEFAULT NULL COMMENT '运单数量' ,
`waybill_list`  longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '运单列表' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编号' ,
`warehouse_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库id' ,
`warehouse_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家id' ,
`customer_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`carrier_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流商ID' ,
`carrier_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流商名称' ,
`deliverid`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商id' ,
`deliver_name`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商名称' ,
`is_calculated`  varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他' ,
`total_weight`  double NULL DEFAULT NULL COMMENT '重量' ,
`adjust_weight`  double NULL DEFAULT NULL COMMENT '调整重量' ,
`total_volume`  double NULL DEFAULT NULL COMMENT '体积' ,
`service_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务类型' ,
`dispatch_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配送类型' ,
`collect_money`  double NULL DEFAULT NULL COMMENT '代收金额' ,
`month_fee_count`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '月结账号' ,
`send_name`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人' ,
`send_province_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人省份' ,
`send_city_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人城市' ,
`send_district_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人地区' ,
`send_street`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人街道' ,
`send_detail_address`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄件人详细地址' ,
`receive_name`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人' ,
`receive_province_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人省份' ,
`receive_city_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人城市' ,
`receive_district_id`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人地区' ,
`receive_street`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人街道' ,
`receive_detail_address`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人详细地址' ,
`account_state`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算状态' ,
`external_no`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部单号' ,
`sign_time`  timestamp NULL DEFAULT NULL COMMENT '签收时间' ,
`accept_time`  timestamp NULL DEFAULT NULL COMMENT '揽收时间' ,
`receive_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '收件时间' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '单据创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
`calculate_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
`del_flag`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `idx_id` (`id`) USING BTREE ,
INDEX `idx_outstock_no` (`outstock_no`) USING BTREE ,
INDEX `idx_waybill_no` (`waybill_no`) USING BTREE ,
INDEX `idx_is_calculated` (`is_calculated`) USING BTREE ,
INDEX `idx_create_time` (`create_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=330041

;

-- ----------------------------
-- Table structure for `biz_ganxian_roadbill`
-- ----------------------------
DROP TABLE IF EXISTS `biz_ganxian_roadbill`;
CREATE TABLE `biz_ganxian_roadbill` (
`id`  bigint(50) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`tms_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'tms id' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编号' ,
`roadbill_no`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路单号' ,
`roadbill_name`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路单名称' ,
`roadbill_type`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路单类型' ,
`carrier_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '承运商编码' ,
`carrier_name`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '承运商名称' ,
`warehouse_code`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库编码' ,
`waybill_num`  double NULL DEFAULT NULL COMMENT '运单数' ,
`total_box`  double NULL DEFAULT NULL COMMENT '总箱数' ,
`total_weight`  double NULL DEFAULT NULL COMMENT '总重量' ,
`adjust_weight`  double NULL DEFAULT NULL COMMENT '调整重量' ,
`total_volume`  double NULL DEFAULT NULL COMMENT '总体积' ,
`is_calculated`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他' ,
`send_province_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发货省' ,
`send_city_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发货市' ,
`send_district_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发货区县' ,
`receiver_province_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货省' ,
`receiver_city_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货市' ,
`receiver_district_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货区县' ,
`receiver`  varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人' ,
`receiver_telephone`  varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人电话' ,
`temperature_type_code`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`is_light`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否轻货' ,
`vehicle_no`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车牌号' ,
`car_model`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型' ,
`transport_type`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`send_person`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发运人' ,
`send_time`  timestamp NULL DEFAULT NULL COMMENT '发车时间' ,
`finished_time`  timestamp NULL DEFAULT NULL COMMENT '完成时间' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`account_state`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态' ,
`creator`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`calculate_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `idx_id` (`id`) USING BTREE ,
INDEX `idx_roadbill_no` (`roadbill_no`) USING BTREE ,
INDEX `idx_fees_no` (`fees_no`) USING BTREE ,
INDEX `id_calculate_time` (`calculate_time`) USING BTREE ,
INDEX `id_create_time` (`create_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='干线路单'
AUTO_INCREMENT=335

;

-- ----------------------------
-- Table structure for `biz_ganxian_waybill`
-- ----------------------------
DROP TABLE IF EXISTS `biz_ganxian_waybill`;
CREATE TABLE `biz_ganxian_waybill` (
`id`  bigint(50) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`tms_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'tms id' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家号' ,
`customer_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`order_no`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号' ,
`ganxian_no`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '干线运单号' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路单号' ,
`biz_type_code`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型' ,
`warehouse_code`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库' ,
`send_province_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发货省' ,
`send_city_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发货市' ,
`send_district_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发货区县' ,
`receiver_province_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货省' ,
`receiver_city_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货市' ,
`receiver_district_id`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货区县' ,
`receiver`  varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人' ,
`receiver_telephone`  varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人电话' ,
`total_box`  double NULL DEFAULT NULL COMMENT '总箱数' ,
`total_package`  double NULL DEFAULT NULL COMMENT '总件数' ,
`total_varieties`  double NULL DEFAULT NULL COMMENT '总品种数' ,
`total_volume`  double NULL DEFAULT NULL COMMENT '总体积' ,
`total_weight`  double NULL DEFAULT NULL COMMENT '总重量' ,
`adjust_weight`  double NULL DEFAULT NULL COMMENT '调整重量' ,
`temperature_type_code`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`is_light`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否轻货 ' ,
`accept_time`  timestamp NULL DEFAULT NULL COMMENT '揽收时间' ,
`sign_time`  timestamp NULL DEFAULT NULL COMMENT '签收时间' ,
`car_model`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`transport_type`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编号' ,
`account_state`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态' ,
`creator`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`is_calculated`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否已计算费用' ,
`calculate_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `idx_id` (`id`) USING BTREE ,
INDEX `idx_ganxian_no` (`ganxian_no`) USING BTREE ,
INDEX `idx_is_calculated` (`is_calculated`) USING BTREE ,
INDEX `idx_fees_no` (`fees_no`) USING BTREE ,
INDEX `idx_create_time` (`create_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='干线运单'
AUTO_INCREMENT=291

;

-- ----------------------------
-- Table structure for `biz_instock_detail`
-- ----------------------------
DROP TABLE IF EXISTS `biz_instock_detail`;
CREATE TABLE `biz_instock_detail` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`oms_id`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`instock_no`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库单号' ,
`product_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品ID' ,
`product_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称' ,
`external_product_code`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部商品编码' ,
`category_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品类别' ,
`category_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`temperature_code`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`num`  double NULL DEFAULT NULL COMMENT '数量' ,
`adjust_num`  double NULL DEFAULT NULL COMMENT '调整数量' ,
`unit_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位' ,
`unit_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`),
INDEX `idx_instock_no` (`instock_no`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=8313

;

-- ----------------------------
-- Table structure for `biz_instock_master`
-- ----------------------------
DROP TABLE IF EXISTS `biz_instock_master`;
CREATE TABLE `biz_instock_master` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`oms_id`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`instock_no`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库单号' ,
`warehouse_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库编码' ,
`warehouse_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家id' ,
`customer_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家' ,
`external_num`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部单号' ,
`instock_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库类型' ,
`instock_date`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '入库日期' ,
`fees_no`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`fee_amount`  double NULL DEFAULT NULL COMMENT '费用' ,
`num`  double NULL DEFAULT NULL COMMENT '总数量' ,
`adjust_num`  double NULL DEFAULT NULL COMMENT '调整数量' ,
`is_calculated`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他' ,
`calculate_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
`receiver`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人' ,
`remark`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`),
INDEX `idx_instock_no` (`instock_no`) USING BTREE ,
INDEX `idx_fees_no` (`fees_no`) USING BTREE ,
INDEX `idx_calculated` (`is_calculated`) USING BTREE ,
INDEX `idx_create_time` (`create_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2284

;

-- ----------------------------
-- Table structure for `biz_outstock_detail`
-- ----------------------------
DROP TABLE IF EXISTS `biz_outstock_detail`;
CREATE TABLE `biz_outstock_detail` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`oms_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'oms_id' ,
`oms_item_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'oms_item_id' ,
`outstock_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库单号' ,
`product_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品ID' ,
`product_name`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称' ,
`external_product_code`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部商品编码' ,
`product_category_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品类别编码' ,
`product_category_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品类别名称' ,
`temperature_code`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型编码' ,
`temperature_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型名称' ,
`num`  double NULL DEFAULT NULL COMMENT '数量' ,
`resize_num`  double NULL DEFAULT NULL COMMENT '调整数量' ,
`unit_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位编号' ,
`unit_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
PRIMARY KEY (`id`),
INDEX `id_outstock_no` (`outstock_no`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='出库单明细表'
AUTO_INCREMENT=796298

;

-- ----------------------------
-- Table structure for `biz_outstock_master`
-- ----------------------------
DROP TABLE IF EXISTS `biz_outstock_master`;
CREATE TABLE `biz_outstock_master` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`oms_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'oms_id' ,
`outstock_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库单号' ,
`external_no`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部单号' ,
`warehouse_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库编号' ,
`warehouse_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家编号' ,
`customer_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`carrier_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流商编号' ,
`carrier_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流商名称' ,
`deliver_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商编号' ,
`deliver_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商名称' ,
`chyun_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商名称' ,
`chyun_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '承运商名称' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`unpacking`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否拆箱' ,
`send_time`  timestamp NULL DEFAULT NULL COMMENT '发货日期' ,
`temperature_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`temperature_type_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型名称' ,
`bill_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据类型编码' ,
`bill_type_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据类型名称' ,
`b2b_flag`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'B2B标识' ,
`total_weight`  double NULL DEFAULT NULL COMMENT '总重量' ,
`resize_weight`  double NULL DEFAULT NULL COMMENT '调整重量' ,
`total_quantity`  double NULL DEFAULT NULL COMMENT '总数量' ,
`total_varieties`  double NULL DEFAULT NULL COMMENT '总品种数' ,
`split_single`  double NULL DEFAULT NULL COMMENT '是否拆单' ,
`is_calculated`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他' ,
`calculate_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用ID' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`resize_num`  double NULL DEFAULT NULL COMMENT '调整数量' ,
PRIMARY KEY (`id`),
INDEX `idx_outstock_no` (`outstock_no`) USING BTREE ,
INDEX `idx_fees_no` (`fees_no`) USING BTREE ,
INDEX `idx_calculated` (`is_calculated`) USING BTREE ,
INDEX `idx_create_time` (`create_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='出库单主表'
AUTO_INCREMENT=393257

;

-- ----------------------------
-- Table structure for `biz_outstock_packmaterial`
-- ----------------------------
DROP TABLE IF EXISTS `biz_outstock_packmaterial`;
CREATE TABLE `biz_outstock_packmaterial` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`wms_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'wms_id' ,
`outstock_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库单号' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`customer_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家id' ,
`customer_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`consumer_material_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '耗材编码' ,
`consumer_material_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '耗材名称' ,
`warehouse_code`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`warehouse_name`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`num`  double NULL DEFAULT NULL COMMENT '数量' ,
`adjust_num`  double NULL DEFAULT 0 COMMENT '调整数量' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用ID' ,
`dbname`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属DB的名称' ,
`is_calculated`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他' ,
`calculate_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
PRIMARY KEY (`id`),
INDEX `idx_outstock_no` (`outstock_no`) USING BTREE ,
INDEX `idx_waybill_no` (`waybill_no`) USING BTREE ,
INDEX `idx_fees_no` (`fees_no`) USING BTREE ,
INDEX `idx_is_calculated` (`is_calculated`) USING BTREE ,
INDEX `idx_create_time` (`create_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='耗材出库表'
AUTO_INCREMENT=604408

;

-- ----------------------------
-- Table structure for `biz_pack_storage`
-- ----------------------------
DROP TABLE IF EXISTS `biz_pack_storage`;
CREATE TABLE `biz_pack_storage` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`wms_id`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`cur_time`  datetime NULL DEFAULT NULL COMMENT '日期(当前日期)' ,
`warehouse_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库号' ,
`warehouse_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家Id' ,
`customer_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`pack_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '耗材编号' ,
`pack_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'SKU名称' ,
`qty`  double NULL DEFAULT NULL COMMENT '数量' ,
`pallet_num`  double NULL DEFAULT NULL COMMENT '托数' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编码' ,
`dbname`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属哪个仓库的数据库' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`is_calculated`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否已计算费用' ,
`calculate_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='商品库存'
AUTO_INCREMENT=707

;

-- ----------------------------
-- Table structure for `biz_product_pallet_storage`
-- ----------------------------
DROP TABLE IF EXISTS `biz_product_pallet_storage`;
CREATE TABLE `biz_product_pallet_storage` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`data_num`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据编号' ,
`stock_time`  timestamp NULL DEFAULT NULL COMMENT '库存日期' ,
`warehouse_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库Id' ,
`warehouse_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家Id' ,
`customer_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`temperature_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`temperature_type_name`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`pallet_num`  double NULL DEFAULT NULL COMMENT '托数' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编号' ,
`account_state`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态' ,
`is_calculated`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否已计算费用' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`calculate_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
PRIMARY KEY (`id`),
INDEX `idx_fees_no` (`fees_no`) USING BTREE ,
INDEX `idx_create_time` (`create_time`) USING BTREE ,
INDEX `idx_is_calculated` (`is_calculated`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='商品按托存储库存'
AUTO_INCREMENT=4

;

-- ----------------------------
-- Table structure for `biz_product_storage`
-- ----------------------------
DROP TABLE IF EXISTS `biz_product_storage`;
CREATE TABLE `biz_product_storage` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`wms_id`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`data_num`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`cur_day`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日期(当前日期)' ,
`cur_time`  datetime NULL DEFAULT NULL COMMENT '日期(当前日期)' ,
`warehouse_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库号' ,
`warehouse_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家Id' ,
`customer_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`product_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'SKU Id' ,
`product_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'SKU名称' ,
`stock_place_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库存类型编码' ,
`stock_place`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库存类型' ,
`batch_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次编码' ,
`aqty`  double NULL DEFAULT NULL COMMENT '数量' ,
`product_date`  timestamp NULL DEFAULT NULL COMMENT '生产日期' ,
`expiry_date`  timestamp NULL DEFAULT NULL COMMENT '到期日期' ,
`in_time`  timestamp NULL DEFAULT NULL COMMENT '入库时间' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编码' ,
`dbname`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属哪个仓库的数据库' ,
`temperature`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`weight`  double NULL DEFAULT NULL ,
`volume`  double NULL DEFAULT NULL ,
`pallet_num`  double NULL DEFAULT NULL ,
`piece_num`  double NULL DEFAULT NULL ,
`is_calculated`  varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他' ,
`calculate_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '费用计算时间' ,
PRIMARY KEY (`id`),
INDEX `idx_fees_no` (`fees_no`) USING BTREE ,
INDEX `idx_is_calculated` (`is_calculated`) USING BTREE ,
INDEX `idx_create_time` (`create_time`) USING BTREE ,
INDEX `idx_union_cond` (`warehouse_code`, `customerid`, `create_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='商品库存'
AUTO_INCREMENT=365425

;

-- ----------------------------
-- Table structure for `etl_condition`
-- ----------------------------
DROP TABLE IF EXISTS `etl_condition`;
CREATE TABLE `etl_condition` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`pull_type`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务数据类型' ,
`last_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次执行时间' ,
`curr_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
`takes_time`  int(11) NULL DEFAULT NULL COMMENT '耗时' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=30

;

-- ----------------------------
-- Table structure for `fees_abnormal`
-- ----------------------------
DROP TABLE IF EXISTS `fees_abnormal`;
CREATE TABLE `fees_abnormal` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识，自增' ,
`fee_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '费用编号' ,
`reference`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部订单号' ,
`expressnum`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`order_no`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部订单号' ,
`reason_id`  smallint(6) NULL DEFAULT NULL COMMENT '客诉原因ID' ,
`reason`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客诉原因' ,
`reason_detail_id`  smallint(6) NULL DEFAULT NULL COMMENT '客诉明细ID' ,
`reason_detail`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客诉原因详细' ,
`ispay`  tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '是否赔付 0-不赔付 1-赔付' ,
`pay_money`  decimal(8,2) NULL DEFAULT 0.00 COMMENT '赔付金额' ,
`is_conflict`  tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '是否争议' ,
`customer_id`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称Id' ,
`customer_name`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`carrier_id`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '承运商名称Id' ,
`carrier_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '承运商名称' ,
`deliver_id`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '宅配商Id' ,
`deliver_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '宅配商名称' ,
`warehouse_id`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '仓库Id' ,
`warehouse_name`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '仓库名称' ,
`create_person`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人ID' ,
`create_person_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' ,
`remark`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`bill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单编号' ,
`is_calculated`  char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否已计算费用 0-未结算 1-已结算 2-结算异常' ,
`operate_time`  timestamp NULL DEFAULT NULL COMMENT '操作时间（待删除）' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '客诉创建时间' ,
`write_time`  timestamp NULL DEFAULT NULL COMMENT '写入BMS时间' ,
PRIMARY KEY (`id`),
INDEX `idx_fees_no` (`fee_no`) USING BTREE ,
INDEX `idx_order_no` (`order_no`) USING BTREE ,
INDEX `idx_union_cond` (`create_time`, `reason_id`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=9598

;

-- ----------------------------
-- Table structure for `fees_bill`
-- ----------------------------
DROP TABLE IF EXISTS `fees_bill`;
CREATE TABLE `fees_bill` (
`billno`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账单编号' ,
`billname`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单名称' ,
`billstarttime`  timestamp NULL DEFAULT NULL COMMENT '账单起始时间' ,
`billendtime`  timestamp NULL DEFAULT NULL COMMENT '账单结束时间' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家id' ,
`customer_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`totleprice`  double NULL DEFAULT NULL COMMENT '总金额' ,
`receipt_amount`  double NULL DEFAULT 0 COMMENT '实收金额' ,
`billstatus`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0-未结算 1-已结算' ,
`remark`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`delflag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0-未作废 1-作废' ,
`creperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人ID' ,
`crepersonname`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名' ,
`cretime`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`modperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人ID' ,
`modpersonname`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人姓名' ,
`modtime`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
PRIMARY KEY (`billno`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='应收账单'

;

-- ----------------------------
-- Table structure for `fees_complaints_reason`
-- ----------------------------
DROP TABLE IF EXISTS `fees_complaints_reason`;
CREATE TABLE `fees_complaints_reason` (
`id`  int(11) NOT NULL COMMENT '客诉ID 自增' ,
`parentid`  int(11) NULL DEFAULT NULL COMMENT '父ID' ,
`reason`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客诉原因' ,
`create_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' ,
`create_person`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' ,
PRIMARY KEY (`id`),
INDEX `idx_parentid` (`parentid`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `fees_out_bill`
-- ----------------------------
DROP TABLE IF EXISTS `fees_out_bill`;
CREATE TABLE `fees_out_bill` (
`bill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账单编号' ,
`bill_name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单名称' ,
`start_time`  timestamp NULL DEFAULT NULL COMMENT '账单起始时间' ,
`end_time`  timestamp NULL DEFAULT NULL COMMENT '账单结束时间' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家id' ,
`totleprice`  double NULL DEFAULT NULL COMMENT '总金额' ,
`status`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 0-未结算 1-已结算' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志 0-未作废 1-作废' ,
`creator_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人ID' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人ID' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人姓名' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
PRIMARY KEY (`bill_no`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='应付账单'

;

-- ----------------------------
-- Table structure for `fees_pay_dispatch`
-- ----------------------------
DROP TABLE IF EXISTS `fees_pay_dispatch`;
CREATE TABLE `fees_pay_dispatch` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`fees_no`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编号' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`outstock_no`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库单号' ,
`external_no`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部单号' ,
`warehouse_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库ID' ,
`warehouse_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家ID' ,
`customer_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`deliveryid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商 纯配送业务' ,
`carrierid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流商(九曳/顺丰/...) ' ,
`unpacking`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拆箱标识 0-未拆箱 1-已拆箱' ,
`unpack_num`  tinyint(4) NULL DEFAULT 0 COMMENT '拆箱数量' ,
`operate_time`  timestamp NULL DEFAULT NULL COMMENT '操作时间' ,
`temperature_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`bill_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据类型' ,
`b2b_flag`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'B2B标识' ,
`total_weight`  double NULL DEFAULT NULL COMMENT '总重量' ,
`total_quantity`  double NULL DEFAULT NULL COMMENT '总数量' ,
`total_varieties`  double NULL DEFAULT NULL COMMENT '总品种数' ,
`split_single`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否撤单' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板编号' ,
`price_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`amount`  double NULL DEFAULT NULL COMMENT '金额' ,
`bill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单编号' ,
`rule_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则编号' ,
`to_province_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的省' ,
`to_city_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的市' ,
`to_district_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的区县' ,
`charged_weight`  double NULL DEFAULT NULL COMMENT '计费重量' ,
`weight_limit`  double NULL DEFAULT NULL COMMENT '重量界限' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`head_weight`  double NULL DEFAULT NULL COMMENT '首重重量' ,
`continued_weight`  double NULL DEFAULT NULL COMMENT '续重重量' ,
`head_price`  double NULL DEFAULT NULL COMMENT '首重价格' ,
`continued_price`  double NULL DEFAULT NULL COMMENT '续重价格' ,
`accept_time`  timestamp NULL DEFAULT NULL COMMENT '揽收时间' ,
`sign_time`  timestamp NULL DEFAULT NULL COMMENT '签收时间' ,
`param1`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数1' ,
`param2`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数2' ,
`param3`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数3' ,
`param4`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数4' ,
`param5`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数5' ,
`status`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='应付费用-配送费'
AUTO_INCREMENT=77

;

-- ----------------------------
-- Table structure for `fees_pay_transport`
-- ----------------------------
DROP TABLE IF EXISTS `fees_pay_transport`;
CREATE TABLE `fees_pay_transport` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '费用编号' ,
`orderno`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`linename`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '线路名称' ,
`subject_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`other_subject_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`cost_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用类型 INPUT-应收 OUTPUT-应付' ,
`weight`  double NULL DEFAULT NULL COMMENT '重量' ,
`volume`  double NULL DEFAULT NULL COMMENT '体积' ,
`temperaturetype`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`warehouse_code`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库Id' ,
`warehouse_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`carrier_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家Id' ,
`carrier_name`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`originatingcity`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发市' ,
`originatingdistrict`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发区' ,
`targetwarehouse`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的仓' ,
`targetcity`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的市' ,
`targetdistrict`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的区' ,
`category`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品类' ,
`kilometers`  double NULL DEFAULT NULL COMMENT '公里数' ,
`spendtime`  double NULL DEFAULT NULL COMMENT '花费时间' ,
`operationtime`  timestamp NULL DEFAULT NULL COMMENT '操作时间' ,
`carmodel`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型' ,
`templatenum`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板编号' ,
`islight`  tinyint(4) NULL DEFAULT NULL COMMENT '是否轻货 0-不是 1-是' ,
`quantity`  int(11) NULL DEFAULT NULL COMMENT '数量' ,
`bubble_weight`  double NULL DEFAULT NULL ,
`unitprice`  double NULL DEFAULT NULL COMMENT '单价' ,
`totleprice`  double NULL DEFAULT NULL COMMENT '金额' ,
`accepttime`  timestamp NULL DEFAULT NULL COMMENT '揽收时间' ,
`signtime`  timestamp NULL DEFAULT NULL COMMENT '签收时间' ,
`billno`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单编号' ,
`ruleno`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则编号' ,
`state`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态' ,
`creperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人ID' ,
`crepersonname`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名' ,
`cretime`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`modperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人ID' ,
`modpersonname`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人姓名' ,
`modtime`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`status`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态(0 未过账 1已过账)' ,
`extarr1`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段1' ,
`extarr2`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段2' ,
`extarr3`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段3' ,
`extarr4`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段4' ,
`extarr5`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段5' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=434

;

-- ----------------------------
-- Table structure for `fees_receive_dispatch`
-- ----------------------------
DROP TABLE IF EXISTS `fees_receive_dispatch`;
CREATE TABLE `fees_receive_dispatch` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`fees_no`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用编号' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`outstock_no`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库单号' ,
`external_no`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部单号' ,
`warehouse_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库ID' ,
`warehouse_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库' ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家ID' ,
`customer_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`carrierid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流商id' ,
`carrier_name`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流商名称(九曳/顺丰/...) ' ,
`deliveryid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商id' ,
`deliver_name`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宅配商名称' ,
`unpacking`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拆箱标识 0-未拆箱 1-已拆箱' ,
`unpack_num`  tinyint(4) NULL DEFAULT 0 COMMENT '拆箱数量' ,
`delivery_date`  timestamp NULL DEFAULT NULL COMMENT '操作时间' ,
`temperature_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`bill_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据类型' ,
`b2b_flag`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'B2B标识' ,
`total_weight`  double NULL DEFAULT NULL COMMENT '总重量' ,
`total_quantity`  double NULL DEFAULT NULL COMMENT '总数量' ,
`total_varieties`  double NULL DEFAULT NULL COMMENT '总品种数' ,
`split_single`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否撤单' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板编号' ,
`price_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`amount`  double NULL DEFAULT NULL COMMENT '金额' ,
`bill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单编号' ,
`rule_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则编号' ,
`to_province_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的省' ,
`to_district_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的区县' ,
`to_city_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的市' ,
`charged_weight`  double NULL DEFAULT NULL COMMENT '计费重量' ,
`weight_limit`  double NULL DEFAULT NULL COMMENT '重量界限' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`head_weight`  double NULL DEFAULT NULL COMMENT '首重重量' ,
`continued_weight`  double NULL DEFAULT NULL COMMENT '续重重量' ,
`head_price`  double NULL DEFAULT NULL COMMENT '首重价格' ,
`continued_price`  double NULL DEFAULT NULL COMMENT '续重价格' ,
`accept_time`  timestamp NULL DEFAULT NULL COMMENT '揽收时间' ,
`sign_time`  timestamp NULL DEFAULT NULL COMMENT '签收时间' ,
`param1`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数1' ,
`param2`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数2' ,
`param3`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数3' ,
`param4`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数4' ,
`param5`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数5' ,
`status`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`),
INDEX `idx_fees_no` (`fees_no`) USING BTREE ,
INDEX `idx_waybill_no` (`waybill_no`) USING BTREE ,
INDEX `idx_union_cond` (`warehouse_code`, `customerid`, `create_time`) USING BTREE ,
INDEX `idx_create_time` (`create_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='应收费用-配送费'
AUTO_INCREMENT=18169

;

-- ----------------------------
-- Table structure for `fees_receive_storage`
-- ----------------------------
DROP TABLE IF EXISTS `fees_receive_storage`;
CREATE TABLE `fees_receive_storage` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`fees_no`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '费用编号' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`customer_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家id' ,
`customer_name`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`warehouse_code`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库编号' ,
`warehouse_name`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称' ,
`order_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据类型' ,
`order_no`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据编号 (OMS orderno)' ,
`product_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品类别' ,
`cost_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值' ,
`subject_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`other_subject_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓储增值费编号' ,
`tempreture_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`product_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编号' ,
`product_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称' ,
`external_product_no`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部商品编号' ,
`quantity`  int(11) NULL DEFAULT NULL COMMENT '数量' ,
`unit`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位' ,
`weight`  double(9,4) NULL DEFAULT NULL COMMENT '重量' ,
`volume`  double(9,4) NULL DEFAULT NULL COMMENT '体积' ,
`varieties`  int(11) NULL DEFAULT NULL COMMENT '品种数' ,
`unit_price`  double(9,4) NULL DEFAULT NULL COMMENT '单价' ,
`continued_price`  double(9,4) NULL DEFAULT NULL COMMENT '续件价格' ,
`cost`  decimal(16,6) NULL DEFAULT NULL COMMENT '金额' ,
`param1`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`param2`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`param3`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`param4`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`param6`  varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`rule_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则编号' ,
`bill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单编号' ,
`status`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态(0 未过账 1已过账)' ,
`operate_time`  timestamp NULL DEFAULT NULL ,
`biz_id`  bigint(20) NULL DEFAULT NULL COMMENT '业务数据主键' ,
PRIMARY KEY (`id`),
INDEX `idx_fees_no` (`fees_no`) USING BTREE ,
INDEX `idx_create_time` (`create_time`) USING BTREE ,
INDEX `idx_union_cond` (`customer_id`, `warehouse_code`, `subject_code`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='仓储应收费用'
AUTO_INCREMENT=3017

;

-- ----------------------------
-- Table structure for `fees_receive_transport`
-- ----------------------------
DROP TABLE IF EXISTS `fees_receive_transport`;
CREATE TABLE `fees_receive_transport` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`fees_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '费用编号' ,
`orderno`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号' ,
`waybill_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运单号' ,
`linename`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '线路名称' ,
`subject_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`other_subject_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运输增值费编号' ,
`cost_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用类型 INPUT-应收 OUTPUT-应付' ,
`weight`  double NULL DEFAULT NULL COMMENT '重量' ,
`volume`  double NULL DEFAULT NULL COMMENT '体积' ,
`temperaturetype`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`warehouse_code`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库Id' ,
`warehouse_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家Id' ,
`customer_name`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`originatingcity`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发市' ,
`originatingdistrict`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发区' ,
`targetwarehouse`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的仓' ,
`targetcity`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的市' ,
`targetdistrict`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的区' ,
`category`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品类' ,
`kilometers`  double NULL DEFAULT NULL COMMENT '公里数' ,
`spendtime`  double NULL DEFAULT NULL COMMENT '花费时间' ,
`operationtime`  timestamp NULL DEFAULT NULL COMMENT '操作时间' ,
`carmodel`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型' ,
`templatenum`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板编号' ,
`islight`  tinyint(4) NULL DEFAULT NULL COMMENT '是否轻货 0-不是 1-是' ,
`quantity`  int(11) NULL DEFAULT NULL COMMENT '数量' ,
`unitprice`  double NULL DEFAULT NULL COMMENT '单价' ,
`totleprice`  double NULL DEFAULT NULL COMMENT '金额' ,
`accepttime`  timestamp NULL DEFAULT NULL COMMENT '揽收时间' ,
`signtime`  timestamp NULL DEFAULT NULL COMMENT '签收时间' ,
`billno`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单编号' ,
`ruleno`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则编号' ,
`state`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态' ,
`creperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人ID' ,
`crepersonname`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名' ,
`cretime`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`modperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人ID' ,
`modpersonname`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人姓名' ,
`modtime`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`status`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态(0 未过账 1已过账)' ,
`extarr1`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段1' ,
`extarr2`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段2' ,
`extarr3`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段3' ,
`extarr4`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段4' ,
`extarr5`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段5' ,
PRIMARY KEY (`id`),
INDEX `idx_fees_no` (`fees_no`) USING BTREE ,
INDEX `idx_union_cond` (`subject_code`, `warehouse_code`, `customerid`) USING BTREE ,
INDEX `idx_cretime` (`cretime`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=9223372036854775807

;

-- ----------------------------
-- Table structure for `price_airport_bl_line`
-- ----------------------------
DROP TABLE IF EXISTS `price_airport_bl_line`;
CREATE TABLE `price_airport_bl_line` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`quotation_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`city_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市编号' ,
`start_address_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发地' ,
`end_address_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的地' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='机场提货Lading(BL)路线'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `price_carmodel_quotation`
-- ----------------------------
DROP TABLE IF EXISTS `price_carmodel_quotation`;
CREATE TABLE `price_carmodel_quotation` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`temperature_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`car_model`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`reverse_price`  double NULL DEFAULT NULL COMMENT '逆向单价' ,
`quotation_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='车型报价 '
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `price_consumer_material_template`
-- ----------------------------
DROP TABLE IF EXISTS `price_consumer_material_template`;
CREATE TABLE `price_consumer_material_template` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`consumer_material_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`consumer_material_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '重量下限' ,
`spec_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '重量上限' ,
`outside_diameter`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数量下限' ,
`inner_diameter`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数量上限' ,
`wall_thickness`  double NULL DEFAULT NULL COMMENT 'SKU下限' ,
`warehouse_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'SKU上限' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='耗材报价'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `price_contract_info`
-- ----------------------------
DROP TABLE IF EXISTS `price_contract_info`;
CREATE TABLE `price_contract_info` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
`contract_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同编号' ,
`contract_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同类型(供应商合同/客户合同)' ,
`contract_state`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '合同审核状态' ,
`customerId`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家Id' ,
`customerName`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' ,
`contract_obj`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同对象' ,
`paper_contract_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '纸质合同编号' ,
`start_date`  timestamp NULL DEFAULT NULL COMMENT '生效日期' ,
`expire_date`  timestamp NULL DEFAULT NULL COMMENT '失效日期' ,
`description`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同描述' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='合同信息'
AUTO_INCREMENT=104

;

-- ----------------------------
-- Table structure for `price_contract_item`
-- ----------------------------
DROP TABLE IF EXISTS `price_contract_item`;
CREATE TABLE `price_contract_item` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
`contract_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同编号' ,
`biz_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型编码(仓储，运输，配送）' ,
`subject_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计费模板' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='合同计费项'
AUTO_INCREMENT=741

;

-- ----------------------------
-- Table structure for `price_dispatch_detail`
-- ----------------------------
DROP TABLE IF EXISTS `price_dispatch_detail`;
CREATE TABLE `price_dispatch_detail` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`start_warehouse_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发仓ID' ,
`province_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份ID' ,
`city_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市ID' ,
`area_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '县/区ID' ,
`area_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地域类型' ,
`timeliness`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时效' ,
`weight_limit`  double NULL DEFAULT NULL COMMENT '重量界限' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`first_weight`  double NULL DEFAULT NULL COMMENT '首重重量' ,
`first_weight_price`  double NULL DEFAULT NULL COMMENT '首重价格' ,
`continued_weight`  double NULL DEFAULT NULL COMMENT '续重重量' ,
`continued_price`  double NULL DEFAULT NULL COMMENT '续重价格' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='配送报价表'
AUTO_INCREMENT=4118

;

-- ----------------------------
-- Table structure for `price_dispatch_template`
-- ----------------------------
DROP TABLE IF EXISTS `price_dispatch_template`;
CREATE TABLE `price_dispatch_template` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`template_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`deliver`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配送商' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='报价模版'
AUTO_INCREMENT=121

;

-- ----------------------------
-- Table structure for `price_ecommerce_line`
-- ----------------------------
DROP TABLE IF EXISTS `price_ecommerce_line`;
CREATE TABLE `price_ecommerce_line` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`quotation_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`ecommerce_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电商名称' ,
`target_city_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的城市' ,
`end_warehouse_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的仓编号' ,
`start_warehouse_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发仓' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='电商仓送货路线表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `price_fee_subject`
-- ----------------------------
DROP TABLE IF EXISTS `price_fee_subject`;
CREATE TABLE `price_fee_subject` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
`biz_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型:仓储/运输/配送' ,
`subject_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目编号' ,
`subject_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目名称' ,
`price_table`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价表' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='费用科目'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `price_general_quotation`
-- ----------------------------
DROP TABLE IF EXISTS `price_general_quotation`;
CREATE TABLE `price_general_quotation` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`quotation_no`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`biz_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型' ,
`subject_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`fee_unit_code`  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计费单位' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`description`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计费说明' ,
`price_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '计费说明' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`, `price_type`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=104

;

-- ----------------------------
-- Table structure for `price_general_template`
-- ----------------------------
DROP TABLE IF EXISTS `price_general_template`;
CREATE TABLE `price_general_template` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`template_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`biz_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型' ,
`subject_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`bill_way_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计费方式' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`storage_template_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓储模板类型' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='报价模版'
AUTO_INCREMENT=112

;

-- ----------------------------
-- Table structure for `price_instock_quotation_delete`
-- ----------------------------
DROP TABLE IF EXISTS `price_instock_quotation_delete`;
CREATE TABLE `price_instock_quotation_delete` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`quotation_no`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`subject_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`fee_unit_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计费单位' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='入仓费报价'
AUTO_INCREMENT=8

;

-- ----------------------------
-- Table structure for `price_order_operation_template_delete`
-- ----------------------------
DROP TABLE IF EXISTS `price_order_operation_template_delete`;
CREATE TABLE `price_order_operation_template_delete` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`bill_way_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计费方式' ,
`temperature_type_code`  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`weight_lower_limit`  double NULL DEFAULT NULL COMMENT '重量下限' ,
`weight_upper_limit`  double NULL DEFAULT NULL COMMENT '重量上限' ,
`num_lower_limit`  double NULL DEFAULT NULL COMMENT '数量下限' ,
`num_upper_limit`  double NULL DEFAULT NULL COMMENT '数量上限' ,
`variety_lower_limit`  double NULL DEFAULT NULL COMMENT '品种下限' ,
`variety_upper_limit`  double NULL DEFAULT NULL COMMENT '品种上限' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`continuation_price`  double NULL DEFAULT NULL COMMENT '续件价格' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='订单操作费阶梯报价'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `price_out_dispatch_detail`
-- ----------------------------
DROP TABLE IF EXISTS `price_out_dispatch_detail`;
CREATE TABLE `price_out_dispatch_detail` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`start_warehouse_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发仓ID' ,
`province_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份ID' ,
`city_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市ID' ,
`area_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '县/区ID' ,
`area_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地域类型' ,
`timeliness`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时效' ,
`weight_limit`  double NULL DEFAULT NULL COMMENT '基础重量' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`first_weight`  double NULL DEFAULT NULL COMMENT '首重重量' ,
`first_weight_price`  double NULL DEFAULT NULL COMMENT '首重价格' ,
`continued_weight`  double NULL DEFAULT NULL COMMENT '续重重量' ,
`continued_price`  double NULL DEFAULT NULL COMMENT '续重价格' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='配送报价表'
AUTO_INCREMENT=338

;

-- ----------------------------
-- Table structure for `price_out_dispatch_template`
-- ----------------------------
DROP TABLE IF EXISTS `price_out_dispatch_template`;
CREATE TABLE `price_out_dispatch_template` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`template_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`deliver`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配送商' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='报价模版'
AUTO_INCREMENT=93

;

-- ----------------------------
-- Table structure for `price_out_transport_line`
-- ----------------------------
DROP TABLE IF EXISTS `price_out_transport_line`;
CREATE TABLE `price_out_transport_line` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`transport_line_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`from_warehouse_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发仓' ,
`from_province_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`from_city_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发城市编号' ,
`from_district_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发地区' ,
`from_address`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发地址' ,
`end_warehouse_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的仓' ,
`to_province_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`to_city_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的城市' ,
`to_district_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的地区' ,
`to_address`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的地址' ,
`send_cycle`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发车周期' ,
`timeliness`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时效' ,
`remark`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应模版' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='报价-运输路线'
AUTO_INCREMENT=227

;

-- ----------------------------
-- Table structure for `price_out_transport_line_range`
-- ----------------------------
DROP TABLE IF EXISTS `price_out_transport_line_range`;
CREATE TABLE `price_out_transport_line_range` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '运输梯度报价ID' ,
`temperature_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`product_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品类' ,
`min_weight_shipment`  double NULL DEFAULT 0 COMMENT '最低起运（kg）' ,
`weight_lower_limit`  double NULL DEFAULT 0 COMMENT '重量下限' ,
`weight_upper_limit`  double NULL DEFAULT 0 COMMENT '重量上限' ,
`num_lower_limit`  double NULL DEFAULT 0 COMMENT '数量下限' ,
`num_upper_limit`  double NULL DEFAULT 0 COMMENT '数量上限' ,
`sku_lower_limit`  double NULL DEFAULT 0 COMMENT 'SKU下限' ,
`sku_upper_limit`  double NULL DEFAULT 0 COMMENT 'SKU上限' ,
`volume_lower_limit`  double NULL DEFAULT 0 COMMENT '体积下限' ,
`volume_upper_limit`  double NULL DEFAULT 0 COMMENT '体积上限' ,
`min_distance`  double NULL DEFAULT 0 COMMENT '距离下限' ,
`max_distance`  double NULL DEFAULT 0 COMMENT '距离上限' ,
`min_time`  double NULL DEFAULT 0 COMMENT '时间下限' ,
`max_time`  double NULL DEFAULT 0 COMMENT '时间上限' ,
`car_model_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型' ,
`bubble_weight`  double NULL DEFAULT NULL ,
`unit_price`  double NULL DEFAULT 0 COMMENT '单价' ,
`extra1`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra2`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra3`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra4`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra5`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`line_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应报价' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='报价-运输梯度报价'
AUTO_INCREMENT=240

;

-- ----------------------------
-- Table structure for `price_out_transport_template`
-- ----------------------------
DROP TABLE IF EXISTS `price_out_transport_template`;
CREATE TABLE `price_out_transport_template` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`template_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`transport_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型' ,
`transport_form_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`template_type_code`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='运输报价模版'
AUTO_INCREMENT=76

;

-- ----------------------------
-- Table structure for `price_out_transport_valueadded`
-- ----------------------------
DROP TABLE IF EXISTS `price_out_transport_valueadded`;
CREATE TABLE `price_out_transport_valueadded` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`quotation_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`car_model_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型' ,
`weight_limit`  double NULL DEFAULT NULL COMMENT '重量界限' ,
`fee_unit_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计费单位' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`subject_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`extra1`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra2`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra3`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra4`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra5`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NOT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='报价-运输增值报价'
AUTO_INCREMENT=12

;

-- ----------------------------
-- Table structure for `price_storage_extra`
-- ----------------------------
DROP TABLE IF EXISTS `price_storage_extra`;
CREATE TABLE `price_storage_extra` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`subject_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`fee_unit_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计费方式' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='仓储增值费报价'
AUTO_INCREMENT=62

;

-- ----------------------------
-- Table structure for `price_storage_material`
-- ----------------------------
DROP TABLE IF EXISTS `price_storage_material`;
CREATE TABLE `price_storage_material` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`material_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`material_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '重量下限' ,
`spec_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '重量上限' ,
`outside_diameter`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数量下限' ,
`inner_diameter`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数量上限' ,
`wall_thickness`  double NULL DEFAULT NULL COMMENT 'SKU下限' ,
`warehouse_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'SKU上限' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='耗材报价'
AUTO_INCREMENT=82

;

-- ----------------------------
-- Table structure for `price_storage_step`
-- ----------------------------
DROP TABLE IF EXISTS `price_storage_step`;
CREATE TABLE `price_storage_step` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`quotation_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`weight_lower`  double NULL DEFAULT NULL COMMENT '重量下限' ,
`weight_upper`  double NULL DEFAULT NULL COMMENT '重量上限' ,
`temperature_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`continued_item`  double NULL DEFAULT NULL COMMENT '续件价格' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`num_upper`  double NULL DEFAULT NULL ,
`num_lower`  double NULL DEFAULT NULL ,
`sku_upper`  double NULL DEFAULT NULL ,
`sku_lower`  double NULL DEFAULT NULL ,
`volume_upper`  double NULL DEFAULT NULL ,
`volume_lower`  char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`user_define1`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`user_define2`  char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`user_define3`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`user_define4`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`user_define5`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='存储费阶梯报价'
AUTO_INCREMENT=470

;

-- ----------------------------
-- Table structure for `price_storage_template`
-- ----------------------------
DROP TABLE IF EXISTS `price_storage_template`;
CREATE TABLE `price_storage_template` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版Id' ,
`weight_lower_limit`  double NULL DEFAULT NULL COMMENT '重量下限' ,
`weight_upper_limit`  double NULL DEFAULT NULL COMMENT '重量上限' ,
`temperature_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='存储费重量阶梯报价'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `price_transport_line`
-- ----------------------------
DROP TABLE IF EXISTS `price_transport_line`;
CREATE TABLE `price_transport_line` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`transport_line_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`from_warehouse_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发仓' ,
`from_province_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`from_city_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发城市编号' ,
`from_district_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发地区' ,
`from_address`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '始发地址' ,
`end_warehouse_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的仓' ,
`to_province_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`to_city_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的城市' ,
`to_district_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的地区' ,
`to_address`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的地址' ,
`send_cycle`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发车周期' ,
`timeliness`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时效' ,
`remark`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应模版' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `index_line_id` (`id`) USING BTREE ,
INDEX `index_line_from_province_id` (`from_province_id`) USING BTREE ,
INDEX `index_line_from_city_id` (`from_city_id`) USING BTREE ,
INDEX `index_line_from_district_id` (`from_district_id`) USING BTREE ,
INDEX `index_line_to_province_id` (`to_province_id`) USING BTREE ,
INDEX `index_line_to_city_id` (`to_city_id`) USING BTREE ,
INDEX `index_line_to_district_id` (`to_district_id`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='报价-运输路线'
AUTO_INCREMENT=3324

;

-- ----------------------------
-- Table structure for `price_transport_line_range`
-- ----------------------------
DROP TABLE IF EXISTS `price_transport_line_range`;
CREATE TABLE `price_transport_line_range` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '运输梯度报价ID' ,
`temperature_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度类型' ,
`product_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品类' ,
`min_weight_shipment`  double NULL DEFAULT 0 COMMENT '最低起运（kg）' ,
`weight_lower_limit`  double NULL DEFAULT 0 COMMENT '重量下限' ,
`weight_upper_limit`  double NULL DEFAULT 0 COMMENT '重量上限' ,
`num_lower_limit`  double NULL DEFAULT 0 COMMENT '数量下限' ,
`num_upper_limit`  double NULL DEFAULT 0 COMMENT '数量上限' ,
`sku_lower_limit`  double NULL DEFAULT 0 COMMENT 'SKU下限' ,
`sku_upper_limit`  double NULL DEFAULT 0 COMMENT 'SKU上限' ,
`volume_lower_limit`  double NULL DEFAULT 0 COMMENT '体积下限' ,
`volume_upper_limit`  double NULL DEFAULT 0 COMMENT '体积上限' ,
`min_distance`  double NULL DEFAULT 0 COMMENT '距离下限' ,
`max_distance`  double NULL DEFAULT 0 COMMENT '距离上限' ,
`min_time`  double NULL DEFAULT 0 COMMENT '时间下限' ,
`max_time`  double NULL DEFAULT 0 COMMENT '时间上限' ,
`car_model_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型' ,
`unit_price`  double NULL DEFAULT 0 COMMENT '单价' ,
`extra1`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra2`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra3`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra4`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra5`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`line_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应报价' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='报价-运输梯度报价'
AUTO_INCREMENT=419

;

-- ----------------------------
-- Table structure for `price_transport_template`
-- ----------------------------
DROP TABLE IF EXISTS `price_transport_template`;
CREATE TABLE `price_transport_template` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`template_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`template_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`transport_type_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型' ,
`transport_form_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
`template_type_code`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='运输报价模版'
AUTO_INCREMENT=87

;

-- ----------------------------
-- Table structure for `price_transport_valueadded`
-- ----------------------------
DROP TABLE IF EXISTS `price_transport_valueadded`;
CREATE TABLE `price_transport_valueadded` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`quotation_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报价编号' ,
`car_model_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型' ,
`weight_limit`  double NULL DEFAULT NULL COMMENT '重量界限' ,
`fee_unit_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计费单位' ,
`unit_price`  double NULL DEFAULT NULL COMMENT '单价' ,
`subject_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用科目' ,
`template_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模版编号' ,
`extra1`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra2`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra3`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra4`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`extra5`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者' ,
`create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者' ,
`last_modify_time`  timestamp NOT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='报价-运输增值报价'
AUTO_INCREMENT=164

;

-- ----------------------------
-- Table structure for `pub_address`
-- ----------------------------
DROP TABLE IF EXISTS `pub_address`;
CREATE TABLE `pub_address` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
`area_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域编号' ,
`area_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域名称' ,
`level_num`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属层级' ,
`parent_area_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级区域编号' ,
`region_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '大区编号' ,
`creator_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者编号' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者编号' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `idx_address_id` (`id`) USING BTREE ,
INDEX `idx_address_area_code` (`area_code`) USING BTREE ,
INDEX `idx_address_area_name` (`area_name`) USING BTREE ,
INDEX `idx_address_parent_area_code` (`parent_area_code`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='地址库(省市区)'
AUTO_INCREMENT=3734

;

-- ----------------------------
-- Table structure for `pub_elec_warehouse`
-- ----------------------------
DROP TABLE IF EXISTS `pub_elec_warehouse`;
CREATE TABLE `pub_elec_warehouse` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,
`warehouse_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库编号' ,
`warehouse_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓库名称' ,
`elec_biz_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电商编号' ,
`elec_biz_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电商名称' ,
`province_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份编号' ,
`province_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份名称' ,
`city_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市编号' ,
`city_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市名称' ,
`district_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区编号' ,
`district_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区名称' ,
`detail_address`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址' ,
`remark`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`creator_code`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者编号' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modifier`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改者编号' ,
`last_modify_time`  timestamp NULL DEFAULT NULL COMMENT '修改时间' ,
`del_flag`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='电商仓库'
AUTO_INCREMENT=23

;

-- ----------------------------
-- Table structure for `pub_sequence`
-- ----------------------------
DROP TABLE IF EXISTS `pub_sequence`;
CREATE TABLE `pub_sequence` (
`idname`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' ,
`seqnum`  bigint(20) NOT NULL DEFAULT 0 ,
PRIMARY KEY (`idname`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `sys_datagroup`
-- ----------------------------
DROP TABLE IF EXISTS `sys_datagroup`;
CREATE TABLE `sys_datagroup` (
`id`  char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`dgroupid`  char(15) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`dgroupname`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`remark`  varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`creperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`crepersonid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`cretime`  datetime NULL DEFAULT NULL ,
`modperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`modpersonid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`modtime`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
INDEX `data_powergroupid_idx` (`dgroupid`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `sys_datagroup_customer`
-- ----------------------------
DROP TABLE IF EXISTS `sys_datagroup_customer`;
CREATE TABLE `sys_datagroup_customer` (
`dgroupid`  char(6) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`customerid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`creperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`crepersonid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`cretime`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`dgroupid`, `customerid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `sys_datagroup_warehouse`
-- ----------------------------
DROP TABLE IF EXISTS `sys_datagroup_warehouse`;
CREATE TABLE `sys_datagroup_warehouse` (
`dgroupid`  char(6) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`warehouseid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`creperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`crepersonid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`cretime`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`dgroupid`, `warehouseid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `sys_sequencenumber`
-- ----------------------------
DROP TABLE IF EXISTS `sys_sequencenumber`;
CREATE TABLE `sys_sequencenumber` (
`id`  bigint(20) NOT NULL ,
`variablename`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`digit`  tinyint(4) NOT NULL ,
`prefix`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`suffix`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`value`  decimal(20,0) NOT NULL ,
`blocksize`  tinyint(4) NOT NULL ,
`startvalue`  decimal(20,0) NOT NULL ,
`fixlenshowflag`  tinyint(4) NOT NULL ,
`status`  tinyint(4) NOT NULL ,
`dateflag`  int(11) NOT NULL ,
`cretime`  datetime NOT NULL ,
`lastmodtime`  datetime NOT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `sys_user_limitgroup`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_limitgroup`;
CREATE TABLE `sys_user_limitgroup` (
`id`  char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`userid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`groupid`  char(6) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`grouptype`  varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`creperson`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`crepersonid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`cretime`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
INDEX `userpower_idx_userid` (`userid`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin

;

-- ----------------------------
-- Table structure for `system_code`
-- ----------------------------
DROP TABLE IF EXISTS `system_code`;
CREATE TABLE `system_code` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`type_code`  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类型编码' ,
`code_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '名称' ,
`code`  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '编码' ,
`code_desc`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述' ,
`status`  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '状态' ,
`create_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人ID' ,
`create_dt`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`update_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人ID' ,
`update_dt`  datetime NULL DEFAULT NULL COMMENT '修改时间' ,
`sort_no`  bigint(20) NULL DEFAULT NULL COMMENT '序号' ,
`extattr1`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '扩展字段1' ,
`extattr2`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '扩展字段2' ,
`extattr3`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '扩展字段3' ,
`extattr4`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '扩展字段4' ,
`extattr5`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '扩展字段5' ,
`delete_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '删除人' ,
`delete_dt`  datetime NULL DEFAULT NULL COMMENT '删除时间' ,
PRIMARY KEY (`id`),
INDEX `AK_Key_1ms_code` (`type_code`, `code`) USING BTREE ,
INDEX `Index_1ms_code` (`type_code`, `code`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='枚举值'
AUTO_INCREMENT=779

;

-- ----------------------------
-- Table structure for `system_code_type`
-- ----------------------------
DROP TABLE IF EXISTS `system_code_type`;
CREATE TABLE `system_code_type` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`type_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类型名称' ,
`type_code`  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类型编码' ,
`type_status`  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '状态' ,
`type_desc`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述' ,
`create_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人ID' ,
`create_dt`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`update_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人ID' ,
`update_dt`  datetime NULL DEFAULT NULL COMMENT '修改时间' ,
`extattr1`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '扩展字段1' ,
`extattr2`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '扩展字段2' ,
`extattr3`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '扩展字段3' ,
`delete_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '删除人' ,
`delete_dt`  datetime NULL DEFAULT NULL COMMENT '删除时间' ,
PRIMARY KEY (`id`),
INDEX `Index_1ms_code_type` (`type_code`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='枚举值类型'
AUTO_INCREMENT=154

;

-- ----------------------------
-- Auto increment value for `bill_dispatch_distinct`
-- ----------------------------
ALTER TABLE `bill_dispatch_distinct` AUTO_INCREMENT=23862;

-- ----------------------------
-- Auto increment value for `bill_rule_pay`
-- ----------------------------
ALTER TABLE `bill_rule_pay` AUTO_INCREMENT=30;

-- ----------------------------
-- Auto increment value for `bill_rule_receive`
-- ----------------------------
ALTER TABLE `bill_rule_receive` AUTO_INCREMENT=30;

-- ----------------------------
-- Auto increment value for `biz_consumer_material_detail`
-- ----------------------------
ALTER TABLE `biz_consumer_material_detail` AUTO_INCREMENT=2;

-- ----------------------------
-- Auto increment value for `biz_dispatch_bill`
-- ----------------------------
ALTER TABLE `biz_dispatch_bill` AUTO_INCREMENT=392525;

-- ----------------------------
-- Auto increment value for `biz_dispatch_pay_bill`
-- ----------------------------
ALTER TABLE `biz_dispatch_pay_bill` AUTO_INCREMENT=330041;

-- ----------------------------
-- Auto increment value for `biz_ganxian_roadbill`
-- ----------------------------
ALTER TABLE `biz_ganxian_roadbill` AUTO_INCREMENT=335;

-- ----------------------------
-- Auto increment value for `biz_ganxian_waybill`
-- ----------------------------
ALTER TABLE `biz_ganxian_waybill` AUTO_INCREMENT=291;

-- ----------------------------
-- Auto increment value for `biz_instock_detail`
-- ----------------------------
ALTER TABLE `biz_instock_detail` AUTO_INCREMENT=8313;

-- ----------------------------
-- Auto increment value for `biz_instock_master`
-- ----------------------------
ALTER TABLE `biz_instock_master` AUTO_INCREMENT=2284;

-- ----------------------------
-- Auto increment value for `biz_outstock_detail`
-- ----------------------------
ALTER TABLE `biz_outstock_detail` AUTO_INCREMENT=796298;

-- ----------------------------
-- Auto increment value for `biz_outstock_master`
-- ----------------------------
ALTER TABLE `biz_outstock_master` AUTO_INCREMENT=393257;

-- ----------------------------
-- Auto increment value for `biz_outstock_packmaterial`
-- ----------------------------
ALTER TABLE `biz_outstock_packmaterial` AUTO_INCREMENT=604408;

-- ----------------------------
-- Auto increment value for `biz_pack_storage`
-- ----------------------------
ALTER TABLE `biz_pack_storage` AUTO_INCREMENT=707;

-- ----------------------------
-- Auto increment value for `biz_product_pallet_storage`
-- ----------------------------
ALTER TABLE `biz_product_pallet_storage` AUTO_INCREMENT=4;

-- ----------------------------
-- Auto increment value for `biz_product_storage`
-- ----------------------------
ALTER TABLE `biz_product_storage` AUTO_INCREMENT=365425;

-- ----------------------------
-- Auto increment value for `etl_condition`
-- ----------------------------
ALTER TABLE `etl_condition` AUTO_INCREMENT=30;

-- ----------------------------
-- Auto increment value for `fees_abnormal`
-- ----------------------------
ALTER TABLE `fees_abnormal` AUTO_INCREMENT=9598;

-- ----------------------------
-- Auto increment value for `fees_pay_dispatch`
-- ----------------------------
ALTER TABLE `fees_pay_dispatch` AUTO_INCREMENT=77;

-- ----------------------------
-- Auto increment value for `fees_pay_transport`
-- ----------------------------
ALTER TABLE `fees_pay_transport` AUTO_INCREMENT=434;

-- ----------------------------
-- Auto increment value for `fees_receive_dispatch`
-- ----------------------------
ALTER TABLE `fees_receive_dispatch` AUTO_INCREMENT=18169;

-- ----------------------------
-- Auto increment value for `fees_receive_storage`
-- ----------------------------
ALTER TABLE `fees_receive_storage` AUTO_INCREMENT=3017;

-- ----------------------------
-- Auto increment value for `price_airport_bl_line`
-- ----------------------------
ALTER TABLE `price_airport_bl_line` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `price_carmodel_quotation`
-- ----------------------------
ALTER TABLE `price_carmodel_quotation` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `price_consumer_material_template`
-- ----------------------------
ALTER TABLE `price_consumer_material_template` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `price_contract_info`
-- ----------------------------
ALTER TABLE `price_contract_info` AUTO_INCREMENT=104;

-- ----------------------------
-- Auto increment value for `price_contract_item`
-- ----------------------------
ALTER TABLE `price_contract_item` AUTO_INCREMENT=741;

-- ----------------------------
-- Auto increment value for `price_dispatch_detail`
-- ----------------------------
ALTER TABLE `price_dispatch_detail` AUTO_INCREMENT=4118;

-- ----------------------------
-- Auto increment value for `price_dispatch_template`
-- ----------------------------
ALTER TABLE `price_dispatch_template` AUTO_INCREMENT=121;

-- ----------------------------
-- Auto increment value for `price_ecommerce_line`
-- ----------------------------
ALTER TABLE `price_ecommerce_line` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `price_fee_subject`
-- ----------------------------
ALTER TABLE `price_fee_subject` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `price_general_quotation`
-- ----------------------------
ALTER TABLE `price_general_quotation` AUTO_INCREMENT=104;

-- ----------------------------
-- Auto increment value for `price_general_template`
-- ----------------------------
ALTER TABLE `price_general_template` AUTO_INCREMENT=112;

-- ----------------------------
-- Auto increment value for `price_instock_quotation_delete`
-- ----------------------------
ALTER TABLE `price_instock_quotation_delete` AUTO_INCREMENT=8;

-- ----------------------------
-- Auto increment value for `price_order_operation_template_delete`
-- ----------------------------
ALTER TABLE `price_order_operation_template_delete` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `price_out_dispatch_detail`
-- ----------------------------
ALTER TABLE `price_out_dispatch_detail` AUTO_INCREMENT=338;

-- ----------------------------
-- Auto increment value for `price_out_dispatch_template`
-- ----------------------------
ALTER TABLE `price_out_dispatch_template` AUTO_INCREMENT=93;

-- ----------------------------
-- Auto increment value for `price_out_transport_line`
-- ----------------------------
ALTER TABLE `price_out_transport_line` AUTO_INCREMENT=227;

-- ----------------------------
-- Auto increment value for `price_out_transport_line_range`
-- ----------------------------
ALTER TABLE `price_out_transport_line_range` AUTO_INCREMENT=240;

-- ----------------------------
-- Auto increment value for `price_out_transport_template`
-- ----------------------------
ALTER TABLE `price_out_transport_template` AUTO_INCREMENT=76;

-- ----------------------------
-- Auto increment value for `price_out_transport_valueadded`
-- ----------------------------
ALTER TABLE `price_out_transport_valueadded` AUTO_INCREMENT=12;

-- ----------------------------
-- Auto increment value for `price_storage_extra`
-- ----------------------------
ALTER TABLE `price_storage_extra` AUTO_INCREMENT=62;

-- ----------------------------
-- Auto increment value for `price_storage_material`
-- ----------------------------
ALTER TABLE `price_storage_material` AUTO_INCREMENT=82;

-- ----------------------------
-- Auto increment value for `price_storage_step`
-- ----------------------------
ALTER TABLE `price_storage_step` AUTO_INCREMENT=470;

-- ----------------------------
-- Auto increment value for `price_storage_template`
-- ----------------------------
ALTER TABLE `price_storage_template` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `price_transport_line`
-- ----------------------------
ALTER TABLE `price_transport_line` AUTO_INCREMENT=3324;

-- ----------------------------
-- Auto increment value for `price_transport_line_range`
-- ----------------------------
ALTER TABLE `price_transport_line_range` AUTO_INCREMENT=419;

-- ----------------------------
-- Auto increment value for `price_transport_template`
-- ----------------------------
ALTER TABLE `price_transport_template` AUTO_INCREMENT=87;

-- ----------------------------
-- Auto increment value for `price_transport_valueadded`
-- ----------------------------
ALTER TABLE `price_transport_valueadded` AUTO_INCREMENT=164;

-- ----------------------------
-- Auto increment value for `pub_address`
-- ----------------------------
ALTER TABLE `pub_address` AUTO_INCREMENT=3734;

-- ----------------------------
-- Auto increment value for `pub_elec_warehouse`
-- ----------------------------
ALTER TABLE `pub_elec_warehouse` AUTO_INCREMENT=23;

-- ----------------------------
-- Auto increment value for `system_code`
-- ----------------------------
ALTER TABLE `system_code` AUTO_INCREMENT=779;

-- ----------------------------
-- Auto increment value for `system_code_type`
-- ----------------------------
ALTER TABLE `system_code_type` AUTO_INCREMENT=154;
