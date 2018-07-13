/*应付业务数据新增字段*/
ALTER table biz_dispatch_pay_bill add zexpressnum VARCHAR(24) default null COMMENT '转寄后运单号',add bigstatus VARCHAR(12) default null COMMENT '小状态',
add smallstatus VARCHAR(12) default null COMMENT '大状态',add system_weight double COMMENT '系统逻辑重量',add totalqty int(11),add product_detail longtext DEFAULT null COMMENT '商品明细',
add expresstype VARCHAR(8) default null COMMENT '快递业务类型',add sku_num int(11) default null comment 'sku数',add extra1 VARCHAR(32),add extra2 VARCHAR(32),add extra3 VARCHAR(32),add extra4 VARCHAR(32)