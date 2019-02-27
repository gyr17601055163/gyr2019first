/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: TiKuController
 * Author:   gyr
 * Date:     2019/2/14 19:21
 * Description:
 */
package com.zbf.web;

import com.alibaba.fastjson.JSON;
import com.zbf.Enum.MyRedisKey;
import com.zbf.common.ResponseResult;
import com.zbf.core.CommonUtils;
import com.zbf.core.page.Page;
import com.zbf.core.page.SolrPage;
import com.zbf.core.utils.UID;
import com.zbf.entity.TiMu;
import com.zbf.oauthLogin.User;
import com.zbf.service.TiKuService;
import io.jsonwebtoken.Claims;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RequestMapping("/tiku")
@RestController
public class TiKuController {

    @Autowired
    private TiKuService tiKuService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SolrClient solrClient;

    /**
     * 新增题库功能
     *
     * @param request
     * @return
     */
    @RequestMapping("/xinZengTiKu")
    public ResponseResult xinZengTiKu(HttpServletRequest request) {

        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);

        ResponseResult responseResult = ResponseResult.getResponseResult();

        parameterMap.put("id", UID.getUUIDOrder());

        tiKuService.xinZengTiKu(parameterMap, redisTemplate);

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setError("error");
        }

        return responseResult;
    }

    /**
     * 题库列表
     *
     * @param request
     * @return
     */
    @RequestMapping("/tiKuLieBiao")
    public ResponseResult tiKuLieBiao(HttpServletRequest request) {

        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap(request);

        ResponseResult responseResult = ResponseResult.getResponseResult();

        Page<Map<String, Object>> page = new Page<>();

        Page.setPageInfo(page, parameterMap);
        page.setParams(parameterMap);

        tiKuService.tiKuLieBiao(page);

        responseResult.setResult(page);

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setError("error");
        }

        return responseResult;
    }

    /**
     * 修改题库信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/updateTiKu")
    public ResponseResult updateTiKu(HttpServletRequest request) {

        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);

        if (parameterMap.get("tikuzhuangtai").toString().equals("开放")) {
            parameterMap.put("tikuzhuangtai", "1");
        } else {
            parameterMap.put("tikuzhuangtai", "2");
        }

        ResponseResult responseResult = ResponseResult.getResponseResult();

        tiKuService.updateTiKu(parameterMap);

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setError("error");
        }

        return responseResult;
    }

    /**
     * 删除题库信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/shanchutiku")
    public ResponseResult shanchutiku(HttpServletRequest request) {

        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);

        ResponseResult responseResult = ResponseResult.getResponseResult();

        //删除数据库中的数据
        tiKuService.shanchutiku(parameterMap);

        //删除Redis中的数据
        //redisTemplate.delete(parameterMap.get("id"));
        redisTemplate.opsForHash().delete(MyRedisKey.TIKU.getKey(), parameterMap.get("id"));

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setError("error");
        }

        return responseResult;
    }


    @RequestMapping("/shiTiList")
    public ResponseResult shiTiList(HttpServletRequest request) {


        ResponseResult responseResult = ResponseResult.getResponseResult();
        return responseResult;
    }

    /**
     * 从Redis获取所有的题库信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/getTiKuInfoForRedis")
    public ResponseResult getTiKuInfoForRedis(HttpServletRequest request) {

        Map entries = redisTemplate.opsForHash().entries(MyRedisKey.TIKU.getKey());

        ResponseResult responseResult = ResponseResult.getResponseResult();

        responseResult.setResult(entries);

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setError("error");
        }

        return responseResult;
    }

    /**
     * 新增试卷功能
     *
     * @param request
     * @return
     */
    @RequestMapping("/addShiJuan")
    public ResponseResult addShiJuan(HttpServletRequest request) throws IOException, SolrServerException {

        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);

        String tikus = parameterMap.get("tikus").toString();

        Map<String, Object> tiku = (Map<String, Object>) redisTemplate.opsForHash().get(MyRedisKey.TIKU.getKey(), parameterMap.get("tikuid"));

        String tikuname = tiku.get("tikuname").toString();

        parameterMap.put("id", UID.getUUIDOrder());

        ResponseResult responseResult = ResponseResult.getResponseResult();

        /**
         * //将用户信息放进redis中
         *         redisTemplate.opsForHash ().put ( userget.getId (),"userinfo", userget);
         */
        //从redis中将用户信息取出来
        Claims claims = (Claims) request.getAttribute("userinfo");
        User userinfo = JSON.parseObject(claims.get("userinfo").toString(), User.class);
        parameterMap.put("createuserid", userinfo.getId());


        //添加数据到Solr中一份
        TiMu tiMu = new TiMu();
        tiMu.setId(parameterMap.get("id").toString());
        tiMu.setCreateuserid(parameterMap.get("createuserid").toString());
        tiMu.setUserName(userinfo.getUsername());
        tiMu.setTixingid(parameterMap.get("tixingid").toString());
        tiMu.setTikuname(tikuname);
        tiMu.setTikuid(parameterMap.get("tikuid").toString());
        tiMu.setShitizhuangtai(parameterMap.get("shitizhuangtai").toString());
        tiMu.setNanduid(parameterMap.get("nanduid").toString());
        tiMu.setLaiyuan(parameterMap.get("laiyuan").toString());
        tiMu.setTigan(parameterMap.get("tigan").toString());
        tiMu.setFenxi(parameterMap.get("fenxi").toString());
        tiMu.setDaan(parameterMap.get("daan").toString());
        tiMu.setXuanxiang(parameterMap.get("xuanxiang").toString());
        tiMu.setXuanxiangmiaoshu(parameterMap.get("xuanxiangmiaoshu").toString());
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:Ss");
        String format = sd.format(new Date());
        tiMu.setCreatetime(format);
        //添加操作
        solrClient.addBean(tiMu);
        //提交事物
        solrClient.commit();

        //添加数据到数据库中一份
        tiKuService.addShiJuan(parameterMap, redisTemplate);


        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setError("error");
        }

        return responseResult;
    }

    /**
     * 管理试卷的列表功能
     *
     * @param request
     * @return
     */
    @RequestMapping("/shiJuanLeiBiao")
    public ResponseResult shiJuanLeiBiao(HttpServletRequest request) throws IOException, SolrServerException {

        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap(request);

        ResponseResult responseResult = ResponseResult.getResponseResult();

        //创建Solr的分页工具类对象
        SolrPage<TiMu> page = new SolrPage<>();

        SolrPage.setPageInfo(page, parameterMap);

        //设置solr的查寻对象
        SolrQuery solrQuery = new SolrQuery();

        //设置查询参数
        if (parameterMap.get("tixingid") != null) {
            solrQuery.set("q", "tixingid:" + parameterMap.get("tixingid"));
        } else if (parameterMap.get("tikuid") != null) {
            solrQuery.set("q", "tikuid:" + parameterMap.get("tikuid"));
        } else if (parameterMap.get("nanduid") != null) {
            solrQuery.set("q", "nanduid:" + parameterMap.get("nanduid"));
        } /*else if (parameterMap.get("shitizhuangtai").toString().equals("0") || parameterMap.get("shitizhuangtai").toString().equals("1")) {
            solrQuery.set("q", "shitizhuangtai:" + parameterMap.get("shitizhuangtai"));
        }*/ else if (parameterMap.get("tigan") != null) {
            solrQuery.set("q", "tigan:" + parameterMap.get("tigan"));
        } else if (parameterMap.get("") != null) {

        } else {
            solrQuery.set("q", "*:*");    //查寻所有
        }

        //开启高亮
        solrQuery.setHighlight(true);
        //设置高亮前缀
        solrQuery.setHighlightSimplePre("");
        //设置高亮后缀
        solrQuery.setHighlightSimplePost("");
        //添加高亮字段
        solrQuery.addHighlightField("");

        //设置分页
        solrQuery.setStart((page.getPageNo() - 1) * page.getPageSize());
        //设置每页返回多少条
        solrQuery.setRows(page.getPageSize());


        //查询
        QueryResponse query = solrClient.query(solrQuery);
        //获取查询结果
        List<TiMu> beans = query.getBeans(TiMu.class);
        //获取高亮的数据
        Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
        //计算总记录数
        page.setTotalCount(query.getResults().getNumFound());

        //遍历高亮数据
        beans.forEach((timu) -> {
            highlighting.entrySet().forEach((entry1) -> {
                if (entry1.getKey().equals(timu.getId())) {
                    entry1.getValue().entrySet().forEach(entry2 -> {
                        if (entry2.getKey().equals("")) {
                            //goods.setGoodsName1 ( entry2.getValue ().get ( 0 ) );
                        }
                    });
                }
            });
        });


        page.setResultList(beans);

        responseResult.setResult(page);


        /*Page<Map<String, Object>> page = new Page<>();

        Page.setPageInfo(page, parameterMap);
        page.setParams(parameterMap);

        tiKuService.shiJuanLeiBiao(page);

        responseResult.setResult(page);*/

        //从Solr中查寻列表


        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setError("error");
        }

        return responseResult;
    }

}