package com.snowing.tool.cash;

import javax.swing.JOptionPane;

public class AutoPush {
	public static boolean pc() {
		if(null!=EShare.autoPushCommand&&!"".equals(EShare.autoPushCommand)&&!"null".equals(EShare.autoPushCommand)) {
			Ui.autoPushPC();
			return true;
		} else {
			EShare.autoPushCommand = JOptionPane.showInputDialog(EShare.frame, "δ�趨���Ͳ�����������...", "��ʾ", JOptionPane.INFORMATION_MESSAGE)+" ";
			System.out.println(EShare.autoPushCommand);
			return false;
		}
	}
	
	public boolean phone() {
		return false;
	}
}
