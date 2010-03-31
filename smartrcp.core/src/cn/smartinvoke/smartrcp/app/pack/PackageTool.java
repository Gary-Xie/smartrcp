package cn.smartinvoke.smartrcp.app.pack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import cn.smartinvoke.rcp.ErrorMessages;
import cn.smartinvoke.util.ConfigerLoader;

/**
 * Ӧ�ó�������
 * @author pengzhen
 */
public class PackageTool {

	public PackageTool() {
	   	
	}
	/**
	 * ѹ������
	 * @param appInfo  Ӧ�ó�����Ϣ��
	 * @param savePath �ļ�����Ŀ¼
	 */
	public void compress(CAppInfo appInfo,String saveFolder){
		if(appInfo!=null && saveFolder!=null){
		  DataOutputStream out=null;
		  try{
			String fileName=appInfo.name+"_"+appInfo.version+".rcp";
		    out=new DataOutputStream(new FileOutputStream(saveFolder+File.separator+fileName));
		    appInfo.init();
		    out.writeUTF(appInfo.name);
		    out.writeUTF(appInfo.version); 
		    out.writeUTF(appInfo.provider);
		    out.writeUTF(appInfo.updateUrl);//���µ�ַ
		    out.writeUTF(appInfo.describe);
		    //ѹ��logo.png
		    if(appInfo.logoPath==null){//û��logo�ļ�
		    	out.writeBoolean(false);
		    }else{
		    	out.writeBoolean(true);
		    	saveFileData(appInfo.logoPath,out);
		    }
		    //ѹ��main.swf·��
		    out.writeUTF(appInfo.mainSwfPath);
		    //ѹ��splash.swf·��
		    out.writeUTF(appInfo.splashSwfPath);
		    //ѹ��ģ���ļ�
		    this.saveListFiles(appInfo,appInfo.modules, out);
		    //ѹ��ͼ���ļ�����
		    List<String> icons=appInfo.icons;
		    
		    this.saveListFiles(appInfo,icons, out);
		    //ѹ����չ����Ϣ
		    List<String> libPaths=appInfo.getLibs();
		    //д����չ��ĸ���
		    out.writeInt(libPaths.size());
		    for(int i=0;i<libPaths.size();i++){
		       	String libPath=libPaths.get(i);
		       	//д���ļ�����
		       	out.writeUTF(i+"_"+new File(libPath).getName());
		       	//д���ļ�����
		       	saveFileData(libPath,out);
		    }
		    //���������С
		    out.writeInt(appInfo.splashWidth);out.writeInt(appInfo.splashHeight);
		  }catch(Exception e){
			  throw new RuntimeException(ErrorMessages.Compress_Error+e.getMessage());
		  }finally{
			  if(out!=null){
				  try{out.flush();}catch(Exception e){};
				  try{out.close();}catch(Exception e){};
			  }
		  }
		}
	}
	private void saveListFiles(CAppInfo appInfo,List<String> list,DataOutputStream out)throws Exception{
		if(list==null){
			list=new LinkedList<String>();
		}
		out.writeInt(list.size());
	    for(int i=0;i<list.size();i++){
	    	String path=list.get(i);
	    	out.writeUTF(path);//д���ļ�����
	    	this.saveFileData(appInfo.basePath+path, out);//д���ļ�����
	    }
	}
	private void saveFileData(String filePath,DataOutputStream out)throws Exception{
		File file=new File(filePath);
		FileInputStream in=new FileInputStream(filePath);
		byte[] bts=new byte[(int)file.length()];
		int readedNum=0;
		int fileSize=(int)file.length();
		
		while(true){
			int curReaded=in.read(bts, readedNum,fileSize-readedNum);
			if(curReaded==0){
				break;
			}else{
				readedNum+=curReaded;
			}
		}
		out.writeInt(bts.length);
		out.write(bts,0,bts.length);
		out.flush();
	}
	/**
	 * �Ӵ���ļ��ж�ȡ������Ϣ
	 * @param appFilePath
	 * @return
	 */
	public static CAppInfo readBasicInfo(String appFilePath){
		CAppInfo appInfo=new CAppInfo();
		DataInputStream in=null;
		try{
		  in=new DataInputStream(new FileInputStream(appFilePath));
		  appInfo.name=in.readUTF();
		  appInfo.version=in.readUTF();
		  //��ȡ�ṩ�ߣ����µ�ַ
		  appInfo.provider=in.readUTF();
		  appInfo.updateUrl=in.readUTF();
		  //��ȡ����������Ϣ
		  appInfo.describe=in.readUTF();
		  //��ֵ����·��
		  appInfo.packLocation=appFilePath;
		}catch(Exception e){
		  throw new RuntimeException(ErrorMessages.Uncompress_Error+e.getMessage());
		}finally{
		  if(in!=null){
			  try{in.close();}catch(Exception e){};
		  }
		}
		return appInfo;
	}
	/**
	 * ��ȡappFilePathλ�ô��ĳ����ļ����������ļ���ѹ��
	 * @param appFilePath �����ļ�·��
	 * @param targetPath �ͷŵ�Ŀ��·��
	 */
	public CAppInfo uncompress(String appFilePath,String targetPath){
		DataInputStream in=null;
		CAppInfo appInfo=null;
		try{
		  in=new DataInputStream(new FileInputStream(appFilePath));
		  appInfo=new CAppInfo();
		  appInfo.name=in.readUTF();
		  appInfo.version=in.readUTF();
		  //��ȡ�ṩ�ߣ����µ�ַ
		  appInfo.provider=in.readUTF();
		  appInfo.updateUrl=in.readUTF();
		  //��ȡ����������Ϣ
		  appInfo.describe=in.readUTF();
		  
		  //��������Ŀ¼
		  String appFolderStr=targetPath+File.separator+appInfo.name+"_"+appInfo.version;
		  File appFolder=new File(appFolderStr);
		  appFolder.mkdirs();
		  appInfo.basePath=appFolderStr;
		  //��ѹlogo.png
		  if(in.readBoolean()){//�����logo�ļ�
		   String logoStr=appFolderStr+File.separator+"logo.png";
		   this.writeFileData(logoStr, in);
		  }
		  //��ѹmain.swf�ļ�
		  String mainStr=in.readUTF();//main.swf����·��
		  appInfo.mainSwfPath=mainStr;
		  //this.writeFileData(mainStr, in);
		  //��ѹplash.swf�ļ�
		  String splashStr=in.readUTF();//splash.swf����·��
		  appInfo.splashSwfPath=splashStr;
		  //this.writeFileData(splashStr, in);
		  //��ѹģ���ļ���ͼ���ļ�
		  this.writeFileList(appFolderStr,in);
		  //��ѹ��չ����Ϣ
		  String libSavePath=appFolderStr+File.separator+"/lib";
		  new File(libSavePath).mkdirs();//��չ��洢Ŀ¼
		  int libNum=in.readInt();
		  for(int i=0;i<libNum;i++){
			  String libName=in.readUTF();
			  this.writeFileData(libSavePath+"/"+libName, in);
		  }
		  //��ѹ�����ļ�
		  this.writeConfigFile(appFolderStr,appInfo,in);
		  //�洢������Ϣ��property.prop�ļ�
		  writePropertyFile(appFolderStr,appInfo);
		}catch(Exception e){
			throw new RuntimeException(ErrorMessages.Uncompress_Error+e.getMessage());
		}finally{
			if(in!=null){
				try{in.close();}catch(Exception e){};
			}
		}
		return appInfo;
	}
	private void writeFileList(String appFolderStr,DataInputStream in)throws Exception{
	    //uncompressģ���ļ�
		int moudleNum=in.readInt();
		for(int i=0;i<moudleNum;i++){
			String subPathStr=in.readUTF();
			writeFileData(appFolderStr+subPathStr,in);
		}
		//uncompressͼ���ļ�
		moudleNum=in.readInt();
		for(int i=0;i<moudleNum;i++){
			String subPathStr=in.readUTF();
			writeFileData(appFolderStr+subPathStr,in);
		}
	}
	/**
	 * ��ѹconfig.ini�����ļ�
	 * @param appFolderStr
	 * @param in
	 * @throws Exception
	 */
	private void writeConfigFile(String appFolderStr,CAppInfo appInfo,DataInputStream in)throws Exception{
		int width=in.readInt();int height=in.readInt();
		appInfo.splashWidth=width;appInfo.splashHeight=height;
		
		String configFilePath=appFolderStr+File.separator+Key_Config_File;
		FileWriter fileWriter=new FileWriter(configFilePath);
		fileWriter.write(ConfigerLoader.key_splash+"="+appInfo.splashSwfPath+"\r\n");
		fileWriter.write(ConfigerLoader.key_splash_size+"="+width+"*"+height+"\r\n");
		fileWriter.write(ConfigerLoader.key_runtime+"="+appInfo.mainSwfPath+"\r\n");
		fileWriter.flush();
		fileWriter.close();
	}
	public static final String Key_Modules_Folder="modules";
	public static final String Key_Icons_Folder="icons";
	
	public static final String Key_Config_File="config.ini";
	public static final String Key_Property_File="property.prop";
	/**
	 * �������������Ϣд�뵽��Ϥ�ļ���
	 * @param appFolderStr
	 * @param appInfo
	 */
	private void writePropertyFile(String appFolderStr,CAppInfo appInfo)throws Exception{
		String path=appFolderStr+File.separator+Key_Property_File;
		File propFile=new File(path);
		propFile.createNewFile();
		DataOutputStream out=null;
		try{
		 out=new DataOutputStream(new FileOutputStream(propFile));
		 out.writeUTF(appInfo.name);
		 out.writeUTF(appInfo.version); 
		 out.writeUTF(appInfo.provider);
		 out.writeUTF(appInfo.updateUrl);//���µ�ַ
		 out.writeUTF(appInfo.describe);
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(out!=null){
				out.flush();
				out.close();
			}
		}
	}
	private void writeFileData(String toPath,DataInputStream in)throws Exception{
		int len=in.readInt();
		FileOutputStream fileOut=null;
		try{
		  File toFile=new File(toPath);
		  toFile.getParentFile().mkdirs();
		  fileOut=new FileOutputStream(toPath);
		  //��ȡ����
		  byte[] bts=new byte[len];
		  int readedNum=0;
		  while(true){
			int curReaded=in.read(bts, readedNum,len-readedNum);
			readedNum+=curReaded;
			if(readedNum>=len){
					break;
			}
		  }
		  //д�����ݵ��ļ�
		  fileOut.write(bts);
		}finally{
		  if(fileOut!=null){
			  fileOut.flush();
			  fileOut.close();
		  }
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		unCompress();
		//compress();
	}
	static void unCompress(){
		new PackageTool().uncompress("C:/DBExplorer_2010-03-18.rcp", "C:/rcp");
	}
	static void compress(){
		CAppInfo appInfo=new CAppInfo();
	   	appInfo.basePath="D:/temp/dbExplorer";
	   	appInfo.name="DBExplorer";
	   	appInfo.version="2010-03-18";
	   	appInfo.provider="smartDB";
	   	appInfo.describe="data base explorer";
	   	//appInfo.logoPath="C:/smartrcp.png";
	   	appInfo.mainSwfPath="D:/temp/dbExplorer/modules/main.swf";
	   	appInfo.splashSwfPath="D:/temp/dbExplorer/modules/DBSplash.swf";
	   	appInfo.splashWidth=300;appInfo.splashHeight=300;
	   	
	   	appInfo.addLib("D:/temp/com.mysql.jdbc.Driver_1.0.0.201003301725.jar");
	   	
	   	new PackageTool().compress(appInfo, "C:");
	}

}
