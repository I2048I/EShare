package com.snowing.tool.cash;

public class Push {
	final public static String Version = "V1.0.0_201210";
	public static void updateInfo() {
		DataBase.setItemState(EShare.itemID, false);
		if(DataBase.getEnableItemsNum()>0) {
			EShare.copyableItems = DataBase.getEnableItemsNum();
			String[] itemInfo;
			if(EShare.enableRandomAcquireItem) {
				itemInfo = DataBase.getRandomEnableItem();
			} else {
				itemInfo = DataBase.getEnableItem();
			}
			EShare.updateItem(itemInfo);
		} else {
			EShare.copyableItems = 0;
			String[] emptyItemInfo = new String[7];
			EShare.updateItem(emptyItemInfo);
		}
	}
}
