/*应收宅配运单新增温度类型*/
ALTER table biz_dispatch_bill add temperature_type_code VARCHAR(32) default null COMMENT '温度类型'

/*应收宅配运单新增温度类型名称*/
ALTER table biz_dispatch_bill add temperature_type_name VARCHAR(128) default null COMMENT '温度类型名称'

/*应收配送报价中新增温度类型 */
ALTER table price_dispatch_detail add temperature_type_code VARCHAR(32) default null COMMENT '温度类型'

/*应付配送报价中新增冗余字段 */
ALTER table price_dispatch_detail add extra1 VARCHAR(32),add extra2 VARCHAR(32),add extra3 VARCHAR(32),add extra4 VARCHAR(32)

/*应付宅配运单新增温度类型*/
ALTER table biz_dispatch_pay_bill add temperature_type_code VARCHAR(32) default null COMMENT '温度类型'

/*应付宅配运单新增温度类型名称*/
ALTER table biz_dispatch_pay_bill add temperature_type_name VARCHAR(128) default null COMMENT '温度类型名称'

/*应付配送报价中新增温度类型 */
ALTER table price_out_dispatch_detail add temperature_type_code VARCHAR(32) default null COMMENT '温度类型'

/*应付配送报价中新增冗余字段 */
ALTER table price_out_dispatch_detail add extra1 VARCHAR(32),add extra2 VARCHAR(32),add extra3 VARCHAR(32),add extra4 VARCHAR(32)

/*应收宅配运单新增备注*/
ALTER table biz_dispatch_bill add remark VARCHAR(1000) default null COMMENT '备注'

/*应付宅配运单新增备注*/
ALTER table biz_dispatch_pay_bill add remark VARCHAR(1000) default null COMMENT '备注'

/*耗材出库表新增备注*/
ALTER table biz_outstock_packmaterial add remark VARCHAR(1000) default null COMMENT '备注'

/*耗材库存表新增备注*/
ALTER table biz_pack_storage add remark VARCHAR(1000) default null COMMENT '备注'

/*商品库存表新增备注*/
ALTER table biz_product_storage add remark VARCHAR(1000) default null COMMENT '备注'

/*商品按托存储库存新增备注*/
ALTER table biz_product_pallet_storage add remark VARCHAR(1000) default null COMMENT '备注'





