package cn.smartinvoke;

import java.lang.reflect.Method;

import cn.smartinvoke.lllIIlllllllIIlI;
import cn.smartinvoke.exception.InvokeException;
import cn.smartinvoke.exception.Messages;
import cn.smartinvoke.util.HelpMethods;

/**
 * ִ����
 * 
 * @author pengzhen
 * 
 */
 class Executor {
    private ObjectPool pool=ObjectPool.INSTANCE;
    private String appId;
	public Executor(String appId){
        this.appId=appId;
	}
    /**
     * <invoke name="hello" returntype="xml">
     * <arguments><string>&lt;hello&gt;str&lt;/hello&gt;</string></arguments>
     * </invoke>
     * @param command
     * @return
     */
	public String execute(String command){
		String ret = null;
	 try{
		if (command != null) {
			// disposed�����򵥴���
			boolean isDisposeAction = false;
			String reqPack = this.getRequestPack(command);// ȥ������Ľڵ�
			if (reqPack == null) {//���Ĵ���
				RuntimeException e=new RuntimeException(Messages.ERROR_PACKAGE);
				ret=lllIIlllllllIIlI.getProtocolStr(e,this.appId);
			}
			if (isDisposeAction) {//despose����void
				this.dispose(reqPack);
			} else {
				//StringBuilder reqBuf = new StringBuilder(reqPack);
				try {
					if (command.indexOf("name=\"create\"") != -1) {
						ret = this.create(reqPack);
					} else if (command.indexOf("name=\"call\"") != -1) {
						ret = this.call(reqPack);
					}
				} catch (Throwable e) {//e.printStackTrace();
					  
					ret=lllIIlllllllIIlI.getProtocolStr(new InvokeException(HelpMethods.getThrowableInfo(e)),this.appId);
					
				}
			}
		}else{//���Ĵ���
			//RuntimeException e=new RuntimeException(Messages.ERROR_PACKAGE);
			ret=lllIIlllllllIIlI.getProtocolStr(new InvokeException(Messages.ERROR_PACKAGE),this.appId);
		}
	 }catch(Throwable e){
		 ret=lllIIlllllllIIlI.getProtocolStr(new InvokeException(HelpMethods.getThrowableInfo(e)),this.appId);
	 }
		return ret;
	}
    /**
     * ��������һ���飬Ԫ��һ��ʾ������������ƣ��ڶ���Ԫ���ǣ����ݸ����캯���Ĳ���
     * @param pack
     * @return
     * @throws Exception
     */
	public String create(String pack)throws Exception {
		String ret =null;
		Object[] ivkInfo=(Object[])lllIIlllllllIIlI.getObject(pack,this.appId);
		String clsName=(String)ivkInfo[0];
		//clsName=TypeFactory.getInstance().typeMapper.getNativeClsName(clsName);
		Object[] pars=null;
		if(ivkInfo[1]!=null){
			pars=(Object[])ivkInfo[1];
		}else{
			pars=new Object[0];
		}
		String objId=this.pool.createObject(this.appId,clsName, pars);
		ret=lllIIlllllllIIlI.getProtocolStr(objId,this.appId);
		return ret;
	}
    /**
     * ��������һ����
     *  Ԫ��һ��objId��
     *  Ԫ�ض����������ƣ�
     *  Ԫ����������������
     * @param pack
     * @return
     * @throws Exception
     */
	public String call(String pack)throws Exception {
		StringBuilder ret = new StringBuilder();
		Object[] ivkInfo=(Object[])lllIIlllllllIIlI.getObject(pack,this.appId);
		String objId=ivkInfo[0].toString();
		String methodName=ivkInfo[1].toString();
        Object callObj=this.pool.getObject(this.appId,objId);
        
        Object retObj=null;
        
        if(callObj!=null){
           Class cls=callObj.getClass();
           
   		   Object[] pars=null;
   		   if(ivkInfo[2]!=null){
   			   pars=(Object[])ivkInfo[2];
   		   }else{
   			   pars=new Object[0];
   		   }
   		   Method ivkMethod=HelpMethods.getMethod(cls, methodName, pars);
		   if(ivkMethod==null){//û�ж�Ӧ�ķ����򱨸��쳣
				InvokeException invokException=
					new InvokeException(InvokeException.TYPE_INVOK_METHOD_NULL,
							Messages.INSTANCE.getMsg(Messages.IVK_METHOD_NOT_FOUND,new String[]{cls.getName(),methodName}));
				
				throw invokException;
		   }else{
			  // try{
                  retObj=ivkMethod.invoke(callObj, pars);
			   /*}catch(Exception e){
				 InvokeException invokException=
					new InvokeException(InvokeException.TYPE_INVOK_METHOD_EXCEPTION,
							Messages.INSTANCE.getMsg(Messages.IVK_METHOD_EXCEPTION,
							new String[]{cls.getName(),methodName,e.getMessage()}));
				
				 throw invokException;
			   }*/
		   }
        }else{//�����ö���Ϊ�գ��׳��쳣
        	InvokeException invokException=
				new InvokeException(InvokeException.TYPE_INVOK_OBJECT_NULL,
						Messages.INSTANCE.getMsg(Messages.IVK_OBJECT_NOT_FOUND,new String[]{objId}));
			
			throw invokException;
        }
		return lllIIlllllllIIlI.getProtocolStr(retObj,this.appId);
	}
	public void dispose(String objId) {
		
		pool.removeObject(this.appId,objId);
	}

	private String getRequestPack(String command) {
		String ret = null;
		String startTag = "<string>";
		String endTag = "</string>";
		if (command != null) {
			int start = command.indexOf(startTag);
			int end = command.lastIndexOf(endTag);
			if (start != -1 && end != -1) {
				ret = command.substring(start + startTag.length(), end);
			}
		}
		return ret;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
