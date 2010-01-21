
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* �˴�������CodeTransform�����Զ�����
* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package org.eclipse.swt.widgets
{
 import cn.smartinvoke.RemoteObject;
 import cn.smartinvoke.smartrcp.gui.control.EventRegister;
 [Bindable]
	[RemoteClass(alias="org.eclipse.swt.widgets.Button")]
 public class CButton extends RemoteObject {
  public function CButton(parent:RemoteObject,style:int){
	 super();
	  //���÷��񷽹���Button���ͷ������
	 this.createRemoteObject(arguments);
  }
  public function getText():String{
	 var retObj:Object=this.call("getText",arguments);
	 return retObj as String;
  }
 
  public function setBounds(x:int,y:int,w:int,h:int):void{
  	this.call("setBounds",arguments);
  }
  public function setText(string:String):void{
	 var retObj:Object=this.call("setText",arguments);
  }
  public function addListener(type:int,taget:Object,listener:Function):void{
  	 EventRegister.Instance.addListener(this,type,taget,listener);
  }
 }

}
