
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* ����Ը��ƻ��޸Ĵ˴��룬���Ǳ�����Դ����Ͷ����ư�����������Ա�ʾ���û��޸���smartinvoke�Ĵ���
* ����ϧ���ߵ��Ͷ��ɹ�* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package cn.smartinvoke.exception;
/**
 * ��������쳣
 * @author pengzhen
 *
 */
public class InvokeException extends RuntimeException {
	/**
	 *  A�����ö��󲻴��ڼ����ö���Ϊ���쳣����ʱ�쳣����Ϊ InvokObjectNull
        B. ���ö����ϲ�������Ӧ�ķ�������ʱ���쳣����Ϊ InvokMethodNull 
        C. �ڵ��÷����Ͽ��ܻ��׳��쳣����ʱ���쳣����Ϊ InvokMethodException
	 */
	public static final int TYPE_INVOK_OBJ_CREATE_FAULT=-1;
    public static final int TYPE_INVOK_OBJECT_NULL=0;
    public static final int TYPE_INVOK_METHOD_NULL=1;
    public static final int TYPE_INVOK_METHOD_EXCEPTION=2;
    private int type=TYPE_INVOK_OBJECT_NULL;
    private String msg;
	public InvokeException(int type,String msg) {
		this(msg);
		this.type=type;
		
	}
	public InvokeException(String message) {
		super(message);	
	}
	public InvokeException() {
		//super(message);	
	   
	}
	public void setMessage(String msg){
		System.out.println(msg +" in invokeException");
		this.msg=msg;
	}
	public InvokeException(Throwable cause) {
		super(cause);
	}
	public InvokeException(String message, Throwable cause) {
		super(message, cause);
	}
    public int getType(){
    	return this.type;
    }
    public String toXml(){
        StringBuffer buf=new StringBuffer("<e t=\"InvokException\" type=\""+this.type+"\">");
        buf.append(this.getMessage());
        buf.append("</e>");
    	return buf.toString();
    }
}
