
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* ����Ը��ƻ��޸Ĵ˴��룬���Ǳ�����Դ����Ͷ����ư�����������Ա�ʾ���û��޸���smartinvoke�Ĵ���
* ����ϧ���ߵ��Ͷ��ɹ�* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package cn.smartinvoke.exception;

public class Messages {
	public static final Messages INSTANCE=new Messages();
	
    public static final String Ivk_INVOK_OBJ_CREATE_FAULT="����{0}����ʧ�ܣ������Ƕ�Ӧ���Ͳ����ڻ�Ĭ�Ϲ��캯�����ɼ���δ����";
    public static final String IVK_OBJECT_NOT_FOUND="���Ϊ{0}�Ķ���Ϊ��";
    public static final String IVK_METHOD_NOT_FOUND="������{0}�е�{1}�������ɼ���û�ж����������Ͳ�ƥ��";
    public static final String IVK_METHOD_EXCEPTION="��������{0}�е�{1}����ʱ�׳��쳣��{2}";
    public static final String ERROR_PACKAGE="���Ĵ���";
	private Messages() {
	 	
	}
	public String getMsg(String key,String[] param){
		return getRealInfo(key, param);
	}
	/**
     * ����������{0}������{1}Ӵ������{2} �������ַ�������values�������Ӧ��ֵȡ����Ӧ��ռλ������ϳ���
     * ���ַ�������
     * @param msg
     * @param values
     * @return
     */
    private static String getRealInfo(String msg,String[] values){
    	StringBuffer ret=new StringBuffer(msg);
    	String arrayIndex="";
    	int msgIndex=0;
    	char startChar='{',endChar='}';
    	while(ret.length()>msgIndex){
    		int replStart=0,replEnd=0;
    		int index=0;
    		char ch=ret.charAt(msgIndex++);
    		if(ch==startChar){
    		   replStart=msgIndex-1;
    		   while((ch=ret.charAt(msgIndex++))!=endChar){
    			arrayIndex+=ch;
    		   }
    		   replEnd=msgIndex;
    		   try{
    			  index=Integer.valueOf(arrayIndex);
    			  if(index<values.length){
    			   ret.replace(replStart, replEnd, values[index]);
    			   arrayIndex="";
    			  }
    			  msgIndex+=values[index].length()-(replEnd-replStart);
    		   }catch(Throwable e){
    			   e.printStackTrace();
    		   }
    		}
    		
    	}
    	return ret.toString();
    }
}
