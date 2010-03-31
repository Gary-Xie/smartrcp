package cn.smartinvoke.smartrcp.app;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.smartrcp.app.pack.CAppInfo;
import cn.smartinvoke.smartrcp.app.pack.PackageTool;
import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;
import cn.smartinvoke.smartrcp.gui.module.CEventBean;
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
    /**
     * �����Ѱ�װ�ĳ���
     * @return
     */
    public List<CAppInfo> getInstallApps(){
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
    	return ret;
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
    	try{
    		/**
    		 * out.writeUTF(appInfo.name);
		 out.writeUTF(appInfo.version); 
		 out.writeUTF(appInfo.provider);
		 out.writeUTF(appInfo.updateUrl);//���µ�ַ
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
     * ��װָ��·���µĳ���
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
     * ����flex������
     * @param appInfo
     */
    private void fireEvents(CAppInfo appInfo){
    	List<CEventBean> listeners=CAppService.listeners;
    	for(int i=0;i<listeners.size();i++){
    		listeners.get(i).fireEvent(appInfo);
    	}
    }
    
    public void runApp(String installFolder){
    	SmartRCPBuilder.Instance.reStart(installFolder);
    }
    public void deleteApp(String installFolder){
    	Log.println("��ж����"+installFolder);
    }
    public void updateApp(String installFolder){
    	Log.println("���³���"+installFolder);
    }
	public void dispose() {
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File folder=new File("C:/rcp/ţ��_1.22");
		File[] subFiles=folder.listFiles();
		System.out.println(subFiles);
	}

}