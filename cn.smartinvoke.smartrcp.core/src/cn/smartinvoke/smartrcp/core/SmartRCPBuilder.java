package cn.smartinvoke.smartrcp.core;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

import cn.smartinvoke.IServiceObjectCreator;
import cn.smartinvoke.TypeMapper;
import cn.smartinvoke.gui.ObjectPool;
import cn.smartinvoke.rcp.CMenuBar;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.smartrcp.gui.SplashWindow;
import cn.smartinvoke.smartrcp.gui.control.CAction;
import cn.smartinvoke.smartrcp.gui.control.CActionImpl;
import cn.smartinvoke.smartrcp.gui.control.CActionManager;
import cn.smartinvoke.smartrcp.gui.control.EventFilter;
import cn.smartinvoke.smartrcp.gui.control.EventRegister;
import cn.smartinvoke.smartrcp.gui.control.FlashViewInvoker;
import cn.smartinvoke.smartrcp.gui.control.GlobalServiceId;
import cn.smartinvoke.smartrcp.gui.control.ViewManager;
import cn.smartinvoke.smartrcp.util.JFaceConstant;
import cn.smartinvoke.util.ConfigerLoader;

public class SmartRCPBuilder {
    private static SplashWindow splash_win=SplashWindow.INSTANCE;
	private SmartRCPBuilder() {
		
	}
	/**
	 * ��ʼ��SmartRCP��ó�ʼ����Ϣ
	 */
    public static void init(IServiceObjectCreator objectCreator){
    	// TODO ����ĳ�ʼ��
		// ע��ȫ�ַ���
		ObjectPool objectPool = ObjectPool.INSTANCE;
		objectPool.objectCreator=objectCreator;
		objectPool.putObject(new CActionManager(),
				GlobalServiceId.CAction_Manager);
		objectPool.putObject(new FlashViewInvoker(),
				GlobalServiceId.FlashView_Invoker);
		// ����¼�ע��������
		EventRegister eventRegister = new EventRegister();
		objectPool.putObject(eventRegister, GlobalServiceId.Event_Register);
		// ��splash��������Ϊ�������flex����
		ObjectPool.INSTANCE.putObject(splash_win, GlobalServiceId.Splash_Win);
		// ���÷�����
		try {
			ConfigerLoader.init();
			String servicePacks = ConfigerLoader
					.getProperty(ConfigerLoader.key_export_package);
			TypeMapper.addServiceConfig(servicePacks);
			
			// ����������Ϣ
			CPerspective.init();
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
    private static CActionManager actionManager = null;
    /**
     *����action
     */
    public static void createActions(){
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
							actionManager.addAction(new CActionImpl(
									(CAction) actionObj));
						}
					}
				}
			}
		}
    }
    /**
     * �����˵�
     * @param menuBar
     */
    public static void createMenuBar(IMenuManager menuBar){
    	CPerspective cPerspective = SplashWindow.getPerspective();
		if (cPerspective != null &&actionManager!=null) {
			Object[] menuBarObjs=cPerspective.menuBars;
			if(menuBarObjs!=null){
				for(int i=0;i<menuBarObjs.length;i++){
				   Object obj=menuBarObjs[i];
				   if(obj instanceof CMenuBar){
					   CMenuBar cMenuBar=(CMenuBar)obj;
					   MenuManager menuManager=new MenuManager(cMenuBar.label);
					   Object[] idArr=cMenuBar.actionIds;
					   if(idArr!=null){
					    for(int a=0;a<idArr.length;a++){
					     String id=idArr[a].toString();
					     if(id.equals(JFaceConstant.Menu_Separator_Str)){
					    	 menuManager.add(new Separator());
					     }else{
					      IAction action=actionManager.getAction(id);
					      if(action!=null){
					        menuManager.add(action);
					      }
					     }
					    }
					    menuBar.add(menuManager);
					   }
				   }
				}
			}
		}
    }
    public static void preWindowOpen( IWorkbenchWindowConfigurer configurer){
    	//TODO ��Display����ע��Ϊȫ�ַ�����󣬲�������¼��������
    	//System.out.println();
    	Display curDisp=Display.getCurrent();
    	ObjectPool.INSTANCE.putObject(curDisp, GlobalServiceId.Swt_Display);
    	EventFilter.exeFilter(curDisp);
    	//������ͼ������
    	ObjectPool.INSTANCE.putObject(new ViewManager(),GlobalServiceId.View_Manager);
        if(configurer!=null){
        configurer.setInitialSize(new Point(800, 600));
        configurer.setShowMenuBar(true);
        configurer.setShowCoolBar(false);
        configurer.setShowFastViewBars(false);
        //configurer.setShowPerspectiveBar(true);
        configurer.setShowStatusLine(false);
        }
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
