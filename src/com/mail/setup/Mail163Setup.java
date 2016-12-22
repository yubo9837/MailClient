package com.mail.setup;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.mail.file.FileOp;
import com.mail.file.PropertiesFile;
import com.mail.main.MailContext;
import com.mail.main.MainInterface;

public class Mail163Setup extends JFrame {

	private JPanel contentPane;
	private JTextField mail163Name;
	private JPasswordField mail163Passwd;
	
	private MainInterface mainInterface;

	/**
	 * Create the frame.
	 */
	public Mail163Setup(MainInterface mainInterface) {
		setTitle("163邮箱");
		setResizable(false);
		this.mainInterface=mainInterface;
		init(this.mainInterface.getContext());
	}
	private void init(MailContext context) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		mail163Name = new JTextField();
		mail163Name.setBounds(119, 69, 147, 21);
		contentPane.add(mail163Name);
		mail163Name.setColumns(10);
		
		JLabel lblqqcom = new JLabel("@163.com");
		lblqqcom.setBounds(276, 72, 93, 15);
		contentPane.add(lblqqcom);
		
		JLabel lblQq = new JLabel("163邮箱名：");
		lblQq.setBounds(31, 72, 78, 15);
		contentPane.add(lblQq);
		
		JLabel label = new JLabel("密码：");
		label.setBounds(31, 117, 54, 15);
		contentPane.add(label);
		
		mail163Passwd = new JPasswordField();
		mail163Passwd.setBounds(119, 114, 147, 21);
		contentPane.add(mail163Passwd);
		
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
	
	private void  confirm() {
		MailContext context=getMailContext(this.mainInterface.getContext());
		context.setReset(true);
		PropertiesFile.store(context);
		this.mainInterface.setContext(context);
		FileOp.createFolder(context);
		this.setVisible(false);
	}
	
	private void  hideFrame() {
		
	}
	private MailContext getMailContext(MailContext context) {
		String account=this.mail163Name.getText()+"@163.com";
		String password=this.getPassword();
		context.setAccount(account);
		context.setPassword(password);
		context.setSmtpServer("smtp.163.com");
		context.setSmtpPort(25);
		context.setPop3Server("pop3.163.com");
		context.setPop3Port(110);
		//由于重新设置了连接信息, 因此设置MailContext的reset值为true
		context.setReset(true);
		
		return context;
		
	}
	
	private String getPassword() {
		char[] passes = this.mail163Passwd.getPassword();
		StringBuffer password = new StringBuffer();
		for (char c : passes) {
			password.append(c);
		}
		return password.toString();
	}
}
