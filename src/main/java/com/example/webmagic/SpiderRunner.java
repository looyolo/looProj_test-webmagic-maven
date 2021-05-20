package com.example.webmagic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

public class SpiderRunner {

    public static void setUp() {
        Logger logger = LoggerFactory.getLogger(SpiderRunner.class);
    }

    public static void main(String[] args) {
        /*
        * 步骤：
        *     1：创建一个 Spider 对象
        *     2：添加要从中爬取数据的 Url 字符串，可以多个，但这里不是数组
        *     3：指定 管道输出到 控制台
        *     4：设定 多线程，起到 加速爬取 的作用
        *     5：启动
        *
        *  */
        Spider.create(new PP_SogouHomePage())
                .addUrl("https://www.hao123.com/")
                .addPipeline(new ConsolePipeline())
                .thread(3)
                .run();
    }

}
