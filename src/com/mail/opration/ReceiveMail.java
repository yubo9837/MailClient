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

import com.mail.file.FileObject;
import com.mail.file.FileOp;
import com.mail.file.Mail;
import com.mail.main.MailContext;

public class ReceiveMail {
	
	public List<Mail> getMessages(MailContext context) {
		Folder inbox=getINBOXFolder(context);
		try {
			inbox.open(Folder.READ_WRITE);
			Message[] messages=inbox.getMessages();
			List<Mail> result=getMailList(context, messages);
			inbox.close(true);
			return result;
		} catch (Exception e) {
			throw new MailException(e.getMessage());
		}
	}
	
	/*
	 * 得到邮箱INBOX
	 */
	public Folder getINBOXFolder(MailContext context) {
		Store store = context.getStore();
		try {
			return store.getFolder("INBOX");
		} catch (MessagingException e) {
			throw new MailException(e.getMessage());
		}
	}
	
	//获得邮件的附件
	private List<FileObject> getFiles(MailContext context, Message m) throws Exception {
		List<FileObject> files = new ArrayList<FileObject>();
		//是混合类型, 就进行处理
		if (m.isMimeType("multipart/mixed")) {
			Multipart mp = (Multipart)m.getContent();
			//得到邮件内容的Multipart对象并得到内容中Part的数量
			int count = mp.getCount();
			for (int i = 1; i < count; i++) {
				Part part = mp.getBodyPart(i);
				//在本地创建文件并添加到结果中
				files.add(FileOp.createFileFromPart(context, part));
			}
		}
		return files;
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
						m.getSubject(), getReceivedDate(m), Mail.getSize(m.getSize()),  
						content,FileOp.INBOX);
				mail.setFiles(getFiles(context, m));
				result.add(mail);
			}
			return result;
		} catch (Exception e) {
			throw new MailException(e.getMessage());
		}
		
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
