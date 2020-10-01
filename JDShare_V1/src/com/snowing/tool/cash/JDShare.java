package com.snowing.tool.cash;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import cn.snowing.system.HostOS;

public class JDShare {
	final public static String Version = "V1.0.0_200905";
	final public static int CoreVersion = 100;
	//*************ITEMS DATA*************//
	static String CSVUrl = "";//目标CSV文件目录
	static int allItems = 0;//商品总数
	static int copyableItems = 0;//符合条件商品数
	static String itemName = "";//商品名称
	static String itemID = "";//商品ID
	static String itemPrice = "";//商品价格
	static String itemRepayPencent = "";//返点
	static String itemRepay = "";//佣金
	static String itemURL = "";//推送链接
	static String itemDiscountURL = "";//优惠劵链接
	static String itemRealPrice = "";//商品原价
	
	//*************SETTING*************//
	static boolean enableSmartReformat = false;//自动格式化
	static boolean enableAutoRemoveSame = false;//自动去重
	static int leastRepayPoint = 0;//最少返佣率
	static int leastItemPrice = 0;//目标商品最低价格
	static int highestItemPrice = 0;//目标商品最高价格
	public static String tempURL = new HostOS().getUserHome()+"//tmp//";//缓存文件目录位置
	static int autosaveTime = 1000;//自动保存时间(默认1000ms)
	
	//*************GUI*****************//
	static JFrame frame = new JFrame();
	static JPanel panel = new JPanel();
	public static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	//**********THREAD DEFINE*********//
	AutoSave autosave = new AutoSave();
	
	public JDShare() {
		frame.getContentPane().add(panel);
		frame.setTitle("推广联盟V1 载入->京东配置");
		frame.setBounds(ScreenSize.width / 2 - 600 / 2, ScreenSize.height / 2 - 400 / 2, 600, 400);
		frame.setVisible(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		autosave.start();
		Ui.load();
	}
	
	public static void updateItem(String[] itemData) {
		if(null!=itemData) {
			itemName = itemData[1];
			itemID = itemData[0];
			itemPrice = itemData[2];
			itemRepayPencent = itemData[3];
			itemRepay = itemData[4];
			itemURL = itemData[5];
			itemDiscountURL = itemData[6];
		}
	}
	
	public static void main(String[] ages) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new JDShare();
	}
}
