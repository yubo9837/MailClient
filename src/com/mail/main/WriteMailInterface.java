package com.mail.main;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class WriteMailInterface extends JFrame {

	private JPanel contentPane;
	private JTextField receive;
	private JTextField mailTitle;

	//发送
	private Action send = new AbstractAction("发送") {
		public void actionPerformed(ActionEvent e) {
			send();
		}
	};
	//保存至发件箱
	private Action saveToSend = new AbstractAction("保存至发件箱") {
		public void actionPerformed(ActionEvent e) {
			saveToSend();
		}
	};
	
	//保存至草稿箱
	private Action saveToDraft = new AbstractAction("保存至草稿箱") {
		public void actionPerformed(ActionEvent e) {
			saveToDraft();
		}
	};
	//上传附件
	private Action uploadFile = new AbstractAction("上传附件") {
		public void actionPerformed(ActionEvent e) {
			uploadFile();
		}
	};
	//删除附件
	private Action deleteFile = new AbstractAction("删除附件") {
		public void actionPerformed(ActionEvent e) {
			deleteFile();
		}
	};

	/**
	 * Create the frame.
	 */
	public WriteMailInterface() {
		setTitle("写邮件");
		init();
	}
	
//	初始化界面
	private void init() {
		setBounds(100, 100, 773, 490);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelReceive = new JLabel("收件人：");
		labelReceive.setBounds(28, 112, 54, 15);
		contentPane.add(labelReceive);
		
		JLabel labelTitle = new JLabel("主题：");
		labelTitle.setBounds(28, 149, 54, 15);
		contentPane.add(labelTitle);
		//发件人
		receive = new JTextField();
		receive.setBounds(79, 109, 336, 21);
		contentPane.add(receive);
		receive.setColumns(10);
//		主题
		mailTitle = new JTextField();
		mailTitle.setBounds(79, 146, 336, 21);
		contentPane.add(mailTitle);
		mailTitle.setColumns(10);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBounds(28, 10, 542, 74);
		contentPane.add(toolBar);
		toolBar.add(this.send);
		toolBar.add(this.saveToSend);
		toolBar.add(this.saveToDraft);
		toolBar.add(this.uploadFile);
		toolBar.add(this.deleteFile);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(28, 193, 696, 248);
		contentPane.add(splitPane);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
	}
	//发送按钮
	private void send() {
		
	}
//	保存到收件箱
	private void saveToSend() {
		
	}
//保存到草稿箱
	private void saveToDraft() {
	
	}
//上传附件
	private void uploadFile() {
	
	}
//删除附件
	private void deleteFile() {
	
	}
}

