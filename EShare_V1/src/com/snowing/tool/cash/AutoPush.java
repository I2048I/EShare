package com.snowing.tool.cash;

import javax.swing.JOptionPane;

public class AutoPush {
	final public static String Version = "V1.0.0_201002";
	public static boolean pc() {
		if(null!=EShare.autoPushCommand&&!"".equals(EShare.autoPushCommand)&&!"null ".equals(EShare.autoPushCommand)&&!"null".equals(EShare.autoPushCommand)) {
			Ui.autoPushPC();
			return true;
		} else {
			EShare.autoPushCommand = JOptionPane.showInputDialog(EShare.frame, "未设定推送参数，请输入...", "提示", JOptionPane.INFORMATION_MESSAGE)+" ";
			System.out.println(EShare.autoPushCommand);
			return false;
		}
	}
	
	public static boolean phone() {
		return false;
	}
}
