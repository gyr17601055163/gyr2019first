/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: TiKuService
 * Author:   gyr
 * Date:     2019/2/14 20:36
 * Description:
 */
package com.zbf.service;

import com.zbf.Enum.MyRedisKey;
import com.zbf.core.page.Page;
import com.zbf.mapper.TiKuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Component
public class TiKuService {

    @Autowired
    private TiKuMapper tiKuMapper;

    @Transactional
    public int xinZengTiKu(Map<String, Object> map, RedisTemplate redisTemplate) {
        //添加到redis中(需要使用Hash类型)
        //redisTemplate.opsForList().rightPush(MyRedisKey.TIKU.getKey(), map);     //rightPush(key,添加的数据)从右边添加
        redisTemplate.opsForHash().put(MyRedisKey.TIKU.getKey(),map.get("id"),map);

        //添加到数据库
        return tiKuMapper.xinZengTiKu(map);
    }

    public int updateTiKu(Map<String, Object> map) {
        return tiKuMapper.updateTiKu(map);
    }

    public int shanchutiku(Map<String, Object> map) {
        return tiKuMapper.shanchutiku(map);
    }

    public void tiKuLieBiao(Page<Map<String, Object>> page) {
        List<Map<String, Object>> list = tiKuMapper.tiKuLieBiao(page);

        list.forEach(tk -> {
            if (tk.get("tikuzhuangtai").toString().equals("1")) {
                tk.put("tikuzhuangtai", "开放");
            } else {
                tk.put("tikuzhuangtai", "关闭");
            }
        });

        page.setResultList(list);
    }


    /**
     * 新增试卷
     *
     * @param map
     * @return
     */
    public int addShiJuan(Map<String, Object> map,RedisTemplate redisTemplate) {
        //将数据添加到Solr中一份


        //将数据添加到数据库一份
        return tiKuMapper.addShiJuan(map);
    }

    public void shiJuanLeiBiao(Page<Map<String, Object>> page) {
        List<Map<String, Object>> list = tiKuMapper.shiJuanLeiBiao(page);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        list.forEach(x->{
            x.put("createtime",sd.format(x.get("createtime")));
        });

        page.setResultList(list);
    }
}