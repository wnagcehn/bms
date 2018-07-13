package com.jiuyescm.bms;

import org.junit.Test;

public class BizTransportTest {

	@Test
	public void testAdd(){
		/*ClassPathXmlApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(
						"classpath:spring/spring-context.xml");
			context.start();
			IBizTransportRpcService bizTransportRpcServiceImpl = (IBizTransportRpcService)context.getBean("bizTransportRpcServiceImpl");
			
			List<BizGanxianWaybillVo> list = new ArrayList<BizGanxianWaybillVo>();
			BizGanxianWaybillVo vo = new BizGanxianWaybillVo();
			vo.setTmsId("T000000001");
			vo.setChargeType("REC");
			vo.setBizTypeCode("TC");
			vo.setCustomerId("1100000027");
			vo.setCustomerName("上海都乐食品有限公司");
			vo.setOrderNo("bhl17080300130");
			vo.setSendTime(Timestamp.valueOf("2017-09-23 00:00:00"));
			vo.setInterfaceType("PUT");
			vo.setSendProvinceName("上海");
			vo.setSendCityName("上海市");
			vo.setReceiverProvinceName("上海");
			vo.setReceiverCityName("上海市");
			vo.setCarModel("4.2");
			list.add(vo);
			List<BizGanxianWaybillReturnVo> returnList = bizTransportRpcServiceImpl.pushTransportBizBatch(list);
			if (null != returnList && !returnList.isEmpty()) {
				for (BizGanxianWaybillReturnVo entity : returnList) {
					System.out.println(entity.getTmsId());
					System.out.println(entity.getIsLight());
					System.out.println(entity.getSysAmount());
					System.out.println(entity.getReturnCode());
					System.out.println(entity.getReturnMsg());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			context.stop();
		}*/
	}
	
	@Test
	public void invalid(){
		/*ClassPathXmlApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
			context.start();
			
			IBizTransportRpcService bizTransportRpcServiceImpl = (IBizTransportRpcService)context.getBean("bizTransportRpcServiceImpl");
			List<String> list = new ArrayList<String>();
			list.add("T000000001");
			
			List<BizGanxianWaybillReturnVo> returnList = bizTransportRpcServiceImpl.invalidTransportBizBatch(list);
			if (null != returnList && !returnList.isEmpty()) {
				for (BizGanxianWaybillReturnVo entity : returnList) {
					System.out.println(entity.getTmsId());
					System.out.println(entity.getIsLight());
					System.out.println(entity.getSysAmount());
					System.out.println(entity.getReturnCode());
					System.out.println(entity.getReturnMsg());
				}
			}
		} catch (Exception e) {
		}*/
	}
}
