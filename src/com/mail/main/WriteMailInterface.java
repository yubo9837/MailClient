package com.mail.main;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

import com.mail.file.Mail;
import com.mail.opration.SendMail;

public class WriteMailInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField receive;
	private JTextField mailTitle;
	
	private MainInterface mainInterface;
	private SendMail sendMail= new SendMail();

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
	private JTextArea textArea;

	/**
	 * Create the frame.
	 */
	public WriteMailInterface(MainInterface mainInterface) {
		this.mainInterface=mainInterface;
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
		//收件人
		receive = new JTextField();
		receive.setText("983763802@qq.com");
		receive.setBounds(79, 109, 336, 21);
		contentPane.add(receive);
		receive.setColumns(10);
//		主题
		mailTitle = new JTextField();
		mailTitle.setText("测试");
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
		//测试用，过后删掉
		textArea = new JTextArea();
		textArea.setText("这是一封测试邮件");
		scrollPane.setViewportView(textArea);
	}
	
	private List<String> getAddressList(JTextField field) {
		String all = field.getText();
		List<String> result = new ArrayList<String>();
		if (all.equals("")) return result; 
		for (String re : all.split(",")) {
			result.add(re);
		}
		return result;
	}
	
	//发送按钮
	private void send() {
		String xmlName = UUID.randomUUID().toString() + ".xml";
		Mail mail = new Mail(xmlName,this.mainInterface.getMailContext().getAccount(),
				getAddressList(this.receive),  this.mailTitle.getText(), 
				new Date(), "10",true, this.textArea.getText());
		sendMail.send(mail, this.mainInterface.getMailContext());
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

