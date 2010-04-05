package org.smartrcp.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.smartrcp.gui.module.CEventBean;
import cn.smartinvoke.smartrcp.gui.module.CObservable;
/**
 * �ļ������ƶ�ȡ���������ṩ�Զ����ƴ��ļ��Ķ�ȡ������
 * 
 * �����ṩ�ķ���ȫΪ�첽�����������ʺ϶Դ��ļ��Ķ�ȡ
 * 
 * ����̳�CObservable�࣬��ʹ���������Ӽ�����������
 * @author pengzhen
 */
public class CFileStreamReader extends CObservable implements IServerObject {
	
    /**
     * �ļ���ȡ��
     */
    private InputStream in=null;
    /**
     * ��Ҫ��ȡ���ļ�
     */
    private File readFile;
    /**
     * һ�ζ�ȡ�ļ��Ļ������Ĵ�С
     */
    private int bufferSize=1024;
    /**
     * @param path
     * @throws FileNotFoundException 
     */
	public CFileStreamReader(String path) throws FileNotFoundException{
		    this.readFile=new File(path);
			in=new FileInputStream(this.readFile);
	}
	/**
	 * ��java.io.InputStream.skip������ͬ
	 * @param n
	 * @return
	 * @throws IOException
	 */
	public long skip(long n) throws IOException{
		return this.in.skip(n);
	}
	/**
	 * ��java.io.InputStream.available������ͬ
	 * @return
	 * @throws IOException
	 */
	public int available() throws IOException {
		return this.in.available();
	}
	/**
	 * ��java.io.FileInputStream.mark������ͬ
	 * @param readlimit
	 */
	public  void mark(int readlimit){
		this.in.mark(readlimit);
	}
	/**
	 * ��java.io.FileInputStream.reset������ͬ
	 */
	public  void reset() throws IOException{
		this.in.reset();
	}
	/**
	 * ��java.io.FileInputStream.markSupported������ͬ
	 */
	public boolean markSupported(){
		return this.in.markSupported();
	}
	//=========================================
	/**
	 * ����Ҫ��ȡ�ļ��Ĵ�С
	 * @return
	 */
	public long getSize(){
		return this.readFile.length();
	}
	
	public int getBufferSize() {
		return bufferSize;
	}
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	/**
	 * ���첽��ʽ���Զ��������ķ�ʽ��ȡ�ļ�����
	 * 
	 * �÷���������첽�̶߳�ȡ�ļ����ݣ����Ե��µ�ǰUI�̵߳�����
	 * @param bufferSize һ�ζ�ȡ�ֽڵĶ���
	 */
	public void readBytes(int bufferSize){
	   this.setBufferSize(bufferSize);
	   //TODO ����һ���̶߳�ȡ�ļ����ݣ������̳߳�Ч�������Щ
	   Thread thread=new Thread(){
		   public void run(){
			   while(true){
				   byte[] buf=new byte[getBufferSize()];
				   try{
					 //��ȡ
					 int readNum=in.read(buf);
					 if(readNum==-1){//�����ļ�ĩβ
						 break;
					 }else{
						 //����flex������
						 CBytesBean bytesBean=new CBytesBean(readNum, buf);
						 fireEvent(bytesBean);
					 }
				   }catch(IOException e){
					   //��ȡ�����׳��쳣��flex
					    fireEvent(new RuntimeException(e));
				   }
			   }
		   }
	   };
	   thread.setDaemon(true);
	   thread.start();
	}
	
	
	public void dispose() {
		 this.close();
	}
	/**
	 * �ر�����Դ
	 */
	public void close(){
		//�ر���Դ
	     if(in!=null){
	    	 try{in.close();}catch(Exception e){e.printStackTrace();}
	     }
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
