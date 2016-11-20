package com.mail.main;

import javax.swing.JFrame;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.mail.box.DeletedBox;
import com.mail.box.DraftBox;
import com.mail.box.ReceiveBox;
import com.mail.box.SentBox;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JLabel;

//主界面
public class MainInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private SetupInterface setupInterface;
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
	
	//收邮件
	private AbstractAction in = new AbstractAction("收取邮件") {
		public void actionPerformed(ActionEvent e) {
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
		this.context=context;
		init();
		creatToolBar();
		this.tree=createTree();
	}
	
	private void openSetup() {
		if(this.setupInterface==null){
			this.setupInterface=new SetupInterface(this);
		}
		setupInterface.setVisible(true);
	}
	
	public MailContext getContext() {
		return context;
	}

	public void setContext(MailContext context) {
		this.context = context;
	}

	private JTree createTree() {
		DefaultMutableTreeNode root=new DefaultMutableTreeNode();
		root.add(new DefaultMutableTreeNode(new DeletedBox()));
		root.add(new DefaultMutableTreeNode(new DraftBox()));
		root.add(new DefaultMutableTreeNode(new ReceiveBox()));
		root.add(new DefaultMutableTreeNode(new SentBox()));
		root.add(new DefaultMutableTreeNode(new DraftBox()));
		
		JTree tree = new JTree(root);
		//隐藏根节点
		tree.setRootVisible(true);
		return tree;
	}
	
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 718, 441);
		getContentPane().setLayout(null);
		
		toolBar = new JToolBar();
		toolBar.setBounds(6, 6, 579, 32);
		getContentPane().add(toolBar);
		
		welcomeLabel = new JLabel("欢迎");
		
		splitPane = new JSplitPane();
		splitPane.setBounds(16, 50, 569, 327);
		getContentPane().add(splitPane);
		
		treePane = new JScrollPane();
		splitPane.setLeftComponent(treePane);
		
		tree = new JTree();
		tree=createTree();
		
		mailList = new JSplitPane();
		mailList.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		mailList.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(mailList);
		
		tablePane = new JScrollPane();
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
		this.toolBar.add(welcomeLabel);
	}
}
