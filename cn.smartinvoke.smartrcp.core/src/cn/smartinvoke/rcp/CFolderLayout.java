package cn.smartinvoke.rcp;

import java.util.List;

public class CFolderLayout implements ICFolderLayout{
	public int relationship;
	public double ratio;
	/**
	 * ����viewPart��Id�������Ե�������ʹ�����͵���ͼ��
	 * ��folder�д�
	 */
	public String placeholderViewPartId=null;
	/**
	 * ��folder�ڲ��ֵ�ʱ���յ�layout
	 */
	public ICFolderLayout refLayout = null;
	/**
	 * ��layout����ʾ��ģ�鼯��
	 */
	private List<CLayoutBasicInfo> modules=null;
    public CFolderLayout(){
      
    }
	public List<CLayoutBasicInfo> getModules() {
		return modules;
	}
	public void setModules(List<CLayoutBasicInfo> modules) {
		this.modules = modules;
	}
    
}
