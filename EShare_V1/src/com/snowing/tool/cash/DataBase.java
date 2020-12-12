package com.snowing.tool.cash;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import cn.snowing.io.Filer;
import cn.snowing.io.Text;
import cn.snowing.system.HostOS;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DataBase {

	final public static String Version = "V1.1.1_201212";

	public static String dataUrl = new HostOS().getUserHome()+"//db//";
	public static String databaseUrl = new HostOS().getUserHome()+"//db//eshare.db";

	static Filer file = new Filer();
	static Text text = new Text();

	/**
	 * ��csv�ļ��������ݿ⣬xls�ļ�Ҳ��ͨ��ת������ʹ�ô˷�������
	 * 
	 * @param url csv�ļ�·��
	 * @param platform ��Ʒƽ̨
	 */
	public static void buildCSVData(String url, String platform) {
		int totalNeedBuildItem = 0;//������Ҫ�������ݿ����Ʒ
		int buildedItem = 0;//�Ѿ����ڵ���Ʒ
		int buildSuccessItem = 0;//�ɹ��������ݿ����Ʒ
		int buildFailueItem = 0;//ʧ�ܵ������ݿ����Ʒ
		int bannedItem = 0;//�򲻷���ban_string��δ��¼�����Ʒ
		int discountPaperlessItem = 0;//�����Ż݄���δ��¼�����Ʒ
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
					if(info.length==8&&!"��Ʒ����".equals(itemName)) {
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
					if(!"T��Ʒid".equals(itemID)) {
						String itemTaoKey = info[19];
						try {
							itemTaoKey = "��"+itemTaoKey.split("��")[1]+"��";
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
				id[i] = file.getLineString(databaseUrl, i+1).split(",")[0];
			}
			String[] csvid = new String[(int)file.getLine(url)-1];
			for(int i=0;i<csvid.length;i++) {
				totalNeedBuildItem+=1;
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
					if(info.length==8&&!"��Ʒ����".equals(itemName)) {
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
					if(!"T��Ʒid".equals(itemID)) {
						String itemTaoKey = info[19];
						try {
							itemTaoKey = "��"+itemTaoKey.split("��")[1]+"��";
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
		String[] data = {"����Դ:"+url,"ƽ̨:"+(platform.charAt(0)=='J' ? "����" : (platform.charAt(0)=='T') ? "�Ա�" : "δ֪"),"Դ������Ʒ��Ŀ:"+totalNeedBuildItem+"��","�ɹ��������:"+buildSuccessItem+"��","���ݿ����Ѵ�����Ʒ����:"+buildedItem+"��","ʧ�ܵ������:"+buildFailueItem+"��(���Żݾ�:"+discountPaperlessItem+"��,������ban_string:"+bannedItem+"��)"};
		JOptionPane.showMessageDialog(EShare.frame, data, "�������ݿ�����...", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * ������xls�ļ�Ϊ����Դ�����ݿ⣬
	 * ��Ҫ�������Ա�����
	 * 
	 * @param url xls�ļ���ַ
	 */
	public static void buildXLSData(String url, String platform) {
		//��xlsתΪcsv�ļ�����
		String filename = EShare.tempURL+"xls2csv_"+new Random().nextInt(99999999)+".csv";
		try {
			// ���ļ����л�ȡExcel����������WorkBook��
			Workbook wb = Workbook.getWorkbook(new File(url)); 
			// �ӹ�������ȡ��ҳ��Sheet��,Ĭ�ϵ���һҳ����һҳ
			Sheet sheet = wb.getSheet(0); 
			// ���ԣ�ѭ����ӡExcel���е�����
			for (int i = 0; i < sheet.getRows(); i++) { 
				String line = "";
				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					if(j != sheet.getColumns()-1) {
						line = line + cell.getContents() + ",";
					}
					//System.out.print(cell.getContents()+",");
				}
				if(!file.isExists(filename)) {
					file.createNewFile(filename);
				} else {
					//����
				}
				file.write(filename, line, true);
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//ʹ��csv��ʽ����
		buildCSVData(filename, platform);
	}

	/**
	 * ��ȡ���ݿ�������Ʒ����
	 * 
	 * @return ����Ʒ����
	 */
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

	public static void setItemState(String id, boolean state) {
		int line = text.findLine(databaseUrl, id)[0];
		file.replace(databaseUrl, line, state==true?",x":",o", state==true?",o":",x");
	}
}
