package com.cheng.qian.pagProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheng.qian.enums.SizeImage;
import com.cheng.qian.model.ImageDTO;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class PinPageProcessor implements PageProcessor {
    private static final String pwdAddress = "/Users/chengqianliang/wTTP/";
    private Site                site       = Site.me()
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36")
        .addHeader("Accept", "*/*").addHeader("Accept-Encoding", "gzip, deflate, sdch")
        .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
        .addHeader("X-Requested-With", "XMLHttpRequest").setCharset("utf-8")
        .addHeader("Connection", "keep-alive").setRetryTimes(3).setSleepTime(50000)
        .setTimeOut(30000);

    /** 
     * @see us.codecraft.webmagic.processor.PageProcessor#process(us.codecraft.webmagic.Page)
     */
    public void process(Page page) {
        String html = page.getHtml().toString();
        System.out.println(html);
        int index = html.indexOf("window.rawData=");
        int close = html.indexOf("</script>");
        html = html.substring(index, close);
        html = html.replace("window.rawData=", "");
        html = html.replace(";", "");
        String title, mkdir;
        StringBuffer strBuffer = new StringBuffer();
        List<ImageDTO> dtos = new ArrayList<ImageDTO>();
        ImageDTO imageDTO = null;
        JSONObject jsonObject = JSONObject.parseObject(html);
        jsonObject = JSONObject.parseObject(jsonObject.getString("goods"));
        System.out.println(jsonObject.toJSONString());
        //标题
        System.out.println(jsonObject.getString("goodsName"));
        title = jsonObject.getString("goodsName");
        mkdir = jsonObject.getString("goodsName");
        strBuffer.append("\r\n标题\n" + jsonObject.getString("goodsName"));
        //描述
        System.out.println(jsonObject.getString("goodsDesc"));
        strBuffer.append("\r\n描述\n" + jsonObject.getString("goodsDesc"));
        //主
        System.out.println(jsonObject.getString("topGallery"));
        JSONArray jsonArray = JSONObject.parseArray(jsonObject.getString("topGallery"));
        for (int i = 0; i < jsonArray.size(); i++) {
            imageDTO = new ImageDTO();
            imageDTO.setName("主图" + i);
            imageDTO.setUrl(jsonArray.getString(i));
            imageDTO.setSaveAddress(SizeImage.S800_800.getSize());
            imageDTO.setSaveAddress(SizeImage.S800_800.getAddress());
            dtos.add(imageDTO);
        }
        //描述
        System.out.println(jsonObject.getString("detailGallery"));
        JSONArray detailGallery = JSONObject.parseArray(jsonObject.getString("detailGallery"));
        for (int i = 0; i < detailGallery.size(); i++) {
            imageDTO = new ImageDTO();
            imageDTO.setName("详情" + i);
            imageDTO.setUrl(detailGallery.getJSONObject(i).getString("url"));
            imageDTO.setSaveAddress(SizeImage.S_DETAIl.getSize());
            imageDTO.setSaveAddress(SizeImage.S_DETAIl.getAddress());
            dtos.add(imageDTO);
        }
        //skus
        System.out.println(jsonObject.getString("skus"));
        JSONArray skus = JSONObject.parseArray(jsonObject.getString("skus"));
        JSONArray specs = null;
        List<String> thumbUrls = new ArrayList<String>();
        Map<String, List<String>> colorSizeMap = new HashMap<String, List<String>>();
        List<String> sizeList = null;
        strBuffer.append("\r\n颜色\n");
        for (int i = 0; i < skus.size(); i++) {
            specs = skus.getJSONObject(i).getJSONArray("specs");
            if (!thumbUrls.contains(skus.getJSONObject(i).getString("thumbUrl"))) {
                thumbUrls.add(skus.getJSONObject(i).getString("thumbUrl"));
                imageDTO = new ImageDTO();
                imageDTO.setName(specs.getJSONObject(0).getString("spec_value"));
                imageDTO.setSaveAddress(SizeImage.S200_200.getAddress());
                imageDTO.setSize(SizeImage.S200_200.getSize());
                imageDTO.setUrl(skus.getJSONObject(i).getString("thumbUrl"));
                dtos.add(imageDTO);
                strBuffer.append("\n" + specs.getJSONObject(0).getString("spec_value"));
            }
            if (colorSizeMap.containsKey(specs.getJSONObject(0).getString("spec_value"))) {
                sizeList = colorSizeMap.get(specs.getJSONObject(0).getString("spec_value"));
            } else {
                sizeList = new ArrayList<String>();
                colorSizeMap.put(specs.getJSONObject(0).getString("spec_value"), sizeList);
            }
            sizeList.add(specs.getJSONObject(1).getString("spec_value"));
            strBuffer.append("\n" + specs.getJSONObject(1).getString("spec_value"));
        }

        //获取标题
        //1创建文件夹
        judeDirExists(pwdAddress + mkdir);
        for (SizeImage sizeImage : SizeImage.values()) {
            judeDirExists(pwdAddress + mkdir + "/" + sizeImage.getAddress());
        }
        //2写入文件信息
        strBuffer.append("\r\n" + "地址:" + page.getUrl());
        WriteStringToFile(pwdAddress + mkdir + "/" + title, strBuffer.toString());
        for (int i = 0; i < dtos.size(); i++) {
            imageDTO = dtos.get(i);
            try {
                System.err.println(imageDTO.getUrl());
                if (imageDTO.getSize() != null) {
                    download(imageDTO.getUrl(), imageDTO.getName() + ".jpg",
                        pwdAddress + mkdir + "/" + imageDTO.getSaveAddress());
                } else {
                    download(imageDTO.getUrl(), imageDTO.getName() + ".jpg",
                        pwdAddress + mkdir + "/" + imageDTO.getSaveAddress());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(JSONObject.toJSONString(dtos));

    }

    public Site getSite() {
        return site;
    }

    public void WriteStringToFile(String filePath, String content) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filePath));
            pw.println(content);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 判断文件夹是否存在
    public static void judeDirExists(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            if (file.isDirectory()) {
                System.out.println("dir exists");
            } else {
                System.out.println("the same name file exists, can not create dir");
            }
        } else {
            System.out.println("dir not exists, create it ...");
            file.mkdir();
        }

    }

    // 判断文件是否存在
    public static void judeFileExists(File file) {

        if (file.exists()) {
            System.out.println("file exists");
        } else {
            System.out.println("file not exists, create it ...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void download(String urlString, String filename,
                                String savePath) throws Exception {
        System.out.println("urlString" + urlString);
        System.out.println(filename);
        System.out.println(savePath);
        try {
            // 构造URL  
            URL url = new URL(urlString);
            // 打开连接  
            URLConnection con = url.openConnection();
            //设置请求超时为5s  
            con.setConnectTimeout(5 * 1000);
            // 输入流  
            InputStream is = con.getInputStream();

            // 1K的数据缓冲  
            byte[] bs = new byte[1024];
            // 读取到的数据长度  
            int len;
            // 输出的文件流  
            File sf = new File(savePath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            OutputStream os = new FileOutputStream(sf.getPath() + "/" + filename);
            // 开始读取  
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接  
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
