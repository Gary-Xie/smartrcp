package cn.smartinvoke.smartrcp;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.packageadmin.PackageAdmin;

import cn.smartinvoke.smartrcp.app.pack.CAppInfo;
import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;
import cn.smartinvoke.smartrcp.util.BundleHelpMethod;
import cn.smartinvoke.util.Log;

/**
 * ά��һ�����е�smatrcpӦ�ó������Ϣ��
 * ���磺��ǰ���������صĿ⣬������Ϣ
 * @author pengzhen
 *
 */
public class SmartRCPBundle{
	//��smartrcp���������ص�����bundles jar
    private List<Bundle> bundles=new  LinkedList<Bundle>();
    /**
     * Ӧ�ó�����Ϣ�࣬ά����Ӧ�ó���İ�װ·��swf�ļ�·����������Ϣ
     */
    private CAppInfo appInfo;
	public SmartRCPBundle(CAppInfo appInfo){
	  this.appInfo=appInfo;
	}
	public CAppInfo getAppInfo() {
		return appInfo;
	}
	/**
	 *���ص�ǰ�����ڴ�
	 */
    public void load()throws BundleException{
       File libFolder=new File(this.appInfo.basePath+File.separator+"lib");
       this.bundles=BundleHelpMethod.installBundles(libFolder);
    }
    /**
     *���ڴ��в�ж��ǰ����
     * @throws BundleException 
     */
    public void unLoad() throws BundleException{
    	//������leve�ķ������жbundle
    	for(int i=bundles.size()-1;i>=0;i--){
    		Bundle bundle=bundles.get(i);
    		Log.println("unLoad bundle jar:"+bundle);
    		bundle.uninstall();
    	}
    }
    /**
     * ���ڲ�����url�ϵ�osgi bundle jar
     * @param url
     */
    public void loadBundle(String url){
    	
    }
    public static void main(String[] args) {
    	
	}
}
