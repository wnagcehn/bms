//定义一个函数，判断参数是否为空
function isNullOrEmpty(e) {
    if (e == null || e == undefined || e == "") {
        return true;
    } else {
        return false;
    }
}

//弹出框
function alertMsg(code, message){
	if("SUCC" == code){
		dorado.MessageBox.alert(message);
	}else if("ERROR" == code){
		dorado.MessageBox.alert(message, {
		    icon: "EXCEPTION"
		});
	}else if("FAIL" == code){
		dorado.MessageBox.alert(message, {
		    icon: "WARNING"
		});
	}
}

/**
 * 加法
 * @param num1
 * @param num2
 * @returns {Number}
 */
function addNum (num1, num2) {
	 var sq1,sq2,m;
	 try {
	  sq1 = num1.toString().split(".")[1].length;
	 }
	 catch (e) {
	  sq1 = 0;
	 }
	 try {
	  sq2 = num2.toString().split(".")[1].length;
	 }
	 catch (e) {
	  sq2 = 0;
	 }
	 m = Math.pow(10,Math.max(sq1, sq2));
	 return (num1 * m + num2 * m) / m;
}

/** 
 * 减法 
 * @param arg1 
 * @param arg2 
 * @returns 
 */  
function accSub(arg1,arg2){  
    var r1,r2,m,n;  
    try{
    	r1=arg1.toString().split(".")[1].length;
    }catch(e){
    	r1=0;
    }  
    try{
    	r2=arg2.toString().split(".")[1].length;
    }catch(e){
    	r2=0;
    }  
    m=Math.pow(10,Math.max(r1,r2));  
    //动态控制精度长度  
    n=(r1>=r2)?r1:r2;  
    return ((arg1*m-arg2*m)/m).toFixed(n);  
  } 


/**
 * 计算日期之差
 */
function getDays(strDateStart,strDateEnd){
   var iDays = parseInt(Math.abs(strDateEnd - strDateStart ) / 1000 / 60 / 60 /24);
   return iDays ;
}