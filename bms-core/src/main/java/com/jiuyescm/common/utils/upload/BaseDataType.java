package com.jiuyescm.common.utils.upload;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class BaseDataType {
	public String ID;
	public String name;
	public List<DataProperty> dataProps = new ArrayList<DataProperty>();

	public List<DataProperty> getDataProps() {
		return dataProps;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getPropIds(){
		List<String> propIds=(List<String>) CollectionUtils.collect(dataProps, new Transformer(){
			public Object transform(Object arg0) {
				BaseDataType u = (BaseDataType) arg0;
                return u.ID;
            }
		});
		return propIds;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getPropNames(){
		List<String> propNames=(List<String>) CollectionUtils.collect(dataProps, new Transformer(){
			public Object transform(Object arg0) {
				BaseDataType u = (BaseDataType) arg0;
                return u.name;
            }
		});
		return propNames;
	}

}
