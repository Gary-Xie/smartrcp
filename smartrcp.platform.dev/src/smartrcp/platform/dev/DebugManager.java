package smartrcp.platform.dev;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.smartrcp.DebugServer;
import cn.smartinvoke.util.HelpMethods;

/**
 * ����debug����
 * @author pengzhen
 *
 */
public class DebugManager implements IServerObject{
	public static final String Args="-isDebug ";
    public static int runningCheckPort=17385;
	public DebugManager() {
		
	}
	
	public void debugAsShell(boolean set){
		//if(set){
			DebugServer.isDebugAsShell=set;
		//}
	}
	
	
	/**
	 * ��鵱ǰ�����Ƿ��������У�smartrcp�Ŀ�����ֻ�ṩһ������ʵ��
	 * 
	 * @return
	 */
    public static boolean isRunning(){
    	boolean ret=false;
    	try {
    		new ServerSocket(runningCheckPort);
    		checkDebugInvoke();
		} catch (IOException e) {
			ret=true;
		}
    	return ret;
    }
    /**
     * ����һ�߳�ר�ż���debug����ĵ���
     */
    private static void checkDebugInvoke(){
       Thread thread=new Thread(){
    	   public void run(){
    		   
    	   }
       };
       thread.setName("smartrcp_debug");
       thread.setDaemon(true);
       
    }
    /**
     * ��ȡdebugĿ¼�ļ�����·��
     * @return
     */
    public static String getDebugFilePath(){
    	return HelpMethods.getPluginFolder()+File.separator+"isRunning.ini";
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
