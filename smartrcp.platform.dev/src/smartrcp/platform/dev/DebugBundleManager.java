package smartrcp.platform.dev;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import cn.smartinvoke.IServerObject;
/**
 * ����Ե��Ե�osgi bundle���м��ع���
 * @author pengzhen
 *
 */
public class DebugBundleManager implements IServerObject {
    
	public DebugBundleManager() {
	  	
	}
	private String bundlePath;
	private Bundle  bundle;
	
	public void loadBundle(String path) throws BundleException{
		this.bundlePath=path;
		
	   	if(bundle!=null){
	   		bundle.uninstall();
	   	}
	   	
	   	bundle=
	   	Activator.getDefault().getContext().installBundle("file:"+this.bundlePath);
	   	
	}
	public void dispose() {
		
	}

}
