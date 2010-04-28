package cn.smartinvoke.smartrcp.app;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Platform;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.smartrcp.app.pack.CAppInfo;
import cn.smartinvoke.smartrcp.app.pack.PackageTool;
import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;
import cn.smartinvoke.smartrcp.gui.module.CEventBean;
import cn.smartinvoke.smartrcp.util.JFaceHelpMethod;
import cn.smartinvoke.util.HelpMethods;
import cn.smartinvoke.util.Log;
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
    private static List<CAppInfo> appPacks=null;
    /**
     * �����Ѱ�װ�ĳ���
     * @return
     */
    public List<CAppInfo> getInstallApps(){
      if(appPacks==null){
    	List<CAppInfo> ret=new LinkedList<CAppInfo>();
    	File folder=new File(getInstallFolder());
    	File[] appFolders=folder.listFiles();
    	if(appFolders!=null){
    		for(int i=0;i<appFolders.length;i++){
    			File appFolder=appFolders[i];
    			if(appFolder.isDirectory()){//��Ŀ¼
    				String path=appFolder.getAbsolutePath();
    				if(isAppFolder(path)){//�ǳ���Ŀ¼
    				  	ret.add(getAppInfo(path));
    				}
    			}
    		}
    	}
    	appPacks=ret;
       }
       return appPacks;
    }
    /**
     * �ж�һĿ¼�Ƿ�Ϊ����Ŀ¼
     * @param installFolder
     * @return
     */
    private boolean isAppFolder(String installFolder){
      if(installFolder!=null){
        File configFile=new File(installFolder+File.separator+PackageTool.Key_Config_File);
        File propFile=new File(installFolder+File.separator+PackageTool.Key_Property_File);
        File moduleFolder=new File(installFolder+File.separator+"modules");
        if(configFile.exists() && propFile.exists() && moduleFolder.exists()){
        	return true;
        }
      }
      return false;	
    }
    /**
     * ��ȡ����װĿ¼�µ�property.prop��Ϣ������CAppInfo�������ʽ����
     * 
     * @param installFolder
     * @return
     */
    public static CAppInfo getAppInfo(String installFolder){
    	CAppInfo ret=new CAppInfo();
    	DataInputStream in=null;
    	String propFilePath=installFolder+File.separator+PackageTool.Key_Property_File;
    	try{
    	 
    	 ret.basePath=installFolder;
    	 in=new DataInputStream(new FileInputStream(propFilePath));
    	 ret.name=in.readUTF();ret.version=in.readUTF();
    	 ret.provider=in.readUTF();ret.updateUrl=in.readUTF();
    	 String logoPath=installFolder+File.separator+"logo.png";
    	 if(new File(logoPath).exists()){
    		 ret.setLogoPath(logoPath);
    	 }
    	 ret.describe=in.readUTF();
    	}catch(Exception e){
    		Log.printError(propFilePath+" file not exist or format  error!");
    	}finally{
    		if(in!=null){
    			try{in.close();}catch(Exception e){};
    		}
    	}
    	return ret;
    }
    /**
     * ��װָ��·���µĳ���
     * @param path
     */
    public void installApp(String path){
    	if(path!=null){
    	 if(new File(path).exists()){
    	   CAppInfo appInfo=new PackageTool().install(path, CAppService.getInstallFolder());
    	   this.fireEvents(appInfo);
    	 }
    	}
    }
    private static List<CEventBean> listeners=new LinkedList<CEventBean>();
    
    /**
     * 
     * @param eventBean
     */
    public void addListener(CEventBean eventBean){
      if(eventBean!=null){
    	  listeners.add(eventBean);
      }
    }
    /**
     * ����flex������
     * @param appInfo
     */
    private void fireEvents(CAppInfo appInfo){
		if (appInfo != null) {
			if (!appPacks.contains(appInfo)) {
				appPacks.add(appInfo);
				List<CEventBean> listeners = CAppService.listeners;
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).fireEvent(appInfo);
				}
			}
		}
    }
    
    public void runApp(String installFolder){
    	SmartRCPBuilder.Instance.reStart(installFolder);
    }
    public void deleteApp(String installPath){
    	//File installFolder=new File(installPath);
    	//if(installFolder.exists()){
    	try{
    	   HelpMethods.deleteFolder(installPath);
    	}catch(Exception e){
    	   JFaceHelpMethod.showError(installPath+"�ļ����޷�ɾ����\n�����Ǹ�Ŀ¼�µĳ����������У����˳���������ɾ��");
    	}
    	//}
    }
   
    public void updateApp(String installFolder){
    	Log.println("���³���"+installFolder);
    }
	public void dispose(){
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception{
		File folder=new File("D:/Temporary Internet Files");
		File[] subFiles=folder.listFiles();
		for(File subFile : subFiles){
			System.out.println(subFile.getAbsolutePath());
		}
	}
}
