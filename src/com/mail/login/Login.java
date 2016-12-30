package com.mail.login;

import java.io.File;

import com.mail.file.FileOp;
import com.mail.file.PropertiesFile;
import com.mail.main.MailContext;
import com.mail.main.MainInterface;

public class Login{

	//启动界面   
	public static void main(String[] args) {
		String user="admin";
		//创建data文件夹
		File mainFolder = new File("data");
		if(mainFolder.exists()){
			deleteFile(mainFolder);
		}
		mainFolder.mkdir();
		//创建user文件夹(若之前没有则创建)
		File folder = new File("data" + File.separator + user);
		if(!folder.exists()){
			folder.mkdir();
		}
		
		File config=new File(folder.getAbsolutePath()+FileOp.CONFIG_FILE);
		try{
			if(!config.exists()){
				config.createNewFile();
			}
			MailContext mailContext=PropertiesFile.createContext(config);
			mailContext.setUser(user);
			MainInterface mainInterface=new MainInterface(mailContext);
			mainInterface.setVisible(true);//显示主界面
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
//	删除文件夹的所有内容
	private static void deleteFile(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++){
				deleteFile(files[i]);
			}
		}
		file.delete();
	}
}

