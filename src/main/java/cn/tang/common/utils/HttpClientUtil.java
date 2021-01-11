package cn.tang.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @Auther: tangpd
 * @Date: 2020/12/29 09:57
 * @Description:
 */
@Slf4j
public class HttpClientUtil {

    public static final int PARAM_TYPE_XWWWFORMURLENCODED = 1;
    public static final int PARAM_TYPE_JSON = 2;

    /**
     *
     * 功能描述: 获取客户端的ip地址；
     *
     * @param: [request]
     * @return: java.lang.String
     * @auther: tangpd
     * @date: 2020/12/29 17:56
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.indexOf(",") != -1) {// 多次反向代理后会有多个ip值，第一个ip才是真实ip
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }


    /**
     *
     * 功能描述: 发送 get 请求，目前接收处理 200、302 结果；其他状态返回 null;
     *
     * @param: [url, requestHeaderJson]
     * @return: com.alibaba.fastjson.JSONObject
     * @auther: tangpd
     * @date: 2020/12/29 17:57
     */
    public static JSONObject getJsonForGetRequest(String url, JSONObject requestHeaderJson) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            // 1、创建httpClient实例、创建HttpGet远程连接实例
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);

            // 2、为HttpGet实例配置请求参数实例、设置请求头信息
            setConfigAndRequestHeader(httpGet, requestHeaderJson);
            httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");//鉴权

            // 3、执行get请求得到返回对象
            httpResponse = httpClient.execute(httpGet);

            // 4、获取 http 请求结果并返回
            return getHttpResult(httpResponse);
        } catch (IOException e) {
            log.error("\n发送请求失败*************************（）" + e.getMessage());
        } finally {
            // 5、关闭资源
            closeSource(httpResponse, httpClient);
        }
        return null;
    }

    /**
     *
     * 功能描述: 发送 post 请求，目前接收处理 200、302 结果；其他状态返回 null;
     *
     * @param: [url, requestHeaderJson, paramJson, paramDataType]
     * @return: com.alibaba.fastjson.JSONObject
     * @auther: tangpd
     * @date: 2020/12/29 17:57
     */
    public static JSONObject getJsonForPostRequest(String url, JSONObject requestHeaderJson, JSONObject paramJson, int paramDataType) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            // 1、创建httpClient实例、创建httpPost远程连接实例
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            // 2、为httpPost实例配置请求参数实例、设置请求头信息
            setConfigAndRequestHeader(httpPost, requestHeaderJson);

            // 3、封装post请求参数
            if (PARAM_TYPE_JSON == paramDataType){
                httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
                if (null != paramJson && paramJson.size() > 0){
                    httpPost.setEntity(new StringEntity(paramJson.toJSONString(), "UTF-8"));
                }
            }else if (PARAM_TYPE_XWWWFORMURLENCODED == paramDataType){
                httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                if (null != paramJson && paramJson.size() > 0) {
                    List<NameValuePair> nvps = new ArrayList();
                    paramJson.forEach((k, v) -> {
                        nvps.add(new BasicNameValuePair(k, v.toString()));
                    });
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                }
            }else {
                log.error("无效的 Content-Type 类型，请检查校验");
                return null;
            }

            // 4、httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);

            // 5、获取 http 请求结果并返回
            return getHttpResult(httpResponse);
        } catch (IOException e) {
            log.error("\n发送请求失败*************************（）" + e.getMessage());
        } finally {
            // 6、关闭资源
            closeSource(httpResponse, httpClient);
        }
        return null;
    }

    //=====================================================================================================================================================================
    //=====================================================================================================================================================================

    //为httpPost实例配置请求参数实例、设置请求头信息
    private static void setConfigAndRequestHeader(HttpRequestBase httpPostOrGet, JSONObject requestHeaderJson) {
        // 为httpPost实例配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(15000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(15000)// 设置连接请求超时时间
                .setSocketTimeout(30000)// 设置读取数据连接超时时间
                .build();
        httpPostOrGet.setConfig(requestConfig);
        // 设置请求头
        if (null != requestHeaderJson) {
            requestHeaderJson.forEach((k, v) -> {
                httpPostOrGet.addHeader(k, v.toString());
            });
        }
    }

    // 获取 http 请求结果
    private static JSONObject getHttpResult(CloseableHttpResponse httpResponse) throws IOException {
        //1、获取响应头中的 responseHeaders
        Map<String, List<String>> responseHeaderMap = new HashMap<>();
        List<String> cookieList = new ArrayList<>();
        for (Header header : httpResponse.getAllHeaders()) {
            if ("Set-Cookie".equals(header.getName())) {
                cookieList.add(header.getValue());
                continue;
            }
            responseHeaderMap.put(header.getName(), Arrays.asList(header.getValue()));
        }
        responseHeaderMap.put("cookieList", cookieList);
        //2、拼装返回参数
        int responseStatus = httpResponse.getStatusLine().getStatusCode();
        if (responseStatus == 200 || responseStatus == 302) {
            JSONObject responseData = JSONObject.parseObject(EntityUtils.toString(httpResponse.getEntity(), "UTF-8"));
            JSONObject resultMap = new JSONObject();
            resultMap.put("responseData", responseData);
            resultMap.put("responseHeaderMap", responseHeaderMap);
            resultMap.put("responseStatus", responseStatus);
            return resultMap;
        }
        return null;
    }

    // 关闭资源
    private static void closeSource(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
        if (null != httpResponse) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
