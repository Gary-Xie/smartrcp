package cn.smartinvoke.smartrcp.gui;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.application.ActionBarAdvisor;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.rcp.CToolBar;
import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;
import cn.smartinvoke.smartrcp.gui.control.CAction;
import cn.smartinvoke.smartrcp.gui.control.CActionImpl;
import cn.smartinvoke.smartrcp.gui.control.CViewPartActionImpl;
import cn.smartinvoke.smartrcp.gui.module.CActionEntity;
import cn.smartinvoke.smartrcp.gui.module.CEventBean;
import cn.smartinvoke.smartrcp.gui.module.CObservable;
import cn.smartinvoke.smartrcp.util.JFaceConstant;
/**
 *���ฺ�����Flash viewPart�Ĺ������������˵����ֵ��߼�
 *
 */
public class ViewPartActionBar extends ActionBarBase implements IServerObject {
    //private IActionBars actionBars;
    private IToolBarManager toolBarManager;
    private IMenuManager menuManager;
	public ViewPartActionBar(IActionBars actionBars) {
		this.toolBarManager=actionBars.getToolBarManager();
		this.menuManager=actionBars.getMenuManager();
	}
	
	//-----------��������drop down menu
	/**
	 * ���һaction��Ϣ����
	 */
	public void addAction(CAction cAction){
		CViewPartActionImpl actionImpl = null;
		int declType = cAction.getType();
		if (declType == IAction.AS_CHECK_BOX) {
			actionImpl = new CViewPartActionImpl(cAction.getText(),
					IAction.AS_CHECK_BOX, cAction.isChecked());
		} else if (declType == IAction.AS_RADIO_BUTTON) {
			actionImpl = new CViewPartActionImpl(cAction.getText(),
					IAction.AS_RADIO_BUTTON, cAction.isChecked());
		} else if (declType == -1) {
			actionImpl = new CViewPartActionImpl(cAction.getText());
		} else {
			actionImpl = new CViewPartActionImpl(cAction.getText(), declType);
		}

		actionImpl.init(cAction,this);
		this.addAction(actionImpl);
	}
	/**
	 * ��չ�����
	 */
	public void clearToolBar(){
		this.toolBarManager.removeAll();
		this.toolBarManager.update(true);
	}
	/**
	 * ���ù�����
	 * @param cToolBar ������action��id��������
	 */
	public void fillToolBar(CToolBar toolBar){
		if (toolBar != null) {
			//�������
			this.toolBarManager.removeAll();
			List<String> actionIds = toolBar.actionIds;
			if (actionIds != null) {
				//IToolBarManager curDisplayToolBar =toolBarManager;
				for (int n = 0; n < actionIds.size(); n++) {
					Object idObj = actionIds.get(n);
					if (idObj instanceof String) {
						String id = (String) idObj;
						if (id.equals(JFaceConstant.Menu_Separator_Str)) {
							//curDisplayToolBar = this.createToolBar(toolBar.type);
							toolBarManager.add(new Separator());
						} else {
							IAction action =this.getAction(id);
							if (action != null) {
								toolBarManager.add(action);
							}
						}
					}
				}// end for
			}// end if
			this.toolBarManager.update(true);
		}
	}
	/**
	 * ���ô���ͼ�������˵�
	 * @param actionId  �˵���Ӧ��action��Id����id��Ӧ��actionһ��Ҫͨ������addAction��������ע��
	 * @param path  �˵���·����null��ʾ�����˵�
	 */
	public void addDropDownMenu(String actionId,String path){
		if (actionId == null) {
			return;
		}else{
			IMenuManager menuManager=this.createMenuManager(path);
			actionId=actionId.trim();
			if(actionId.equals(JFaceConstant.Menu_Separator_Str)){
				menuManager.add(new Separator());
			}else{
				IAction action=this.getAction(actionId);
				action.setDescription(path);
				if(action!=null){
					menuManager.add(action);
				}
			}
			this.menuManager.updateAll(true);
		}
	}
	private IMenuManager createMenuManager(String path){
		IMenuManager ret=this.menuManager;
		if(path!=null){
			path=path.trim();
			if(path.length()!=0){
			 String[] paths=path.split("/");
			 IMenuManager parentManager=this.menuManager;
			 for(int n=0;n<paths.length;n++){
				 IMenuManager manager=new MenuManager(paths[n],paths[n]);
				 parentManager.add(manager);
				 
				 parentManager=manager;
			 }
			 ret=parentManager;
			}
		}
		return ret;
	}
	/**
	 * ɾ��ָ��path�µĶ�ӦactionId�Ĳ˵���
	 * @param actionId
	 * @param path
	 */
	public void removeDropDownMenu(String actionId,String path){
		if(actionId!=null){
			IMenuManager delMenuManager=this.menuManager;
			if(path!=null){
				delMenuManager=this.menuManager.findMenuUsingPath(path);
			}
			if(delMenuManager!=null){
				delMenuManager.remove(actionId);
				this.menuManager.updateAll(true);
			}
		}
	}
	/**
	 * ��������б�˵�
	 */
	public void clearDropDownMenus(){
		this.menuManager.removeAll();
		this.menuManager.updateAll(true);
	}
	public void dispose() {
		
	}
	public void removeAction(String actionId) {
		if (actionId != null) {
			//ɾ����ǰmanager�е�����
			this.actionMap.remove(actionId);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str="a/";
		String[] items=str.split("/");
		System.out.println(items);
	}

}
