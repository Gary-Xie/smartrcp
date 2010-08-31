
package cn.smartinvoke;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.ole.win32.OleEvent;
import org.eclipse.swt.ole.win32.OleListener;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.RemoteObject;
public class FlashContainer extends Flash implements IServerObject{
	// flashCall�¼�
	final static int EVENT_FLASH_CALL = 0x000000c5;
	final static int EVENT_FSCOMMAND = 0x00000096;

	final static String CMD_FINISH = "cmd_finish";
	final static String CMD_ASYNC_CALL = "cmd_async_call";
	final static String CMD_DISPOSE = "cmd_dispose", CMD_BOUNDS = "3";
	
	private boolean isFlashLoaded = false;
	private Executor executor = null;
	public List<ILoadCompleteListener> listeners = new LinkedList<ILoadCompleteListener>();
	static{
		TypeFactory.Object_Pool=ObjectPool.INSTANCE;
		TypeFactory.objectCreator=ObjectPool.INSTANCE.objectCreator;
 		TypeFactory.Type_Mapper=TypeMapper.Instance;
	}
	public FlashContainer(Composite parent,String appId) {
		super(parent, SWT.NO_BACKGROUND, null);
		this.setAppId(appId);
		//��ӵ�ȫ��flash��������
		//FlashContainer.add_Container(this);
		//����û��������Ƿ�װflash player
		
		this.executor = new Executor(this.getAppId());
		//
		oleControlSite.addEventListener(EVENT_FSCOMMAND, new OleListener() {
			public void handleEvent(OleEvent event) {
				Variant command = event.arguments[0];
				Variant args = event.arguments[1];
				String val = args.getString();
				if (command != null && val != null) {
					String cmd = command.getString();
					if (cmd != null) {
						if (cmd.equals(CMD_FINISH)) {
							if (val.equals("1")) {
								isFlashLoaded = true;
								//����Ӧ�ó���ID���ݸ�flash
								String pack="<invoke name=\"setId\" returntype=\"xml\"><arguments><string>" +
								        FlashContainer.this.getAppId()+
										"</string></arguments></invoke>";
								FlashContainer.this.asyncCallFunction(pack);
								
								for(int i=0;i<FlashContainer.this.listeners.size();i++){
									ILoadCompleteListener listener=FlashContainer.this.listeners.get(i);
									listener.run();
								}
								//��ռ�����
								FlashContainer.this.listeners.clear();
								
								notifyAllCaller();
							}
						} else if (cmd.equals(CMD_DISPOSE)) {
							executor.dispose(val);
						} else if (cmd.equals(CMD_BOUNDS)) {
							
						}else if(cmd.equals(CMD_ASYNC_CALL)){
							try{
								executor.call(val);
							}catch(Exception e){
							    e.printStackTrace();
							}
						}
					}
				}
			}
		});
		// flash call
		oleControlSite.addEventListener(EVENT_FLASH_CALL, new OleListener() {
			public void handleEvent(OleEvent event) {
				Variant command = event.arguments[0];
				if (command != null) {
					String reqPack = command.getString();
					
					if (reqPack != null) {
						StringBuilder resPack = new StringBuilder("<string>");
						resPack.append(executor.execute(reqPack))
						.append("</string>");
						setReturnValue(resPack.toString());
					}
				}
			}
		});
	}

	private synchronized void notifyAllCaller() {
		this.notifyAll();
	}

	/**
	 * ����flash��ָ����������������߳�ֱ������ asyncCallFunction
	 * 
	 * @param info
	 */
	public synchronized String asyncCallFunction(final String info) {
		
		if (Display.getCurrent() == null) {
			final String[] ret = new String[1];
			this.getDisplay().asyncExec(new Runnable() {
				public void run() {
					ret[0] = directCall(info);
				}
			});
			// ��������߳�
			while (ret[0] == null) {
				try {
					this.wait(2);
				} catch (InterruptedException e) {
				}
			}
			return ret[0];
		} else {
			return directCall(info);
		}
	}

	private synchronized String directCall(final String info) {
		
		final String[] retArr = new String[1];
		Variant[] args = new Variant[1];
		args[0] = new Variant(info);
		
		Variant ret = this.flashObject.invoke(0x000000c6, args);
		if(ret==null){
			throw new RuntimeException("flex����null������flex�޷����ض�Ӧ�����࣬����flex��Ŀ����ʾ�����Ӧ������");
		}
		if (ret != null) {
			retArr[0] = ret.getString();
			ret.dispose();
		}
		return retArr[0].substring(8, retArr[0].length() - 9);
	}

	/**
	 * ���÷���ֵ
	 * 
	 * @param retVal
	 */
	public void setReturnValue(String retVal) {
		Variant[] args = new Variant[1];
		args[0] = new Variant(retVal);
		int methodId = this.flashObject.getDispID("SetReturnValue");
		Variant ret = flashObject.invoke(methodId, args);
		// System.out.println(ret);
		ret.dispose();
	}

	public void disableLocalSecurity() {
		// Variant[] args = new Variant[0];
		int methodId = this.flashObject.getDispID("DisableLocalSecurity");
		Variant ret = flashObject.invoke(methodId);
		ret.dispose();
	}
    public void superDispose(){
    	
    	super.dispose();
    }
    private String appId;
    public String getAppId(){
    	return appId;
    }
    public void setAppId(String appId){
    	this.appId=appId;
    }
    /**
     * ����swf�ļ�����������loadModule����������ģ��
     * @param layer
     * @param url ��һ��Ϊswf·�����ڶ���Ϊģ��·��
     */
    public void loadMovie(int layer,final String[] url) {
    	if(url==null || url.length<2){
    		return;
    	}
    	String runtimeSwfUrl=url[0];
    	//runtime������Ϻ����ģ��
        ILoadCompleteListener listener=new ILoadCompleteListener(){
        	public void run(){
    	String moduleUrl=url[1];
    	RemoteObject app=new RemoteObject(FlashContainer.this,false);
    	app.setRemoteId("app");
    	app.call("loadModule",new Object[]{moduleUrl});
        	}
        };
        this.addListener(listener);
    	super.loadMovie(layer, runtimeSwfUrl);
    	
    }
    private boolean isDispose=false;//ȷ��dispose����ֻ����һ��
	public void disposeResource() {
		if(isDispose){
			return;
		}
		isDispose=true;
		//��ȫ������������ɾ��
		//FlashContainer.remove_Container(this);
		//ɾ��������еĶ�Ӧ������
		Display curDisplay=Display.getCurrent();
		if(curDisplay==null){//�����̵߳���
		 this.getDisplay().asyncExec(new Runnable(){
			public void run() {
				FlashContainer.this.superDispose();
				ObjectPool.INSTANCE.clearAppPool(FlashContainer.this.getAppId());
			}
		 });
	   }else{
		   super.dispose();
		   ObjectPool.INSTANCE.clearAppPool(this.getAppId());
	   }
	}
	//�Զ���������
	protected void finalize(){
		this.dispose();
		
	}
	public void addListener(ILoadCompleteListener listener){
	    if(listener!=null){
	    	if(!this.listeners.contains(listener)){
	    		this.listeners.add(listener);
	    	}
	    }
	}
	public void removeListener(ILoadCompleteListener listener){
		if(listener!=null){
			this.listeners.remove(listener);
		}
	}
//	public Integer[] getRectangle(){
//		Integer[] ret=new Integer[2];
//		Rectangle rect=this.getShell().getDisplay().getBounds();
//		ret[0]=rect.width;
//		ret[1]=rect.height;
//	   
//		return ret;
//	}
	//public static String Cur_App_Id=null;//��ǰ��ý����flash
//	public boolean setFocus () {
//		Cur_App_Id=this.getAppId();
//		System.out.println("FlashContainer.setFocus()"+this.getAppId());
//		return super.setFocus();
//	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//OS.MessageBoxW(0, "��ã�\0".toCharArray(), "hello\0".toCharArray(), 0);
	}

}
