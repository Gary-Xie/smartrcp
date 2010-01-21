package cn.smartinvoke.smartrcp.core;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.CLayout;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.util.ImageManager;
public class FlashViewPart extends ViewPart{
	public static final String ID = "cn.smartinvoke.smartrcp.core.FlashViewPart"; //$NON-NLS-1$
	private CLayout clayout;
	
	private FlashViewer flashViewer;
	@Override
	public void createPartControl(Composite parent) {
		try{
		String secondId=this.getViewSite().getSecondaryId();
		Map<Integer,CLayout> layoutMap=Perspective.swfLayoutMap;
		clayout=layoutMap.get(Integer.valueOf(secondId));
		String viewId=clayout.getViewId();
		if(viewId!=null){
			//if(viewId.indexOf('/')!=-1){//��flexģ��·��
		//TODO Ĭ��Ϊģ�����ģʽ
		String[] paths=new String[]{CPerspective.getRuntimeSWFPath(),CPerspective.getRuntimeSwfFolder()+"/"+viewId};
		flashViewer=new FlashViewer(parent,paths);
		//���ò�����Ϣ
		this.setPartName(clayout.getTitle());
		//����ͼ��
		if(clayout.image!=null){
			ImageDescriptor imageDescriptor=ImageManager.getImageDescriptor(clayout.image);
			if(imageDescriptor!=null){
			 this.setTitleImage(imageDescriptor.createImage());
			}
		}
//			}else{//java������
//				Class viewerCls=Class.forName(viewId);
//				Object viewerObj=viewerCls.newInstance();
//				if(viewerObj instanceof AbstrNativeViewer){
//					AbstrNativeViewer viewer=(AbstrNativeViewer)viewerObj;
//					Composite top=new Composite(parent,SWT.NONE);
//					//top.setLayout(new FillLayout());
//					viewer.creatContent(top);
//				}
//			}
		}
		//this.setTitleImage(Activator.getImageDescriptor("/icons/sample.gif").createImage());
		//
		//createActions();
		//this.getViewSite().getActionBars().updateActionBars();
		
		
		//fileMenu.add();
		}catch(Exception e){e.printStackTrace();};
	}
	
	public FlashViewer getFlashViewer() {
		return flashViewer;
	}
	//public static String Cur_App_Id=null;//��ǰ��ý����flash
	@Override
	public void setFocus() {
	   
	}
	public void dispose() {
		if(this.flashViewer!=null){
			//FlashViewer.remove_Viewer(this.flashViewer);
			this.flashViewer.dispose();
		}
	}	
}