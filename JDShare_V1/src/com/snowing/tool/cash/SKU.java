package com.snowing.tool.cash;

import cn.snowing.io.Download;
import cn.snowing.io.Text;

public class SKU {
	
	final public static String Version = "V1.0.0_200904";
	
	static Download dl = new Download();
	static Text text = new Text();
	public static String getPrice(String url) {
		String outUrl_1 = JDShare.tempURL + JDShare.itemID + "_preview.tmp";
		String outUrl_2 = JDShare.tempURL + JDShare.itemID + ".tmp";
		dl.get(url, outUrl_1);
		String[] url_1 = text.grep(outUrl_1, "var hrl=", '\'');
		dl.get(url_1[0], outUrl_2);
		String[] url_2 =  text.grep(outUrl_2, ",\"sku_price\":", '"');
		if(null!=url_2&&url_2.length>0) {
			return url_2[0];
		} else {
			return null;
		}
	}
}
