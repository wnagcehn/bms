package com.jiuyescm.common;


/**
 * 常量定义
 * 
 * @author zhengyishan
 * 
 */
public interface ConstantInterface {
	/**
	 * 单据状态
	 * 
	 * @author zhengyishan (BEGIN,AUDIT,CONFIRM,FINISH) 初始；审核；确认；完成；
	 */
	public interface BillStatus {
		public static final String BEGIN = "BEGIN";
		//转换转储单
		public static final String TRANS ="TRANS";

		public static final String AUDIT = "AUDIT";

		public static final String CONFIRM = "CONFIRM";
		
		public static final String PRINT = "PRINT";

		public static final String FINISH = "FINISH";
		
		public static final String PART_FINISH = "PART_FINISH";
		
	}

	/**
	 * 产地直配发货单状态表
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface Operatestatus {
		public static final String WMACP = "WMACP"; // 接单

		public static final String PBCRE = "PBCRE"; // 创建拣货单

		public static final String PBCFM = "PBCFM"; // 复核

		public static final String WMWEIGHT = "WMWEIGHT"; // 称重 （处于当前状态不能关闭）
		
		public static final String GENCNSIGN="CRECNSIGN"; //已生成交接单 CRECNSIGN

		public static final String CNSIGN = "CNSIGN"; // 交接 （处于当前状态不能关闭）

	}

	/**
	 * 单据是否作废状态
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface InvalidInterface {
		// 未作废
		public static final int INVALID_0 = 0;

		// 已作废
		public static final int INVALID_1 = 1;
	}
	
	/**
	 * 合同归属
	 * 
	 */
	public interface ContractAttr {
		// BMS
		public static final int INVALID_1 = 1;

		// 合同在线
		public static final int INVALID_2 = 2;
	}

	/**
	 * 单据类型常量定义
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface BillTypeInterface { // 单据类型
		// 产地直配出库
		public static final String BILL_10001 = "10001";
		// 采购入库单
		public static final String BILL_10002 = "10002";
		// 退货入库单
		public static final String BILL_10003 = "10003";
		// 调拨入库单
		public static final String BILL_10004 = "10004";
		// 销售出库单
		public static final String BILL_10005 = "10005";
		// 其他出库单（仓配）
		public static final String BILL_10006 = "10006";
		// 调拨出库单
		public static final String BILL_10007 = "10007";
		// 库间转储
		public static final String BILL_10008 = "10008";
		// 库内调拨
		public static final String BILL_10009 = "10009";
		// 转储出库
		public static final String BILL_10010 = "10010";
		// 运输预约单出库
		public static final String BILL_10011 = "10011";
		//B2B其他
		public static final String BILL_10012 = "10012";
		//B2C其他
		public static final String BILL_10013 = "10013";
	}

	/**
	 * 来源ERP常量定义
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface FromERPCodeInterface { // 来源ERP
		public static final String ERP_TABAO = "taobao"; // 物流宝

		public static final String ERP_JD = "jd"; // 京东

		public static final String ERP_OMS = "oms"; // OMS系统

		public static final String ERP_GUANYI = "guanyi"; // 管易

		public static final String ERP_WDT = "wangdiantong"; // 旺店通
		
		public static final String ERP_OT = "other"; // 其他
		
	}

	/**
	 * 是否赠品
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface Isgiftflag {
		// 是
		public static final int GIFTFLAG_0 = 0;
		// 否
		public static final int GIFTFLAG_1 = 1;
	}

	/**
	 * 支付方式
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface PaymentType {
		// 在线支付
		public static final String PAY_ONLINEPAY = "onlinepay";
		// 现金支付
		public static final String PAY_CODCASH = "codcash";
		// POS刷卡
		public static final String PAY_CODPOS = "codpos";
		// 货到付款
//		public static final String PAY_CODDELIVERY = "coddelivery";
	}

	/**
	 * 发票类型
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface Invoicetype {
		public static final String INVOICE = "INVOICE";// 普通发票
		public static final String VINVOICE = "VINVOICE"; // 增值税普通发票
		public static final String EVINVOICE = "EVINVOICE"; // 电子增票

	}

	/**
	 * 拣货单创建单据形态选择
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface BillIde {
		// 单品单件
		public static final String ONE2ONE_PDU = "ONE2ONE_PDU";
		// 单品多件
		public static final String ONEPDU_2MANY = "ONEPDU_2MANY";
		// 单件多品
		public static final String ONE2MANY_PDU = "ONE2MANY_PDU";
		// 多件多品
		public static final String MANY2MANY = "MANY2MANY";
		//其他
		public static final String OTHER ="OTHER";

	}
	 
	/**
	 * 产地支配-拣货单类型
	 * @author zhengyishan
	 *
	 */
    public interface Pickingbatchtype{
    	//单品单件
    	public static final String  ONE2ONE="P1Q1";
    	
    	//其他
    	public static final String OTHER ="PMQM";
    }
	/**
	 * 配送温度常量定义 冷藏、冷冻、常温
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface Temperature {

		public static final String ALL = "ALL";
		// 常温
		public static final String CW = "CW";
		// 冷冻
		public static final String LD = "LD";
		// 冷藏
		public static final String LC = "LC";
	}

	/**
	 * 撤单标记
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface Exceptionflag {
		public static final int FLAG_0 = 0; // 未撤单

		public static final int FLAG_1 = 1; // 已撤单
	}

	/**
	 * 挂起标记
	 * 
	 * @author zhengyishan
	 * 
	 */
	public interface Suspendflag {
		public static final int FLAG_0 = 0; // 未挂起

		public static final int FLAG_1 = 1; // 已挂起
	}
    /**
     * B2B标示
     * @author zhengyishan
     *
     */
	public interface B2BFlag{
		public static final int FLAG_0=0 ; //B2C
		public static final int FLAG_1=1 ; //B2B
		public static final int FLAG_2=2 ; //疑似B2B
	}
	/**
	 * 维护运单操作类型
	 * @author zhengyishan
	 *
	 */
	public interface Operatetype{
		public static final String Operatetype_NORMAL="NORMAL"; //正常维护
		
		public static final String Operatetype_MODCARRIER="MODCARRIER"; //更换物流商
		
		public static final String Operatetype_MODYDNO="MODYDNO";  //更换运单号
		
		
	}
	/**
	 * OMS交接单状态
	 * @author zhengyishan
	 *
	 */
	public interface TransferStatus{
		public static final String Status_CREATE="CREATE"; //已创建
		
		public static final String Status_CONFIRM="CONFIRM"; //已交接
	}

	/**
	 * 平台编码
	 * @author zhengyishan
	 *
	 */
	public interface PlatFormID{
		
		public static final String  TM="TM"; //天猫
		
		public static final String TB="TB"; //淘宝
		
		public static final String YHD="YHD"; //1号店
		
		public static final String JD="JD";  //京东
		
		public static final String SN="SN" ;//苏宁易购
		
		public static final String  AMAZON="AMAZON"; //亚马逊
		
		public static final String WSYZ="WSYZ"; //微商有赞
		
		public static final String OT="OTHER"; //其他
		
	}

	/**
	 * 打印条数范围
	 * @author chenfei
	 *
	 */
	public interface PrintCountRange{
		public static final int MIN_PRINT_COUNT=1;//打印的最小条数
		
		public static final int MAX_PRINT_COUNT=3000;//打印的最大条数
	}
	
	/**
	 * 单据基本类型
	 * @author chenfei
	 *
	 */
	public interface Basetype{
		public static final String Basetype_RK="RK";// 入库
        public static final String Basetype_CK="CK";//出库
        public static final String Basetype_ZP="ZP";//产地直配
        public static final String Basetype_KJ="KJ";//库间转储
        public static final String Basetype_KN="KN";//库内调拨
        public static final String Basetype_YS="YS";//运输单
        public static final String Basetype_ZC="ZC";//转储单
        public static final String Basetype_PD="PD";//盘点单
	}
	
	/**
	 * 是否
	 * @author chenfei
	 *
	 */
	public interface OmsYesNo{
		public static final Integer OMS_FLAG_0=0;//否
		public static final Integer OMS_FLAG_1=1;//是
	}
	
	/**
	 * 面单打印类型
	 * @author chenfei
	 *
	 */
	public interface DigitalPrint{
		public static final String RM="RM";//热敏单
		public static final String CT="CT";//背胶面单
	}
	
	/**
	 * 打印单据的类型
	 * @author chenfei
	 *
	 */
	public interface PrintType{
		public static final String SORTING_BILL="SORTING";//拣货单
		public static final String HEAT_BILL="HEAT";//热敏（面单）
		public static final String TRANSPORT_BILL="TRANSPORT";//背胶（运单）
		public static final String JOIN_BILL="JOIN";//交接单 
		public static final String SORTING_BILL_NEW="SORTING_NEW";//新拣货单
	}
	
	/**
	 * 打印的样式
	 * @author chenfei
	 *
	 */
	public interface PrintStyle{
		public static final String SORTING_BILL="SORTING";//拣货单
		public static final String JOIN_BILL="JOIN";//交接单 
		public static final String JY_HEAT_BILL_1="JY_HEAT_BILL_1";//久曳热敏（面单）
		public static final String JY_HEAT_BILL_2="JY_HEAT_BILL_2";//久曳热敏（面单）
		public static final String QF_HEAT_BILL_10_18="QF_HEAT_BILL_10_18";//全峰快递（面单）10*18
		public static final String SF_HEAT_BILL_1="SF_HEAT_BILL_1";//顺丰热敏（面单）
		public static final String JY_TRANSPORT_BILL_1="JY_TRANSPORT_BILL_1";//久曳四联单（运单）
		public static final String ZHONGTONG_HEAT_BILL_1="ZHONGTONG_HEAT_BILL_1";//中通热敏
		public static final String YTO_HEAT_BILL_1="YTO_HEAT_BILL_1";//圆通热敏
		public static final String UC_HEAT_BILL_10_18="UC_HEAT_BILL_10_18";//优速快递（面单）10*18
		public static final String BS_HEAT_BILL_10_18="BS_HEAT_BILL_10_18";//百世汇通快递（面单）10*18
		public static final String SORTING_BILL_NEW="SORTING_NEW";//新拣货单
	}
	
	/**
	 * 打印的样式
	 * @author wangdi
	 *
	 */
	public interface omsPrintStyle{
		public static final String JY_HEAT_BILL_1="JY_HEAT_BILL_1";//久曳热敏8-12
		public static final String JY_HEAT_BILL_2="JY_HEAT_BILL_2";//久曳热敏10-18
		public static final String QF_HEAT_BILL_10_18="QF_HEAT_BILL_10_18";//全峰快递（面单）10*18
		public static final String JY_TRANSPORT_BILL_1="JY_TRANSPORT_BILL_1";//久曳四联单
		public static final String SF_HEAT_BILL_1="SF_HEAT_BILL_1";//顺丰热敏10-15
		public static final String ZHONGTONG_HEAT_BILL_1="ZHONGTONG_HEAT_BILL_1";//中通热敏10-18
		public static final String YTO_HEAT_BILL_1="YTO_HEAT_BILL_1";//圆通热敏10-18
		public static final String UC_HEAT_BILL_10_18="UC_HEAT_BILL_10_18";//优速快递（面单）10*18
		public static final String BS_HEAT_BILL_10_18="BS_HEAT_BILL_10_18";//百世汇通快递（面单）10*18
	}
	
	/**
	 * 运单生成规则
	 * @author chenfei
	 *
	 */
	public interface ExpressAddRule{
		public static final String RULE_JY="JY";//九曳
		public static final String RULE_SF="SF";//顺丰
		public static final String RULE_BS="BS";//百世汇通
		public static final String RULE_ZHONGTONG="ZHONGTONG";//中通
		public static final String RULE_YTO="YTO";//圆通
		public static final String RULE_UC="UC";//优速
	}
	
	/**
	 * 导入Excel数据校验结果
	 * @author chenfei
	 *
	 */
	public interface ImportExcelStatus{
		public static final String IMP_ERROR="error"; //导入Excel有错误信息
		public static final String IMP_SUCC="succ"; //导入Excel成功
	}

	/**
	 * 拆箱标记
	 * @author chenfei
	 *
	 */
	public interface SplitBoxFlag{
		public static final int SPLIT_BOX_NO=0;//未拆箱
		public static final int SPLIT_BOX_YES=1;//已拆箱
		public static final int SPLIT_BOX_GOING=2;//拆箱中
	}
	
	/**
	 * 顺丰接口地址
	 * @author chenfei
	 *
	 */
	public interface SfURI{
		public static final String URI="URI";
		public static final String HEAD="HEAD";
		public static final String CHECKWORD="CHECKWORD";
	}
	
	/**
	 * 中通接口地址
	 * @author chenfei
	 *
	 */
	public interface ZtURI{
		public static final String ZT_URI="ZT_URI";
		public static final String PARTNER="PARTNER";
		public static final String PASS="PASS";
	}
	
	/**
	 * 圆通接口地址
	 * @author chenfei
	 *
	 */
	public interface YtoURI{
		public static final String YTO_URI="YTO_URI";
		public static final String YTO_CLIENTID="YTO_CLIENTID";
		public static final String YTO_PARTERNID="YTO_PARTERNID";
	}
	
	/**
	 * 优速接口接口参数
	 * @author chenfei
	 *
	 */
	public interface UcURI{
		public static final String SEND_URL = "UC_SEND_URL";//发送地址	
		public static final String CHARSET = "UC_CHARSET";//字符集
		public static final String SIGN_TYPE = "UC_SIGN_TYPE";//签名方式
		public static final String DATA_TYPE = "UC_DATA_TYPE";//数据格式
		public static final String SECURITY_KEY = "UC_SECURITY_KEY";//密钥
		public static final String PARTNER = "UC_PARTNER";//合作伙伴编号
		public static final String SERVER_NAME = "UC_SERVER_NAME";//调用的服务(映射的方法名)
		public static final String CUSTOMER_ID = "UC_CUSTOMER_ID";//发件客户编号
		public static final String CHECK_CODE = "UC_CHECK_CODE";//客户校验码
	}
	
	/**
	 * 批量获取中通运单号单次的个数
	 * @author chenfei
	 *
	 */
	public interface ZtBatchGetExpressnumMax{
		public static final int max=50;
	}
	
	/**
	 * 付款方式
	 * @author chenfei
	 *
	 */
	public interface Paymethod{
		public static final int SENDER_PAY=1;//1:寄方付
		public static final int RECEIVER_PAY=2;//2:收方付
		public static final int THIRD_PAY=3;//3:第三方付
	}
	
//	/**
//	 * 顺丰快件产品类别
//	 * @author chenfei
//	 *
//	 */
//	public interface Expresstype{
//		public static final String CLOUD_WAREHOUSE="37"; //云仓专配次日
//		
//		public static final String STA_DELIVERY="1"; //标准快递-1
//		public static final String SF_Sale="2"; //顺丰特惠-2
//		public static final String DS_Sale="3"; //电商特惠-3
//		public static final String DS_SP="7"; //电商速配-7
//		public static final String SX_SP="15"; //生鲜速配-15
//		public static final String CLOUD_WAREHOUSE_G="38"; //云仓专配隔日-38
//		
//	}
	
	/**
	 * 是否支持拆箱运单号
	 * @author chenfei
	 *
	 */
	public interface Issplitboxflag{
		public static final String YES="Y"; //支持
		public static final String NO="N"; //不支持
	}
	
	/**
	 * WMS接口地址
	 * @author chenfei
	 *
	 */
	public interface WMSURI{
		public static final String INTERCEPT_ORDER_URI="INTERCEPT_ORDER_URI";
	}
	
	/**
	 * 分配物流信息类型
	 * @author chenfei
	 *
	 */
	public interface ActType{
		public static final String ACT_TYPE_FPWLS="FPWLS"; // 分配物流商
		public static final String ACT_TYPE_FPZPS="FPZPS"; // 分配宅配商
		public static final String ACT_TYPE_FPZD="FPZD"; // 分配站点
	}
	

	
	
	/**
	 * 预约单状态	order_status
	 * 
	 */
	public interface OrderStatus {
		public static final String ACCEPTED = "ACCEPTED"; // 初始
		public static final String CANCELED = "CANCELED"; // 已取消
		public static final String CHECKED = "CHECKED"; // 已审核
		public static final String CONFIRMED = "CONFIRMED"; // 已确认
		public static final String STOPED = "STOPED"; // 已关闭
	}
	
	/**
	 * 地址类型（收货、发货）	addrType
	 * 
	 */
	public interface AddrType {
		public static final String RECEIVE = "RECEIVE"; // 收货
		public static final String DELIVER = "DELIVER"; // 发货
	}
	
	/**
	 * 返回状态
	 * @author chenfei
	 *
	 */
	public interface StatusResult {
		public static final String ERROR = "error"; //失败
		public static final String SUCC = "succ"; //成功
	}
	
	/**
	 * 地址状态
	 * @author chenfei
	 *
	 */
	public interface AddrStatus {
		public static final String USED = "USED"; //使用中
		public static final String CLOSED = "CLOSED"; //已关闭
	}
	
	/**
	 * 标识
	 * @author chenfei
	 *
	 */
	public interface OmsFlag{
		public static final Integer OMS_FLAG_0=0;//未开始
		public static final Integer OMS_FLAG_1=1;//进行中
		public static final Integer OMS_FLAG_2=2;//已完成
	}

	/**
	 * 标识判断是否计算过
	 * @author zhaofeng
	 *
	 */
	public interface Calculate{
		 public static final String CALCULATE_YES="1";//计算过的
		 public static final String CALCULATE_NO="0";//未计算过的
	}
	
	/**
	 * 判断是否审核过
	 * @author zhaofeng
	 *
	 */
	public interface IsChecked{
		 public static final String ISCHECKED_YES="1";//计算过的
		 public static final String ISCHECKED_NO="0";//未算过的
	}
	
	/**
	 * 商家合同类型
	 * @author zhaofeng
	 *
	 */
	public interface ContractType{
		public static final String CUSTOMER_CONTRACT="CUSTOMER_CONTRACT";//商家合同
		public static final String CARRIER_CONTRACT="CARRIER_CONTRACT";//承运商合同
		public static final String DELIVER_CONTRACT="DELIVER_CONTRACT";//宅配商合同
	}
	

	/**
	 * 费用账单状态
	 * @author yangss
	 */
	public interface FeeBillStatus{
		 public static final int STATUS_0 = 0;//未过账
		 public static final int STATUS_1 = 1;//已过账
	}
	
	/**
	 * 费用状态
	 * @author yangss
	 */
	public interface FeeStatus{
		 public static final String STATUS_0 = "0";//未对账
		 public static final String STATUS_1 = "1";//已对账
	}
	
	/**
	 * 费用账单类型
	 * @author yangss
	 */
	public interface FeeBillType{
		 public static final String TYPE_1 = "1";//宅配、异常
		 public static final String TYPE_2 = "2";//运输
	}
	
	/**
	 * 作废标识类型
	 * @author yangss
	 */
	public interface DelFlag{
		 public static final String NO = "0";//未删除
		 public static final String YES = "1";//已删除
	}
	
	/**
	 * 返回编码
	 * @author yangss
	 */
	public interface ReturnCode{
		 public static final String SUCCESS = "SUCCESS";//成功
		 public static final String FAIL = "FAIL";//失败
	}
	
	/**
	 * 接口返回编码
	 * @author yangss
	 */
	public interface ResponseFlag{
		 public static final String SUCCESS = "success";//成功
		 public static final String FAIL = "fail";//失败
	}
	
	
	/**
	 * BMS MQ 消息队列key值枚举
	 * @author caojianwei
	 *
	 */
	public interface BmsMQQueue{
		/**
		 * 异步导出
		 */
		public static final String BMS_FILE_EXPORT_QUEUE = "BMS_FILE_EXPORT_QUEUE";
		
		/**
		 * 异步导入
		 */
		public static final String BMS_FILE_IMPORT_QUEUE = "BMS_FILE_IMPORT_QUEUE";
		
	}
	
}
