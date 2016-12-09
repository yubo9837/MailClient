package com.mail.main;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

public class MailContext {
	private String user;//用户名
	private String account;//邮箱名
	private String password;//密码
	private String smtpServer;//SMTP服务器
	private int smtpPort;//SMTP端口
	private String pop3Server;//pop3服务器
	private int pop3Port;//pop3端口
	private boolean reset=false;//
	
	public MailContext(String user,String account,String password,String smtpServer,
			int smtpPort,String pop3Server,int pop3Port) {
		super();
		this.user=user;
		this.account = account;
		this.password = password;
		this.smtpServer = smtpServer;
		this.smtpPort = smtpPort;
		this.pop3Server = pop3Server;
		this.pop3Port = pop3Port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getPop3Server() {
		return pop3Server;
	}

	public void setPop3Server(String pop3Server) {
		this.pop3Server = pop3Server;
	}

	public int getPop3Port() {
		return pop3Port;
	}

	public void setPop3Port(int pop3Port) {
		this.pop3Port = pop3Port;
	}
	
	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	private Store store;
	//接收
	public Store getStore() {
		//重置了信息, 设置session为null
		if (this.reset) {
			this.store = null;
			this.session = null;
			this.reset = false;
		}
		if (this.store == null || !this.store.isConnected()) {
			try {
				Properties props = System.getProperties();
				//创建mail的Session
				Session session = Session.getDefaultInstance(props, getAuthenticator());
				//使用pop3协议接收邮件
				URLName url = new URLName("pop3", getPop3Server(), getPop3Port(), null,   
						getAccount(), getPassword());
				//得到邮箱的存储对象
				Store store = session.getStore(url);
				store.connect();
				this.store = store;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("连接出错");
//				throw new MailConnectionException("连接邮箱异常，请检查配置");
			}
		}
		return this.store;
	}
	
	private Session session;
	//发送
	public Session getSession() {
		//重置了信息, 设置session为null
		if (this.reset) {
			this.session = null;
			this.store = null;
			this.reset = false;
		}
		if (this.session == null) {
			Properties props = System.getProperties();
			System.out.println(this.getSmtpPort());
			props.put("mail.smtp.host", this.getSmtpServer());  
			props.put("mail.smtp.port", this.getSmtpPort());
			props.put("mail.smtp.auth", true);
			Session sendMailSession = Session.getDefaultInstance(props, getAuthenticator());
			this.session = sendMailSession;
		}
		return this.session;
	}
	private Authenticator getAuthenticator() {
		return new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getAccount(), getPassword());
			}
		};
	}
	
}
