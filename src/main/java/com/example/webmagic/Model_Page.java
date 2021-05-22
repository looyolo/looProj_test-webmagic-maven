package com.example.webmagic;

import us.codecraft.webmagic.model.annotation.*;

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
*       所以，即使不指定 HelpUrl 也是可以的。例如某些博客页总会有“下一篇”链接，这种情况下无需指定 HelpUrl 。
*     简而言之，TargetUrl 是最终的项目页，而 Helper 是项目搜索页，会在控制台打印出匹配到的所有项目的连接。
*
*     TargetUrl 还支持定义 sourceRegion ，这个参数是一个 XPath表 达式，指定了这个 URL 从哪里得到。不在 sourceRegion 的 URL 不会被抽取。
*
*     需要注意的是，WebMagic 对 Java 正则表达式 做了 2 点改动：
*         ① 对 url 中常见的 . 默认作了转义，即 \.
*         ② 将 * 替换为 .* ，直接使用可以表示通配符
*
*  */
@TargetUrl("https://www.hao123.com/")
@HelpUrl("https://www.hao123.com[/\\w]+")
public class Model_Page {
    // 抽取 “地市”
    //   @ExtractBy 注解式抽取， 默认 type = ExtractBy.Type.XPath ，不需要 显式指定。还可以支持 CSS 选择器、正则表达式 和 JsonPath
    //   @ExtractBy 包含一个 notNull 属性，默认为 false ，对于一些页面的关键性属性（例如文章的标题等），设为true，可以有效的过滤掉无用的页面。
    //   @ExtractBy 这里示例是是注解在 类的成员变量 上，作用是一个页面只对应一条结果。
    //     它还可以注解在 类 上，作用是一个页面有多个抽取的记录。参阅 http://webmagic.io/docs/zh/posts/ch5-annotation/extractby-on-class.html
    //   另外，@ExtractByUrl 是一个单独的注解，它的意思是“从 URL 中进行抽取”。它只支持 正则表达式 作为抽取规则。
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

    // 抽取 hao123网站的标题
    //   因为抽取到的内容总是 String ，而我们想要的内容则可能是其他类型。类型转换（Formatter机制）可以将抽取到的内容，自动转换为所有基本类型和装箱类型。
    //     在特殊情况下，需要手动显式指定转换类型。这主要发生在字段是 List 类时。
    //   另外，还支持 java.util.Date 类型转换。需要指定 Date 格式，具体规范可以看这里：http://java.sun.com/docs/books/tutorial/i18n/format/simpleDateFormat.html
    //   这里再示例的是，自定义 Formatter（TODO），做一些结果的后处理的事情。例如，我们有一种需求场景，需要将抽取的结果作为结果的一部分，拼接上一部分字符串来使用。
    @Formatter(value = "Title is %s",formatter = StringTemplateFormatter.class)
    @ExtractBy(value = "//*[@id=\"indexLogo\"]/a/@title", notNull = true)
    private String title;

}
