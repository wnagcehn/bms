package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;
import java.util.List;

public class QuotationCJVo implements Serializable {

	private static final long serialVersionUID = 4603639551358877089L;

	//时效
	private String timeLiness;
	//重货
	private List<WeightLimitCJVo> weight;
	//泡货
	private List<VolumeLimitVo> light;

	public String getTimeLiness() {
		return timeLiness;
	}

	public void setTimeLiness(String timeLiness) {
		this.timeLiness = timeLiness;
	}

	public List<WeightLimitCJVo> getWeight() {
		return weight;
	}

	public void setWeight(List<WeightLimitCJVo> weight) {
		this.weight = weight;
	}

	public List<VolumeLimitVo> getLight() {
		return light;
	}

	public void setLight(List<VolumeLimitVo> light) {
		this.light = light;
	}

}
