
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* �˴�������CodeTransform�����Զ�����
* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package cn.smartinvoke.rcp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
 public class CPageLayout  implements ICFolderLayout{
	/**
	 *����ͼģ��idΪkey��appId����Ϊֵ��map 
	 */
	private Map<String,List<String>> moduleIdAppIdMap=new HashMap<String, List<String>>();
	private boolean editorAreaVisible=true;
	public boolean fixed=false;
    private LinkedList<CLayout> layouts=null;
    
	public LinkedList<CLayout> getLayouts() {
		return layouts;
	}
	public void setLayouts(LinkedList<CLayout> layouts) {
		this.layouts = layouts;
	}
	public boolean isEditorAreaVisible() {
		return editorAreaVisible;
	}
	public void setEditorAreaVisible(boolean editorAreaVisible) {
		this.editorAreaVisible = editorAreaVisible;
	}
	public CPageLayout() {
		super();
	}
	
	public void addViewPartInfo(String moduleStr,String appId){
		if(moduleStr!=null && appId!=null){
		  List<String> appIds=this.moduleIdAppIdMap.get(moduleStr);
		  if(appIds==null){
			  appIds=new LinkedList<String>();
			  moduleIdAppIdMap.put(moduleStr, appIds);
		  }
		  //��֤Ψһ��
		  if(!appIds.contains(appId)){
		    appIds.add(appId);
		  }
		}
	}
	public void removeViewPartInfo(String moduleStr,String appId){
		if(moduleStr!=null && appId!=null){
		   List<String> appIds=this.moduleIdAppIdMap.get(moduleStr);
		   if(appIds!=null){
			  if(appIds.contains(appId)){
			    appIds.remove(appId);
			  }
		   }
		}
	}
	/**
	 * ���ظ�appId��Ӧ��ģ���ַ���
	 * @param appId
	 * @return
	 */
	public String getModuleStr(String appId){
		String ret=null;
		if(appId==null){
			return null;
		}
		Iterator<String> keys=this.moduleIdAppIdMap.keySet().iterator();
		while(keys.hasNext()){
			String key=keys.next();
			List<String> appIds=this.moduleIdAppIdMap.get(key);
			if(appIds!=null){
				if(appIds.contains(appId)){
					ret=key;
					break;
				}
			}
		}
		return ret;
	}
	public Map<String, List<String>> getModuleIdAppIdMap() {
		return moduleIdAppIdMap;
	}
 }
