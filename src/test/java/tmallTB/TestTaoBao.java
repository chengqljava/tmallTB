package tmallTB;

import com.cheng.qian.pagProcessor.TaoBaoTBPageProcessor;

import us.codecraft.webmagic.Spider;

public class TestTaoBao {
    public static void main(String[] args) {
        Spider spider = Spider.create(new TaoBaoTBPageProcessor());
        spider.addUrl(
            "https://item.taobao.com/item.htm?spm=a230r.1.14.104.fSQQjF&id=549924513094&ns=1&abbucket=13#detail")
            // .addPipeline(starpropertyProjectPipeline)
            //开启5个线程抓取
            .thread(1)
            //启动爬虫
            .run();

    }
}
