package cn.smartinvoke.smartrcp;

import java.io.DataOutputStream;
import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 
 * smartrcpӦ�ó�����Ϣ��
 * Ӧ�ó����Ŀ¼�ṹ���£�
 * modules��Ϊģ���ļ�
 * iconsΪͼ���ļ�
 * main.swfΪ������swf
 * splash.swfΪ���������ļ�
 * @author pengzhen
 * 
 */
public class CAppInfo {
	//Ӧ�ó���ĸ��ļ���·��
	public String basePath;
	//Ӧ�ó������Ʊ�������ļ����У����淶
    public String name;
    //�汾��
    public float version;
    //logo.png�ļ�·����ֻ����png��ʽ
    public String logoPath;
    //Ӧ�ó���˵��
    public String describe;
    
    /**swf��ַ
    */
    public String mainSwfPath;//��swf��ַ
    public String splashSwfPath;//splash���������ַ
    
    
    
    public List<String> modules=new LinkedList<String>();//swfģ���ļ�·������
    /**
    * ͼ���ļ�·��
    */
    public List<String> icons=new LinkedList<String>();
    /**
     * ��Դ�ļ�·��
     */
   // public List<String> resources=null;
    
	public CAppInfo(){
	    
	}
	/**
	 * ����basePathĿ¼�µĳ����ļ���ʼ�����ֶ�
	 */
    public void init(){
    	this.mainSwfPath=this.mainSwfPath.substring(this.basePath.length());
    	this.splashSwfPath=this.splashSwfPath.substring(this.basePath.length());
    	
    	//��ʼ��ģ��·��
    	File folder = new File(this.basePath+File.separator+"modules");
    	this.initFileList(folder, this.modules);
		//��ʼ��ͼ��·��
    	folder=new File(this.basePath+File.separator+"icons");
    	this.initFileList(folder, this.icons);
           	
    }
    private void initFileList(File folder,List<String> list){
    	//List<String> ret = new LinkedList<String>();
		if (folder != null) {
			int basePathLen=this.basePath.length();
			Stack<File> stack = new Stack<File>();
			stack.push(folder);
			while (!stack.isEmpty()) {
				File temp = stack.pop();
				File[] subFiles = temp.listFiles();
				if (subFiles != null) {
					for (int i = 0; i < subFiles.length; i++) {
						File file = subFiles[i];
						if (!file.isDirectory()) {
							//if(file.getName().endsWith(".swf")){
							 list.add(file.getAbsolutePath().substring(basePathLen));
							//}
						} else {
							stack.push(file);
						}
					}
				}
			}
		}
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
