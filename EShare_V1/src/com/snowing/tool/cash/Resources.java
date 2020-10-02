package com.snowing.tool.cash;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import cn.snowing.system.HostOS;

public class Resources {
	public static String iconLocal = new HostOS().getUserHome()+"//img//";
	final public static String Version = "V1.0.0_201002";
	
	private static String Setting = iconLocal+"Setting";
	private static String Help = iconLocal+"Help";
	private static String Loading_1 = iconLocal+"Loading_1";
	private static String WeChat_128x128 = iconLocal+"WeChat_128x128";
	private static String AliPay_128x128 = iconLocal+"AliPay_128x128";
	private static String SwitchON = iconLocal+"Switch_On";
	private static String SwitchOFF = iconLocal+"Switch_Off";
	private static String Icon = iconLocal+"Icon";
	private static String Delete = iconLocal+"Delete";
	private static String AutoPush = iconLocal+"AutoPush";
	
	public ImageIcon getIcon(String Component, int subject) {
		if(!"".equals(Component)&&null!=Component&&subject!=0) {
			if(Component.equals("Setting")) {
				ImageIcon icon = new ImageIcon(Setting+"_"+subject+".png");
				return icon;
			} else if(Component.equals("Help")) {
				ImageIcon icon = new ImageIcon(Help+"_"+subject+".png");
				return icon;
			} else if(Component.equals("Delete")) {
				ImageIcon icon = new ImageIcon(Delete+"_"+subject+".png");
				return icon;
			} else if(Component.equals("AutoPush")) {
				ImageIcon icon = new ImageIcon(AutoPush+"_"+subject+".png");
				return icon;
			} else if(Component.equals("Loading")) {
				ImageIcon icon = new ImageIcon(Loading_1+"_"+subject+".png");
				return icon;
			} else if(Component.equals("WeChat_128x128")) {
				ImageIcon icon = new ImageIcon(WeChat_128x128+".png");
				return icon;
			} else if(Component.equals("AliPay_128x128")) {
				ImageIcon icon = new ImageIcon(AliPay_128x128+".png");
				return icon;
			} else if(Component.equals("SwitchON")) {
				ImageIcon icon = new ImageIcon(SwitchON+"_"+subject+".png");
				return icon;
			} else if(Component.equals("SwitchOFF")) {
				ImageIcon icon = new ImageIcon(SwitchOFF+"_"+subject+".png");
				return icon;
			} else if(Component.equals("Icon")) {
				ImageIcon icon = null;
				switch(subject) {
				case 32:
					icon = new ImageIcon(Icon+"_32x32.png");
				case 64:
					icon = new ImageIcon(Icon+"_64x64.png");
				case 128:
					icon = new ImageIcon(Icon+"_128x128.png");
				}
				return icon;
			}else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public Image getImage(String Component, int subject) {
		 if(Component.equals("Icon")) {
				Image icon = null;
				switch(subject) {
				case 32:
					icon = Toolkit.getDefaultToolkit().getImage(Icon+"_32x32.png");
				case 64:
					icon = Toolkit.getDefaultToolkit().getImage(Icon+"_64x64.png");
				case 128:
					icon = Toolkit.getDefaultToolkit().getImage(Icon+"_128x128.png");
				}
				return icon;
			}
		return null;
	}
}
