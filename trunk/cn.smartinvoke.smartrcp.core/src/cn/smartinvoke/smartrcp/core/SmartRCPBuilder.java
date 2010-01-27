package cn.smartinvoke.smartrcp.core;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

import cn.smartinvoke.IServiceObjectCreator;
import cn.smartinvoke.TypeMapper;
import cn.smartinvoke.gui.ObjectPool;
import cn.smartinvoke.rcp.CMenuRelation;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.smartrcp.gui.CAppToolBarManager;
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
import cn.smartinvoke.util.ImageManager;
import cn.smartinvoke.util.Log;

public class SmartRCPBuilder {
	private static SplashWindow splash_win = SplashWindow.INSTANCE;

	private SmartRCPBuilder() {

	}

	/**
	 * ��ʼ��SmartRCP��ó�ʼ����Ϣ
	 */
	public static void init(IServiceObjectCreator objectCreator) {
		// TODO ����ĳ�ʼ��

		// ----------- ע��ȫ�ַ���
		ObjectPool objectPool = ObjectPool.INSTANCE;
		objectPool.objectCreator = objectCreator;
		objectPool.putObject(new CActionManager(),
				GlobalServiceId.CAction_Manager);
		objectPool.putObject(new FlashViewInvoker(),
				GlobalServiceId.FlashView_Invoker);
		// ����¼�ע��������
		EventRegister eventRegister = new EventRegister();
		objectPool.putObject(eventRegister, GlobalServiceId.Event_Register);
		// ��splash��������Ϊ�������flex����
		ObjectPool.INSTANCE.putObject(splash_win, GlobalServiceId.Splash_Win);
		// ------------����������Ϣ
		try {
			ConfigerLoader.init();
			String servicePacks = ConfigerLoader
					.getProperty(ConfigerLoader.key_export_package);
			TypeMapper.addServiceConfig(servicePacks);

			// ����������Ϣ
			CPerspective.init();
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

	public static void initImageRegistry(ImageRegistry imageRegistry) {
		// ---------����ͼ��ע����Ϣ
		ImageManager.init(imageRegistry);
	}

	public static CActionManager actionManager = null;

	/**
	 * ����action
	 */
	public static void createActions() {
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
							CAction cAction = (CAction) actionObj;
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
							actionManager.addAction(actionImpl);
						}
					}
				}
			}
		}
	}

	/**
	 * �����˵�
	 * 
	 * @param menuBar
	 */
	public static void fillMenuBar(IMenuManager menuBar) {
		CPerspective cPerspective = SplashWindow.getPerspective();
		if (cPerspective != null && actionManager != null) {
			Object[] menuBarObjs = cPerspective.menuBars;
			if (menuBarObjs != null) {
				for (int i = 0; i < menuBarObjs.length; i++) {
					Object obj = menuBarObjs[i];
					if (obj instanceof CMenuRelation) {
						fillMenuBar(menuBar,(CMenuRelation)obj);
					}
				}
			}
		}
	}

	private static void fillMenuBar(IMenuManager menuBar, CMenuRelation cMenuBar) {
		if (cMenuBar == null) {
			return;
		}
		MenuManager menuManager = new MenuManager(cMenuBar.label);
		Object[] actions = cMenuBar.actions;
		if (actions != null) {
			for (int a = 0; a < actions.length; a++) {
				Object action = actions[a];
				if (action instanceof CMenuRelation) {
					fillMenuBar(menuManager,(CMenuRelation)action);
				} else if (action instanceof String) {
					String id = (String) action;
					if (id.equals(JFaceConstant.Menu_Separator_Str)) {
						menuManager.add(new Separator());
					} else {
						IAction iAction = actionManager.getAction(id);
						if (iAction != null) {
							menuManager.add(iAction);
						}
					}
				}
			}// end for
			menuBar.add(menuManager);
		}
	}

	public static void fillCoolBar(ICoolBarManager coolBar) {
		CPerspective cPerspective = SplashWindow.getPerspective();
		if (cPerspective != null && actionManager != null) {
			CAppToolBarManager toolBarManager=new CAppToolBarManager(coolBar);
			//��ӵ�ȫ�ַ���...
			ObjectPool.INSTANCE.putObject(toolBarManager, GlobalServiceId.App_ToolBar_Manager);
			
			toolBarManager.addToolBar(cPerspective.toolBar);
		}
		
	}

	public static void preWindowOpen(IWorkbenchWindowConfigurer configurer) {
		// TODO ��Display����ע��Ϊȫ�ַ�����󣬲�������¼��������
		// System.out.println();
		Display curDisp = Display.getCurrent();
		// Log.println("shell obj="+configurer.getWindow().getShell());
		ObjectPool.INSTANCE.putObject(curDisp, GlobalServiceId.Swt_Display);
		EventFilter.exeFilter(curDisp);
		// ������ͼ������
		ObjectPool.INSTANCE.putObject(new ViewManager(),
				GlobalServiceId.View_Manager);
		if (configurer != null) {
			configurer.setInitialSize(new Point(800, 600));
			configurer.setShowMenuBar(true);
			configurer.setShowCoolBar(true);
			configurer.setShowFastViewBars(true);
			configurer.setShowStatusLine(true);
		}
	}

	public static void postWindowOpen(Shell shell) {
		Log.println(" postWindowOpen shell obj=" + shell);
		ObjectPool.INSTANCE.putObject(shell, GlobalServiceId.Swt_Main_Win);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
