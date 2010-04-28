package cn.smartinvoke.smartrcp.gui.module;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.util.Log;
/**
 * ���öԻ��򣬰�����ɫ�Ի��򣬴�ӡ�Ի���Ŀ¼ѡ����ļ�ѡ����ļ���ѡ���
 * @author pengzhen
 *
 */
public class CommonDialogs implements IServerObject {

	public CommonDialogs() {
		Log.println("creat...");
	}
	/**
	 * ����ɫ�Ի���
	 * @param parent
	 * @param bean
	 */
    public void openColorDialog(Shell parent,CEventBean bean,int style){
       if(bean!=null){
    	parent=Display.getCurrent().getActiveShell();
    	ColorDialog dlg=new ColorDialog(parent,style);
    	RGB rgb=dlg.open();
    	if(rgb!=null){
    		CRGB crgb=new CRGB();
    		crgb.red=rgb.red;
    		crgb.green=rgb.green;
    		crgb.blue=rgb.blue;
    		bean.fireOnetimeEvent(crgb);
    	}else{
    		bean.fireOnetimeEvent(null);
    	}
       }
    }
    /**
     * ��Ŀ¼ѡ��Ի���
     * @param title
     * @param msg
     * @param filterPath
     */
    public void openDirectoryDialog(Shell parent,CEventBean bean,
    		                        String title,String msg,String filterPath,int style){
    	if(bean==null){
    		return;
    	}
    	parent=Display.getCurrent().getActiveShell();
    	DirectoryDialog dialog=new DirectoryDialog(parent,style);
    	if(title!=null){
    		dialog.setText(title);
    	}
    	if(msg!=null){
    		dialog.setMessage(msg);
    	}
    	if(filterPath!=null){
    		if(new File(filterPath).exists()){
    			dialog.setFilterPath(filterPath);
    		}
    	}
    	
    	String dir=dialog.open();
    	bean.fireOnetimeEvent(dir);
    }
    /**
     * ���ļ�ѡ��Ի���
     * @param parent
     * @param bean
     * @param isMulti �Ƿ��Ƕ�ѡ
     * @param filterExtensions
     */
    public void openFileSelDialog(Shell parent,CEventBean bean,boolean isMulti,Object[] filterExtensions){
    	FileDialog dlg=null;
    	if(bean==null){
    		return;
    	}
    	parent=Display.getCurrent().getActiveShell();
    	
    	if(isMulti){
    		dlg=new FileDialog(parent,SWT.OPEN | SWT.MULTI);
    	}else{
    		dlg=new FileDialog(parent,SWT.OPEN);
    	}
    	//�����ļ���׺��
    	if(filterExtensions!=null){
    		String[] filterExts=new String[filterExtensions.length];
    		for(int n=0;n<filterExts.length;n++){
    			filterExts[n]=filterExtensions[n].toString();
    		}
    		dlg.setFilterExtensions(filterExts);
    	}
    	//�򿪶Ի���
    	String selFile=dlg.open();
    	List<String> selFiles=new LinkedList<String>();
    	selFiles.add(selFile);
    	if(isMulti){
          String[] selNames=dlg.getFileNames();
          if(selNames!=null){
        	  for(int n=0;n<selNames.length;n++){
        		  selFiles.add(selNames[n]);
        	  }
          }
    	}
    	bean.fireOnetimeEvent(selFiles);
    }
    /**
     * ���ļ�����Ի���
     * @param parent
     * @param bean
     */
    public void openFileSaveDialog(Shell parent,CEventBean bean,String filterPath){
    	if(bean==null){
    		return;
    	}
    	parent=Display.getCurrent().getActiveShell();
    	
    	FileDialog dlg=new FileDialog(parent,SWT.SAVE);
    	if(filterPath!=null){
    		if(new File(filterPath).exists()){
    			dlg.setFilterPath(filterPath);
    		}
    	}
    	String selFile=dlg.open();
    	bean.fireOnetimeEvent(selFile);
    	
    }
	public void dispose() {
		
	}

}
