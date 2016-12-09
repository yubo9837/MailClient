package com.mail.main;

import javax.swing.JFrame;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import com.mail.box.MailBox;
import com.mail.box.DeletedBox;
import com.mail.box.DraftBox;
import com.mail.box.InBox;
import com.mail.box.SentBox;
import com.mail.file.Mail;
import com.mail.opration.ReceiveMail;
import com.mail.setup.MainSetup;
import com.mail.system.SystemHandler;
import com.mail.system.SystemLoader;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JLabel;

//主界面
public class MainInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private MainSetup mainSetup;
	private WriteMailInterface writeMailInterface;
	private JToolBar toolBar;//操作选项
	private JSplitPane splitPane;
	private JScrollPane treePane;
	private JTree tree;
	private JSplitPane mailList;//邮件列表
	private JScrollPane tablePane;
	private JSplitPane splitPane_1;
	private JScrollPane filePane;//文件显示区
	private JList list;
	private JScrollPane mailScrollPane;
	private JTextArea textArea;
	private JLabel welcomeLabel;//欢迎语
	
	//当前打开的文件对象
	private Mail currentMail;
	
	//邮件列表的JTable
	private MailListTable mailListTable;
	
	//收件箱的Mail对象集合，代表所有在收件箱中的邮件
	private List<Mail> inMails;
	//发件箱的邮件集合
	private List<Mail> outMails;
	//成功发送的邮件集合
	private List<Mail> sendMails;
	//草稿箱的邮件集合
	private List<Mail> draftMails;
	//垃圾箱的邮件集合
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
	//发送邮件
	private AbstractAction sent = new AbstractAction("发送邮件") {
		public void actionPerformed(ActionEvent e) {
		}
	};
	
	//写邮件
	private AbstractAction write = new AbstractAction("写邮件") {
		public void actionPerformed(ActionEvent e) {
			writeMail();
		}
	};
		
	//回复邮件
	private AbstractAction reply = new AbstractAction("回复邮件") {
		public void actionPerformed(ActionEvent e) {
		}
	};
		
	//转发邮件
	private AbstractAction transmit = new AbstractAction("转发邮件") {
		public void actionPerformed(ActionEvent e) {
		}
	};
		
	//删除邮件
	private AbstractAction delete = new AbstractAction("删除邮件") {
		public void actionPerformed(ActionEvent e) {
		}
	};
		
	//彻底邮件
	private AbstractAction deepDelete = new AbstractAction("彻底邮件") {
		public void actionPerformed(ActionEvent e) {
		}
	};
		
	//还原邮件
	private AbstractAction revert = new AbstractAction("还原邮件") {
		public void actionPerformed(ActionEvent e) {
		}
	};
		
	//设置
	private AbstractAction setup = new AbstractAction("设置") {
		public void actionPerformed(ActionEvent e) {
			openSetup();
		}
	};
	
	private MailContext context;
	
	public MainInterface(MailContext context) {
		setTitle("主界面");
		this.context=context;
		initMails();init();
		
		
		initListeners();
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
	
	//清空当前打开的邮件及对应的界面组件
	public void cleanMailInfo() {
		//设置当前打开的邮件对象为空
		this.currentMail = null;
		this.textArea.setText("");
	}

	private JTree createTree() {
		DefaultMutableTreeNode root=new DefaultMutableTreeNode();
		root.add(new DefaultMutableTreeNode(new InBox()));
		root.add(new DefaultMutableTreeNode(new DraftBox()));
		root.add(new DefaultMutableTreeNode(new InBox()));
		root.add(new DefaultMutableTreeNode(new SentBox()));
		root.add(new DefaultMutableTreeNode(new DeletedBox()));
		
		JTree tree = new JTree(root);
		//加入鼠标监听器
		tree.addMouseListener(new TreeListener(this));
		//隐藏根节点
		tree.setRootVisible(false);
		//设置节点处理类
		TreeSelece cellRenderer = new TreeSelece();
		tree.setCellRenderer(cellRenderer);
		return tree;
	}
//	初始化界面
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1366, 768);
		getContentPane().setLayout(null);
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBounds(6, 6, 579, 32);
		getContentPane().add(toolBar);
		creatToolBar();
		
		splitPane = new JSplitPane();
		splitPane.setAutoscrolls(true);
		splitPane.setBounds(16, 48, 1200, 700);
		getContentPane().add(splitPane);
		
		this.currentMails=this.inMails;
		
		tree = new JTree();
		tree=createTree();
		
		treePane = new JScrollPane(this.tree);
		splitPane.setLeftComponent(treePane);
		
		DefaultTableModel tableModel=new DefaultTableModel();
		this.mailListTable=new MailListTable(tableModel);
		tableModel.setDataVector(createViewDatas(this.currentMails), getListColumn());
		
		mailList = new JSplitPane();
		mailList.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		mailList.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(mailList);
		
		setTableFace();
		tablePane = new JScrollPane(this.mailListTable);
		mailList.setRightComponent(tablePane);
		
		splitPane_1 = new JSplitPane();
		mailList.setLeftComponent(splitPane_1);
		
		filePane = new JScrollPane();
		splitPane_1.setLeftComponent(filePane);
		
		list = new JList();
		filePane.setViewportView(list);
		
		mailScrollPane = new JScrollPane();
		splitPane_1.setRightComponent(mailScrollPane);
		
		textArea = new JTextArea();
		mailScrollPane.setViewportView(textArea);
		//设置欢迎语
		welcomeLabel = new JLabel("欢迎:"+context.getUser());
		welcomeLabel.setBounds(690, 23, 84, 15);
		getContentPane().add(welcomeLabel);
	}
	//初始化时创建各个box中的数据
	private void initMails() {
		this.inMails = this.systemLoader.getInBoxMails(this.context);
		this.draftMails = this.systemLoader.getDraftBoxMails(this.context);
		this.deleteMails = this.systemLoader.getDeletedBoxMails(this.context);
		this.outMails = this.systemLoader.getOutBoxMails(this.context);
		this.sendMails = this.systemLoader.getSentBoxMails(this.context);
	}
	//时间格式对象
	private DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	//格式时间
	private String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	//设置邮件列表的样式
	private void setTableFace() {
		//隐藏邮件对应的xml文件的名字
		this.mailListTable.getColumn("xmlName").setMinWidth(0);
		this.mailListTable.getColumn("xmlName").setMaxWidth(0);
		this.mailListTable.getColumn("打开").setMaxWidth(40);
		this.mailListTable.getColumn("发件人").setMinWidth(200);
		this.mailListTable.getColumn("主题").setMinWidth(320);
		this.mailListTable.getColumn("日期").setMinWidth(130);
		this.mailListTable.getColumn("大小").setMinWidth(80);
		this.mailListTable.setRowHeight(30);
	}
	//将邮件数据集合转换成视图的格式
	@SuppressWarnings("unchecked")
	private Vector<Vector> createViewDatas(List<Mail> mails) {
		Vector<Vector> views = new Vector<Vector>();
		for (Mail mail : mails) {
			Vector view = new Vector();
			view.add(mail.getXmlName());
			view.add(mail.getSender());
			view.add(mail.getSubject());
			view.add(formatDate(mail.getReceiveDate()));
			view.add(mail.getSize() + "k");
			views.add(view);
		}
		return views;
	}
	
	//获得邮件列表的列名
	@SuppressWarnings("unchecked")
	private Vector getListColumn() {
		Vector columns = new Vector();
		columns.add("xmlName");
		columns.add("打开");
		columns.add("发件人");
		columns.add("主题");
		columns.add("日期");
		columns.add("大小");
		return columns;
	}
	
	//创建toolbar中的按钮
	private void creatToolBar() {
		this.toolBar.add(this.in);
		this.toolBar.add(this.sent);
		this.toolBar.add(this.write);
		this.toolBar.add(this.reply);
		this.toolBar.addSeparator(new Dimension(20, 0));
		this.toolBar.add(this.transmit);
		this.toolBar.add(this.delete);
		this.toolBar.add(this.deepDelete);
		this.toolBar.add(this.revert);
		this.toolBar.add(this.setup);
		this.toolBar.addSeparator(new Dimension(20, 0));
	}
	
	private void writeMail() {
		writeMailInterface=new WriteMailInterface(this);
		writeMailInterface.setVisible(true);
	}
	
	public void setMailContext(MailContext context) {
		this.context = context;
	}
	
	public MailContext getMailContext() {
		return this.context;
	}
	
	private void receiveMail() {
		try {
			List<Mail> newMails=receiveMail.getMessages(this.context);
			this.inMails.addAll(0,newMails);
			saveToInBox(newMails);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("出错");
		}
	}
	
	//保存到本地的收件箱中, 具体目录是: 用户名/邮件帐号名/inbox/Mail对象的uuid.xml
	private void saveToInBox(List<Mail> newMails) {
		for (Mail mail : newMails) {
			//生成xml来存放这些新的邮件
			systemHandler.saveInBox(mail, this.context);
		}
	}
	
	private void initListeners() {
		//列表选择监听器
		this.mailListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent event) {
				//当选择行时鼠标释放时才执行
				if (!event.getValueIsAdjusting()) {
					//如果没有选中任何一行, 则返回
					if (mailListTable.getSelectedRowCount() != 1) return;
					viewMail();
				}
			}
		});
	}
	
	//查看一封邮件
	private void viewMail() {
		this.textArea.setText("");
		Mail mail = getSelectMail();
		this.textArea.append("发送人：  " + mail.getSender());
		this.textArea.append("\n");
		this.textArea.append("收件人:   " + mail.getReceiverString());
		this.textArea.append("\n");
		this.textArea.append("主题：  " + mail.getSubject());
		this.textArea.append("\n");
		this.textArea.append("接收日期：  " + dateFormat.format(mail.getReceiveDate()));
		this.textArea.append("\n\n");
		this.textArea.append("邮件正文：  ");
		this.textArea.append("\n\n");
		this.textArea.append(mail.getContent());
		//设置当前被打开的邮件对象
		this.currentMail = mail;
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
			this.currentMails = this.sendMails;
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
	
	//刷新列表的方法, 参数是不同的数据
	public void refreshTable() {
		DefaultTableModel tableModel=new DefaultTableModel();
		this.mailListTable=new MailListTable(tableModel);
		tableModel.setDataVector(createViewDatas(this.currentMails), getListColumn());
		setTableFace();
	}
}
