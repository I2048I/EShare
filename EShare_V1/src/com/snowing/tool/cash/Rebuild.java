package com.snowing.tool.cash;

import cn.snowing.io.Filer;
import cn.snowing.system.HostOS;

public class Rebuild {
	final public static String Version = "V1.0.0_201210";
	
	static Filer file = new Filer();
	
	public static String itemName(String str) {
		
		return str;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean wordsBan(String str) {
		final String banstrUrl = new HostOS().getUserHome()+"\\conf\\ban_string.txt";
		for(String line:file.getAllLineString(banstrUrl)) {
			if(line.length()>0) {
				for(int i=0;i<str.length()-(line.length()-1);i++) {
					String target = "";
					for(int x=0;x<line.length();x++) {
						target = target + str.charAt(i+x);
					}
					if(target.equals(line)) {
						return true;
					}
				}
			} else {
				return true;
			}
		}
		return false;
	}
}
