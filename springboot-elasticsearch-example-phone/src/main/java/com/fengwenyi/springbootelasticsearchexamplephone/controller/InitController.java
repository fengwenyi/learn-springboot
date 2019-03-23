package com.fengwenyi.springbootelasticsearchexamplephone.controller;

import com.alibaba.fastjson.JSON;
import com.fengwenyi.springbootelasticsearchexamplephone.jsoup.ColorModeBean;
import com.fengwenyi.springbootelasticsearchexamplephone.jsoup.HuaWeiPhoneBean;
import com.fengwenyi.springbootelasticsearchexamplephone.model.PhoneModel;
import com.fengwenyi.springbootelasticsearchexamplephone.repository.PhoneRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * @author Erwin
 * @date 2019-03-17 02:39
 */
@RestController
public class InitController {

    @Autowired
    private PhoneRepository phoneRepository;

    @PostConstruct
    public void init() {
        //phoneRepository.deleteAll();
        Iterable<PhoneModel> posts = phoneRepository.findAll();
        if (posts.iterator().hasNext()) {
            return;
        }
        try {
            huawei();
            xiaomi();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void huawei() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault(); // 创建httpclient实例
        HttpGet httpget = new HttpGet("https://consumer.huawei.com/cn/phones/?ic_medium=hwdc&ic_source=corp_header_consumer"); // 创建httpget实例

        CloseableHttpResponse response = httpclient.execute(httpget); // 执行get请求
        HttpEntity entity=response.getEntity(); // 获取返回实体
        //System.out.println("网页内容："+ EntityUtils.toString(entity, "utf-8")); // 指定编码打印网页内容
        String content = EntityUtils.toString(entity, "utf-8");
        response.close(); // 关闭流和释放系统资源

//        System.out.println(content);

        Document document = Jsoup.parse(content);
        Elements elements = document.select("#content-v3-plp #pagehidedata .plphidedata");
        for (Element element : elements) {
//            System.out.println(element.text());
            String jsonStr = element.text();
            List<HuaWeiPhoneBean> list = JSON.parseArray(jsonStr, HuaWeiPhoneBean.class);
            for (HuaWeiPhoneBean bean : list) {
                String productName = bean.getProductName();
                List<ColorModeBean> colorsItemModeList = bean.getColorsItemMode();

                StringBuilder colors = new StringBuilder();
                for (ColorModeBean colorModeBean : colorsItemModeList) {
                    String colorName = colorModeBean.getColorName();
                    colors.append(colorName).append(";");
                }

                List<String> sellingPointList = bean.getSellingPoints();
                StringBuilder sellingPoints = new StringBuilder();
                for (String sellingPoint : sellingPointList) {
                    sellingPoints.append(sellingPoint).append(";");
                }

//                System.out.println("产品名：" + productName);
//                System.out.println("颜  色：" + color);
//                System.out.println("买  点：" + sellingPoint);
//                System.out.println("-----------------------------------");
                PhoneModel phoneModel = new PhoneModel()
                        .setName(productName)
                        .setColors(colors.substring(0, colors.length() - 1))
                        .setSellingPoints(sellingPoints.substring(0, sellingPoints.length() - 1))
                        .setCreateTime(new Date());
                phoneRepository.save(phoneModel);
            }
        }
    }

    private void xiaomi() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault(); // 创建httpclient实例
        HttpGet httpget = new HttpGet("https://www.mi.com/p/1915.html"); // 创建httpget实例

        CloseableHttpResponse response = httpclient.execute(httpget); // 执行get请求
        HttpEntity entity=response.getEntity(); // 获取返回实体
        //System.out.println("网页内容："+ EntityUtils.toString(entity, "utf-8")); // 指定编码打印网页内容
        String content = EntityUtils.toString(entity, "utf-8");
        response.close(); // 关闭流和释放系统资源

//        System.out.println(content);

        Document document = Jsoup.parse(content);

        Elements titles = document.select(".site-header .container .header-nav .nav-list .nav-item .item-children .container .children-list li .title");
        Elements prices = document.select(".site-header .container .header-nav .nav-list .nav-item .item-children .container .children-list li .price");

        int i = 0;

        for (Element element : titles) {
            String name = element.text();
            if (name.contains("查看"))
                continue;
            PhoneModel phoneModel = new PhoneModel()
                    .setName(name)
                    .setPrice(prices.get(i).text())
                    .setCreateTime(new Date());
            phoneRepository.save(phoneModel);
            i++;
        }
    }

}
