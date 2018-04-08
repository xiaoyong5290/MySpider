package com.xiaoyong.download.mmjpg;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.*;

/**
 * @author : XiaoYong
 * @date : 2018/4/2 16:56
 * Description    :
 */
public class ImageDownload {

    /**
     *   描述：从输入流中读取图片
    **/
    private static byte[] readInputStream(InputStream instream) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024 * 5];
        int len;
        while ((len = instream.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
        instream.close();
        return output.toByteArray();
    }

    /**
     *   描述：从URL获取图片
    **/
    public byte[] getImg(String strUrl,String referer) throws IOException {
        try {
            //设置Fiddler配置
            /*System.setProperty("http.proxyHost", "127.0.0.1");
            System.setProperty("https.proxyHost", "127.0.0.1");
            System.setProperty("http.proxyPort", "8888");
            System.setProperty("https.proxyPort", "8888");*/
            OkHttpClient okHttp = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strUrl)
                    .addHeader("Referer", referer)
                    .build();
            Response response = okHttp.newCall(request).execute();

            if (response.isSuccessful()) {
                System.out.println("状态码：    "+response.code());
            } else {
//                释放资源
                response.body().close();
                throw new IOException("Unexpected code " + response);
            }
            InputStream input = response.body().byteStream();
            return readInputStream(input);
        } catch (Exception e) {
            System.out.println("没有妹子图片，自动切换URL");
            e.printStackTrace();

        }
        return null;
    }


    /**
     *   描述：将文件存入本地
    **/
    public void saveImages(byte[] img, String fileName, int issue) throws IOException {
        try {
            String saveDirectory = "F://mmjpg/" + issue;
            //separator=="/",相当于分隔符
            File directory = new File(saveDirectory);
            File file = new File(saveDirectory + File.separator + fileName);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(img);
            fileOut.flush();
            fileOut.close();
            System.out.println("图片已经写入到" + saveDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

