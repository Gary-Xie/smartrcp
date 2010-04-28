package cn.smartinvoke.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;

import cn.smartinvoke.smartrcp.CApplication;
import cn.smartinvoke.smartrcp.app.CAppService;
import cn.smartinvoke.smartrcp.util.JFaceHelpMethod;

/**
 * ����������Ϣ
 * @author pengzhen
 */
public class ConfigerLoader {
	//��������
	public static final String key_app_name="app_name";
	//����汾
	public static final String key_app_version="app_version";
	//���µ�ַ
	public static final String key_update_url="update_url";
	//��������swf��ַ
    public static final String key_splash="splash";
    //���������С width*height��ʽ
    public static final String key_splash_size="splash-size";
    //���г���swf·��
    public static final String key_runtime="runtime";
    
    
    public static final String key_debug_port="debug-port";
    public static final String key_debug="debug";
    public static final String key_export_package="Export-Package";
	private ConfigerLoader(){
		
	}
	//private static Properties properties=null;
	public static String configPath=null;
	public static int[] splashSize=new int[]{300,300};
	private static boolean isInit=false;
	/**
	 * �÷���������ֻ������һ��
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void init() throws FileNotFoundException, IOException{
	  if(!isInit){
//		  try{
//		    JFaceHelpMethod.showInfo(HelpMethods.getPluginFolder());
//		  }catch(Exception e){
//			  JFaceHelpMethod.showInfo(e.getMessage());
//		  }
		isInit=true;
		startupSwf=getStartInfo();//��start.ini�ļ��л�ȡ��������Ŀ¼
		if(startupSwf==null){
			startupSwf=getAppArg();//�ӿ���̨������������·��
		}
		if(startupSwf!=null){
			int spl=-1;
			//��ô��ڴ�С
			splashSize=getShellSize(startupSwf);
			if((spl=startupSwf.lastIndexOf('?'))!=-1){
				startupSwf=startupSwf.substring(0,spl);
			}
		}else{//���û����������׼����
	    	if(!checkSwfExist()){
	    		//��׼��������
		    	startupSwf=CApplication.Instance.getInstallLocation()+"default.swf";
		    	//JFaceHelpMethod.showInfo("splash:"+startupSwf);
		    	if(!new File(startupSwf).exists()){
		    	  JFaceHelpMethod.showError("����SWF�ļ�"+startupSwf+"�����ڣ������˳�");
		    	  Runtime.getRuntime().exit(0);
		    	}
	    	}
	    }
	  }
//	  
	}
	/**
	 * ���
	 * @return
	 */
	private static boolean checkSwfExist(){
		if(startupSwf==null){
			return false;
		}
		String tempPath=startupSwf.toLowerCase();
		if(tempPath.startsWith("http://")){
			try {
				URL url=new URL(startupSwf);
				url.openConnection().getInputStream();
				return true;
			} catch (Exception e) {
				return false;
			}
		}else{
			return new File(startupSwf).exists();
		}
	}
	/**
	 * ��swf·���л�����������С
	 * @param swfPath
	 * @return
	 */
	 private static int[] getShellSize(String swfPath){
		    int[] ret=new int[]{300,300};
		    int spl=swfPath.lastIndexOf('?');
		    if(spl!=-1){
		    	String sizeStr=swfPath.substring(spl+1).trim();
		    	spl=sizeStr.indexOf('*');
		    	if(spl!=-1){
		    		try{
		    	      ret[0]=Integer.valueOf(sizeStr.substring(0,spl));
		    	      ret[1]=Integer.valueOf(sizeStr.substring(spl+1));
		    		}catch(Exception e){
		    		   
		    		}
		    	}
		    }
		    return ret;
	   }
	/**
	 * ��ǰ����swf����·��
	 */
	private static String startupSwf;
	
	public static String getSplashSwf() {
		return startupSwf;
	}
	/**
	 * �Ӱ�װĿ¼��start.ini�ļ��ж�ȡ��������Ŀ¼��������ļ������ڣ������ݴ���smartrcp��
	 * ����getAppArg�������Դӿ���̨�����������Ŀ¼���������̨û����������׼����
	 */
	private static String getStartInfo()throws FileNotFoundException, IOException{
		String appPath=null;
		String path=HelpMethods.getPluginFolder()+File.separator+"start.ini";
	    if(new File(path).exists()){
		 BufferedReader reader=new BufferedReader(new FileReader(path));
		 appPath=reader.readLine();
		 reader.close();
	    }
		return appPath;
	}
	/**
	 * ��ȡ����̨smartrcpĿ¼����ֵ�����û���򷵻�null
	 * Ӧ�ò������£�-app smartrcpӦ�ó���ľ���·��
	 * 
	 * ���û��smartrcpӦ�ò�����������Ĭ��smartrcp����ƽ̨���򣬸�ƽ̨�����
	 * smartrcp����İ�װ�������û���ȷ��װsmartrcp����
	 * @return
	 */
	private static String getAppArg(){
		return getAppArg("-app ");
	}
	/**
	 * ��ȡָ���������в���
	 * @param key
	 * @return
	 */
	public static String getAppArg(String key){
		String ret=null;
		String[] arr=Platform.getCommandLineArgs();
		if(arr!=null){
			String cmdName=key;
    		for(int i=0;i<arr.length;i++){
    			String arg=arr[i].trim();
    			if(arg.equals(cmdName.trim())){
    				ret=arr[i+1].trim();
    			}else
    			if(arg.startsWith(cmdName)){
    				ret=arg.substring(cmdName.length()+1).trim();
    				break;
    			}
    		}
    	}
		return ret;
	}
	/*public static String getProperty(String key){
		if(key.equals(key_debug)){
		  return isDebug+"";
		}else{
		  return properties.getProperty(key);
		}
	}*/
	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		ConfigerLoader.init();
	}

}
