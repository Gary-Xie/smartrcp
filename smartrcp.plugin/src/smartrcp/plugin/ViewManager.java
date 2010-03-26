package smartrcp.plugin;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.CLayoutBasicInfo;
import cn.smartinvoke.rcp.CPageLayout;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.smartrcp.core.Perspective;
import cn.smartinvoke.smartrcp.core.SmartRCPViewPart;
import cn.smartinvoke.smartrcp.gui.FlashViewPart;
import cn.smartinvoke.smartrcp.gui.SplashWindow;
import cn.smartinvoke.smartrcp.gui.module.CObservable;

/**
 * ��ͼ����������Ҫ��flex���ô���ķ�����ʵ��java��flex��ͬ�� �������ͼ������ViewPart ,Shell����
 * 
 * @author pengzhen
 * 
 */
public class ViewManager  extends CObservable implements IServerObject{
	private IWorkbenchWindow workbenchWindow;
	public ViewManager(IWorkbenchWindow workbenchWindow) {
		this.workbenchWindow=workbenchWindow;
	}
	/**
	 * ����������Ϣ��һviewPart
	 * ���CLayoutBasicInfo ��viewId��.swf��β����FlashViewPart��ͼ�������ض�Ӧ��swf��
	 * �����viewId��plugin.xml�ж������ͼ��
	 * 
	 * @param basicInfo
	 * @return
	 */
	public FlashViewer openViewPart(CLayoutBasicInfo basicInfo,boolean isMultiple, int state) {
		try {
			FlashViewer ret=null;int appId=-1;
			IWorkbenchPage page=this.workbenchWindow.getActivePage();
			if (basicInfo != null) {
				String viewId=basicInfo.getViewId();
				if(viewId!=null){
				  // IWorkbenchPage page=SmartRCPBuilder.window.getActivePage();
				   
				   if(viewId.endsWith(".swf")){//�����swf
					  appId = FlashViewer.getViewNum();
					  basicInfo.autoLoad=true;//����Ϊtrue���Ա�FlashViewPart�Զ�����swf
					  Perspective.swfLayoutMap.put(Integer.valueOf(appId),basicInfo);
					  
					  FlashViewPart flashViewPart=(FlashViewPart)page.showView(FlashViewPart.ID, appId + "",state);
					  ret=flashViewPart.getFlashViewer();
				   }else{
					  Integer num=FlashViewer.getViewNum();
					  IViewPart showView=null;
					  if(isMultiple){
						 try{
						 showView=page.showView(viewId, num+"", state);
						 }catch(Exception e){e.printStackTrace();};
					  }
					  if(showView==null){
					    showView=page.showView(viewId);
					  }
					  if(showView!=null && (showView instanceof SmartRCPViewPart)){
						  SmartRCPViewPart smartRCPViewPart=(SmartRCPViewPart)showView;
						  ret=smartRCPViewPart.getFlashViewer();
					  }
				   }
				}
			}
			return ret;
		} catch (Throwable e) {
			if(basicInfo!=null){
			  throw new RuntimeException("view "+basicInfo.getViewId()+" create fault,check isMultiple param or viewId");
			}else{
			  throw new RuntimeException(e.getMessage());
			}
		}
	}
	public void showViewPart(String appId){
		if(appId!=null){
		IWorkbenchPage page=this.workbenchWindow.getActivePage();
		 FlashViewer flashViewer=this.findFlashViewer(appId);
		 if(flashViewer!=null){//���appId��Ӧ����ͼ�Ѿ���ʼ��
		  Object parent=flashViewer.getParent();
		  if(parent instanceof IViewPart){
			  IViewPart part=(IViewPart)parent;
			  page.activate(part);
		  }
		 }else{//��û��ʼ���򲻴���
		    IViewReference[] refs=page.getViewReferences();
		    if(refs!=null){
		    	String moduleStr=
		    		CPageLayout.Instance.getModuleStr(appId);
		    	if(moduleStr==null){
		    		throw new RuntimeException("appIdΪ"+appId+"����ͼ�����������޷���");
		    	}
		    	for(int n=0;n<refs.length;n++){
		    		IViewReference ref=refs[n];
		    		if(ref.getId().equals(FlashViewPart.ID)){//flash������ͼ
		    			if(ref.getSecondaryId().equals(appId)){
		    				page.activate(ref.getPart(true));
		    				break;
		    			}
		    		}else{//java������ͼ
		    			if(ref.getId().equals(moduleStr)){
		    				page.activate(ref.getPart(true));
		    				break;
		    			}
		    		}
		    	}
		    }
		 }
		}
	}
	/**
	 * ɾ��viewPart��FlashViewer�����Լ�ģ���Ӧ���е�������Ϣ
	 * @param part
	 */
	private void deletePartInfo(IWorkbenchPart part){
		if(part==null || !(part instanceof IViewPart)){
			return;
		}
		//��FlashViewer������ɾ������ǰ�رյ�ViewPart
		List<FlashViewer> curViews=FlashViewer.getViewers();//��ǰ������FlashViewer
		for(int n=0;n<curViews.size();n++){
			FlashViewer viewer=curViews.get(n);
			if(viewer.getParent().equals(part)){//
				//��PageLayout��appIdģ���Ӧ����ɾ����part����Ϣ
				CPageLayout.Instance.removeViewPartInfo(viewer.getModulePath(), viewer.getAppId());
				
				curViews.remove(n);
				break;
			}
		}
	}
	
	/**
	 * �رռ�����modulePathģ���������ͼ
	 * @param modulePath
	 */
	public void closeViewParts(String modulePath){
		if(modulePath!=null){
		  List<FlashViewer> views=this.findFlashViewers(modulePath);
		  for(int n=0;n<views.size();n++){
			  FlashViewer viewer=views.get(n);
			  this.closeViewPart(viewer.getParent());
		  }
		}
	}
	
	/**
	 * �ر�ָ��appId����ͼ
	 * @param appId
	 */
	public void closeViewPart(String appId){
		if(appId!=null){
			 FlashViewer flashViewer=this.findFlashViewer(appId);
			 if(flashViewer!=null){
			  this.closeViewPart(flashViewer.getParent());
			 }
		}
	}
	private void closeViewPart(Object parent){
		
		if(parent!=null && parent instanceof ViewPart){
			IWorkbenchPage page=this.workbenchWindow.getActivePage();
			ViewPart viewPart=(ViewPart)parent;
			//ɾ����viewPart��������Ϣ
			deletePartInfo(viewPart);
			
			page.hideView(viewPart);
			viewPart.dispose();
		}
	}
	//--------------------FlashView�Ĺ�����
	/**
	 * ��ָ����viewpart��״̬������󻯣���С�������Ǳ���ԭ��
	 */
	public void  setState(String viewPartId,int state){
		//FlashViewer ret=null;
		if(viewPartId!=null){
		  IWorkbenchPage page=this.workbenchWindow.getActivePage();
		  FlashViewer flashViewer=this.findFlashViewer(viewPartId);
		  if(flashViewer!=null){
			  Object parent=flashViewer.getParent();
			  if(parent!=null && parent instanceof ViewPart){
				    ViewPart viewPart=(ViewPart)parent;
					//IWorkbenchPage page=SmartRCPBuilder.window.getActivePage();
				    
					//���Ȼ�ý���
					page.activate(viewPart);
					//����state
					IWorkbenchPartReference partReference=page.getActivePartReference();
					if(page.getPartState(partReference)!=state){
					 page.setPartState(partReference,state);
					}
			  }
		  }
		}
		//return ret;
	}
	
	/**
	 * ��õ�ǰ��ý����viewPart
	 * @return
	 */
	public FlashViewer getActiveViewPart(){
		FlashViewer ret=null;
		IWorkbenchPage page=this.workbenchWindow.getActivePage();
		IWorkbenchPart part=page.getActivePart();
		
		if(part!=null){
		  if(part instanceof FlashViewPart){
			FlashViewPart flashViewPart=(FlashViewPart)part;
			ret=flashViewPart.getFlashViewer();
		  }else{
			//java��ViewPart
			List<FlashViewer> curViews=FlashViewer.getViewers();
			for(int n=0;n<curViews.size();n++){
				FlashViewer viewer=curViews.get(n);
				if(viewer.getParent().equals(part)){
					return viewer;
				}
			}
		  }
		}
		return ret;
	}
	public List<String> findAppIds(String modulePath){
		
		/**List<String> ret=new LinkedList<String>();
		//���
		IViewReference[] refs=SmartRCPBuilder.window.getActivePage().getViewReferences();
		if(refs!=null){
			for(int n=0;n<refs.length;n++){
				IViewReference ref=refs[n];
				
			}
		}*/
		List<String> ret=null;
		if(modulePath!=null){
			if(modulePath.endsWith(".swf")){
			 modulePath=CPerspective.getRuntimeSwfFolder() + "/"+modulePath;
			}
			ret=CPageLayout.Instance.getModuleIdAppIdMap().get(modulePath);
		}
		return ret;
	}
	public List<FlashViewer> findFlashViewers(String modulePath){
		List<FlashViewer> ret=new LinkedList<FlashViewer>();
		if(modulePath!=null){
			if(modulePath.endsWith(".swf")){
			  modulePath=CPerspective.getRuntimeSwfFolder() + "/"+modulePath;
			}
			List<FlashViewer> curViews=FlashViewer.getViewers();//��ǰ������FlashViewer
			for(int n=0;n<curViews.size();n++){
				FlashViewer viewer=curViews.get(n);
				if(viewer.getModulePath().equals(modulePath)){
					ret.add(viewer);
				}
			}
		}
		return ret;
	}
	
	public FlashViewer findFlashViewer(String appId){
		if(appId!=null){
			List<FlashViewer> curViews=FlashViewer.getViewers();//��ǰ������FlashViewer
			for(int n=0;n<curViews.size();n++){
				FlashViewer viewer=curViews.get(n);
				if(viewer.getAppId().equals(appId)){
					return viewer;
				}
			}
		}
		return null;
	}
	private ViewPart findViewPart(String appId){
		ViewPart ret=null;
		FlashViewer flashViewer=this.findFlashViewer(appId);
		if(flashViewer!=null){
			Object parent=flashViewer.getParent();
			if(parent instanceof IViewPart){
			  	 ret=(ViewPart)parent;
			}
		}
		return ret;
	}
	public void resetViews(){
		IWorkbenchPage page=this.workbenchWindow.getActivePage();
		page.resetPerspective();
	}
	public void close(){
		IWorkbenchPage page=this.workbenchWindow.getActivePage();
		page.close();
	}
	public void activate(String appId){
		ViewPart viewPart=this.findViewPart(appId);
		IWorkbenchPage page=this.workbenchWindow.getActivePage();
		if(viewPart!=null){
			page.activate(viewPart);
		}
	}
	public void bringToTop(String appId){
		ViewPart viewPart=this.findViewPart(appId);
		IWorkbenchPage page=this.workbenchWindow.getActivePage();
		if(viewPart!=null){
			page.bringToTop(viewPart);
		}
	}
	public void hideView(String appId){
		ViewPart viewPart=this.findViewPart(appId);
		if(viewPart!=null){
			IWorkbenchPage page=this.workbenchWindow.getActivePage();
			page.hideView(viewPart);
		}
	}
	public boolean isPartVisible(String appId){
		ViewPart viewPart=this.findViewPart(appId);
		//if(viewPart!=null){
		IWorkbenchPage page=this.workbenchWindow.getActivePage();
		return page.isPartVisible(viewPart);
	}
	public static void main(String[] args) {
		
	}
	public void dispose() {
		super.dispose();
	}
}
