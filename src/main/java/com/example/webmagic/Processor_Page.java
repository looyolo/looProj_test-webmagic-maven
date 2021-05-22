package com.example.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.management.JMException;

/*
 * 编写 Processor 类 抽取逻辑
 *
 * 代码解释：
 *     PageProcessor 的定制，分为 3 个部分：
 *         （1）爬虫的配置
 *               包括编码、抓取间隔、超时时间、重试次数等，也包括一些模拟的参数，例如 User Agent、cookie，以及 proxy 代理的设置
 *         （2）页面元素的抽取，保存结果
 *               page.getHtml() 返回的是一个 Html 对象，它实现了 Selectable 接口。这个接口包含一些重要的方法，支持"链式调用"
 *               三种抽取技术：XPath、正则表达式 和 CSS选择器。另外，对于 JSON 格式的内容，可使用 JsonPath 进行解析
 *               定制 Pipeline ，可以实现保存结果到文件、数据库等
 *               处理非HTTP GET请求
 *         （3）后续链接的主动发现，并爬取
 *               page.addTargetRequests() 将发现的链接加入到待抓取的队列中去，对应的日志打印，如下，
 *                 2021-05-22 13:30:27.829 [pool-1-thread-1] DEBUG us.codecraft.webmagic.scheduler.QueueScheduler - push to queue https://www.hao123.com/haoxue
 *
 *  */
public class Processor_Page implements PageProcessor {
    // 设定 抓取网站 的相关配置，包括 字符编码、抓取间隔、抓取重试次数
    private final Site site = Site.me().setCharset("utf-8").setRetryTimes(3).setSleepTime(1000);

    // process 是 定制 爬虫逻辑 的 核心接口，在这里 编写 抽取逻辑，并保存抽取结果
    @Override
    public void process(Page page) {
        // 抽取 “地市”
        page.putField("city_name",
                page.getHtml().xpath("//*[@id=\"topColumn\"]//span[@class=\"city-name\"]/text()").toString());
        if (page.getResultItems().get("city_name") == null) {
            // skip this page when resultItem is null
            page.setSkip(true);
        }

        // 抽取 今天的气象、气温
        page.putField("meteo_today",
                page.getHtml().xpath("//*[@id=\"topColumn\"]//a[@class=\"line-item weather-today\"]//span[@class=\"weather-status\"]/text()").toString());
        page.putField("temperature_today",
                page.getHtml().xpath("//*[@id=\"topColumn\"]//a[@class=\"line-item weather-today\"]//span[@class=\"weather-temp\"]/text()").toString());

        // 抽取 明天的气象、气温
        page.putField("meteo_tomorrow",
                page.getHtml().xpath("//*[@id=\"topColumn\"]//a[@class=\"line-item weather-tomorrow\"]//span[@class=\"weather-status\"]/text()").toString());
        page.putField("temperature_tomorrow",
                page.getHtml().xpath("//*[@id=\"topColumn\"]//a[@class=\"line-item weather-tomorrow\"]//span[@class=\"weather-temp\"]/text()").toString());

        // 从后续发现的 url 来抽取，个数不限，经常需要使用 regex 表达式来过滤得到想要的
        page.addTargetRequests(page.getHtml().links().regex("(https://www.hao123.com[/\\w]+)").all());
    }

    // 必须返回 site 实例对象，否则启动会报错 Exception in thread "main" java.lang.NullPointerException
    @Override
    public Site getSite() {
        return site;
    }

}
