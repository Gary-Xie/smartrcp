
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* 你可以复制或修改此代码，但是必须在源代码和二进制包中添加申明以表示引用或修改了smartinvoke的代码
* 请珍惜作者的劳动成果* 您可以通过 
*  网站:http://smartinvoke.cn/ 
*  邮件:pzxiaoxiao130130@gmail.com
*  QQ：90636900*  联系到作者^_^ 
*******************************************************************************/ 
/**
 * 消息类，封装windows gui与swt之间的消息数据
 */
package cn.smartinvoke.gui;

import org.eclipse.swt.internal.win32.MSG;
import org.eclipse.swt.internal.win32.OS;
public class Msg {
	private MSG msg;

	public int getMessage() {
		return msg.message;
	}

	public int getLParam() {
		return msg.lParam;
	}

	public int getWParam() {
		return msg.wParam;
	}

	public int getTime() {
		return msg.time;
	}

	public int getHwnd() {
		return msg.hwnd;
	}

	public int getX() {
		return msg.x;
	}

	public int getY() {
		return msg.y;
	}
	public Msg(MSG msg) {
		this.msg = msg;
	}

	public void setMessage(int message) {
		msg.message = message;
	}

	public void setLParam(int lParam) {
		msg.lParam = lParam;
	}

	public void setWParam(int wParam) {
		msg.wParam = wParam;
	}

	public void setTime(int time) {
		msg.time = time;
	}

	public void setHwnd(int hwnd) {
		msg.hwnd = hwnd;
	}

	public void setX(int x) {
		msg.x = x;
	}

	public void setY(int y) {
		msg.y = y;
	}

	public static Msg valueOf(int point) {
		MSG msg = new MSG();
		OS.MoveMemory(msg, point, MSG.sizeof);
		return new Msg(msg);
	}

	public void saveToPoint(int point) {
		OS.MoveMemory(point, msg, MSG.sizeof);
	}

	public void setValue(Msg msg) {
		if (this.msg == null)
			this.msg = msg.msg;
		else {
			this.msg.hwnd = msg.msg.hwnd;
			this.msg.message = msg.msg.message;
			this.msg.wParam = msg.msg.wParam;
			this.msg.lParam = msg.msg.lParam;
			this.msg.time = msg.msg.time;
			this.msg.x = msg.msg.x;
			this.msg.y = msg.msg.y;
		}
	}
}
