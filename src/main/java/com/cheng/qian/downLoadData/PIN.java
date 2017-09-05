package com.cheng.qian.downLoadData;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import com.alibaba.fastjson.JSONArray;
import com.cheng.qian.model.GoodsIdOuterIdSpec;
import com.cheng.qian.util.ExcelUtil;

public class PIN {
    public static void main(String[] args) {
        Order order = new Order();
        order.orderList("110937", "1308706231", "1", 1);
        Map<String, Map<String, GoodsIdOuterIdSpec>> map = order.orderDetail("110937", "1308706231",
            order.getOrderSNs(), null);
        System.out.println(JSONArray.toJSONString(map));
        StringBuffer strBuffer = new StringBuffer();
        List<String> columns = new ArrayList<String>();
        columns.add("商品ID");
        columns.add("商家编码颜色尺寸");
        columns.add("数量");
        columns.add("图片");
        //  columns.add("图片");
        List<List<String>> datas = new ArrayList<List<String>>();
        List<String> data = null;
        //商品ID/商品编码颜色尺码/数量
        Map<String, List<List<String>>> goodIdsCodeSize = new HashMap<String, List<List<String>>>();
        Map<String, String> goodIdsImg = new HashMap<String, String>();
        List<String> codeSize = null;
        List<List<String>> codeSizeList = null;
        for (Map.Entry<String, Map<String, GoodsIdOuterIdSpec>> entry : map.entrySet()) {
            strBuffer.append(entry.getKey());
            for (Entry<String, GoodsIdOuterIdSpec> outerIdSpec : entry.getValue().entrySet()) {
                //  System.out.println(outerIdSpec.getKey() + "=" + outerIdSpec.getValue());
                //System.out.println(JSONObject.toJSONString(outerIdSpec.getValue()));
                codeSize = new ArrayList<String>();
                codeSize.add(
                    outerIdSpec.getValue().getOuterId() + outerIdSpec.getValue().getGoodsSpec());
                codeSize.add(outerIdSpec.getValue().getGoodsCount() + "");
                if (!goodIdsImg.containsKey(outerIdSpec.getValue().getGoodsId())) {
                    goodIdsImg.put(outerIdSpec.getValue().getGoodsId(),
                        outerIdSpec.getValue().getGoodsImg());
                }
                if (goodIdsCodeSize.containsKey(outerIdSpec.getValue().getGoodsId())) {
                    codeSizeList = goodIdsCodeSize.get(outerIdSpec.getValue().getGoodsId());
                } else {
                    codeSizeList = new ArrayList<List<String>>();
                    goodIdsCodeSize.put(outerIdSpec.getValue().getGoodsId(), codeSizeList);
                }
                codeSizeList.add(codeSize);

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
        // FileUtil.WriteStringToFile("/Users/chengqianliang/wTTP/test.txt", strBuffer.toString());
        try {
            ExcelUtil.exportDataToExcelImg(columns,
                new FileOutputStream("/Users/chengqianliang/wTTP/"
                                     + DateTime.now().toString("yyyy-MM-dd_HH点mm分") + ".xls"),
                "PIN" + DateTime.now().toString("yyyy-MM-dd_HH点mm分") + ".xls", "test", "PIN", null,
                null, null, goodIdsImg, goodIdsCodeSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
