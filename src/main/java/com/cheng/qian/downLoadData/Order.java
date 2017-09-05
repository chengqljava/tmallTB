package com.cheng.qian.downLoadData;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.joda.time.DateTime;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheng.qian.model.GoodsIdOuterIdSpec;
import com.cheng.qian.util.ExcelUtil;
import com.cheng.qian.util.FileUtil;
import com.cheng.qian.util.SignUtil;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

public class Order {
    //mall_id 110937 secret 1308706231

    private List<String> orderSNs = new ArrayList<String>();

    public void orderList(String mallId, String secret, String orderStatus, int pageNumer) {
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("mall_id", mallId);
        params.put("type", "pdd.order.number.list.get");
        params.put("order_status", orderStatus);
        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("data_type", "JSON");
        params.put("page", pageNumer);
        //total_count
        //两页
        //先加密
        String sign = SignUtil.signRequest(params, secret);
        params.put("sign", sign);
        //参数序列
        // String paramSer = SignUtil.parmsStr(params);
        // System.out.println(paramSer);
        HttpRequest httpRequest = HttpRequest.post("http://open.yangkeduo.com/api/router")
            .contentType("application/x-www-form-urlencoded; charset=UTF-8").form(params);
        HttpResponse httpResponse = httpRequest.send();
        //获取内空转JSON
        JSONObject jsonObject = JSONObject.parseObject(httpResponse.bodyText());
        //获取信息
        JSONObject order_sn_list_get_response = jsonObject
            .getJSONObject("order_sn_list_get_response");
        //获取列表
        JSONArray order_sn_list = order_sn_list_get_response.getJSONArray("order_sn_list");
        //获取总数
        int total_count = order_sn_list_get_response.getIntValue("total_count");
        //具体定
        JSONObject orderSN = null;
        if (order_sn_list != null && order_sn_list.size() > 0) {
            for (int i = 0; i < order_sn_list.size(); i++) {
                orderSN = order_sn_list.getJSONObject(i);
                orderSNs.add(orderSN.getString("order_sn"));
            }

        }
        if (total_count > (pageNumer * 100)) {
            orderList(mallId, secret, orderStatus, pageNumer++);
        }
        // System.out.println(JSONObject.toJSONString(orderSNs));

    }

    public Map<String, Map<String, GoodsIdOuterIdSpec>> orderDetail(String mallId, String secret,
                                                                    List<String> orderSNs,
                                                                    List<String> notContain) {
        Map<String, Map<String, GoodsIdOuterIdSpec>> codeColorSize = null;
        if (orderSNs != null && orderSNs.size() > 0) {
            Map<String, Object> params = null;
            Map<String, GoodsIdOuterIdSpec> goodsIdOuterIdSpecMap = null;
            HttpRequest httpRequest = null;
            HttpResponse httpResponse = null;
            JSONObject jsonObject = null;
            JSONObject order_info_get_response = null;
            JSONObject order_info = null;
            JSONArray item_list = null;
            JSONObject item = null;
            String goodsIdOuterIdSpec = null;
            String outerIdSpec = null;
            int goods_count = 0;
            GoodsIdOuterIdSpec idOuterIdSpec = null;
            codeColorSize = new HashMap<String, Map<String, GoodsIdOuterIdSpec>>();
            for (String orderSN : orderSNs) {
                params = new TreeMap<String, Object>();
                params.put("mall_id", mallId);
                params.put("type", "pdd.order.information.get");
                params.put("timestamp", System.currentTimeMillis());
                params.put("data_type", "JSON");
                params.put("order_sn", orderSN);
                //先加密
                String sign = SignUtil.signRequest(params, secret);
                params.put("sign", sign);
                httpRequest = HttpRequest.post("http://open.yangkeduo.com/api/router")
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8").form(params);
                httpResponse = httpRequest.send();
                System.out.println(httpResponse.bodyText());
                //获取内空转JSON
                jsonObject = JSONObject.parseObject(httpResponse.bodyText());
                order_info_get_response = jsonObject.getJSONObject("order_info_get_response");
                order_info = order_info_get_response.getJSONObject("order_info");
                if (notContain != null
                    && notContain.contains(order_info.getString("receiver_phone"))) {
                    continue;
                }

                item_list = order_info.getJSONArray("item_list");
                for (int i = 0; i < item_list.size(); i++) {
                    item = item_list.getJSONObject(i);
                    goodsIdOuterIdSpec = "商品ID_" + item.getString("goods_id");
                    outerIdSpec = "商家编码_" + item.getString("outer_id") + "_尺码_"
                                  + item.getString("goods_spec");
                    goods_count = item.getIntValue("goods_count");
                    if (codeColorSize.containsKey(goodsIdOuterIdSpec)) {
                        if (codeColorSize.get(goodsIdOuterIdSpec).containsKey(outerIdSpec)) {
                            idOuterIdSpec = codeColorSize.get(goodsIdOuterIdSpec).get(outerIdSpec);
                            idOuterIdSpec
                                .setGoodsCount(idOuterIdSpec.getGoodsCount() + goods_count);
                        } else {
                            idOuterIdSpec = new GoodsIdOuterIdSpec();
                            idOuterIdSpec.setGoodsId(item.getString("goods_id"));
                            idOuterIdSpec.setGoodsSpec(item.getString("goods_spec"));
                            idOuterIdSpec.setOuterId(item.getString("outer_id"));
                            idOuterIdSpec.setGoodsCount(goods_count);
                            codeColorSize.get(goodsIdOuterIdSpec).put(outerIdSpec, idOuterIdSpec);
                        }

                    } else {
                        goodsIdOuterIdSpecMap = new TreeMap<String, GoodsIdOuterIdSpec>();
                        idOuterIdSpec = new GoodsIdOuterIdSpec();
                        idOuterIdSpec.setGoodsId(item.getString("goods_id"));
                        idOuterIdSpec.setGoodsSpec(item.getString("goods_spec"));
                        idOuterIdSpec.setOuterId(item.getString("outer_id"));
                        idOuterIdSpec.setGoodsCount(goods_count);
                        goodsIdOuterIdSpecMap.put(outerIdSpec, idOuterIdSpec);
                        codeColorSize.put(goodsIdOuterIdSpec, goodsIdOuterIdSpecMap);
                    }

                }

            }
        }

        //  System.out.println(JSONObject.toJSONString(codeColorSize));
        return codeColorSize;
    }

    public List<String> getOrderSNs() {
        return orderSNs;
    }

    public void setOrderSNs(List<String> orderSNs) {
        this.orderSNs = orderSNs;
    }

    public static void main(String[] args) {
        Order order = new Order();
        order.orderList("110937", "1308706231", "1", 1);
        Map<String, Map<String, GoodsIdOuterIdSpec>> map = order.orderDetail("110937", "1308706231",
            order.getOrderSNs(), null);
        StringBuffer strBuffer = new StringBuffer();
        List<String> columns = new ArrayList<String>();
        columns.add("商品ID");
        columns.add("商家编码");
        columns.add("颜色尺寸");
        columns.add("数量");
        List<List<String>> datas = new ArrayList<List<String>>();
        List<String> data = null;
        for (Map.Entry<String, Map<String, GoodsIdOuterIdSpec>> entry : map.entrySet()) {
            //System.out.println(entry.getKey()+"="+entry.getValue());  
            strBuffer.append(entry.getKey());
            for (Entry<String, GoodsIdOuterIdSpec> outerIdSpec : entry.getValue().entrySet()) {
                //System.out.println(outerIdSpec.getKey()+"="+outerIdSpec.getValue());  
                //  System.out.println(JSONObject.toJSONString(outerIdSpec.getValue()));
                data = new ArrayList<String>();
                data.add(outerIdSpec.getValue().getGoodsId());
                data.add(outerIdSpec.getValue().getOuterId());
                data.add(outerIdSpec.getValue().getGoodsSpec());
                data.add(outerIdSpec.getValue().getGoodsCount() + "");
                datas.add(data);
                strBuffer.append(
                    "\t\t" + outerIdSpec.getKey() + ":" + outerIdSpec.getValue().getGoodsCount());
            }
            data = new ArrayList<String>();
            data.add("");
            data.add("");
            data.add("");
            data.add("");
            datas.add(data);
            data = new ArrayList<String>();
            strBuffer.append("\n");

        }
        FileUtil.WriteStringToFile("/Users/chengqianliang/wTTP/test.txt", strBuffer.toString());

        try {
            ExcelUtil.exportDataToExcel(columns, datas,
                new FileOutputStream("/Users/chengqianliang/wTTP/"
                                     + DateTime.now().toString("yyyy-MM-dd_HH点mm分") + ".xls"),
                DateTime.now().toString("yyyy-MM-dd_HH点mm分") + ".xls", "test", "PIN", null, null,
                null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}