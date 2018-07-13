package com.jiuyescm.bms.base.system;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.core.Configure;


public class MenuInterceptor extends HandlerInterceptorAdapter  {

	@SuppressWarnings("rawtypes")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			
		//“bdf2.formLoginUrl”是从configure.properties文件 配置的值
		String  loginUrl = Configure.getString("bdf2.formLoginUrl");
		//判断是否请求登录页面
		if (request.getRequestURI().contains(loginUrl)){
			//判断用户是否存在，如果不存在，清空所有session值，否则 ，跳转到主业务页面
			IUser user = ContextHolder.getLoginUser();
			if (user == null || StringUtils.isEmpty(user.getUsername())){ 
				Enumeration em = request.getSession().getAttributeNames();
				while (em.hasMoreElements()) {
					request.getSession().removeAttribute(em.nextElement().toString());
				}
			} else {   
				//“bdf2.loginSuccessDefaultTargetUrl”是从configure.properties文件 配置的值
				String path = request.getContextPath()+Configure.getString("bdf2.loginSuccessDefaultTargetUrl"); 
				response.sendRedirect(path);
			}
		} else {
			//判断用户是否存在，如果不存在，跳转到登录页面，否则 ，校验是否有权限访问地址
			IUser user = ContextHolder.getLoginUser();
			if (user == null || StringUtils.isEmpty(user.getUsername())){
				String path = request.getContextPath()+loginUrl;
				response.sendRedirect(path);
			} else { 
			
				String url = request.getRequestURI(); 
				
				if (url.contains(".d")){
					UserAuthentication authentication = new UserAuthentication(user);
					authentication.setAuthenticated(true);
					String[] urls = url.split("/");
					if (urls.length <= 0){ 
						throw new Exception("URL异常，url："+ url);
					}
					//取出尾部地址
					url = urls[urls.length -1];
					
					SecurityUtils.checkUrl(authentication, url);
				} 
			}
		}
		
		return true; 
	}
}
