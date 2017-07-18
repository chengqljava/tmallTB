package com.cheng.qian.util;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 签名工具类
 * 
 * @author eric
 * @version $Id: SignUtil.java, v 0.1 2015年3月23日 下午8:03:39 eric Exp $
 */
public abstract class SignUtil {

    private SignUtil() {
    }

    /**
     * 给请求签名。<br>
     * 算法：
     *  根据参数名称将你的所有请求参数按照字母先后顺序排序:key + value .... key + value
     *  对除签名和图片外的所有请求参数按key做的升序排列, value无需编码。
     *  例如：将foo=1,bar=2,baz=3 排序为bar=2,baz=3,foo=1
     *  参数名和参数值链接后，得到拼装字符串bar2baz3foo1
     *  将app secret 加在字符串后面 加密  encodeHex(sha1(key1value1key2value2...secret))
     * 
     * @param request
     * @param secret
     * @return
     * @throws IOException
     */
    public static String signRequest(Map<String, Object> sortedParams, String secret) {

        Set<Entry<String, Object>> paramSet = sortedParams.entrySet();
        StringBuilder query = new StringBuilder();
        //第一步
        query.append(secret);
        // 第二步：把所有参数名和参数值串在一起

        for (Entry<String, Object> param : paramSet) {
            if (StringUtils.isNotBlank(param.getKey())
                && StringUtils.isNotBlank(param.getValue().toString())) {
                query.append(param.getKey()).append(param.getValue());
            }
        }
        // 第三步：使用sha1加密
        query.append(secret);
      //  System.out.println("SECRET " + query.toString());
        String sign = Encodes.encodeHex(Digests.md5(query.toString().getBytes()));

        return sign.toUpperCase();
    }

    public static String parmsStr(Map<String, Object> params) {
        Set<Entry<String, Object>> paramSet = params.entrySet();
        StringBuilder query = new StringBuilder();
        //第一步
        // 第二步：把所有参数名和参数值串在一起
        int index = 0;
        for (Entry<String, Object> param : paramSet) {
            index++;
            if (StringUtils.isNotBlank(param.getKey())
                && StringUtils.isNotBlank(param.getValue().toString())) {
                query.append(
                    param.getKey() + "=" + param.getValue() + (index < params.size() ? "&" : ""));
            }
        }
        return query.toString();
    }

}
