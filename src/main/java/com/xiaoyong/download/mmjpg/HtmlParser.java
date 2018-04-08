package com.xiaoyong.download.mmjpg;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : XiaoYong
 * @date : 2018/4/3 17:43
 * Description    :
 */
public class HtmlParser {
    public static void main(String[] args) throws Exception {
        HtmlParser parser = new HtmlParser();
        parser.catchMMJpg(parser);
    }

    private void catchMMJpg(HtmlParser parser) throws Exception {
        String oldUrl = "http://www.mmjpg.com/mm/";
        String selectAttr = "img[data-img$=.jpg]";
        String attr = "data-img";
        String referer = "http://img.mmjpg.com/small/2016/656_s.jpg";
        int start = 1310;
        int end = start + 0;

        for (int i = start; i <= end; i++) {
            String newUrl = oldUrl + i;
            parser.downloadAndSave(parser.getImgUrlsFromMMJpg(newUrl, selectAttr, attr), referer, i);
        }
    }
    
    /**
     *   描述：从mmjpg.com抓取图片url
    **/
    private List<String> getImgUrlsFromMMJpg(String url, String selectAttr, String attr) throws IOException {
        // HtmlUnit 模拟浏览器
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        // 启用JS解释器，默认为true
        webClient.getOptions().setJavaScriptEnabled(true);
        // 禁用css支持
        webClient.getOptions().setCssEnabled(false);
        // js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        // 设置连接超时时间
        webClient.getOptions().setTimeout(10 * 1000);
//        抓取界面
        HtmlPage page = webClient.getPage(url);
//       点击界面的“全部图片”按钮
        page = page.getElementById("opic").click();
        // 等待js后台执行2秒
        webClient.waitForBackgroundJavaScript(2 * 1000);

        String pageAsXml = page.asXml();
//        System.out.println(pageAsXml);

        // Jsoup解析处理
        Document doc = Jsoup.parse(pageAsXml);
        // 获取所有图片元素集
        Elements jpgs = doc.select(selectAttr);

        List<String> imgUrls = new ArrayList<>();
        // 此处省略其他操作
        for (Element e : jpgs) {
            imgUrls.add(e.attr(attr));
        }

        return imgUrls;
    }
    /**
     *   描述：通过图片url从服务器下载和获取
    **/
    private void downloadAndSave(List<String> imgUrls, String referer, int issue) throws Exception {
        ImageDownload download = new ImageDownload();
        System.out.println(imgUrls.size());

        int index = 1;
        for (String imgUrl : imgUrls) {
            /*System.out.println(imgUrl);*/
//            Thread.sleep(1000);
            byte[] b = download.getImg(imgUrl,referer);

            if (b == null) {
                throw new Exception();
            }
            if (b.length > 0) {
                System.out.println("读取到: " + b.length + "字节");
                String filename = "妹子图片" + (index++) + ".jpg";
                download.saveImages(b, filename, issue);
            }
        }
    }
}
