
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* �˴�������CodeTransform�����Զ�����
* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package cn.smartinvoke.rcp;

import java.util.LinkedList;
 public class CPageLayout  implements ICFolderLayout{
	 
	private boolean editorAreaVisible=true;
    private LinkedList<CFolderLayout> folderLayouts=null;
    
    private LinkedList<CStandaloneLayout> standaloneLayouts=null;

	public LinkedList<CFolderLayout> getFolderLayouts() {
		return folderLayouts;
	}
	public void setFolderLayouts(LinkedList<CFolderLayout> folderLayouts) {
		this.folderLayouts = folderLayouts;
	}

	public LinkedList<CStandaloneLayout> getStandaloneLayouts() {
		return standaloneLayouts;
	}

	public void setStandaloneLayouts(LinkedList<CStandaloneLayout> standaloneLayouts) {
		this.standaloneLayouts = standaloneLayouts;
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
	
	
 }
