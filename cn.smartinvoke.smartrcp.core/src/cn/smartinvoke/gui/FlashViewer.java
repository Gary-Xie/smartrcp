package cn.smartinvoke.gui;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;

import cn.smartinvoke.FlashContainer;
import cn.smartinvoke.ILoadCompleteListener;
import cn.smartinvoke.IServerObject;
import cn.smartinvoke.Msg;
import cn.smartinvoke.ObjectPool;
import cn.smartinvoke.OleHookInterceptor;
import cn.smartinvoke.RemoteObject;
import cn.smartinvoke.Win32Constant;
import cn.smartinvoke.exception.InvokeException;
import cn.smartinvoke.exception.Messages;
import cn.smartinvoke.rcp.CPageLayout;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.smartrcp.core.ISWTPartUnit;
import cn.smartinvoke.smartrcp.core.SWTUnitViewPart;
import cn.smartinvoke.smartrcp.gui.FlashShell;
import cn.smartinvoke.smartrcp.gui.FlashViewPart;
import cn.smartinvoke.smartrcp.gui.SplashWindow;
import cn.smartinvoke.smartrcp.gui.control.CAction;
import cn.smartinvoke.smartrcp.gui.control.EventFilter;
import cn.smartinvoke.smartrcp.gui.control.EventRegister;
import cn.smartinvoke.smartrcp.gui.control.GlobalServiceId;
import cn.smartinvoke.smartrcp.gui.module.CObservable;
import cn.smartinvoke.smartrcp.util.JFaceHelpMethod;
import cn.smartinvoke.smartrcp.util.UIHelpMethod;
import cn.smartinvoke.util.HelpMethods;
import cn.smartinvoke.util.Log;

public class FlashViewer implements IServerObject {
	private static List<FlashViewer> containers = new LinkedList<FlashViewer>();
	/**
	 * ��ʹ�õ�id
	 */
	public static List<Integer> usedAppIds=new LinkedList<Integer>();
	/**
	 * ��ǰ��ý����FlashViewer
	 */
	public static FlashViewer curFlashViewer=null;
	/** ����һ����Ĳ���Ψһ��appId
	 * 
	 * @return
	 */
	public static synchronized int getViewNum() {
		int num=-1;
		//ѭ������Id�����������ֱ�����һ��δʹ�õ�IdΪֹ
		while(true){
			num=getRandomNum();
			if(!usedAppIds.contains(num)){
				break;
			}
		}
		usedAppIds.add(num);
		return num;
	}
    private static int getRandomNum(){
    	double val=Math.random()*Integer.MAX_VALUE;
    	return (int)val;
    }
	public static void add_Viewer(FlashViewer container) {
		if (container != null) {
			if (!containers.contains(container)){
				//��id��ӵ���ʹ�ü�����
				usedAppIds.add(Integer.valueOf(container.getAppId()));
				containers.add(container);
			}
			//����ǰ�򿪵�viewPart����Ϣ��ӽ�ģ���Ӧ����
			CPageLayout page= CPageLayout.Instance;
			String modulePath=container.getModulePath();
			String appId=container.getAppId();
			page.addViewPartInfo(modulePath,appId);
			Log.println("addModule:"+modulePath+" addId:"+appId);
		}
	}

	public static void remove_Viewer(FlashViewer container) {
		if (container != null) {
			    if(containers.contains(container)){
				  containers.remove(container);
			    }
				//��PageLayout��appIdģ���Ӧ����ɾ����part����Ϣ
				CPageLayout page= CPageLayout.Instance;
				String modulePath=container.getModulePath();
				String appId=container.getAppId();
				page.removeViewPartInfo(modulePath,appId);
				
				Log.println("removeModule:"+modulePath+" addId:"+appId);
		}
		
	}

	public static List<FlashViewer> getViewers() {
		return containers;
	}

	public static FlashViewer getViewerBySwfPath(String swfPath) {
		FlashViewer ret = null;
		if (swfPath != null) {
			for (int i = 0; i < containers.size(); i++) {
				FlashViewer temp = containers.get(i);
				if (temp != null) {
					String path = temp.getModulePath();
					if (path != null && path.equals(swfPath)) {
						ret = temp;
						break;
					}
				}
			}
		}
		return ret;
	}
    public static FlashViewer getViewerByParent(IViewPart part){
    	FlashViewer ret = null;
		if (part != null) {
			for (int i = 0; i < containers.size(); i++) {
				FlashViewer temp = containers.get(i);
				if (temp != null) {
					if(temp.getParent().equals(part)){
					  ret=temp;
					  break;
					}
				}
			}
		}
		return ret;
    }
	public static FlashViewer getViewerByAppId(String appId) {
		FlashViewer ret = null;
		if (appId != null) {
			for (int i = 0; i < containers.size(); i++) {
				FlashViewer temp = containers.get(i);
				if (temp != null) {
					String id = temp.getAppId();
					if (id != null && id.equals(appId)) {
						ret = temp;
						break;
					}
				}
			}
		}
		return ret;
	}

	private Object parent = null;
    private RemoteObject flexApp=null;
    //�Ƿ���debug
    public boolean debugModule=false;
    private Map<String,Object> dataMap=new HashMap<String, Object>();
	public FlashViewer(String appId) {
		this.appId = appId;
	}
	private FlashContainer flashContainer;

	public FlashViewer(String appId, Composite parent, String swfPath) {
		this(appId);
		
		this.createView(parent, swfPath);
		FlashViewer.add_Viewer(this);
	}

	private String swfPath = null;

	/**
	 * 
	 * @param parent
	 * @param swfPath
	 *            ��һ��Ԫ��Ϊswf�����ļ�·�����ڶ���Ԫ��Ϊģ��·��
	 */
	public FlashViewer(String appId, Composite parent, String[] swfPath) {
		this(appId);
		
		this.createView(parent, swfPath);
		FlashViewer.add_Viewer(this);
	}

	private String[] swfAndModulePath;
    private String modulePath=null;
	private void createView(Composite parent, String swfPath) {
		this.createFlashContainer(parent);
		this.swfPath = swfPath;
		this.modulePath=swfPath;
	}

	private void createView(Composite parent, String[] swfPath) {
		this.createFlashContainer(parent);
		if(swfPath!=null && swfPath.length==2){
		 this.modulePath=swfPath[1];
		}
		swfAndModulePath = swfPath;
	}
    public boolean isLoaded=false;
	/**
	 * ����flash
	 */
	public void loadFlash() {
		if(isLoaded){
			return;
		}
		if(this.flashContainer==null){
			return;
		}
		// ����flash
		
		if (this.swfPath != null) {
			this.flashContainer.loadMovie(0, swfPath);
			
		} else if (this.swfAndModulePath != null) {
			if (swfAndModulePath.length >= 2) {
				
				this.swfPath = swfAndModulePath[1];
				this.flashContainer.loadMovie(0, swfAndModulePath);
				//JFaceHelpMethod.showInfo("load Main:"+swfAndModulePath[0]+" module:"+this.swfPath);
			}
		}
		
	}
    public boolean isBreak=false;
	private void createFlashContainer(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		// ����FlashViewer��Ψһ��ʶ��
		String appId = this.getAppId();
		flashContainer = new FlashContainer(container, appId);
		//flex��ǰ����������
		this.flexApp=new RemoteObject(flashContainer);
        this.flexApp.setRemoteId("app");
        
        //��flex������Ϻ󴫵�����������flex application����
        flashContainer.addListener(new ILoadCompleteListener(){
        	public void run(){
        	   FlashViewer.this.isLoaded=true;
        	   
        	   flexApp.asyncCall("onJavaCreate", new Object[]{FlashViewer.this,flashContainer});
        	}
        });
        
      /*  
		// ����ǰ��FlashViewerע��Ϊ��������Ա�������ڵ�flex�ĵ���
		ObjectPool.INSTANCE.putObject(flashContainer.getAppId(), this,
				GlobalServiceId.FlashViewer);
		// ��flash�ؼ���Ϊ������
		ObjectPool.INSTANCE.putObject(flashContainer.getAppId(),
				flashContainer, GlobalServiceId.FlashViewer_Control);
*/
		flashContainer.addHookInterceptor(new OleHookInterceptor() {
			public boolean intercept(Msg message, int code, int param,
					int param2) {
				if (message.getMessage() == Win32Constant.WM_RBUTTONDOWN) {
					Point cursor = flashContainer.getParent().toControl(
							Display.getCurrent().getCursorLocation());
					if (flashContainer.getBounds().contains(cursor)
							&& flashContainer.isVisible()) {
						createMouseClickEvent(3, message.getX(), message.getY());
						return true;
					}
				}
				return false;
				
				//���mouseOver
				/*if (message.getMessage() == Win32Constant.WM_MOUSEHOVER){
					
					return false;
				}
				if (message.getMessage() == Win32Constant.WM_MBUTTONDOWN) {
					Point cursor = flashContainer.getParent().toControl(
							Display.getCurrent().getCursorLocation());
					if (flashContainer.getBounds().contains(cursor)
							&& flashContainer.isVisible()) {
						createMouseClickEvent(2, message.getX(), message.getY());
						return true;
					}
				}
				if (message.getMessage() == Win32Constant.WM_LBUTTONDOWN) {
					Point cursor = flashContainer.getParent().toControl(
							Display.getCurrent().getCursorLocation());
					if (flashContainer.getBounds().contains(cursor)
							&& flashContainer.isVisible()) {
						createMouseClickEvent(1, message.getX(), message.getY());
						return false;
					}
				}
				return false;*/
			}

		});
	}
    
	/**
	 * �������ɷ�����¼�
	 * 
	 * @param button
	 * @param x
	 * @param y
	 */
	private void createMouseClickEvent(int button, int x, int y) {
		Event evt = new Event();
		evt.widget = flashContainer;

		Point loca = this.flashContainer.toControl(x, y);
		evt.x = loca.x;
		evt.y = loca.y;
		evt.button = button;
		evt.widget = this.flashContainer;
		evt.type = SWT.MouseDown;

		EventFilter.dealEvent(evt);
	}

	public FlashContainer getFlashContainer() {
		return flashContainer;
	}

	public void setFlashContainer(FlashContainer flashContainer) {
		this.flashContainer = flashContainer;
	}
	public String getSwfPath1() {
		return swfPath;
	}
	
	public String getModulePath() {
		return modulePath;
	}
	public void setModulePath(String modulePath){
		this.modulePath=modulePath;
	}
	public void setSwfPath(String path){
		this.swfPath=path;
	}
	//--------------
   /* public boolean equals(Object obj){
    	boolean ret=false;
    	if(obj!=null && obj instanceof FlashViewer){
    		FlashViewer other=(FlashViewer)obj;
    		return this.appId.equals(other.appId);
    	}
    	return ret;
    }*/
    public int hashCode(){
    	return this.appId.hashCode();
    }
    public void setData(String key,Object data){
 	   if(key!=null){
 		 this.dataMap.put(key, data);
 	   }
 	}
 	public void removeData(String key){
 		if(key!=null){
 			this.dataMap.remove(key);
 		}
 	}
 	public Object getData(String key){
 		//Object ret=null;
 		if(key!=null){
 			return this.dataMap.get(key);
 		}else{
 			return null;
 		}
 		//return ret;
 	}
 	/**
 	 * ��flashViewer�˳�֮ǰ����flex app��onExist����
 	 */
 	public void flexAppExist(){
 		try{this.flexApp.call("onExist",null);}catch(Exception e){};
 	}
 	private boolean isDispose=false;//ȷ��dispose����ֻ����һ��
	public void dispose() {
		if(isDispose){
			return;
		}
		if(!isDispose){
		//��flashViewer�˳�֮ǰ����flex app��onExist����
		//try{this.flexApp.call("onExist",null);}catch(Exception e){};
		
		//���������
        this.dataMap.clear();
		// ��ȫ������������ɾ��
		//if (containers.contains(this)) {
		//	Log.println("in FlashViewer dispose");
			FlashViewer.remove_Viewer(this);
		//}
		
		if(this.parent instanceof FlashViewPart 
				|| parent instanceof FlashShell){//��ǰflashViewer������flash��������
			// ɾ��������
			ObjectPool pool = ObjectPool.INSTANCE;
			EventRegister eventRegister = (EventRegister) pool
					.getObject(GlobalServiceId.Event_Register);
			String appId = this.flashContainer.getAppId();
			eventRegister.removeListeners(appId);
			
			//ɾ��Observable�е����м�����
			CObservable.deleteListeners(this.getAppId());
			
			// ɾ��������еĶ�Ӧ������
			if (this.flashContainer != null) {
				this.flashContainer.dispose();
			}
		}
		isDispose=true;
		}
	}

	// -----------------
	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}
	
	public void addAction(CAction cAction){
    	/*if(cAction==null){
			return;
		}
		CActionImpl actionImpl = null;
		int declType = cAction.getType();
		if (declType == IAction.AS_CHECK_BOX) {
			actionImpl = new CActionImpl(cAction.getText(),
					IAction.AS_CHECK_BOX, cAction
							.isChecked());
		} else if (declType == IAction.AS_RADIO_BUTTON) {
			actionImpl = new CActionImpl(cAction.getText(),
					IAction.AS_RADIO_BUTTON, cAction
							.isChecked());
		} else if(declType==-1){
			actionImpl = new CActionImpl(cAction.getText());
		}else{
			actionImpl = new CActionImpl(cAction.getText(),declType);
		}
		actionImpl.init(cAction);
		if(this.parent instanceof FlashViewPart){
		  FlashViewPart viewPart=(FlashViewPart)parent;
		 IToolBarManager toolBarManager=viewPart.getToolBarManager();
		 IMenuManager menuManager=viewPart.getMenuManager();
		 toolBarManager.add(actionImpl);
		 toolBarManager.update(true);
		 menuManager.add(actionImpl);
		 menuManager.updateAll(true);
		}*/
    }
	private String appId;

	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getFlexAppId(){
		return appId;
	}
	private Object data;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getTitle(){
		if(this.parent==null){
			return null;
		}
		if(this.parent instanceof IWorkbenchPart){
			IWorkbenchPart workbenchPart=(IWorkbenchPart)this.parent;
			return workbenchPart.getTitle();
		}else 
		if(this.parent instanceof FlashShell){
			FlashShell shell=(FlashShell)this.parent;
			return shell.getText();
		}else{
			return null;
		}
	}
	public void setTitle(String title){
		if(this.parent!=null && title!=null){
			if(this.parent instanceof FlashViewPart){
				FlashViewPart viewPart=(FlashViewPart)this.parent;
				viewPart.setViewTitle(title);
			}else 
			if(this.parent instanceof FlashShell){
				FlashShell shell=(FlashShell)this.parent;
				shell.setText(title);
			}
		}
	}
	public String toString(){
		if(this.modulePath!=null){
			int spl=this.modulePath.lastIndexOf('/');
			if(spl!=-1){
				return this.modulePath.substring(spl+1)+this.getAppId();
			}else{
				return this.modulePath+this.getAppId();
			}
		}
		return null;
	}
	public RemoteObject getFlexApp() {
		return flexApp;
	}

	/**
	 * flex���ô˷�������ǰflashViewer�µ�flex application����ģ��
	 * @param moduleUrl
	 */
	public void setDebugModule(String moduleUrl){
	  if(this.debugModule){
		if(moduleUrl!=null){
			moduleUrl=UIHelpMethod.getFullPath(moduleUrl);
			this.flexApp.asyncCall("loadModule", new Object[]{moduleUrl});
		}
	  }
	}
	public Object invoke(String methodName,Object[] pars){
		if(this.parent instanceof FlashViewPart || this.parent instanceof FlashShell){
		  return this.flexApp.call(methodName, pars);
		}else if(this.parent instanceof SWTUnitViewPart){
		  ISWTPartUnit partUnit=((SWTUnitViewPart)this.parent).getPartUnit();
		  if(partUnit!=null){
			  return invokeObject(partUnit,methodName,pars);
		  }else{
			  throw new RuntimeException("current ViewPart's ISWTPartUnit is null");
		  }
		}else{
		  return invokeObject(parent,methodName,pars);
		}
	}
	private Object invokeObject(Object callObj,String methodName,Object[] pars){
        Object retObj=null;
        if(callObj!=null){
           Class cls=callObj.getClass();
           if(pars==null){
        	   pars=new Object[0];
           }
   		   Method ivkMethod=HelpMethods.getMethod(cls, methodName, pars);
		   if(ivkMethod==null){//û�ж�Ӧ�ķ����򱨸��쳣
				InvokeException invokException=
					new InvokeException(InvokeException.TYPE_INVOK_METHOD_NULL,
							Messages.INSTANCE.getMsg(Messages.IVK_METHOD_NOT_FOUND,new String[]{cls.getName(),methodName}));
				
				throw invokException;
		   }else{
			   try{
                  retObj=ivkMethod.invoke(callObj, pars);
			   }catch(Exception e){
//				 InvokeException invokException=
//					new InvokeException(InvokeException.TYPE_INVOK_METHOD_EXCEPTION,
//							Messages.INSTANCE.getMsg(Messages.IVK_METHOD_EXCEPTION,
//							new String[]{cls.getName(),methodName,e.getMessage()}));
//				 
				 Throwable cause=e;
				 while(true){
					 Throwable tempCause=null;
					 if((tempCause=cause.getCause())==null){
						 break;
					 }else{
						 cause=tempCause;
					 }
				 }
				 throw  new RuntimeException(cause.getMessage());
			   }
		   }
        }else{//�����ö���Ϊ�գ��׳��쳣
        	InvokeException invokException=
				new InvokeException(InvokeException.TYPE_INVOK_OBJECT_NULL,
						Messages.INSTANCE.getMsg(Messages.IVK_OBJECT_NOT_FOUND,new String[]{""}));
			
			throw invokException;
        }
        return retObj;
	}
	
}
