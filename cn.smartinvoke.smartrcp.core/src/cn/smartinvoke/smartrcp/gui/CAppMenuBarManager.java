package cn.smartinvoke.smartrcp.gui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.rcp.CMenuRelation;
import cn.smartinvoke.smartrcp.gui.control.CActionManager;
import cn.smartinvoke.smartrcp.gui.control.CNativeActionImpl;
import cn.smartinvoke.smartrcp.util.JFaceConstant;
import cn.smartinvoke.util.Log;

public class CAppMenuBarManager implements IServerObject{
	private IMenuManager root = null;
	private CActionManager actionManager;

	public CAppMenuBarManager(IMenuManager root, CActionManager actionManager) {
		this.root = root;
		this.actionManager = actionManager;
	}
	/**
	 * ����path·���µ����в˵����÷�����ı�����˵��Ĳ���
	 * @param path
	 */
	public void initMenuBar(Object[] menuRelations){
		if(menuRelations!=null){
		   this.root.removeAll();//�������
		   
		   for (int i = 0; i < menuRelations.length; i++) {
			   Object menuRelationObj=menuRelations[i];
			   if(menuRelationObj!=null && (menuRelationObj instanceof CMenuRelation)){
				   setMenu((CMenuRelation)menuRelationObj);
			   }
		   }
		   
		   //������ʾ
		   this.root.updateAll(true);
		}
	}
	
	/**
	 * ���ò˵���
	 * @param label
	 * @param menuBar
	 */
	public void setMenu(CMenuRelation menuRelation){
		this.initMenu(this.root, menuRelation);
		//������ʾ
		this.root.updateAll(true);
	}
	/**
	 * ɾ��ĳһ�˵���
	 * @param path
	 */
	public void removeMenu(String path){
		if(path!=null){
			IMenuManager menuManager=this.root.findMenuUsingPath(path);
			if(menuManager!=null){
			   menuManager.removeAll();
			}
			
			this.root.updateAll(true);
		}
	}
	/**
	 * ����CMenuRelation��ʼ��menuManager�˵�
	 * @param menuManager
	 * @param menuRelation
	 */
	private  void initMenu(IMenuManager	parentMenuManager,CMenuRelation menuRelation){
		if(menuRelation==null){
			return;
		}else
		if(menuRelation.label==null || menuRelation.label.trim().length()==0){//�˵����ֲ���Ϊnull
			return;
		}
		IMenuManager menuManager=parentMenuManager.findMenuUsingPath(menuRelation.label);
		if(menuManager==null){
			menuManager=new MenuManager(menuRelation.label, menuRelation.label);
			parentMenuManager.add(menuManager);
		}else{
			menuManager.removeAll();//ȷ�����
		}
		
		Object[] actions = menuRelation.actions;
		if (actions != null) {
			for (int a = 0; a < actions.length; a++) {
				Object action = actions[a];
				if(action==null){
					continue;
				}
				if (action instanceof CMenuRelation) {
					CMenuRelation relation=(CMenuRelation) action;
					if(relation.label==null || relation.label.trim().length()==0){
						continue;
					}else{
						initMenu(menuManager, relation);
					}
				} else if (action instanceof String) {
					String id = (String) action;
					if (id.equals(JFaceConstant.Menu_Separator_Str)) {
						menuManager.add(new Separator());
					} else {
						IAction iAction = this.actionManager.getAction(id);
						if (iAction != null) {
							//Log.println("add action="+iAction);
							menuManager.add(iAction);
						}
					}
				}
			}// end for
		}
		//������ʾ
		menuManager.updateAll(true);
	}
	/**
	 * ������в˵�
	 */
	public void removeAll(){
		this.root.removeAll();
		this.root.updateAll(true);
	}
	/**
	 * ��path�˵������actionIdָ���˵���
	 * @param actionId
	 * @param path
	 */
	public void addMenuItem(String actionId, String path) {
		if (actionId == null) {
			return;
		}
		if (path == null) {
			path = "";
		}
		path = path.trim();

		IMenuManager menuManager = null;
		if (path.length() == 0) {
			menuManager = root;
		} else {
			menuManager = this.root.findMenuUsingPath(path);
		}
		if (menuManager != null) {
			IAction actionImpl = this.actionManager.getAction(actionId);
			if (actionImpl != null) {
				//path�洢��description��
				actionImpl.setDescription(path);
				menuManager.add(actionImpl);
				menuManager.update();
				this.root.updateAll(true);
			}
		}
	}

	public void removeMenuItem(String actionId, String path) {
		if (actionId == null) {
			return;
		}
		if (path == null) {
			path = "";
		}
		path = path.trim();
		IMenuManager menuManager = null;
		if (path.length() == 0) {
			menuManager = root;
		} else {
			menuManager = this.root.findMenuUsingPath(path);
		}
		if (menuManager != null) {
			IAction actionImpl = actionManager.getAction(actionId);
			if (actionImpl != null) {
				menuManager.remove(actionId);
				//menuManager.update();
				this.root.updateAll(true);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
	public void dispose() {
		//�������
		root.removeAll();
		root.updateAll(true);
	}

}
