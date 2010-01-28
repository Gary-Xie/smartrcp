
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* �˴�������CodeTransform�����Զ�����
* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package cn.smartinvoke.smartrcp.gui.control
{
 import cn.smartinvoke.RemoteObject;
 import cn.smartinvoke.executor.Executor;
 import cn.smartinvoke.pool.ObjectPool;
 import cn.smartinvoke.rcp.CAction;
 import cn.smartinvoke.rcp.CMenuRelation;
 import cn.smartinvoke.smartrcp.gui.module.CEventBean;
 
 import mx.core.Application;
 import mx.utils.UIDUtil;
 public class CNativeMenuBuilder extends RemoteObject {
  public function CNativeMenuBuilder(parent:RemoteObject){
	 super();
	 this.createRemoteObject(arguments);
  }
   
   public function initAction(cActions:Array):void{
	 var retObj:Object=this.call("initAction",arguments);
   }
   public function addAction(cAction:CAction):void{
	 var retObj:Object=this.call("addAction",arguments);
   }
   public function initMenu(menuRelation:CMenuRelation):void{
	 var retObj:Object=this.call("initMenu",arguments);
   }
   public function fireActionClick(actionId:String):void{
	 var retObj:Object=this.call("fireActionClick",arguments);
   }
   public function show(x:int,y:int):void{
	 var retObj:Object=this.call("show",arguments);
   }
   public function hide():void{
	 var retObj:Object=this.call("hide",arguments);
   }
   public function setEnable(actionId:String,enable:Boolean):void{
	 var retObj:Object=this.call("setEnable",arguments);
   }
   //-------------------------
   public function addMenuItem(actionId:String,path:String):void{
	 var retObj:Object=this.call("addMenuItem",arguments);

   }
   public function removeMenuItem(actionId:String,path:String):void{
	 var retObj:Object=this.call("removeMenuItem",arguments);

   }
    //------------------------------------
    public function addListener(listener:Function,thisObject:Object=null):void{
			if(listener==null){
				return;
			}
			//-------�����¼���Ϣ��UID
			var thisObjId:String="app";
			if(thisObject!=null){
				thisObjId=UIDUtil.getUID(thisObject);
			}else{
				thisObject=Application.application;
			}
			var listenerId:String=UIDUtil.getUID(listener);
			var eventUid:String=thisObjId+listenerId;
			//
			ObjectPool.INSTANCE.addObject([thisObject,listener],eventUid);
			//�����¼���Ϣ��java
			var bean:CEventBean=new CEventBean();
			bean.appId=Executor.applicationId;
			bean.funId=listenerId;
			bean.tagetId=thisObjId;
			this.call("addListener",[bean]);
		}
		public function removeListener(
		   listener:Function,thisObject:Object=null):void{
		   if(listener!=null){
		   	 //ɾ��������
		   	 var listenerId:String=UIDUtil.getUID(listener);
			 var thisObjId:String="app";
			 if(thisObject!=null){
			    thisObjId=UIDUtil.getUID(thisObject);
			 }
			 var eventUid:String=thisObjId+listenerId;
			 ObjectPool.INSTANCE.removeObject(eventUid);
			 //�����¼���Ϣ��java
			 var bean:CEventBean=new CEventBean();
			 bean.appId=Executor.applicationId;
			 bean.funId=listenerId;
			 bean.tagetId=thisObjId;
		   	 this.call('removeListener',[bean]);
		   }
		}
 }

}
