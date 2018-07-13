package com.jiuyescm.common;

public interface TransConstantDefine {
	/**
	    * 物流
	    * @author zhengyishan
	    *
	    */
	   public interface CarrierConstant{
		   public static final String CARRIER_JY="1500000016"; //久曳
		   
		   public static final String CARRIER_SF="1500000015"; //顺丰
		   
		   public static final String CARRIER_BS="1500000017"; //百世汇通
		   
//		   public static final String CARRIER_ZT="1500000119"; //客户自提
//		   
//		   public static final String CARRIER_JYZS="1500000120"; //九曳自送
		   
		   public static final String CARRIER_ZHONGTONG="1500000018";//中通
		   
		   public static final String CARRIER_YTO="1500000019";//圆通
		   
		   public static final String CARRIER_UC="1500000020";//优速
	   }
	   
	   /**
	    * 特殊宅配商
	    * @author zhengyishan
	    *
	    */
	   public interface IDeliver{
	   	public static final String DELIVER_JYZS="1400000051"; //久耶自送 
	   	public static final String DELIVER_QFKD="1400000065"; //全峰快递
	   }
	   
	   /**
	    * 宅配商code
	    * @author zhouyouwen
	    *
	    */
	   public interface IDeliverCode{
		   public static final String DELIVER_QFKD="QFKD"; //全峰快递
		   public static final String DELIVER_ZSF = "ZSF"; //转顺丰
		   public static final String DELIVER_ZJS = "ZJS"; //宅急送
		   public static final String DELIVER_ZYT = "ZYT"; //转圆通
		   public static final String DELIVER_ZZT = "ZZT"; //转中通
	   }
}
