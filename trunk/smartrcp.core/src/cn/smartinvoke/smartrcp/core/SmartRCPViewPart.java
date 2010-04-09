package cn.smartinvoke.smartrcp.core;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.CLayoutBasicInfo;
import cn.smartinvoke.smartrcp.gui.SplashWindow;
/**
 * ��SmartRCP�У�ֻ������ViewPart���ֱ���FlashViewPart��
 * ��SmartRCPViewPart��ǰ�����ڼ���flexӦ�ã�������java������չViewPart�ĳ��࣬����Java��SmartRCP
 * ����չ��ʵ��Flex��java�Ļ�����
 * @author pengzhen
 *
 */
public abstract class SmartRCPViewPart extends ViewPart {
    private FlashViewer flashViewer;
	public SmartRCPViewPart() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		String appId=null;
		String secondId=this.getViewSite().getSecondaryId();
		if(secondId!=null){
			appId=secondId;
		}else{
			appId=FlashViewer.getViewNum()+"";
		}
		this.flashViewer=new FlashViewer(appId);
		CLayoutBasicInfo layoutInfo =this.getLayoutInfo();
		String moduleStr=null;
   	    if(layoutInfo!=null){
   	    	moduleStr=layoutInfo.modulePath;
   	    }else{
   	    	moduleStr=this.getConfigurationElement().getAttribute("id");
   	    }
		this.flashViewer.setModulePath(moduleStr);
		this.flashViewer.setParent(this);
		
		//��ӵ�flashViewer����
		//�����flashViewer����Ĳ�����Flash���������ǽ�swt��ViewPart ����FlashViewer���Դ�
		FlashViewer.add_Viewer(flashViewer);
		
	}
   	 /**
   	  * ��ȫ�ֲ���map��ȡ���뵱ǰviewPart��Ӧ��CLayoutBasicInfo�����û���򷵻�null
   	  * @return
   	  */
    protected CLayoutBasicInfo getLayoutInfo(){
    	String secondId=this.getViewSite().getSecondaryId();
    	Map<Integer, CLayoutBasicInfo> layoutMap = Perspective.swfLayoutMap;
   	    CLayoutBasicInfo layoutInfo =layoutMap.get(Integer.valueOf(secondId));
    	return layoutInfo;
    }
	@Override
	public void setFocus() {
		
	}
	public FlashViewer getFlashViewer() {
		return flashViewer;
	}
	public void dispose(){
		if(this.flashViewer!=null){
			Perspective.swfLayoutMap.remove(Integer.valueOf(this.flashViewer
					.getAppId()));
			FlashViewer.remove_Viewer(this.flashViewer);
		}
	}
	public String getType(){
    	return "SmartRCPViewPart";
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
