package cn.smartinvoke.smartrcp.app;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.smartrcp.app.pack.CAppInfo;
import cn.smartinvoke.smartrcp.app.pack.PackageTool;
import cn.smartinvoke.smartrcp.gui.module.CEventBean;
import cn.smartinvoke.util.HelpMethods;
import cn.smartinvoke.util.Log;
/**
 * 已安装应用程序的管理
 * @author pengzhen
 *
 */
public class CAppService implements IServerObject {

	public CAppService(){
	  	
	}
	/**
     * 所有程序的安装目录
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
    /**
     * 返回已安装的程序
     * @return
     */
    public List<CAppInfo> getInstallApps(){
    	List<CAppInfo> ret=new LinkedList<CAppInfo>();
    	File folder=new File(getInstallFolder());
    	File[] appFolders=folder.listFiles();
    	if(appFolders!=null){
    		for(int i=0;i<appFolders.length;i++){
    			File appFolder=appFolders[i];
    			if(appFolder.isDirectory()){//是目录
    				String path=appFolder.getAbsolutePath();
    				if(isAppFolder(path)){//是程序目录
    				  	ret.add(getAppInfo(path));
    				}
    			}
    		}
    	}
    	return ret;
    }
    /**
     * 判断一目录是否为程序目录
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
     * 读取程序安装目录下的property.prop信息，并以CAppInfo对象的形式返回
     * 
     * @param installFolder
     * @return
     */
    private CAppInfo getAppInfo(String installFolder){
    	CAppInfo ret=new CAppInfo();
    	DataInputStream in=null;
    	try{
    		/**
    		 * out.writeUTF(appInfo.name);
		 out.writeUTF(appInfo.version); 
		 out.writeUTF(appInfo.provider);
		 out.writeUTF(appInfo.updateUrl);//更新地址
		 out.writeUTF(appInfo.describe);
    		 */
    	 ret.basePath=installFolder;
    	 in=new DataInputStream(new FileInputStream(installFolder+File.separator+PackageTool.Key_Property_File));
    	 ret.name=in.readUTF();ret.version=in.readUTF();
    	 ret.provider=in.readUTF();ret.updateUrl=in.readUTF();
    	 ret.describe=in.readUTF();
    	}catch(Exception e){
    	 throw new RuntimeException(e);	
    	}finally{
    		if(in!=null){
    			try{in.close();}catch(Exception e){};
    		}
    	}
    	return ret;
    }
    /**
     * 安装指定路径下的程序
     * @param path
     */
    public void installApp(String path){
    	if(path!=null){
    	 if(new File(path).exists()){
    	   CAppInfo appInfo=new PackageTool().uncompress(path, CAppService.getInstallFolder());
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
     * 唤醒flex监听器
     * @param appInfo
     */
    private void fireEvents(CAppInfo appInfo){
    	List<CEventBean> listeners=CAppService.listeners;
    	for(int i=0;i<listeners.size();i++){
    		listeners.get(i).fireEvent(appInfo);
    	}
    }
    
    public void runApp(String installFolder){
    	Log.println("运行程序"+installFolder);
    }
    public void deleteApp(String installFolder){
    	Log.println("拆卸程序"+installFolder);
    }
    public void updateApp(String installFolder){
    	Log.println("更新程序"+installFolder);
    }
	public void dispose() {
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File folder=new File("C:/rcp/牛逼_1.22");
		File[] subFiles=folder.listFiles();
		System.out.println(subFiles);
	}

}
