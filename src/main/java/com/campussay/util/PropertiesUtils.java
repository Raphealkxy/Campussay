package com.campussay.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** 操作properties文件类 */
public class PropertiesUtils {
	
	private PropertiesUtils() {
		// 构造方法私有化，外部不能实例化该类 
	}
	
	private static Properties properties ;
	private static Properties wechatProperties ;
	static {
		properties = new Properties();
		wechatProperties = new Properties();
		
		try {
			// 解析文件config.properties
						InputStream alipayStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("alipay.properties");
						properties.load(alipayStream);
						alipayStream.close();
			// 解析文件config.properties
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("campussay.properties");
			properties.load(is);
			is.close();
			
			// 解析文件wechat.properties
			InputStream wechatInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("wechat.properties");
			wechatProperties.load(wechatInputStream);
			wechatInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取properties文件属性
	 * 
	 * @param key
	 *            config.properties文件key
	 * @return config.properties文件key对应value
	 */
	public static String getProp(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * 获取wechat.properties文件属性
	 * 
	 * @param key
	 *            文件key
	 * @return 文件key对应value
	 */
	public static String getWechatValue(String key) {
		return wechatProperties.getProperty(key);
	}
}