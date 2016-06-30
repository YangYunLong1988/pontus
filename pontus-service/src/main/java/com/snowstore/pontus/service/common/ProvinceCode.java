package com.snowstore.pontus.service.common;

import com.google.common.collect.BiMap;
import com.snowstore.pontus.service.common.util.Function;

public class ProvinceCode {
	private static String code[] = new String[] { "0013", "0014", "0015", "0021", "0022", "0023", "0032", "0033", "0034", "0035", "0036", "0037", "0041", "0042", "0043", "0044", "0045", "0046", "0051", "0052", "0053", "0054", "0061", "0062", "0063", "0064", "0065", "2001",
			"2002", "2003", "0011", "0012", "0050", "0031"

	};
	private static String provinceName[] = new String[] { "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆", "香港", "澳门", "台湾", "北京", "天津", "重庆", "上海" };

	private static BiMap<String, String> privinceCodeMap;

	/**
	 * 通过省份代码码获取省份名称或通过省份名称获取省份代码
	 */
	public static String getCode(String str) {
		if (null == privinceCodeMap) {
			privinceCodeMap = Function.map(code, provinceName);
		}
		if (str.matches("\\d+")) {
			return privinceCodeMap.get(str); // 返回银行名称
		} else {
			return privinceCodeMap.inverse().get(str); // 返回银行编码
		}
	}

}
