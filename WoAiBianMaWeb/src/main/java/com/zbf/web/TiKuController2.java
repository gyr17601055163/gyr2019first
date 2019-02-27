/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: TiKuController2
 * Author:   gyr
 * Date:     2019/2/19 14:01
 * Description:
 */
package com.zbf.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zbf.Enum.MyRedisKey;
import com.zbf.common.ResponseResult;
import com.zbf.core.CommonUtils;
import com.zbf.core.page.Page;
import com.zbf.core.page.SolrPage;
import com.zbf.core.utils.FileUploadDownUtils;
import com.zbf.core.utils.UID;
import com.zbf.entity.TiMu;
import com.zbf.mapper.TiKuMapper;
import com.zbf.oauthLogin.User;
import com.zbf.service.TiKuService;
import com.zbf.service.TiKuService2;
import io.jsonwebtoken.Claims;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.awt.geom.AreaOp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 题库2控制层： 不知道服务端代码版本更新，去做的上传代码
 */
@RestController
@RequestMapping("tiku2")
public class TiKuController2 {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TiKuService2 tiKuService2;
    @Autowired
    private SolrClient solrClient;
    @Autowired
    private TiKuMapper tiKuMapper;

    /**
     * 从Redis中获取所有的题库
     *
     * @return
     */
    @RequestMapping("/getTiKusToRedis")
    public ResponseResult getTiKusToRedis() {

        Map titus = redisTemplate.opsForHash().entries(MyRedisKey.TIKU.getKey());

        ResponseResult responseResult = new ResponseResult();

        responseResult.setResult(titus);

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            responseResult.setError("error");
        }

        return responseResult;
    }


    /**
     * 添加试题功能
     *
     * @param request
     * @return
     */
    @RequestMapping("addShiJuan")
    public ResponseResult addShiJuan(HttpServletRequest request) throws IOException, SolrServerException {

        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);

        parameterMap.put("id", UID.getUUIDOrder());
        Claims claims = (Claims) request.getAttribute("userinfo");
        User userinfo = JSON.parseObject(claims.get("userinfo").toString(), User.class);
        parameterMap.put("createuserid", userinfo.getId());

        //将数据添加到solr中一份
        TiMu tiMu = new TiMu();
        tiMu.setId(parameterMap.get("id").toString());
        tiMu.setUserName(userinfo.getUsername());
        tiMu.setCreateuserid(userinfo.getId().toString());
        Map<String, Object> tikus = (Map<String, Object>) redisTemplate.opsForHash().get(MyRedisKey.TIKU.getKey(), parameterMap.get("tikuid"));
        tiMu.setTikuname(tikus.get("tikuname").toString());
        tiMu.setTikuid(tikus.get("id").toString());
        tiMu.setTixingid(parameterMap.get("tixingname").toString());
        if (parameterMap.get("shitizhuangtai").toString().equals("0")) {
            tiMu.setShitizhuangtai("关闭");
        } else {
            tiMu.setShitizhuangtai("打开");
        }
        tiMu.setNanduid(parameterMap.get("nanduname").toString());
        tiMu.setLaiyuan(parameterMap.get("laiyuan").toString());
        tiMu.setTigan(parameterMap.get("tigan").toString());
        tiMu.setFenxi(parameterMap.get("fenxi").toString());
        tiMu.setDaan(parameterMap.get("daan").toString());
        tiMu.setXuanxiang(parameterMap.get("xuanxiang").toString());
        tiMu.setXuanxiangmiaoshu(parameterMap.get("xuanxiangmiaoshu").toString());
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        tiMu.setCreatetime(sd.format(new Date()));
        solrClient.addBean(tiMu);
        solrClient.commit();

        //将数据添加到数据库一份
        tiKuService2.addShiJuan(parameterMap);

        ResponseResult responseResult = new ResponseResult();

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            responseResult.setError("error");
        }

        return responseResult;
    }


    /**
     * 从solr中获取试题（分页+模糊+高亮+列表）
     *
     * @param request
     * @return
     */
    @RequestMapping("/show")
    public ResponseResult show(HttpServletRequest request) throws IOException, SolrServerException {

        ResponseResult responseResult = new ResponseResult();

        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap(request);

        //创建Solr的分页工具类对象
        SolrPage<TiMu> page = new SolrPage<>();

        SolrPage.setPageInfo(page, parameterMap);

        //设置solr的查寻对象
        SolrQuery solrQuery = new SolrQuery();

        //设置查询参数
        if (parameterMap.containsKey("tixingid")) {
            if (parameterMap.get("tixingid").toString() != null && !parameterMap.get("tixingid").toString().equals("")) {
                solrQuery.set("q", "tixingid:" + parameterMap.get("tixingid"));
            }
        } else if (parameterMap.containsKey("tikuid")) {
            if (parameterMap.get("tikuid").toString() != null && !parameterMap.get("tikuid").toString().equals("")) {
                solrQuery.set("q", "tikuname:" + parameterMap.get("tikuid"));
            }
        } else if (parameterMap.containsKey("nanduid")) {
            if (parameterMap.get("nanduid").toString() != null && !parameterMap.get("nanduid").toString().equals("")) {
                solrQuery.set("q", "nanduid:" + parameterMap.get("nanduid"));
            }
        } else if (parameterMap.containsKey("tigan")) {
            if (parameterMap.get("tigan").toString() != null && !parameterMap.get("tigan").toString().equals("")) {
                solrQuery.set("q", "tigan:" + parameterMap.get("tigan"));
            }
        } else if (parameterMap.containsKey("shitizhuangtai")) {
            if (parameterMap.get("shitizhuangtai").toString() != null && !parameterMap.get("shitizhuangtai").toString().equals("")) {
                solrQuery.set("q", "shitizhuangtai:" + parameterMap.get("shitizhuangtai"));
            }
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

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            responseResult.setError("error");
        }

        return responseResult;
    }


    /**
     * 批量导入试卷功能
     *
     * @param file：上传的文件内容
     * @param request：上传文件时提交的附带数据
     * @return
     */
    @RequestMapping("/toImportExcelData")
    public ResponseResult toImportExcelData(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {

        /**
         * 第一部分：文件上传
         */
        /**
         * 第二总分：对Excel的解析
         */

        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);
        //获取文件输入流，将文件数据输入到虚拟机当中，只有输入到虚拟机当中，才会被读取处理，因为java程序都是在虚拟机中运行的
        InputStream inputStream = file.getInputStream();        //这里会抛出一个io流

        //获取上传文件的文件名
        String originalFilename = file.getOriginalFilename();   //如果想要判断兼容性的话，可以根据这个文件名进行判断，如果是.xls结尾的文件，创建工作簿时，使用HSSFWorkbook,如果是.xlsx结尾的文件，创建工作簿时，使用XSSFWorkbook

        //创建一个工件簿,传入文件内容
        Workbook workbook = null;

        //判断兼容性
        if (originalFilename.endsWith(".xls")) {  //兼容Excel2003
            workbook = new HSSFWorkbook(inputStream);
        }else {
            workbook = new XSSFWorkbook(inputStream);
        }

        //读取文件数据（默认情况下，数据都是放在第一个Sheet，也就是第一个工作表中的，而工作表在Excel中描述的时候是一个数据，所以读取的时候，取数组的第一个就行）
        Sheet sheetAt = workbook.getSheetAt(0);     //工作簿

        //获取工作簿中的数据的物理行（作用是避免读取空行）
        int numberOfRows = sheetAt.getPhysicalNumberOfRows();   //获取数据的行数（数据的第一行是标题，第二行开始才是数据）

        //创建一个List集合，用于存放第一行的数据
        List<Map<String,Object>> rowList = new ArrayList<>();
        Map<String,Object> rowMap = null;

        //处理表头
        Row header = sheetAt.getRow(0);
        Cell cell = header.getCell(0);
        String stringCellValue = cell.getStringCellValue();

        //遍历每行的数据
        for(int i = 1; i < numberOfRows; i++){  //从第二行开始遍历
            //读取每一行的数据
            Row row = sheetAt.getRow(i);

            //获取每行的有多少个单元格
            int number = row.getPhysicalNumberOfCells();

            //创建一个Map对象，将每行中每个单元格的数据存入进来
            rowMap = new HashMap<>();   //每行的数据

            //遍历每行中每个单元格的数据
            for(int j = 0; j < number; j++){
                //题干-->
                rowMap.put("tigan",row.getCell(0).getStringCellValue());            //1
                //选项标号-->
                rowMap.put("xuanxiangbianhao",row.getCell(1).getStringCellValue()); //2

                JSONArray objects = JSON.parseArray(row.getCell(1).getStringCellValue());

                //选项描述【A,B,C,D...的】-->
                ArrayList<String> xuanxiangmiaoshuList = new ArrayList<>();     //用于存放第个单元格的数据

                //动态的去获取有多少个选项
                for(int k = 2; k < objects.size()+2; k++){
                    xuanxiangmiaoshuList.add(row.getCell(k).getStringCellValue());
                }
                String xuanxiangmiaoshu = JSON.toJSONString(xuanxiangmiaoshuList);
                rowMap.put("xuanxiangmiaoshu",xuanxiangmiaoshu);                    //3、4、5、6
                //正确答案-->
                rowMap.put("daan",row.getCell(6).getStringCellValue());             //7
                //解析-->
                //因为解析很有可能是空，所以判断一下
                if(row.getCell(7) != null){
                    rowMap.put("timujiexi",row.getCell(7).getStringCellValue());    //8
                }
            }

            //将遍历完的每一行的数据存入list集合中
            rowList.add(rowMap);

        }

        //将rowList存入Solr中一份，存入数据库中一份
        System.out.print(JSON.toJSONString(rowList));





        ResponseResult responseResult = new ResponseResult();

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            responseResult.setError("error");
        }

        return responseResult;
    }


    /**
     * 下载Excel模板
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("downloadExcel")
    public ResponseResult downloadExcel(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {

        //调用下载工具类进行下载
        File excelTemplate = FileUploadDownUtils.getExcelTemplate("exceltemplate/timu.xlsx");

        FileUploadDownUtils.responseFileBuilder(response,excelTemplate,"试题模板【timu】.xlsx");



        ResponseResult responseResult = new ResponseResult();

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            responseResult.setError("error");
        }

        return responseResult;

    }


    /**
     * 题库导出功能（从数据库中导出题库，导出格式为Excel格式）
     * @param request
     * @return
     */
    @RequestMapping("exportExcelData")
    public ResponseResult exportExcelData(HttpServletRequest request,HttpServletResponse response ) throws IOException {

        /**
         * 第一部分：从数据库中查寻数据
         */

        //获取客户端提交过来的数据（题库id）
        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);

        Page<Map<String,Object>> page = new Page<>();

        //可以设置导出数据的条数，比如一次最多可以导出30条数据
        page.setPageSize(30);

        //将查寻条件题库id存入page对象中
        page.setParams(parameterMap);

        //根据题库id查寻试题
        List<Map<String, Object>> tiKuShiTiByTiKuId = tiKuMapper.getTiKuShiTiByTiKuId(page);

        /**
         * 第二部分：对poi的API进行操作，将数据放到Excel表格中
         */

        //创建工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();

        //创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet("大数据题库");//给工作簿起个名字

        //设置表头
        XSSFRow headerrow = sheet.createRow(0);

        Map<String, Object> stringObjectMap = tiKuShiTiByTiKuId.get(0);

        Set<String> strings = stringObjectMap.keySet();

        for(String string:strings){
            if(string.equals(strings)){
                headerrow.createCell(0).setCellValue("题库创建时间");
            }
            if(string.equals("daan")){
                headerrow.createCell(1).setCellValue("试题答案");
            }
            if(string.equals("xuanxiang")){
                headerrow.createCell(2).setCellValue("选项编号");
            }
            if(string.equals("tigan")){
                headerrow.createCell(3).setCellValue("题干");
            }
            if(string.equals("fenxi")){
                headerrow.createCell(4).setCellValue("试题分析");
            }
            if(string.equals("tixingid")){
                headerrow.createCell(5).setCellValue("试题类型");
            }
            if(string.equals("shitizhuangtai")){
                headerrow.createCell(6).setCellValue("试题状态");
            }
            if(strings.equals("tikuid")){
                headerrow.createCell(7).setCellValue("所属题库ID");
            }
            if(string.equals("nanduid")){
                headerrow.createCell(8).setCellValue("试题难易程度");
            }
            if(string.equals("createuserid")){
                headerrow.createCell(9).setCellValue("创建人ID");
            }
            if(string.equals("laiyuan")){
                headerrow.createCell(10).setCellValue("试题来源");
            }
            if(string.equals("id")){
                headerrow.createCell(11).setCellValue("试题ID");
            }
        }


        for (int i = 0; i < tiKuShiTiByTiKuId.size(); i++) {

            //获取每一行的数据
            Map<String, Object> datamap = tiKuShiTiByTiKuId.get(i);

            //在excel工作表中创建一个行
            XSSFRow row = sheet.createRow(i+1);

            //获取每一行的数据的key的个数，就知道需要创建多少个单元格
            List<String> collect = datamap.keySet().stream().collect(Collectors.toList());  //每个单元格的key

            for (int j = 0; j < collect.size(); j++) {

                XSSFCell cell = row.createCell(j); //创建每行的单元格

                cell.setCellValue(datamap.get(collect.get(j))!=null?datamap.get(collect.get(j)).toString():"");      //通过key获得key对应的value
            }

        }

        //输出工作簿===========
        //设置响应头
        String filename=new String("【实例】信息表.xlsx".getBytes (),"ISO8859-1"); //给工作簿起个名
        response.setContentType ( "application/octet-stream;charset=ISO8859-1" );       //设置内容的类型
        response.setHeader("Content-Disposition", "attachment;filename="+filename);

        //输出工作簿
        xssfWorkbook.write ( response.getOutputStream () );



        ResponseResult responseResult = new ResponseResult();

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            responseResult.setError("error");
        }

        return responseResult;
    }

    /**
     * 试卷分类列表
     */
    @RequestMapping("getfenleiList")
    public ResponseResult getfenleiList(){
        List<Map<String, Object>> list = tiKuService2.fenleiList();

        ResponseResult responseResult = new ResponseResult();

        responseResult.setResult(list);

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            responseResult.setError("error");
        }

        return responseResult;
    }


    /**
     * 创建试卷
     * @param request
     * @return
     */
    @RequestMapping("createKaoShiShiJuan")
    public ResponseResult createKaoShiShiJuan(HttpServletRequest request){

        //获取客户端发送过来的数据
        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);

        parameterMap.put("id",UID.getUUIDOrder());

        //从redis中将用户信息取出来
        Claims claims = (Claims) request.getAttribute("userinfo");
        User userinfo = JSON.parseObject(claims.get("userinfo").toString(), User.class);
        parameterMap.put("chuangjianren",userinfo.getUsername());

        //添加创建试卷表
        tiKuService2.createKaoShiShiJuan(parameterMap);

        //判断试卷类型是变通试卷还是随机试卷，如果是普通试卷，在客户端手动的往试卷中添加题目，如果是随机试卷，在这里随机的生成试卷。
        if(parameterMap.get("shijuanleixing").toString().equals("1")){  //则创建的空试卷的类型就是随机试卷
            //开始随机的生成题目。

        }

        ResponseResult responseResult = new ResponseResult();


        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            responseResult.setError("error");
        }

        return responseResult;
    }


    /**
     * 试卷管理功能
     * @param request
     * @return
     */
    @RequestMapping("/manageKaoShiShiJuan")
    public ResponseResult manageKaoShiShiJuan(HttpServletRequest request){

        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);

        Page<Map<String,Object>> page = new Page<>();

        Page.setPageInfo(page,parameterMap);

        page.setParams(parameterMap);

        tiKuService2.manageKaoShiShiJuan(page);

        ResponseResult responseResult = new ResponseResult();

        responseResult.setResult(page);

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            responseResult.setError("error");
        }

        return responseResult;
    }


    /**
     * 生成随机试卷
     * @return
     */
    @RequestMapping("/getRandomTiMu")
    public ResponseResult getRandomTiMu(HttpServletRequest request) throws IOException, SolrServerException {

        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);

        //设置查询条件
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.add("q","*:*");

        //设置返回的数据量，相当于pageSize
        Integer pageSize = Integer.valueOf(parameterMap.get("pageSize").toString());
        solrQuery.setRows(pageSize);

        //设置查询的下标，相当于（PageNo-1）*PageSize
        Integer pageNo = Integer.valueOf(parameterMap.get("pageNo").toString());
        solrQuery.setStart((pageNo-1)*pageSize);

        //配置随机获取数据-加上随机数
        int num = (int) (Math.random() * 100000);
        solrQuery.addSort(""+num,SolrQuery.ORDER.desc);     //配置随机排序，获取随机数据

        //配置随机获取数据
        QueryResponse query = solrClient.query(solrQuery);

        //获取查出的结果的总条数
        long numFound = query.getResults().getNumFound();

        ResponseResult responseResult = new ResponseResult();

        try {
            responseResult.setSuccess("ok");
        } catch (Exception e) {
            responseResult.setError("error");
        }

        return responseResult;
    }


}