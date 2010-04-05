package org.smartrcp.io;

import java.io.File;

import cn.smartinvoke.IServerObject;
/**
 * �ļ��࣬������ļ���д������в���
 * @author pengzhen
 *
 */
public class CFile extends File implements IServerObject{
    
	public CFile(String pathname) {
		super(pathname);
	}
	public CFile getParentFile() {
		return new CFile(super.getParentFile().getAbsolutePath());
	}
	/**
	 * ��content�ַ���д��path�ļ��У�������ļ��Ѿ���������д���������򴴽�
	 * ���д�������׳�����ʱ�쳣RuntimeException
	 * 
	 * �÷���Ϊͬ���������ʺ���д���������ݣ����д�����������ʹ��CFileWriter��
	 * @param path �ļ�����·��
	 * @param content �ļ�����
	 * @param encode  �ַ����룬����Ϊgbk,utf-8��
	 */
	public static void writeText(String path,String content,String encode){
		
	}
	/**
	 * ��encode���뷽ʽ��ȡpath�ļ��е����ݣ������ַ�����ʽ����
	 * �����ȡ������׳�����ʱ�쳣RuntimeException
	 * 
	 * �÷���Ϊͬ���������ʺ��ڶ�ȡ�������ݣ������ȡ����������ʹ��CFileReader��
	 * 
	 * @param path
	 * @param encode
	 * @return
	 */
	public static String readText(String path,String encode){
	  return null;	
	}
	/**
	 * �÷�����readText���ƣ�ֻ�Ƿ�������Ϊ�ļ���������ɵ�����
	 * �����ȡ������׳�����ʱ�쳣RuntimeException
	 * 
	 * �÷���Ϊͬ���������ʺ��ڶ�ȡ�������ݣ������ȡ����������ʹ��CFileReader��
	 * @param path
	 * @param encode
	 * @return
	 */
	public static String[] readAllLines(String path,String encode){
		return null;
	}
	/**
	 * ���ֽ�����btsд��path���ڵ��ļ���������ļ��Ѿ���������д���������򴴽�
	 * ���д�������׳�����ʱ�쳣RuntimeException
	 * 
	 * �÷���Ϊͬ���������ʺ���д���������ݣ����д�����������ʹ��CFileStreamWriter��
	 * 
	 * @param path
	 * @param bts
	 */
	public static void writeByte(String path,byte[] bts){
		
	}
	/**
	 * ��path�����ļ������ݶ�ȡ���ֽ������У�������
	 * �����ȡ������׳�����ʱ�쳣RuntimeException
	 * 
	 * �÷���Ϊͬ���������ʺ���д���������ݣ����д�����������ʹ��CFileStreamReader��
	 * @param path
	 * @return
	 */
	public static byte[] readAllBytes(String path){
		return null;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	public void dispose() {
		
	}

}
