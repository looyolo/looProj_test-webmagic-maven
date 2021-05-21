package com.example.webmagic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.monitor.SpiderMonitor;

import javax.management.JMException;

public class OOSpiderRunner {

    public static void setUp() {
        Logger logger = LoggerFactory.getLogger(OOSpiderRunner.class);
    }

    public static void main(String[] args) throws JMException {
        /*
         * 步骤：
         *     1：创建一个 Spider 对象，入参中需实现了一个 Model 类及其实例化，并指定 管道输出到 控制台
         *     2：添加要从中爬取数据的 Url 字符串，可以多个，但这里不是数组
         *     3：设定 多线程，起到 加速爬取 的作用
         *     4：新建一个 SpiderMonitor 对象，注册一个 Spider 对象，起到 监控爬虫 的作用
         *     5：启动
         *
         *  */
        Spider oospider_hao123 = OOSpider.create(
                Site.me().setCharset("UTF-8").setRetryTimes(3).setSleepTime(1000),
                new ConsolePageModelPipeline(), Model_Page.class)
                .addUrl("https://www.hao123.com/")
                .thread(3);
        SpiderMonitor.instance().register(oospider_hao123);

//        // 设定 代理，则不再经过 Site 爬取网站。代理可以是多个
//        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        httpClientDownloader.setProxyProvider(
//                SimpleProxyProvider.from(
//                        new Proxy("localhost",10101),
//                        new Proxy("101.101.101.101",8888,"username","password")));
//        spider_hao123.setDownloader(httpClientDownloader);

        oospider_hao123.run();
    }

}
