package com.mail.login;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mail.main.MainInterface;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel userNameLabel;//提示文本
	private JButton confirmButton;//确认按钮
	private JTextField userTextField;//用户名输入框
	private MainInterface mainInterface;//主界面
	
	//启动界面
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginInterface frame = new LoginInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginInterface() {
		setTitle("登录");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 141);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//提示文本
		userNameLabel = new JLabel("用户名");
		userNameLabel.setBounds(24, 26, 54, 15);
		contentPane.add(userNameLabel);
		
		//用户名输入框
		userTextField = new JTextField();
		//添加按键事件，按enter==点击确认键
		userTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					confirm();
			}
		});
		userTextField.setBounds(88, 23, 200, 21);
		contentPane.add(userTextField);
		userTextField.setColumns(10);
		
		//确认按钮
		confirmButton = new JButton("确定");
		confirmButton.addMouseListener(new MouseAdapter() {
			//添加鼠标点击事件，点击鼠标时执行confirm()
			@Override
			public void mouseClicked(MouseEvent event) {
				//点击鼠标左键
				if(event.getButton()==MouseEvent.BUTTON1)
					confirm();
			}
		});
		confirmButton.setBounds(126, 69, 82, 23);
		contentPane.add(confirmButton);
	}
	
	private void confirm() {
		String user=this.userTextField.getText();
		if(user.trim().equals("")){
			//若输入为空，则弹出警告
			JOptionPane.showConfirmDialog(this, "请输入用户名","警告",
					JOptionPane.OK_CANCEL_OPTION);
			return;
		}
		//创建data文件夹
		File mainFolder = new File("data");
		if(!mainFolder.exists()){
			mainFolder.mkdir();
		}
		//创建user文件夹(若之前没有则创建)
		File folder = new File("data" + File.separator + user);
		if(!folder.exists()){
			folder.mkdir();
		}
		this.mainInterface=new MainInterface();
		this.mainInterface.setVisible(true);//显示主界面
		this.setVisible(false);//隐藏登录界面
	}
}

