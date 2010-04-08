package cn.smartinvoke.smartrcp.core;

import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;

import cn.smartinvoke.rcp.CLayoutBasicInfo;
import cn.smartinvoke.util.ImageManager;

/**
 * ����SWT�ؼ���Ԫ����������
 * @author pengzhen
 */
public class SWTUnitViewPart extends SmartRCPViewPart {
    
	public SWTUnitViewPart() {
		
	}
	private ISWTPartUnit partUnit;
	public void createPartControl(Composite parent){
		super.createPartControl(parent);
		
		String secondId=this.getViewSite().getSecondaryId();
	    if(secondId!=null){
	    	Map<Integer, CLayoutBasicInfo> layoutMap = Perspective.swfLayoutMap;
	    	 CLayoutBasicInfo layoutInfo =layoutMap.get(Integer.valueOf(secondId));
	    	 if(layoutInfo!=null){
	    		String unitId=layoutInfo.modulePath;//swt�ؼ���Ԫ��id
	    		//���ñ����ͼƬ
	    		this.setPartName(layoutInfo.title);
	    		if (layoutInfo.image != null) {
					ImageDescriptor imageDescriptor = ImageManager
							.getImageDescriptor(layoutInfo.image);
					if (imageDescriptor != null){
						this.setTitleImage(imageDescriptor.createImage());
					}
				}
	    		
	    		partUnit=
	    			SmartRCPViewPartService.Instance.getSWTPartUnits().get(unitId);
	    		if(partUnit!=null){//����swt�ؼ���Ԫ
	    		   partUnit.createPartControl(parent);
	    		}
	    	 } 
	    }
	}
	
	public ISWTPartUnit getPartUnit(){
		return this.partUnit;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
