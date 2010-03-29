package cn.smartinvoke.smartrcp.app.download.http;

import java.io.*;
import java.net.*;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import cn.smartinvoke.smartrcp.app.pack.CAppInfo;
import cn.smartinvoke.smartrcp.app.pack.PackageTool;

import smartrcp.platform.Application;

public class SiteFileFetch extends Job {
	SiteInfoBean siteInfoBean = null; // �ļ���ϢBean
	long[] nStartPos; // ��ʼλ��
	long[] nEndPos; // ����λ��
	FileSplitterFetch[] fileSplitterFetch; // ���̶߳���
	long nFileLength; // �ļ�����
	private float mbSize=0;
	boolean bFirst = true; // �Ƿ��һ��ȡ�ļ�
	boolean bStop = false; // ֹͣ��־
	File tmpFile; // �ļ����ص���ʱ��Ϣ
	DataOutputStream output; // ������ļ��������
    private IProgressMonitor monitor;
    //������ɺ�ļ�����
    public ICompleteListener completeListener;
    
	public SiteFileFetch(String taskName,SiteInfoBean bean,ICompleteListener completeListener){
		super(taskName);
		this.completeListener=completeListener;
		siteInfoBean = bean;
		tmpFile = new File(bean.getSFilePath() + File.separator
				+ bean.getSFileName() + ".info");
		if (tmpFile.exists()){
			bFirst = false;
			try{
				read_nPos();
			}catch(Exception e){
				tmpFile.delete();//ɾ�������ļ�
				this.finishDownload("���������ļ���ȡ����", true);
			}
		} else {
			nStartPos = new long[bean.getNSplitter()];
			nEndPos = new long[bean.getNSplitter()];
		}
	}
	
	protected IStatus run(IProgressMonitor monitor) {
		this.monitor=monitor;
		// ����ļ�����
		// �ָ��ļ�
		// ʵ��FileSplitterFetch
		// ����FileSplitterFetch�߳�
		// �ȴ����̷߳���
		try {
			if (bFirst) {
				nFileLength = getFileSize();
				if (nFileLength == -1) {
					this.finishDownload("�޷�����ļ�"+siteInfoBean.getSSiteURL()+"����",true);
				} else if (nFileLength == -2){
					this.finishDownload("�޷������ļ�"+siteInfoBean.getSSiteURL(),true);
				} else {
					this.mbSize=(float)nFileLength/1048576f;//�ļ��״�С
					//��ʼ������ʾ
					monitor.beginTask(siteInfoBean.getSFileName(),(int)nFileLength);
					
					for (int i = 0; i < nStartPos.length; i++) {
						nStartPos[i] = (long) (i * (nFileLength / nStartPos.length));
					}
					for (int i = 0; i < nEndPos.length - 1; i++) {
						nEndPos[i] = nStartPos[i + 1];
					}
					nEndPos[nEndPos.length - 1] = nFileLength;
				}
			}else{
				this.mbSize=(float)nFileLength/1048576f;//�ļ��״�С
				//��ʼ������ʾ
				monitor.beginTask(siteInfoBean.getSFileName(),(int)nFileLength);
				//��ʾ����ɽ���
				monitor.worked((int)(this.nFileLength-restBts));
			}
			// �������߳�
			fileSplitterFetch = new FileSplitterFetch[nStartPos.length];
			for (int i = 0; i < nStartPos.length; i++) {
				fileSplitterFetch[i] = new FileSplitterFetch(this,siteInfoBean
						.getSSiteURL(), siteInfoBean.getSFilePath()
						+ File.separator + siteInfoBean.getSFileName(),
						nStartPos[i], nEndPos[i], i);
				//Utility.log("Thread " + i + " , nStartPos = " + nStartPos[i]
				//		+ ", nEndPos = " + nEndPos[i]);
				fileSplitterFetch[i].start();
			}
			boolean breakWhile = false;
			while (!bStop) {
				write_nPos();
				//����û�ȡ��������
				if (monitor.isCanceled()){
					this.siteStop();
					return Status.CANCEL_STATUS;
				}
				Utility.sleep(500);
				breakWhile = true;
				for (int i = 0; i < nStartPos.length; i++) {
					if (!fileSplitterFetch[i].bDownOver) {
						breakWhile = false;
						break;
					}
				}
				if (breakWhile)
					break;
			}
			if(!isErrorShutDown){//���Ǵ����˳�
			  finishDownload("�ļ�"+siteInfoBean.getSSiteURL()+"���ؽ���",false);
			  //���Ѽ�����
			  if(completeListener!=null){ 
			   String savePath=siteInfoBean.getSFilePath()+File.separator+siteInfoBean.getSFileName();
			   CAppInfo appInfo=PackageTool.readBasicInfo(savePath);
			   completeListener.complete(appInfo);
			  }
			  tmpFile.delete();//ɾ�������ļ�
			}
			
		} catch (Exception e) {
			errorShutDown(e);
		}
		return Status.OK_STATUS;
	}
	//�������ɽ���
	public synchronized void addWorked(int num){
		this.monitor.worked(num);
	}
	
	/**
	 * �����û���������ִ�����
	 * @param info
	 */
    private void finishDownload(String info,boolean isError){
    	monitor.done();
    	if(isError){
    		Application.openError(info);
    	}else{
    	    Application.openInfo(info);
    	}
    }
    private boolean isErrorShutDown=false;
    public void errorShutDown(Throwable e){
    	Throwable casue=e;
    	isErrorShutDown=true;
		while(true){
			Throwable temp=e.getCause();
			if(temp==null){
				break;
			}else{
				casue=temp;
			}
		}
		this.siteStop();
		finishDownload("�������ع����г���ԭ�����£�\n"+casue.getMessage(),true);
    }
	// ����ļ�����
	public long getFileSize() {
		int nFileLength = -1;
		try {
			URL url = new URL(siteInfoBean.getSSiteURL());
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection.setRequestProperty("User-Agent", "NetFox");
			int responseCode = httpConnection.getResponseCode();
			if (responseCode >= 400) {
				processErrorCode(responseCode);
				return -2; // -2 represent access is error
			}
			String sHeader;
			for (int i = 1;; i++) {
				// DataInputStream in = new
				// DataInputStream(httpConnection.getInputStream ());
				// Utility.log(in.readLine());
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null) {
					if (sHeader.equals("Content-Length")) {
						nFileLength = Integer.parseInt(httpConnection
								.getHeaderField(sHeader));
						break;
					}
				} else
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Utility.log(nFileLength);
		return nFileLength;
	}
    private int restBts=0;
	// ����������Ϣ���ļ�ָ��λ�ã�
	private void write_nPos() throws Exception{
			output = new DataOutputStream(new FileOutputStream(tmpFile));
			output.writeInt((int)this.nFileLength);//�洢�ļ�����
			output.writeInt(nStartPos.length);
			restBts=0;//δ���صı�����
			for (int i = 0; i < nStartPos.length; i++){
				output.writeLong(fileSplitterFetch[i].nStartPos);
				output.writeLong(fileSplitterFetch[i].nEndPos);
				restBts+=fileSplitterFetch[i].nEndPos-fileSplitterFetch[i].nStartPos;
			}
			
			float alreadyDownload=((float)(this.nFileLength-restBts))/1048576f;
			monitor.subTask(this.siteInfoBean.getSSiteURL()+"  "+alreadyDownload+"/" +this.mbSize);
			
			output.close();
            
	}

	// ��ȡ�����������Ϣ���ļ�ָ��λ�ã�
	private void read_nPos()throws Exception{
		
			DataInputStream input = new DataInputStream(new FileInputStream(
					tmpFile));
			this.nFileLength=input.readInt();
			
			
			int nCount = input.readInt();
			nStartPos = new long[nCount];
			nEndPos = new long[nCount];
			restBts=0;//δ���صı�����
			for (int i = 0; i < nStartPos.length; i++) {
				nStartPos[i] = input.readLong();
				nEndPos[i] = input.readLong();	
				restBts+=nEndPos[i]-nStartPos[i];
			}
			
			input.close();
	}

	private void processErrorCode(int nErrorCode) {
		System.err.println("Error Code : " + nErrorCode);
	}
	// ֹͣ�ļ�����
	public void siteStop() {
		bStop = true;
		if(fileSplitterFetch!=null){
		 for (int i = 0; i < fileSplitterFetch.length; i++){
			if(fileSplitterFetch[i]!=null){
				fileSplitterFetch[i].splitterStop();
			}
		 }
		}
	}
	public static void main(String[] args) {
		try{
            
			 System.out.println(1024*1024);
			}
			catch(Exception e){e.printStackTrace ();}

	}
}
