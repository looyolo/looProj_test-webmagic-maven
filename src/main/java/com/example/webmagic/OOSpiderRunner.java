package com.example.webmagic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.monitor.SpiderMonitor;

import javax.management.JMException;

/* 通过 注解方式 来实现网络爬虫
*     引入 webmagic-extension 包即可使用此功能。
*     适用于编写 简单的爬虫 ，这样写既简单又容易理解，并且管理起来也很方便。号称这特色为 OEM(Object/Extraction Mapping) 。
*     注解模式的开发方式是这样的：
*         首先定义你需要抽取的数据，并编写类。
*         在类上写明@ TargetUrl 注解，定义对哪些 URL 进行下载和抽取。
*         在类的字段上加上 @ExtractBy 注解，定义这个字段使用什么方式进行抽取。
*         定义结果的存储方式。
*
*  */
public class OOSpiderRunner {

    public static void setUp() {
        Logger logger = LoggerFactory.getLogger(OOSpiderRunner.class);
    }

    public static void main(String[] args) throws JMException {
        /*
         * 注解模式 的 入口 是 OOSpider，它继承了 Spider 类，提供了类似的方法。
         *     创建一个注解模式的爬虫，需要一个或多个 Model 类，以及一个或多个 PageModelPipeline 结果处理方式类对象实例。
         *       注意：PageModelPipeline 与 Model 类是对应的，多个 Mode l可以对应一个 PageModelPipeline
         *
         * 步骤：
         *     1：创建一个 Spider 对象，入参中需实现了一个 Model 类及其实例化，并指定 管道输出到 控制台
         *     2：添加要从中爬取数据的 Url 字符串，可以多个，但这里不是数组
         *     3：设定 多线程，起到 加速爬取 的作用
         *     4：新建一个 SpiderMonitor 对象，注册一个 Spider 对象，起到 监控爬虫 的作用
         *     5：启动
         *
         *  */
        Spider oospider_hao123 = OOSpider.create(Site.me().setCharset("UTF-8").setRetryTimes(3).setSleepTime(1000))
                .addPageModel(new ConsolePageModelPipeline(), Model_Page.class)
                .thread(3)
                .addUrl("https://www.hao123.com/");
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
