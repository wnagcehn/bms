
/* 应收运输模板的路线修改：*/
/* 承运商编号  */
alter table price_transport_line add carrier_code varchar(32) before transport_line_no; 
alter table price_transport_line add carrier_name varchar(50) after carrier_code; 
/* 路线名称  */
alter table price_transport_line add transport_line_name varchar(50) after transport_line_no; 
/* 服务类型 */
alter table price_transport_line add service_type_code varchar(32) after to_address;
/* 发车时间 sendTime */
alter table price_transport_line add send_time timestamp after send_cycle; 
/* 回单时效 receiptAging */
alter table price_transport_line add receipt_aging double after timeliness; 
/* 营业时间 businessTime */
alter table price_transport_line add business_time timestamp after timeliness; 
/* 订单受理时效(天) orderAcceptAging */
alter table price_transport_line add order_acceptaging double after timeliness; 
/* 截单时间点 orderDeadLine */
alter table price_transport_line add order_deadline timestamp after timeliness; 
/* 到站自提时效(天) selfPickedAging */
alter table price_transport_line add self_pickedaging double after timeliness; 
/* 到站派送时效(天) deliveryAging */
alter table price_transport_line add delivery_aging double after timeliness; 

/* 重泡比现在确定在路线上,一条路线对应一个重泡比*/
/* 重泡比w2bubbleRatio */
alter table price_transport_line add w2bubble_ratio double after delivery_aging; 


