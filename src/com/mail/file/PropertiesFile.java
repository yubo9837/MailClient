package com.mail.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.mail.main.MailContext;

public class PropertiesFile {
	
	private static Properties getProperties(File propertiesFile) throws IOException {
		Properties properties=new Properties();
		FileInputStream fileInputStream=new FileInputStream(propertiesFile);
		properties.load(fileInputStream);
		return properties;
	}
	
	public static MailContext createContext(File propertiesFile) throws IOException {
		Properties properties = getProperties(propertiesFile);
		int smtpPort=getInt(properties.getProperty("smtpPort"), 25);
		int pop3Port=getInt(properties.getProperty("pop3Port"), 110);
		
		
		return new MailContext(null, 
				properties.getProperty("account"), 
				properties.getProperty("password"),
				properties.getProperty("smtpServer"),smtpPort,
				properties.getProperty("pop3Server"),pop3Port);
	}
	
	private static Integer getInt(String s, int defaultNum) {
		if (s == null || s.trim().equals("")) {
			return defaultNum;
		}
		return Integer.parseInt(s);
	}
	
	public static void store(MailContext context) {
		try {
			File propFile = new File(FileOp.DATE_FOLDER + context.getUser() + FileOp.CONFIG_FILE);
			Properties prop = getProperties(propFile);
			prop.setProperty("account", context.getAccount());
			prop.setProperty("password", context.getPassword());
			prop.setProperty("smtpHost", context.getSmtpServer());
			prop.setProperty("smtpPort", String.valueOf(context.getSmtpPort()));
			prop.setProperty("pop3Host", context.getPop3Server());
			prop.setProperty("pop3Port", String.valueOf(context.getPop3Port()));
			FileOutputStream fos = new FileOutputStream(propFile);
			prop.store(fos, "These are mail configs.");
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
