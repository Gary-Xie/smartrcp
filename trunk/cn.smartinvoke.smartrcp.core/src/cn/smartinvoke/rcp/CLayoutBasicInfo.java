package cn.smartinvoke.rcp;

public class CLayoutBasicInfo {
	//��ǰlayout��viewId����Ӧ��swf�Ƿ���ģ��swf
	public boolean isModuleSwf=true;
	//�Ƿ���FlashViewer�򿪵�ʱ���Զ�����swf
	public boolean autoLoad=false;
	private String viewId=null;
	private String title;
	public String image=null;
	public CLayoutBasicInfo() {
		
	}
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
