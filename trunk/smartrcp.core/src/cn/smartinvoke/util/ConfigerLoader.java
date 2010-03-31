package cn.smartinvoke.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import cn.smartinvoke.smartrcp.app.pack.PackageTool;

/**
 * ����������Ϣ
 * @author pengzhen
 *
 */
public class ConfigerLoader {
    public static final String key_splash="splash";
    public static final String key_splash_size="splash-size";
    public static final String key_runtime="runtime";
    public static final String key_debug_port="debug-port";
    public static final String key_debug="debug";
    public static final String key_export_package="Export-Package";
	private ConfigerLoader() {
	   
	}
	private static Properties properties=null;
	public static String configPath=null;
	public static void init() throws FileNotFoundException, IOException{
	  getStartInfo();//���������Ϣ
	  //if(properties==null){
		properties=new Properties();
		configPath=appPath+File.separator+PackageTool.Key_Config_File;
		Log.println("start.ini location="+configPath);
		properties.load(new FileInputStream(configPath));
	  //}
	}
	/**
	 * ��ǰ������Ӧ�ó���װĿ¼
	 */
	public static String appPath;
	private static boolean isDebug=false;
	//�����ļ�����·��
	public static String startFilePath="F:/myWork/cn.smartinvoke.smartrcp.core/start.ini";
	/**
	 * ���������Ϣ
	 */
	private static void getStartInfo()throws FileNotFoundException, IOException{
		//TODO �ļ�λ��Ϊ��ʱ����ʱ���
		//String path="F:/myWork/cn.smartinvoke.smartrcp.core/start.ini";
		BufferedReader reader=new BufferedReader(new FileReader(startFilePath));
		appPath=reader.readLine();
		String debugStr=reader.readLine();
		if(debugStr!=null){
			try{
				isDebug=Boolean.valueOf(debugStr);
			}catch(Exception e){};
		}
		reader.close();
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
