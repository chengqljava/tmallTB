package tmallTB;

import com.cheng.qian.pagProcessor.TmallTBPageProcessor;

import us.codecraft.webmagic.Spider;

public class TestTmall {

    private static final String url = "https://detail.tmall.com/item.htm?id=559682873844";

    public static void main(String[] args) {
        Spider spider = Spider.create(new TmallTBPageProcessor());
        spider.addUrl(url)
            // .addPipeline(starpropertyProjectPipeline)
            //寮�鍚�5涓嚎绋嬫姄鍙�
            .thread(1)
            //鍚姩鐖櫕
            .run();

    }
}
