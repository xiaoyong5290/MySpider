package com.xiaoyong.download.webmagicdemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.CssSelector;

import java.util.List;

/**
 * @author : XiaoYong
 * @date : 2018/4/4 16:12
 * Description    :
 */
public class MmJpgPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public static void main(String[] args) {
        Spider.create(new MmJpgPageProcessor())
                .addUrl("http://www.mmjpg.com/mm/1307")
                .addPipeline(new ConsolePipeline())
                .run();

    }

    @Override
    public void process(Page page) {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://www.mmjpg.com/mm/1307");

        WebElement element = driver.findElement(By.id("opic"));
        element.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement content = driver.findElement(By.id("content"));
        List<WebElement> aElements = content.findElements(By.tagName("a"));
        page.putField("size",aElements.size());
        for(WebElement imgTag : aElements) {
            System.out.println(imgTag.getAttribute("data-img"));
        }

//        List<WebElement> elements = driver.findElements()
        /* page.getHtml().select(new CssSelector(""));
        List<String> url = page.getHtml().xpath("//div[@id='content']/a/img").selectList(page.getRawText());
        page.putField("length", url);
        page.addTargetRequests(url);*/
    }

    @Override
    public Site getSite() {
        return site;
    }

}
