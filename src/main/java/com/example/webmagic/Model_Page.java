package com.example.webmagic;

import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/*
* 编写 Model 类 抽取逻辑，通过 注解 的方式
*
* 代码解释： （摘录自 http://webmagic.io/docs/zh/posts/ch5-annotation/targeturl.html）
*     HelpUrl / TargetUrl 是一个非常有效的爬虫开发模式，TargetUrl 是我们最终要抓取的 URL ，最终想要的数据都来自这里；
*       而 HelpUrl 则是为了发现这个最终 URL ，我们需要访问的页面。几乎所有垂直爬虫的需求，都可以归结为对这两类 URL 的处理：
*         - 对于博客页，HelpUrl 是列表页，TargetUrl 是文章页。
*         - 对于论坛，HelpUrl 是帖子列表，TargetUrl 是帖子详情。
*         - 对于电商网站，HelpUrl 是分类列表，TargetUrl 是商品详情。
*     在 WebMagic 中，从 TargetUrl 页面得到的 URL ，只要符合 TargetUrl 的格式，也是会被下载的。
*       所以，即使不指定HelpUrl也是可以的——例如某些博客页总会有“下一篇”链接，这种情况下无需指定HelpUrl。
*
*     TargetUrl 还支持定义 sourceRegion ，这个参数是一个 XPath表 达式，指定了这个 URL 从哪里得到——不在 sourceRegion 的 URL 不会被抽取。
*
*  */
@TargetUrl("https://www.hao123.com/")
@HelpUrl("https://www.hao123.com[/\\w]+")
public class Model_Page {
    // 抽取 “地市”
    //   ExtractBy 默认 type = ExtractBy.Type.XPath ，不需要 显式指定
    @ExtractBy(value = "//*[@id=\"topColumn\"]//span[@class=\"city-name\"]/text()",
            notNull = true)
    private String city_name;

    // 抽取 今天的气象、气温
    @ExtractBy(value = "//*[@id=\"topColumn\"]//a[@class=\"line-item weather-today\"]//span[@class=\"weather-status\"]/text()",
            notNull = true)
    private String meteo_today;
    @ExtractBy(value = "//*[@id=\"topColumn\"]//a[@class=\"line-item weather-today\"]//span[@class=\"weather-temp\"]/text()",
            notNull = true)
    private String temperature_today;

    // 抽取 明天的气象、气温
    @ExtractBy(value = "//*[@id=\"topColumn\"]//a[@class=\"line-item weather-tomorrow\"]//span[@class=\"weather-status\"]/text()",
            notNull = true)
    private String meteo_tomorrow;
    @ExtractBy(value = "//*[@id=\"topColumn\"]//a[@class=\"line-item weather-tomorrow\"]//span[@class=\"weather-temp\"]/text()",
            notNull = true)
    private String temperature_tomorrow;

}
