/**
 * OGPayServer
 */
package com.og.platform.pay.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.payinfo.net.log.LoggerFactory;

/**
 * @author ogplayer.com
 *
 * 2013年10月31日
 */
public class HttpTool {
	private static Logger logger = LoggerFactory.getLogger(HttpTool.class);
	
	
	public static String sendHttp(String urlStr, String httpReq) {
		return sendHttp(urlStr, httpReq, "utf8");
	}
	
	public static String sendHttp(String urlStr, String httpReq, String charset) {
		logger.info("指令url=" + urlStr);
		StringBuffer temp = new StringBuffer();
		try {
			String _url = urlStr;
			URL url = new URL(_url);
			// 请求配置，可根据实际情况采用灵活配置
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(1000 * 60);
			connection.setConnectTimeout(1000 * 60);
			// 请求的方法 Get or Post
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 相关请求头
			connection.setRequestProperty("Charsert", charset);
			connection.setRequestProperty("Content-Type", "text/plain");
			// 写入请求实体
			connection.getOutputStream().write(httpReq.getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			// 进去连接
			connection.connect();
			// 响应
			InputStream in = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, charset));
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			connection.disconnect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("指令结果=" + temp.toString());
		return temp.toString();
	}
	
	/** 沃勤特殊处理*/
	public static String sendHttp1(String addr, String content, String ip) {
		URL url;
		StringBuffer returnxml = new StringBuffer();
		try {
			url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			conn.setRequestProperty("remote-host-c", ip);
			// String timeout = XmlTool.getInstance().getValue("timeout");
			String timeout = "3000";
			conn.setConnectTimeout(Integer.parseInt(timeout));
			conn.setReadTimeout(Integer.parseInt(timeout));
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.getOutputStream().write(content.getBytes("UTF-8"));
			conn.getOutputStream().flush();
			conn.getOutputStream().close();
			InputStream in = conn.getInputStream();
			InputStreamReader ireader = new InputStreamReader(in, "UTF-8");
			java.io.BufferedReader reader = new java.io.BufferedReader(ireader);
			String s = "";
			while ((s = reader.readLine()) != null) {
				returnxml.append(s + "\n");
			}
			reader.close();
		} catch (MalformedURLException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
		return returnxml.toString().trim();
	}
	
	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.setReadTimeout(1000 * 5);
			connection.setConnectTimeout(1000 * 5);
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	
	public static String simpleSendHttp(String addr,String charset){    
        String result = "";    
        try {    
            URL url = new URL(addr);    
            URLConnection connection = url.openConnection();    
            connection.connect();    
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),charset));    
            String line;    
            while((line = reader.readLine())!= null){     
                result += line;    
                result += "\n";
            }
        } catch (Exception e) {    
            e.printStackTrace();    
            return "";
        }
        return result;
    }
}
