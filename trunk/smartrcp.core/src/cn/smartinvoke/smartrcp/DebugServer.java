package cn.smartinvoke.smartrcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Display;

import cn.smartinvoke.gui.ObjectPool;
import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;
import cn.smartinvoke.smartrcp.gui.FlashShell;
import cn.smartinvoke.smartrcp.gui.control.GlobalServiceId;
import cn.smartinvoke.util.ConfigerLoader;

/**
 * ���𴴽�debug����
 * 
 * @author pengzhen
 * 
 */
public class DebugServer {
    static CApplication cur_application=null;
	private DebugServer() {
       
	}
	public static void start() {
		cur_application=(CApplication)ObjectPool.INSTANCE.getObject(GlobalServiceId.Cur_Application);
		Thread thread = new Thread() {
			public void run() {
				Display display=(Display)
		        ObjectPool.INSTANCE.getObject(GlobalServiceId.Swt_Display);
				// ��ö˿�
				int port = 1738;
				/*String portStr = ConfigerLoader
						.getProperty(ConfigerLoader.key_debug_port);
				if (portStr != null) {
					try {
						port = Integer.valueOf(portStr);
					} catch (Throwable e) {
					}
					;
				}*/
				try {
					ServerSocket serverSocket = new ServerSocket(port);
					while (true) {
						BufferedReader reader=null;
					  try{
						Socket socket = serverSocket.accept();
						reader = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						//��ȡ��ַ
						String url = reader.readLine();
                        final String swfUrl=url.substring(0, url.length()-4)+"swf";
                        //��debug����
                        display.asyncExec(new Runnable(){
    						public void run() {
    							 openDebugShell(swfUrl);
    						}
    					});
                        //-----------
					  }catch(Throwable e){
						  e.printStackTrace();
					  }finally{
						  if(reader!=null){
							  reader.close();
						  }
					  }
					}
				} catch (IOException e) {
					final int p=port;
					display.asyncExec(new Runnable(){
						public void run() {
							MessageDialog.openError(SmartRCPBuilder.Main_Shell, "����",
									"�˿�" + p + "��ռ�û򲻺Ϸ�");
						}
					});
					
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
    static void openDebugShell(String swfPath){
    	FlashShell flashShell=new FlashShell(true);
    	flashShell.setText(swfPath);
    	flashShell.setSize(550, 550);
    	String modulePath=cur_application.getDebugPath();
    	if(modulePath==null){
    	   flashShell.createFlashContainer(swfPath);
    	}else{
    	   flashShell.createFlashContainer(swfPath,modulePath);
    	}
    	flashShell.open();
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
