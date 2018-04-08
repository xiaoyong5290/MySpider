package com.xiaoyong.download.mmjpg;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

/**
 * @author : XiaoYong
 * @date : 2018/4/4 18:03
 * Description    :
 */
public class Sel {

    public static void main(String[] args) {
        System.setProperty ( "webdriver.firefox.bin" , "D:/FireFox/firefox.exe" );
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
        for(WebElement imgTag : aElements) {
            System.out.println(imgTag.getAttribute("data-img"));
        }
    }
}
