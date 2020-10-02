package com.snowing.tool.cash;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import cn.snowing.io.Filer;
import cn.snowing.system.HostOS;

public class Setting {
	final public static String Version = "V1.0.0_201001";
	static Filer file = new Filer();
	
	public static boolean loadSetting() {
		checkSettingStatue();
		String[] data = file.getAllLineString(new HostOS().getUserHome()+"\\conf\\setting.conf");
		if(null!=data&&data.length>0) {    //判断设置文件是否空文件
			for(int i=0;i<data.length;i++) {
				if(!"".equals(data[i])&&null!=data[i]) {    //排除空行
					if(data[i].charAt(0)!='#') {    //排除注释
						if(data[i].split("=")[0].equals("version")&&EShare.CoreVersion<Integer.parseInt(data[i].split("=")[1])) {    //排除版本问题
							//版本不匹配,采用无设置启动
							Ui.lblNewLabel_6.setText("配置载入失败...");
							JOptionPane.showMessageDialog(Ui.loadFrame,"配置版本不匹配，将采用无配置启动");
							return true;
						} else {
							//一切正常,开始录入设置
							Ui.lblNewLabel_6.setText("加载配置中...");
							String item = data[i].split("=")[0];
							String set = data[i].split("=")[1];
							EShare.CSVUrl = "".equals(EShare.CSVUrl) ? ("CSVUrl".equals(item) ? set : "") : EShare.CSVUrl;
							EShare.enableSmartReformat = false == EShare.enableSmartReformat ? ("enableSmartReformat".equals(item) ? Boolean.parseBoolean(set) : false) : EShare.enableSmartReformat;
							EShare.enableAutoRemoveSame = false == EShare.enableAutoRemoveSame ? ("enableAutoRemoveSame".equals(item) ? Boolean.parseBoolean(set) : false) : EShare.enableAutoRemoveSame;
							EShare.leastRepayPoint = 0 == EShare.leastRepayPoint ? ("leastRepayPoint".equals(item) ? Integer.parseInt(set) : 0) : EShare.leastRepayPoint;
							EShare.leastItemPrice = 0 == EShare.leastItemPrice ? ("leastItemPrice".equals(item) ? Integer.parseInt(set) : 0) : EShare.leastItemPrice;
							EShare.highestItemPrice = 0 == EShare.highestItemPrice ? ("highestItemPrice".equals(item) ? Integer.parseInt(set) : 0) : EShare.highestItemPrice;
							EShare.tempURL = "".equals(EShare.tempURL) ? ("tempURL".equals(item) ? set : "") : EShare.tempURL;
							EShare.autoPushCommand = "".equals(EShare.autoPushCommand) ? ("autoPushCommand".equals(item) ? set : "") : EShare.autoPushCommand;
							EShare.pushDelay = 0 == EShare.pushDelay ? ("pushDelay".equals(item) ? Integer.parseInt(set) : 0) : EShare.pushDelay;
						}
					} else {
						continue;
					}
				} else {
					continue;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
	public static boolean saveSetting() {
		String dataURL = new HostOS().getUserHome()+"\\conf\\setting.conf";
		if(!file.isExists(dataURL)) {
			file.createNewFile(dataURL);
		}
		file.write(dataURL, "#!SETTING", false);
		file.write(dataURL, "version="+EShare.CoreVersion, true);
		file.write(dataURL, "CSVUrl="+EShare.CSVUrl, true);
		file.write(dataURL, "enableSmartReformat="+EShare.enableSmartReformat, true);
		file.write(dataURL, "enableAutoRemoveSame="+EShare.enableAutoRemoveSame, true);
		file.write(dataURL, "leastRepayPoint="+EShare.leastRepayPoint, true);
		file.write(dataURL, "leastItemPrice="+EShare.leastItemPrice, true);
		file.write(dataURL, "highestItemPrice="+EShare.highestItemPrice, true);
		file.write(dataURL, "autoPushCommand="+EShare.autoPushCommand, true);
		file.write(dataURL, "pushDelay="+EShare.autoPushCommand, true);
		return true;
	}
	
	private static void checkSettingStatue() {
		String dataURL = new HostOS().getUserHome()+"\\conf\\setting.conf";
		if(!file.isExists(new HostOS().getUserHome()+"\\conf\\")||!file.isExists(new HostOS().getUserHome()+"\\conf\\default_setting.conf")||file.isEmpty(new HostOS().getUserHome()+"\\conf\\default_setting.conf")) {
			Object[] options = {"确定","取消"};
			int Choose = JOptionPane.showOptionDialog(EShare.frame,"程序内容丢失!!无法支持程序运行，请重新安装!!","错误", JOptionPane.ERROR_MESSAGE,
					JOptionPane.ERROR_MESSAGE, null,options,options[1]);
			if(Choose==0) {
				System.exit(0);
			} else {
				System.exit(0);
			}
		} else if(file.isExists(new HostOS().getUserHome()+"\\conf\\")&&!file.isExists(dataURL)) {
			file.copy(new HostOS().getUserHome()+"\\conf\\default_setting.conf", dataURL, false);
		} else if(file.isExists(new HostOS().getUserHome()+"\\conf\\")&&file.isExists(dataURL)&&file.isEmpty(dataURL)) {
			file.delFile(dataURL);
			file.copy(new HostOS().getUserHome()+"\\conf\\default_setting.conf", dataURL, false);
		}
		
	}
}

class AutoSave extends Thread {
	boolean missFailed = false;
	public void run() {
		while(true) {
			boolean isfine = Setting.saveSetting();
			if(!isfine&&!missFailed) {
				Object[] options = {"查看原因","忽略"};
				int Choose = JOptionPane.showOptionDialog(EShare.frame,"保存设置失败!!!","错误", JOptionPane.ERROR_MESSAGE,
						JOptionPane.ERROR_MESSAGE, null,options,options[1]);
				if(Choose==0) {
					Desktop dp =Desktop.getDesktop();
					try {
						URI url = new URI("https://github.com/I2048I/JDShare/blob/master/FailedSaveSetting.md");
						dp.browse(url);
					} catch (IOException | URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					missFailed = true;
				}
			}
			try {
				Thread.sleep(EShare.autosaveTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
