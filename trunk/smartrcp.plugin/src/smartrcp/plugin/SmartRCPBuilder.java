package smartrcp.plugin;

import java.io.File;
import java.io.PrintWriter;

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
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import cn.smartinvoke.IServiceObjectCreator;
import cn.smartinvoke.TypeMapper;
import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.gui.ObjectPool;
import cn.smartinvoke.rcp.CPageLayout;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.rcp.CWindowConfigurer;
import cn.smartinvoke.smartrcp.CApplication;
import cn.smartinvoke.smartrcp.DebugServer;
import cn.smartinvoke.smartrcp.gui.FlashViewPart;
import cn.smartinvoke.smartrcp.gui.SplashWindow;
import cn.smartinvoke.smartrcp.gui.control.EventFilter;
import cn.smartinvoke.smartrcp.gui.control.EventRegister;
import cn.smartinvoke.smartrcp.gui.control.FlashViewInvoker;
import cn.smartinvoke.smartrcp.gui.control.GlobalServiceId;
import cn.smartinvoke.util.ConfigerLoader;
import cn.smartinvoke.util.ImageManager;
import cn.smartinvoke.util.Log;

public class SmartRCPBuilder {
	private static SplashWindow splash_win = SplashWindow.INSTANCE;
	private SmartRCPBuilder() {
        
	}
	/**
	 * ��ʼ��SmartRCP��ó�ʼ����Ϣ
	 */
	public static void init(final BundleContext context){//IServiceObjectCreator objectCreator) {
	    
		//----------------------
		//����������Ϣ
		try{
			ConfigerLoader.init();//���������ļ�
			CPerspective.init();//����swf����·��
			startBundles(context);//������չ��
		}catch(Exception e){
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
					throw new ClassNotFoundException(clsName);
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
	public static void startBundles(BundleContext context){
		 
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
		/*IViewReference[] refs=window.getActivePage().getViewReferences();
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
		memento.putString(key_config_path, ConfigerLoader.configPath);*/
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
	public static void initImageRegistry(ImageRegistry imageRegistry) {
		// ---------����ͼ��ע����Ϣ
		ImageManager.init(imageRegistry);
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
								 CPageLayout.Instance.addViewPartInfo(flashViewer.getModulePath(), flashViewer.getAppId());
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
	/**public static void postWindowOpen(Shell shell) {
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
		
	}*/
	
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
    	
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
          
	}

}
