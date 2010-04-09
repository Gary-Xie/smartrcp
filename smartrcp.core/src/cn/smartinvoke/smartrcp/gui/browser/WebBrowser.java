package cn.smartinvoke.smartrcp.gui.browser;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import cn.smartinvoke.IServerObject;
import cn.smartinvoke.smartrcp.core.SmartRCPViewPart;
import cn.smartinvoke.smartrcp.gui.module.CEventBean;
import cn.smartinvoke.util.Log;

public class WebBrowser extends SmartRCPViewPart implements IServerObject {
	public static final String ID = "cn.smartinvoke.smartrcp.WebBrowser"; 
	 public Browser browser;
		private List<CEventBean> listeners=new LinkedList<CEventBean>();
	    @Override
		public void createPartControl(Composite parent) {
			super.createPartControl(parent);
			
			Composite container = new Composite(parent, SWT.NONE);
			container.setLayout(new FillLayout());
			browser=new Browser(container,SWT.NONE);
			//����¼�
			browser.addTitleListener(new TitleListener() {
				@Override
				public void changed(TitleEvent event){
					 WebBrowser.this.setViewTitle(event.title);
				}
			});
            browser.addProgressListener(new ProgressListener() {
				
				@Override
				public void completed(ProgressEvent event){
					//Log.println("ҳ��������...");
					for(int i=0;i<listeners.size();i++){
						CEventBean eventBean=listeners.get(i);
						try{
						 eventBean.fireOnetimeEvent(null);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				
				@Override
				public void changed(ProgressEvent event) {
				       
				}
			});
			createActions();
			initializeToolBar();
			initializeMenu();
		}
	    /**
	     * ���flex����������ҳ�������Ϻ���øü���������
	     * @param eventBean
	     */
	    public void addCompleteListener(CEventBean eventBean){
	      	if(eventBean!=null){
	      		listeners.add(eventBean);
	      	}
	    }
	    /**
	     * �ڵ�ǰҳ����ִ�д����js�ű�
	     * @param code
	     * @return
	     */
	    public boolean execute(String code){
	    	return this.browser.execute(code);
	    }
		
		public void openUrl(String url){
			this.browser.setUrl(url);
		}
		public void setHtml(String html){
			this.browser.setText(html);
		}
		
		
		
		public void dispose(){
			super.dispose();
		}
		public void setViewTitle(String title){
			this.setPartName(title);
		}
		/**
		 * Create the actions
		 */
		private void createActions() {
			
		}
		/**
		 * Initialize the toolbar
		 */
		private void initializeToolBar() {
			IToolBarManager toolbarManager = getViewSite().getActionBars()
					.getToolBarManager();
		}

		/**
		 * Initialize the menu
		 */
		private void initializeMenu() {
			IMenuManager menuManager = getViewSite().getActionBars()
					.getMenuManager();
		}

		@Override
		public void setFocus() {
			
		}
		

}
