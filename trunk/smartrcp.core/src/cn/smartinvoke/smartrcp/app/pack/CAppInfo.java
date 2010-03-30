package cn.smartinvoke.smartrcp.app.pack;

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
    public String version;
    //�ṩ��
    public String provider;
    //logo.png�ļ�·����ֻ����png��ʽ
    public String logoPath;
    //Ӧ�ó���˵��
    public String describe;
    //���µ�ַ
    public String updateUrl="http://smartinvoke.cn/update";
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
     *��չ��jar·��
     */
    private List<String> libs=new LinkedList<String>();
    /**
     * ���������С
     */
    public int splashWidth=424;
    public int splashHeight=225;
    //�������ļ��ľ���·��
    public String packLocation=null;
	public CAppInfo(){
	    
	}
	/**
	 * ����basePathĿ¼�µĳ����ļ���ʼ�����ֶ�
	 */
    public void init(){
    	this.mainSwfPath=this.mainSwfPath.substring(this.basePath.length());
    	this.splashSwfPath=this.splashSwfPath.substring(this.basePath.length());
    	
    	//��ʼ��ģ��·��
    	File folder = new File(this.basePath+File.separator+PackageTool.Key_Modules_Folder);
    	this.initFileList(folder, this.modules);
		//��ʼ��ͼ��·��
    	folder=new File(this.basePath+File.separator+PackageTool.Key_Icons_Folder);
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
    public List<String> addLib(String path){
    	if(path!=null){
    		if(new File(path).exists()){
    			if(!this.libs.contains(path)){
    				this.libs.add(path);
    			}
    		}
    	}
    	return this.libs;
    }
    public List<String> getLibs(){
    	return this.libs;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
