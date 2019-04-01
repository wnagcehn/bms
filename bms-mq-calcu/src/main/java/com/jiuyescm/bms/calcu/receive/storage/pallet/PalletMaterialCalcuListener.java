package com.jiuyescm.bms.calcu.receive.storage.pallet;

import org.springframework.stereotype.Service;

@Service("palletMaterialCalcuListener")
public class PalletMaterialCalcuListener extends PalletCalcuBase{

	public String bizType = "material";
}
