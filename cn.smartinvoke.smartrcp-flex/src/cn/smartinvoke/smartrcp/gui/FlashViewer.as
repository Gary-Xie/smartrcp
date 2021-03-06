package cn.smartinvoke.smartrcp.gui
{
	import cn.smartinvoke.RemoteObject;
	import cn.smartinvoke.rcp.GlobalServiceId;
	import cn.smartinvoke.smartrcp.CApplication;
	
	import org.eclipse.swt.widgets.Display;
	import org.eclipse.swt.widgets.Shell;
    [Bindable]
	[RemoteClass(alias="cn.smartinvoke.gui.FlashViewer")]
	public class FlashViewer extends RemoteObject
	{
		/**
		 *返回当前获得焦点的FlashViewer对象
		 */
		public static function  getActiveFlashViewer():FlashViewer{
			return CApplication.Instance.getActiveFlashViewer();
		}
		public function FlashViewer()
		{
			super();
			this.remoteId=GlobalServiceId.FlashViewer;
		}
		
		public function getFlexAppId():String{
			return this.call("getFlexAppId",arguments) as String;
		}
		public function setTitle(title:String):void{
			this.call("setTitle",arguments);
		}
		public function getTitle():String{
			return this.call("getTitle",arguments) as String;
		}
		/*public function addAction(action:CAction):void{
			this.call("addAction",arguments);
		}*/
		public function invoke(methodName:String,params:Array=null):Object{
			return this.call("invoke",[methodName,params]);
		}
		public function getParent():RemoteObject{
			return this.call("getParent") as RemoteObject;
		}
		//--------------
		public function getShell():Shell{
			var ret:Shell=Display.getCurrent().call('getActiveShell',[]) as Shell;
			return ret;
		}
		//返回模块路径
		public function getModulePath():String{
			return this.call('getModulePath',arguments) as String;
		}
		//debug调用此方法
		public function setDebugModule(url:String):void{
			this.call("setDebugModule",arguments);
		}
		//
		public override function gc():void{
			
		}
		public function setData(key:String,data:Object):void{
			if(key!=null){
			 this.call("setData",arguments);
			}
		}
		public function getData(key:String):Object{
			if(key!=null){
				return this.call("getData",arguments);
			}else{
				return null;
			}
		}
		public function removeData(key:String):void{
			if(key!=null){
			 this.call("removeData",arguments);
			}
		}
		
	}
}