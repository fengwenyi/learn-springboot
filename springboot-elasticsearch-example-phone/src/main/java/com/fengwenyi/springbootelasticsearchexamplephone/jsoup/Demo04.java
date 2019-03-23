package com.fengwenyi.springbootelasticsearchexamplephone.jsoup;

import com.alibaba.fastjson.JSON;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抓取 小米手机
 * @author Erwin
 * @date 2019-03-17 01:27
 */
public class Demo04 {

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault(); // 创建httpclient实例
        HttpGet httpget = new HttpGet("https://www.mi.com/p/1915.html"); // 创建httpget实例

        CloseableHttpResponse response = httpclient.execute(httpget); // 执行get请求
        HttpEntity entity=response.getEntity(); // 获取返回实体
        //System.out.println("网页内容："+ EntityUtils.toString(entity, "utf-8")); // 指定编码打印网页内容
        String content = EntityUtils.toString(entity, "utf-8");
        response.close(); // 关闭流和释放系统资源

//        System.out.println(content);

        Document document = Jsoup.parse(content);
//        Elements titles = document.select(".container .children-list .first .title");
//        Elements prices = document.select(".container .children-list .first .price");

        Elements titles = document.select(".site-header .container .header-nav .nav-list .nav-item .item-children .container .children-list li .title");
        Elements prices = document.select(".site-header .container .header-nav .nav-list .nav-item .item-children .container .children-list li .price");

        List<Map<String, String>> list = new ArrayList<>();

        int i = 0;

        for (Element element : titles) {
            String text = element.text();
            if (text.contains("查看"))
                continue;
            Map<String, String> map = new HashMap<>();
            map.put("name", text);
            map.put("price", prices.get(i).text());
            list.add(map);
            i++;
        }

        for (Map<String, String> map : list) {
            String name = map.get("name");
            String price = map.get("price");
            System.out.println(name + " -> " + price);
        }


        System.out.println("title number : " + titles.size());
        System.out.println("price number : " + prices.size());


    }
}
