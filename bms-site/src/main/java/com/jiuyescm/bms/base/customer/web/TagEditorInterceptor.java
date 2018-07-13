package com.jiuyescm.bms.base.customer.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.view.widget.base.Button;
import com.bstek.dorado.view.widget.base.Panel;
import com.bstek.dorado.view.widget.base.menu.BaseMenuItem;
import com.bstek.dorado.view.widget.base.menu.MenuItem;
import com.bstek.dorado.view.widget.base.toolbar.MenuButton;
import com.bstek.dorado.view.widget.base.toolbar.ToolBar;
import com.bstek.dorado.view.widget.layout.AnchorLayoutConstraint;
import com.bstek.dorado.view.widget.layout.AnchorMode;
import com.jiuyescm.bms.quotation.storage.entity.TestEntity;
import com.sun.mail.handlers.image_gif;

@Controller("tagEditorInterceptor")
@Component
public class TagEditorInterceptor {

	@DataProvider
	public List<TestEntity> getTags() {
		List<TestEntity> languages = new ArrayList<TestEntity>();
		TestEntity language = new TestEntity();
		language.setName("Java");
		languages.add(language);
		language = new TestEntity();
		language.setName("JavaScript");
		languages.add(language);
		language = new TestEntity();
		language.setName("Ruby");
		languages.add(language);
		language = new TestEntity();
		language.setName("Python");
		languages.add(language);
		language = new TestEntity();
		language.setName("Basic");
		languages.add(language);
		language = new TestEntity();
		language.setName("C");
		languages.add(language);
		language = new TestEntity();
		language.setName("C++");
		languages.add(language);
		language = new TestEntity();
		language.setName("C#");
		languages.add(language);
		language = new TestEntity();
		language.setName("Objective C");
		languages.add(language);
		language = new TestEntity();
		language.setName("Pascal");
		languages.add(language);
		return languages;
	}
	
	public void onViewInit(Panel panelButtons) {
        //动态设置panelButtons的标题
        panelButtons.setCaption("此标题是通过视图拦截器设置的");
 
        //动态生成8个Button控件
        for (int i = 1; i <= 8; i++) {
            Button button = new Button();
            //设置按钮的标题
            button.setCaption("Button " + i);
 
            //设置按钮的布局
            AnchorLayoutConstraint layoutConstraint = new AnchorLayoutConstraint();
            layoutConstraint.setAnchorLeft(AnchorMode.previous);
            layoutConstraint.setLeft("5");
            layoutConstraint.setTop("10");
            button.setLayoutConstraint(layoutConstraint);
 
            //给按钮添加单击事件
            button
                    .addClientEventListener(
                            "onClick",
                            new DefaultClientEvent(
                                    "dorado.MessageBox.alert('You clicked ' + self.get('caption'));"));
 
            //将按钮加入panelButtons对象中
            panelButtons.addChild(button);
        }
    }
	
	public void onViewInit1(MenuButton btnTemplateDownload) {
        //动态设置panelButtons的标题
		btnTemplateDownload.setCaption("下载模板测试");
        //动态生成8个Button控件
        for (int i = 1; i <= 3; i++) {
        	MenuItem item = new MenuItem();
            //设置按钮的标题
        	item.setCaption("Item " + i);
            //给按钮添加单击事件
        	String script = "var parameter={templetCode:'billcheck_bill_import'}; view.get('#downloadTemplateAction').set('parameter',parameter).execute();";
            item.addClientEventListener(
                            "onClick",
                            new DefaultClientEvent(script));
 
            //将按钮加入panelButtons对象中
            btnTemplateDownload.addItem(item);
        }
    }
	
}
