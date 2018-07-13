package com.jiuyescm.common.utils;

import java.text.DecimalFormat;

import com.jiuyescm.cfm.common.PubUtil;

/**
 * 运单号生成规则计算
 * 
 * @author zhengyishan
 * 
 */
public class YunDanNoGenerate {
	public static final String ERROR = "ERROR"; // 生成错误
	public static final String JY="JY";
	/**
	 * 久曳运单号生成规则
	 * 
	 * @param beginNo
	 * @return
	 * @throws Exception 
	 */
	public static String JiuYeNextNoGenerate(String beginNo) throws Exception {
		beginNo = PubUtil.trim(beginNo);
		if (!beginNo.equals("") && beginNo.length() != 11) {
			//throw new Exception( "传入的久曳开始运单号长度不等于11!");
			throw new Exception( "传入久曳运单号格式有问题!");
		}
		if(!beginNo.equals("") && !beginNo.substring(0, 2).equals(JY)){
			throw new Exception( "输入的久曳运单号有问题!");
		}
		DecimalFormat df = new DecimalFormat("000000000");
		long lnum=Long.parseLong(beginNo.substring(2, 11))+1;
		
		
		return JY+df.format(lnum);
	}

	/**
	 * 单个顺丰单号生成
	 * 
	 * @param beginNo
	 * @return
	 * @throws Exception 
	 */
	public static String ShunFengNextNoGenerate(String beginNo) throws Exception {
		beginNo = PubUtil.trim(beginNo);
		if (!beginNo.equals("") && beginNo.length() != 12) {
			throw new Exception("传入的顺丰开始运单号长度不等于12!");
		}
		DecimalFormat df = new DecimalFormat("00000000000"); //11
		DecimalFormat df2 = new DecimalFormat("00000000"); //8
		
		String a10 = beginNo.substring(0, 3);
		Long sNum = Long.parseLong(beginNo.substring(3, 11)) + 1;
		long cb = 0;

		// 计算乘积
		long p7 = Long.parseLong(df.format(sNum).substring(3, 4)) * 15;
		long p6 = Long.parseLong(df.format(sNum).substring(4, 5)) * 13;
		long p5 = Long.parseLong(df.format(sNum).substring(5, 6)) * 11;
		long p4 = Long.parseLong(df.format(sNum).substring(6, 7)) * 9;
		long p3 = Long.parseLong(df.format(sNum).substring(7, 8)) * 7;
		long p2 = Long.parseLong(df.format(sNum).substring(8, 9)) * 5;
		long p1 = Long.parseLong(df.format(sNum).substring(9, 10)) * 3;
		long p0 = Long.parseLong(df.format(sNum).substring(10, 11));

		// 计算商
		long q0 = p0 / 10;
		long q1 = p1 / 10;
		long q2 = p2 / 10;
		long q3 = p3 / 10;
		long q4 = p4 / 10;
		long q5 = p5 / 10;
		long q6 = p6 / 10;
		long q7 = p7 / 10;
		// 计算余数
		long r0 = p0 % 10;
		long r1 = p1 % 10;
		long r2 = p2 % 10;
		long r3 = p3 % 10;
		long r4 = p4 % 10;
		long r5 = p5 % 10;
		long r6 = p6 % 10;
		long r7 = p7 % 10;

		// 将所有的商和余数相加
		long sumQR = q0 + q1 + q2 + q3 + q4 + q5 + q6 + q7 + r0 + r1 + r2 + r3
				+ r4 + r5 + r6 + r7;
		// 用最小的且大于该和（SQ+R）的10的倍数（X）减去该和（SQ+R）

		long m = (sumQR / 10 + 1) * 10 - sumQR;

		cb = m;
		if (m == 10) {
			cb = 0;
		}

		return a10 +df2.format(sNum).substring(0, 8) + cb;
	}
	//测试
	public static void main(String args[]) throws Exception{
		String beginNo="919654969811";  //"919654969741";
		System.out.println(ShunFengNextNoGenerate("919654969741"));

		
	}
}
