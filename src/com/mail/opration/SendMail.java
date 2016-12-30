/** 
* @author yubo: 
* @version 创建时间：2016年12月6日 下午6:48:14 
* 类说明 
*/
package com.mail.opration;
//发送邮件类
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.mail.file.FileObject;

import com.mail.file.Mail;
import com.mail.main.MailContext;

public class SendMail{
	public Mail send(Mail mail, MailContext context) {
		try {
			Session session = context.getSession();
			Message message = new MimeMessage(session);
			//设置发件人地址
			Address from = new InternetAddress(context.getUser() + " <" + context.getAccount() + ">");
			message.setFrom(from);
			//设置所有收件人的地址
			Address[] to = getAddress(mail.getReceivers());
			message.setRecipients(Message.RecipientType.TO, to);
			//设置主题
			message.setSubject(mail.getSubject());
			//发送日期
			message.setSentDate(new Date());
			//构建整封邮件的容器
			Multipart main = new MimeMultipart();
			//正文的body
			BodyPart body = new MimeBodyPart();
			body.setContent(mail.getContent(), "text/html; charset=utf-8");
			main.addBodyPart(body);
			for (FileObject f : mail.getFiles()) {
				//每个附件的body
				MimeBodyPart fileBody = new MimeBodyPart();
				fileBody.attachFile(f.getFile());
				//为文件名进行转码
				fileBody.setFileName(MimeUtility.encodeText(f.getSourceName()));
				main.addBodyPart(fileBody);
			}
			//将正文的Multipart对象设入Message中
			message.setContent(main);
			Transport.send(message);
			return mail;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MailException(e.getMessage());
		}
		
	}
	//获得所有的收件人地址
	private Address[] getAddress(List<String> addList) throws Exception {
		Address[] result = new Address[addList.size()];
		for (int i = 0; i < addList.size(); i++) {
			if (addList.get(i) == null || "".equals(addList.get(i))) {
				continue;
			}
			result[i] = new InternetAddress(addList.get(i));
		}
		return result;
	}
}
