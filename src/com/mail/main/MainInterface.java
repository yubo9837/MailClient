package com.mail.main;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//主界面
public class MainInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private SetupInterface setupInterface;
	private JButton btn_setup;
	public MainInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 766, 490);
		getContentPane().setLayout(null);
		
		btn_setup = new JButton("设置");
		btn_setup.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1)
					openSetup();
			}
		});
		btn_setup.setBounds(29, 31, 185, 82);
		getContentPane().add(btn_setup);
	}
	
	private void openSetup() {
		this.setupInterface=new SetupInterface();
		setupInterface.setVisible(true);
	}
}
