package cn.smartinvoke.smartrcp.gui.module;
/**
 * ��ͼ�Ĳ˵�bean���󣬰���toolBar,MenuBar,��contextMenu�˵�
 * @author pengzhen
 *
 */
public class ViewPartMenus {
    private Object[]  toolBarActions=null;
    private Object[]  menuBarActions=null;
    private Object[]  contextMenuActions=null;
	public ViewPartMenus() {
		
	}
	public Object[] getToolBarActions() {
		return toolBarActions;
	}
	public void setToolBarActions(Object[] toolBarActions) {
		this.toolBarActions = toolBarActions;
	}
	public Object[] getMenuBarActions() {
		return menuBarActions;
	}
	public void setMenuBarActions(Object[] menuBarActions) {
		this.menuBarActions = menuBarActions;
	}
	public Object[] getContextMenuActions() {
		return contextMenuActions;
	}
	public void setContextMenuActions(Object[] contextMenuActions) {
		this.contextMenuActions = contextMenuActions;
	}
    
}
