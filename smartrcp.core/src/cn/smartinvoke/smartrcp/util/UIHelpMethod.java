package cn.smartinvoke.smartrcp.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.gui.Flash;

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
	 * @param args
	 */
	public static void main(String[] args) {
		Display display=null;
		Flash flash=null;
		
	}
	@Override
	public void dispose() {
		
	}

}
