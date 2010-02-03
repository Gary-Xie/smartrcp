package cn.smartinvoke.smartrcp;

import java.io.File;

import org.eclipse.ui.PlatformUI;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.rcp.CPerspective;

/**
 * ȫ�ַ����࣬���ฺ�����ǰӦ�ó�����Ϣ�����ṩ�����˳���һЩʵ�÷���
 * @author pengzhen
 *
 */
public class CApplication implements IServerObject {

	public CApplication() {
	   	
	}
	/**
	 * ��ó���İ�װ·��
	 * @return
	 */
	public String getBaseFolder(){
		return CPerspective.getRuntimeSwfFolder();
	}
	/**
	 * �˳�����
	 */
	public void exit(){
		PlatformUI.getWorkbench().close();
	}
	public String getRuntimeSwfPath(){
		return CPerspective.getRuntimeSWFPath();
	}
	private String debugPath;
	public void setDebugPath(String path){
		this.debugPath=path;
	}
	public String getDebugPath(){
		return this.debugPath;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	   	File f=null;
	   	f.list();
	   	
	}
	public void dispose() {
	   
	}
}
