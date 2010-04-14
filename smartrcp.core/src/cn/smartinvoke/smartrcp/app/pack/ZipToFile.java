package cn.smartinvoke.smartrcp.app.pack;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.tools.zip.*;

public class ZipToFile {
	
	public static  String ZIP_FILENAME	= "C:\\XJPDA.zip";//��Ҫ��ѹ�����ļ���
	public static  String ZIP_DIR = "C:\\XJPDA\\";//��Ҫѹ�����ļ���
	public static  String UN_ZIP_DIR = "C:\\";//Ҫ��ѹ���ļ�Ŀ¼
	public static final int BUFFER = 1024 ;//�����С
	
	/**
	* zipѹ������.
	* ѹ��baseDir(�ļ���Ŀ¼)�������ļ���������Ŀ¼
	* @throws Exception
	*/
	public static void zipFile(String baseDir,String fileName,String comment) throws Exception{
		List fileList=getSubFiles(new File(baseDir));
		ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(fileName));
		ZipEntry ze=null;
		byte[] buf=new byte[BUFFER];
		int readLen=0;
		for(int i = 0; i <fileList.size(); i++) {
			File f=(File)fileList.get(i);
			ze=new ZipEntry(getAbsFileName(baseDir, f));
			ze.setSize(f.length());
			ze.setTime(f.lastModified());	
			zos.putNextEntry(ze);
			InputStream is=new BufferedInputStream(new FileInputStream(f));
			while ((readLen=is.read(buf, 0, BUFFER))!=-1) {
				zos.write(buf, 0, readLen);
			}
			is.close();
		}
		zos.setComment(comment);
		zos.close();
	}
	
	/**
	* ������Ŀ¼��������һ���ļ��������·��������zip�ļ��е�·��.
	* @param baseDir java.lang.String ��Ŀ¼
	* @param realFileName java.io.File ʵ�ʵ��ļ���
	* @return ����ļ���
	*/
	private static String getAbsFileName(String baseDir, File realFileName){
		File real=realFileName;
		File base=new File(baseDir);
		String ret=real.getName();
		while (true) {
			real=real.getParentFile();
			if(real==null) 
				break;
			if(real.equals(base)) 
				break;
			else
				ret=real.getName()+"/"+ret;
		}
		return ret;
	}
	
	/**
	* ȡ��ָ��Ŀ¼�µ������ļ��б�������Ŀ¼.
	* @param baseDir File ָ����Ŀ¼
	* @return ����java.io.File��List
	*/
	private static List getSubFiles(File baseDir){
		List ret=new ArrayList();
		File[] tmp=baseDir.listFiles();
		for (int i = 0; i <tmp.length; i++) {
			if(tmp[i].isFile())
				ret.add(tmp[i]);
			if(tmp[i].isDirectory())
				ret.addAll(getSubFiles(tmp[i]));
		}
		return ret;
	}
	
	/**
	* ��ѹ������.
	* ��zipFilePath��ѹ��saveDirĿ¼
	* @throws Exception
	*/
	public static void upZipFile(String zipFilePath,String saveDir) throws Exception{
		ZipFile zfile = null;
		try {
			zfile = new ZipFile(zipFilePath);
			Enumeration zList = zfile.getEntries();
			ZipEntry ze = null;
			byte[] buf = new byte[1024];
			while (zList.hasMoreElements()) {
				ze = (ZipEntry) zList.nextElement();
				if (ze.isDirectory()) {
					File f = new File(saveDir + File.separatorChar
							+ ze.getName());
					f.mkdir();
					continue;
				}
				OutputStream os = null;
				InputStream is = null;
				try {
					os = new BufferedOutputStream(new FileOutputStream(
							getRealFileName(saveDir + File.separatorChar, ze
									.getName())));
					is = new BufferedInputStream(zfile.getInputStream(ze));
					int readLen = 0;
					while ((readLen = is.read(buf, 0, 1024)) != -1) {
						os.write(buf, 0, readLen);
					}
				} finally {
					is.close();
					os.close();
				}
			}
		} finally {
			zfile.close();
		}
	}

	/**
	* ������Ŀ¼������һ�����·������Ӧ��ʵ���ļ���.
	* @param baseDir ָ����Ŀ¼
	* @param absFileName ���·������������ZipEntry�е�name
	* @return java.io.File ʵ�ʵ��ļ�
	*/
   public static File getRealFileName(String baseDir, String absFileName){
		String[] dirs=absFileName.split("/");
		File ret=new File(baseDir);
		if(dirs.length>1){
			for (int i = 0; i < dirs.length-1;i++) {
				ret=new File(ret, dirs[i]);
			}
			if(!ret.exists())
				ret.mkdirs();
			ret=new File(ret, dirs[dirs.length-1]);
			return ret;
		}
		return ret;
   }
   public static void main(String[] args) throws Exception {
	//  ZipToFile.ZIP_DIR=;
	  String baseDir="D:/���/Kanulau@C0930 ��ľ��Ү/chijyo0079pic.zip";
	  ZipToFile.upZipFile(baseDir, "D:/export");
	  //ZipToFile.zipFile(baseDir, "D:/ac.zip");
   }
}
