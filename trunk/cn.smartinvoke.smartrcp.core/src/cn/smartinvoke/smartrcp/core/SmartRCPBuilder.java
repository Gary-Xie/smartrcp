package cn.smartinvoke.smartrcp.core;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import cn.smartinvoke.IServiceObjectCreator;
import cn.smartinvoke.TypeMapper;
import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.gui.ObjectPool;
import cn.smartinvoke.rcp.CMenuRelation;
import cn.smartinvoke.rcp.CPageLayout;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.rcp.CWindowConfigurer;
import cn.smartinvoke.smartrcp.CApplication;
import cn.smartinvoke.smartrcp.DebugServer;
import cn.smartinvoke.smartrcp.gui.CAppMenuBarManager;
import cn.smartinvoke.smartrcp.gui.CAppToolBarManager;
import cn.smartinvoke.smartrcp.gui.CStatusLineManager;
import cn.smartinvoke.smartrcp.gui.FlashViewPart;
import cn.smartinvoke.smartrcp.gui.SplashWindow;
import cn.smartinvoke.smartrcp.gui.control.CAction;
import cn.smartinvoke.smartrcp.gui.control.CActionManager;
import cn.smartinvoke.smartrcp.gui.control.EventFilter;
import cn.smartinvoke.smartrcp.gui.control.EventRegister;
import cn.smartinvoke.smartrcp.gui.control.FlashViewInvoker;
import cn.smartinvoke.smartrcp.gui.control.GlobalServiceId;
import cn.smartinvoke.smartrcp.gui.control.ViewManager;
import cn.smartinvoke.smartrcp.util.JFaceConstant;
import cn.smartinvoke.util.ConfigerLoader;
import cn.smartinvoke.util.HelpMethods;
import cn.smartinvoke.util.ImageManager;
import cn.smartinvoke.util.Log;

public class SmartRCPBuilder {
	private static SplashWindow splash_win = SplashWindow.INSTANCE;
    private static SmartRCPWindowAdvisor window_Advisor=new SmartRCPWindowAdvisor();
	private SmartRCPBuilder() {
        
	}
	
	public static void createWindowContents(Shell shell,IWorkbenchWindowConfigurer configurer) {
		window_Advisor.createWindowContents(shell, configurer);
	}
	
	public static void setShowToolbar(boolean visible) {
		window_Advisor.setShowToolbar(visible);
	}
	private static BundleContext context=null;
	/**
	 * ��ʼ��SmartRCP��ó�ʼ����Ϣ
	 */
	public static void init(final BundleContext context){//IServiceObjectCreator objectCreator) {
		//----------------------
		SmartRCPBuilder.context=context;
		//----------------------
		//����������Ϣ
		try{ConfigerLoader.init();}catch(Exception e){
			throw new RuntimeException();
			
		};
		
		
		// TODO ����ĳ�ʼ��
		ObjectPool objectPool = ObjectPool.INSTANCE;
		//��������ػ���
		objectPool.objectCreator = new IServiceObjectCreator(){

			public Class getClass(String clsName) throws ClassNotFoundException {
				Log.println("����������"+clsName+"�Ķ���");
				Class cls=null;
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
					throw new ClassNotFoundException();
				}
				return cls;
			}
 		};
 	    // ----------- ע��ȫ�ַ���
		objectPool.putObject(new CApplication(), GlobalServiceId.Cur_Application);
		objectPool.putObject(new FlashViewInvoker(),GlobalServiceId.FlashView_Invoker);
		// ����¼�ע��������
		EventRegister eventRegister = new EventRegister();
		objectPool.putObject(eventRegister, GlobalServiceId.Event_Register);
		
	}
	private static void startBundles(BundleContext context){
		  
		 // ����������Ϣ
		 try{
		 CPerspective.init();
		 }catch(Exception e){
			 throw new RuntimeException(e.getMessage());
		 }
		 String libPath=CPerspective.getRuntimeSwfFolder()+"/lib";
		 String logFile=CPerspective.getRuntimeSwfFolder()+"/log.txt";
		 PrintWriter logWriter=null;
		 File folder=new File(libPath);
		 if(!folder.exists()){
			 folder.mkdirs();
		 }
		 //���ز��
		 File[] bundleFiles=folder.listFiles();
		 if(bundleFiles!=null){
			 for(int i=0;i<bundleFiles.length;i++){
				 String path="file:"+bundleFiles[i].getAbsolutePath();
				 Log.println("load bundle:"+path);
				 try{
				  Bundle bundle=context.installBundle(path);
				  bundle.start();
				 }catch(Exception e){
				   //��¼������־
				   if(logWriter==null){
					   File log=new File(logFile);
					   if(!log.exists()){
						  try{ log.createNewFile();}catch(Exception e1){};
					   }
					   try{logWriter=new PrintWriter(log);}catch(Exception e1){};
				   }
				   e.printStackTrace(logWriter); 
				 }
			 }
		 }
		 //�رմ�����־�ļ�
		 if(logWriter!=null){
			 logWriter.flush();
			 logWriter.close();
		 }
	}
	/**
	 * ��Splash���ڻ�ó�ʼ����Ϣ����ʼ������
	 */
	public static void initWindows(){
		// ------------����������Ϣ
		// ��splash��������Ϊ�������flex����
		ObjectPool.INSTANCE.putObject(splash_win, GlobalServiceId.Splash_Win);
		try {
			
			String servicePacks = ConfigerLoader
					.getProperty(ConfigerLoader.key_export_package);
			TypeMapper.addServiceConfig(servicePacks);
			
			// -----------����splash���ڣ�����flex��splash��Ϣ
			try {
				splash_win.open();
			} catch (Throwable e) {
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
	public static void initWorkbench(IWorkbenchConfigurer configurer){
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
	public static void saveWorkbenchState(IMemento memento){
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
	public static void restoreWorkbenchState(IMemento memento){
		//����start.ini�ļ�
		ConfigerLoader.configPath=memento.getString(key_config_path);
		String val=memento.getString(key_workbench_state);
		if(val!=null){
			CPageLayout pageLayout=SplashWindow.getPerspective().page;
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
	public static void initImageRegistry(ImageRegistry imageRegistry) {
		// ---------����ͼ��ע����Ϣ
		ImageManager.init(imageRegistry);
	}

	public static CActionManager actionManager = null;
	private static ActionBarAdvisor actionBarAdvisor;
    public static IWorkbenchWindow window;
	/**
	 * ����action
	 */
	public static void createActions(ActionBarAdvisor actionBarAdvisor,IWorkbenchWindow window){
		if(actionBarAdvisor!=null){
			SmartRCPBuilder.actionBarAdvisor=actionBarAdvisor;
		}else{
			actionBarAdvisor=SmartRCPBuilder.actionBarAdvisor;
		}
		if(window!=null){
		  SmartRCPBuilder.window=window;
		}else{
		  window=SmartRCPBuilder.window;
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
    private static IMenuManager MENU_BAR;
	/**
	 * �����˵�
	 * 
	 * @param menuBar
	 */
	public static void fillMenuBar(IMenuManager menuBar) {
		if(menuBar!=null){//��һ������
		  SmartRCPBuilder.MENU_BAR=menuBar;
		}else{//�ڶ�������
		  menuBar=SmartRCPBuilder.MENU_BAR;
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

	private static void fillMenuBar(IMenuManager menuBar, CMenuRelation cMenuBar,String managerId,String pathStr) {
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
	private static ICoolBarManager COOL_BAR=null;
    /**
     * ��ʼ��������
     * @param coolBar
     */
	public static void fillCoolBar(ICoolBarManager coolBar) {
		if(coolBar!=null){
			COOL_BAR=coolBar;
		}else{
			coolBar=COOL_BAR;
		}
		
		CPerspective cPerspective = SplashWindow.getPerspective();
		if (cPerspective != null && actionManager != null) {
			CAppToolBarManager toolBarManager = new CAppToolBarManager(coolBar);
			// ��ӵ�ȫ�ַ���...
			ObjectPool.INSTANCE.putObject(toolBarManager,
					GlobalServiceId.App_ToolBar_Manager);
			toolBarManager.fillToolBar(cPerspective.toolBar);
		}

	}
	/**
	 * ��ʼ��״̬��������
	 * @param statusLineManager
	 */
    public static void initStatusLine(IStatusLineManager statusLineManager){
    	CStatusLineManager lineManager=new CStatusLineManager(statusLineManager);
    	ObjectPool.INSTANCE.putObject(lineManager,GlobalServiceId.App_StatusLine_Manager);
    	
    }
	public static void preWindowOpen(IWorkbenchWindowConfigurer configurer) {
		// TODO ��Display����ע��Ϊȫ�ַ�����󣬲�������¼��������
		// System.out.println();
		Display curDisp = Display.getCurrent();
		ObjectPool.INSTANCE.putObject(curDisp, GlobalServiceId.Swt_Display);
		EventFilter.exeFilter(curDisp);
		
		if (configurer != null) {
			CPerspective cPerspective = SplashWindow.getPerspective();
			if (cPerspective != null) {
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
		//���͸��ͼ������
		configurer.getWindow().addPerspectiveListener(new IPerspectiveListener(){

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
								 SplashWindow.getPerspective().
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
	}
    public static Shell Main_Shell=null;
	public static void postWindowOpen(Shell shell) {
		if(shell!=null){
		  Main_Shell=shell;
		}else{
		  shell=Main_Shell;
		}
		
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
			
		}
		// ȫ�ַ��� ����������ͼ������
		ViewManager viewManager=new ViewManager();
		IWorkbenchWindow window=SmartRCPBuilder.window;
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
		openDebugServer();
		
		//���ؿ�
		startBundles(context);
	}
	
	/**
	 * ��debug���񣬽���flexBuilder����������Ѿ��򿪾�ֱ�ӷ���
	 */
	private static boolean isStart=false;
    private static void openDebugServer(){
      if(!isStart){
       String debugStr=ConfigerLoader.getProperty(ConfigerLoader.key_debug);
       if(debugStr!=null){
    	  debugStr=debugStr.trim();
    	  if(debugStr.equals("true")){
    			  DebugServer.start();
    	  }
       }
       
       isStart=true;
      }
    }
    
    public static void dispose(){
    	//�ر�������ͼ
    	SplashWindow.getPerspective().page.close();
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
     * @param configPath
     */
    public static void reStart(String configPath){
    	/*if(configPath!=null){
    	 String loadAppConfig=HelpMethods.getPluginFolder()+"/loadApp.ini";
    	 FileWriter fileWriter=null;
    	 try{
    	   //д�����ļ�
    	   fileWriter=new FileWriter(loadAppConfig);
    	   fileWriter.write(configPath);
    	   fileWriter.flush();
    	   fileWriter.close();
    	   //��������ϵͳ
    	   PlatformUI.getWorkbench().restart();
    	 }catch(Exception e){
    	   throw new RuntimeException(e.getMessage()); 
    	 }finally{
    	    if(fileWriter!=null){
    	    	try{fileWriter.close();}catch(Exception e){};
    	    }
    	 }*/
    	 File configFile=new File(configPath);
    	 if(configFile.exists()){
    		
    		 try{
    		   
    		   //���ü��س��������·��
    		   ConfigerLoader.init(configPath);
    		   CPerspective.init();
    		   //��С�������ͷ�flex�ڴ���Դ
    		   Main_Shell.setMinimized(true);
    		   
    		   //�򿪼��ش���Splashwin
    		   SmartRCPBuilder.initWindows();
    		   //���´�͸��ͼ
    		   String perspectiveId="cn.smartinvoke.smartrcp.core.perspective";
    		   
    		   window.getWorkbench().showPerspective(perspectiveId, window);
    		   
    		   SmartRCPBuilder.createActions(null,null);
    		   SmartRCPBuilder.fillMenuBar(null);
    		   SmartRCPBuilder.fillCoolBar(null);
    		   SmartRCPBuilder.postWindowOpen(null);
    		   
    		   //��ԭ����
    		   Main_Shell.setMinimized(false);
    		 }catch(Exception e){
    			 Shell mainShell=Display.getCurrent().getActiveShell();
    			 MessageDialog.openError(mainShell, "����", "��������������Ϣ���£�\n"+e.getMessage());
    		 }
    	}
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
          
	}

}
