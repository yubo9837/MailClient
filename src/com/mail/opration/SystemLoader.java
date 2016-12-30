package com.mail.opration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


import com.mail.file.FileObject;
import com.mail.file.FileOp;
import com.mail.file.Mail;
import com.mail.main.MailContext;
import com.mail.opration.SystemLoader;

public class SystemLoader{
	public SystemLoader() {
		
	}
	//实现接口方法, 得到垃圾箱的邮件
	public List<Mail> getDeletedBoxMails(MailContext context) {
		//先从与用户对应的deleted目录中得到全部的xml文件
		return getMails(context, FileOp.DELETED);
	}

	//实现接口方法, 得到草稿箱的邮件
	public List<Mail> getDraftBoxMails(MailContext context) {
		//先从与用户对应的draft中得到全部的xml文件
		return getMails(context, FileOp.DRAFT);
	}

	public List<Mail> getInBoxMails(MailContext context) {
		return getMails(context, FileOp.INBOX);
	}
	
	//将xml文件转换成Mail对象, 并排序
	private List<Mail> convert(List<File> xmlFiles, MailContext context) {
		List<Mail> result = new ArrayList<Mail>();
		for (File file : xmlFiles) {
			//将xml转换成Mail对象
			Mail mail = FileOp.fromXML(context, file);
			result.add(mail);
		}
		return result;
	}

	//实现接口方法, 得到已发送的邮件
	public List<Mail> getSentBoxMails(MailContext context) {
		return getMails(context, FileOp.SENT);
	}

	private List<Mail> getMails(MailContext context, String box) {
		List<File> xmlFiles = FileOp.getXMLFiles(context, box);
		List<Mail> result = convert(xmlFiles, context);
		return result;
	}
}