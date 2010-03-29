package cn.smartinvoke.smartrcp.app.pack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.jobs.Job;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.smartrcp.app.CAppService;
import cn.smartinvoke.smartrcp.app.download.http.SiteFileFetch;
import cn.smartinvoke.smartrcp.app.download.http.SiteInfoBean;
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
			 SiteInfoBean bean=new SiteInfoBean(downloadUrl,this.getSaveFolder(),saveName,1);
			 Job job =new SiteFileFetch("����", bean);
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
    		   File saveFile=new File(getSaveFolder()+"/"+file.getName());
    		   FileInputStream source=null;FileOutputStream out=null;
    		   try{
    		    source=new FileInputStream(file);
    		    out=new FileOutputStream(saveFile);
    		    int readNum=0;
    		    byte[] buf=new byte[1024];
    		    while((readNum=source.read(buf,0,buf.length))!=0){
    		    	out.write(buf, 0, readNum);
    		    }
    		    out.flush();
    		    out.close();
    		   }catch(Exception e){
    			   throw new RuntimeException("�޷���"+file.getAbsolutePath()+"������"+this.getSaveFolder()+"Ŀ¼");
    		   }finally{
    			   if(source!=null){
    				   try{source.close();}catch(Exception e){};
    			   }
    			   if(out!=null){
    				   try{out.flush();}catch(Exception e){};
    				   try{out.close();}catch(Exception e){};
    			   }
    		   }
    		}
    	}
    }
    /**
     * �������г��������Ϣ
     * @return
     */
    public List<CAppInfo> getApps(){
    	List<CAppInfo> ret=new LinkedList<CAppInfo>();
    	File folder=new File(getSaveFolder());
    	File[] filePacks=folder.listFiles();
    	if(filePacks!=null){
    		for(int i=0;i<filePacks.length;i++){
    		   if(filePacks[i].getName().endsWith(".rcp")){
    			ret.add(PackageTool.readBasicInfo(filePacks[i].getAbsolutePath()));
    		   }
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
    	   new PackageTool().uncompress(path, CAppService.getInstallFolder());
    	 }
    	}
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
