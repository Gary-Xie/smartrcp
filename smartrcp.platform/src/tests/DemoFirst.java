package tests;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import cn.smartinvoke.RemoteObject;
import cn.smartinvoke.TypeMapper;
import cn.smartinvoke.gui.FlashContainer;
import cn.smartinvoke.gui.ILoadCompleteListener;
import cn.smartinvoke.gui.Msg;
import cn.smartinvoke.gui.OleHookInterceptor;
import cn.smartinvoke.gui.Win32Constant;
public class DemoFirst extends Shell {

	/**
	 * Launch the application
	 * @param args
	 */
	static Display Display;
	public static void main(String args[]) {
		try {
			
			Display display = Display.getDefault();
			Display=display;
			DemoFirst shell = new DemoFirst(display, SWT.SHELL_TRIM);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell
	 * @param display
	 * @param style
	 */
	public DemoFirst(Display display, int style) {
		super(display, style);
		createContents();
		setLayout(new FillLayout());
	}
	FlashContainer flashContainer;
	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		setText("smartinvoke���Գ���");
		//��flash�ؼ���ӵ�����
		String appId="first";
		this.flashContainer=new FlashContainer(this,appId);
		//�����Ϣ����
		this.flashContainer.addHookInterceptor(new OleHookInterceptor(){
			/**����˷�������false��ʾ�����˴���Ϣ������true��ʾ��Ϣ�����˵��ˣ���ô
			 *flashPlayer�ͽ��ղ�����Ӧ����Ϣ�¼���Ҳ��ʵ��������ϵͳ�Ҽ��˵����Զ���
			 *�����¼���Ŀ��
			 */
			public boolean intercept(Msg message, int code, int wParam,
					int lParam) {
				int msg=message.getMessage();
				
				if (msg== Win32Constant.WM_RBUTTONDOWN) {//ϵͳ�Ҽ���Ϣ
					
					Point cursor = flashContainer.getParent().toControl(
							Display.getCurrent().getCursorLocation());
					//�����ǰ�����flashPlayer������
					if (flashContainer.getBounds().contains(cursor) && flashContainer.isVisible()) {
						//��ʾһ��Ϣ�Ի���
						MessageBox messageBox = new MessageBox(flashContainer
								.getShell());
						messageBox.setMessage("���������Ҽ�");
						messageBox.open();
						return true;//����true��������flashPlayer��ϵͳ�Ҽ�
					}
					
				}
				if(msg==Win32Constant.WM_KEYDOWN){//���̰��������¼�
					if(message.getWParam()==Win32Constant.VK_RETURN){
						System.out.println("���»س�");
					}
				}
				//���µĴ�����Բ��񵽼�����ϼ�
				if((OS.GetKeyState(Win32Constant.VK_CONTROL)<0)&&(OS.GetKeyState(Win32Constant.VK_DELETE)<0)&&(message.getWParam()   ==   'Z')){
					System.out.println("control DELETE and Z...........");
				}
				return false;
			}
		});
		
		/**
		 *��flash������Ϻ�FlashContainer�����ILoadCompleteListener
		 *��run�������������������һЩ��ʼ��
		 */
		flashContainer.addListener(new ILoadCompleteListener(){
			public void run() {
				
				runBackagoundTask();
			}	
		});
		TypeMapper.addServiceConfig("test");
		
		//����smartinvoke.mxml�������ɵ�smartinvoke.swf
		//String path="E:/flexWork/SmartRCPDemo/bin-debug/NativeFileLoader.swf";
		//flashContainer.loadMovie(0,path);
		String path="E:/flexWork/cn.smartinvoke.smartrcp-flex/bin-debug/PlatformManager.swf";
		flashContainer.loadMovie(0, path);
		setSize(500, 375);
		
		//
	}
    private void runBackagoundTask(){
    	//���������ִ̨���߳�
    	for(int i=0;i<1;i++){
		   Thread task=new Thread("thread"+i){
			   public void run(){
				   //ģ���̨����
				  try {
					Thread.sleep(1000);//�ȴ�10��
					
					RemoteObject flexShell=new RemoteObject(flashContainer);
					flexShell.setRemoteId("app");
					for(int i=0;i<1000;i++){
					 flexShell.asyncCall("setInfo",new Object[]{ i+""});
					}
					flashContainer.dispose();
					
					final DemoFirst curShell=DemoFirst.this;
					//DemoFirst.this.close();
					//DemoFirst.this.dispose();
					
					Display.asyncExec(new Runnable(){

						public void run() {
							curShell.close();
							curShell.dispose();
							
							DemoFirst shell = new DemoFirst(Display, SWT.SHELL_TRIM);
							shell.open();
							shell.layout();
							
							while (!shell.isDisposed()) {
								if (!Display.readAndDispatch())
									Display.sleep();
							}
						}
						
					});
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			   }
		   };
		   
		   task.setDaemon(true);//����̨�߳�����Ϊ�ػ��̣߳���֤���߳��˳�ʱ���߳�Ҳ���˳�
		   task.start();
    	}
    }
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
    
}
