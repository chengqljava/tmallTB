package com.cheng.qian.pagProcessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class TmallTBPageProcessor implements PageProcessor {
    private Site site = Site.me()
        .addHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36")
        .addHeader("Accept", "*/*").addHeader("Accept-Encoding", "gzip, deflate, sdch")
        .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        .addHeader("X-Requested-With", "XMLHttpRequest").setCharset("UTF-8")
        .addHeader("Connection", "keep-alive").setRetryTimes(3).setSleepTime(50000)
        .setTimeOut(30000);

    public void process(Page page) {
        System.out.println(page.getHtml().xpath("//div[@id='detail']"));
        // 五幅图
        System.out.println(page.getHtml().xpath("//ul[@id='J_UlThumb']"));
        //tb-prop tm-sale-prop tm-clear 
        System.out.println(page.getHtml().xpath("//ul[@data-property='尺码']"));

        //  System.out.println(page.getHtml().xpath("//div[@id='description']"));
        //  System.out.println(page.getHtml().xpath("//img[@id='desc-module-1']"));

    }

    public Site getSite() {
        return site;
    }

}
