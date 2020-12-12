package com.snowing.tool.cash;

import cn.snowing.io.Download;
import cn.snowing.io.Filer;
import cn.snowing.io.Text;

public class SKU {
	
	final public static String Version = "V1.0.1_201211";
	
	static Download dl = new Download();
	static Text text = new Text();
	static Filer file = new Filer();
	public static String getPrice(String url) {
		String outUrl_1 = EShare.tempURL + EShare.itemID + "_preview.tmp";
		String outUrl_2 = EShare.tempURL + EShare.itemID + ".tmp";
		dl.get(url, outUrl_1);
		if(file.isEmpty(outUrl_1)) {
			return "ERROR404";
		} else {
			String[] url_1 = text.grep(outUrl_1, "var hrl=", '\'');
			dl.get(url_1[0], outUrl_2);
			String[] url_2 =  text.grep(outUrl_2, ",\"p\":", '"');
			if(null!=url_2&&url_2.length>0) {
				return url_2[0];
			} else {
				return null;
			}
		}
		
	}
}
