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
	static String CSVUrl = "";//Ŀ��CSV�ļ�Ŀ¼
	static int allItems = 0;//��Ʒ����
	static int copyableItems = 0;//����������Ʒ��
	static String itemName = "";//��Ʒ����
	static String itemID = "";//��ƷID
	static String itemPrice = "";//��Ʒ�۸�
	static String itemRepayPencent = "";//����
	static String itemRepay = "";//Ӷ��
	static String itemURL = "";//��������
	static String itemDiscountURL = "";//�Ż݄�����
	static String itemRealPrice = "";//��Ʒԭ��
	
	//*************SETTING*************//
	static boolean enableSmartReformat = false;//�Զ���ʽ��
	static boolean enableAutoRemoveSame = false;//�Զ�ȥ��
	static int leastRepayPoint = 0;//���ٷ�Ӷ��
	static int leastItemPrice = 0;//Ŀ����Ʒ��ͼ۸�
	static int highestItemPrice = 0;//Ŀ����Ʒ��߼۸�
	public static String tempURL = new HostOS().getUserHome()+"//tmp//";//�����ļ�Ŀ¼λ��
	static int autosaveTime = 1000;//�Զ�����ʱ��(Ĭ��1000ms)
	
	//*************GUI*****************//
	static JFrame frame = new JFrame();
	static JPanel panel = new JPanel();
	public static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	//**********THREAD DEFINE*********//
	AutoSave autosave = new AutoSave();
	
	public JDShare() {
		frame.getContentPane().add(panel);
		frame.setTitle("�ƹ�����V1 ����->��������");
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
