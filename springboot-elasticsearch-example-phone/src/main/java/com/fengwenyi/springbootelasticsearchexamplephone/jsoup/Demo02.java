package com.fengwenyi.springbootelasticsearchexamplephone.jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import java.util.List;

/**
 * @author Erwin
 * @date 2019-03-17 01:27
 */
public class Demo02 {

    public static void main(String[] args) throws IOException {
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

                String color = "";
                for (ColorModeBean colorModeBean : colorsItemModeList) {
                    String colorName = colorModeBean.getColorName();
                    color += colorName + ", ";
                }
                List<String> sellingPoints = bean.getSellingPoints();
                String sellingPoint = "";
                for (String s : sellingPoints) {
                    sellingPoint += s + ", ";
                }
                System.out.println("产品名：" + productName);
                System.out.println("颜  色：" + color);
                System.out.println("买  点：" + sellingPoint);
                System.out.println("-----------------------------------");

            }
        }

    }
}
