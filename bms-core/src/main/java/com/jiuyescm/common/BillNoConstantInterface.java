package com.jiuyescm.common;

public interface BillNoConstantInterface {
	/**
	 * OMS订单号
	 * @author zhengyishan
	 *
	 */
	public interface OmsOrderNoRule{
		public static final String BEGINNAME="30";
		
		public static final String LENGTH="0000000000"; //"35"+10 ，12位
	}
	
	/**
	 * OMS订单号
	 * @author zhengyishan
	 *
	 */
	public interface OmsItoIdRule{
		public static final String BEGINNAME="40";
		
		public static final String LENGTH="0000000000"; //"40"+10 ，12位
	}
	
	/**
	 * 商家
	 * @author zhengyishan
	 *
	 */
	public interface OmsCustomerIDRule{
		public static final String BEGINNAME="11";
		
		public static final String LENGTH="00000000"; //"11"+8 ，10位
	}
    /**
     * 商品
     * @author zhengyishan
     *
     */
	public interface OmsProductIDRule{
		public static final String BEGINNAME="17";
		
		public static final String LENGTH="00000000"; //"17"+8 ，10位
	}
	/**
	 * OMS拣货单号
	 * @author zhengyishan
	 *
	 */
	public interface OmsPickNoRule{
		public static final String BEGINNAME="91";
		
		public static final String LENGTH="00000000"; //"91"+8 ，10位
	}
	
	/**
	 * 数据组
	 * @author xlj
	 *
	 */
	public interface DGroupIDNo{
		public static final String BEGINNAME="P";
		
		public static final String LENGTH="00000"; //共六位
	}
	/**
	 * 物流商
	 * @author chenfei
	 *
	 */
	public interface OmsCarrierIDRule{
		public static final String BEGINNAME="13";
		
		public static final String LENGTH="00000000"; //"11"+8 ，10位
	}
	/**
	 * 宅配商
	 * @author chenfei
	 *
	 */
	public interface OmsDeliverIDRule{
		public static final String BEGINNAME="14";
		
		public static final String LENGTH="00000000"; //"14"+8 ，10位
	}
	/**
	 * 项目ID
	 * @author chenfei
	 *
	 */
	public interface OmsProjectIDRule{
		public static final String BEGINNAME="15";
		
		public static final String LENGTH="00000000"; //"15"+8 ，10位
	}
	
	/**
	 * 单据类型ID
	 * @author chenfei
	 *
	 */
	public interface OmsBilltypeIDRule{
		public static final String BEGINNAME="10";
		
		public static final String LENGTH="000"; //"10"+3 ，5位
	}
	
	/**
	 * 度量单位ID
	 * @author chenfei
	 *
	 */
	public interface OmsUomIDRule{
		public static final String BEGINNAME="1";
		
		public static final String LENGTH="0000"; //"1"+4 ，5位
	}
	
	/**
	 * 区域ID
	 * @author chenfei
	 *
	 */
	public interface OmsRegionIDRule{
		public static final String BEGINNAME="1";
		
		public static final String LENGTH="00000"; //"1"+5 ，6位
	}
	
	/**
	 * 库存地类型ID
	 * @author chenfei
	 *
	 */
	public interface OmsStockPlaceTypeIDRule{
		public static final String BEGINNAME="1";
		
		public static final String LENGTH="00000"; //"1"+5 ，6位
	}

	/**
	 * 出库运输预约单号格式：PT+数字自增编码；共12位，不足补零
	 * @author chenfei
	 *
	 */
	public interface TmTOrderNo{
		public static final String BEGINNAME="PT";
		
		public static final String LENGTH="0000000000"; //"PT"+10位数字，12位
	}
}
