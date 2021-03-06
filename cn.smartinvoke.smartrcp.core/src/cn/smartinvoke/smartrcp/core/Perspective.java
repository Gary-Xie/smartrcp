package cn.smartinvoke.smartrcp.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.CFolderLayout;
import cn.smartinvoke.rcp.CLayout;
import cn.smartinvoke.rcp.CLayoutBasicInfo;
import cn.smartinvoke.rcp.CPageLayout;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.rcp.CStandaloneLayout;
import cn.smartinvoke.rcp.ICFolderLayout;
import cn.smartinvoke.smartrcp.gui.FlashViewPart;
import cn.smartinvoke.smartrcp.gui.SplashWindow;
import cn.smartinvoke.util.Log;
public class Perspective implements IPerspectiveFactory {
	public static final String ID="cn.smartinvoke.smartrcp.core.Perspective";
	/*public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addView(FlashViewPart.ID+":1", IPageLayout.TOP, 0.5f, editorArea);
		layout.addView(FlashViewPart.ID+":2", IPageLayout.RIGHT, 0.5f, FlashViewPart.ID+":1");//"org.eclipse.ui.browser.view"
		//layout.addView("org.eclipse.ui.browser.view", IPageLayout.BOTTOM, 0.5f, FlashView.ID);
		//layout.addView(Test.ID, IPageLayout.BOTTOM, 0.5f, editorArea);
		
		
	}*/
	//private int viewNum=0;
    
	private String viewId=FlashViewPart.ID;
	private IPageLayout layout;
	//布局块表
	private HashMap<ICFolderLayout,String> layoutMap=new HashMap<ICFolderLayout, String>();
	private CPageLayout pageLayout;
	public void createInitialLayout(IPageLayout layout) {
		CPerspective perspective=SplashWindow.getPerspective();
		if(perspective==null){
			return;
		}
		//如果保存了工作台的状态就不执行下面的代码
		/*if(perspective.saveAndRestore){
			return;
		}*/
		
		this.layout=layout;
		
		pageLayout=perspective.page;
		if(pageLayout!=null){
			String editorArea = layout.getEditorArea();
			layout.setEditorAreaVisible(pageLayout.isEditorAreaVisible());
			layout.setFixed(pageLayout.fixed);
			layoutMap.put(pageLayout, editorArea);
			//
			List<CLayout> layouts=pageLayout.getLayouts();
			if(layouts!=null){
			  for(int i=0;i<layouts.size();i++){
				  CLayout cLayout=layouts.get(i);
				  if(cLayout instanceof CFolderLayout){
				   CFolderLayout folderLayout=(CFolderLayout)cLayout;
				   layoutMap.put(folderLayout, "folderLayout"+i);
				   folderLayout.setCreate(false);
				  }
			  }
			 //设置布局
			  for(int i=0;i<layouts.size();i++){
				  CLayout cLayout=layouts.get(i);
				if(cLayout!=null){
				  if(cLayout instanceof CFolderLayout){
					createFolder((CFolderLayout)cLayout);
				  }
				  if(cLayout instanceof CStandaloneLayout){
					createStandaloneFolder((CStandaloneLayout)cLayout);
				  }
				}
			  }
			}
			
			
		}
		
	}
	private void createFolder(CFolderLayout folderLayout){
		//CFolderLayout folderLayout=folderLayouts.get(i);
		  if(folderLayout==null || folderLayout.isCreate()){
			  return;
		  }
		  
		  String folderName=layoutMap.get(folderLayout);
		  ICFolderLayout refFolder=folderLayout.getRefLayout();
		  if(refFolder==null){
			  refFolder=pageLayout;
		  }else if(refFolder!=this.pageLayout){
			  if(refFolder instanceof CFolderLayout){
				  this.createFolder((CFolderLayout)refFolder);
			  }
		  }
		  
		  String relFolderName=layoutMap.get(refFolder);
		  if(folderName!=null && relFolderName!=null){
			 // System.out.println(">"+folderName+"*"+folderLayout.getRelationship()+"*"+folderLayout.getRatio());
		   IFolderLayout folder = layout.createFolder(folderName,folderLayout.getRelationship(),(float)folderLayout.getRatio(),
				   relFolderName);
		   if(folderLayout.placeholderViewId!=null){
			   folder.addPlaceholder(folderLayout.placeholderViewId+":*");
		   }
		   String viewIdSstr=this.getViewIdString(folderLayout);
		   if(viewIdSstr!=null){
		    folder.addView(viewIdSstr);
		   }
		   //TODO　ｎａｍｅ
		   IViewLayout viewPart=layout.getViewLayout(viewIdSstr);
		   viewPart.setCloseable(folderLayout.isCloseable());
		   viewPart.setMoveable(folderLayout.isMoveable());
		   
		   folderLayout.setCreate(true);
		  }
	}
	private void createStandaloneFolder(CStandaloneLayout standaloneLayout){
		//CStandaloneLayout standaloneLayout=standaloneLayouts.get(t);
		if(standaloneLayout==null){
			return;
		}
		//String folderName=layoutMap.get(standaloneLayout);
		ICFolderLayout refFolder=standaloneLayout.getRefLayout();
		if(refFolder==null){
			  refFolder=pageLayout;
		}else if(refFolder!=this.pageLayout){
			  if(refFolder instanceof CFolderLayout){
				  this.createFolder((CFolderLayout)refFolder);
			  }
		}
		String relFolderName=layoutMap.get(refFolder);
		if(relFolderName!=null){
			String viewIdSstr=this.getViewIdString(standaloneLayout);
			if(viewIdSstr!=null){
			 layout.addStandaloneView(viewIdSstr, standaloneLayout.isShowTitle(), standaloneLayout.getRelationship(),
					(float)standaloneLayout.getRatio(),relFolderName);
			
			 layout.getViewLayout(viewIdSstr).setCloseable(standaloneLayout.isCloseable());
			 layout.getViewLayout(viewIdSstr).setMoveable(standaloneLayout.isMoveable());
			}
		}
		
	}
	private String getViewIdString(CLayout layout){
		   String viewIdSstr=null;
		   String layoutViewStr=layout.getViewId();
		   if(layoutViewStr!=null && layoutViewStr.endsWith(".swf")){
			   int viewNum=FlashViewer.getViewNum();
			   viewIdSstr=viewId+":"+viewNum;
               swfLayoutMap.put(viewNum,layout);
		   }else{
			   viewIdSstr=layoutViewStr;
		   }
		return viewIdSstr;
	}
	public static Map<Integer,CLayoutBasicInfo> swfLayoutMap=new HashMap<Integer, CLayoutBasicInfo>();
	
	
}
