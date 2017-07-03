package com.example.ai.forhealth.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.graphics.Palette;


import com.example.ai.forhealth.model.express.bean.CompanyNo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 */
final public class Tools {

    /**
     * 把JSONl流转换为字符串
     * @param is 流
     * @return 字符串
     */
    public static String readStream(InputStream is) throws IOException
    {
        InputStreamReader isr;
        String result = "";

        String line;
        isr = new InputStreamReader(is,"utf-8");

        BufferedReader reader = new BufferedReader(isr); //将字节流转化为字符流
        while ( (line = reader.readLine()) != null )
            result += line;
        return result;
    }

    /**
     * Web正文提取（网页去噪）
     * 返回数据格式：（json格式数据）
     * {"code":0,"data":{"title":"文章标题","pic":"图片地址","body":"正文"}}
     * 其中code的值的含义为，0：成功，1：失败。pic的值返回 0 说明文章没有图片
     * @param webUrl
     * @return
     */
    public static String WebText(String webUrl)
    {
        final String APPKEY = "62f1925eaea94045ca7c35b175540400";
        final String URL = "http://apis.baidu.com/weixinxi/extracter/extracter";
        //请求示例：http://apis.baidu.com/weixinxi/extracter/extracter?url='  -H 'apikey:您自己的apikey'
        String re = "ERROR";
        JSONObject json;
        JSONObject data;
        String url = URL+"?url="+webUrl+"&apikey="+APPKEY;
        try {
            String jsonString = Tools.readStream(new URL(url).openStream());
            json = new JSONObject(jsonString);
            if (json.getString("code").equals("0")) //成功的返回
            {
                data = json.getJSONObject("data");
                re = data.getString("body");
            }
            else return re;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }
    /**
     * @param httpArg
     *            :参数
     * @return 返回结果
     */
    public static String getWebText(String httpArg) {
        final String APPKEY = "62f1925eaea94045ca7c35b175540400";
        String URL = "http://apis.baidu.com/weixinxi/extracter/extracter";
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        URL = URL + "?" + httpArg;

        try {
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  APPKEY);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 借用OKHTTP库异步联网获取数据并借用Gson把数据转存为对象返回
     * @param url url
     * @param cla 类类型
     * @param <T> Gson要转为的类型
     * @return 结果对象
     * @throws Exception 获取失败
     */
    public static <T> T getData(String url,Class<T> cla)  throws Exception
    {
        String jsonString = "";
        jsonString = Tools.readStream(new URL(url).openStream());
        T bean = JsonUtils.deserialize(jsonString,cla);

        return bean;
    }

    public static <T> T getData(String url, Type type)  throws Exception
    {
        String jsonString = "";
        jsonString = Tools.readStream(new URL(url).openStream());
        T bean = JsonUtils.deserialize(jsonString,type);

        return bean;
    }


    /**
     * 从assert文件夹中读取文件
     * @param resName 文件名
     * @return 流
     */
    public static InputStream getAssert(Context context,String resName)
    {
        return context.getClass().getClassLoader().getResourceAsStream("assets/"+ resName);
    }

    /**
     * 用fromJson(String json, Class<T> cls)解析时Gson报类型强制转换错误
     * 后经express结果查询验证得可用fromJson(tring json, Type type),没有报错
     * @param context
     * @return
     */
    // TODO 2016-12-1 fix Gson
    public static ArrayList<CompanyNo> getExpressCompanyNos(Context context ) {

        InputStream is = Tools.getAssert(context,"express_company");

        JSONObject json;
        JSONArray list;
        ArrayList<CompanyNo> data = new ArrayList<>();

        try {
            String jsonString = Tools.readStream(is);
            json = new JSONObject(jsonString);
            list = json.getJSONArray("result");
            for (int i = 0; i < list.length(); i++) {
                CompanyNo companyNo = new CompanyNo();
                JSONObject temp = (JSONObject) list.get(i);
                companyNo.letter = temp.getString("letter");
                companyNo.name = temp.getString("name");
                companyNo.number = temp.getString("number");
                companyNo.tel = temp.getString("tel");
                companyNo.type = temp.getString("type");
                data.add(companyNo);
            }
            return data;

        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从一张图片中抽取出占比最多的颜色
     * @param bitmap 图片
     * @return color
     */
    public static int getLargestAmountsColorOfBitmap(Bitmap bitmap)
    {
        int defaultColor = Color.GRAY;
        if (bitmap == null)
            return defaultColor;
        Palette.Builder from = Palette.from(bitmap);
        return from.generate().getDarkVibrantColor(defaultColor);
    }

    /**
     * 获得一个随机的颜色
     * @return 颜色
     */
    public static int getRandomColor()
    {
        int r = (int)(Math.random()*255); //产生一个255以内的整数
        int g = (int)(Math.random()*255); //产生一个255以内的整数
        int b = (int)(Math.random()*255); //产生一个255以内的整数
        return Color.rgb(r,g,b);
    }

    /**
     * 获得一个比较暗的随机颜色
     * @return 颜色
     */
    public static int getRandomColor_d()
    {
        int r = (int)(Math.random()*100); //产生一个100以内的整数
        int g = (int)(Math.random()*100);
        int b = (int)(Math.random()*100);
        return Color.rgb(r,g,b);
    }

}
