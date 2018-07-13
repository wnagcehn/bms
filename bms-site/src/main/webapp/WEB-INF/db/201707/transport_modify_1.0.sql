/* 应收运输模板的修改：*/
/* 产品类型变了后,不需要运输方式 */
alter table price_transport_template drop column transport_form_code; 


/* 应收运输模板的阶梯修改：*/
/* 是否轻货 */
alter table price_transport_line_range add is_light boolean after unit_price; 



/* 应付运输模板的修改：*/
/* 产品类型变了后,不需要运输方式 */
alter table price_out_transport_template drop column transport_form_code; 

/* 应付运输模板的阶梯修改：*/
/* 是否轻货 */
alter table price_out_transport_line_range add is_light boolean after unit_price; 


