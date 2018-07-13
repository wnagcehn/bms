<%@page import="com.bstek.dorado.core.Configure"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Welcome</title>
</head>
<body>
<body onload="init()">
</body>
<script language="javascript">
<%
String loginPage=request.getContextPath()+Configure.getString("bdf2.formLoginUrl");
%>
function init(){
	window.document.location.href = "<%=loginPage %>";
}
</script>
</html>