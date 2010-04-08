package cn.smartinvoke.smartrcp.gui;

import java.io.File;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.CLayoutBasicInfo;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.smartrcp.core.Perspective;
import cn.smartinvoke.util.ImageManager;
import cn.smartinvoke.util.Log;

public class FlashViewPart extends ViewPart implements IServerObject,
		ISaveablePart2 {
	public static final String ID = "cn.smartinvoke.smartrcp.gui.FlashViewPart"; //$NON-NLS-1$
	private CLayoutBasicInfo layoutInfo;

	private FlashViewer flashViewer;
	public IWorkbenchWindow window;
	/**
	 * viewPart�Ĺ����������˵� ����������
	 */
    private ViewPartActionBar partActionBar;
    /**
     * ��flash�Ƿ����
     */
    private boolean isLoaded=false;
	@Override
	public void createPartControl(Composite parent) {
		try {
			this.window = this.getViewSite().getWorkbenchWindow();
			String secondId = this.getViewSite().getSecondaryId();
			Map<Integer, CLayoutBasicInfo> layoutMap = Perspective.swfLayoutMap;
			if(layoutInfo==null){//�־û�״̬��û�оʹ�map��ȡ
			  layoutInfo = layoutMap.get(Integer.valueOf(secondId));
			}else{
			  layoutMap.put(Integer.valueOf(secondId), layoutInfo);
			}
			//���û���򷵻�
			if(layoutInfo==null){
				return;
			}
			String viewId = layoutInfo.modulePath;
			if (viewId != null) {
				if (layoutInfo.isModuleSwf) {
					String[] paths = new String[] {
							CPerspective.getRuntimeSWFPath(),
							CPerspective.getRuntimeSwfFolder() + "/" + viewId };
					flashViewer = new FlashViewer(secondId, parent, paths);
				} else {
					if(new File(viewId).exists()){
						flashViewer = new FlashViewer(secondId, parent,viewId);
					}else{
					    flashViewer = new FlashViewer(secondId, parent,
							CPerspective.getRuntimeSwfFolder() + "/" + viewId);
					}
				}
				// ���ø��׿ؼ�
				this.flashViewer.setParent(this);
				// ���ò�����Ϣ
				this.setViewTitle(layoutInfo.title);
                //debug
				//this.setPartName(flashViewer.getAppId());
				// ����ͼ��
				if (layoutInfo.image != null) {
					ImageDescriptor imageDescriptor = ImageManager
							.getImageDescriptor(layoutInfo.image);
					if (imageDescriptor != null) {
						this.setTitleImage(imageDescriptor.createImage());
					}
				}
				// ����swf
				//if (!isLoaded) {
					//flashViewer.loadFlash();
				//}
			}
			//��ʼ�������������˵�����������
			this.partActionBar=new ViewPartActionBar(this.getViewSite().getActionBars());
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		
	}
	/**
	 * ��ʾ��ǰ��ViewPart
	 * @param state
	 */
    public void showViewPart(int state){
    	try{
    	this.getViewSite().getPage().showView(FlashViewPart.ID,
    			this.getViewSite().getSecondaryId(), state);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	public FlashViewer getFlashViewer() {
		return flashViewer;
	}

	public ViewPartActionBar getPartActionBar() {
		return partActionBar;
	}
    
	public void setViewTitle(String title) {
		
		super.setPartName(title);
	}

	public String getViewTitle() {
		return this.getPartName();
	}

	@Override
	public void setFocus() {
		//���õ�ǰ��ý����FlashViewer
		FlashViewer.curFlashViewer=this.flashViewer;
        
	}
    
	public IToolBarManager getToolBarManager() {
		
		return this.getViewSite().getActionBars().getToolBarManager();
	}

	public IMenuManager getMenuManager() {
		return this.getViewSite().getActionBars().getMenuManager();
	}

	public void dispose() {
		// ɾ��͸��ͼ�����е�layout��Ϣ����
		try {

			Perspective.swfLayoutMap.remove(Integer.valueOf(this.flashViewer
					.getAppId()));
			if (this.flashViewer != null) {
				this.flashViewer.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.dispose();
	}
	public String getType(){
    	return "FlashViewPart";
    }
	public int promptToSaveOnClose() {
		if(!this.flashViewer.isLoaded){
			return ISaveablePart2.NO;
		}
		if(this.isDirty()&&this.isSaveAsAllowed()){
			  Object obj=
				  this.flashViewer.getFlexApp().call("promptTitleAndMessage", new Object[]{});
			  Object[] arr=null;
			  if(obj!=null || obj.getClass().isArray()){
				  arr=(Object[])obj;
			  }
			  if(arr==null || arr.length<2){
				  arr=new Object[]{"�Ƿ񱣴�","��ͼ�����Ѿ��޸ģ��Ƿ񱣴�"};
			  }
			  boolean ret=
				  MessageDialog.openConfirm(this.getViewSite().getShell(), arr[0]+"", arr[1]+"");
			  if(ret){
				 return ISaveablePart2.YES;
			  }else{
				 return ISaveablePart2.NO;
			  }
		}
		if(this.isDirty()&&this.isSaveOnCloseNeeded()){
			return ISaveablePart2.YES;
		}
		return ISaveablePart2.NO;
	}

	public void doSave(IProgressMonitor monitor) {
		if(!this.flashViewer.isLoaded){
			return ;
		}
		try{
		this.flashViewer.getFlexApp().asyncCall("doSave",
				null);
	}catch(Exception e){e.printStackTrace();};
	}

	public void doSaveAs() {
		if(!this.flashViewer.isLoaded){
			return ;
		}
		try{
		this.flashViewer.getFlexApp().asyncCall("doSaveAs", new Object[] {});
	}catch(Exception e){e.printStackTrace();};
	}

	public boolean isDirty() {
		if(!this.flashViewer.isLoaded){
			return false;
		}
		try{
		Object retObj = this.flashViewer.getFlexApp().call("isDirty",
				new Object[] {});
		if (retObj != null) {
			return (Boolean) retObj;
		} else {
			return false;
		}
	}catch(Exception e){e.printStackTrace();};
	return false;
	}

	public boolean isSaveAsAllowed() {
		if(!this.flashViewer.isLoaded){
			return false;
		}
		try{
		Object retObj = this.flashViewer.getFlexApp().call("isSaveAsAllowed",
				new Object[] {});
		if (retObj != null) {
			return (Boolean) retObj;
		} else {
			return false;
		}
	}catch(Exception e){e.printStackTrace();};
	return false;
	}

	public boolean isSaveOnCloseNeeded() {
		if(!this.flashViewer.isLoaded){
			return false;
		}
		try{
		Object retObj = this.flashViewer.getFlexApp().call(
				"isSaveOnCloseNeeded", new Object[] {});
		if (retObj != null) {
			return (Boolean) retObj;
		} else {
			return false;
		}
		}catch(Exception e){e.printStackTrace();};
		return false;
	}
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		Log.println("FlashViewPart secondId="+this.getViewSite().getSecondaryId());
		CLayoutBasicInfo layoutInfo=new CLayoutBasicInfo();
		if(layoutInfo.init(memento)){//��Ϣ�Ѿ��洢��memento��
			this.layoutInfo=layoutInfo;
			//this.isRestorePart=true;
		}
	}
	public void saveState(IMemento memento) {  
	   this.layoutInfo.save(memento);
	}
}