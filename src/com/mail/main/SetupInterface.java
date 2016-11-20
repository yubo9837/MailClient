package com.mail.main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mail.file.FileOp;
import com.mail.file.PropertiesFile;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SetupInterface extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextField textf_mailName;//邮箱名
	private JTextField textf_SMTP;//SMTP服务器
	private JTextField textf_POP3;//pop3服务器
	private JTextField textf_SMTPport;//SMTP端口
	private JTextField textf_POPport;//pop3端口
	private JPasswordField passwordField;//密码
	
	//提示文本
	private JLabel lbl_mailName;
	private JLabel lbl_passwd;
	private JLabel lbl_smtp;
	private JLabel lbl_pop;
	private JLabel lbl_portSMTP;
	private JLabel lbl_portPOP;
	
	private JButton btn_yes;//确定按钮
	private JButton btn_no;//取消按钮
	
	private MainInterface mainInterface;

	/**
	 * Create the frame.
	 */
	public SetupInterface(MainInterface mainInterface) {
		this.mainInterface=mainInterface;
		init(this.mainInterface.getContext());
	}
	
	private void init(MailContext context) {
		setTitle("设置账号");
		setBounds(100, 100, 529, 213);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lbl_mailName = new JLabel("邮箱名：");
		lbl_mailName.setBounds(31, 29, 54, 15);
		contentPane.add(lbl_mailName);
		
		lbl_passwd = new JLabel("密码：");
		lbl_passwd.setBounds(31, 54, 54, 15);
		contentPane.add(lbl_passwd);
		
		lbl_smtp = new JLabel("发送邮件服务器（SMTP）：");
		lbl_smtp.setBounds(31, 79, 179, 15);
		contentPane.add(lbl_smtp);
		
		lbl_pop = new JLabel("接收邮件服务器（POP3）：");
		lbl_pop.setBounds(31, 104, 179, 15);
		contentPane.add(lbl_pop);
		
		btn_yes = new JButton("确定");
		btn_yes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getButton()==MouseEvent.BUTTON1)
					confirm();
			}
		});
		btn_yes.setBounds(117, 141, 93, 23);
		contentPane.add(btn_yes);
		
		btn_no = new JButton("取消");
		btn_no.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getButton()==MouseEvent.BUTTON1)
					hideSetup();
			}
		});
		btn_no.setBounds(309, 141, 93, 23);
		contentPane.add(btn_no);
		
		textf_mailName = new JTextField();
		textf_mailName.setBounds(95, 26, 172, 21);
		contentPane.add(textf_mailName);
		textf_mailName.setColumns(10);
		
		lbl_portSMTP = new JLabel("端口：");
		lbl_portSMTP.setBounds(348, 79, 54, 15);
		contentPane.add(lbl_portSMTP);
		
		lbl_portPOP = new JLabel("端口：");
		lbl_portPOP.setBounds(348, 104, 54, 15);
		contentPane.add(lbl_portPOP);
		
		textf_SMTP = new JTextField();
		textf_SMTP.setBounds(199, 76, 130, 21);
		contentPane.add(textf_SMTP);
		textf_SMTP.setColumns(10);
		
		textf_POP3 = new JTextField();
		textf_POP3.setBounds(199, 101, 130, 21);
		contentPane.add(textf_POP3);
		textf_POP3.setColumns(10);
		
		textf_SMTPport = new JTextField();
		textf_SMTPport.setText("25");
		textf_SMTPport.setBounds(410, 76, 66, 21);
		contentPane.add(textf_SMTPport);
		textf_SMTPport.setColumns(10);
		
		textf_POPport = new JTextField();
		textf_POPport.setText("110");
		textf_POPport.setBounds(410, 101, 66, 21);
		contentPane.add(textf_POPport);
		textf_POPport.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(95, 51, 172, 21);
		contentPane.add(passwordField);
		
		//初始化各个配置
		//如果之前配置过账号信息，则直接读出来
		this.textf_mailName.setText(context.getAccount());
		this.passwordField.setText(context.getPassword());
		this.textf_SMTP.setText(context.getSmtpServer());
		this.textf_POP3.setText(context.getPop3Server());
		this.textf_SMTPport.setText(String.valueOf(context.getSmtpPort()));
		this.textf_POPport.setText(String.valueOf(context.getPop3Port()));
	}
	
	private void  confirm() {
		MailContext context=getMailContext(this.mainInterface.getContext());
		context.setReset(true);
		PropertiesFile.store(context);
		this.mainInterface.setContext(context);
		FileOp.createFolder(context);
		this.setVisible(false);
	}
	//关闭设置界面
	private void hideSetup() {
		this.setVisible(false);
	}
	
	private String getPassword() {
		char[] passes = this.passwordField.getPassword();
		StringBuffer password = new StringBuffer();
		for (char c : passes) {
			password.append(c);
		}
		return password.toString();
	}
	
	private MailContext getMailContext(MailContext context) {
		String account=this.textf_mailName.getText();
		String password=this.getPassword();
		//得到密码框内的字符
		String smtpServer = this.textf_SMTP.getText();
		String smtpPortS = this.textf_SMTPport.getText();
		String pop3Server = this.textf_POP3.getText();
		String pop3PortS = this.textf_POPport.getText();
		String[] values = new String[]{account, password, smtpServer, smtpPortS, 
				pop3Server, pop3Server, pop3PortS};
//		validateRequire(values);
		//验证端口数字
//		validateLegal(new String[]{smtpPortS, pop3PortS});
		//强制转化为int
		Integer smtpPort = Integer.valueOf(smtpPortS);
		Integer pop3Port = Integer.valueOf(pop3PortS);
		context.setAccount(account);
		context.setPassword(password);
		context.setSmtpServer(smtpServer);
		context.setSmtpPort(smtpPort);
		context.setPop3Server(pop3Server);
		context.setPop3Port(pop3Port);
		//由于重新设置了连接信息, 因此设置MailContext的reset值为true
		context.setReset(true);
		
		return context;
		
	}
	
	private void validateLegal(String[] values) {
		try {
			for (String s : values) {
				Integer.valueOf(s);
			}
		} catch (NumberFormatException e) {
		}
	}
	
	/*
	 * 验证必填输入
	 */
	private void validateRequire(String[] values) {
		for (String s :values) {
			if (s.trim().equals("")) {
			}
		}
	}
}
