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

	private JPanel contentPane;
	
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
		setTitle("\u767B\u5F55");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 141);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		userNameLabel = new JLabel("\u7528\u6237\u540D");
		userNameLabel.setBounds(24, 26, 54, 15);
		contentPane.add(userNameLabel);
		
		userTextField = new JTextField();
		userTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==e.VK_ENTER)
					confirm();
			}
		});
		userTextField.setBounds(88, 23, 200, 21);
		contentPane.add(userTextField);
		userTextField.setColumns(10);
		
		confirmButton = new JButton("\u786E\u5B9A");
		confirmButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getButton()==event.BUTTON1)
					confirm();
			}
		});
		confirmButton.setBounds(126, 69, 82, 23);
		contentPane.add(confirmButton);
	}
	
	private void confirm() {
		String user=this.userTextField.getText();
		if(user.trim().equals("")){
					JOptionPane.OK_CANCEL_OPTION);
			return;
		}
		File mainFolder = new File("data");
		if(!mainFolder.exists()){
			mainFolder.mkdir();
		}
		File folder = new File("data" + File.separator + user);
		if(!folder.exists()){
			folder.mkdir();
		}
		this.mainInterface=new MainInterface();
	}
}

