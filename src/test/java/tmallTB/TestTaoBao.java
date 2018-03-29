package tmallTB;

import com.cheng.qian.pagProcessor.TaoBaoTBJSONPageProcessor;

import us.codecraft.webmagic.Spider;

public class TestTaoBao {

    private static String url = "https://item.taobao.com/item.htm?id=565272034046";

    public static void main(String[] args) {
        Spider spider = Spider.create(new TaoBaoTBJSONPageProcessor());
        spider.addUrl(url)
            // .addPipeline(starpropertyProjectPipeline)
            //开启5个线程抓取
            .thread(1)
            //启动爬虫
            .run();

    }
}
