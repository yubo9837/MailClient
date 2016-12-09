/** 
* @author yubo: 
* @version 创建时间：2016年12月8日 下午10:40:43 
* 类说明 
*/
package com.mail.box;


public abstract class AbstractBox implements MailBox{
	public String toString() {
		return getText();
	}
}
