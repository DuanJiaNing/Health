package com.example.ai.forhealth.utils;

/**
 * Created by ai on 2016/12/2.
 */

public class CharUtils {

    // 根据Unicode编码完美的判断中文汉字和符号
    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 快递查询中把JSON中获得的电话号码字符串中的电话号码取出
     * @param res 字符串
     * @return 字符串数组
     */
    public String[] spitExpressPhone(String res) {

        //数字0 48 数字9 57
        String[] strs;
        if (!res.contains(" "))   /**没有空格*/
        // 400-694-1256
        // 台北：+886-2-25016988澳门：00853-28520722福建：0592-5569715广东：0769-88763939
        //95338
        {
            boolean china = false;
            for (int i = 0;i < res.length();i++) {
                if (isChinese(res.charAt(i)))
                {
                    china = true;
                    break;
                }
            }
            if (china)//包含中文
            {
                String[] te = res.split("：");
                strs = new String[te.length-1];
                for (int i = 1; i < te.length; i++) { //有几个电话号码（把第一个舍弃）
                    StringBuilder builder = new StringBuilder();
                    for (int j = 0; j < te[i].length(); j++) //遍历每一个号码的每一个字符
                    {
                        char tem = te[i].charAt(j);
                        if (tem >= 48 && tem <= 57) //是数字
                            builder.append(tem);
                    }
                    strs[i-1] = new String(builder);
                }
                return strs;
            }
            else
            {
                StringBuilder builder = new StringBuilder();
                strs = new String[1];
                for (int j = 0; j < res.length(); j++) //遍历每一个号码的每一个字符
                {
                    char tem = res.charAt(j);
                    if (tem >= 48 && tem <= 57) //是数字
                        builder.append(tem);
                }
                strs[0] = new String(builder);
                return strs;
            }
        }

        else /**有空格*/
        // 021-69777888 021-69777999
        // 0755-29778899 \/ 29778100
        {
            String[] te = res.split(" ");
            strs = new String[te.length];
            for (int i = 0; i < te.length; i++) { //有几个电话号码
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < te[i].length(); j++) //遍历每一个号码的每一个字符
                {
                    char tem = te[i].charAt(j);
                    if (tem >= 48 && tem <= 57) //是数字
                        builder.append(tem);
                }
                strs[i] = new String(builder);
            }
            return strs;
        }
    }
}

