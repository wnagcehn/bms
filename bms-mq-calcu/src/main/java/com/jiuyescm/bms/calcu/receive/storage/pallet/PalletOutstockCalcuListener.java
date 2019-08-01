/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calcu.receive.storage.pallet;

import org.springframework.stereotype.Service;

/**
 * <功能描述>
 * 
 * @author zhangyuanzheng
 * @date 2019年7月31日 下午3:16:55
 */
@Service("palletOutstockCalcuListener")
public class PalletOutstockCalcuListener extends PalletCalcuBase{
	public String bizType = "outstock";
}


