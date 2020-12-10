package com.snowing.tool.cash;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import cn.snowing.system.HostOS;

public class EShare {
	final public static String Version = "V1.0.0_201002";
	final public static int CoreVersion = 100;
	//*************ITEMS DATA*************//
	static String dataUrl = "";//目标源数据文件目录
	static int allItems = 0;//商品总数
	static int copyableItems = 0;//符合条件商品数
	static String itemName = "";//商品名称
	static String itemID = "";//商品ID
	static String itemPrice = "";//商品价格
	static String itemRepayPencent = "";//返点
	static String itemRepay = "";//佣金
	static String itemURL = "";//推送链接
	static String itemDiscountPrice = "";//优惠卷面额
	static String itemDiscountURL = "";//优惠劵链接
	static String itemRealPrice = "";//商品原价
	static String itemTaoKey = "";//商品淘口令
	
	//*************SETTING*************//
	static boolean enableSmartReformat = false;//自动格式化
	static boolean enableAutoRemoveSame = false;//自动去重
	static int leastRepayPoint = 0;//最少返佣率
	static int leastItemPrice = 0;//目标商品最低价格
	static int highestItemPrice = 0;//目标商品最高价格
	public static String tempURL = new HostOS().getUserHome()+"//tmp//";//缓存文件目录位置
	static int autosaveTime = 1000;//自动保存时间(默认1000ms)
	static String autoPushCommand = "";//自动推送参数
	static int pushDelay = 1800;//自动推送时间(默认1800s)
	
	//*************GUI*****************//
	static JFrame frame = new JFrame();
	static JPanel panel = new JPanel();
	public static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public EShare() {
		frame.getContentPane().add(panel);
		frame.setTitle("推广联盟V1 [DEBUG] 本程序遵循GPL_V3开源，禁止倒卖！！！");
		frame.setBounds(ScreenSize.width / 2 - 600 / 2, ScreenSize.height / 2 - 400 / 2, 600, 400);
		frame.setVisible(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Ui.load();
	}
	
	public static void updateItem(String[] itemData) {
		if(null!=itemData) {
			if(itemData[0].charAt(0)=='J') {
				itemName = itemData[1];
				itemID = itemData[0];
				itemPrice = itemData[2];
				itemRepayPencent = itemData[3];
				itemRepay = itemData[4];
				itemURL = itemData[5];
				itemDiscountURL = itemData[6];
			} else if(itemData[0].charAt(0)=='T') {
				itemName = itemData[1];
				itemID = itemData[0];
				itemRealPrice = itemData[2];
				itemRepayPencent = itemData[3];
				itemRepay = itemData[4];
				itemDiscountPrice = itemData[5];
				itemTaoKey = itemData[6];
				itemPrice = "";
				int leftNum1 = 0;
				int rightNum1 = 0;
				int leftNum2 = 0;
				int rightNum2 = 0;
				if(itemRealPrice.indexOf(".")!=-1) {
					leftNum1 = Integer.parseInt(itemRealPrice.split("\\.")[0]);
					rightNum1 = Integer.parseInt(itemRealPrice.split("\\.")[1]);
				} else {
					leftNum1 = Integer.parseInt(itemRealPrice);
				}
				if(itemDiscountPrice.indexOf(".")!=-1) {
					leftNum2 = Integer.parseInt(itemDiscountPrice.split("\\.")[0]);
					rightNum2 = Integer.parseInt(itemDiscountPrice.split("\\.")[1]);
				} else {
					leftNum2 = Integer.parseInt(itemDiscountPrice);
				}
				String a = ""+rightNum1;
				String b = ""+rightNum2;
				if(a.length()<b.length()) {
					rightNum1 = rightNum1*(b.length()-a.length())*10;
				} else if(a.length()>b.length()) {
					rightNum2 = rightNum2*(a.length()-b.length())*10;
				}
				int c = rightNum1-rightNum2;
				String d = ""+c;
				if(c<0) {
					leftNum1 = leftNum1 - 1;
					c = (10*(d.length()-1)) -c;
				}
				itemPrice = leftNum1-leftNum2+"."+c;
			}
		}
	}
	
	public static void main(String[] ages) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new EShare();
	}
}
