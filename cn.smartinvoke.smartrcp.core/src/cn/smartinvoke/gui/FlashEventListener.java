
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* 你可以复制或修改此代码，但是必须在源代码和二进制包中添加申明以表示引用或修改了smartinvoke的代码
* 请珍惜作者的劳动成果* 您可以通过 
*  网站:http://smartinvoke.cn/ 
*  邮件:pzxiaoxiao130130@gmail.com
*  QQ：90636900*  联系到作者^_^ 
*******************************************************************************/ 

package cn.smartinvoke.gui;

public interface FlashEventListener {
	
	void onReadyStateChange(int newState);

	void onProgress(int percentDone);
	void onFSCommand(String command, String args);
	void onFlashCall(String command);
}
