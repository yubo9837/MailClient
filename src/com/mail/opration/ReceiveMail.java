package com.mail.opration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

import com.mail.file.Mail;
import com.mail.main.MailContext;

public class ReceiveMail{
	public ReceiveMail() {
		
	}
	public List<Mail> getMessages(MailContext context) {
		Folder inbox=getINBOXFolder(context);
		try {
			inbox.open(Folder.READ_WRITE);
			Message[] messages=inbox.getMessages();
			List<Mail> result=getMailList(context, messages);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 得到邮箱INBOX
	 */
	private Folder getINBOXFolder(MailContext context) {
		Store store = context.getStore();
		try {
			return store.getFolder("INBOX");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//将javamail中的Message对象转换成本项目中的Mail对象
	private List<Mail> getMailList(MailContext context, Message[] messages) {
		List<Mail> result = new ArrayList<Mail>();
		try {
			//将得到的Message对象封装成Mail对象
			for (Message m : messages) {
				//生成UUID的文件名
				String xmlName = UUID.randomUUID().toString() + ".xml"; 
				//获得内容
				String content = getContent(m, new StringBuffer()).toString();
				//得到邮件的各个值
				Mail mail = new Mail(xmlName,getSender(m), getAllRecipients(m), 
						m.getSubject(), getReceivedDate(m), Mail.getSize(m.getSize()), hasRead(m), 
						content);
				result.add(mail);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
		
	//得到接收的日期, 优先返回发送日期, 其次返回收信日期
	private Date getReceivedDate(Message m) throws Exception {
		if (m.getSentDate() != null) return m.getSentDate();
		if (m.getReceivedDate() != null) return m.getReceivedDate();
		return new Date();
	}
		
	//返回邮件正文
	private StringBuffer getContent(Part part, StringBuffer result) throws Exception {
		if (part.isMimeType("multipart/*")) {
			Multipart p = (Multipart)part.getContent();
			int count = p.getCount();
			//Multipart的第一部分是text/plain, 第二部分是text/html的格式, 只需要解析第一部分即可
			if (count > 1) count = 1; 
			for(int i = 0; i < count; i++) {
				BodyPart bp = p.getBodyPart(i);
				//递归调用
				getContent(bp, result);
			}
		} else if (part.isMimeType("text/*")) {
			//遇到文本格式或者html格式, 直接得到内容
			result.append(part.getContent());
		}
		return result;
	}	
		
	//判断一封邮件是否已读, true表示已读取, false表示没有读取
	private boolean hasRead(Message m) throws Exception {
		Flags flags = m.getFlags();
		if (flags.contains(Flags.Flag.SEEN)) return true;
		return false;
	}
		
	//得到一封邮件的所有收件人
	private List<String> getAllRecipients(Message m) throws Exception {
		Address[] addresses = m.getAllRecipients();
		return getAddresses(addresses);
	}
		
	//工具方法, 将参数的地址字符串封装成集合
	private List<String> getAddresses(Address[] addresses) {
		List<String> result = new ArrayList<String>();
		if (addresses == null) return result;
		for (Address a : addresses) {
			result.add(a.toString());
		}
		return result;
	}
		
	//得到发送人的地址
	private String getSender(Message m) throws Exception  {
		Address[] addresses = m.getFrom();
		return MimeUtility.decodeText(addresses[0].toString());
	}
		
	//将邮件数组设置为删除状态
	private void deleteFromServer(Message[] messages) throws Exception {
		for (Message m : messages) {
			m.setFlag(Flags.Flag.DELETED, true);
		}
	}
}
