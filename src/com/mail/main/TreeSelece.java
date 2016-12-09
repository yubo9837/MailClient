/** 
* @author yubo: 
* @version 创建时间：2016年12月8日 下午8:48:56 
* 类说明 
*/
package com.mail.main;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import com.mail.box.MailBox;

public class TreeSelece extends DefaultTreeCellRenderer{

	//树节点被选中时的字体
	private Font selectFont;
	
	public TreeSelece() {
		this.selectFont = new Font(null, Font.BOLD, 12);
	}
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		MailBox box = (MailBox) node.getUserObject();
		this.setText(box.getText());
		//判断是否选中, 再决定使用字体
		if (isSelected(node, tree)) {
			this.setFont(this.selectFont);
		} else {
			this.setFont(null);
		}
		return this;
	}
	
	//判断一个node是否被选中
	private boolean isSelected(DefaultMutableTreeNode node, JTree tree) {
		//得到选中的TreePath
		TreePath treePath = tree.getSelectionPath();
		if (treePath == null) return false;
		//得到被选中的节点
		DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
		if (node.equals(selectNode)) {
			return true;
		}
		return false;
	}
	
}