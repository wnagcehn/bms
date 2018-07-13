package com.jiuyescm.common.utils.download;

import java.io.InputStream;

import com.bstek.dorado.uploader.DownloadFile;
/**
 * 文件下载工具类
 * 
 * @author wangdi
 * 
 */
public class DownloadUtil {
    
    
    /**
     * OMS订单下载
     * @param fileName
     * @return
     */
    public static DownloadFile returnOrderDownLoadFile(String newFileName) {// 传入参数fileName是要读取的资源文件的文件名如(file.properties)
        
        InputStream in = null;
        String filename = "order_template.xlsx";//订单模板名字
        in = DownloadUtil.class.getResourceAsStream(filename);
        
        return new DownloadFile(newFileName, in);
    }
    
    /**
     * OMS项目资源配置物流商下载
     * @param fileName
     * @return
     */
    public static DownloadFile returnCarrierDownLoadFile(String newFileName) {// 传入参数fileName是要读取的资源文件的文件名如(file.properties)
        
        InputStream in = null;
        String filename = "carrier_template.xlsx";//订单模板名字
        in = DownloadUtil.class.getResourceAsStream(filename);
        
        return new DownloadFile(newFileName, in);
    }
    
    /**
     * OMS项目资源配置宅配商下载
     * @param fileName
     * @return
     */
    public static DownloadFile returnDeliverDownLoadFile(String newFileName) {// 传入参数fileName是要读取的资源文件的文件名如(file.properties)
        
        InputStream in = null;
        String filename = "deliver_template.xlsx";//订单模板名字
        in = DownloadUtil.class.getResourceAsStream(filename);
        
        return new DownloadFile(newFileName, in);
    }
    
	/**
	 * OMS商品信息
	 * @param fileName
	 * @return
	 */
	public static DownloadFile returnProductDownLoadFile(String newFileName) {// 传入参数fileName是要读取的资源文件的文件名如(file.properties)
		
		InputStream in = null;
		String filename = "product_template.xlsx";//订单模板名字
		in = DownloadUtil.class.getResourceAsStream(filename);
		
		return new DownloadFile(newFileName, in);
	}
	
	/**
	 * OMS商品PN信息
	 * @param fileName
	 * @return
	 */
	public static DownloadFile returnProductPNDownLoadFile(String newFileName) {// 传入参数fileName是要读取的资源文件的文件名如(file.properties)
		
		InputStream in = null;
		String filename = "productPN_template.xlsx";//订单模板名字
		in = DownloadUtil.class.getResourceAsStream(filename);
		
		return new DownloadFile(newFileName, in);
	}
	
	/**
	 * OMS商品PN信息
	 * @param fileName
	 * @return
	 */
	public static DownloadFile returnForwardDownLoadFile(String newFileName) {// 传入参数fileName是要读取的资源文件的文件名如(file.properties)
		
		InputStream in = null;
		String filename = "forward_template.xlsx";//转寄模板名字
		in = DownloadUtil.class.getResourceAsStream(filename);
		
		return new DownloadFile(newFileName, in);
	}
    
}

