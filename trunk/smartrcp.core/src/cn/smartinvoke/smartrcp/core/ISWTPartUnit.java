package cn.smartinvoke.smartrcp.core;

import org.eclipse.swt.widgets.Composite;

/**
 * ʵ�ִ˽ӿڵĶ��󣬿���ע�ᵽSmartRCPViewPartService
 * ��ʵ��swt������flex�ļ���
 * @author pengzhen
 */
public interface ISWTPartUnit {
	/**
	 *��ǰswt���浥Ԫ��ע�ᵽ SmartRCPViewPartService��
	 *ʱʹ�õ�id
	 * @return
	 */
   public String getId();
   /**
    * ����ʵ�ָ÷����Ѵ���swt����
    * @param parent
    */
   public void createPartControl(Composite parent);
   /**
    * �÷����ṩ��flex���ã��Ա�flex���Է���ĵ��õ���ǰSWT�ؼ���Ԫ�ϵķ���
    * @param methodName
    * @param params
    * @return
    */
   //public Object invoke(String methodName,Object[] params);
}
