package com.mail.file;

import java.io.File;

import com.mail.main.MailContext;

public class FileOp {

	//存放所有用户数据的目录
	public static final String DATE_FOLDER = "datas" + File.separator;
	//存放具体某个用户配置的properties文件
	public static final String CONFIG_FILE = File.separator + "mail.properties";
	
	//收件箱的目录名
	public static final String INBOX = "inbox";
	//发件箱的目录名
	public static final String OUTBOX = "outbox";
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
	 * @param ctx
	 */
	public static void createFolder(MailContext context) {
		String accountRoot = getAccountRoot(context);
		//使用用户当前设置的帐号来生成目录, 例如一个用户叫user1,那么将会在datas/user1/下生成一个帐号目录
		mkdir(new File(accountRoot));
		//创建INBOX目录
		mkdir(new File(accountRoot + INBOX));
		//发件箱
		mkdir(new File(accountRoot + OUTBOX));
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
	private static String getAccountRoot(MailContext ctx) {
		String accountRoot = DATE_FOLDER + ctx.getUser() + 
		File.separator + ctx.getAccount() + File.separator;
		return accountRoot;
	}
	
	//得到某个目录名字, 例如得到file的目录, inbox的目录
	public static String getBoxPath(MailContext ctx, String folderName) {
		return getAccountRoot(ctx) + folderName + File.separator;
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
}
