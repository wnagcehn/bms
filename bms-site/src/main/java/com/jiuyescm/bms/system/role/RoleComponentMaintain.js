/** @Controller */

// @Bind @dataTypeUrl.#children.beforeLoadData
!function(self, arg) {
	var id=view.id("dataSetRole").getData("#.id");
	self.set("parameter",{parentId:arg.entity.get("id"),roleId:id});
}

// @Bind @dataTypeUrl.#components.beforeLoadData
!function(self, arg) {
	var ds=view.id("dataSetRole");
	var roleId=ds.getData("#.id");
	var url=arg.entity.get("url");
	var urlId=arg.entity.get("id");
	self.set("parameter",{viewName:url,urlId:urlId,roleId:roleId});
}

// @Bind #dataSetRole.onLoadData
!function(self, arg) {
	if (self.getData().entityCount==0){
		dorado.MessageBox.alert("请先进行系统的角色维护,有角色的前提下才能控件设置权限!");
	}
}
//@Bind #checkBoxAutoRefreshCache.onReady
!function(self, arg) {
	self.set("value", true);
}

// @Bind #buttonSave.onClick
!function(self, arg) {
	var action=view.id("ajaxActionInsertUrlComponent");
	var ds=view.id("dataSetRole");
	var roleId=ds.getData("#.id");
	var urlId=ds.getData("!currentUrl.id");
	
	var tree=view.id("dataTreeComponents");
	var rootNode=tree.get("root");
	var components=[];
	buildSelectedNodeId(rootNode,components);
	action.set("parameter",{components:components,urlId:urlId,roleId:roleId});
	action.execute();
	function buildSelectedNodeId(parentNode,components){
		var nodes=parentNode.get("nodes");
		if(!nodes){
			return;
		}
		nodes.each(function(node){
			var data=node.get("data");
			if(node.get("checked")){
				components.push({id:data.get("id"),authorityType:data.get("authorityType")});
			}
			buildSelectedNodeId(node,components);
		});
	}
}

// @Bind #dataTreeComponents.onDataNodeCreate
!function(self, arg) {
	var data=arg.data;
	var enabled=data.get("enabled");
	if(enabled){
		arg.node.set("checkable",true);
	}else{
		arg.node.set("checkable",false);
	}
}

// @Bind #dataTreeComponents.onRenderNode
!function(self, arg) {
	var data=arg.node.get("data");
	var id=data.get("id");
	var name=data.get("name");
	var use=data.get("use");
	if(id){
		if(use){
			arg.dom.innerHTML="<strong><font color='green'>"+id+"["+name+"]</font></strong>";
		}else{
			arg.dom.innerHTML=id+"["+name+"]";
		}
	}else{
		if(use){
			arg.dom.innerHTML="<strong><font color='green'>["+name+"]</font></strong>";
		}else{
			arg.dom.innerHTML="["+name+"]";
		}
	}
}

// @Bind #dataTreeUrl.onRenderNode
!function(self, arg) {
	if(arg.node.get("data").get("use")){
		arg.dom.innerHTML="<strong><font color='green'>"+arg.label+"</font></strong>";
	}else{
		arg.dom.innerHTML=arg.label;
	}
}

