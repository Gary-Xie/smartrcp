package org.smartrcp.io;

import java.io.File;
import java.util.Date;

import cn.smartinvoke.IServerObject;
/**
 * Ŀ¼�࣬�����Ŀ¼���в���
 * @author pengzhen
 *
 */
public class CDirectory implements IServerObject {
	/**
	 * ����path��ָ�����ļ���
	 * @param path
	 */
    public static void create(String path){
    	new File(path).mkdirs();
    }
    /**
     * �ж�path�Ƿ���Ŀ¼
     * @param path
     * @return
     */
    public static boolean isDirectory(String path){
    	return new File(path).isDirectory();
    }
    /**
     * ���ظ�Ŀ¼�µ������ļ����ļ��д�С���ܺ�
     * @param path
     * @return
     */
    public static long getSize(String path){
    	return 0;
    }
    /**
     * ����pathĿ¼�µ������ļ�������·��
     * @param path
     * @return
     */
    public static String[] getSubFiles(String path){
    	return null;
    }
    /**
     * ����pathĿ¼�µ������ļ��е�����·��
     * @param path
     * @return
     */
    public static String[] getSubFolders(String path){
    	return null;
    }
    /**
     * ���pathΪ��Ŀ¼����ɾ����Ŀ¼
     * @param path
     */
    public static void delete(String path){
    	new File(path).delete();
    }
    /**
     * ɾ����Ŀ¼�µ������ļ����ļ���
     * @param path
     */
    public static void deleteAll(String path){
    	
    }
    /**
     * ��fromPathĿ¼�µ��ļ����ļ��ж�������toPathĿ¼��
     * @param fromPath
     * @param toPath
     */
    public static void copyTo(String fromPath,String toPath){
    	
    }
    /**
     * �ж�һĿ¼�Ƿ����
     * @param path
     * @return
     */
    public static boolean exists(String path){
    	return new File(path).exists();
    }
    /**
     * ���Ŀ¼�Ĵ���ʱ�䣬
     * @param path
     * @return �����ļ��д�������
     */
    public static Date getCreationTime(String path){
    	return null;
    }
    
    public void dispose() {
 	   
	}
	public static void main(String[] args) {
		
	}

}
