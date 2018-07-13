package com.jiuyescm.bms.common;

public interface IOmsApiConstantDefine {
	/**
	 * 物流商
	 * @author zhengyishan
	 *
	 */
    public interface ICarrier{
    	//久曳
    	public static final String CARRIER_JIUYE="1500000016";
    	
    }
    /**
     * 特殊宅配商
     * @author zhengyishan
     *
     */
    public interface IDeliver{
    	public static final String DELIVER_JYZS="1400000074"; //久耶自送 
    	public static final String DELIVER_QFKD="1400000065"; //全峰快递
    	public static final String DELIVER_KHZT="1400000073"; //客户自提
    }
    /**
     * 调用外部系统常量
     * @author zhengyishan
     *
     */
    public interface IOutSystemPara{
    	public static final String FD_REQUEST_STATION="RequestOrdersStation";
    }
    
    /**
     * 物流
     * @author zhengyishan
     *
     */
    public interface CarrierConstant{
 	   public static final String CARRIER_JY="1500000016"; //久曳
 	   
 	   public static final String CARRIER_SF="1500000015"; //顺丰
 	   
 	   public static final String CARRIER_BS="1500000017"; //百事汇通
 	   
// 	   public static final String CARRIER_ZT="1500000119"; //客户自提
// 	   
// 	   public static final String CARRIER_JYZS="1500000120"; //九曳自送
 	   
 	   public static final String CARRIER_ZHONGTONG="1500000018";//中通
 	   
 	   public static final String CARRIER_JYZT="1300020016";//九曳自提(物流商，用于客户自提)
 	   
    }
}
