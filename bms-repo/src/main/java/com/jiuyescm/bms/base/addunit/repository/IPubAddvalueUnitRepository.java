package com.jiuyescm.bms.base.addunit.repository;

import java.util.List;
import java.util.Map;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IPubAddvalueUnitRepository {

    List<String> queryUnitBySecondSubject(Map<String, Object> condition);

}
