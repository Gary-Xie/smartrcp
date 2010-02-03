package cn.smartinvoke.smartrcp;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
	//private static final String PERSPECTIVE_ID = "cn.smartinvoke.smartrcp.coreperspective";

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    	//��ʼ��smartRCP�������������������cn.smartinvoke.smartrcp.core���ܶ�̬����������
		SmartRCPBuilder.init(new MyServiceObjectCreator());
		
    	//---------����ͼ��ע����Ϣ
    	SmartRCPBuilder.initImageRegistry(Activator.plugin.getImageRegistry());
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		
		return "cn.smartinvoke.smartrcp.core.perspective";// cn.smartinvoke.smartrcp.core.Perspective.ID;
	} 
	
}
