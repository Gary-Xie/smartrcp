package cn.smartinvoke.smartrcp.gui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
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
	    curDisplayToolBar=new ToolBarManager(coolBar.getStyle());
		//���ù�������ʾ����
		this.coolBar.add(curDisplayToolBar);
	}
	public void addToolBar(CToolBar toolBar){
		if(toolBar!=null){
			Object[] actionIds=toolBar.actionIds;
			if(actionIds!=null){
			  curDisplayToolBar= new ToolBarManager(this.coolBar.getStyle()|toolBar.type);
			  for(int n=0;n<actionIds.length;n++){
			   Object idObj=actionIds[n];
			   if(idObj instanceof String){
				 String id=(String)idObj;
				 if (id.equals(JFaceConstant.Menu_Separator_Str)) {
					  curDisplayToolBar.add(new Separator());
				 }else{
					IAction action=SmartRCPBuilder.actionManager.getAction(id);
					if(action!=null){
					  curDisplayToolBar.add(action);
					}
				 }
			   }
			  }//end for
			  curDisplayToolBar.update(true);
			  this.coolBar.add(curDisplayToolBar);
			}//end if
		}
	}
	/**
	 * ���ع������ϵ�ָ����
	 * @param toolBarId
	 */
	public void removeItem(String actionId){
		if(actionId!=null && curDisplayToolBar!=null){
			curDisplayToolBar.remove(actionId);
			this.coolBar.update(true);
		}
	}
	public void removeAll(){
		curDisplayToolBar.removeAll();
		this.coolBar.update(true);
	}
	/**
	 * ��ʾ�������ϵ�ָ����
	 * @param actionId
	 */
	public void insertItem(String actionId){
		if(actionId!=null && curDisplayToolBar!=null){
			IAction action=SmartRCPBuilder.actionManager.getAction(actionId);
			if(action!=null){
			 curDisplayToolBar.add(action);
			 this.coolBar.update(true);
			}
		}
	}
	public void insertBefore(String beforeId,String actionId){
	   if(beforeId!=null && actionId!=null){
		 IContributionItem item=this.curDisplayToolBar.find(beforeId);
		 IAction action=SmartRCPBuilder.actionManager.getAction(actionId);
		 if(action!=null){
		   	if(item==null){
		   		curDisplayToolBar.add(action);
		   	}else{
		   		curDisplayToolBar.insertBefore(beforeId, action);
		   	}
		   	this.coolBar.update(true);
		 }
	   }
	}
    public void insertAfter(String afterId,String actionId){
    	if(afterId!=null && actionId!=null){
   		 IContributionItem item=this.curDisplayToolBar.find(afterId);
   		 IAction action=SmartRCPBuilder.actionManager.getAction(actionId);
   		 if(action!=null){
   		   	if(item==null){
   		   		curDisplayToolBar.add(action);
   		   	}else{
   		   		curDisplayToolBar.insertAfter(afterId, action);
   		   	}
   		   	this.coolBar.update(true);
   		 }
   	   }
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
