/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: JWTUtils
 * Author:   gyr
 * Date:     2019/1/28 9:30
 * Description:
 */
package com.zbf.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    /**
     * 生成JWT 加密信息
     * @param userinfo：信息
     * @return 加密串
     *
     *      在服务端认证完用户名和密码后，将用户信息通过JWT加密生成加密串
     *      加密串由三部分组成（中间用.隔开）：
     *          1、头部（header）
     *              该Jwt最基本的信息，例如其类型以及签名所用的算法，可以被表示成一个json对象
     *          2、载荷（playload）
     *              iss：该JWT的签发者
     *              sub：该JWT所面向的用户
     *              aud：接收该JWT的一方
     *              exp（expires）：什么时候过期，这里是一个Unix时间戳
     *              iat（issued at）：在什么时候签发的
     *          3、签名（signature）
     */
    public static String generateToken(String userinfo){
        Map<String,Object> map = new HashMap<>();
        map.put("userinfo",userinfo);   //用户信息
        map.put("created",new Date());  //创建时间

        // 返回的字符串compact便是我们的token串了
        String compact = Jwts.builder().
                        //设置加密算法  加密算法的名称              签名的键
                        signWith(SignatureAlgorithm.HS512, "secretkey").             //算法必须设置
                        //设置过期时间                返回当前时间              30分钟（1000L表示1秒 * 60表示1分钟 * 30表示30分钟）
                        setExpiration(new Date(System.currentTimeMillis()+30*60*1000L)).    //new Date(System.currentTimeMillis()+30*60*1000L)表示从当前token产生时间算起后，30分钟过期
                        //设置信息
                        setClaims(map).
                        //compact()：将全部设置完成后拼成jwt串的方法
                        compact();

        return compact; //返回token串
    }

}