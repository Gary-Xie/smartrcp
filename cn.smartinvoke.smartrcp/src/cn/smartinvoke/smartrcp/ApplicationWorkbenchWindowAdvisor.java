package cn.smartinvoke.smartrcp;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    	
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
    	
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
    	SmartRCPBuilder.preWindowOpen(this.getWindowConfigurer());
    }
    public void postWindowOpen() {
    	final Shell shell=this.getWindowConfigurer().getWindow().getShell();
    	//shell.setMaximized(maximized)
    	//System.out.println(">>>>>>>>>>>>>>>>>>postWindowOpen");
    	SmartRCPBuilder.postWindowOpen(shell);
    }
}
