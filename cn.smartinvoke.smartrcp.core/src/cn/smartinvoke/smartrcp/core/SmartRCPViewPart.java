package cn.smartinvoke.smartrcp.core;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cn.smartinvoke.gui.FlashViewer;
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
		this.flashViewer.setModulePath(this.getConfigurationElement().getAttribute("id"));
		this.flashViewer.setParent(this);
		
		//��ӵ�flashViewer����
		//�����flashViewer����Ĳ�����Flash���������ǽ�swt��ViewPart ����FlashViewer���Դ�
		FlashViewer.add_Viewer(flashViewer);
		//����ǰ�򿪵�viewPart����Ϣ��ӽ�ģ���Ӧ����
		SplashWindow.getPerspective().
			page.addViewPartInfo(flashViewer.getModulePath(), flashViewer.getAppId());
		
		//super.setPartName(flashViewer.getAppId());
	}

	@Override
	public void setFocus() {
		
	}
	public FlashViewer getFlashViewer() {
		return flashViewer;
	}
	public void dispose(){
		if(this.flashViewer!=null){
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
