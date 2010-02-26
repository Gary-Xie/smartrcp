package cn.smartinvoke.smartrcp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.CPerspective;
/**
 * ȫ�ַ����࣬���ฺ�����ǰӦ�ó�����Ϣ�����ṩ�����˳���һЩʵ�÷���
 * @author pengzhen
 *
 */
public class CApplication implements IServerObject {
    private Map<String,Object> dataMap=new HashMap<String, Object>();
	public CApplication() {
	   	
	}
	/**
	 * ���ص�ǰ��ý����FlashViewer����
	 * @return
	 */
	public FlashViewer getActiveFlashViewer(){
		return FlashViewer.curFlashViewer;
	}
	/**
	 * ��ó���İ�װ·��
	 * @return
	 */
	public String getBaseFolder(){ 
		return CPerspective.getRuntimeSwfFolder();
	}
	/**
	 * �˳�����
	 */
	public void exit(){
		try{
		 IWorkbench workbench=PlatformUI.getWorkbench();
		 if(workbench!=null){
			workbench.close();
		 }else{
			 Display display=Display.getCurrent();
			 if(display!=null){
				 Shell activeShell=display.getActiveShell();
				 if(activeShell!=null){
					 activeShell.close();
				 }
				 display.close();
				 display.dispose();
			 }
		 }
		}catch(Exception e){
			Display display=Display.getCurrent();
			if(display!=null){
			 display.close();
			 display.dispose();
			}
		}finally{
			Runtime.getRuntime().exit(0);
		}
	}
	public void restart(){
		PlatformUI.getWorkbench().restart();
	}
	public String getRuntimeSwfPath(){
		return CPerspective.getRuntimeSWFPath();
	}
	private String debugPath;
	public void setDebugPath(String path){
		this.debugPath=path;
	}
	public String getDebugPath(){
		return this.debugPath;
	}
	public void setData(String key,Object data){
	   if(key!=null){
		 this.dataMap.put(key, data);
	   }
	}
	public void removeData(String key){
		if(key!=null){
			this.dataMap.remove(key);
		}
	}
	public Object getData(String key){
		//Object ret=null;
		if(key!=null){
			return this.dataMap.get(key);
		}else{
			return null;
		}
		//return ret;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	   	File f=null;
	   	f.list();
	   	
	}
	public void dispose() {
	   
	}
	//--��ɫ�Ի���
	
	//---------------------���öԻ���
	public  void openError(Shell mainShell,String title, String message) {
		//Shell mainShell=(Shell)ObjectPool.INSTANCE.getObject(GlobalServiceId.Swt_Main_Win);
		mainShell=Display.getCurrent().getActiveShell();
		MessageDialog.openError(mainShell, title, message);
	}
	public  void openInformation(Shell mainShell,String title, String message) {
		//Shell mainShell=(Shell)ObjectPool.INSTANCE.getObject(GlobalServiceId.Swt_Main_Win);
		mainShell=Display.getCurrent().getActiveShell();
		MessageDialog.openInformation(mainShell, title, message);
	}
	public  void openWarning(Shell mainShell,String title, String message) {
		//Shell mainShell=(Shell)ObjectPool.INSTANCE.getObject(GlobalServiceId.Swt_Main_Win);
		mainShell=Display.getCurrent().getActiveShell();
		MessageDialog.openWarning(mainShell, title, message);
	}
}
