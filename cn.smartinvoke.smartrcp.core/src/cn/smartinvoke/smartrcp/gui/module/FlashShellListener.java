package cn.smartinvoke.smartrcp.gui.module;

import java.util.List;

import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.smartrcp.gui.FlashShell;

public class FlashShellListener implements ShellListener ,Listener{
    private FlashShell flashViewer;
	public FlashShellListener(FlashShell flashViewer) {
		this.flashViewer=flashViewer;
	}
	public void shellActivated(ShellEvent e) {
		//���õ�ǰ��ý����FlashViwer
		FlashViewer.curFlashViewer=flashViewer.getFlashViewer();
	   	e.doit=this.fireEvent(CShellEvent.Event_Activated,e);	
	}
	
	public void shellDeactivated(ShellEvent e) {
		e.doit=this.fireEvent(CShellEvent.Event_Deactivated,e);	
	}
	public void shellDeiconified(ShellEvent e) {
		e.doit=this.fireEvent(CShellEvent.Event_Deiconified,e);	
	}
	public void shellIconified(ShellEvent e) {
	    e.doit=this.fireEvent(CShellEvent.Event_Iconified,e);	
	}
    private boolean fireEvent(int type,ShellEvent e){
    	List<CEventBean> typeListeners=flashViewer.listeners[type];
    	for(int n=0;n<typeListeners.size();n++){
    		CEventBean eventBean=typeListeners.get(n);
    		CShellEvent evt=new CShellEvent();
    		evt.time=e.time;
    		Object ret=eventBean.fireResEvent(evt);
    		if(ret!=null && ret instanceof Boolean){
    			return (Boolean)ret;
    		}
    	}
    	return true;
    }
	public void handleEvent(Event e) {
	    
	}
	
	public void shellClosed(ShellEvent e) {
		
		this.fireEvent(CShellEvent.Event_Closed,e);
		List<CEventBean> typeListeners=flashViewer.listeners[CShellEvent.Event_Closed];
		//flexû����Ӽ���������ִ��Ĭ�ϲ���
		if(typeListeners.size()==0){
		  //���ö�Ӧflex���������Դ
		  this.flashViewer.getFlashViewer().flexAppExist();
		  //�������赽��С�������ڴ�
		  flashViewer.getShell().setMinimized(true);
	   	  e.doit=true; 
		}else{//�ȴ�flex���첽����
		  e.doit=false;
		}
	}

}
