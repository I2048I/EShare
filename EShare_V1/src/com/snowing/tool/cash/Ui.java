package com.snowing.tool.cash;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import cn.snowing.io.Filer;
import cn.snowing.system.Toolkits;
import net.miginfocom.swing.MigLayout;

public class Ui extends EShare{

	final public static String Version = "V1.0.1_201211";

	static Filer file = new Filer();
	static Toolkits tk = new Toolkits();
	static private Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static Resources res = new Resources();

	private static JTextField textField_CVSFile;
	private static JButton button_smartReformat = new JButton("ON");
	private static JButton button_autoRemoveSame = new JButton("ON");
	private static JButton button_randomAcquireItem = new JButton("ON");

	protected static JFrame loadFrame = new JFrame();
	protected static JLabel lblNewLabel_6 = new JLabel("\u8F7D\u5165\u6570\u636E\u4E2D...");
	private static JLabel label_enableItems = new JLabel("\u53EF\u7528\u5546\u54C1:0");
	private static JLabel label_itemName = new JLabel("\u540D\u79F0:");
	private static JLabel label_platform = new JLabel("\u5E73\u53F0:");
	private static JLabel label_itemID = new JLabel("ID:");
	private static JLabel label_cashInfo = new JLabel("折后价:￥0    \u8FD4\u4F63\u70B9\u6570:0%    \u8FD4\u4F63\u91D1:￥0");

	static boolean loaded = false;
	static boolean SKUloaded = false;//是否加载完成SKU读取操作
	static boolean pushed = false;//是否Push
	static boolean isgetData = false;//是否成功获取商品信息
	//**********THREAD DEFINE*********//
	static AutoSave autosave = new AutoSave();
	private static JTextField textField_1;
	//private static JTextField textField;

	public static void load() {
		frame.setVisible(false);

		JFrame loadFrame = new JFrame();
		loadFrame.setUndecorated(true);
		loadFrame.setBounds(ScreenSize.width / 2 - 300 / 2, ScreenSize.height / 2 - 130 / 2, 300, 130);
		loadFrame.setIconImage(res.getImage("Icon", 128));
		loadFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadFrame.setVisible(true);
		JPanel loadPanel = new JPanel();
		loadPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		loadFrame.getContentPane().add(loadPanel);
		loadPanel.setBackground(Color.WHITE);
		loadPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		loadPanel.add(panel, BorderLayout.NORTH);

		JLabel lblNewLabel_3 = new JLabel("\u63A8\u5E7F\u8054\u76DF");
		lblNewLabel_3.setFont(new Font("黑体", Font.PLAIN, 24));
		panel.add(lblNewLabel_3);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		loadPanel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_5 = new JLabel("");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_5);

		lblNewLabel_6.setFont(new Font("黑体", Font.PLAIN, 15));
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_6, BorderLayout.SOUTH);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBackground(Color.WHITE);
		loadPanel.add(panel_2, BorderLayout.SOUTH);

		JLabel lblNewLabel_4 = new JLabel("Power by SnowingSDK");
		lblNewLabel_4.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_2.add(lblNewLabel_4);

		new Timer().schedule(new TimerTask() {
			public void run() {
				frame.setIconImage(res.getImage("Icon", 128));
				allItems = DataBase.getAllItemsNums();
				if(DataBase.getEnableItemsNum()>0) {
					copyableItems = DataBase.getEnableItemsNum();
					String[] itemInfo;
					if(EShare.enableRandomAcquireItem) {
						itemInfo = DataBase.getRandomEnableItem();
					} else {
						itemInfo = DataBase.getEnableItem();
					}
					updateItem(itemInfo);
				}
				loaded = Setting.loadSetting();
				autosave.start();
			}
		}, 10);

		while(!loaded) {
			for(int i=1;i<17;i++) {
				lblNewLabel_5.setIcon(res.getIcon("Loading", i));
				lblNewLabel_5.updateUI();
				try {
					Thread.sleep(62);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadFrame.setVisible(false);
		frame.setVisible(true);
		home();
	}

	public static void home() {
		panel.removeAll();
		panel.setLayout(new BorderLayout(5,5));
		JPanel homePanel = new JPanel();
		panel.add(homePanel);
		homePanel.setLayout(new BorderLayout(5, 5));

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		homePanel.add(panel, BorderLayout.NORTH);

		JLabel label_dataFile = new JLabel("\u6E90\u6570\u636E\u6587\u4EF6");
		label_dataFile.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(label_dataFile);

		textField_CVSFile = new JTextField();
		textField_CVSFile.setEditable(false);
		textField_CVSFile.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(textField_CVSFile);
		textField_CVSFile.setColumns(18);

		JButton btncsv_1 = new JButton("\u9009\u62E9...");
		btncsv_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame fileChooseFrame = new JFrame();
				int result = 0;
				String local = null;
				JFileChooser fileChooser = new JFileChooser(".");
				FileNameExtensionFilter filter= new FileNameExtensionFilter("源数据文件(*.csv/*.xls/*.xlsx)", "csv", "xls", "xlsx");
				fileChooser.setFileFilter(filter);
				fileChooser.setDialogTitle("请选择...");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				result = fileChooser.showOpenDialog(fileChooseFrame);
				if (JFileChooser.APPROVE_OPTION == result) {
					local=fileChooser.getSelectedFile().getPath();
					dataUrl = local;
				}
				if(null!=local&&!"".equals(local)) {
					textField_CVSFile.setText(dataUrl);
				}
			}

		});
		btncsv_1.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(btncsv_1);

		JButton btncsv = new JButton("\u83B7\u53D6\u6700\u65B0\u6587\u4EF6");
		btncsv.setFont(new Font("黑体", Font.PLAIN, 15));
		btncsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] options = {"京东联盟","淘宝联盟","取消"};
				int Choose = JOptionPane.showOptionDialog(EShare.frame,"请选择要推广的平台","选择推广平台", JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.INFORMATION_MESSAGE, null,options,options[1]);
				Desktop dp = Desktop.getDesktop();
				switch(Choose) {
				case 0:
					try {
						URI url = new URI("https://union.jd.com/proManager/index?pageNo=1");
						dp.browse(url);
					} catch (IOException | URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 1:
					try {
						URI url = new URI("https://pub.alimama.com/promo/search/index.htm");
						dp.browse(url);
					} catch (IOException | URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 2:
					break;
				}
			}
		});
		panel.add(btncsv);

		JButton btnNewButton_5 = new JButton("\u5EFA\u7ACB\u6570\u636E\u5E93");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!file.isEmpty(dataUrl)) {
					Object[] options = {"京东联盟","淘宝联盟","取消"};
					int Choose = JOptionPane.showOptionDialog(EShare.frame,"源数据来源平台","选择平台", JOptionPane.INFORMATION_MESSAGE,
							JOptionPane.INFORMATION_MESSAGE, null,options,options[1]);
					switch(Choose) {
					case 0:
						if(file.getFileKinds(dataUrl).equals("csv")) {
							DataBase.buildCSVData(dataUrl, "JD");
						} else if(file.getFileKinds(dataUrl).equals("xls")||file.getFileKinds(dataUrl).equals("xlsx")) {
							DataBase.buildXLSData(dataUrl, "JD");
						}
						if(DataBase.getEnableItemsNum()>0) {
							copyableItems = DataBase.getEnableItemsNum();
							String[] itemInfo;
							if(EShare.enableRandomAcquireItem) {
								itemInfo = DataBase.getRandomEnableItem();
							} else {
								itemInfo = DataBase.getEnableItem();
							}
							updateItem(itemInfo);
						} else {
							copyableItems = 0;
							String[] emptyItemInfo = new String[7];
							updateItem(emptyItemInfo);
						}
						labelReflash();
						break;
					case 1:
						if(file.getFileKinds(dataUrl).equals("csv")) {
							DataBase.buildCSVData(dataUrl, "TB");
						} else if(file.getFileKinds(dataUrl).equals("xls")||file.getFileKinds(dataUrl).equals("xlsx")) {
							DataBase.buildXLSData(dataUrl, "TB");
						}
						if(DataBase.getEnableItemsNum()>0) {
							copyableItems = DataBase.getEnableItemsNum();
							String[] itemInfo;
							if(EShare.enableRandomAcquireItem) {
								itemInfo = DataBase.getRandomEnableItem();
							} else {
								itemInfo = DataBase.getEnableItem();
							}
							updateItem(itemInfo);
						} else {
							copyableItems = 0;
							String[] emptyItemInfo = new String[7];
							updateItem(emptyItemInfo);
						}
						labelReflash();
						break;
					case 2:
						break;
					}

				} else {
					JOptionPane.showMessageDialog(frame, "目标源数据文件不存在", "错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton_5.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(btnNewButton_5);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		homePanel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.WEST);
		panel_4.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_1 = new JLabel("\u7248\u672C:"+Version);
		lblNewLabel_1.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_4.add(lblNewLabel_1);

		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5, BorderLayout.EAST);

		JButton button_help = new JButton("?");
		button_help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				help();
			}
		});
		button_help.setToolTipText("\u5E2E\u52A9");
		button_help.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_5.add(button_help);

		JButton button_setting = new JButton("*");
		button_setting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setting();
			}
		});
		button_setting.setToolTipText("\u8BBE\u7F6E");
		button_setting.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_5.add(button_setting);

		JLabel lblgplv = new JLabel("\u672C\u8F6F\u4EF6\u57FA\u4E8EGPL_V3\u5F00\u6E90\uFF0C\u7981\u6B62\u5012\u5356!!!");
		lblgplv.setForeground(Color.RED);
		lblgplv.setFont(new Font("黑体", Font.PLAIN, 12));
		lblgplv.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblgplv, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		homePanel.add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(new BorderLayout(0, 2));

		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_6.getLayout();
		flowLayout_2.setVgap(2);
		panel_2.add(panel_6, BorderLayout.NORTH);

		label_enableItems.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_6.add(label_enableItems);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setHgap(10);
		flowLayout_3.setVgap(2);
		panel_2.add(panel_3, BorderLayout.SOUTH);

		JButton button_removeItem = new JButton("x");
		button_removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(itemID.equals("")||itemID.equals("null")||itemID==null) {
					JOptionPane.showMessageDialog(frame, "当前无商品", "错误", JOptionPane.ERROR_MESSAGE);
				} else {
					Object[] options = {"确定","取消"};
					int Choose = JOptionPane.showOptionDialog(frame,"将该商品从可用列表中剔除。(非永久删除)","ID:"+itemID, JOptionPane.INFORMATION_MESSAGE,
							JOptionPane.INFORMATION_MESSAGE, null,options,options[1]);
					if(Choose==0) {
						DataBase.setItemState(itemID, false);
						if(DataBase.getEnableItemsNum()>0) {
							copyableItems = DataBase.getEnableItemsNum();
							String[] itemInfo;
							if(EShare.enableRandomAcquireItem) {
								itemInfo = DataBase.getRandomEnableItem();
							} else {
								itemInfo = DataBase.getEnableItem();
							}
							updateItem(itemInfo);
						} else {
							copyableItems = 0;
							String[] emptyItemInfo = {"","","","","","",""};
							updateItem(emptyItemInfo);
						}
						labelReflash();
					}
				}
			}
		});
		button_removeItem.setToolTipText("\u5220\u9664\u8BE5\u5546\u54C1");
		button_removeItem.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3.add(button_removeItem);

		JButton button_autoPush = new JButton(">");
		button_autoPush.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(itemID.equals("")||itemID.equals("null")||itemID==null) {
					JOptionPane.showMessageDialog(frame, "当前无商品", "错误", JOptionPane.ERROR_MESSAGE);
				} else {
					Object[] options = {"PC","安卓"};
					int Choose = JOptionPane.showOptionDialog(frame,"选择推送方式","提示", JOptionPane.INFORMATION_MESSAGE,
							JOptionPane.INFORMATION_MESSAGE, null,options,options[1]);
					if(Choose==0) {
						AutoPush.pc();
					} else {
						AutoPush.phone();
					}
				}
			}
		});
		button_autoPush.setToolTipText("\u81EA\u52A8\u63A8\u9001");
		button_autoPush.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3.add(button_autoPush);

		JButton button_getText = new JButton("\u83B7\u53D6\u6587\u672C");
		button_getText.setToolTipText("\u83B7\u53D6\u53EF\u4EE5\u76F4\u63A5\u53D1\u9001\u7684\u6587\u672C");
		button_getText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyLink();
			}
		});
		button_getText.setMnemonic('s');
		button_getText.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_2.add(button_getText, BorderLayout.CENTER);

		JPanel panel_7 = new JPanel();
		homePanel.add(panel_7, BorderLayout.CENTER);
		panel_7.setLayout(new BorderLayout(2, 2));

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(new LineBorder(new Color(55, 55, 55), 1), "当前商品详情", TitledBorder.LEADING, TitledBorder.TOP, new Font("黑体", Font.PLAIN, 15), new Color(0, 0, 0)));
		panel_7.add(panel_8, BorderLayout.NORTH);
		panel_8.setLayout(new GridLayout(0, 1, 2, 0));

		label_platform.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_8.add(label_platform);

		label_itemName.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_8.add(label_itemName);

		label_itemID.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_8.add(label_itemID);

		label_cashInfo.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_8.add(label_cashInfo);

		JPanel panel_3_1 = new JPanel();
		panel_3_1.setBorder(new TitledBorder(new LineBorder(new Color(55, 55, 55), 1), "文本数据处理", TitledBorder.LEADING, TitledBorder.TOP, new Font("黑体", Font.PLAIN, 15), new Color(0, 0, 0)));
		panel_7.add(panel_3_1, BorderLayout.CENTER);
		panel_3_1.setLayout(new MigLayout("", "[][]", "[][][]"));

		JLabel label_smartReformat = new JLabel("\u667A\u80FD\u683C\u5F0F\u5316");
		label_smartReformat.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3_1.add(label_smartReformat, "cell 0 0");

		button_smartReformat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(enableSmartReformat) {
					enableSmartReformat = false;
				} else {
					enableSmartReformat = true;
				}
				button_smartReformat = switchButton(button_smartReformat, enableSmartReformat);
			}
		});
		button_smartReformat.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3_1.add(button_smartReformat, "cell 1 0");

		JLabel label_autoRemoveSame = new JLabel("\u81EA\u52A8\u53BB\u91CD");
		label_autoRemoveSame.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3_1.add(label_autoRemoveSame, "cell 0 1");

		button_autoRemoveSame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(enableAutoRemoveSame) {
					enableAutoRemoveSame = false;
				} else {
					enableAutoRemoveSame = true;
				}
				button_autoRemoveSame = switchButton(button_autoRemoveSame, enableAutoRemoveSame);
			}
		});
		button_autoRemoveSame.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3_1.add(button_autoRemoveSame, "cell 1 1");
		
		JLabel label_randomAcquireItem = new JLabel("\u968F\u673A\u83B7\u53D6\u5546\u54C1");
		label_randomAcquireItem.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3_1.add(label_randomAcquireItem, "cell 0 2");
		button_randomAcquireItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(enableRandomAcquireItem) {
					enableRandomAcquireItem = false;
				} else {
					enableRandomAcquireItem = true;
				}
				button_randomAcquireItem = switchButton(button_randomAcquireItem, enableRandomAcquireItem);
			}
		});
		
		button_randomAcquireItem.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3_1.add(button_randomAcquireItem, "cell 1 2");

		//************ADD ITEMS DATA*************//
		labelReflash();
		//***********SETTING BUTTON REFLASH*************//
		if(loaded) {
			button_smartReformat = switchButton(button_smartReformat, enableSmartReformat);
			button_autoRemoveSame = switchButton(button_autoRemoveSame, enableAutoRemoveSame);
			button_randomAcquireItem = switchButton(button_randomAcquireItem, enableRandomAcquireItem);
			button_autoPush = buttonImgAdd(button_autoPush, "AutoPush");
			button_removeItem = buttonImgAdd(button_removeItem, "Delete");
			button_setting = buttonImgAdd(button_setting, "Setting");
			button_help = buttonImgAdd(button_help, "Help");
		}

		panel.updateUI();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void setting() {
		panel.removeAll();
		panel.setLayout(new BorderLayout(5,5));
		JPanel settingPanel = new JPanel();
		panel.add(settingPanel);
		settingPanel.setLayout(new BorderLayout(5, 5));
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout(5, 5));
		JButton button_back = new JButton("<--");
		northPanel.add(button_back, BorderLayout.WEST);
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout(5, 5));
		northPanel.add(rightPanel, BorderLayout.CENTER);
		settingPanel.add(northPanel, BorderLayout.NORTH);
		
		panel.updateUI();
	}

	public static void copyLink() {
		JDialog dialog = new JDialog(frame, "ID:"+itemID, true);
		dialog.setBounds(ScreenSize.width / 2 - 340 / 2, ScreenSize.height / 2 - 160 / 2, 340, 160);
		CardLayout card = new CardLayout();
		dialog.getContentPane().setLayout(card);
		JPanel dialogPanel = new JPanel();
		JPanel loadingPanel = new JPanel();
		loadingPanel.setBackground(Color.WHITE);
		loadingPanel.setLayout(new BorderLayout(5,5));
		dialog.getContentPane().add( "b1",loadingPanel);
		dialog.getContentPane().add( "b2",dialogPanel);
		//*************LOADING***************//
		JLabel label_loading = new JLabel("");
		label_loading.setHorizontalAlignment(SwingConstants.CENTER);
		loadingPanel.add(label_loading, BorderLayout.CENTER);

		JLabel label_loadingText = new JLabel("\u83B7\u53D6\u6570\u636E\u4E2D...");
		label_loadingText.setFont(new Font("黑体", Font.PLAIN, 15));
		label_loadingText.setHorizontalAlignment(SwingConstants.CENTER);
		loadingPanel.add(label_loadingText, BorderLayout.SOUTH);

		new Timer().schedule(new TimerTask() {
			public void run() {
				card.show(dialog.getContentPane(), "b1");
				while(!SKUloaded) {
					for(int i=1;i<17;i++) {
						label_loading.setIcon(res.getIcon("Loading", i));
						label_loading.updateUI();
						try {
							Thread.sleep(62);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}	
				}
			}
		}, 10);

		new Timer().schedule(new TimerTask() {
			public void run() {
				if(itemID.charAt(0)=='J') {
					itemRealPrice = SKU.getPrice(itemURL);
					if(null!=itemRealPrice) {
						label_loadingText.setText("正在获取原价...");
						if(!"".equals(itemRealPrice)&&null!=itemRealPrice) {
							if("ERROR404".equals(itemRealPrice)) {
								JOptionPane.showMessageDialog(EShare.frame,"获取失败，可能是链接已过期，请清除数据库重新获取。");
								label_loadingText.setText("获取商品原价失败...");
								itemRealPrice = "0";
								pushFailueTime+=1;
								SKUloaded = false;
								dialog.setVisible(false);
							} else {
								//SKUloaded = true;
								label_loadingText.setText("正在将数据复制到剪切板...");
								tk.clipboard(itemName+"\r\n"+"原价￥"+itemRealPrice+"，"+"卷后￥"+itemPrice+"\r\n"+"链接:"+itemURL+"\r\n"+"优惠劵:"+itemDiscountURL);
								label_loadingText.setText("载入新商品数据中...");
								Push.updateInfo();
								labelReflash();
								label_loadingText.setText("数据已成功获取...");
								JOptionPane.showMessageDialog(frame,"获取完成!!");
								pushTime+=1;
								SKUloaded = false;
								dialog.setVisible(false);
							}
						} else {
							label_loadingText.setText("获取商品原价失败...");
							SKUloaded = true;
							card.show(dialog.getContentPane(), "b2");
						}
					} else {
						label_loadingText.setText("获取商品信息失败...");
						JOptionPane.showMessageDialog(frame,"获取失败，请在接下来显示的网页中查看原价格，并输入到输入框中。");
						SKUloaded = true;
						card.show(dialog.getContentPane(), "b2");
						Desktop dp = Desktop.getDesktop();
						try {
							URI url = new URI(itemURL);
							dp.browse(url);
						} catch (IOException | URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					label_loadingText.setText("正在将数据复制到剪切板...");
					tk.clipboard(itemName+"\r\n"+"原价￥"+itemRealPrice+"，"+"卷后￥"+itemPrice+"\r\n"+"淘口令:"+itemTaoKey);
					label_loadingText.setText("载入新商品数据中...");
					Push.updateInfo();
					labelReflash();
					label_loadingText.setText("数据已成功获取...");
					JOptionPane.showMessageDialog(frame,"获取完成!!");
					pushTime+=1;
					SKUloaded = false;
					dialog.setVisible(false);
				}
			}
		}, 10);
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		dialogPanel.setLayout(new BorderLayout(0,0));
		dialogPanel.add(centerPanel, BorderLayout.CENTER);
		dialogPanel.add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new BorderLayout());
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		southPanel.add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 0));

		JLabel label_textMode = new JLabel("\u6587\u672C\u6837\u5F0F:1");
		label_textMode.setFont(new Font("黑体", Font.PLAIN, 15));
		leftPanel.add(label_textMode);
		southPanel.add(rightPanel, BorderLayout.EAST);
		southPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		JPanel insidePanel = new JPanel();
		JPanel emptyPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout(10,10));
		centerPanel.add(emptyPanel, BorderLayout.NORTH);
		centerPanel.add(insidePanel, BorderLayout.CENTER);

		JLabel label_price = new JLabel("原￥");
		JTextField textField_price = new JTextField();
		textField_price.setColumns(6);
		textField_price.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(!"".equals(textField_price.getText())&&null!=textField_price.getText()) {
					int length = textField_price.getText().length();
					String patch = "0123456789.";
					if(length<6) {
						if (patch.indexOf(e.getKeyChar())< 0) {
							e.consume();
						} else if (patch.indexOf(e.getKeyChar())>= 0) {
							//e.consume();
						}
					} else {
						e.consume();
					}
				} else {
					if ("0123456789.".indexOf(e.getKeyChar())>0) {
					} else {
						e.consume();
					}
				}
			}
		});
		JLabel label_nowPrice = new JLabel("现￥");
		JTextField textField_nowPrice = new JTextField(itemPrice);
		textField_nowPrice.setColumns(6);
		textField_nowPrice.setEditable(false);
		JLabel label_discountPrice = new JLabel("优惠劵￥");
		JTextField textField_discountPrice = new JTextField();
		textField_discountPrice.setColumns(6);
		JButton button_copyText = new JButton("复制文本");
		button_copyText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable tText = null;
				if(null!=textField_price.getText()) {
					itemRealPrice = textField_price.getText();
					tText = new StringSelection(itemName+"\r\n"+"原价￥"+itemRealPrice+"，"+"卷后￥"+itemPrice+"\r\n"+"链接:"+itemURL+"\r\n"+"优惠劵:"+itemDiscountURL);  
				} else {
					tText = new StringSelection(itemName+"\r\n"+"卷后￥"+itemPrice+"\r\n"+"链接:"+itemURL+"\r\n"+"优惠劵:"+itemDiscountURL);  
				}
				clip.setContents(tText, null); 
				DataBase.setItemState(itemID, false);
				if(DataBase.getEnableItemsNum()>0) {
					copyableItems = DataBase.getEnableItemsNum();
					String[] itemInfo;
					if(EShare.enableRandomAcquireItem) {
						itemInfo = DataBase.getRandomEnableItem();
					} else {
						itemInfo = DataBase.getEnableItem();
					}
					updateItem(itemInfo);
				} else {
					copyableItems = 0;
					String[] emptyItemInfo = {"","","","","","",""};
					updateItem(emptyItemInfo);
				}
				labelReflash();
				pushTime+=1;
				SKUloaded = false;
				JOptionPane.showMessageDialog(frame,"已将内容复制到剪切板");
				dialog.setVisible(false);
			}
		});
		label_price.setFont(new Font("黑体", Font.PLAIN, 15));
		textField_price.setFont(new Font("黑体", Font.PLAIN, 15));
		label_nowPrice.setFont(new Font("黑体", Font.PLAIN, 15));
		textField_nowPrice.setFont(new Font("黑体", Font.PLAIN, 15));
		label_discountPrice.setFont(new Font("黑体", Font.PLAIN, 15));
		textField_discountPrice.setFont(new Font("黑体", Font.PLAIN, 15));
		insidePanel.add(label_price);
		insidePanel.add(textField_price);
		insidePanel.add(label_nowPrice);
		insidePanel.add(textField_nowPrice);
		insidePanel.add(label_discountPrice);
		insidePanel.add(textField_discountPrice);
		button_copyText.setFont(new Font("黑体", Font.PLAIN, 15));
		rightPanel.add(button_copyText);
		dialog.setVisible(true);
	}

	public static void help() {
		JDialog dialog = new JDialog(frame, "帮助", true);
		dialog.setIconImage(res.getImage("Icon", 32));
		dialog.setBounds(ScreenSize.width / 2 - 420 / 2, ScreenSize.height / 2 - 330 / 2, 420, 330);
		JPanel helpPanel = new JPanel();
		dialog.getContentPane().add(helpPanel);
		helpPanel.setLayout(new BorderLayout(5, 2));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(55, 55, 55)), "捐赠", TitledBorder.LEADING, TitledBorder.TOP, new Font("黑体", Font.PLAIN, 15), new Color(0, 0, 0)));
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		helpPanel.add(panel, BorderLayout.SOUTH);

		JLabel lblNewLabel_8 = new JLabel("\u5F00\u53D1\u4E0D\u6613\uFF0C\u611F\u8C22\u6350\u8D60!");
		lblNewLabel_8.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(lblNewLabel_8);

		JButton tglbtnNewToggleButton = new JButton("\u5FAE\u4FE1");
		tglbtnNewToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog dialog1 = new JDialog(frame, "WeChat Donate", true);
				dialog1.setBounds(ScreenSize.width / 2 - 168 / 2, ScreenSize.height / 2 - 198 / 2, 168, 198);
				JPanel wechatPanel = new JPanel();
				dialog1.getContentPane().add(wechatPanel);
				wechatPanel.setBackground(Color.white);
				JLabel wechatPhoto = new JLabel();
				wechatPhoto.setIcon(res.getIcon("WeChat_128x128", 128));
				wechatPanel.add(wechatPhoto);
				dialog1.setVisible(true);
			}
		});
		tglbtnNewToggleButton.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(tglbtnNewToggleButton);

		JButton tglbtnNewToggleButton_1 = new JButton("\u652F\u4ED8\u5B9D");
		tglbtnNewToggleButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog dialog1 = new JDialog(frame, "AliPay Donate", true);
				dialog1.setBounds(ScreenSize.width / 2 - 168 / 2, ScreenSize.height / 2 - 198 / 2, 168, 198);
				JPanel aliPayPanel = new JPanel();
				dialog1.getContentPane().add(aliPayPanel);
				aliPayPanel.setBackground(Color.white);
				JLabel aliPayPhoto = new JLabel();
				aliPayPhoto.setIcon(res.getIcon("AliPay_128x128", 128));
				aliPayPanel.add(aliPayPhoto);
				dialog1.setVisible(true);
			}
		});
		tglbtnNewToggleButton_1.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(tglbtnNewToggleButton_1);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		helpPanel.add(panel_1, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(res.getIcon("Icon", 64));
		panel_1.add(lblNewLabel);

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_2 = new JLabel("\u63A8\u5E7F\u8054\u76DF");
		panel_3.add(lblNewLabel_2, BorderLayout.CENTER);
		lblNewLabel_2.setFont(new Font("黑体", Font.BOLD, 36));

		JLabel lblNewLabel_7 = new JLabel("<html><Font color=#000000>\u7248\u672C:"+Version+"</font></html>");
		lblNewLabel_7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_7.setText("<html><u><Font color=#0000ff>\u7248\u672C:"+Version+"</font></u></html>");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel_7.setText("<html><Font color=#000000>\u7248\u672C:"+Version+"</font></html>");
			}
			@Override
			public void mousePressed(MouseEvent e) {
				lblNewLabel_7.setText("<html><u><Font color=#4169e1>\u7248\u672C:"+Version+"</font></u></html>");
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				lblNewLabel_7.setText("<html><u><Font color=#0000ff>\u7248\u672C:"+Version+"</font></u></html>");
				JDialog dialog2 = new JDialog(frame, "模块版本", true);
				dialog2.setBounds(ScreenSize.width / 2 - 300 / 2, ScreenSize.height / 2 - 280 / 2, 300, 280);
				JPanel versionPanel = new JPanel();
				dialog2.getContentPane().add(versionPanel);
				JTextArea textarea = new JTextArea();
				textarea.setEditable(false);
				textarea.setFont(new Font("黑体", Font.PLAIN, 16));
				textarea.append("主程序版本:"+EShare.Version+"\r\n");
				textarea.append("UI版本:"+Ui.Version+"\r\n");
				textarea.append("数据库版本:"+DataBase.Version+"\r\n");
				textarea.append("资源库版本:"+Resources.Version+"\r\n");
				textarea.append("SKU版本:"+SKU.Version+"\r\n");
				textarea.append("自动推送版本:"+AutoPush.Version+"\r\n");
				textarea.append("Push版本:"+Push.Version+"\r\n");
				textarea.append("重构器版本:"+Rebuild.Version+"\r\n");
				textarea.append("\r\n");
				textarea.append("Power by SnowingSDK\r\n");
				versionPanel.setLayout(new BorderLayout());
				versionPanel.add(textarea, BorderLayout.CENTER);
				JButton updateButton = new JButton("检查更新");
				updateButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Desktop dp =Desktop.getDesktop();
						try {
							URI url = new URI("https://github.com/I2048I/JDShare/releases/");
							dp.browse(url);
						} catch (IOException | URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				updateButton.setFont(new Font("黑体", Font.PLAIN, 15));
				versionPanel.add(updateButton, BorderLayout.SOUTH);
				dialog2.setVisible(true);
			}
		});
		lblNewLabel_7.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3.add(lblNewLabel_7, BorderLayout.SOUTH);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(55, 55, 55), 1), "\u5E2E\u52A9\u6587\u6863", TitledBorder.LEADING, TitledBorder.TOP, new Font("黑体", Font.PLAIN, 15), new Color(0, 0, 0)));
		helpPanel.add(panel_2, BorderLayout.CENTER);

		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4);

		JLabel lblNewLabel_9 = new JLabel("[PDF]\u65B0\u624B\u6559\u7A0B");
		lblNewLabel_9.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_4.add(lblNewLabel_9);

		JLabel lblNewLabel_10 = new JLabel("<html><u><Font color=#000000>>></font></u></html>");
		lblNewLabel_10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_10.setText("<html><u><Font color=#0000ff>>></font></u></html>");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel_10.setText("<html><u><Font color=#000000>>></font></u></html>");
			}
			@Override
			public void mousePressed(MouseEvent e) {
				lblNewLabel_10.setText("<html><u><Font color=#4169e1>>></font></u></html>");
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				lblNewLabel_10.setText("<html><u><Font color=#000000>>></font></u></html>");
				Desktop desktop = Desktop.getDesktop();
				File dpFile;
				try {
					dpFile = new File("D:\\QQ\\QQ_Messages\\596799741\\FileRecv\\2月16日周练.pdf");
					desktop.open(dpFile);
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		});
		lblNewLabel_10.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_4.add(lblNewLabel_10);

		JPanel panel_4_1 = new JPanel();
		panel_2.add(panel_4_1);

		JLabel lblNewLabel_9_1 = new JLabel("[PDF]\u8BBE\u7F6E\u7684\u4F7F\u7528");
		lblNewLabel_9_1.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_4_1.add(lblNewLabel_9_1);

		JLabel lblNewLabel_10_1 = new JLabel("<html><u><Font color=#000000>>></font></u></html>");
		lblNewLabel_10_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_10_1.setText("<html><u><Font color=#0000ff>>></font></u></html>");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel_10_1.setText("<html><u><Font color=#000000>>></font></u></html>");
			}
			@Override
			public void mousePressed(MouseEvent e) {
				lblNewLabel_10_1.setText("<html><u><Font color=#4169e1>>></font></u></html>");
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				lblNewLabel_10_1.setText("<html><u><Font color=#000000>>></font></u></html>");
				Desktop desktop = Desktop.getDesktop();
				File dpFile;
				try {
					dpFile = new File("D:\\QQ\\QQ_Messages\\596799741\\FileRecv\\2月16日周练.pdf");
					desktop.open(dpFile);
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		});
		lblNewLabel_10_1.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_4_1.add(lblNewLabel_10_1);

		JPanel panel_4_2 = new JPanel();
		panel_2.add(panel_4_2);

		JLabel lblNewLabel_9_2 = new JLabel("[PDF]\u9AD8\u7EA7\u6559\u7A0B");
		lblNewLabel_9_2.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_4_2.add(lblNewLabel_9_2);

		JLabel lblNewLabel_10_2 = new JLabel("<html><u><Font color=#000000>>></font></u></html>");
		lblNewLabel_10_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_10_2.setText("<html><u><Font color=#0000ff>>></font></u></html>");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel_10_2.setText("<html><u><Font color=#000000>>></font></u></html>");
			}
			@Override
			public void mousePressed(MouseEvent e) {
				lblNewLabel_10_2.setText("<html><u><Font color=#4169e1>>></font></u></html>");
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				lblNewLabel_10_2.setText("<html><u><Font color=#000000>>></font></u></html>");
				Desktop desktop = Desktop.getDesktop();
				File dpFile;
				try {
					dpFile = new File("D:\\QQ\\QQ_Messages\\596799741\\FileRecv\\2月16日周练.pdf");
					desktop.open(dpFile);
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		});
		lblNewLabel_10_2.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_4_2.add(lblNewLabel_10_2);

		JPanel panel_4_3 = new JPanel();
		panel_2.add(panel_4_3);

		JLabel lblNewLabel_9_3 = new JLabel("[PDF]\u5F00\u53D1\u8005\u6A21\u5F0F");
		lblNewLabel_9_3.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_4_3.add(lblNewLabel_9_3);

		JLabel lblNewLabel_10_3 = new JLabel("<html><u><Font color=#000000>>></font></u></html>");
		lblNewLabel_10_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_10_3.setText("<html><u><Font color=#0000ff>>></font></u></html>");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel_10_3.setText("<html><u><Font color=#000000>>></font></u></html>");
			}
			@Override
			public void mousePressed(MouseEvent e) {
				lblNewLabel_10_3.setText("<html><u><Font color=#4169e1>>></font></u></html>");
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				lblNewLabel_10_3.setText("<html><u><Font color=#000000>>></font></u></html>");
				Desktop desktop = Desktop.getDesktop();
				File dpFile;
				try {
					dpFile = new File("D:\\QQ\\QQ_Messages\\596799741\\FileRecv\\2月16日周练.pdf");
					desktop.open(dpFile);
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		});
		lblNewLabel_10_3.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_4_3.add(lblNewLabel_10_3);
		dialog.setVisible(true);
	}

	public static void autoPushPC() {
		pushed = true;
		isgetData = false;
		JDialog dialog = new JDialog(frame, "PC端Push", true);
		dialog.setIconImage(res.getImage("Icon", 32));
		dialog.setBounds(ScreenSize.width / 2 - 420 / 2, ScreenSize.height / 2 - 330 / 2, 420, 330);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				pushed = false;
				EShare.pushDelayCounted = 0;
				isgetData = false;
				dialog.setVisible(false);
			}
		}); 
		JPanel pushPanel = new JPanel();
		dialog.getContentPane().add(pushPanel);
		pushPanel.setLayout(new BorderLayout(0, 5));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "\u63A8\u9001\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, new Font("黑体", Font.PLAIN, 15), new Color(0, 0, 0)));
		pushPanel.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 1, 2, 2));

		JLabel label_qq = new JLabel("QQ\u53F7:"+EShare.autoPushCommand.replace(" ", "").split("/")[1].replace("uin:", ""));
		label_qq.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(label_qq);

		JLabel label_targetID = new JLabel("\u5BF9\u8BDDid:"+EShare.autoPushCommand.replace(" ", "").split("/")[2].replace("quicklunch:", ""));
		label_targetID.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(label_targetID);

		JLabel label_nowPlatform = new JLabel("\u5E73\u53F0:"+(EShare.itemID.charAt(0)=='J' ? "京东" : (EShare.itemID.charAt(0)=='T') ? "淘宝" : "未知"));
		label_nowPlatform.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(label_nowPlatform);

		JLabel label_pushPreview = new JLabel("\u63A8\u9001\u9884\u89C8:"+itemName+"原价￥"+itemRealPrice+"，"+"卷后￥"+itemPrice+"链接:"+itemURL+"优惠劵:"+itemDiscountURL);
		label_pushPreview.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(label_pushPreview);

		JLabel label_pushTime = new JLabel("\u5DF2\u63A8\u9001:0\u6B21(\u5931\u8D250\u6B21)");
		label_pushTime.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(label_pushTime);

		JLabel label_nextPushTime = new JLabel("\u4E0B\u6B21\u63A8\u9001\u5728:0\u79D2\u540E");
		label_nextPushTime.setFont(new Font("黑体", Font.PLAIN, 15));
		panel.add(label_nextPushTime);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pushPanel.add(panel_1, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("\u7EC8\u6B62\u63A8\u9001");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pushed = false;
				EShare.pushDelayCounted = 0;
				isgetData = false;
				dialog.setVisible(false);
			}
		});
		btnNewButton.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_1.add(btnNewButton);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "\u63A8\u9001\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, new Font("黑体", Font.PLAIN, 15), new Color(0, 0, 0)));
		pushPanel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new MigLayout("", "[]", "[]"));

		JLabel lblNewLabel_14_1 = new JLabel("\u63A8\u9001\u95F4\u9694");
		lblNewLabel_14_1.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3.add(lblNewLabel_14_1, "flowx,cell 0 0");


		textField_1 = new JTextField(10);
		textField_1.setText(""+EShare.pushDelay);
		textField_1.setFont(new Font("黑体", Font.PLAIN, 15));
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(!"".equals(textField_1.getText())&&null!=textField_1.getText()) {
					int length = textField_1.getText().length();
					String patch = "0123456789.";
					if(length<5) {
						if (patch.indexOf(e.getKeyChar())< 0) {
							e.consume();
						} else if (patch.indexOf(e.getKeyChar())>= 0) {
							//e.consume();
						}
					} else {
						e.consume();
					}
				} else {
					if ("0123456789.".indexOf(e.getKeyChar())>0) {
					} else {
						e.consume();
					}
				}
			}
		});
		
		panel_3.add(textField_1, "cell 0 0");

		JLabel lblNewLabel_15_1 = new JLabel("s");
		lblNewLabel_15_1.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3.add(lblNewLabel_15_1, "cell 0 0");

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel_2.add(panel_4, BorderLayout.SOUTH);

		JButton btnNewButton_1 = new JButton("\u5E94\u7528");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pushed = false;
				EShare.pushDelayCounted = 0;
				//********SETTING CHANGE*********//
				if(textField_1.getText().length()>0) {
					EShare.pushDelay = Integer.parseInt(textField_1.getText());
				}

				pushed = true;
			}
		});
		btnNewButton_1.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_4.add(btnNewButton_1);
		//自动推送线程
		new Timer().schedule(new TimerTask() {
			public void run() {
				while(pushed) {
					//开始推送
					if(itemID.charAt(0)=='J') {
						itemRealPrice = SKU.getPrice(itemURL);
						if(!"".equals(itemRealPrice)&&null!=itemRealPrice) {
							//SKUloaded = true;
							tk.clipboard(itemName+"\r\n"+"原价￥"+itemRealPrice+"，"+"卷后￥"+itemPrice+"\r\n"+"链接:"+itemURL+"\r\n"+"优惠劵:"+itemDiscountURL);
							Push.updateInfo();
							isgetData = true;
						} else {
							Push.updateInfo();
							autoPushFailueTime+=1;
						}
					} else if(EShare.itemID.charAt(0)=='T') {
						tk.clipboard(itemName+"\r\n"+"原价￥"+itemRealPrice+"，"+"卷后￥"+itemPrice+"\r\n"+"淘口令:"+itemTaoKey);
						isgetData = true;
					}

					if(isgetData) {
						tk.runShell(autoPushCommand);//启动qq对话框
						try {
							Thread.sleep(1000);
							Robot rb = new Robot();
							rb.keyPress(KeyEvent.VK_CONTROL);
							rb.keyPress(KeyEvent.VK_V);
							rb.keyRelease(KeyEvent.VK_CONTROL);
							rb.keyRelease(KeyEvent.VK_V);
							rb.keyPress(KeyEvent.VK_CONTROL);
							rb.keyPress(KeyEvent.VK_ENTER);
							rb.keyRelease(KeyEvent.VK_CONTROL);
							rb.keyRelease(KeyEvent.VK_ENTER);

						} catch (AWTException | InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						pushTime+=1;
						autoPushTime+=1;
						Push.updateInfo();
						if(null==itemID&&!"".equals(itemID)) {
							JOptionPane.showMessageDialog(frame, "当前无商品", "错误", JOptionPane.ERROR_MESSAGE);
							pushed = false;
							EShare.pushDelayCounted = 0;
							isgetData = false;
							dialog.setVisible(false);
							labelReflash();
						}
						label_pushPreview.setText("\u63A8\u9001\u9884\u89C8:"+itemName+"原价￥"+itemRealPrice+"，"+"卷后￥"+itemPrice+"淘口令:"+itemTaoKey);
						label_nowPlatform.setText("\u5E73\u53F0:"+(EShare.itemID.charAt(0)=='J' ? "京东" : (EShare.itemID.charAt(0)=='T') ? "淘宝" : "未知"));
						labelReflash();
					}
					label_pushTime.setText("\u5DF2\u63A8\u9001:"+autoPushTime+"\u6B21(\u5931\u8D25"+autoPushFailueTime+"\u6B21)");
					//结束推送
					//JDShare.pushDelay = 10;
					isgetData = false;
					for(EShare.pushDelayCounted=0;EShare.pushDelayCounted<EShare.pushDelay;EShare.pushDelayCounted++) {
						if(!pushed) {
							break;
						} else {
							label_nextPushTime.setText("\u4E0B\u6B21\u63A8\u9001\u5728:"+(EShare.pushDelay-EShare.pushDelayCounted)+"\u79D2\u540E");
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}, 10);

		dialog.setVisible(true);
	}
	public static void labelReflash() {
		if(null!=itemID&&!"".equals(itemID)) {
			label_platform.setText("平台:"+(EShare.itemID.charAt(0)=='J' ? "京东" : (EShare.itemID.charAt(0)=='T') ? "淘宝" : "未知"));
			label_enableItems.setText("可用商品:"+copyableItems);
			label_itemName.setText("名称:"+itemName);
			label_itemID.setText("ID:"+itemID);
			label_cashInfo.setText("折后价:￥"+itemPrice+"    返佣点数:"+itemRepayPencent+"%    返佣金:￥"+itemRepay);
		} else {
			label_platform.setText("平台:未知");
			label_enableItems.setText("可用商品:0");
			label_itemName.setText("名称:无可用商品，请获取新的CSV文件");
			label_itemID.setText("ID:Unknown ID");
			label_cashInfo.setText("折后价:￥0    返佣点数:0%    返佣金:￥0");
		}
		if(null!=dataUrl&&!"".equals(dataUrl)) {
			textField_CVSFile.setText(dataUrl);
		}
	}

	public static JButton buttonImgAdd(JButton button, String name) {
		button.setText("");
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setIcon(res.getIcon(name, 1));
		button.setRolloverIcon(res.getIcon(name, 2));
		button.setPressedIcon(res.getIcon(name, 3));
		return button;
	}
	public static JButton switchButton(JButton button, boolean statue) {
		button.setText("");
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		if(statue) {
			button.setIcon(res.getIcon("SwitchON", 1));
			button.setRolloverIcon(res.getIcon("SwitchON", 2));
			button.setPressedIcon(res.getIcon("SwitchON", 3));
		} else {
			button.setIcon(res.getIcon("SwitchOFF", 1));
			button.setRolloverIcon(res.getIcon("SwitchOFF", 2));
			button.setPressedIcon(res.getIcon("SwitchOFF", 3));
		}
		return button;
	}
}
