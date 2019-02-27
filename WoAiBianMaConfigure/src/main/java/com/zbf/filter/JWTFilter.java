/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: JWTFilter
 * Author:   gyr
 * Date:     2019/1/28 13:51
 * Description:
 */
package com.zbf.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbf.jwt.JWTUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT的过滤器：JWTFilter
 *      目的是：解决JWT的token的过期问题
 *      使用方式：
 *          设置配置类加载JWT的过滤器即可
 */
public class JWTFilter extends GenericFilterBean {

    //redisTemplate：主要用于退出登录
    private RedisTemplate redisTemplate;

    public JWTFilter(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    //符合该拦截器的所有拦截规则的请求路径（/url），都会进入该方法进行判断，判断是否放行
    @Override           //定义为final意为不可变
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain chain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        /**
         * 第一步：判断http请求类型，
         *      1、OPTIONS类型的请求：
         *          ① 直接返回状态码200，
         *          ② 放行（OPTIONS性质的请求是探测性质的请求）
         *      2、OPTIONS以外的其他请求都要经过JWT验证，判断以后才决定是否放行
         */
        //1、OPTIONS类型的请求：
        if("OPTIONS".equals(request.getMethod())){
            // ① 直接返回状态码200，
            response.setStatus(HttpServletResponse.SC_OK);      //HttpServletResponse.SC_OK = 200  200是响应正常的意思
            // ② 放行（OPTIONS性质的请求是探测性质的请求）
            chain.doFilter(request,response);
        }else {//2、OPTIONS以外的其他请求都要经过JWT验证，判断以后才决定是否放行

            /**
             * 第二步：获取VUE请求拦截器中存入请求头中的token
             *      1、从request请求头中获取Authorization（因为在请求拦截器中，token被存在Authorization中）
             *      2、获取token
             */
            //1、从request请求头中获取Authorization（因为在请求拦截器中，token被存在Authorization中）
            final String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(0);

            /**
             * 第三步：判断token是否过期
             *      1、token没有过期（对token进行解密，能解密出来说明没有过期，解密不出来，说明过期）
             *          ① 使用JWT对token进行解密
             *      2、token过期：会抛出一个JWT自定义的过期异常，catch会捕捉到
             */
            try { //1、token没有过期

                //① 使用JWT对token进行解密
                Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();   ////（本行是jwt的API）Claims是key，value的格式，类型于Map的格式
                //将claims存入request使用域
                request.setAttribute("userinfo",claims);
                //Claims claims1111 = (Claims) request.getAttribute("userinfo");
                //获取用户信息
                String user = claims.get("userinfo").toString();    //userinfo是一个json串
                //将json串转换成json对象
                JSONObject userinfo = JSON.parseObject(user);
                //获取用户id
                Long userid = userinfo.getLong("id");

                //从Redis中通过用户id取出用户信息
                Object userinfo1 = redisTemplate.opsForHash().get(userid, "userinfo");
                if(userinfo1 != null){
                    /**
                     * 第四步：刷新token
                     *      重新将user信息通过JWTUtils工具类加密并保存到响应头中
                     */
                    //重新将user信息通过JWTUtils工具类加密并保存到响应头中
                    response.setHeader("authorization", JWTUtils.generateToken(claims.get("userinfo").toString()));
                    response.setHeader("Access-Control-Expose-Headers","authorization");

                    //操作完成，放行
                    chain.doFilter(request,response);
                }else {
                    throw new RuntimeException("token过期，登录失败，请重新登录");
                }

            }catch (ExpiredJwtException e){ //2、token过期：会抛出一个JWT自定义的过期异常，catch会捕捉到
                e.printStackTrace();
                throw new RuntimeException("token过期，登录失败，请重新登录");
            }

        }
    }
}