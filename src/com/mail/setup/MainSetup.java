package com.mail.setup;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.mail.main.MainInterface;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainSetup extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private OtherSetup otherSetup;
	private MainInterface mainInterface;
	private SinaMailSetup qqMailSetup;
	private Mail163Setup mail163Setup;
	/**
	 * Create the frame.
	 */
	public MainSetup(MainInterface mainInterface) {
		this.mainInterface=mainInterface;
		initFrame();
	}
	private void initFrame(){
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("选择邮箱类型：");
		label.setBounds(22, 36, 130, 15);
		contentPane.add(label);
		
		JButton btnQq = new JButton("新浪邮箱");
		btnQq.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getButton()==MouseEvent.BUTTON1)
					confirmQQ();
			}
		});
		btnQq.setBounds(22, 74, 93, 23);
		contentPane.add(btnQq);
		
		JButton button = new JButton("163邮箱");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				confirm163();
			}
		});
		button.setBounds(22, 123, 93, 23);
		contentPane.add(button);
		
		JButton button_1 = new JButton("其他邮箱");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getButton()==MouseEvent.BUTTON1)
					confirmOther();
			}
		});
		button_1.setBounds(22, 170, 93, 23);
		contentPane.add(button_1);
	}
	private void confirmOther() {
		otherSetup=new OtherSetup(this.mainInterface);
		this.setVisible(false);
		otherSetup.setVisible(true);
	}
	
	private void confirmQQ() {
		qqMailSetup=new SinaMailSetup(this.mainInterface);
		this.setVisible(false);
		qqMailSetup.setVisible(true);
	}
	
	private void confirm163() {
		mail163Setup=new Mail163Setup(this.mainInterface);
		this.setVisible(false);
		mail163Setup.setVisible(true);
	}
}
