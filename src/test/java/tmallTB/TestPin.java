package tmallTB;

import com.cheng.qian.pagProcessor.PinPageProcessor;

import us.codecraft.webmagic.Spider;

public class TestPin {

    private static String url = "http://mobile.yangkeduo.com/goods2.html?goods_id=17748041";

    public static void main(String[] args) {
        Spider spider = Spider.create(new PinPageProcessor());
        spider.addUrl(url)
            // .addPipeline(starpropertyProjectPipeline)
            //开启5个线程抓取
            .thread(1)
            //启动爬虫
            .run();

    }
}
