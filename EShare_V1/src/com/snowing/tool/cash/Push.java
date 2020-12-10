package com.snowing.tool.cash;

public class Push {
	final public static String Version = "V1.0.0_201002";
	public static void updateInfo() {
		DataBase.setItemState(EShare.itemID, false);
		if(DataBase.getEnableItemsNum()>0) {
			EShare.copyableItems = DataBase.getEnableItemsNum();
			String[] itemInfo = DataBase.getEnableItem();
			EShare.updateItem(itemInfo);
		} else {
			EShare.copyableItems = 0;
			String[] emptyItemInfo = new String[6];
			EShare.updateItem(emptyItemInfo);
		}
	}
}
