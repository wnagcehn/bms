package com.jiuyescm.bms.billimport.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.billimport.entity.BillFeesReceiveTransportTempEntity;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveTransportTempService;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;

/**
 * 干线
 * 
 * @author liuzhicheng
 * 
 */
@Component("干线")
public class TransportHandler extends CommonHandler<BillFeesReceiveTransportTempEntity> {
	
	@Autowired
	private IBillFeesReceiveTransportTempService billFeesReceiveTransportTempService;

	@Override
	public List<BillFeesReceiveTransportTempEntity> transRowToObj(DataRow dr) throws Exception {
		List<BillFeesReceiveTransportTempEntity> listEntity = new ArrayList<BillFeesReceiveTransportTempEntity>();
		BillFeesReceiveTransportTempEntity entity = new BillFeesReceiveTransportTempEntity();
		entity.setRowExcelNo(dr.getRowNo());
		BillFeesReceiveTransportTempEntity entity1 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity2 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity3 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity4 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity5 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity6 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity7 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity8 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity9 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity10 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity11 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity12 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity13 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity14 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity15 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity16 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity17 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity18 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity19 = new BillFeesReceiveTransportTempEntity();
		BillFeesReceiveTransportTempEntity entity20 = new BillFeesReceiveTransportTempEntity();

		for (DataColumn dc : dr.getColumns()) {
			try {
				System.out.println("列名【" + dc.getColName() + "】|值【"
						+ dc.getColValue() + "】");

				switch (dc.getColName()) {
				case "订单创建日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						Timestamp createTime =DateUtil.transStringToTimeStamp(dc
								.getColValue());
						entity.setCreateTime(createTime);
						entity.setCreateMonth(DateUtil.timeStamp2YYMM(createTime));
						}
					break;
				case "订单号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setOrderNo(dc.getColValue());
					}
					break;
//				case "外部订单号":
//					if (StringUtils.isNotBlank(dc.getColValue())) {
//						entity.setWarehouseName(dc.getColValue());
//					}
//					break;
				case "温控":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTemperatureType(dc.getColValue());
						
//						entity.setTemperatureCode( );
					}
					break;
				case "派车单号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setSendNo(dc.getColValue());
						}
					break;
				case "运单号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setWaybillNo(dc.getColValue());
					}
					break;
				case "业务类型":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setBizType(dc.getColValue());
					}
					break;
				case "始发站":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setSendSite(dc.getColValue());
					}
					break;
				case "始发省份":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setSendProvince(dc.getColValue());
					}
					break;
				case "始发城市":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setSendCity(dc.getColValue());
					}
					break;
				case "始发区":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setSendDistinct(dc.getColValue());
					}
					break;
				case "目的站":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setReceiveSite(dc.getColValue());
					}
					break;
				case "目的省份":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setReceiveProvince(dc.getColValue());
					}
					break;
				case "目的城市":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setReceiveCity(dc.getColValue());
					}
					break;
				case "目的区":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setReceiveDistinct(dc.getColValue());
					}
					break;
				case "体积":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalVolumn(new BigDecimal(dc.getColValue()));
					}
					break;
				case "重量":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setTotalWeight(new BigDecimal(dc.getColValue()));
						}
					break;
				case "是否泡货":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setIsLight(dc.getColValue());
					}
					break;
				case "车型":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						entity.setCarModel(dc.getColValue());
					}
					break;
				default:
					break;
				}

			} catch (Exception ex) {
				throw new BizException("行【" + dr.getRowNo() + "】，列【"
						+ dc.getColName() + "】格式不正确");
			}
		}
	
		for(DataColumn dc : dr.getColumns()){
			switch (dc.getColName()) {
			case "运费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity1, entity);
					entity1.setAmount(new BigDecimal(dc.getColValue()));
					entity1.setSubjectCode("YS_YS");
					entity1.setSubjectName("运费");
					listEntity.add(entity1);
				}
				break;
			case "卸货费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity2, entity);
					entity2.setSubjectCode("YS_XH");
					entity2.setSubjectName("卸货费");
					listEntity.add(entity2);	
				}
				break;
			case "装货费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity3, entity);
					entity3.setSubjectCode("YS_ZH");
					entity3.setSubjectName("装货费");
					listEntity.add(entity3);
				}
				break;
			case "缠绕膜费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity4, entity);
					entity4.setSubjectCode("YS_CRM");
					entity4.setSubjectName("缠绕膜费");
					listEntity.add(entity4);
				}
				break;
			case "纸面回单费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity5, entity);
					entity5.setSubjectCode("YS_ZMHD");
					entity5.setSubjectName("纸面回单费");
					listEntity.add(entity5);
				}
				break;
			case "逆向物流费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity6, entity);
					entity6.setSubjectCode("YS_NXWL");
					entity6.setSubjectName("逆向物流费");
					listEntity.add(entity6);
				}
				break;
			case "拆箱费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity7, entity);
					entity7.setSubjectCode("YS_CX");
					entity7.setSubjectName("拆箱费");
					listEntity.add(entity7);	
				}
				break;
			case "贴码费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity8, entity);
					entity8.setSubjectCode("YS_TM");
					entity8.setSubjectName("贴码费");
					listEntity.add(entity8);	
				}
				break;
			case "分流费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity9, entity);
					entity9.setSubjectCode("YS_FL");
					entity9.setSubjectName("分流费");
					listEntity.add(entity9);
				}
				break;
			case "上楼搬运费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity10, entity);
					entity10.setSubjectCode("YS_SLBY");
					entity10.setSubjectName("上楼搬运费");
					listEntity.add(entity10);	
				}
				break;
			case "等待制冷费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity11, entity);
					entity11.setSubjectCode("YS_DD");
					entity11.setSubjectName("等待制冷费");
					listEntity.add(entity11);	
				}
				break;
			case "过夜制冷费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity12, entity);
					entity12.setSubjectCode("YS_GY");
					entity12.setSubjectName("过夜制冷费");
					listEntity.add(entity12);
				}
				break;
			case "放空费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity13, entity);
					entity13.setSubjectCode("YS_FK");
					entity13.setSubjectName("放空费");
					listEntity.add(entity13);	
				}
				break;
			case "单据打印费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity14, entity);
					entity14.setSubjectCode("YS_DJDY");
					entity14.setSubjectName("单据打印费");
					listEntity.add(entity14);	
				}
				break;
			case "提货费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity15, entity);
					entity15.setSubjectCode("YS_TH");
					entity15.setSubjectName("提货费");
					listEntity.add(entity15);
				}
				break;
			case "送货费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity16, entity);
					entity16.setSubjectCode("YS_SH");
					entity16.setSubjectName("送货费");
					listEntity.add(entity16);	
				}
				break;
			case "加点费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity17, entity);
					entity17.setSubjectCode("YS_JD");
					entity17.setSubjectName("加点费");
					listEntity.add(entity17);	
				}
				break;
			case "赔付费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity18, entity);
					entity18.setSubjectCode("YS_PF");
					entity18.setSubjectName("赔付费");
					listEntity.add(entity18);
				}
				break;
			case "中转费":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity19, entity);
					entity19.setSubjectCode("YS_ZZ");
					entity19.setSubjectName("中转费");
					listEntity.add(entity19);	
				}
				break;
			case "其他":
				if (StringUtils.isNotBlank(dc.getColValue())) {
					PropertyUtils.copyProperties(entity20, entity);
					entity20.setSubjectCode("YS_QT");
					entity20.setSubjectName("其他");
					listEntity.add(entity20);
				}
				break;
			default:
				break;
			}

		}
		
		return listEntity;
	}

	@Override
	public void transErr(DataRow dr) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		billFeesReceiveTransportTempService.insertBatchTemp(list);
		
	}

	

	

}
