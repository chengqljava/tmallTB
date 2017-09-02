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

public class TaoBaoTBPageProcessor implements PageProcessor {
    private static  String pwdAddress = "/Users/chengqianliang/tmallTB/";
    private static boolean winMac =false;
 
    private Site                site       = Site.me()
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36")
        .addHeader("Accept", "*/*").addHeader("Accept-Encoding", "gzip, deflate, sdch")
        .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=GBK")
        .addHeader("X-Requested-With", "XMLHttpRequest").setCharset("GBK")
        .addHeader("Connection", "keep-alive").setRetryTimes(3).setSleepTime(50000)
        .setTimeOut(30000);
    public TaoBaoTBPageProcessor() {
  		super();
  		String os = System.getProperty("os.name").toLowerCase();  
  		if(os.toLowerCase().startsWith("win")){  
  		  System.out.println(os + " can't gunzip");  
  		  pwdAddress="E:\\tmallTB\\"+"taobao\\";
  		winMac=true;
  		}  
  		
  	}
    /** 
     * @see us.codecraft.webmagic.processor.PageProcessor#process(us.codecraft.webmagic.Page)
     */
    public void process(Page page) {
    	try{
        String html = page.getHtml().toString();
        List<ImageDTO> dtos = new ArrayList<ImageDTO>();
        ImageDTO imageDTO = null;
        StringBuffer buffer = new StringBuffer();
        System.out.println(page.getHtml().toString());
        // System.out.println(page.getHtml().xpath("//div[@id='detail']"));

        // System.out.println(page.getHtml().xpath("//ul[@id='J_UlThumb']/li/div/a"));

        //主图5
        Document document = Jsoup.parse(page.getHtml().xpath("//ul[@id='J_UlThumb']").toString());
        Elements imgElements = document.getElementsByTag("img");
        for (int i = 0; i < imgElements.size(); i++) {
            for (SizeImage sizeImage : SizeImage.values()) {
                if (sizeImage.getSize() != null && !"".equals(sizeImage.getSize())) {
                    imageDTO = new ImageDTO();
                    imageDTO.setName("默认主图" + i + sizeImage.getSize());
                    imageDTO.setUrl(imgElements.get(i).attr("data-src").replace("_50x50.jpg", ""));
                    imageDTO.setSaveAddress(sizeImage.getAddress());
                    imageDTO.setSize(sizeImage.getSize());
                    dtos.add(imageDTO);
                }
            }
        }
        //标题 描述
        buffer.append("\r\n" + page.getHtml().xpath("//div[@id='J_Title']").toString() + "\r\n");
        System.out.println(page.getHtml().toString().contains("data-property='尺寸'"));
        //尺码
        String sizeStr=null;
        if(page.getHtml().toString().contains("尺码")){
        sizeStr=page.getHtml().xpath("//ul[@data-property='尺码']").toString();
        }else{
        	sizeStr=page.getHtml().xpath("//ul[@data-property='尺寸']").toString();
        }
        document = Jsoup.parse(sizeStr);
         
        Elements sizeElements = document.getElementsByTag("span");
        for (int i = 0; i < sizeElements.size(); i++) {
            buffer.append("\r\n" + sizeElements.get(i).text());
        }
        //颜色 
        document = Jsoup.parse(page.getHtml().xpath("//ul[@data-property='颜色分类']").toString());
        Elements colorElements = document.getElementsByTag("a");
        System.out.println(page.getHtml().xpath("//ul[@data-property='颜色分类']").toString());
        String colorUrlImage = null;
        for (int i = 0; i < colorElements.size(); i++) {
            buffer.append("\r\n" + colorElements.get(i).getElementsByTag("span").text());
            colorUrlImage = colorElements.get(i).attr("style").replace("background:url(", "");
            colorUrlImage = colorUrlImage.replace(") center no-repeat;", "");
            colorUrlImage = colorUrlImage.replace("_30x30.jpg", "");
            for (SizeImage sizeImage : SizeImage.values()) {
                if (sizeImage.getSize() != null && !"".equals(sizeImage.getSize())) {
                    imageDTO = new ImageDTO();
                    imageDTO.setName("颜色" + colorElements.get(i).getElementsByTag("span").text() + i
                                     + sizeImage.getSize());
                    imageDTO.setUrl(colorUrlImage);
                    imageDTO.setSaveAddress(sizeImage.getAddress());
                    imageDTO.setSize(sizeImage.getSize());
                    dtos.add(imageDTO);
                }
            }

        }
        //System.out.println(page.getHtml().xpath("//div[@id='J_DivItemDesc']"));

        int indexScript = html.indexOf("<script>");
        int closeScript = html.indexOf("</script>");
        String g_config = html.substring(indexScript, closeScript);
        String desc = null;
        indexScript = g_config.indexOf("=");
        closeScript = g_config.indexOf("};");
        g_config = g_config.substring(indexScript + 1, closeScript + 1);
        indexScript = g_config.indexOf("descUrl");
        closeScript = g_config.indexOf("counterApi");
        try {
            g_config = g_config.substring(indexScript + 1, closeScript);
            desc = g_config.replace(",", "").split(":")[3].replace("'//", "").replace("'", "");
            dtos.addAll(detailImage(desc));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(JSONObject.toJSONString(dtos));
        //生成文件夹 确认文件名
        // mkdir = System.currentTimeMillis() + "";
        document = Jsoup.parse(page.getHtml().xpath("//div[@id='J_Title']").toString());

        String mkdir = document.getElementsByTag("h3").text();
        String titleText = mkdir;

        //获取标题
        //1创建文件夹
        judeDirExists(pwdAddress + mkdir);
        for (SizeImage sizeImage : SizeImage.values()) {
            judeDirExists(pwdAddress + mkdir + (winMac?"\\":"/") + sizeImage.getAddress());
        }
        //2写入文件信息
        buffer.append("\r\n" + "地址:" + page.getUrl());
        WriteStringToFile(pwdAddress + mkdir + (winMac?"\\":"/") + titleText, buffer.toString());
        System.out.println("KJEFE" + JSONObject.toJSONString(dtos));
        for (int i = 0; i < dtos.size(); i++) {
            imageDTO = dtos.get(i);
            try {
                if (imageDTO.getUrl().startsWith("//")) {
                    imageDTO.setUrl("https:" + imageDTO.getUrl());
                }
                System.err.println(imageDTO.getUrl());
                if (imageDTO.getSize() != null) {
                    download(imageDTO.getUrl() + imageDTO.getSize(), imageDTO.getName() + ".jpg",
                        pwdAddress + mkdir +  (winMac?"\\":"/")  + imageDTO.getSaveAddress());
                } else {
                    download(imageDTO.getUrl(), imageDTO.getName() + ".jpg",
                        pwdAddress + mkdir +  (winMac?"\\":"/")  + imageDTO.getSaveAddress());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}

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
            int descIndex = desc.indexOf("desc");
            int descClose = desc.lastIndexOf(";");
            desc = desc.substring(descIndex + 5, descClose);
            Document document = Jsoup.parse(desc);
            Elements elements = document.getElementsByTag("img");
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).hasAttr("align")) {
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
