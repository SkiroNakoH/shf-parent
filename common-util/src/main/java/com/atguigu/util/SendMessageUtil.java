package com.atguigu.util;

import com.atguigu.util.QiniuUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;


@Component
public class SendMessageUtil {

    public static String accessKey;
    public static String secretKey;

    static {
        try {
            Properties properties = new Properties();

            InputStream is = QiniuUtils.class.getClassLoader().getResourceAsStream("qiniu.properties");
            properties.load(is);

            accessKey = properties.getProperty("accessKey");
            secretKey = properties.getProperty("secretKey");
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送手机验证码
     */
    public static Boolean sendMessageCheck(String phone, Map<String, String> map){

        Auth auth = Auth.create(accessKey, secretKey);
        SmsManager smsManager = new SmsManager(auth);
        try {
            //七牛云短信需要企业注册
            String TemplateId = "1";

            Response resp = smsManager.sendMessage(TemplateId, new String[]{phone} , map);

            if(resp.statusCode == 200){
                return true;
            }else {
                return false;
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return false;
    }

}