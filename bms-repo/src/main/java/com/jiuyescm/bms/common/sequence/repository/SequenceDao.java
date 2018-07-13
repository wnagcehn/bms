package com.jiuyescm.bms.common.sequence.repository;

import java.util.List;

/**
 * 
 * @author zhengyishan
 *
 */
public interface SequenceDao {
  /**
   * 获取流水
   * @param idName 标示
   * @param getNums 要取得的个数
   * @return
   */
  public long getStartNum(String idName,long getNums);
  
  /**
   * 获得单据号:批量
   * @param startName
   * @param length
   * @param beginNum
   * @return
   */
  public List<String> getBillNoList(String idName,String startName,int size,String formatStr);
  /**
   * 单个获取单号
   * @param idName
   * @param startName
   * @param formatStr
   * @return
   */
  public String getBillNoOne(String idName,String startName,String formatStr);
  
}
