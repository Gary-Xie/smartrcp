package cn.smartinvoke.smartrcp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.smartrcp.CApplication;

/**
 * ����İ���������
 * @author pengzhen
 *
 */
public class UIHelpMethod implements IServerObject {
    /**
     * ������������data���洢��path�ļ���
     * @param data
     * @param path
     */
	public static void saveImage(byte[] data,String path){
		OutputStream out=null;
		try {
			 out=new FileOutputStream(path);
			 out.write(data);
			 out.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(out!=null){
				try{out.close();}catch (Exception e) {};
			}
		}
	}
	/**
	 * �������·�����ؾ���·��
	 * @param subPath
	 * @return
	 */
	public static String getFullPath(String subPath){
		String ret=null;
		if(subPath!=null){
			//Javaģ��λ�ã�ģ��·����*�ſ�ͷ
			if(subPath.charAt(0)=='*'){
				return subPath;
			}else
			// ����·��
			if (subPath.toLowerCase().startsWith("http://")) {
				ret = subPath;
			} else
			// ����·��
			if (subPath.indexOf(':')!=-1) {
				ret = subPath;
			} else {
				// ���·��
				String baseFolder = CApplication.Instance.getBaseFolder();
				if (subPath.charAt(0) == '/') {
					ret = baseFolder + subPath;
				} else {
					ret = baseFolder + '/' + subPath;
				}
			}
		}
		return ret.replace('\\', '/');
	}
	public static String getFullPathForJFace(String subPath){
		String fullPath=getFullPath(subPath);
		String temp=fullPath.toLowerCase();
		if(!temp.startsWith("http://")){
			return "file:/"+fullPath.replace('\\', '/');
		}else{
			return fullPath.replace('\\', '/');
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			URL url=new URL("file://");
		} catch (MalformedURLException e){
			e.printStackTrace();
		}
	}
	public void dispose() {
		
	}

}
