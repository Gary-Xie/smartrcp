package cn.smartinvoke.smartrcp;

import java.io.File;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.application.ApplicationDescriptor;

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
