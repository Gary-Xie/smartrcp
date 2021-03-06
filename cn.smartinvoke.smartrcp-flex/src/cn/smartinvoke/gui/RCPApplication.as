package cn.smartinvoke.gui
{
	import cn.smartinvoke.executor.Executor;
	import cn.smartinvoke.pool.ObjectPool;
	import cn.smartinvoke.rcp.GlobalServiceId;
	import cn.smartinvoke.smartrcp.gui.FlashViewer;
	import cn.smartinvoke.smartrcp.gui.control.CEventNotifer;
	
	import flash.events.*;
	import flash.system.*;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.events.FlexEvent;
	import mx.events.ModuleEvent;
	import mx.modules.IModuleInfo;
	import mx.modules.Module;
	import mx.modules.ModuleManager;
	public class RCPApplication extends Application
	{
		private var clsResource_rcpModule:RCPModule;
		/**
		 *当前鼠标下的控件
		 */
		private var _mouseOverTaget:Object=null;
		
		public var flashViewer:FlashViewer=null;//容器对象的引用
		public var flashContainer:FlashContainer=null;//
		public var contextMenuManager:CContextMenuManager=null;
		
		public static var Instance:RCPApplication=null;
		public function RCPApplication()
		{ 
			super();
			RCPApplication.Instance=this;
			
			this.addEventListener(FlexEvent.PREINITIALIZE,this.preInitialize);
			//用于右键控件监听
			this.addEventListener(MouseEvent.MOUSE_OVER,this.onMouseOver);
			this.addEventListener(MouseEvent.CLICK,this.onMouseClick);
		}
		private function onMouseClick(evt:MouseEvent):void{
			this.contextMenuManager.hideAlreadyOpenMenu();
		}
		private function preInitialize(evt:FlexEvent):void{
			//添加全局服务对象
   			ObjectPool.INSTANCE.addObject(new CEventNotifer(),GlobalServiceId.Flex_CEvent_Notifer);
			Executor.init();
			//Executor.onLoadComplete=this.onJavaCreate;
		}
		public function onJavaCreate(flashViewer:FlashViewer,flashContainer:FlashContainer):void{
			this.flashViewer=flashViewer//new FlashViewer();
			//Alert.show(this.flashViewer+"");
			this.flashContainer=flashContainer;//new FlashContainer();
			//右键菜单管理器
			this.contextMenuManager=new CContextMenuManager(this);
			this.onJavaLoaded();
		}
		/**
		 *当java加载flash完毕，全局变量准备好后调用此方法，子类可以重载此方法实现自己的功能
		 */
		protected function onJavaLoaded():void{
			
		}
		private function onMouseOver(evt:Event):void{
			_mouseOverTaget=evt.target;
		}
		public function get mouseOverTaget():Object{
			return this._mouseOverTaget;
		}
		private var moduleUrl:String=null;
		private var moduleInfo:IModuleInfo;
		/**
    	 *加载模块
        **/ 
    	public function loadModule(url:String):void{
    		  this.moduleUrl=url;
    		  this.moduleInfo=ModuleManager.getModule(url);
    		  if(this.moduleInfo!=null){
			       this.moduleInfo.addEventListener(ModuleEvent.READY, onModuleReady);
			       this.moduleInfo.addEventListener(ModuleEvent.ERROR,onModuleLoadError);
                   //加载模块
                try{
			       this.moduleInfo.load();//ApplicationDomain.currentDomain
                }catch(e:Error){
              	   Alert.show(e.message);
                }
			  }
    	}
    	private function onModuleLoadError(e:ModuleEvent):void{
    		Alert.show("can not load moudle"+this.moduleUrl);
    	}
        private function onModuleReady(e:ModuleEvent):void {
        	try{
                   moduleInfo = e.currentTarget as IModuleInfo;
                  if(this.moduleInfo!=null){
                   var createObj:Object=moduleInfo.factory.create();
                   if(createObj!=null){
                   	//Alert.show('create obj='+createObj);
                    var module:Module=createObj as Module;
                    if(module!=null){
                     if(module is RCPModule){ 
                      try{
                     	var rcpModule:RCPModule=module as RCPModule;
                     	rcpModule.flashViewer=this.flashViewer;
                     	rcpModule.flashContainer=this.flashContainer;
                     	rcpModule.contextMenuManager=this.contextMenuManager;
                     }catch( e:Error){
                     		
                     }
                     }
                     module.percentWidth=100;
                     module.percentHeight=100;
                     this.addChild(module);
                    }
                   }
                  }
              }catch(e:Error){
              	 Alert.show(e.message);
              }
        }
        //--------------------------
        
		public function promptTitleAndMessage():Array{
		   return ["是否保存","视图数据已经修改，是否保存"];
	    }

	    public function doSave():void{
	       
	    }
	    public function doSaveAs():void {
		   
	    }

	    public function isDirty():Boolean {
		  return false;
	    }

 	    public function isSaveAsAllowed():Boolean {
		   return false;
	    }
        private var isInvoked:Boolean=false;//是否已经调用过了
	    public function isSaveOnCloseNeeded():Boolean {
	    	if(!isInvoked){
	    		this.onExist();
	    		isInvoked=true;
	    	}
	    	return false;
	    }
        /**
	    *当程序要退出时平台会调用此方法
	    */
	    public function onExist():void{
	       	
	    }
	}
}