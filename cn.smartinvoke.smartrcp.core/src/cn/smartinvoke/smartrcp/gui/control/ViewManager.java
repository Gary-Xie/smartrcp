package cn.smartinvoke.smartrcp.gui.control;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.IWorkbenchPage;

import cn.smartinvoke.gui.FlashViewer;
import cn.smartinvoke.rcp.CLayoutBasicInfo;
import cn.smartinvoke.smartrcp.core.FlashViewPart;
import cn.smartinvoke.smartrcp.core.Perspective;
import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;

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
	public int showViewPart(CLayoutBasicInfo basicInfo,boolean isMultiple, int state) {
		try {
			int appId = -1;
			if (basicInfo != null) {
				String viewId=basicInfo.getViewId();
				if(viewId!=null){
				   if(viewId.endsWith(".swf")){//�����swf
					  appId = FlashViewer.getViewNum();
					  Perspective.swfLayoutMap.put(Integer.valueOf(appId),basicInfo);
					  basicInfo.autoLoad=true;//����Ϊtrue���Ա�FlashViewPart�Զ�����swf
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
	
}
