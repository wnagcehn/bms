package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;

import com.jiuyescm.common.utils.DoubleUtil;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double originWeight=1111.0;
		double totalWeight=0d;
		String weightString=originWeight+"";
		if(weightString.contains(".0")){
			String value=weightString.substring(weightString.indexOf("."));
			if(value.length()>2){
				String v=value.substring(value.indexOf(".")+2,value.indexOf(".")+3);
				Double val=Double.valueOf(v);
				if(val==0){
					totalWeight=Math.floor(originWeight);
				}else{
					BigDecimal   a1   =BigDecimal.valueOf(originWeight);
					BigDecimal   a2  =new BigDecimal(0.1);
					totalWeight=a1.add(a2).doubleValue();
				}
			}else{
				totalWeight=originWeight;
			}
		}else{
			totalWeight=originWeight;
		}
		
		System.out.println(totalWeight);
	}

}
