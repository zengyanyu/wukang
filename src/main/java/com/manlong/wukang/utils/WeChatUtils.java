package com.manlong.wukang.utils;

import com.manlong.wukang.bean.BaseData;
import com.manlong.wukang.bean.MyX509TrustManager;
import com.manlong.wukang.bean.material.*;
import com.manlong.wukang.bean.menu.Menu;
import com.manlong.wukang.bean.other.Template;
import com.manlong.wukang.bean.other.Token;
import com.manlong.wukang.bean.other.WechatUserInfo;
import com.manlong.wukang.bean.other.WeixinOauth2Token;
import com.manlong.wukang.config.WeChatConfig;
import com.manlong.wukang.entity.wechat.Wx_config;
import com.manlong.wukang.mapper.wechat.Wx_configMapper;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

@Component
public class WeChatUtils {

    @Autowired
    WeChatConfig wechatConfig;

    private static Logger log = LoggerFactory.getLogger(WeChatUtils.class);

    public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public final static String sns_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public final static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    public final static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    public final static String user_info_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";

    public final static String jsapi_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    public final static String reminder_template_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    public final static String material_list_url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";

    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        }catch (ConnectException ce) {
            log.error("连接超时：{}", ce);
        } catch (Exception e) {
            log.error("https请求异常：{}", e);
        }
        return jsonObject;
    }

    public static Token getToken(String appid, String appsecret) {
        Token token = null;
        String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                token = new Token();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInt("expires_in"));
            } catch (JSONException e) {
                token = null;
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return token;
    }

    public static WeixinOauth2Token getOauth2AccessToken(String code) {
        WeixinOauth2Token wat = null;
        //System.out.println("sns_token_url:"+sns_token_url);
        //System.out.println("code:"+code);
        String url = sns_token_url.replace("APPID", BaseData.WECHAT_APPID).replace("SECRET", BaseData.WECHAT_APPSECRET).replace("CODE", code);
        JSONObject jsonObject = WeChatUtils.httpsRequest(url, "GET", null);
        if (null != jsonObject) {
            try {
                wat = new WeixinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                // TODO: handle exception
                wat = null;
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return wat;
    }


    public static int createMenu(Menu menu, String accessToken) {
        int result = 0;
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        String jsonMenu = JSONObject.fromObject(menu).toString();
        JSONObject jsonObject = httpsRequest(url, "POST", jsonMenu);
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }

    public static int deleteMenu(Menu menu, String accessToken) {
        int result = 0;
        String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);
        String jsonMenu = JSONObject.fromObject(menu).toString();
        JSONObject jsonObject = httpsRequest(url, "POST", jsonMenu);
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("删除菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }

    public static WechatUserInfo getUserInfo(String accessToken, String openId){
        WechatUserInfo wechatUserInfo = null;
        String url = user_info_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        JSONObject jsonObject = httpsRequest(url, "GET", null);
        if(null != jsonObject){
            try {
                wechatUserInfo = new WechatUserInfo();
                wechatUserInfo.setOpenId(jsonObject.getString("openid"));
                // 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
                wechatUserInfo.setSubscribe(jsonObject.getInt("subscribe"));
                // 用户关注时间
                wechatUserInfo.setSubscribeTime(jsonObject.getString("subscribe_time"));
                // 昵称
                wechatUserInfo.setNickname(jsonObject.getString("nickname"));
                // 用户的性别（1是男性，2是女性，0是未知）
                wechatUserInfo.setSex(jsonObject.getInt("sex"));
                // 用户所在国家
                wechatUserInfo.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                wechatUserInfo.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                wechatUserInfo.setCity(jsonObject.getString("city"));
                // 用户的语言，简体中文为zh_CN
                wechatUserInfo.setLanguage(jsonObject.getString("language"));
                // 用户头像
                wechatUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
            } catch (Exception e) {
                // TODO: handle exception
                if (0 == wechatUserInfo.getSubscribe()) {
                    log.error("用户{}已取消关注", wechatUserInfo.getOpenId());
                } else {
                    int errorCode = jsonObject.getInt("errcode");
                    String errorMsg = jsonObject.getString("errmsg");
                    log.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
                }
            }
        }
        return wechatUserInfo;
    }

    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, Object> getWxConfig(HttpServletRequest request,Wx_configMapper wx_configMapper){
        Map<String, Object> ret = new HashMap<String, Object>();
        String requestUrl = request.getRequestURL().toString();

        Wx_config lastWx_config = wx_configMapper.getLastWx_config();

        String jsapi_ticket = lastWx_config.getJsapi_ticket();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = UUID.randomUUID().toString();
        String signature = "";
        String sign = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr+ "&timestamp=" + timestamp + "&url=" + requestUrl;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sign.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ret.put("appId", BaseData.WECHAT_APPID);
        ret.put("timestamp", timestamp);
        ret.put("nonceStr", nonceStr);
        ret.put("signature", signature);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;

    }

    public static JSONObject sendTemplate(Template template, String token){
        String url = reminder_template_url.replace("ACCESS_TOKEN", token);
        String jsonTemplate = JSONObject.fromObject(template).toString();
        JSONObject jsonObject = httpsRequest(url, "POST", jsonTemplate);
        return jsonObject;
    }

    public static Result_material_list getAllMaterialList(Param_material_list param, String token){
        String url = material_list_url.replace("ACCESS_TOKEN", token);
        String jsonParam = JSONObject.fromObject(param).toString();
        JSONObject jsonObject = httpsRequest(url, "POST", jsonParam);
        Map<String,Class> classMap = new HashMap<String, Class>();
        classMap.put("item", Material_item.class);
        classMap.put("content", Material_content.class);
        classMap.put("news_item", News_item.class);
        Result_material_list rml = (Result_material_list) JSONObject.toBean(jsonObject, Result_material_list.class,classMap);
        return rml;
    }

    public void excuteWechatTask(Wx_configMapper wx_configMapper){
        // System.out.println("开始执行wechat任务");

        Token token = WeChatUtils.getToken(BaseData.WECHAT_APPID, BaseData.WECHAT_APPSECRET);

        if(token!=null){
            String url = WeChatUtils.jsapi_url.replace("ACCESS_TOKEN", token.getAccessToken());
            JSONObject json = WeChatUtils.httpsRequest(url, "GET", null);
            String jsapi_ticket = json.getString("ticket");
            Wx_config wx_config = new Wx_config();
            wx_config.setAccess_token(token.getAccessToken());
            wx_config.setJsapi_ticket(jsapi_ticket);
            wx_config.setCreate_time(new Timestamp(new Date().getTime()));
            int resultCount = wx_configMapper.insertWx_config(wx_config);
            if(resultCount>0){
                log.info("获取access_token成功，有效时长{}秒 token:{}", token.getExpiresIn(), token.getAccessToken());
                log.info("获取jsapi_ticket成功，有效时长{}秒 jsapi_ticket:{}", token.getExpiresIn(), jsapi_ticket);
            }
        }else{
            log.info("获取失败");
        }
    }
}
