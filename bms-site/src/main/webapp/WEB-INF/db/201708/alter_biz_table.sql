ALTER TABLE biz_outstock_packmaterial add weight DOUBLE COMMENT '重量';

ALTER TABLE fees_receive_storage MODIFY COLUMN cost decimal(16,2);
