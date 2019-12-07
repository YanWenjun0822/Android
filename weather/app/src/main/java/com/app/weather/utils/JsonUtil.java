package com.app.weather.utils;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
* @author: 105032016090颜文君
* @description: 序列化对象为JSON格式 遵循JSON组织公布标准
 * JSON:轻量级的数据交换格式，非常适合于服务器与JavaScript的交互
* @data: 2019/5/26
*/
public class JsonUtil {
	/**
	 *
	 * @param obj 任意对象
	 * @return
	 */
	public static String object2json(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer || obj instanceof Float
				|| obj instanceof Boolean || obj instanceof Short || obj instanceof Double
				|| obj instanceof Long || obj instanceof BigDecimal || obj instanceof BigInteger
				|| obj instanceof Byte) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj));
		} else {
			json.append(bean2json(obj));
		}
		Log.i("json", json.toString());
		return json.toString();
	}

	/**
	 *
	 * @param bean bean对象
	 * @return
	 */
	public static String bean2json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		Field[] fileds = null;
		try {
			fileds = bean.getClass().getDeclaredFields();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fileds != null) {
			for (int i = 0; i < fileds.length; i++) {
				try {
					String name = object2json(fileds[i].getName());

					Method m = (Method) bean.getClass().getMethod(
							"get" + getMethodName(fileds[i].getName()));
					String value = object2json(m.invoke(bean));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * @param list list对象
	 * @return
	 */
	public static String list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}


	/**
	 * @param array 对象数组
	 * @return
	 */
	public static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * @param map map对象
	 * @return
	 */
	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 *
	 * @param set 集合对象
	 * @return
	 */
	public static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 *
	 * @param s 参数
	 * @return
	 */
	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				case '/':
					sb.append("\\/");
					break;
				default:
					if (ch >= '\u0000' && ch <= '\u001F') {
						String ss = Integer.toHexString(ch);
						sb.append("\\u");
						for (int k = 0; k < 4 - ss.length(); k++) {
							sb.append('0');
						}
						sb.append(ss.toUpperCase());
					} else {
						sb.append(ch);
					}
			}
		}
		return sb.toString();
	}
	private static String getMethodName(String fildeName) throws Exception {
		byte[] items = fildeName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}

	/** 按照默认设置, 创建Gson对象 */
	private static Gson createGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
		return builder.create();
	}

	/** 将输入JSON字符串转换为typeOfT所指定的类型对象 */
	public static <T> T fromJson(String json, Type typeOfT) {
		return createGson().fromJson(json, typeOfT);
	}

	/** 将src对象转换为JSON字符串 */
	public static <T> String toJson(T src) {
		return createGson().toJson(src);
	}

	/**
	 * 使用{@link #toJson(Object)}
	 *
	 * @deprecated
	 */
	public static <T> String toJson(T src, Type typeOfSrc) {
		return createGson().toJson(src, typeOfSrc);
	}
	private static Gson gson = new Gson();
	public static <T> T fromJson(String json,Class<T> classOfT){
		return gson.fromJson(json, classOfT);
	}
}