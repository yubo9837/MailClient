package com.mail.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.mail.Part;
import javax.mail.internet.MimeUtility;

import com.mail.main.MailContext;
import com.mail.opration.MailEcp;
import com.thoughtworks.xstream.XStream;

public class FileOp {
	
	private static XStream xStream=new XStream();

	//存放所有用户数据的目录
	public static final String DATE_FOLDER = "data" + File.separator;
	//存放具体某个用户配置的properties文件
	public static final String CONFIG_FILE = File.separator + "mail.properties";
	//收件箱的目录名
	public static final String INBOX = "inbox";
	//已发送的目录名
	public static final String SENT = "sent";
	//草稿箱的目录名
	public static final String DRAFT = "draft";
	//垃圾箱的目录名
	public static final String DELETED = "deleted";
	//附件的存放目录名
	public static final String FILE = "file";
	
	/**
	 * 创建用户的帐号目录和相关的子目录
	 */
	public static void createFolder(MailContext context) {
		String accountRoot = getAccountRoot(context);
		//使用用户当前设置的帐号来生成目录, 例如一个用户叫user1,那么将会在data/user1/下生成一个帐号目录
		mkdir(new File(accountRoot));
		//创建INBOX目录
		mkdir(new File(accountRoot + INBOX));
		//已发送
		mkdir(new File(accountRoot + SENT));
		//草稿箱
		mkdir(new File(accountRoot + DRAFT));
		//垃圾箱
		mkdir(new File(accountRoot + DELETED));
		//附件存放目录
		mkdir(new File(accountRoot + FILE));
	}
	
	//得到邮件帐号的根目录
	private static String getAccountRoot(MailContext context) {
		String accountRoot = DATE_FOLDER + context.getUser() + 
				File.separator + context.getAccount() + File.separator;
		return accountRoot;
	}
	
	//得到某个目录名字, 例如得到file的目录, inbox的目录
	public static String getBoxPath(MailContext context, String folderName) {
		return getAccountRoot(context) + folderName + File.separator;
	}
	
	//复制文件的方法
	public static void copy(File sourceFile, File targetFile) {
		try {
			Process process = Runtime.getRuntime().exec("cmd /c copy \"" + 
					sourceFile.getAbsolutePath() + "\" \"" + 
					targetFile.getAbsolutePath() + "\"");
			process.waitFor();
		} catch (Exception e) {
		}
	}

	/*
	 * 创建目录的工具方法, 判断目录是否存在
	 */
	private static void mkdir(File file) {
		if (!file.exists()) file.mkdir();
	}
	
	public static List<File> getXMLFiles(MailContext context, String box) {
		String rootPath = getAccountRoot(context);
		String boxPath = rootPath + box;
		//得到某个box的目录
		File boxFolder = new File(boxPath);
		//对文件进行后缀过滤
		List<File> files = filterFiles(boxFolder, ".xml");
		return files;
	}
	
	//从一个文件目录中, 以参数文件后缀subfix为条件, 过滤文件
	private static List<File> filterFiles(File folder, String sufix) {
		List<File> result = new ArrayList<File>();
		File[] files = folder.listFiles();
		if (files == null) return new ArrayList<File>();
		for (File f : files) {
			if (f.getName().endsWith(sufix)) result.add(f);
		}
		return result;
	}
	
	//得到文件名的后缀
	public static String getFileSufix(String fileName) {
		if (fileName == null || fileName.trim().equals("")) return "";
		if (fileName.indexOf(".") != -1) {
			return fileName.substring(fileName.indexOf("."));
		}
		return "";
	}
	
	//将一个邮件对象使用XStream写到xml文件中
	public static void writeToXML(MailContext context, Mail mail, String boxFolder) {
		//得到mail对应的xml文件的文件名
		String xmlName = mail.getXmlName();
		//得到对应的目录路径
		String boxPath = getAccountRoot(context) + boxFolder + File.separator;
		File xmlFile = new File(boxPath + xmlName);
		writeToXML(xmlFile, mail);
	}
	
	//将一个mail对象写到xmlFile中
	public static void writeToXML(File xmlFile, Mail mail) {
		try {
			if (!xmlFile.exists()) xmlFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(xmlFile);
			OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF8");
			xStream.toXML(mail, writer);
			writer.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//将一份xml文档转换成Mail对象
	public static Mail fromXML(MailContext context, File xmlFile) {
		try {
			FileInputStream fis = new FileInputStream(xmlFile);
			//调用XStream的转换方法将文件转换成对象
			Mail mail = (Mail)xStream.fromXML(fis);
			fis.close();
			return mail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//为附件创建本地文件, 目录是登录用户的邮箱名的file下
	public static FileObject createFileFromPart(MailContext context, Part part) {
		try {
			//得到文件存放的目录
			String fileRepository = getBoxPath(context, FILE);
			String serverFileName = MimeUtility.decodeText(part.getFileName());
			//生成UUID作为在本地系统中唯一的文件标识
			String fileName = UUID.randomUUID().toString();
			File file = new File(fileRepository + fileName + 
					getFileSufix(serverFileName));
			//读写文件
			FileOutputStream fos = new FileOutputStream(file);
			InputStream is = part.getInputStream();
			BufferedOutputStream outs = new BufferedOutputStream(fos);
			//如果附件内容为空part.getSize为-1, 如果直接new byte, 将抛出异常
			int size = (part.getSize() > 0) ? part.getSize() : 0;
			byte[] b = new byte[size];
			is.read(b);
			outs.write(b);
			outs.close();
			is.close();
			fos.close();
			//封装对象
			FileObject fileObject = new FileObject(serverFileName, file);
			return fileObject;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MailEcp(e.getMessage());
		}
	}
}
