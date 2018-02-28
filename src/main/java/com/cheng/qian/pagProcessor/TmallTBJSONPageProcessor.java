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
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheng.qian.enums.SizeImage;
import com.cheng.qian.model.ColorKeyValue;
import com.cheng.qian.model.ImageDTO;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class TmallTBJSONPageProcessor implements PageProcessor {

    private static String  pwdAddress = "/Users/chengqianliang/tmallTB/";
    private static boolean winMac     = false;
    private Site           site       = Site.me()
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36")
        .addHeader("Accept", "*/*").addHeader("Accept-Encoding", "gzip, deflate, sdch")
        .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=GBK")
        .addHeader("X-Requested-With", "XMLHttpRequest").setCharset("GBK")
        .addHeader("Connection", "keep-alive").setRetryTimes(3).setSleepTime(50000)
        .setTimeOut(30000);

    public TmallTBJSONPageProcessor() {
        super();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.toLowerCase().startsWith("win")) {
            System.out.println(os + " can't gunzip");
            pwdAddress = "E:\\tmallTB\\" + "tmall\\";
            winMac = true;
        }

    }

    /** 
     * @see us.codecraft.webmagic.processor.PageProcessor#process(us.codecraft.webmagic.Page)
     */
    public void process(Page page) {
        StringBuffer textContent = new StringBuffer();
        String html = page.getHtml().toString();
        int start, end;
        start = html.indexOf("TShop.Setup(");
        html = html.substring(start);
        end = html.indexOf(");");
        html = html.substring(0, end).replace("TShop.Setup(", "");
        System.err.println(html);
        JSONObject jsonObject = JSONObject.parseObject(html);
        //Video
        System.out.println(jsonObject.getJSONObject("itemDO").get("imgVedioPic"));
        System.out.println(jsonObject.getJSONObject("itemDO").get("imgVedioUrl"));

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
            // TODO Auto-generated catch block
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

    /**
     * 详情图片
     * @param url
     * @return
     */
    public List<ImageDTO> detailImage(String url) {
        List<ImageDTO> dtos = new ArrayList<ImageDTO>();
        ImageDTO imageDTO = null;
        try {
            if (url.startsWith("//")) {
                url = url.replace("//", "");
            }

            HttpResponse response = HttpRequest.get(url).send();
            String desc = response.toString();
            System.err.println(response);
            int descIndex = desc.indexOf("desc");
            int descClose = desc.lastIndexOf(";");
            desc = desc.substring(descIndex + 5, descClose);
            System.out.println(desc);
            Document document = Jsoup.parse(desc);
            Elements elements = document.getElementsByTag("img");
            System.out.println(elements.size());
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).attr("src").contains("img.alicdn.com")) {
                    System.out.println(elements.get(i).attr("src"));
                    imageDTO = new ImageDTO();
                    imageDTO.setName("详情" + i);
                    imageDTO.setUrl(elements.get(i).attr("src"));
                    imageDTO.setSaveAddress(SizeImage.S_DETAIl.getAddress());
                    dtos.add(imageDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtos;
    }

    /**
     * 详情图片
     * @param url
     * @return
     */
    public List<ImageDTO> propertyPics(JSONObject jsonObject, List<ColorKeyValue> colorKeyValues) {
        List<ImageDTO> dtos = new ArrayList<ImageDTO>();
        JSONArray propertyPicsArray = jsonObject.getJSONArray("default");
        dtos.addAll(propertyPicsCreateSize(propertyPicsArray, "默认主图5张", SizeImage.S800_800,
            SizeImage.S480_480, SizeImage.S640_640));
        for (int i = 0; i < colorKeyValues.size(); i++) {
            propertyPicsArray = jsonObject.getJSONArray(";" + colorKeyValues.get(i).getKey() + ";");
            dtos.addAll(propertyPicsCreateSize(propertyPicsArray, colorKeyValues.get(i).getValue(),
                SizeImage.S800_800, SizeImage.S480_480, SizeImage.S200_200));

        }
        return dtos;
    }

    public List<ImageDTO> propertyPicsCreateSize(JSONArray propertyPicsArray, String name,
                                                 SizeImage... images) {
        List<ImageDTO> dtos = new ArrayList<ImageDTO>();
        ImageDTO imageDTO = null;
        for (int i = 0; i < propertyPicsArray.size(); i++) {
            for (int j = 0; j < images.length; j++) {
                imageDTO = new ImageDTO();
                imageDTO.setName(name + i);
                imageDTO.setUrl(propertyPicsArray.getString(i));
                imageDTO.setSaveAddress(images[j].getAddress());
                imageDTO.setSize(images[j].getSize());
                dtos.add(imageDTO);
            }
        }
        return dtos;
    }

    public static void download(String urlString, String filename,
                                String savePath) throws Exception {
        System.out.println("urlString" + urlString);
        System.out.println(filename);
        System.out.println(savePath);
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
        OutputStream os = new FileOutputStream(sf.getPath() + (winMac ? "\\" : "/") + filename);
        // 开始读取  
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接  
        os.close();
        is.close();
    }

}
