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
	static String dataUrl = "";//Ŀ��Դ�����ļ�Ŀ¼
	static int allItems = 0;//��Ʒ����
	static int copyableItems = 0;//����������Ʒ��
	static String itemName = "";//��Ʒ����
	static String itemID = "";//��ƷID
	static String itemPrice = "";//��Ʒ�۸�
	static String itemRepayPencent = "";//����
	static String itemRepay = "";//Ӷ��
	static String itemURL = "";//��������
	static String itemDiscountPrice = "";//�Żݾ����
	static String itemDiscountURL = "";//�Ż݄�����
	static String itemRealPrice = "";//��Ʒԭ��
	static String itemTaoKey = "";//��Ʒ�Կ���
	
	//*************SETTING*************//
	static boolean enableSmartReformat = false;//�Զ���ʽ��
	static boolean enableAutoRemoveSame = false;//�Զ�ȥ��
	static int leastRepayPoint = 0;//���ٷ�Ӷ��
	static int leastItemPrice = 0;//Ŀ����Ʒ��ͼ۸�
	static int highestItemPrice = 0;//Ŀ����Ʒ��߼۸�
	public static String tempURL = new HostOS().getUserHome()+"//tmp//";//�����ļ�Ŀ¼λ��
	static int autosaveTime = 1000;//�Զ�����ʱ��(Ĭ��1000ms)
	static String autoPushCommand = "";//�Զ����Ͳ���
	static int pushDelay = 1800;//�Զ�����ʱ��(Ĭ��1800s)
	
	//*************GUI*****************//
	static JFrame frame = new JFrame();
	static JPanel panel = new JPanel();
	public static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public EShare() {
		frame.getContentPane().add(panel);
		frame.setTitle("�ƹ�����V1 [DEBUG] ��������ѭGPL_V3��Դ����ֹ����������");
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
