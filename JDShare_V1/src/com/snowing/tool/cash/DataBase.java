package com.snowing.tool.cash;

import java.util.ArrayList;
import java.util.List;

import cn.snowing.io.Filer;
import cn.snowing.io.Text;
import cn.snowing.system.HostOS;

public class DataBase {
	
	final public static String Version = "V1.0.0_200904";
	
	public static String dataUrl = new HostOS().getUserHome()+"//db//";
	public static String debugUrl = new HostOS().getUserHome()+"//db//jd.db";
	
	static Filer file = new Filer();
	static Text text = new Text();
	
	public static void buildCSVData(String url) {
		if(!file.isExists(dataUrl)) {
			file.mkdir(dataUrl);
			file.createNewFile(debugUrl);
		} else if(file.isExists(dataUrl)&&!file.isExists(debugUrl)) {
			file.createNewFile(debugUrl);
		}
		if(file.isEmpty(debugUrl)) {
			for(String line : file.getAllLineString(url)) {
				String[] info = line.replace("\t", "").split(",");
				String itemName = info[0];
				String itemSourcesUrl = info[1];
				String itemPrice = info[3];
				String itemRepayPencent = info[4];
				String itemRepay = info[5];
				String itemURL = info[6];
				if(info.length==8&&!"商品名称".equals(itemName)) {
					String itemDiscountURL = info[7];
					itemName = Rebuild.itemName(itemName);
					String itemID = itemSourcesUrl = itemSourcesUrl.replace("	", "").replace("http://item.jd.com/", "").replace(".html", "");
					file.write(debugUrl, itemID+","+itemName+","+itemPrice+","+itemRepayPencent+","+itemRepay+","+itemURL+","+itemDiscountURL+","+"o", true);
				}
			}
		} else {
			String[] id = new String[(int)file.getLine(debugUrl)];
			for(int i=0;i<id.length;i++) {
				id[i] = file.getLineString(debugUrl, i+1).split(",")[0];
			}
			String[] csvid = new String[(int)file.getLine(url)-1];
			for(int i=0;i<csvid.length;i++) {
				csvid[i] = file.getLineString(url, i+2).split(",")[1].replace("	", "").replace("http://item.jd.com/", "").replace(".html", "");
			}
			List<String> usefulLine = new ArrayList<String>();
			for(int i=0;i<csvid.length;i++) {
				boolean match = false;
				for(int x=0;x<id.length;x++) {
					if(csvid[i].equals(id[x])) {
						match = true;
					}
				}
				if(!match) {
					usefulLine.add(""+(i+2));
				}
			}
			int[] usefulLine_int = new int[usefulLine.size()];
			for(int i=0;i<usefulLine_int.length;i++) {
				usefulLine_int[i] = Integer.parseInt(usefulLine.get(i));
			}
			for(int i=0;i<usefulLine_int.length;i++) {
				String[] info = file.getLineString(url, usefulLine_int[i]).replace("\t", "").split(",");
				String itemName = info[0];
				String itemSourcesUrl = info[1];
				String itemPrice = info[3];
				String itemRepayPencent = info[4];
				String itemRepay = info[5];
				String itemURL = info[6];
				if(info.length==8&&!"商品名称".equals(itemName)) {
					String itemDiscountURL = info[7];
					itemName = Rebuild.itemName(itemName);
					String itemID = itemSourcesUrl = itemSourcesUrl.replace("	", "").replace("http://item.jd.com/", "").replace(".html", "");
					file.write(debugUrl, itemID+","+itemName+","+itemPrice+","+itemRepayPencent+","+itemRepay+","+itemURL+","+itemDiscountURL+","+"o", true);
				}
			}
		}
	}
	
	public static int getAllItemsNums() {
		if(!file.isExists(dataUrl)) {
			file.mkdir(dataUrl);
			file.createNewFile(debugUrl);
		} else if(file.isExists(dataUrl)&&!file.isExists(debugUrl)) {
			file.createNewFile(debugUrl);
		}
		return (int)file.getLine(debugUrl);
	}
	
	public static String[] getEnableItemsID() {
		if(file.isExists(debugUrl)) {
			List<String> id = new ArrayList<String>();
			for(String line : file.getAllLineString(debugUrl)) {
				if(!"x".equals(line.split(",")[7])) {
					id.add(line.split(",")[0]);
				}
			}
			String[] result = new String[id.size()];
			for(int i=0;i<result.length;i++) {
				result[i] = id.get(i);
			}
			return result;
		} else {
			return null;
		}
	}
	
	public static int getEnableItemsNum() {
		String[] data = getEnableItemsID();
		int result = 0;
		if(null!=data) {
			result = data.length;
		}
		return result;
	}
	
	public static String[] getEnableItem() {
		for(String line : file.getAllLineString(debugUrl)) {
			if(!"x".equals(line.split(",")[7])) {
				String[] result = line.split(",");
				return result;
			}
		}
		return null;
	}
	
	public static String[] getItemData(String id) {
		for(String line : file.getAllLineString(debugUrl)) {
			if(line.split(",")[0].equals(id)) {
				String[] result = line.split(",");
				return result;
			}
		}
		return null;
	}
	
	public String toShare(String id) {
		
		return null;
	}
	
	public String[] getCSV(String url) {
		return null;
	}
	
	public static void setItemState(String id, boolean state) {
		int line = text.findLine(debugUrl, id)[0];
		if(state) {
			file.replace(debugUrl, line, ",x", ",o");
		} else {
			file.replace(debugUrl, line, ",o", ",x");
		}
		
	}
}
