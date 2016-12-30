package com.mail.main;

import javax.swing.JFrame;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.mail.box.MailBox;
import com.mail.box.DeletedBox;
import com.mail.box.DraftBox;
import com.mail.box.InBox;
import com.mail.box.SentBox;
import com.mail.file.FileOp;
import com.mail.file.Mail;
import com.mail.opration.ReceiveMail;
import com.mail.opration.SystemHandler;
import com.mail.opration.SystemLoader;
import com.mail.setup.MainSetup;
import com.mail.main.FileMouseListener;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;

//主界面
public class MainInterface extends JFrame {
	private static final long serialVersionUID = 1L;

	private MailContext context;
	
	private MainSetup mainSetup;
	private WriteMailInterface writeMailInterface;
	//工具条
	private JToolBar toolBar;
	private JSplitPane splitPane;
	private JScrollPane treePane;
	private JTree tree;
	//邮件列表
	private JSplitPane mailList;
	private JScrollPane tablePane;
	private JSplitPane splitPane_1;
	//文件显示区
	private JScrollPane filePane;
	private JList fileList;
	private JScrollPane mailScrollPane;
	private JTextArea textArea;
	private JTextField searchText;
	//邮件列表的JTable
	private MailListTable mailListTable;
	
	//收件箱的邮件对象集合
	private List<Mail> inMails;
	//发件箱
	private List<Mail> sentMails;
	//草稿箱
	private List<Mail> draftMails;
	//垃圾箱
	private List<Mail> deleteMails;
	//当前界面列表所显示的对象
	private List<Mail> currentMails;
	
	private SystemLoader systemLoader=new SystemLoader();
	private SystemHandler systemHandler=new SystemHandler();
	ReceiveMail receiveMail=new ReceiveMail();
	
	//收邮件
	private AbstractAction in = new AbstractAction("收取邮件") {
		public void actionPerformed(ActionEvent e) {
			receiveMail();
		}
	};
	
	//收邮件
	private AbstractAction write = new AbstractAction("写邮件") {
		public void actionPerformed(ActionEvent e) {
			writeMail();
		}
	};
		
	//设置
	private AbstractAction setup = new AbstractAction("设置") {
		public void actionPerformed(ActionEvent e) {
			openSetup();
		}
	};
	
//	10秒延时
	private long receiveInterval = 1000 * 10;
	public MainInterface(MailContext context) {
		setResizable(false);
		writeMailInterface=new WriteMailInterface(this);
		setTitle("mailClient");
		this.context=context;
		initMails();
		initFrame();
		initListeners();
		Timer timer = new Timer();
		timer.schedule(new ReceiveTask(this), 10000, this.receiveInterval);
	}
	
	//初始化时创建各个box中的数据
	private void initMails() {
		this.inMails = this.systemLoader.getInBoxMails(this.context);
		this.draftMails = this.systemLoader.getDraftBoxMails(this.context);
		this.deleteMails = this.systemLoader.getDeletedBoxMails(this.context);
		this.sentMails = this.systemLoader.getSentBoxMails(this.context);
	}
	
//	初始化界面
	private void initFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1240, 710);
		getContentPane().setLayout(null);
		//工具栏
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBounds(16, 6, 175, 32);
		getContentPane().add(toolBar);
		creatToolBar();
		
		splitPane = new JSplitPane();
		splitPane.setAutoscrolls(true);
		splitPane.setBounds(16, 48, 1200, 613);
		
		
		this.currentMails=this.inMails;
		//左侧功能列表
		tree = new JTree();
		tree=createTree();
		
		treePane = new JScrollPane(this.tree);
		splitPane.setLeftComponent(treePane);
		
		DefaultTableModel tableModel=new DefaultTableModel();
		tableModel.setDataVector(createViewDatas(this.currentMails), getListColumn());
		this.mailListTable=new MailListTable(tableModel);
		
		
		setTableFace();
		mailList = new JSplitPane();
		mailList.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		mailList.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tablePane = new JScrollPane(this.mailListTable);
		mailList.setRightComponent(tablePane);
		splitPane.setRightComponent(mailList);
		getContentPane().add(splitPane);
		
		
		
		splitPane_1 = new JSplitPane();
		mailList.setLeftComponent(splitPane_1);
		//附件区域
		filePane = new JScrollPane();
		splitPane_1.setLeftComponent(filePane);
		
		fileList = new JList();
		this.fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.fileList.addMouseListener(new FileMouseListener());
		filePane.setViewportView(fileList);
		
		mailScrollPane = new JScrollPane();
		mailScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane_1.setRightComponent(mailScrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		//自动换行 
		textArea.setLineWrap(true);        
		textArea.setWrapStyleWord(true);  
		mailScrollPane.setViewportView(textArea);
		
		//下拉列表
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("按发件人");
		comboBox.addItem("按主题");
		comboBox.addItem("按内容");
		comboBox.setBounds(518, 17, 90, 21);
		getContentPane().add(comboBox);
//		搜索框
		searchText = new JTextField();
		searchText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if(event.getKeyChar()==KeyEvent.VK_ENTER)
					//搜索
					if(searchText.getText().equals("")){
						JOptionPane.showMessageDialog(null, "请输入搜索内容");
					}else{
						search(searchText.getText(),comboBox.getSelectedIndex());
					}
			}
		});
		searchText.setBounds(611, 17, 169, 21);
		getContentPane().add(searchText);
		searchText.setColumns(10);

//		搜索按钮
		JButton searchButton = new JButton(new ImageIcon("image/search.png"));
		searchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getButton()==1){
					//搜索
					if(searchText.getText().equals("")){
						JOptionPane.showMessageDialog(null, "请输入搜索内容");
					}else{
						search(searchText.getText(),comboBox.getSelectedIndex());
					}
				}
			}
		});
		searchButton.setBounds(781, 15, 19, 20);
		getContentPane().add(searchButton);
		
	}
//	右键菜单
	JPopupMenu menu = new JPopupMenu();
	private  MouseInputListener rightMouseClick() {
		JMenuItem resent,reply,transmit,delete,realDelete,revert;
		menu=new JPopupMenu();
		resent=new JMenuItem("重发");
		reply=new JMenuItem("回复");
		transmit=new JMenuItem("转发");
		delete=new JMenuItem("删除");
		realDelete=new JMenuItem("彻底删除");
		revert=new JMenuItem("还原");
		if(currentMails==inMails){
			menu.add(reply);
			menu.add(transmit);
			menu.add(delete);
			menu.add(realDelete);
		}else if(currentMails==draftMails){
			menu.add(resent);
			menu.add(delete);
			menu.add(realDelete);
		}else if(currentMails==deleteMails){
			menu.add(revert);
			menu.add(realDelete);
		}else if(currentMails==sentMails){
			menu.add(resent);
			menu.add(delete);
			menu.add(realDelete);
		}
//		回复
		reply.addActionListener(new ActionListener() {
	       public void actionPerformed(ActionEvent e) {  
	           reply();
	       }  
	    }); 
//		转发
		transmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				transmit();
			}
		});
//		删除
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
//		彻底删除
		realDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realDelete();
			}
		});
//		还原
		revert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				revert();
			}
		});
//		重发
		resent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resent();
			}
		});
		
		return new MouseInputListener() {
			
			@Override
			public void mouseClicked(MouseEvent event ) {
				if(event.getButton()==MouseEvent.BUTTON3)
					menu.show(MainInterface.this.mailListTable,event.getX(), event.getY());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		};
	}
//	搜索功能实现方法
	private void search(String str,int k){
		int rowCount = mailListTable.getRowCount();//行
//		int columnCount = mailListTable.getColumnCount();//列
		for (int i = 0; i < rowCount; i++) {
//			for (int k = 0; k < columnCount; k++) {
				String value = mailListTable.getValueAt(i, k).toString();
				if (value.contains(str)) {
					mailListTable.getSelectionModel().setSelectionInterval(i, i);
					Rectangle cellRect = mailListTable.getCellRect(i, k, true);
					mailListTable.scrollRectToVisible(cellRect);
					return;
				}
//			}
		}
	}
//	初始化监听器
	private void initListeners() {
		//列表选择监听器
		this.mailListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent event) {
				//当选择行时鼠标释放时才执行
				if (!event.getValueIsAdjusting()) {
					//如果没有选中任何一行, 则返回
					if (mailListTable.getSelectedRowCount() != 1) return;
					mailListTable.addMouseListener(rightMouseClick());
					viewMail();
				}
			}
		});
	}
	
	private JTree createTree() {
		DefaultMutableTreeNode root=new DefaultMutableTreeNode();
		root.add(new DefaultMutableTreeNode(new InBox()));
		root.add(new DefaultMutableTreeNode(new DraftBox()));
		root.add(new DefaultMutableTreeNode(new SentBox()));
		root.add(new DefaultMutableTreeNode(new DeletedBox()));
		
		JTree tree = new JTree(root);
		//加入鼠标监听器
		tree.addMouseListener(new TreeListener(this));
		//隐藏根节点
		tree.setRootVisible(false);
		//设置节点处理类
		TreeSelece treeSelece = new TreeSelece();
		tree.setCellRenderer(treeSelece);
		return tree;
	}
	
	//清空当前打开的邮件及对应的界面组件
	public void cleanMailInfo() {
		this.textArea.setText("");
		this.fileList.setListData(new Object[]{});
	}

	//时间格式对象
	private DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	
	//格式时间
	private String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	//设置邮件列表的样式
	private void setTableFace() {
		this.mailListTable.getColumn("内容").setMinWidth(0);
		this.mailListTable.getColumn("内容").setMaxWidth(0);
		this.mailListTable.getColumn("xmlName").setMinWidth(0);
		this.mailListTable.getColumn("xmlName").setMaxWidth(0);
		if(currentMails==inMails||currentMails==deleteMails){
			this.mailListTable.getColumn("发件人").setMinWidth(200);
		}else{
			this.mailListTable.getColumn("收件人").setMinWidth(200);
		}
		this.mailListTable.getColumn("主题").setMinWidth(300);
		
		this.mailListTable.getColumn("日期").setMinWidth(100);
		this.mailListTable.getColumn("大小").setMinWidth(80);
		
		this.mailListTable.setRowHeight(30);
	}
	//将邮件数据集合转换成视图的格式
	@SuppressWarnings("unchecked")
	private Vector<Vector> createViewDatas(List<Mail> mails) {
		Vector<Vector> views = new Vector<Vector>();
		for (Mail mail : mails) {
			Vector view = new Vector();
			if(currentMails==inMails||currentMails==deleteMails){
				view.add(mail.getSender());
			}else{
				view.add(mail.getReceivers());
			}
			view.add(mail.getSubject());
			view.add(mail.getContent());
			view.add(formatDate(mail.getReceiveDate()));
			view.add(mail.getSize() + "k");
			view.add(mail.getXmlName());
			views.add(view);
		}
		return views;
	}
	
	//获得邮件列表的列名
	@SuppressWarnings("unchecked")
	private Vector getListColumn() {
		Vector columns = new Vector();
		if(currentMails==inMails||currentMails==deleteMails){
			columns.add("发件人");
		}else{
			columns.add("收件人");
		}
		columns.add("主题");
		columns.add("内容");
		columns.add("日期");
		columns.add("大小");
		columns.add("xmlName");
		return columns;
	}
	
	//创建toolbar中的按钮
	private void creatToolBar() {
		this.toolBar.add(this.in);
		this.toolBar.add(this.write);
		this.toolBar.addSeparator(new Dimension(20, 0));
		this.toolBar.add(this.setup);
	}
	
//	写邮件实现
	private void writeMail() {
		this.writeMailInterface.setVisible(true);
		this.writeMailInterface.clean();
	}
	
//	收邮件实现
	void receiveMail() {
		try {
			List<Mail> newMails=this.receiveMail.getMessages(this.context);
			if(newMails.size()!=0){
				this.inMails.addAll(0,newMails);
				saveToInBox(newMails);
				refreshTable();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	回复邮件
	void reply(){
		Mail mail=getSelectMail();
		if(noSelectData(mail)) return;
		this.writeMailInterface.replyInit(mail);
	}
	
//	转发邮件
	void transmit(){
		Mail mail = getSelectMail();
		if (noSelectData(mail)) return;
		this.writeMailInterface.transmitInit(mail);
	}
	
//	重发草稿箱的邮件
	void resent(){
		Mail mail = getSelectMail();
		if (noSelectData(mail)) return;
		this.writeMailInterface.reSentInit(mail);
	}
	
//	删除邮件
	void delete(){
		Mail mail = getSelectMail();
		if (noSelectData(mail)) return;
		//判断垃圾箱中是否有该份邮件(已经被放到垃圾箱中), 有的话不进行处理
		if (!this.deleteMails.contains(mail)) {
			//从当前的集合中删除
			this.currentMails.remove(mail);
			//加到垃圾箱的集合中
			this.deleteMails.add(0, mail);
			//将邮件对应的xml文件放到deleted的目录中
			this.systemHandler.delete(mail, this.context);
		}
		//刷新列表
		refreshTable();
		cleanMailInfo();
	}
	
//	彻底删除邮件
	void realDelete(){
		Mail mail = getSelectMail();
		if (noSelectData(mail)) return;
		//从当前的集合中删除 
		this.currentMails.remove(mail);
		//删除xml文件和对应的附件
		this.systemHandler.realDelete(mail, this.context);
		this.currentMails = deleteMails;
		refreshTable();
		cleanMailInfo();
	}
	
//	还原邮件
	void revert(){
		Mail mail = getSelectMail();
		if (noSelectData(mail)) return;
		//垃圾箱包含这个Mail对象才进行还原
		if (this.deleteMails.contains(mail)) {
			//从垃圾箱集合中删除
			this.deleteMails.remove(mail);
			//操作文件, 并deleted目录中的xml中
			this.systemHandler.revert(mail, this.context);
			//还原到各个集合中
			revertMailToList(mail);
		}
		refreshTable();
		cleanMailInfo();
	}
	
	public void setMailContext(MailContext context) {
		this.context = context;
	}
	
	public MailContext getMailContext() {
		return this.context;
	}

	
	private boolean noSelectData(Mail mail) {
		if (mail == null) {
			return true;
		}
		return false;
	}

	
	//还原Mail对象到各个相应的集合
	private void revertMailToList(Mail mail) {
		if (mail.getFrom().equals(FileOp.INBOX)) {
			this.inMails.add(mail);
		} else if (mail.getFrom().equals(FileOp.SENT)) {
			this.sentMails.add(mail);
		} else if (mail.getFrom().equals(FileOp.DRAFT)) {
			this.draftMails.add(mail);
		} 
	}
	
	//保存到本地的收件箱中, 具体目录是: 用户名/邮件帐号名/inbox/Mail对象的uuid.xml
	private void saveToInBox(List<Mail> newMails) {
		for (Mail mail : newMails) {
			//生成xml来存放这些新的邮件
			systemHandler.saveInBox(mail, this.context);
		}
	}
	
	//查看一封邮件
	private void viewMail() {
		this.textArea.setText("");
		Mail mail = getSelectMail();
		this.textArea.append("发送人：  " + mail.getSender());
		this.textArea.append("\n");
		this.textArea.append("主题：  " + mail.getSubject());
		this.textArea.append("\n");
		this.textArea.append("接收日期：  " + dateFormat.format(mail.getReceiveDate()));
		this.textArea.append("\n\n");
		this.textArea.append("邮件正文：  ");
		this.textArea.append("\n\n");
		this.textArea.append(mail.getContent());
		//添加附件
		this.fileList.setListData(mail.getFiles().toArray());
	}
	
	//获取在列表中所选择的Mail对象
	private Mail getSelectMail() {
		String xmlName = getSelectXmlName();
		return getMail(xmlName, this.currentMails);
	}
	//从集合中找到xmlName与参数一致的Mail对象
	private Mail getMail(String xmlName, List<Mail> mails) {
		for (Mail m : mails) {
			if (m.getXmlName().equals(xmlName))return m;
		}
		return null;
	}
	
	//获得列表中所选行的xmlName列的值（该值是唯一的）
	private String getSelectXmlName() {
		int row = this.mailListTable.getSelectedRow();
		int column = this.mailListTable.getColumn("xmlName").getModelIndex();
		if (row == -1) return null;
		String xmlName = (String)this.mailListTable.getValueAt(row, column);
		return xmlName;
	}
	
	public void select() {
		MailBox box = getSelectBox();
		if (box instanceof InBox) {
			this.currentMails = this.inMails;
		} else if (box instanceof SentBox) {
			this.currentMails = this.sentMails;
		} else if (box instanceof DraftBox) {
			this.currentMails = this.draftMails;
		} else {
			this.currentMails = this.deleteMails;
		}
		//刷新列表
		refreshTable();
		//设置当前打开的邮件对象为空并清空组件
		cleanMailInfo();
	}
	
	//获得当前选中的box
	private MailBox getSelectBox() {
		TreePath treePath = this.tree.getSelectionPath();
		if (treePath == null) return null;
		//获得选中的TreeNode
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePath.getLastPathComponent();
		return (MailBox)node.getUserObject();
	}
	
	//在已发送的集合中添加一个邮件对象
	public void addSentMail(Mail mail) {
		this.sentMails.add(0, mail);
		refreshTable();
	}
	
	//在草稿箱的集合中添加一个邮件对象
	public void addDraftMail(Mail mail) {
		this.draftMails.add(0, mail);
		refreshTable();
	}

	
	//刷新列表的方法, 参数是不同的数据
	public void refreshTable() {
		DefaultTableModel tableModel = (DefaultTableModel)this.mailListTable.getModel();
		tableModel.setDataVector(createViewDatas(this.currentMails), getListColumn());
		setTableFace();
	}
	
	private void openSetup() {
		if(this.mainSetup==null){
			this.mainSetup=new MainSetup(this);
		}
		mainSetup.setVisible(true);
	}
	
	public MailContext getContext() {
		return context;
	}

	public void setContext(MailContext context) {
		this.context = context;
	}
}

class ReceiveTask extends TimerTask {

	private MainInterface mainInterface;
	
	public ReceiveTask(MainInterface mainInterface) {
		this.mainInterface = mainInterface;
	}
	
	public void run() {
		try {
			this.mainInterface.getMailContext().getStore();
			this.mainInterface.receiveMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
