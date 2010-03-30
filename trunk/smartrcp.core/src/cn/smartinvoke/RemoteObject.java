
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* ����Ը��ƻ��޸Ĵ˴��룬���Ǳ�����Դ����Ͷ����ư�����������Ա�ʾ���û��޸���smartinvoke�Ĵ���
* ����ϧ���ߵ��Ͷ��ɹ�* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package cn.smartinvoke;

import java.lang.reflect.Method;

import cn.smartinvoke.exception.InvokeException;
import cn.smartinvoke.exception.Messages;
import cn.smartinvoke.gui.FlashContainer;
public class RemoteObject {

	private FlashContainer flashContainer;
	private static String FLASH_CREATE_BEG = "<invoke name=\"create\" returntype=\"xml\"><arguments>";
	private static String FLASH_CALL_BEG = "<invoke name=\"call\" returntype=\"xml\"><arguments>";
	private static String FLASH_DISPOSE_BEG = "<invoke name=\"dispose\" returntype=\"xml\"><arguments>";
	private static String FLASH_SET_PROP_BEG = "<invoke name=\"setProperty\" returntype=\"xml\"><arguments>";
	private static String FLASH_GET_PROP_BEG = "<invoke name=\"getProperty\" returntype=\"xml\"><arguments>";
	private static String FLASH_CALL_END = "</arguments></invoke>";

	private String remoteId = null;
	//�Ƿ����Զ��ڴ����
    private boolean isAutoDispose=true;
	public RemoteObject(FlashContainer flashContainer) {
		this.flashContainer = flashContainer;
	}
	public RemoteObject(FlashContainer flashContainer,boolean isAutoDispose) {
		this(flashContainer);
		this.isAutoDispose=isAutoDispose;
	}
    /**
     * ���ݸ�����objId����RemoteObject���Ͷ���
     * @param container
     * @param objId
     * @return
     
	public static RemoteObject createInstance(FlashContainer container,
			String objId) {
		RemoteObject ret = null;
		Object creatObj = createRemoteObject(container, RemoteObject.class
				.getName(), objId);
		if (creatObj != null) {
			ret = (RemoteObject) creatObj;
		}
		return ret;
	}*/

	/**
	 * �����������ľ�̬�������÷������ڷ���˷����Ѿ������ķ�������id�����
	 * 
	 * @param objId
	 * @return
	 */
	public static Object createRemoteObject(FlashContainer container,
			String clsName, String objId) {
		Object ret = null;
		try {
			Class createCls = Class.forName(clsName);
			Object object = createCls.getConstructor(
					new Class[] { container.getClass() }).newInstance(
					new Object[] { container });
			ret = object;
			// ����remoteId����
			Method method = createCls.getMethod("setRemoteId",
					new Class[] { String.class });
			method.invoke(object, new Object[] { objId });
		} catch (Exception e) {// ���󴴽��쳣
			throw new InvokeException(
					InvokeException.TYPE_INVOK_OBJ_CREATE_FAULT,
					Messages.INSTANCE.getMsg(
							Messages.Ivk_INVOK_OBJ_CREATE_FAULT,
							new String[] { clsName }));
		}
		return ret;
	}

	/**
	 * ��������������ڷ���˶�����е�id
	 * 
	 * @return
	 */
	public String getRemoteId() {
		return this.remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	/**
	 * ��pars���л����м������ַ������ݸ�flashContainer��flashContainer���ݵ�ǰ��������ͣ��ڶ�����й����
	 * ���󣬲����ض��������id remoteId ���������ṩ�������ַ��������캯���Ĳ���ֵ <create cls="org.Test"> <l
	 * n="0">123</l> <str n="1">adsfasdf</str> </create>
	 * 
	 * @param pars
	 
	public void createRemoteObject(Object[] pars) {
		this.nativeCreateObject(this.getClass().getName(), pars, false);
	}
	
	public void asynCreateRemoteObject(Object[] pars) {
		this.nativeCreateObject(this.getClass().getName(), pars, true);
	}
	public void createRemoteObject(String clsName,Object[] pars) {
		this.nativeCreateObject(clsName, pars, false);
	}
	public void asynCreateRemoteObject(String clsName,Object[] pars) {
		this.nativeCreateObject(clsName, pars, true);
	}*/
	public void createRemoteObject() {
		this.nativeCreateObject(this.getClass().getName(),null);
	}
    protected void nativeCreateObject(String clsName,Object[] pars){
    	
    	StringBuilder sb = new StringBuilder();
		sb.append(FLASH_CREATE_BEG).append("<string>");
		     sb.append(clsName);
		sb.append("</string>");
		sb.append(FLASH_CALL_END);
		
		String retPack = null;
		retPack=this.flashContainer.asyncCallFunction(sb.toString());
		
		Object ret=ProtocolBuilder.getObject(retPack,this.flashContainer.getAppId());
		if(ret!=null){
		 String objId=(String)ret;
		 this.setRemoteId(objId);
		}
		
    }
    
	/**
	 * ���������������ƶ��������������ƶ�����
	 * 
	 * @param methodName
	 * @param pars
	 * @return ���õķ���
	 * 
	 * <call objId="123" method="getName"> <l n="0">123</l> <str n="1">adsfasdf</str>
	 * </call>
	 */
	public Object call(String methodName, Object[] pars) {
		return this.nativeCall(methodName, pars, true); 
	}

	public Object asyncCall(String methodName, Object[] pars) {
       return this.nativeCall(methodName, pars, false); 
	}
	/**
     * ��������
     * @param name
     * @param value
     */
    protected void setProperty(String name,Object value){
    	StringBuilder pack = new StringBuilder();
		pack.append(FLASH_SET_PROP_BEG).append("<string>");
		
		Object[] params=new Object[]{this.getRemoteId(),name,value};
		pack.append(ProtocolBuilder.getProtocolStr(params,this.flashContainer.getAppId()));
		pack.append("</string>");
		pack.append(FLASH_CALL_END);
		String retPack=null;
		
		retPack=this.flashContainer.asyncCallFunction(pack.toString());
		//����Ƿ����쳣
		ProtocolBuilder.getObject(retPack,this.flashContainer.getAppId());
    }
    /**
     * ��ȡָ������ֵ���������void��Ϊ��
     * @param name ��������
     * @return
     */
    protected Object getProperty(String name){
		StringBuilder pack = new StringBuilder();
		pack.append(FLASH_GET_PROP_BEG);
		
		pack.append("<string>");
		Object[] params=new Object[]{this.getRemoteId(),name};
		pack.append(ProtocolBuilder.getProtocolStr(params,this.flashContainer.getAppId()));
		
		pack.append("</string>");
		
		pack.append(FLASH_CALL_END);
		String retPack=this.flashContainer.asyncCallFunction(pack.toString());
		Object retObj=ProtocolBuilder.getObject(retPack,this.flashContainer.getAppId());
		return retObj;
    }
    protected Object nativeCall(String methodName,Object[] pars,boolean isAsync){
		StringBuilder pack = new StringBuilder();
		pack.append(FLASH_CALL_BEG).append("<string>");
		Object[] params=new Object[]{this.getRemoteId(),methodName,pars,isAsync};
		pack.append(ProtocolBuilder.getProtocolStr(params,this.flashContainer.getAppId()));
		pack.append("</string>");
		
		pack.append(FLASH_CALL_END);
		String retPack=null;
		
		retPack=this.flashContainer.asyncCallFunction(pack.toString());
		return ProtocolBuilder.getObject(retPack,this.flashContainer.getAppId());
    }
	/**
	 * ��������ʱ���õķ�������������ж���ʧȥ����ʱ���˷����ᱻ�����Ի����ڴ�
	 */
	protected void finalize() {
	  if(this.isAutoDispose){
		this.dispose();
	  }
	}
	public void dispose(){
		StringBuilder pack = new StringBuilder(FLASH_DISPOSE_BEG);
		pack.append("<string>");
		pack.append(this.getRemoteId());
		System.out.println("RemoteObject disposeId="+this.getRemoteId());
		pack.append("</string>");
		pack.append(FLASH_CALL_END);
		this.flashContainer.asyncCallFunction(pack.toString());
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer("ab");
		sb.insert(0, "111");
		System.out.println(sb);
		String str1 = "ab";
		String str2 = "ab";
		System.out.println(str1 == str2);
	}

}
