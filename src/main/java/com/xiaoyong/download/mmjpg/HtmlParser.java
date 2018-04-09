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
class HtmlParser {

    void catchMMJpg(int start, int end) throws Exception {
//        抓取图片需要的信息，通过分析网站页面得到
//        未经拼装的url
        String oldUrl = "http://www.mmjpg.com/mm/";
//        标签选择器+属性选择器选择data-img属性以.jpg结尾的img标签
        String selectAttr = "img[data-img$=.jpg]";
        String attr = "data-img";
//        构造报文头Referer字段需要用到的字符串
        String referer = "http://img.mmjpg.com/small/2016/656_s.jpg";
/*//        要抓取的图片起始期数
        int start = 1310;
//        要抓取的图片截止期数
        int end = start + 0;*/

        for (int i = start; i <= end; i++) {
//            拼装目标期数界面url
            String newUrl = oldUrl + i;
//            调用getImgUrlsFromMMJpg()方法获取界面中的图片url，之后调用
//              downloadAndSave()方法下载保存
            this.downloadAndSave(this.getImgUrlsFromMMJpg(newUrl, selectAttr, attr), referer, i);
        }
    }

    /**
     * 描述：从mmjpg.com抓取图片url
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
//        将page转换成字符串
        String pageAsXml = page.asXml();

        // Jsoup将pageAsXml解析处理为Document对象
        Document doc = Jsoup.parse(pageAsXml);
        // 获取所有符合selectAttr的图片元素
        Elements jpgs = doc.select(selectAttr);

        List<String> imgUrls = new ArrayList<>();

        for (Element e : jpgs) {
//            获取元素的attr属性
            imgUrls.add(e.attr(attr));
        }

        return imgUrls;
    }

    /**
     * 描述：通过图片url从服务器下载和获取
     **/
    private void downloadAndSave(List<String> imgUrls, String referer, int issue) throws Exception {
        ImageDownload download = new ImageDownload();

        int index = 1;
        for (String imgUrl : imgUrls) {

//            调用ImageDownload类的getImg()方法
            byte[] b = download.getImg(imgUrl, referer);

            if (b == null) {
                throw new Exception();
            }
            if (b.length > 0) {
                System.out.println("读取到: " + b.length + "字节");
                String filename = "妹子图片" + (index++) + ".jpg";
                //将图片保存到本地文件系统
                download.saveImages(b, filename, issue);
            }
        }
    }
}
