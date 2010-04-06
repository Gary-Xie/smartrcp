package cn.smartinvoke.smartrcp.app.pack;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


import cn.smartinvoke.smartrcp.app.CAppService;

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
    String logoPath;
    //Ӧ�ó���˵��
    public String describe;
    //�ó���������˵����ڿ�ʼ�˵��ṹ�е�λ��
    public String menuPath;
    //�˵�ͼ�꣬��ͼ��һ��Ҫλ��iconsĿ¼
    public String meunIcon;
    
    //���µ�ַ
    public String updateUrl="http://smartinvoke.cn/update";
    
    
    public boolean isUpdate=true;//�Ƿ���£��û�����ѡ�񲻸��³���
    public String updateVersion;
    public String updateDescribe;
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
    public boolean equals(Object obj){
    	boolean ret=false;
    	if(obj!=null ){
    		if(obj instanceof CAppInfo){
    			CAppInfo other=(CAppInfo)obj;
    			if(other.packLocation!=null && this.packLocation!=null){
    				ret=new File(this.packLocation).equals(new File(other.packLocation));
    			}
    			if(other.basePath!=null && this.basePath !=null){
    				ret=new File(this.basePath).equals(new File(other.basePath));
    			}
    		}
    	}
    	return ret;
    }
    public int hashCode(){
    	if(this.packLocation!=null){
    		return this.packLocation.hashCode();
    	}
    	if(this.basePath!=null){
    		return this.basePath.hashCode();
    	}
    	return super.hashCode();
    }
    
	public String getLogoPath() {
		if(this.logoPath==null){
			return CAppService.getInstallFolder()+"/smartrcp.png";
		}else{
		    return this.logoPath;
		}
	}
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
