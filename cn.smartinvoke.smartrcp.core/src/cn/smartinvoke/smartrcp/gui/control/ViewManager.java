package cn.smartinvoke.smartrcp.gui.control;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.CLayoutBasicInfo;
import cn.smartinvoke.rcp.CPageLayout;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.smartrcp.core.Perspective;
import cn.smartinvoke.smartrcp.core.SmartRCPViewPart;
import cn.smartinvoke.smartrcp.gui.FlashViewPart;
import cn.smartinvoke.smartrcp.gui.module.CObservable;
import cn.smartinvoke.smartrcp.gui.module.CPartEvent;
import cn.smartinvoke.smartrcp.util.UIHelpMethod;
import cn.smartinvoke.util.Log;

/**
 * ��ͼ����������Ҫ��flex���ô���ķ�����ʵ��java��flex��ͬ�� �������ͼ������ViewPart ,Shell����
 * 
 * @author pengzhen
 * 
 */
public class ViewManager  extends CObservable implements IServerObject{
    private IWorkbenchPage page;
    /**
     * ��ʵ��
     */
    public static final ViewManager Instance=new ViewManager();
    /**
     * Ϊ��FlashViewPart��ͼ����FlashViewer���󣬲���ӵ�FlashViewer���ϣ��Ա�ViewManagerͳһ����
     * @param num
     * @param viewId
     * @param workbenchPart
     * @return
     
    public static FlashViewer fillViewerList(String num,String viewId,IWorkbenchPart workbenchPart){
    	//������java ViewPart��Ӧ��FlashViewer
		 FlashViewer flashViewer=new FlashViewer(num+"");
		 flashViewer.setModulePath(viewId);
		 flashViewer.setParent(workbenchPart);
		   //��ӵ�flashViewer����
		   //�����flashViewer����Ĳ�����Flash���������ǽ�swt��ViewPart ����FlashViewer���Դ�
		 FlashViewer.add_Viewer(flashViewer);
		//����ǰ�򿪵�viewPart����Ϣ��ӽ�ģ���Ӧ����
		 SplashWindow.getPerspective().
			page.addViewPartInfo(flashViewer.getModulePath(), flashViewer.getAppId());
		 
		 return flashViewer;
    }*/
	private ViewManager() {
        
	}
	public void initIWorkbenchPageListener(IWorkbenchPage page){
		this.page=page;
		if(page!=null){
			page.addPartListener(new IPartListener(){

				public void partActivated(IWorkbenchPart part) {
				   if(part instanceof SmartRCPViewPart){
					fireEvent(CPartEvent.Part_Activated, (SmartRCPViewPart)part);
				   }
				}

				public void partBroughtToTop(IWorkbenchPart part) {
					if(part instanceof SmartRCPViewPart){
					  fireEvent(CPartEvent.Part_BroughtToTop,(SmartRCPViewPart)part);
					}
				}

				public void partClosed(IWorkbenchPart part) {
					//��FlashViewer������ɾ������ǰ�رյ�ViewPart
					/**List<FlashViewer> curViews=FlashViewer.getViewers();//��ǰ������FlashViewer
					for(int n=0;n<curViews.size();n++){
						FlashViewer viewer=curViews.get(n);
						if(viewer.getParent().equals(part)){//
							//��PageLayout��appIdģ���Ӧ����ɾ����part����Ϣ
							CPageLayout.Instance.removeViewPartInfo(viewer.getModulePath(), viewer.getAppId());
							//����flex������
							fireFlashViewer(CPartEvent.Part_Closed,viewer);
							//curViews.remove(n);
							//curViews.remove(viewer);
							
							viewer.dispose();//�ͷŶ���
							Log.println("curSize="+curViews.size());
							
							break;
						}
					}*/
					if(part instanceof SmartRCPViewPart){
					   FlashViewer flashViewer=FlashViewer.getViewerByParent((SmartRCPViewPart)part);
					   if(flashViewer!=null){
						 //����flex������
						   try{
							   
						     fireFlashViewer(CPartEvent.Part_Closed,flashViewer);
						     flashViewer.disposeResource();//�ͷŶ�����Դ
						     Log.println("curSize="+FlashViewer.getViewers().size());
						     
						   }catch(Throwable e){
							   e.printStackTrace();
						   }
					   }
					}
				}

				public void partDeactivated(IWorkbenchPart part) {
					if(part instanceof SmartRCPViewPart){
					  fireEvent(CPartEvent.Part_Deactivated, (SmartRCPViewPart)part);
					}
				}
				public void partOpened(IWorkbenchPart part) {
				  //��Flexģ����Լ������̳�SmartRCPViewPart�࣬����FlashViewPart���͵�ViewPart�Ĵ�
				  if(part instanceof SmartRCPViewPart){
					 fireEvent(CPartEvent.Part_Opened, (SmartRCPViewPart)part);
				     if(part instanceof FlashViewPart){
				    	 ((FlashViewPart)part).getFlashViewer().loadFlash();
				     }
				  }	 
				}
			});
		}
	}
	/**
	 * 
	 * @param type ��ѡֵ:Part_Activated ��
	 * @param part
	 */
	public void fireEvent(int type,SmartRCPViewPart part){
		//FlashViewer flashViewer=null;
		if(part!=null){
			List<FlashViewer> flashViewers=FlashViewer.getViewers();
			for(int n=0;n<flashViewers.size();n++){
				FlashViewer flashViewer=flashViewers.get(n);
				if(flashViewer.getParent().equals(part)){
					fireFlashViewer(type,flashViewer);
					break;
				}
			}
		}
	}
	private void fireFlashViewer(int type,FlashViewer flashViewer){
		if(flashViewer!=null){
			CPartEvent partEvent=new CPartEvent();
			partEvent.type=type;
			partEvent.taget=flashViewer;
			
			ViewManager.this.fireEvent(partEvent);
		}
	}
    //private Map<String,Integer> multiplViewNum=new HashMap<String, Integer>();
	/**
	 * ����������Ϣ��һviewPart
	 * ���CLayoutBasicInfo ��viewId��.swf��β����FlashViewPart��ͼ�������ض�Ӧ��swf��
	 * �����viewId��plugin.xml�ж������ͼ������allowMultipleһ��Ϊtrue
	 * 
	 * @param basicInfo
	 * @return
	 */
	public FlashViewer openViewPart(CLayoutBasicInfo basicInfo,boolean isMultiple, int state) {
		try {
			FlashViewer ret=null;
			if (basicInfo != null) {
				String viewId=basicInfo.viePartId;String modulePath=basicInfo.modulePath;
				if(viewId!=null && modulePath!=null){
					int moduleId= FlashViewer.getViewNum();//ģ���Ψһ��ʶ��
					basicInfo.autoLoad=true;//����Ϊtrue���Ա�FlashViewPart�Զ�����swf
					
					//ע�᲼����Ϣ��ȫ�ֱ������Ա��ӦviewPart������ʱ����Եõ�
					Perspective.swfLayoutMap.put(Integer.valueOf(moduleId),basicInfo);
					
					IViewPart iViewPart=this.page.showView(viewId,moduleId+"",state);
					if(iViewPart instanceof SmartRCPViewPart){
						ret=((SmartRCPViewPart)iViewPart).getFlashViewer();
					}
				}else{
					throw new RuntimeException("CLayoutBasicInfo 's viePartId and modulePath property can not be null");
				}
			}
			return ret;
		} catch (Throwable e) {
			e.printStackTrace();
			if(basicInfo!=null){
			  throw new RuntimeException("view "+basicInfo.modulePath+" create fault,check isMultiple param or viewId");
			}else{
			  throw new RuntimeException(e.getMessage());
			}
		}
	}
	public void showViewPart(String appId){
		if(appId!=null){
		 FlashViewer flashViewer=this.findFlashViewer(appId);
		 if(flashViewer!=null){//���appId��Ӧ����ͼ�Ѿ���ʼ��
		  Object parent=flashViewer.getParent();
		  if(parent instanceof IViewPart){
			  IViewPart part=(IViewPart)parent;
			  this.page.activate(part);
		  }
		 }else{//��û��ʼ���򲻴���
		    IViewReference[] refs=this.page.getViewReferences();
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
		    				this.page.activate(ref.getPart(true));
		    				break;
		    			}
		    		}else{//java������ͼ
		    			if(ref.getId().equals(moduleStr)){
		    				this.page.activate(ref.getPart(true));
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
		List<String> ret=null;
		if(modulePath!=null){
			modulePath=UIHelpMethod.getFullPath(modulePath);
			ret=CPageLayout.Instance.getModuleIdAppIdMap().get(modulePath);
		}
		return ret;
	}
	public List<FlashViewer> findFlashViewers(String modulePath){
		List<FlashViewer> ret=new LinkedList<FlashViewer>();
		if(modulePath!=null){
			modulePath=UIHelpMethod.getFullPath(modulePath);
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
		this.page.resetPerspective();
		/*this.page.activate(part);
		this.page.bringToTop(part);
		this.page.close();
		this.page.hideView(view);
		this.page.isPartVisible(part);
		this.page.setPartState(ref, state)
		this.page.f*/
	}
	public void close(){
		this.page.close();
	}
	public void activate(String appId){
		ViewPart viewPart=this.findViewPart(appId);
		if(viewPart!=null){
			this.page.activate(viewPart);
		}
	}
	public void bringToTop(String appId){
		ViewPart viewPart=this.findViewPart(appId);
		if(viewPart!=null){
			this.page.bringToTop(viewPart);
		}
	}
	public void hideView(String appId){
		ViewPart viewPart=this.findViewPart(appId);
		if(viewPart!=null){
			this.page.hideView(viewPart);
		}
	}
	public boolean isPartVisible(String appId){
		ViewPart viewPart=this.findViewPart(appId);
		//if(viewPart!=null){
		return this.page.isPartVisible(viewPart);
	}
	public static void main(String[] args) {
		
	}
	public void dispose() {
		super.dispose();
	}
}
