package cn.smartinvoke.smartrcp.app;

import java.io.File;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.util.HelpMethods;
/**
 * �Ѱ�װӦ�ó���Ĺ���
 * @author pengzhen
 *
 */
public class CAppService implements IServerObject {

	public CAppService(){
	  	
	}
	/**
     * ���г���İ�װĿ¼
     * @return
     */
    public static String getInstallFolder(){
    	String path=HelpMethods.getPluginFolder()+"/smartrcpApps";
    	File folder=new File(path);
    	if(!folder.exists()){
    		folder.mkdirs();
    	}
    	return path;
    }
	@Override
	public void dispose() {
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
