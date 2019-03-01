/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: TiMu
 * Author:   gyr
 * Date:     2019/2/18 19:56
 * Description:
 */
package com.zbf.entity;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
//品味
@Data
public class TiMu {

    @Field
    private String id;
    @Field
    private String ids;
    @Field
    private String userName;
    @Field
    private String tikuname;
    @Field
    private String createuserid;
    @Field
    private String tixingid;
    @Field
    private String tikuid;
    @Field
    private String shitizhuangtai;
    @Field
    private String nanduid;
    @Field
    private String laiyuan;
    @Field
    private String tigan;
    @Field
    private String fenxi;
    @Field
    private String daan;
    @Field
    private String xuanxiang;
    @Field
    private String xuanxiangmiaoshu;
    @Field
    private String createtime;

}