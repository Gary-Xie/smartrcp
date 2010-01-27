package cn.smartinvoke.smartrcp.gui;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.rcp.CToolBar;
import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;
import cn.smartinvoke.smartrcp.util.JFaceConstant;

public class CAppToolBarManager implements IServerObject{
	private ICoolBarManager coolBar;
	//���еĹ����������ֵ�
	//private Map<String,IToolBarManager> toolBarMap=new HashMap<String, IToolBarManager>();
	//��ǰ��ʾ�Ĺ�����
	public ToolBarManager curDisplayToolBar=null;
	//private List<IToolBarManager> visualableToolBar=new LinkedList<IToolBarManager>();
	public CAppToolBarManager(ICoolBarManager coolBar) {
	    this.coolBar=coolBar;
	    
	}
	public void addToolBar(CToolBar toolBar){
		if(toolBar!=null){
			Object[] actionIds=toolBar.actionIds;
			if(actionIds!=null){
			  ToolBarManager toolbarManager= new ToolBarManager(this.coolBar.getStyle()|toolBar.type);
			  for(int n=0;n<actionIds.length;n++){
			   Object idObj=actionIds[n];
			   if(idObj instanceof String){
				 String id=(String)idObj;
				 if (id.equals(JFaceConstant.Menu_Separator_Str)) {
					toolbarManager.add(new Separator());
				 }else{
			        toolbarManager.add(SmartRCPBuilder.actionManager.getAction((String)idObj));
				 }
			   }
			  }//end for
			  
			  curDisplayToolBar=toolbarManager;
			  //���ù�������ʾ����
			  this.coolBar.add(toolbarManager);
			}//end if
		}
	}
	/**
	 * ���ع������ϵ�ָ����
	 * @param toolBarId
	 */
	public void hideToolItem(String actionId){
		if(actionId!=null && curDisplayToolBar!=null){
			curDisplayToolBar.remove(actionId);
			this.coolBar.update(true);
		}
	}
	/**
	 * ��ʾ�������ϵ�ָ����
	 * @param actionId
	 */
	public void showToolItem(String actionId){
		if(actionId!=null && curDisplayToolBar!=null){
			curDisplayToolBar.add(SmartRCPBuilder.actionManager.getAction(actionId));
			this.coolBar.update(true);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
