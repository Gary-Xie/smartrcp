package cn.smartinvoke.smartrcp.util;

import org.eclipse.jface.resource.ImageDescriptor;

import cn.smartinvoke.gui.ObjectPool;
import cn.smartinvoke.rcp.CPerspective;
import cn.smartinvoke.smartrcp.gui.control.CAction;
import cn.smartinvoke.smartrcp.gui.control.CActionImpl;
import cn.smartinvoke.smartrcp.gui.control.CActionManager;
import cn.smartinvoke.smartrcp.gui.control.GlobalServiceId;

public class JFaceHelpMethod {

	private JFaceHelpMethod() {
      
	}
    /**
     * ��flex���ݹ�����CAction���飬��ʼ��Ϊ��Ӧ��jface Action���󣬲����ö���
     * �洢��CActionManager�У�keyΪaction��id��ֵΪ��action
     * @param actionArr
     */
	public static void initAction(Object[] actionArr) {
		if (actionArr == null) {
			return;
		}
		ObjectPool objectPool = ObjectPool.INSTANCE;
		// if(perspective!=null){
		CActionManager actionManager = null;
		Object obj = objectPool.getObject(GlobalServiceId.CAction_Manager);
		if (obj != null) {
			actionManager = (CActionManager) obj;
			int len=actionArr.length;
			for(int i=0;i<len;i++){
			   Object actionObj=actionArr[i];
			   if(actionObj instanceof CAction){
				   actionManager.addAction(new CActionImpl((CAction)actionObj));
			   }
			}
		}
		// }
	}

	public static ImageDescriptor getImageDescriptor(String imgUrl) {
		//TODO רҵ��һ�ļ�����ͼ��ע���
		return null;//Activator.getImageDescriptor(imgUrl);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
