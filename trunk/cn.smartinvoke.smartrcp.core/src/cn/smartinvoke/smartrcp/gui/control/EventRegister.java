package cn.smartinvoke.smartrcp.gui.control;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.smartrcp.gui.module.CEventBean;
import cn.smartinvoke.util.Log;

/**
 * �¼�ע����
 * @author pengzhen
 * 
 */
public class EventRegister implements IServerObject{
	//�ؼ���
    private Map<Object,ListenerEntity> widgetmap=new HashMap<Object, ListenerEntity>();
    //displayȫ�ּ�����
    public final ListenerEntity globalListenerEntity=new ListenerEntity();
	public EventRegister() {
		
	}
	public ListenerEntity getListenerEntity(Object widget){
		if(widget==null){
			return null;
		}else{
		    return widgetmap.get(widget);
		}
	}
	/**
	 * ��ָ���ؼ����һָ�����͵ļ�����
	 * @param widget
	 * @param eventType
	 * @param listenerId
	 */
    public void addListener(Object widget,int eventType,CEventBean bean){
    	if(widget!=null){//��ȫ��Display�ļ���
    	   Display curDisplay=Display.getCurrent();
    	   if(curDisplay!=null){
    		   if((widget.equals(curDisplay)) && bean!=null && eventType!=0 ){
    			   List listenerList=globalListenerEntity.listeners[eventType];
    			   if(!listenerList.contains(bean)){//����ͬһ��������������
    	    			listenerList.add(bean);
    	    	   }
    			   return;
    		   }
    	   }
    	}
    	if(widget!=null && bean!=null && eventType!=0){
    		ListenerEntity entity=widgetmap.get(widget);
    		if(entity==null){
    			entity=new ListenerEntity();
    			widgetmap.put(widget, entity);
    		}
    		List listenerList=entity.listeners[eventType];
    		if(!listenerList.contains(bean)){//����ͬһ��������������
    			listenerList.add(bean);
    		}
    	}
    	Log.print("globalListenerEntity="+globalListenerEntity+"  ");
    	Log.print("widgetmap="+widgetmap+"  ");
    }
    /**
	 * ��ָ���ؼ���ɾ��һָ���ļ�����
	 * @param widget
	 * @param eventType
	 * @param listenerId
	 */
    public void removeListener(Object widget,int eventType,CEventBean bean){
    	
    	if(widget!=null && bean!=null && eventType!=0){
    		ListenerEntity entity=null;
    		Display curDisplay=Display.getCurrent();
     	    if(curDisplay!=null){
     		   if(widget.equals(curDisplay)){
     			 entity=globalListenerEntity;
     		   }
     	    }
     	    if(entity==null){
     	      entity=widgetmap.get(widget);
     	    }
    		if(entity!=null){
    			entity.listeners[eventType].remove(bean);
    		}
    	}
        
    	Log.print("globalListenerEntity="+globalListenerEntity+"  ");
    	Log.print("widgetmap="+widgetmap+"  ");
    }
    /**
     * ɾ��ָ��appId�µ����м���������
     * @param appId
     */
    public void removeListeners(String appId){
    	if(appId==null){
    		return;
    	}
    	this.removeFromListenerEntity(this.globalListenerEntity, appId);
    	Iterator<Object> keys=this.widgetmap.keySet().iterator();
    	while(keys.hasNext()){
    		Object key=keys.next();
    		this.removeFromListenerEntity(this.widgetmap.get(key), appId);
    	}
    }
    private void removeFromListenerEntity(ListenerEntity entity,String appId){
    	if(entity!=null){
    		
    		List[] evtLis=entity.listeners;
    		for(int i=0;i<evtLis.length;i++){
    			List listeners=evtLis[i];
    			if(listeners!=null){
    			   for(int l=0;l<listeners.size();l++){
    				   Object beanObj=listeners.get(l); 
    				   if(beanObj!=null && (beanObj instanceof CEventBean)){
    					   CEventBean bean=(CEventBean)beanObj;
    					   String id=bean.getAppId();
    					   if(id==null || id.equals(appId)){
    						   listeners.remove(l);
    						   l--;
    					   }
    				   }
    			   }
    			   //int size=listeners.size();
    			 //  Log.println("app "+appId+" evt size="+size+" type="+i);
    			}
    		}
    		
    	}
    }
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		JarFile jarFile=
			new JarFile("C:/Documents and Settings/pengzhen/����/����/������/SmartInvoke2009-12-2.0.jar");
		String cls="cn.smartinvoke.javaflex.gui.Msg";
		JarEntry entity=jarFile.getJarEntry("cn/smartinvoke/javaflex/gui/Msg.class");
		System.out.println(entity);
		InputStream in=jarFile.getInputStream(entity);
		byte[] bts=new byte[in.available()];
		in.read(bts, 0, bts.length);
		System.out.println(bts);
//		Enumeration<JarEntry> entities=jarFile.entries();
//		while(entities.hasMoreElements()){
//			 entity=entities.nextElement();
//			System.out.println(entity.getName());
//		}
	}
	public void dispose() {
		
	}

}
/**
 * ÿ���ؼ����¼���Ϣ�����ݽṹ����ʾÿ���ؼ������м�������Ϣ
 * @author pengzhen
 *
 */
class ListenerEntity{
	/**
	 * 41�����¼����͵ĸ�����һ��������һ���¼�������EventTypes����
	 */
	//TODO��Ϊ��Ч����map
	public List[] listeners=new List[42];//42��֤�����±겻Խ��
	public ListenerEntity(){
		for(int i=0;i<listeners.length;i++){
			//TODO Ĭ�ϳ�ʼ��10��
			listeners[i]=new LinkedList();
		}
	}
}