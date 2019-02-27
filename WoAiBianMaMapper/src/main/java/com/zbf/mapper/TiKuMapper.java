/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: TiKuMapper
 * Author:   gyr
 * Date:     2019/2/14 20:38
 * Description:
 * History:
 */
package com.zbf.mapper;

import com.zbf.core.page.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TiKuMapper {

    public int xinZengTiKu(Map<String,Object> map);

    public int updateTiKu(Map<String,Object> map);

    public int shanchutiku(Map<String,Object> map);

    public List<Map<String,Object>> tiKuLieBiao(Page<Map<String,Object>> page);

    /**
     * 题库导出
     * @param page
     * @return
     */
    public List<Map<String,Object>> getTiKuShiTiByTiKuId(Page<Map<String,Object>> page);

    /**
     * 新增试卷
     * @param map
     * @return
     */
    public int addShiJuan(Map<String,Object> map);

    public List<Map<String,Object>> shiJuanLeiBiao(Page<Map<String,Object>> page);

    /**
     * 试卷分类列表
     * @return
     */
    public List<Map<String,Object>> fenleiList();

    /**
     * 创建试卷功能
     * @param map
     * @return
     */
    public int createKaoShiShiJuan(Map<String,Object> map);

    /**
     * 添加参加考试的人员
     * @param map
     * @return
     */
    public int addKaoShiRenYuan(Map<String,Object> map);

    /**
     * 试卷管理功能
     * @param page
     * @return
     */
    public List<Map<String,Object>> manageKaoShiShiJuan(Page<Map<String,Object>> page);
}