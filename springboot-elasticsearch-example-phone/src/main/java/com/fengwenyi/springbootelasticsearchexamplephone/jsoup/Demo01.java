package com.fengwenyi.springbootelasticsearchexamplephone.jsoup;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author Erwin
 * @date 2019-03-17 01:27
 */
public class Demo01 {

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault(); // 创建httpclient实例
        HttpGet httpget = new HttpGet("https://www.cnblogs.com/"); // 创建httpget实例

        CloseableHttpResponse response = httpclient.execute(httpget); // 执行get请求
        HttpEntity entity=response.getEntity(); // 获取返回实体
        //System.out.println("网页内容："+ EntityUtils.toString(entity, "utf-8")); // 指定编码打印网页内容
        String content = EntityUtils.toString(entity, "utf-8");
        response.close(); // 关闭流和释放系统资源

        // 解析网页获取文档对象
        Document document = Jsoup.parse(content);
        // 获取tag是title的所有DOM元素
        Elements elements = document.getElementsByTag("title");
        // 获取1个元素
        Element element = elements.get(0);
        // 返回元素的文本
        String title = element.text();
        System.out.println("网页辩题是：" + title);

        Element element2 = document.getElementById("site_nav_top");
        String text = element2.text();
        System.out.println(text);

    }
}
