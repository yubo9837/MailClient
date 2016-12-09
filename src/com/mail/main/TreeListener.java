package com.mail.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TreeListener extends MouseAdapter {
	private MainInterface mainInterface;
	public TreeListener(MainInterface mainInterface) {
		this.mainInterface = mainInterface;
	}
	public void mousePressed(MouseEvent e) {
		mainInterface.select();
	}
}