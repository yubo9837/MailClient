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

public class QQMailSetup extends JFrame {

	private JPanel contentPane;
	private JTextField qqName;
	private JPasswordField qqPasswd;
	
	private MainInterface mainInterface;

	/**
	 * Create the frame.
	 */
	public QQMailSetup(MainInterface mainInterface) {
		this.mainInterface=mainInterface;
		init(this.mainInterface.getContext());
	}
	private void init(MailContext context) {
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		qqName = new JTextField();
		qqName.setBounds(119, 69, 147, 21);
		contentPane.add(qqName);
		qqName.setColumns(10);
		
		JLabel lblqqcom = new JLabel("@qq.com");
		lblqqcom.setBounds(276, 72, 54, 15);
		contentPane.add(lblqqcom);
		
		JLabel lblQq = new JLabel("QQ邮箱名：");
		lblQq.setBounds(31, 72, 78, 15);
		contentPane.add(lblQq);
		
		JLabel label = new JLabel("密码：");
		label.setBounds(31, 117, 54, 15);
		contentPane.add(label);
		
		qqPasswd = new JPasswordField();
		qqPasswd.setBounds(119, 114, 147, 21);
		contentPane.add(qqPasswd);
		
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
		String account=this.qqName.getText()+"@qq.com";
		String password=this.getPassword();
		context.setAccount(account);
		context.setPassword(password);
		context.setSmtpServer("smtp.qq.com");
		context.setSmtpPort(33);
		context.setPop3Server("pop3.qq.com");
		context.setPop3Port(44);
		//由于重新设置了连接信息, 因此设置MailContext的reset值为true
		context.setReset(true);
		
		return context;
		
	}
	
	private String getPassword() {
		char[] passes = this.qqPasswd.getPassword();
		StringBuffer password = new StringBuffer();
		for (char c : passes) {
			password.append(c);
		}
		return password.toString();
	}
}
