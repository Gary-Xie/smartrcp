package cn.smartinvoke.smartrcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.internal.WorkbenchPage;

import cn.smartinvoke.gui.ObjectPool;
import cn.smartinvoke.rcp.CLayoutBasicInfo;
import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;
import cn.smartinvoke.smartrcp.gui.FlashShell;
import cn.smartinvoke.smartrcp.gui.FlashViewPart;
import cn.smartinvoke.smartrcp.gui.control.GlobalServiceId;
import cn.smartinvoke.smartrcp.gui.control.ViewManager;
import cn.smartinvoke.smartrcp.util.JFaceHelpMethod;
import cn.smartinvoke.util.ConfigerLoader;

/**
 * ���𴴽�debug����
 * 
 * @author pengzhen
 * 
 */
public class DebugServer {
    static CApplication cur_application=null;
    static int debugPort=17385;
	private DebugServer() {
       
	}
	public static void start() {
		cur_application=(CApplication)ObjectPool.INSTANCE.getObject(GlobalServiceId.Cur_Application);
		Thread thread = new Thread() {
			public void run() {
				Display display=(Display)
		        ObjectPool.INSTANCE.getObject(GlobalServiceId.Swt_Display);
				// ��ö˿�
				//int port = 17385;
				try {
					ServerSocket serverSocket = new ServerSocket(debugPort);
					while (true) {
						BufferedReader reader=null;
					  try{
						Socket socket = serverSocket.accept();
						reader = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						//��ȡ��ַ
						String url = reader.readLine();
						if(!url.equals("non")){
                         final String swfUrl=url.substring(0, url.length()-4)+"swf";
                         //��debug����
                         display.asyncExec(new Runnable(){
    						public void run() {
    							if(isDebugAsShell){
    							  openDebugShell(swfUrl);
    							}else{
    							  openViewPart(swfUrl);
    							}
    						}
    					 });
						}
                        //-----------
					  }catch(Throwable e){
						  e.printStackTrace();
					  }finally{
						  if(reader!=null){
							  reader.close();
						  }
					  }
					}
				} catch (IOException e) {//smartrcpƽ̨�Ѿ�������ͨ���׽��ֽ�debug·�����͸����е�smartrcp
					 sendDebugFile();
					 Runtime.getRuntime().exit(0);
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
	/**
	 *�Ѿ���smartrcp����ƽ̨�����У���ôֻ��Ҫ����Ҫdebug��swf·��ͨ��
	 *�׽��ַ��͸����Ϳ�����
	 */
	private static void sendDebugFile(){
	   Socket socket=null;
	  try{
		 socket=new Socket("127.0.0.1",debugPort);
		 BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		 String path=ConfigerLoader.getAppArg("-debugPath ");
		 if(path==null){
			 path="non";
		 }
		 writer.write(path);
		 writer.write("\n");
		 writer.flush();
		 writer.close();
	  }catch(Exception e){
		JFaceHelpMethod.showError(e.getMessage());
	  }finally{
		  if(socket!=null){
			  try{socket.close();}catch(Exception e){}
		  }
	  }
	}
	/**
	 * Ϊtrue��ʾ��debug���ڣ�false��ʾ��ViewPart
	 */
	public static boolean isDebugAsShell=true;
	
    static void openDebugShell(String swfPath){
    	FlashShell flashShell=new FlashShell(true);
    	flashShell.setText(swfPath);
    	flashShell.setSize(550, 550);
    	
    	flashShell.createFlashContainer(swfPath);
    	
    	flashShell.open();
    }
    static void openViewPart(String swfPath){
    	CLayoutBasicInfo layoutInfo=new CLayoutBasicInfo();
    	layoutInfo.autoLoad=true;
    	layoutInfo.isDebugLayout=true;
    	layoutInfo.title=swfPath;
    	layoutInfo.modulePath=swfPath;
    	layoutInfo.viePartId=FlashViewPart.ID;
    	//��װ����swf��ʽ�򿪣���ΪloadModule�����ɸ�swf�Լ�����
    	layoutInfo.isModuleSwf=false;
    	
    	ViewManager.Instance.openViewPart(layoutInfo, true,IWorkbenchPage.VIEW_ACTIVATE);
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
