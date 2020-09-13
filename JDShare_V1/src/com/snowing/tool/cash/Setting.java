package com.snowing.tool.cash;

import javax.swing.JOptionPane;

import cn.snowing.io.Filer;
import cn.snowing.system.HostOS;

public class Setting {
	final public static String Version = "V1.0.0_200904";
	static Filer file = new Filer();
	
	public static boolean loadSetting() {
		checkSettingStatue();
		String[] data = file.getAllLineString(new HostOS().getUserHome()+"\\conf\\setting.conf");
		if(null!=data&&data.length>0) {    //判断设置文件是否空文件
			for(int i=0;i<data.length;i++) {
				if(!"".equals(data[i])&&null!=data[i]) {    //排除空行
					if(data[i].charAt(0)!='#') {    //排除注释
						if(data[i].split("=")[0].equals("version")&&JDShare.CoreVersion<Integer.parseInt(data[i].split("=")[1])) {    //排除版本问题
							//版本不匹配,采用无设置启动
							Ui.lblNewLabel_6.setText("配置载入失败...");
							JOptionPane.showMessageDialog(Ui.loadFrame,"配置版本不匹配，将采用无设置启动");
							return true;
						} else {
							//一切正常,开始录入设置
							Ui.lblNewLabel_6.setText("加载配置中...");
							String item = data[i].split("=")[0];
							String set = data[i].split("=")[1];
							JDShare.CSVUrl = "".equals(JDShare.CSVUrl) ? ("CSVUrl".equals(item) ? set : "") : JDShare.CSVUrl;
							JDShare.enableSmartReformat = false == JDShare.enableSmartReformat ? ("enableSmartReformat".equals(item) ? Boolean.parseBoolean(set) : false) : JDShare.enableSmartReformat;
							JDShare.enableAutoRemoveSame = false == JDShare.enableAutoRemoveSame ? ("enableAutoRemoveSame".equals(item) ? Boolean.parseBoolean(set) : false) : JDShare.enableAutoRemoveSame;
							JDShare.leastRepayPoint = 0 == JDShare.leastRepayPoint ? ("leastRepayPoint".equals(item) ? Integer.parseInt(set) : 0) : JDShare.leastRepayPoint;
							JDShare.leastItemPrice = 0 == JDShare.leastItemPrice ? ("leastItemPrice".equals(item) ? Integer.parseInt(set) : 0) : JDShare.leastItemPrice;
							JDShare.highestItemPrice = 0 == JDShare.highestItemPrice ? ("highestItemPrice".equals(item) ? Integer.parseInt(set) : 0) : JDShare.highestItemPrice;
							JDShare.tempURL = "".equals(JDShare.tempURL) ? ("tempURL".equals(item) ? set : "") : JDShare.tempURL;
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
		file.write(dataURL, "version"+JDShare.CoreVersion, true);
		file.write(dataURL, "CSVUrl"+JDShare.CSVUrl, true);
		file.write(dataURL, "enableSmartReformat"+JDShare.enableSmartReformat, true);
		file.write(dataURL, "enableAutoRemoveSame"+JDShare.enableAutoRemoveSame, true);
		file.write(dataURL, "leastRepayPoint"+JDShare.leastRepayPoint, true);
		file.write(dataURL, "leastItemPrice"+JDShare.leastItemPrice, true);
		file.write(dataURL, "highestItemPrice"+JDShare.highestItemPrice, true);
		return false;
	}
	
	private static void checkSettingStatue() {
		String dataURL = new HostOS().getUserHome()+"\\conf\\setting.conf";
		if(!file.isExists(new HostOS().getUserHome()+"\\conf\\")||!file.isExists(new HostOS().getUserHome()+"\\conf\\default_setting.conf")||file.isEmpty(new HostOS().getUserHome()+"\\conf\\default_setting.conf")) {
			Object[] options = {"确定","取消"};
			int Choose = JOptionPane.showOptionDialog(JDShare.frame,"程序内容丢失!!无法支持程序运行，请重新安装!!","错误", JOptionPane.ERROR_MESSAGE,
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
