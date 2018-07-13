package com.jiuyescm.bms.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;  

import org.apache.commons.lang3.StringUtils;

import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.web.DoradoContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;


/**
 * 公共导入 方法
 * @author wuliangfeng
 * T excel 导入映射实体    U Excel  验证 转换 实体
 */
public abstract class HttpCommonImport<T,U> {
	/**
	 * 设置导入excel 模板 实体 
	 * @return
	 */
	protected abstract BaseDataType getBaseDataType();
	/**
	 * 验证 导入excel
	 * @param importList excel表格中实体数据
	 * @param infoList excel 导入验证数据错误信息
	 * @return 转换后实体数据
	 */
	protected abstract List<U> validateImportList(List<T> importList,List<ErrorMessageVo> infoList);
	/**
	 * 批量保存数据
	 * @param list 转换后实体数据
	 * @return 错误/正常  数据消息
	 * @throws Exception 
	 */
	protected abstract void saveDataBatch(List<U> list) throws Exception;
	/**
	 * 泛型 读取Excel
	 * @param file
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	private List<T> readExcel(
			UploadFile file, BaseDataType bs) throws Exception {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<Map<String, String>> datas = Lists.newArrayList();
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {
					
					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				datas.add(data);
			}
			List<T> modelList = Lists.newArrayList();	
			Class<T> entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			for (Map<String, String> data : datas) {
				T p = (T) BeanToMapUtil.convertMapNull(entityClass, data);
				modelList.add(p);
			}
			return modelList;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 导入Excel 公共方法
	 * @param file excel 文件 progressFlag 0-开始处理 1-验证模板 2-读取Excel 3-开始验证数据 4-开始保存数据 5-保存结束 6-异常结束
	 * @return 错误/正常 消息
	 */
	public Map<String,Object> importFile(UploadFile file){
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);//开始处理
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();// 校验信息（报错提示）
		ErrorMessageVo errorVo = null;
		// 当期时间
		try{
			BaseDataType bs=getBaseDataType();
			// 检查导入模板是否正确
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1);//验证模板
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);	
			if (!isTemplate) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 6);//异常结束
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 2);//读取Excel
			List<T> importList=readExcel(file,bs);//读取Excel  生成Excel 实体
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 3);//开始验证数据
			List<U> list=validateImportList(importList,infoList);//验证导入书库
			if(list==null||list.size()==0){
				if(infoList.size()==0){
					errorVo = new ErrorMessageVo();
					errorVo.setLineNo(1);
					errorVo.setMsg("无数据需要保存!");
					infoList.add(errorVo);
				}
			}
			if(infoList.size()>0){
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 6);//异常结束
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 4);//开始保存数据
			saveDataBatch(list);//批量保存数据
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 5);//保存结束
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, list);
			return map;
		}catch(Exception e){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 6);//异常结束
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入异常 ,异常原因："+e.getMessage());
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
	}
	/**
	 * 获取处理进度 0-开始处理 1-验证模板 2-读取Excel 3-开始验证数据 4-开始保存数据 5-保存结束 6-异常结束
	 * @return
	 */
	protected int getProgressStatus(){
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			return 1;
		}
		return (int)(DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag")); 
	}
	/**
	 *重置处理进度
	 */
	protected void setProgressStatus() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		 
		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		} 
	}
		
}
