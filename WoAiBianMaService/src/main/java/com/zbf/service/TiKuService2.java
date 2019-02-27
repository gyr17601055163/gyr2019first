/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: TiKuService2
 * Author:   gyr
 * Date:     2019/2/19 15:41
 * Description:
 */
package com.zbf.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zbf.core.page.Page;
import com.zbf.core.utils.UID;
import com.zbf.entity.TiMu;
import com.zbf.mapper.TiKuMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TiKuService2 {

    @Autowired
    private TiKuMapper mapper;

    public void addShiJuan(Map<String,Object> map){
        //将数据添加到数据库一份
        mapper.addShiJuan(map);
    }

    public List<Map<String,Object>> fenleiList(){
        return mapper.fenleiList();
    }

    /**
     * 创建试卷功能
     * @param map
     */
    @Transactional
    public void createKaoShiShiJuan(Map<String,Object> map){
        mapper.createKaoShiShiJuan(map);

        String string = map.get("checkedInfo").toString();

        //去掉string
        String substring = string.substring(1, string.length() - 1);

        String[] split = substring.split(",");

        //添加参加考试的人员
        Map<String,Object> userMap = new HashMap<>();


        userMap.put("shijuanid",map.get("id").toString());

        for (int i = 0; i < split.length; i++) {
            userMap.put("id",UID.getUUIDOrder());
            userMap.put("userid",split[i]);
            mapper.addKaoShiRenYuan(userMap);
        }


    }


    /**
     * 试卷管理功能
     * @param page
     */
    public void manageKaoShiShiJuan(Page<Map<String,Object>> page){
        List<Map<String, Object>> list = mapper.manageKaoShiShiJuan(page);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        list.forEach(x->{
            x.put("starttime",sd.format(x.get("starttime")));
            x.put("endtime",sd.format(x.get("endtime")));
            x.put("opentime",sd.format(x.get("opentime")));
            x.put("createtime",sd.format(x.get("createtime")));
        });
        page.setResultList(list);
    }

}