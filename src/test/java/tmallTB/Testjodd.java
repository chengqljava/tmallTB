package tmallTB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

public class Testjodd {
    public static void main(String[] args) throws Exception {

        HttpResponse response = HttpRequest
            .get(
                "dsc.taobaocdn.com/i2/550/310/550311142162/TB18S4tRXXXXXb.XpXX8qtpFXlX.desc%7Cvar%5Edesc%3Bsign%5E8ab283b5e986e38cd7d0e72db6dfe6cb%3Blang%5Egbk%3Bt%5E1498008614")
            .send();
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
                download(elements.get(i).attr("src"), i + ".jpg",
                    "/Users/chengqianliang/tmallTB/test/");
            }
        }

    }

    public static void download(String urlString, String filename,
                                String savePath) throws Exception {
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
    }
}
