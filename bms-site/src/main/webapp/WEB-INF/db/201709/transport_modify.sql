ALTER TABLE biz_ganxian_waybill ADD COLUMN warehouse_name VARCHAR(64) COMMENT '始发仓库名称' AFTER warehouse_code;

ALTER TABLE biz_ganxian_waybill ADD COLUMN elec_warehouse_code VARCHAR(32) COMMENT '电商仓库code' AFTER warehouse_name;

ALTER TABLE biz_ganxian_waybill ADD COLUMN elec_warehouse_name VARCHAR(64) COMMENT '电商仓库名称' AFTER elec_warehouse_code;

ALTER TABLE biz_ganxian_waybill ADD COLUMN airport VARCHAR(64) COMMENT '机场' AFTER elec_warehouse_name;