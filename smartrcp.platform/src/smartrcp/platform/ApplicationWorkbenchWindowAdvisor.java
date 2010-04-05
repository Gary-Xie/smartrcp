package smartrcp.platform;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}
    public void postWindowOpen() {
    	final Shell shell=this.getWindowConfigurer().getWindow().getShell();
    	SmartRCPBuilder.Instance.postWindowOpen(shell);
    	
    	
    }
//    public void postWindowCreate() {
//    	final Shell shell = getWindowConfigurer().getWindow().getShell();
//    	shell.setRegion(this.createRegion());
//    	
//    }
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		
//		configurer.setTitle("RCP Application");
//		PlatformUI.getPreferenceStore().setDefault(
//		         IWorkbenchPreferenceConstants.ENABLE_ANIMATIONS, true);
//		     PlatformUI.getPreferenceStore().setDefault(
//		         IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
//		         true);
//		     PlatformUI.getPreferenceStore().setDefault(
//		         IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
//		         IWorkbenchPreferenceConstants.TOP_RIGHT);
//		     
		SmartRCPBuilder.Instance.preWindowOpen(this.getWindowConfigurer());
		
//		configurer.setShowPerspectiveBar(true);
//		configurer.setShowProgressIndicator(true);
//		configurer.setShowStatusLine(true);
	}
	
	private Region createRegion(){
		Region region = new Region();
		Image image=new Image(Display.getCurrent(),"D:/shape1.png");
		ImageData id=image.getImageData();
		//����image�Ǧ����Ǧ�����
		if (id.alphaData != null) {
			for (int y = 0; y < id.height; y++) {
				for (int x = 0; x < id.width; x++) {
					if (id.getAlpha(x, y)!=0) {
						region.add( x, y,1,1);
					}
				}
			}
		} else {
			ImageData mask = id.getTransparencyMask();
			for (int y = 0; y < mask.height; y++) {
				for (int x = 0; x < mask.width; x++) {
					if (mask.getPixel(x, y) !=0 ) {
						region.add( x, y,1,1);
					}
				}
			}
		}
		return region;
	}
}
