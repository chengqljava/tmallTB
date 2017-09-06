package tmallTB;

import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONObject;
import com.cheng.qian.util.SignUtil;

public class TestSign {
    public static void main(String[] args) {

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("mall_id", "110937");
        params.put("type", "pdd.order.number.list.get");
        params.put("order_status", "1");
        params.put("timestamp", "1500626946");
        params.put("data_type", "JSON");
        //total_count
        //两页
        //先加密
        String sign = SignUtil.signRequest(params, "1308706231");
        params.put("sign", sign);
        System.out.println(JSONObject.toJSONString(params));
    }
}
