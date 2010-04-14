package cn.smartinvoke.smartrcp.app.pack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.smartrcp.app.CAppService;
import cn.smartinvoke.smartrcp.app.download.http.ICompleteListener;
import cn.smartinvoke.smartrcp.app.download.http.SiteFileFetch;
import cn.smartinvoke.smartrcp.app.download.http.SiteInfoBean;
import cn.smartinvoke.smartrcp.gui.module.CEventBean;
import cn.smartinvoke.smartrcp.util.JFaceHelpMethod;
import cn.smartinvoke.util.HelpMethods;
/**
 * Ӧ�ó��������
 * @author pengzhen
 *
 */
public class AppPackService implements IServerObject{

	public AppPackService(){
		
	}
	
	/**
	 * ��http���������س��򣬴洢�ڱ���
	 * @param url
	 */
    public void downloadAppFromUrl(String downloadUrl){
    	//���������ļ�����
    	if(downloadUrl!=null){
		  String saveName="temp.rcp";
		  int spl=downloadUrl.lastIndexOf('/');
		  if(spl!=-1){
			saveName=downloadUrl.substring(spl+1);
			if(saveName.endsWith(".rcp")){
			 SiteInfoBean bean=new SiteInfoBean(downloadUrl,getSaveFolder(),saveName,1);
			 SiteFileFetch job =new SiteFileFetch("����", bean,new ICompleteListener() {
					public void complete(CAppInfo appInfo) {
						fireEvents(appInfo);
					}
				 });
			 job.setUser(true);
			 
			 job.schedule();
			}
		  }
    	}
    }
    /**
     * �����س����ļ��洢��Ӧ�ó���Ŀ¼��
     * @param file
     */
    public void downloadAppFromLocal(String filePath){
    	if(filePath!=null){
    		File file=new File(filePath);
    		if(file.exists()){
    		   String savePath=getSaveFolder()+"/"+file.getName();
    		   File saveFile=new File(savePath);
    		   //ת���ļ��Ѿ����ڣ��Ƿ񸲸�
    		   if(!JFaceHelpMethod.checkOverWrite(savePath)){
       			return;
       		   }
    		   
    		   FileInputStream source=null;FileOutputStream out=null;
    		   try{
    		    source=new FileInputStream(file);
    		    out=new FileOutputStream(saveFile);
    		    int readNum=0;
    		    byte[] buf=new byte[1024];
    		    while((readNum=source.read(buf,0,buf.length))>0){
    		    	out.write(buf, 0, readNum);
    		    }
    		    out.flush();
    		    out.close();
    		    
    		   }catch(Exception e){
    			   String str="�޷���"+file.getAbsolutePath()+"������"+getSaveFolder()+"Ŀ¼";
    			   JFaceHelpMethod.showError(str);
    		   }finally{
    			   if(source!=null){
    				   try{source.close();}catch(Exception e){};
    			   }
    			   if(out!=null){
    				   try{out.flush();}catch(Exception e){};
    				   try{out.close();}catch(Exception e){};
    			   }
    		   }
    		}else{
    			JFaceHelpMethod.showInfo("�ļ�"+filePath+"������");
    		}
    	}
    }
    private static List<CAppInfo> appPacks=null;
    /**
     * �������г��������Ϣ
     * @return
     */
    public List<CAppInfo> getApps(){
    	
    	return appPacks;
    }
    
    /**
     * ɾ��ָ��·���µĴ������
     * @param path
     */
    public void deleteAppPack(String path){
    	if(path!=null){
    		new File(path).delete();
    	}
    }
    private static List<CEventBean> eventBeans=new LinkedList<CEventBean>();
    /**
     * 
     * @param eventBean
     */
    public void addListener(CEventBean eventBean){
      if(eventBean!=null){
    	eventBeans.add(eventBean);
      }
    }
    /**
     * ����flex������
     * @param appInfo
     */
    private void fireEvents(CAppInfo appInfo){
      if(appInfo!=null){
    	if(!appPacks.contains(appInfo)){
    	 appPacks.add(appInfo);
    	 List<CEventBean> listeners=eventBeans;
    	 for(int i=0;i<listeners.size();i++){
    		listeners.get(i).fireEvent(appInfo);
    	 }
    	}
      }
    }
    /**
     * ������Ĵ洢Ŀ¼
     * @return
     */
    public static String getSaveFolder(){
    	String path=HelpMethods.getPluginFolder()+"/smartrcpPacks";
    	File folder=new File(path);
    	if(!folder.exists()){
    		folder.mkdirs();
    	}
    	return path;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

	@Override
	public void dispose() {
		
	}

}
