package com.example.webmagic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import javax.management.JMException;

/* 实现 基本的 网络爬虫
*     引入 webmagic-core 包即可使用此功能。
*
*     示例：
*         爬取 "hao123"首页中"天气预报"的气象、气温等数据。
*             有时间的话，尝试爬取 "搜狗搜索"首页中"天气预报"的，由于不支持 Xpath Axis ，编写 表达式 有一些难度，值得挑战。
*
*  */
public class SpiderRunner {

    public static void setUp() {
        Logger logger = LoggerFactory.getLogger(SpiderRunner.class);
    }

    public static void main(String[] args) throws JMException {
        /*
        * 入口 是 Spider 。
        *
        * 步骤：
        *     1：创建一个 Spider 对象
        *     2：添加要从中爬取数据的 Url 字符串，可以多个，但这里不是数组
        *     3：指定 管道输出到 控制台
        *     4：设定 多线程，起到 加速爬取 的作用
        *     5：新建一个 SpiderMonitor 对象，注册一个 Spider 对象，起到 监控爬虫 的作用
        *     6：启动
        *
        *  */
        Spider spider_hao123 = Spider.create(new Processor_Page())
                .addPipeline(new ConsolePipeline())
                .thread(3)
                .addUrl("https://www.hao123.com/");
        SpiderMonitor.instance().register(spider_hao123);

//        // 设定 代理，则不再经过 Site 爬取网站。代理可以是多个
//        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        httpClientDownloader.setProxyProvider(
//                SimpleProxyProvider.from(
//                        new Proxy("localhost",10101),
//                        new Proxy("101.101.101.101",8888,"username","password")));
//        // 设定 Downloader ，一个 Spider 只能有个一个 Downloader
//        spider_hao123.setDownloader(httpClientDownloader);
//        // 设置 Scheduler ，一个 Spider 只能有个一个 Scheduler
//        spider_hao123.setScheduler(new RedisScheduler(new JedisPool("localhost",6379)));

        spider_hao123.run();
    }

}
