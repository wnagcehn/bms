var JIUYE_VIEW_PRINTER = new function() {
	this.view = null;
	this.session = {
		server : "",
		contextPath : "",
		cid : ""
	};
	this.init = function(view_, session_, c) {
		this.view = view_;
		if (session_) {
			this.session = session_;
		}
	};
	this.get = function(a) {
		return this.view.get(a);
	};

	this.cid = function() {
		return this.session.cid;
	};
};


function createXMLHttpRequest(){
    //Mozilla 浏览器（将XMLHttpRequest对象作为本地浏览器对象来创建）
    if(window.XMLHttpRequest){ //Mozilla 浏览器
        xmlHttp = new XMLHttpRequest();
    }else if(window.ActiveXObject) { //IE浏览器
    //IE浏览器（将XMLHttpRequest对象作为ActiveX对象来创建）
        try{
            xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
        }catch(e){
            try {
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }catch(e){}
        }
    }
    if(xmlHttp == null){
        alert("不能创建XMLHttpRequest对象");
        return false;
    }
  }
	//用于发出异步请求的方法
function sendAsynchronRequest(url,parameter,callback){
	    createXMLHttpRequest();
	    if(parameter == null){
	        //设置一个事件处理器，当XMLHttp状态发生变化，就会出发该事件处理器，由他调用
	        //callback指定的javascript函数
	        xmlHttp.onreadystatechange = callback;
	        //设置对拂去其调用的参数（提交的方式，请求的的url，请求的类型（异步请求））
	        xmlHttp.open("GET",url,true);//true表示发出一个异步的请求。
	        xmlHttp.send(null);
	    }else{
	        xmlHttp.onreadystatechange = callback;
	        xmlHttp.open("POST",url,true);
	        xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded;");
	        xmlHttp.send(parameter);
	    }
}
