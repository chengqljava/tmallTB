package tmallTB;

import com.cheng.qian.pagProcessor.TaoBaoTBPageProcessor;

import us.codecraft.webmagic.Spider;

public class TestTaoBao {

    private static String url = "https://item.taobao.com/item.htm?id=522629899293";

    public static void main(String[] args) {
        Spider spider = Spider.create(new TaoBaoTBPageProcessor());
        spider.addUrl(url)
            // .addPipeline(starpropertyProjectPipeline)
            //开启5个线程抓取
            .thread(1)
            //启动爬虫
            .run();

    }
}
