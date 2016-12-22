package com.mail.main;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.mail.file.FileObject;
import com.mail.file.FileOp;
import com.mail.file.Mail;
import com.mail.opration.SendMail;
import com.mail.system.SystemHandler;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class WriteMailInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField receive;
	private JTextField mailTitle;
	
	private MainInterface mainInterface;
	private SendMail sendMail= new SendMail();
	
	SystemHandler systemHandler=new SystemHandler();

	//发送
	private Action send = new AbstractAction("发送") {
		public void actionPerformed(ActionEvent e) {
			send();
		}
	};
	
	//保存至草稿箱
	private Action saveToDraft = new AbstractAction("保存至草稿箱") {
		public void actionPerformed(ActionEvent e) {
			saveToDraft();
		}
	};
	//上传附件
	private Action uploadFile = new AbstractAction("上传附件") {
		public void actionPerformed(ActionEvent e) {
			uploadFile();
		}
	};
	//删除附件
	private Action deleteFile = new AbstractAction("删除附件") {
		public void actionPerformed(ActionEvent e) {
			deleteFile();
		}
	};
	private JTextArea textArea;
	private JList fileList;

	/**
	 * Create the frame.
	 */
	public WriteMailInterface(MainInterface mainInterface) {
		setResizable(false);
		this.mainInterface=mainInterface;
		setTitle("写邮件");
		init();
	}
	
//	初始化界面
	private void init() {
		setBounds(100, 100, 773, 490);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelReceive = new JLabel("收件人：");
		labelReceive.setBounds(28, 112, 54, 15);
		contentPane.add(labelReceive);
		
		JLabel labelTitle = new JLabel("主题：");
		labelTitle.setBounds(28, 149, 54, 15);
		contentPane.add(labelTitle);
		//收件人
		receive = new JTextField();
		receive.setText("983763802@qq.com");
		receive.setBounds(79, 109, 336, 21);
		contentPane.add(receive);
		receive.setColumns(10);
//		主题
		mailTitle = new JTextField();
		mailTitle.setText("测试");
		mailTitle.setBounds(79, 146, 336, 21);
		contentPane.add(mailTitle);
		mailTitle.setColumns(10);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBounds(28, 10, 542, 74);
		contentPane.add(toolBar);
		toolBar.add(this.send);
		toolBar.add(this.saveToDraft);
		toolBar.add(this.uploadFile);
		toolBar.add(this.deleteFile);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(28, 193, 696, 248);
		contentPane.add(splitPane);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		//测试用，过后删掉
		textArea = new JTextArea();
		textArea.setText("这是一封测试邮件");
		scrollPane.setViewportView(textArea);
		fileList=new JList();
//		fileList.addMouseListener(new SendListMouseListener());
		JScrollPane fileScrollPane = new JScrollPane(fileList);
		splitPane.setLeftComponent(fileScrollPane);
	}
	
	private List<String> getAddressList(JTextField field) {
		String all = field.getText();
		List<String> result = new ArrayList<String>();
		if (all.equals("")) return result; 
		for (String re : all.split(",")) {
			result.add(re);
		}
		return result;
	}
	
	//将界面的组件封装成一个Mail对象
	private Mail getMail(String fromBox) {
		String xmlName = UUID.randomUUID().toString() + ".xml";
		Mail mail = new Mail(xmlName,this.mainInterface.getMailContext().getAccount(),
				getAddressList(this.receive),  this.mailTitle.getText(), 
				new Date(), "10",this.textArea.getText(), fromBox);
		mail.setFiles(getFileListObjects());
		return mail;
	}
	//发送按钮
	private void send() {
//		String xmlName = UUID.randomUUID().toString() + ".xml";
		Mail mail = getMail(FileOp.SENT);
		this.sendMail.send(mail, this.mainInterface.getMailContext());
		this.systemHandler.saveSent(mail, this.mainInterface.getMailContext());
		this.mainInterface.addSentMail(mail);
//		clean();
//		this.setVisible(false);
	}

	//清空界面各个元素
	private void clean() {
		this.receive.setText("");
		this.mailTitle.setText("");
		this.textArea.setText("");
		this.fileList.setListData(new Object[]{});
	}
	
//保存到草稿箱
	private void saveToDraft() {
		//得到界面中的Mail, 该对象的位置在草稿箱
		Mail mail = getMail(FileOp.DRAFT);
		this.systemHandler.saveDraftBox(mail, this.mainInterface.getMailContext());
		//添加到mainInterface的草稿箱集合中
		this.mainInterface.addDraftMail(mail);
	}
//上传附件
	private void uploadFile() {
		FileChooser chooser = new FileChooser(this);
		chooser.showOpenDialog(this);
	}
//删除附件
	private void deleteFile() {
		FileObject file = (FileObject)this.fileList.getSelectedValue();
		if (file == null) {
			return;
		}
		List<FileObject> files = getFileListObjects();
		files.remove(file);
		this.fileList.setListData(files.toArray());
	}
	
	//回复邮件初始化界面组件
	public void replyInit(Mail mail) {
		this.setVisible(true);
//		this.fileList.setListData(mail.getFiles().toArray());
		this.receive.setText(mail.getSender());
		this.mailTitle.setText("回复: " + mail.getSubject());
		this.textArea.setText(mail.getContent());
	}
	
	public void transmitInit(Mail mail) {
		this.setVisible(true);
//		this.fileOp.setListData(mail.getFiles().toArray());
		this.receive.setText(mail.getSender());
		this.mailTitle.setText(mail.getSubject());
		this.textArea.setText(mail.getContent());
	}
	
	//从JList中得到附件的对象集合
	public List<FileObject> getFileListObjects() {
		ListModel model = this.fileList.getModel();
		List<FileObject> files = new ArrayList<FileObject>();
		for (int i = 0; i < model.getSize(); i++) {
			files.add((FileObject)model.getElementAt(i));
		}
		return files;
	}
	
	public JList getFileList() {
		return this.fileList;
	}
}

class FileChooser extends JFileChooser {
	
	WriteMailInterface writeMailInterface;
	
	public FileChooser(WriteMailInterface writeMailInterface) {
		this.writeMailInterface = writeMailInterface;
		this.setFileSelectionMode(FILES_ONLY); 
	}

	@Override
	public void approveSelection() {
		File file = this.getSelectedFile();
		FileObject fileObject = new FileObject(file.getName(), file);
		List<FileObject> files = this.writeMailInterface.getFileListObjects();
		files.add(fileObject);
		writeMailInterface.getFileList().setListData(files.toArray());
		super.approveSelection();
	}
	
	
}

