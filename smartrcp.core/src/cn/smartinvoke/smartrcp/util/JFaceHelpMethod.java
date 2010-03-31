package cn.smartinvoke.smartrcp.util;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;

import cn.smartinvoke.smartrcp.core.SmartRCPBuilder;

public class JFaceHelpMethod {

	private JFaceHelpMethod() {
      
	}
	/**
	 * ���path���ڵ��ļ��Ĵ����ԣ�������ʾ�û��Ƿ񸲸�
	 * @return trueΪ���ǣ�falseΪ������
	 */
	public static boolean checkOverWrite(String path){
		boolean ret = true;
		File file=new File(path);
		if (file.exists()){// �ļ����ڣ��ж��Ƿ񸲸�
			String tok="";
			if(file.isDirectory()){
				tok="��";
			}
			final String msg = "�ļ�"+tok + path + "�Ѵ��ڣ��Ƿ񸲸ǣ�";
			if (Display.getCurrent() == null) {
				final boolean[] res = new boolean[]{false};
				final boolean[] isRet = new boolean[]{false};
				SmartRCPBuilder.Main_Shell.getDisplay().asyncExec(
						new Runnable() {
							public void run() {
								boolean ret = MessageDialog.openQuestion(
										SmartRCPBuilder.Main_Shell, "ѡ��", msg);
								res[0] = ret;
								isRet[0] = true;// ����
							}
						});
				// ѭ���ȵ��û��ķ���
				while (true) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
					;
					if (isRet[0]) {
						ret = res[0];
						break;
					}
				}
			} else {
				ret = MessageDialog.openQuestion(SmartRCPBuilder.Main_Shell,
						"ѡ��", msg);
			}
		}
		return ret;
	}
	public static ImageDescriptor getImageDescriptor(String imgUrl) {
		//TODO רҵ��һ�ļ�����ͼ��ע���
		return null;//Activator.getImageDescriptor(imgUrl);
	}
	public static void showError(final String msg){
		if(Display.getCurrent()==null){
			SmartRCPBuilder.Main_Shell.getDisplay().asyncExec(new Runnable() {
				
				public void run() {
					MessageDialog.openError(SmartRCPBuilder.Main_Shell, "����", msg);
				}
			});
		}else{
			MessageDialog.openError(SmartRCPBuilder.Main_Shell, "����", msg);
		}
	}
	public static void showInfo(final String msg){
		if(Display.getCurrent()==null){
			SmartRCPBuilder.Main_Shell.getDisplay().asyncExec(new Runnable() {
				
				public void run() {
					MessageDialog.openInformation(SmartRCPBuilder.Main_Shell, "����", msg);
				}
			});
		}else{
			MessageDialog.openInformation(SmartRCPBuilder.Main_Shell, "����", msg);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
