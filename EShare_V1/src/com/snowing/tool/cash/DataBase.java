package com.snowing.tool.cash;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import cn.snowing.io.Filer;
import cn.snowing.io.Text;
import cn.snowing.system.HostOS;

public class DataBase {

	final public static String Version = "V1.0.1_201211";

	public static String dataUrl = new HostOS().getUserHome()+"//db//";
	public static String databaseUrl = new HostOS().getUserHome()+"//db//eshare.db";

	static Filer file = new Filer();
	static Text text = new Text();

	public static void buildCSVData(String url, String platform) {
		int totalNeedBuildItem = 0;//所有需要建立数据库的商品
		int buildedItem = 0;//已经存在的商品
		int buildSuccessItem = 0;//成功导入数据库的商品
		int buildFailueItem = 0;//失败导入数据库的商品
		int bannedItem = 0;//因不符合ban_string而未被录入的商品
		int discountPaperlessItem = 0;//因无优惠劵而未被录入的商品
		if(!file.isExists(dataUrl)) {
			file.mkdir(dataUrl);
			file.createNewFile(databaseUrl);
		} else if(file.isExists(dataUrl)&&!file.isExists(databaseUrl)) {
			file.createNewFile(databaseUrl);
		}
		if(file.isEmpty(databaseUrl)) {
			for(String line : file.getAllLineString(url)) {
				totalNeedBuildItem+=1;
				String[] info = line.replace("\t", "").split(",");
				if(platform.equals("JD")) {
					String itemName = info[0];
					String itemSourcesUrl = info[1];
					String itemPrice = info[3];
					String itemRepayPencent = info[4];
					String itemRepay = info[5];
					String itemURL = info[6];
					if(info.length==8&&!"商品名称".equals(itemName)) {
						String itemDiscountURL = info[7];
						itemName = Rebuild.itemName(itemName);
						String itemID = "J"+itemSourcesUrl.replace("	", "").replace("http://item.jd.com/", "").replace(".html", "");
						if(!Rebuild.wordsBan(itemName)) {
							buildSuccessItem+=1;
							file.write(databaseUrl, itemID+","+itemName+","+itemPrice+","+itemRepayPencent+","+itemRepay+","+itemURL+","+itemDiscountURL+","+"o", true);
						} else {
							bannedItem+=1;
							System.out.println("ID:"+itemID+" has some word be banned.");
						}
					}
				} else if(platform.equals("TB")){
					String itemID = "T"+info[0];
					String itemName = info[1];
					String itemPrice = info[5];
					String itemRepayPencent = info[7];
					String itemRepay = info[8];
					String itemDiscountPrice = info[15];
					if(!"T商品id".equals(itemID)) {
						String itemTaoKey = info[19];
						try {
							itemTaoKey = "￥"+itemTaoKey.split("￥")[1]+"￥";
						}catch(Exception e){
							discountPaperlessItem+=1;
							System.out.println("ID:"+itemID+" do not have discount paper.");
							continue;
						}
						itemName = Rebuild.itemName(itemName);
						if(!Rebuild.wordsBan(itemName)) {
							buildSuccessItem+=1;
							file.write(databaseUrl, itemID+","+itemName+","+itemPrice+","+itemRepayPencent+","+itemRepay+","+itemDiscountPrice+","+itemTaoKey+","+"o", true);
						} else {
							bannedItem+=1;
							System.out.println("ID:"+itemID+" has some word be banned.");
						}
					}
				} else {
					buildFailueItem+=1;
					System.out.println("Unknown item kinds");
				}

			}
		} else {
			String[] id = new String[(int)file.getLine(databaseUrl)];
			for(int i=0;i<id.length;i++) {
				totalNeedBuildItem+=1;
				id[i] = file.getLineString(databaseUrl, i+1).split(",")[0];
			}
			String[] csvid = new String[(int)file.getLine(url)-1];
			for(int i=0;i<csvid.length;i++) {
				if(platform.equals("JD")) {
					csvid[i] = "J"+file.getLineString(url, i+2).split(",")[1].replace("	", "").replace("http://item.jd.com/", "").replace(".html", "");
				} else if(platform.equals("TB")) {
					csvid[i] = "T"+file.getLineString(url, i+2).split(",")[0];
				}
			}
			List<String> usefulLine = new ArrayList<String>();
			for(int i=0;i<csvid.length;i++) {
				boolean match = false;
				for(int x=0;x<id.length;x++) {
					if(csvid[i].equals(id[x])) {
						buildedItem+=1;
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
				if(platform.equals("JD")) {
					String itemName = info[0];
					String itemSourcesUrl = info[1];
					String itemPrice = info[3];
					String itemRepayPencent = info[4];
					String itemRepay = info[5];
					String itemURL = info[6];
					if(info.length==8&&!"商品名称".equals(itemName)) {
						String itemDiscountURL = info[7];
						itemName = Rebuild.itemName(itemName);
						String itemID = "J"+itemSourcesUrl.replace("	", "").replace("http://item.jd.com/", "").replace(".html", "");
						if(!Rebuild.wordsBan(itemName)) {
							buildSuccessItem+=1;
							file.write(databaseUrl, itemID+","+itemName+","+itemPrice+","+itemRepayPencent+","+itemRepay+","+itemURL+","+itemDiscountURL+","+"o", true);
						} else {
							bannedItem+=1;
							System.out.println("ID:"+itemID+" has some word be banned.");
						}
					}
				} else if(platform.equals("TB")){
					String itemID = "T"+info[0];
					String itemName = info[1];
					String itemPrice = info[5];
					String itemRepayPencent = info[7];
					String itemRepay = info[8];
					String itemDiscountPrice = info[15];
					if(!"T商品id".equals(itemID)) {
						String itemTaoKey = info[19];
						try {
							itemTaoKey = "￥"+itemTaoKey.split("￥")[1]+"￥";
						}catch(Exception e){
							discountPaperlessItem+=1;
							System.out.println("ID:"+itemID+" do not have discount paper.");
							continue;
						}
						itemName = Rebuild.itemName(itemName);
						if(!Rebuild.wordsBan(itemName)) {
							buildSuccessItem+=1;
							file.write(databaseUrl, itemID+","+itemName+","+itemPrice+","+itemRepayPencent+","+itemRepay+","+itemDiscountPrice+","+itemTaoKey+","+"o", true);
						} else {
							bannedItem+=1;
							System.out.println("ID:"+itemID+" has some word be banned.");
						}
					}
				}
			}
		}
		buildFailueItem = buildFailueItem + bannedItem + discountPaperlessItem;
		String[] data = {"数据源:"+url,"平台:"+platform,"源数据商品数目:"+totalNeedBuildItem+"个","成功导入个数:"+buildSuccessItem+"个","数据库中已存在商品个数:"+buildedItem+"个","失败导入个数:"+buildFailueItem+"个(无优惠卷:"+discountPaperlessItem+"个,不符合ban_string:"+discountPaperlessItem+"个)"};
		JOptionPane.showMessageDialog(EShare.frame, data, "导入数据库详情...", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * 建立以xls文件为数据源的数据库
	 * 主要适用于淘宝联盟
	 * 
	 * @param url xls文件地址
	 */
	public static void buildXLSData(String url, String platform) {

	}

	public static int getAllItemsNums() {
		if(!file.isExists(dataUrl)) {
			file.mkdir(dataUrl);
			file.createNewFile(databaseUrl);
		} else if(file.isExists(dataUrl)&&!file.isExists(databaseUrl)) {
			file.createNewFile(databaseUrl);
		}
		return (int)file.getLine(databaseUrl);
	}

	public static String[] getEnableItemsID() {
		if(file.isExists(databaseUrl)) {
			List<String> id = new ArrayList<String>();
			for(String line : file.getAllLineString(databaseUrl)) {
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
		for(String line : file.getAllLineString(databaseUrl)) {
			if(!"x".equals(line.split(",")[7])) {
				String[] result = line.split(",");
				return result;
			}
		}
		return null;
	}
	
	public static String[] getRandomEnableItem() {
		if(getEnableItemsID().length>0) {
			Random rand = new Random();
			int itemNum = (getEnableItemsID().length-1)>1 ? rand.nextInt(getEnableItemsID().length-1) : 0;
			String[] result = getItemData(getEnableItemsID()[itemNum]);
			return result;
		} else {
			return null;
		}
	}

	public static String[] getItemData(String id) {
		for(String line : file.getAllLineString(databaseUrl)) {
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
		int line = text.findLine(databaseUrl, id)[0];
		if(state) {
			file.replace(databaseUrl, line, ",x", ",o");
		} else {
			file.replace(databaseUrl, line, ",o", ",x");
		}

	}
}
