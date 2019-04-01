package com.jiuyescm.bms.calcu.receive.storage.pallet;

import org.springframework.stereotype.Service;

@Service("palletProductCalcuListener")
public class PalletProductCalcuListener extends PalletCalcuBase{

	public String bizType = "product";
}
