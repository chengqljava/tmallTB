package tmallTB;

import com.cheng.qian.pagProcessor.TmallTBPageProcessor;

import us.codecraft.webmagic.Spider;

public class TestTmall {
    //https://detail.tmall.com/item.htm?id=547143017906
    private static final String url = "https://detail.tmall.com/item.htm?id=551934729505";

    public static void main(String[] args) {
        Spider spider = Spider.create(new TmallTBPageProcessor());
        spider.addUrl(url)
            // .addPipeline(starpropertyProjectPipeline)
            //开启5个线程抓取
            .thread(1)
            //启动爬虫
            .run();

    }
}
