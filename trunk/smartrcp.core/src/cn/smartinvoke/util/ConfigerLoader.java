package cn.smartinvoke.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;

import cn.smartinvoke.smartrcp.app.CAppService;
import cn.smartinvoke.smartrcp.app.pack.PackageTool;

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
	private static Properties properties=null;
	public static String configPath=null;
	public static void init() throws FileNotFoundException, IOException{
		appPath=getStartInfo();//��start.ini�ļ��л�ȡ��������Ŀ¼
		if(appPath==null){
			appPath=getAppArg();//�ӿ���̨������������·��
		}
	    if(appPath==null || 
	       (!new File(appPath).exists())){//���û����������׼����
	    	appPath=CAppService.getInstallFolder()+"/smartrcp";
	    }
	    
		properties=new Properties();
		configPath=appPath+File.separator+PackageTool.Key_Config_File;
		if(!new File(configPath).exists()){//���������ļ�������
		   Log.printError(configPath+" is not exist the program exit!");
		}
		Log.println("config.ini location="+configPath);
		properties.load(new FileInputStream(configPath));
	  
	}
	/**
	 * ��ǰ������Ӧ�ó���װĿ¼
	 */
	public static String appPath;
	private static boolean isDebug=false;
	//�����ļ�����·��
	public static String startFilePath=null;
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
		String[] arr=Platform.getApplicationArgs();
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
	public static String getProperty(String key){
		if(key.equals(key_debug)){
		  return isDebug+"";
		}else{
		  return properties.getProperty(key);
		}
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		ConfigerLoader.init();
		
		System.out.println(ConfigerLoader.getProperty(ConfigerLoader.key_splash));
		System.out.println(ConfigerLoader.getProperty(ConfigerLoader.key_splash_size));
		System.out.println(ConfigerLoader.getProperty(ConfigerLoader.key_export_package));
	}

}
