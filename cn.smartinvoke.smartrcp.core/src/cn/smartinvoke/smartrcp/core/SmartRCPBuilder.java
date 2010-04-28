package cn.smartinvoke.smartrcp.core;

import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.*;
import org.eclipse.ui.application.*;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.sun.corba.se.spi.orbutil.threadpool.Work;

import cn.smartinvoke.IServiceObjectCreator;
import cn.smartinvoke.ObjectPool;
import cn.smartinvoke.TypeFactory;
import cn.smartinvoke.TypeMapper;
import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.CMenuRelation;
import cn.smartinvoke.rcp.CPageLayout;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.rcp.CWindowConfigurer;
import cn.smartinvoke.rcp.ErrorMessages;
import cn.smartinvoke.smartrcp.CApplication;
import cn.smartinvoke.smartrcp.SmartRCPBundle;
import cn.smartinvoke.smartrcp.app.CAppService;
import cn.smartinvoke.smartrcp.app.pack.CAppInfo;
import cn.smartinvoke.smartrcp.app.pack.PackageTool;
import cn.smartinvoke.smartrcp.gui.*;
import cn.smartinvoke.smartrcp.gui.control.*;
import cn.smartinvoke.smartrcp.util.BundleHelpMethod;
import cn.smartinvoke.smartrcp.util.JFaceConstant;
import cn.smartinvoke.util.ConfigerLoader;
import cn.smartinvoke.util.HelpMethods;
import cn.smartinvoke.util.ImageManager;
import cn.smartinvoke.util.Log;

public class SmartRCPBuilder {
	private static SplashWindow splash_win = SplashWindow.INSTANCE;
   // private static SmartRCPWindowAdvisor window_Advisor=new SmartRCPWindowAdvisor();
	public static SmartRCPBuilder Instance=new SmartRCPBuilder();
	public SmartRCPBuilder(){
       
	}
	private static BundleContext context=null;
	public static BundleContext getCurContext(){
		return context;
	}
	/**
	 * ��ʼ��SmartRCP��ó�ʼ����Ϣ
	 */
	public  void init(final BundleContext context){//IServiceObjectCreator objectCreator) {
		//----------------------
		SmartRCPBuilder.context=context;
		//----------------------
		//����������Ϣ
		try{
			ConfigerLoader.init();
			
	        //��ӷ�����
	        TypeMapper.addServicePack("org.eclipse.swt.widgets");
	        TypeMapper.addServicePack("org.eclipse.swt");
	        TypeMapper.addServicePack("java.io");
	        
			startBundles(context);//������׼��
		}catch(Exception e){
			throw new RuntimeException();
		};
		ObjectPool objectPool = ObjectPool.INSTANCE;
		
		//ע������
	   	TypeFactory.Object_Pool=ObjectPool.INSTANCE;
		//��������ػ���
		objectPool.objectCreator = new IServiceObjectCreator(){
			
			public Class getClass(String clsName) throws ClassNotFoundException {
				//Log.println("����������"+clsName+"�Ķ���");
				Class cls=null;
				try{
				 
				 cls=Class.forName(clsName);
				 return cls;
				}catch(Exception e){};
				Bundle[] bundles=context.getBundles();
				if(bundles!=null){
					for(int n=0;n<bundles.length;n++){
						Bundle bundle=bundles[n];
						try{
						  cls=bundle.loadClass(clsName);
						  break;
						}catch(ClassNotFoundException e){
						  
						}
					}
				}
				if(cls==null){
					
					throw new ClassNotFoundException(clsName);
				}
				return cls;
			}
 		};
 		TypeFactory.objectCreator=objectPool.objectCreator;
 		TypeFactory.Type_Mapper=TypeMapper.Instance;
 		
 		 // ----------- ע��ȫ�ַ���
 		//objectPool.putObject(new DbUtil(), GlobalServiceId.DB_Util);//���ݿ��������
		objectPool.putObject(CApplication.Instance, GlobalServiceId.Cur_Application);
		objectPool.putObject(new FlashViewInvoker(),GlobalServiceId.FlashView_Invoker);
		// ����¼�ע��������
		EventRegister eventRegister = new EventRegister();
		objectPool.putObject(eventRegister, GlobalServiceId.Event_Register);
		
		ViewManager viewManager=ViewManager.Instance;
		ObjectPool.INSTANCE.putObject(viewManager,
				GlobalServiceId.View_Manager);
	}
	protected  void startBundles(BundleContext context){
		 //���ر�׼��
		 String libPath=HelpMethods.getPluginFolder()+"/lib";
		 startBundles(libPath);
		 libPath=HelpMethods.getPluginFolder()+"/ext";
		 //������չ��
		 startBundles(libPath);
	}
	
	private void startBundles(String libPath){
		Log.println("����"+libPath+"�еĿ�");
		 //PrintWriter logWriter=null;
		 File folder=new File(libPath);
		 if(!folder.exists()){
			 folder.mkdirs();
		 }
		 //���ر�׼����
		 try{
		   CApplication.setStandardBundles(BundleHelpMethod.installBundles(folder));
		 }catch(BundleException e){
			 throw new RuntimeException(e);
		 }
	}
	/**
	 * ��Splash���ڻ�ó�ʼ����Ϣ����ʼ������
	 */
	public  void initWindows(){
		// ------------����������Ϣ
		// ��splash��������Ϊ�������flex����
		ObjectPool.INSTANCE.putObject(splash_win, GlobalServiceId.Splash_Win);
		//���ص�ǰsmartrcp bundle
		//TODO ��ǰ��smartrcp bundleӦ��
		/*try {
//			CAppInfo appInfo=CAppService.getAppInfo(ConfigerLoader.appPath);
//			SmartRCPBundle rcpBundle=new SmartRCPBundle(appInfo);
//			rcpBundle.load();
//			CApplication.setCurApp(rcpBundle);
			//����ͼƬ��Դ
			//ImageManager.loadImages(ConfigerLoader.appPath);
		}catch(BundleException e1){
			MessageDialog.openError(splash_win.shell, "", ErrorMessages.Bundle_Load_Error+e1.getMessage());
		}catch(MalformedURLException e){
			MessageDialog.openError(splash_win.shell, "", ErrorMessages.Image_Load_Error+e.getMessage());
			e.printStackTrace();
		}*/
		try {
			// -----------����splash���ڣ�����flex��splash��Ϣ
			try {
				splash_win.open();
			}catch(Throwable e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * ��ʼ������̨����
	 * @param configurer
	 */
	public  void initWorkbench(IWorkbenchConfigurer configurer){
		if(SplashWindow.getPerspective().saveAndRestore){
		  configurer.setSaveAndRestore(true);
		}else{
		  configurer.setSaveAndRestore(false);
		}
	}
	private static String key_workbench_state="smartrcp_key_workbench_state";
	private static String key_config_path="smartrcp_key_config_path";
	/**
	 * ���湤��̨������Ϣ
	 * @param memento
	 */
	public  void saveWorkbenchState(IMemento memento){
		IViewReference[] refs=window.getActivePage().getViewReferences();
		StringBuilder viewPartInfo=new StringBuilder();
		if(refs!=null){
		 for(int n=0;n<refs.length;n++){
			 IViewReference ref=refs[n];
			 IWorkbenchPart part=ref.getPart(true);
			 if(part  instanceof ViewPart){
				 ViewPart viewPart=(ViewPart)part;
				 List<FlashViewer> flashViewers=FlashViewer.getViewers();
				 for(int i=0;i<flashViewers.size();i++){
					 FlashViewer flashViewer=flashViewers.get(i);
					 if(flashViewer.getParent().equals(viewPart)){
						 viewPartInfo.append(flashViewer.getAppId()).append("=")
						 .append(flashViewer.getModulePath()).append("\n");
						 break;
					 }
				 }
			 }
		 }
		}
		
		memento.putString(key_workbench_state, viewPartInfo.toString());
		//�洢start.ini�ļ�
		memento.putString(key_config_path, ConfigerLoader.configPath);
	}
	/**
	 * �ָ�����̨������Ϣ
	 * @param memento
	 */
	public  void restoreWorkbenchState(IMemento memento){
		//����start.ini�ļ�
		ConfigerLoader.configPath=memento.getString(key_config_path);
		String val=memento.getString(key_workbench_state);
		if(val!=null){
			CPageLayout pageLayout=CPageLayout.Instance;
			String[] items=val.split("\n");
			if(items!=null){
				for(int n=0;n<items.length;n++){
					String item=items[n];
					int spl=item.indexOf("=");
					String appId=item.substring(0,spl);
					String modulePath=item.substring(spl+1);
					pageLayout.addViewPartInfo(modulePath, appId);
				}
			}
		}
	}
    /**
     * ��ʼ��ͼ�������
     * @param imageRegistry
     */
	public  void initImageRegistry(ImageRegistry imageRegistry) {
		// ---------����ͼ��ע����Ϣ
        Display curDisp=Display.getCurrent();
		ObjectPool.INSTANCE.putObject(curDisp, GlobalServiceId.Swt_Display);
		ImageManager.init(imageRegistry);
	}

	public  CActionManager actionManager = null;
	private  ActionBarAdvisor actionBarAdvisor;
    public  IWorkbenchWindow window;
	/**
	 * ����action
	 */
	public  void createActions(ActionBarAdvisor actionBarAdvisor,IWorkbenchWindow window){
		if(actionBarAdvisor!=null){
			this.actionBarAdvisor=actionBarAdvisor;
		}else{
			actionBarAdvisor=this.actionBarAdvisor;
		}
		if(window!=null){
		  this.window=window;
		}else{
		  window=this.window;
		}
		
		ObjectPool.INSTANCE.putObject(new CActionManager(actionBarAdvisor,window),
				GlobalServiceId.CAction_Manager);
		
		CPerspective cPerspective = SplashWindow.getPerspective();
		if (cPerspective != null) {
			Object[] actionArr = cPerspective.actions;
			if (actionArr != null) {
				ObjectPool objectPool = ObjectPool.INSTANCE;

				Object obj = objectPool
						.getObject(GlobalServiceId.CAction_Manager);
				if (obj != null) {
					actionManager = (CActionManager) obj;
					int len = actionArr.length;
					for (int i = 0; i < len; i++) {
						Object actionObj = actionArr[i];
						if (actionObj instanceof CAction) {
							actionManager.addAction((CAction) actionObj);
						}
					}
				}
			}
		}
	}
    private  IMenuManager MENU_BAR;
	/**
	 * �����˵�
	 * 
	 * @param menuBar
	 */
	public  void fillMenuBar(IMenuManager menuBar) {
		if(menuBar!=null){//��һ������
		  this.MENU_BAR=menuBar;
		}else{//�ڶ�������
		  menuBar=this.MENU_BAR;
		}
		
		CPerspective cPerspective = SplashWindow.getPerspective();
		if (cPerspective != null && actionManager != null) {
			
			Object[] menuBarObjs = cPerspective.menuBars;
			if (menuBarObjs != null) {
				for (int i = 0; i < menuBarObjs.length; i++) {
					Object obj = menuBarObjs[i];
					if (obj instanceof CMenuRelation) {
						CMenuRelation relation=(CMenuRelation)obj;
						if(relation.managerId==null){
							relation.managerId=relation.label;
						}
						String path=relation.managerId;
						fillMenuBar(menuBar,relation,relation.managerId,path);
					}
				}
			}
			//����ȫ�ַ������
			ObjectPool.INSTANCE.putObject(new CAppMenuBarManager(menuBar,actionManager), GlobalServiceId.App_MenuBar_Manager);
		}
	}

	private  void fillMenuBar(IMenuManager menuBar, CMenuRelation cMenuBar,String managerId,String pathStr) {
		if (cMenuBar == null) {
			return;
		}
		MenuManager menuManager = new MenuManager(cMenuBar.label,managerId);
		Object[] actions = cMenuBar.actions;
		if (actions != null) {
			for (int a = 0; a < actions.length; a++) {
				Object action = actions[a];
				if (action instanceof CMenuRelation) {
					CMenuRelation relation=(CMenuRelation) action;
					if(relation.managerId==null){
						   relation.managerId=relation.label;
					}
					String path=pathStr+"/"+relation.managerId;
					fillMenuBar(menuManager,relation,relation.managerId,path);
				} else if (action instanceof String) {
					String id = (String) action;
					if (id.equals(JFaceConstant.Menu_Separator_Str)) {
						menuManager.add(new Separator());
					} else {
						IAction actionImp = actionManager.getAction(id);
						if (actionImp != null) {
							//����action��menu�е�path��Ϊdescription�ֶ�
							actionImp.setDescription(pathStr);
							menuManager.add(actionImp);
						}
					}
				}
			}// end for
			menuBar.add(menuManager);
		}
	}
	private  ICoolBarManager COOL_BAR=null;
    /**
     * ��ʼ��������
     * @param coolBar
     */
	public  CAppToolBarManager fillCoolBar(ICoolBarManager coolBar) {
		if(coolBar!=null){
			COOL_BAR=coolBar;
		}else{
			coolBar=COOL_BAR;
		}
		CAppToolBarManager toolBarManager=null;
		CPerspective cPerspective = SplashWindow.getPerspective();
		if (cPerspective != null && actionManager != null) {
			toolBarManager = new CAppToolBarManager(coolBar);
			// ��ӵ�ȫ�ַ���...
			ObjectPool.INSTANCE.putObject(toolBarManager,
					GlobalServiceId.App_ToolBar_Manager);
			toolBarManager.fillToolBar(cPerspective.toolBar);
		}
        return toolBarManager;
	}
	/**
	 * ��ʼ��״̬��������
	 * @param statusLineManager
	 */
    public  void initStatusLine(IStatusLineManager statusLineManager){
    	CStatusLineManager lineManager=new CStatusLineManager(statusLineManager);
    	ObjectPool.INSTANCE.putObject(lineManager,GlobalServiceId.App_StatusLine_Manager);
    	
    }
    private IWorkbenchWindowConfigurer winConfigurer;
	public  void preWindowOpen(IWorkbenchWindowConfigurer configurer) {
		// TODO ��Display����ע��Ϊȫ�ַ�����󣬲�������¼��������
		// System.out.println();
		this.winConfigurer=configurer;
		
		Display curDisp = Display.getCurrent();
		EventFilter.exeFilter(curDisp);
		
		if (configurer != null) {
			CPerspective cPerspective = SplashWindow.getPerspective();
			if (cPerspective != null){
				CWindowConfigurer cWinConfig = cPerspective.windowConfigurer;
				configurer.setShellStyle(cWinConfig.shellStyle);
				configurer.setInitialSize(new Point(cWinConfig.shellWidth,
						cWinConfig.shellHeight));
				
				configurer.setShowPerspectiveBar(cWinConfig.showPerspectiveBar);
				configurer.setShowMenuBar(cWinConfig.showMenuBar);
				configurer.setShowCoolBar(cWinConfig.showCoolBar);
				configurer.setShowStatusLine(cWinConfig.showStatusLine);
			}
		}
		
	}
    public static Shell Main_Shell=null;
	public  void postWindowOpen(Shell shell) {
		if(shell!=null){
		  Main_Shell=shell;
		}else{
		  shell=Main_Shell;
		}
		//���͸��ͼ������
		winConfigurer.getWindow().addPerspectiveListener(new IPerspectiveListener(){

			public void perspectiveActivated(IWorkbenchPage page,
					IPerspectiveDescriptor perspective) {
				IViewReference[] refs=page.getViewReferences();
			    
				page.addPartListener(new IPartListener2(){

					public void partActivated(IWorkbenchPartReference partRef) {
						
					}

					public void partBroughtToTop(IWorkbenchPartReference partRef) {
						
					}

					public void partClosed(IWorkbenchPartReference partRef) {
						
					}

					public void partDeactivated(IWorkbenchPartReference partRef) {
						
					}

					public void partHidden(IWorkbenchPartReference partRef) {
						
					}

					public void partInputChanged(IWorkbenchPartReference partRef) {
						
					}

					public void partOpened(IWorkbenchPartReference partRef) {
						IWorkbenchPart workbenchPart=partRef.getPart(true);
						//ֻ��IViewPart���й���
						if(!(workbenchPart instanceof IViewPart)){
						  return;
						}
						//��FlashViewPart��ͼ����Ҫ�ֶ�����FlashViewer����
					    if(!(workbenchPart instanceof FlashViewPart)){
							
						}else{
							 IViewPart viewPart=(IViewPart)workbenchPart;
							 //����ǰ�򿪵�viewPart����Ϣ��ӽ�ģ���Ӧ����	
							 FlashViewer flashViewer=FlashViewer.getViewerByParent(viewPart);//��ͼ��Ӧ��FlashViewer����
							 if(flashViewer!=null){
								 flashViewer.loadFlash();
								 CPageLayout page= CPageLayout.Instance;
								 page.addViewPartInfo(flashViewer.getModulePath(), flashViewer.getAppId());
							 }
					    }
					}

					public void partVisible(IWorkbenchPartReference partRef) {
						
					}
					
				});
			}
			public void perspectiveChanged(IWorkbenchPage page,
					IPerspectiveDescriptor perspective, String changeId) {
			}
           
        });
		
		
		ObjectPool.INSTANCE.putObject(shell, GlobalServiceId.Swt_Main_Win);
		CPerspective cPerspective = SplashWindow.getPerspective();
		if (cPerspective != null) {
			CWindowConfigurer cWinConfig = cPerspective.windowConfigurer;
			shell.setText(cWinConfig.shellTitle);
			ImageDescriptor imageDescriptor=
				ImageManager.getImageDescriptor(cWinConfig.shellImage);
			if(imageDescriptor!=null){
				shell.setImage(imageDescriptor.createImage());
			}
			if(cWinConfig.maximized){
				shell.setMaximized(true);
			}
		}
		// ȫ�ַ��� ����������ͼ������
		ViewManager viewManager=ViewManager.Instance;
		IWorkbenchWindow window=this.window;
		viewManager.initIWorkbenchPageListener(window.getActivePage());
		ObjectPool.INSTANCE.putObject(viewManager,
				GlobalServiceId.View_Manager);
		
		//Log.println("in postWindowOpen");
		//�������е�flash
		
		List<FlashViewer> flashViewers=FlashViewer.getViewers();
		for(int n=0;n<flashViewers.size();n++){
			FlashViewer flashViewer=flashViewers.get(n);
			flashViewer.loadFlash();
		}
		
	}
    public  void dispose(){
    	//�ر�������ͼ
    	CPageLayout.Instance.close();
    	//���ȫ�ֻ�����Ϣ
    	CApplication application=(CApplication)
    	   ObjectPool.INSTANCE.getObject(GlobalServiceId.Cur_Application);
    	application.dispose();
    	
    	//���actionManager
    	CActionManager actionManager=
    		(CActionManager)ObjectPool.INSTANCE.getObject(GlobalServiceId.CAction_Manager);
    	actionManager.dispose();
    	//��ղ˵�
    	CAppMenuBarManager menuBarManager=
    		(CAppMenuBarManager)ObjectPool.INSTANCE.getObject(GlobalServiceId.App_MenuBar_Manager);
    	menuBarManager.dispose();
    	//��չ�����
    	CAppToolBarManager  toolBarManager=
    		(CAppToolBarManager)ObjectPool.INSTANCE.getObject(GlobalServiceId.App_ToolBar_Manager);
    	toolBarManager.dispose();
    	
    }
    /**
     * ���򲼾�ϵͳ
     * @param installFolder ����İ�װĿ¼
     */
    public  void reStart(String installFolder){
    	
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
         
	}

}
