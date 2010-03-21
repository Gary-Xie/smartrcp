
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* ����Ը��ƻ��޸Ĵ˴��룬���Ǳ�����Դ����Ͷ����ư�����������Ա�ʾ���û��޸���smartinvoke�Ĵ���
* ����ϧ���ߵ��Ͷ��ɹ�* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package cn.smartinvoke.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class HelpMethods {
	private static String installFolder=null;
	public static String getPluginFolder(){
	   if(installFolder==null){
		String ret=null;
		try {
			ret = Path.getPathFromClass(ConfigerLoader.class);
			int sp=ret.lastIndexOf(File.separatorChar);
			if(sp!=-1){
			 ret=ret.substring(0,sp);
			 ret=ret.replace(File.separatorChar, '/');
			}
			installFolder=ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
	   }
		return installFolder;
	}
	public static String getStringFromFile(String filePath) {
		StringBuffer ret = new StringBuffer();
		File file = new File(filePath);
		FileReader fr = null;
		try {
			fr = new FileReader(filePath);
			BufferedReader br=new BufferedReader(fr);
			char[] bs=new char[(int)file.length()];
			int redCount=br.read(bs);
			ret.append(bs, 0, redCount);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fr!=null){
				try{fr.close();}catch(Exception e ){};
			}
		}
		return ret.toString();
	}
	public static void showError(Shell shell,String info){
		   if(shell!=null){
			   MessageBox message = new MessageBox(shell);
				message.setText("��Ϣ");
				message.setMessage(info);
				message.open();
		   }
	}
	/**
	 * ���ַ�������Ϊlen���ȣ���������ÿհײ��㣬������ɾ��
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String addRightSpace(String str, int len) {
		int orgLen = str.length();
		int spaceCount = len - orgLen;
		if (spaceCount > 0) {
			for (int i = 0; i < spaceCount; i++) {
              str+=" ";
			}
		} else {
			 str = str.substring(0, len);
		}
		return str;
	}
	public static Constructor getConstructor(Class cls, Object[] params) {
		Constructor ret = null;
		if (params == null) {
			params = new Object[0];
		}
		List<Constructor> res = new LinkedList<Constructor>();
		Constructor[] cons = cls.getConstructors();
		for (Constructor con : cons) {
			Class[] paramTypes = con.getParameterTypes();
			if (params.length == paramTypes.length) {// ����������ͬ
				StringBuilder inputParamTypeBuf = new StringBuilder(), methodParamTypeBuf = new StringBuilder();
				// ���첢�Ƚϲ������ͷ����Ƿ���ͬ
				int len = params.length;
				for (int i = 0; i < len; i++) {
					Object inputParamObj = params[i];
					if (inputParamObj != null) {
						  Class inputParamCls=inputParamObj.getClass();
						  boolean inputPrim=false;
						  if(inputParamObj instanceof Number){
							  inputPrim=true;
						  }else if(inputParamObj instanceof Boolean){
							  inputPrim=true;
						  }
						  boolean paramPrim=false;
						  if(isSubClass(paramTypes[i],Number.class)){
							  paramPrim=true;
						  }
						  if(isSubClass(paramTypes[i],Boolean.class)){
							  paramPrim=true;
						  }
						  boolean isPrimitiveType=inputPrim&&(paramPrim ||paramTypes[i].isPrimitive());
						  if(!isPrimitiveType){//���ǻ�������
							 char chi='*',chm='&';
							 if(isSubClass(inputParamCls,paramTypes[i])){//���������Ƿ������͵�������ƥ��
								 chm=chi;
							 }
							 inputParamTypeBuf.append(chi).append(' ');
							 methodParamTypeBuf.append(chm).append(' ');
						  }else{//�Ƚϻ�������
							String typeStr = getPrimitiveTypeStr(inputParamCls);
							inputParamTypeBuf.append(typeStr).append(' ');
							typeStr =getPrimitiveTypeStr(paramTypes[i]);
							methodParamTypeBuf.append(typeStr).append(' ');
						  }
					}
				}
				if (inputParamTypeBuf.toString().equals(
						methodParamTypeBuf.toString())) {
					res.add(con);
				}
			}
		}
		if (res.size() == 0) {
			int len = params.length;
			StringBuilder inputParamTypeBuf = new StringBuilder();
			for (int i = 0; i < len; i++) {
				Object inputParamObj = params[i];
				if (inputParamObj != null) {
					inputParamTypeBuf.append(inputParamObj.getClass().getName()).append(
							' ');
				} else {
					inputParamTypeBuf.append("null ");
				}
			}
			throw new RuntimeException("����" + cls.getName() + " ���Ҳ������캯��(" + inputParamTypeBuf + ")");
		} else if (res.size() > 1) {
			throw new RuntimeException("����" + cls.getName() + " ���Ҳ������캯���������壨��ֹ��һ�����캯�����ϵ���ǩ����");
		} else {
			ret = res.get(0);
		}
		//System.out.println(ret.toGenericString());
		return ret;
	}

	/**
	 * 
	 * @param cls
	 *            Ѱ�ҷ������ڵ����Ͷ���
	 * @param name
	 *            ����������
	 * @param params
	 *            �����ĵ��ò��������Ϊ�վ͵���û�����ķ���
	 * @return
	 */
	public static Method getMethod(Class cls, String name, Object[] params) {
		Method ret = null;
		if (params == null) {
			params = new Object[0];
		}
		Method[] methods = cls.getMethods();
		List<Method> res = new LinkedList<Method>();
		for (Method method : methods) {
			if (method.getName().equals(name)) {// ����������ͬ
				Class[] paramTypes = method.getParameterTypes();
				if (params.length == paramTypes.length) {// ����������ͬ
					StringBuilder inputParamTypeBuf = new StringBuilder(), methodParamTypeBuf = new StringBuilder();
					// ���첢�Ƚϲ������ͷ����Ƿ���ͬ
					int len = params.length;
					for (int i = 0; i < len; i++) {
						Object inputParamObj = params[i];
						if (inputParamObj != null) {
						  Class inputParamCls=inputParamObj.getClass();
						  boolean inputPrim=false;
						  if(inputParamObj instanceof Number){
							  inputPrim=true;
						  }else if(inputParamObj instanceof Boolean){
							  inputPrim=true;
						  }
						  boolean paramPrim=false;
						  if(isSubClass(paramTypes[i],Number.class)){
							  paramPrim=true;
						  }
						  if(isSubClass(paramTypes[i],Boolean.class)){
							  paramPrim=true;
						  }
						  boolean isPrimitiveType=inputPrim&&(paramPrim ||paramTypes[i].isPrimitive());
						  if(!isPrimitiveType){//���ǻ�������
							 char chi='*',chm='&';
							 if(isSubClass(inputParamCls,paramTypes[i])){
								 chm=chi;
							 }
							 inputParamTypeBuf.append(chi).append(' ');
							 methodParamTypeBuf.append(chm).append(' ');
						  }else{//�Ƚϻ�������
							String typeStr =getPrimitiveTypeStr(inputParamCls);
							inputParamTypeBuf.append(typeStr).append(' ');
							typeStr =getPrimitiveTypeStr(paramTypes[i]);
							methodParamTypeBuf.append(typeStr).append(' ');
						  }
						}
					}
					if (inputParamTypeBuf.toString().equals(
							methodParamTypeBuf.toString())) {
						res.add(method);
					}
				}
			}
		}
		if (res.size() == 0) {
			int len = params.length;
			StringBuilder inputParamTypeBuf = new StringBuilder();
			for (int i = 0; i < len; i++) {
				Object inputParamObj = params[i];
				if (inputParamObj != null) {
					inputParamTypeBuf.append(inputParamObj.getClass().getName()).append(
							' ');
				} else {
					inputParamTypeBuf.append("null ");
				}
			}
			throw new RuntimeException("����" + cls.getName() + " �Ҳ������� " + name
					+ "(" + inputParamTypeBuf + ")");
		} else if (res.size() > 1) {
			throw new RuntimeException("����" + cls.getName() + " �ϵ�����Ϊ" + name
					+ "�ķ������ô������壨��ֹ��һ���������ϵ���ǩ����");
		} else {
			ret = res.get(0);
		}
		//System.out.println(ret.toGenericString());
		return ret;
	}
	public static boolean isSubClass(Class subClass,Class supClass){
		boolean ret=false;
		if(subClass!=null && supClass!=null){
		   String supClsName=supClass.getName();
		   String objClsName="java.lang.Object";
		   if(subClass.getName().equals(supClsName)){//��������һ�µ�
			   return true;
		   }
		   if(supClsName.equals(objClsName)){//���������Object�϶�����
			   return true;
		   }
		   Class tempSup=subClass;
		   while(true){
			   tempSup=tempSup.getSuperclass();
			   if(tempSup==null){
				   break;
			   }
			   String tempName=tempSup.getName();
			   if(tempName.equals(supClsName)){//ƥ��
					  return true;
			   }else if(tempName.equals(objClsName)){//�ҵ����
				   break;
			   }
		   }
		   //���ӿ�
		   List<Class> supInterfList=new LinkedList<Class>();
		   addInterfClsToList(subClass,supInterfList);
		   for(Class supInterf:supInterfList){
			   if(supInterf.getName().equals(supClsName)){
				   return true;
			   }
		   }
		}
		return ret;
	}
	/**
	 * ����ǰ��̳е����нӿ���ӵ�List
	 * @param curCls
	 * @param list
	 */
	private static void addInterfClsToList(Class curCls, List<Class> list){
		Class[] interfs=curCls.getInterfaces();
		if(interfs==null){
			return;
		}
		for(Class interf:interfs){
		 list.add(interf);
		 addInterfClsToList(interf,list);//��ӽӿڵĸ��ӿ�
		}
	}
	  public static String getPrimitiveTypeStr(Class cls){
			 String val=cls.getName();
			 if(cls.isPrimitive()){
				 String name=cls.getName();
				 if(name.equals("boolean")){
					 val="java.lang.Boolean";
				 }else if(name.equals("byte")){
					 val="java.lang.Byte";
				 }if(name.equals("short")){
					 val="java.lang.Short";
				 }if(name.equals("int")){
					 val="java.lang.Integer";
				 }if(name.equals("long")){
					 val="java.lang.Long";
				 }if(name.equals("float")){
					 val="java.lang.Float";
				 }if(name.equals("double")){
					 val="java.lang.Double";
				 }
			 }
			 return val;
		  }
	public static void main(String[] args) {
		
	}
}
