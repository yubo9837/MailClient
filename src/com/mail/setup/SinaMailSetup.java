package com.mail.setup;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mail.file.FileOp;
import com.mail.file.PropertiesFile;
import com.mail.main.MailContext;
import com.mail.main.MainInterface;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SinaMailSetup extends JFrame {

	private JPanel contentPane;
//	邮箱名
	private JTextField sinaName;
//	密码
	private JPasswordField sinaPasswd;
	
	private MainInterface mainInterface;

	/**
	 * Create the frame.
	 */
	public SinaMailSetup(MainInterface mainInterface) {
		setResizable(false);
		setTitle("新浪邮箱");
		this.mainInterface=mainInterface;
		init(this.mainInterface.getContext());
	}
	private void init(MailContext context) {
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		sinaName = new JTextField();
		sinaName.setBounds(119, 69, 147, 21);
		contentPane.add(sinaName);
		sinaName.setColumns(10);
		
		JLabel lblqqcom = new JLabel("@sina.com");
		lblqqcom.setBounds(276, 72, 102, 15);
		contentPane.add(lblqqcom);
		
		JLabel lblSina = new JLabel("新浪邮箱名：");
		lblSina.setBounds(31, 72, 78, 15);
		contentPane.add(lblSina);
		
		JLabel label = new JLabel("密码：");
		label.setBounds(31, 117, 54, 15);
		contentPane.add(label);
		
		sinaPasswd = new JPasswordField();
		sinaPasswd.setBounds(119, 114, 147, 21);
		contentPane.add(sinaPasswd);
//		确定按键
		JButton confirmButtom = new JButton("确定");
		confirmButtom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getButton()==MouseEvent.BUTTON1)
					confirm();
			}
		});
		confirmButtom.setBounds(31, 185, 93, 23);
		contentPane.add(confirmButtom);
//		取消按键
		JButton cancelButton = new JButton("取消");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getButton()==MouseEvent.BUTTON1)
					hideFrame();
			}
		});
		cancelButton.setBounds(173, 185, 93, 23);
		contentPane.add(cancelButton);
	}
//	点击确定，保存邮箱配置信息
	private void  confirm() {
		MailContext context=getMailContext(this.mainInterface.getContext());
		context.setReset(true);
		PropertiesFile.store(context);
		this.mainInterface.setContext(context);
		FileOp.createFolder(context);
		this.setVisible(false);
	}
	
	private void  hideFrame() {
		this.setVisible(false);
	}
	private MailContext getMailContext(MailContext context) {
//		String account=this.sinaName.getText()+"@sina.com";
		String account="iyuboi@sina.com";//测试用
//		String password=this.getPassword();
		String password="19960922Yb";
		context.setAccount(account);
		context.setPassword(password);
		context.setSmtpServer("smtp.sina.com");
		context.setSmtpPort(25);
		context.setPop3Server("pop.sina.com");
		context.setPop3Port(110);
		//由于重新设置了连接信息, 因此设置MailContext的reset值为true
		context.setReset(true);
		
		return context;
		
	}
//	将密码转化为String型
	private String getPassword() {
		char[] passes = this.sinaPasswd.getPassword();
		StringBuffer password = new StringBuffer();
		for (char c : passes) {
			password.append(c);
		}
		return password.toString();
	}
}
