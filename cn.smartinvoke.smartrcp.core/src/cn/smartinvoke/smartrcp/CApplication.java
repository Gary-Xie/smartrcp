package cn.smartinvoke.smartrcp;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.ErrorMessages;
import cn.smartinvoke.smartrcp.gui.SplashWindow;
import cn.smartinvoke.smartrcp.util.JFaceHelpMethod;
import cn.smartinvoke.smartrcp.util.UIHelpMethod;
import cn.smartinvoke.util.ConfigerLoader;
import org.eclipse.osgi.service.datalocation.Location;
/**
 * ȫ�ַ����࣬���ฺ�����ǰӦ�ó�����Ϣ�����ṩ�����˳���һЩʵ�÷���
 * @author pengzhen
 *
 */
public class CApplication implements IServerObject {
    private Map<String,Object> dataMap=new HashMap<String, Object>();
    //��׼bundle
    private static List<Bundle> standardBundles=null;
    //��ǰsmartrcpӦ��
    private static SmartRCPBundle curApp=null;
    //��ʵ��
    public static final CApplication Instance=new CApplication();
	private CApplication() {
	     	
	}
	
	public void debugAsShell(boolean set){
		//if(set){
			DebugServer.isDebugAsShell=set;
		//}
	}
	/**
	 * ��ȡָ���������в���
	 * @param arg
	 * @return
	 */
	public String getAppArg(String arg){
	   return ConfigerLoader.getAppArg(arg);	
	}
	/**
	 * ��ó���װĿ¼
	 * @return
	 */
	public String getInstallLocation(){
		Location location = Platform.getInstallLocation();  
		if (location != null) { 
			URL url = location.getURL();  
			String path = url.getPath();
			if(path.charAt(0)=='/'){
				return path.substring(1);
			}else{
				return path;
			}
	    }
		return null;
	}
	
	public static SmartRCPBundle getCurApp() {
		return curApp;
	}

	public static void setCurApp(SmartRCPBundle curApp) {
		CApplication.curApp = curApp;
	}

	public static  List<Bundle> getStandardBundles() {
		return standardBundles;
	}

	public static void setStandardBundles(List<Bundle> standardBundles) {
		CApplication.standardBundles = standardBundles;
	}
	/**
	 * ���ص�ǰ��ý����FlashViewer����
	 * @return
	 */
	public FlashViewer getActiveFlashViewer(){
		return FlashViewer.curFlashViewer;
	}
	/**
	 * ��õ�ǰ����ĸ�·��
	 * ��ǰ����·����splash swf���ڵ�Ŀ¼
	 * 
	 * @return
	 */
	public String getBaseFolder(){
		String pathStr=ConfigerLoader.getSplashSwf().toLowerCase().trim();
		if(pathStr.startsWith("http://")){
			String splashPath=ConfigerLoader.getSplashSwf().trim();
			int spl=splashPath.lastIndexOf('/');
			return splashPath.substring(0,spl);
		}else{
		    File splashSwfFile=new File(ConfigerLoader.getSplashSwf());
		    return splashSwfFile.getParentFile().getAbsolutePath();
		}
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
	/**
	 * ���runtime swf������·��
	 * @return
	 */
	public String getRuntimeSwfPath(){
		String swfPath=SplashWindow.getPerspective().runtimeSwfPath;
		if(swfPath==null){
			JFaceHelpMethod.showError(ErrorMessages.Runtime_Swf_Null);
		}
		return UIHelpMethod.getFullPath(swfPath);
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
	   
	}
	public void dispose() {
		dataMap.clear();
		if(curApp!=null){
			try {
				curApp.unLoad();
			} catch (BundleException e) {
				openError(null,"",ErrorMessages.Bundle_UnLoad_Error+e.getMessage());
			}
		}
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
		mainShell=Display.getCurrent().getActiveShell();
		MessageDialog.openWarning(mainShell, title, message);
	}
}
