package com.example.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.management.JMException;

/*
 * 代码解释：
 *         爬取 "hao123"首页中"天气预报"的气象、气温等数据。
 *             有时间，尝试爬取 "搜狗搜索"首页中"天气预报"的，由于不支持 Xpath Axis ，编写 表达式 有一些难度，值得挑战。
 *
 *  */
public class Processor_Page implements PageProcessor {
    // 设定 抓取网站 的相关配置，包括 字符编码、抓取间隔、抓取重试次数
    private final Site site = Site.me().setCharset("utf-8").setRetryTimes(3).setSleepTime(1000);

    // process 是 定制 爬虫逻辑 的 核心接口，在这里 编写 抽取逻辑
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
