package tmallTB;

import com.cheng.qian.pagProcessor.TmallTBPageProcessor;

import us.codecraft.webmagic.Spider;

public class Test {
    public static void main(String[] args) {
        Spider spider = Spider.create(new TmallTBPageProcessor());
        spider.addUrl("https://detail.tmall.com/item.htm?id=550311142162")
            // .addPipeline(starpropertyProjectPipeline)
            //开启5个线程抓取
            .thread(1)
            //启动爬虫
            .run();
    }
}
