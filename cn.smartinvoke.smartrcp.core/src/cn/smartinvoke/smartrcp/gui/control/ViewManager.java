package cn.smartinvoke.smartrcp.gui.control;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.internal.WorkbenchPage;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.CLayoutBasicInfo;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.smartrcp.core.Perspective;
import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;
import cn.smartinvoke.smartrcp.gui.FlashViewPart;

/**
 * ��ͼ����������Ҫ��flex���ô���ķ�����ʵ��java��flex��ͬ�� �������ͼ������ViewPart ,Shell����
 * 
 * @author pengzhen
 * 
 */
public class ViewManager {

	public ViewManager() {

	}
    private Map<String,Integer> multiplViewNum=new HashMap<String, Integer>();
	/**
	 * ����������Ϣ��һviewPart
	 * ���CLayoutBasicInfo ��viewId��.swf��β����FlashViewPart��ͼ�������ض�Ӧ��swf��
	 * �����viewId��plugin.xml�ж������ͼ��
	 * 
	 * @param basicInfo
	 * @return
	 */
	public int openViewPart(CLayoutBasicInfo basicInfo,boolean isMultiple, int state) {
		try {
			int appId = -1;
			if (basicInfo != null) {
				String viewId=basicInfo.getViewId();
				if(viewId!=null){
				   if(viewId.endsWith(".swf")){//�����swf
					  appId = FlashViewer.getViewNum();
					  basicInfo.autoLoad=true;//����Ϊtrue���Ա�FlashViewPart�Զ�����swf
					  Perspective.swfLayoutMap.put(Integer.valueOf(appId),basicInfo);
					  
					  SmartRCPBuilder.window.getActivePage().showView(FlashViewPart.ID, appId + "",state);
				   }else{
					  if(isMultiple){
						  Integer num=multiplViewNum.get(viewId);
						  if(num==null){
							  num=0;
						  }
						  multiplViewNum.put(viewId, num+1);
						  SmartRCPBuilder.window.getActivePage().showView(viewId, num+"", state);
					  }else{
						  SmartRCPBuilder.window.getActivePage().showView(viewId);
					  }
				   }
				}
			}
			return appId;
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	public void showViewPart(String appId,int state){
		if(appId!=null){
		 FlashViewer flashViewer=this.findFlashViewer(appId);
		 if(flashViewer!=null){
		  IServerObject parent=flashViewer.getParent();
		  if(parent instanceof IViewPart){
			IWorkbenchPage page=SmartRCPBuilder.window.getActivePage();
			if(state==IWorkbenchPage.VIEW_ACTIVATE){
				page.activate((IViewPart)parent);
			}else{
				page.bringToTop((IViewPart)parent);
			}
		  }
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
	private void closeViewPart(IServerObject parent){
		if(parent!=null && parent instanceof FlashViewPart){
			FlashViewPart flashViewPart=(FlashViewPart)parent;
			
			SmartRCPBuilder.window.getActivePage().hideView(flashViewPart);
			flashViewPart.dispose();
		}
	}
	//--------------------FlashView�Ĺ�����
	/**
	 * ��ָ����viewpart���
	 */
	public void  setState(String viewPartId,int state){
		//FlashViewer ret=null;
		if(viewPartId!=null){
		  FlashViewer flashViewer=this.findFlashViewer(viewPartId);
		  if(flashViewer!=null){
			  IServerObject parent=flashViewer.getParent();
			  if(parent!=null && parent instanceof FlashViewPart){
					FlashViewPart flashViewPart=(FlashViewPart)parent;
					IWorkbenchPage page=SmartRCPBuilder.window.getActivePage();
				    
					//���Ȼ�ý���
					page.activate(flashViewPart);
					//���
					page.setPartState(page.getActivePartReference(),state);
			  }
		  }
		}
		//return ret;
	}
	public static void main(String[] args) {
		System.out.println(IWorkbenchPage.STATE_MAXIMIZED);
		System.out.println(IWorkbenchPage.STATE_MINIMIZED);
		System.out.println(IWorkbenchPage.STATE_RESTORED);
	}
	public List<String> findAppIds(String modulePath){
		List<String> ret=new LinkedList<String>();
		if(modulePath!=null){
			modulePath=CPerspective.getRuntimeSwfFolder() + "/"+modulePath;
			List<FlashViewer> curViews=FlashViewer.getViewers();//��ǰ������FlashViewer
			for(int n=0;n<curViews.size();n++){
				FlashViewer viewer=curViews.get(n);
				if(viewer.getSwfPath().equals(modulePath)){
					ret.add(viewer.getAppId());
				}
			}
		}
		return ret;
	}
	/**
	 * ��õ�ǰ��ý����viewPart
	 * @return
	 */
	public FlashViewer getActiveViewPart(){
		FlashViewer ret=null;
		IWorkbenchPart part=SmartRCPBuilder.window.getActivePage().getActivePart();
		
		if(part!=null && part instanceof FlashViewPart){
			FlashViewPart flashViewPart=(FlashViewPart)part;
			ret=flashViewPart.getFlashViewer();
		}
		return ret;
	}
	public List<FlashViewer> findFlashViewers(String modulePath){
		List<FlashViewer> ret=new LinkedList<FlashViewer>();
		if(modulePath!=null){
			modulePath=CPerspective.getRuntimeSwfFolder() + "/"+modulePath;
			List<FlashViewer> curViews=FlashViewer.getViewers();//��ǰ������FlashViewer
			for(int n=0;n<curViews.size();n++){
				FlashViewer viewer=curViews.get(n);
				if(viewer.getSwfPath().equals(modulePath)){
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
	
}
