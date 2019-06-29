/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calcu.receive.dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <功能描述>
 * 
 * @author zhaofeng
 * @date 2019年6月21日 下午2:57:11
 */
public class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        List<Map<String, String>> newList=new ArrayList<Map<String, String>>();       
        TreeMap<Integer,List<Map<String, String>>> levelMap=new TreeMap<Integer,List<Map<String, String>>>();
            Integer i=levelMap.lastKey();
            System.out.println(i);
            newList=levelMap.get(i);
        
        System.out.println(newList.size());
    }

}


