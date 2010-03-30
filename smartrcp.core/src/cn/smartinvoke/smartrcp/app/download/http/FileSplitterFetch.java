package cn.smartinvoke.smartrcp.app.download.http;

import java.io.*;
import java.net.*;

public class FileSplitterFetch extends Thread {
	String sURL; // File URL
	long nStartPos; // File Snippet Start Position
	long nEndPos; // File Snippet End Position
	int nThreadID; // Thread's ID
	boolean bDownOver = false; // Downing is over
	boolean bStop = false; // Stop identical
	FileAccessI fileAccessI = null; // File Access interface
private SiteFileFetch siteFileFetch;
	public FileSplitterFetch(SiteFileFetch siteFileFetch,String sURL, String sName, long nStart, long nEnd,
			int id) throws IOException {
		this.siteFileFetch=siteFileFetch;
		this.sURL = sURL;
		this.nStartPos = nStart;
		this.nEndPos = nEnd;
		nThreadID = id;
		fileAccessI = new FileAccessI(sName, nStartPos);
	}

	public void run() {
		while (nStartPos < nEndPos && !bStop) {
			try {
				URL url = new URL(sURL);
				HttpURLConnection httpConnection = (HttpURLConnection) url
						.openConnection();
				httpConnection.setRequestProperty("User-Agent", "NetFox");
				String sProperty = "bytes=" + nStartPos + "-";
				httpConnection.setRequestProperty("RANGE", sProperty);
				Utility.log(sProperty);
				InputStream input = httpConnection.getInputStream();
				// logResponseHead(httpConnection);
				byte[] b = new byte[1024];
				int nRead;
				while ((nRead = input.read(b, 0, 1024)) > 0
						&& nStartPos < nEndPos && !bStop) {
					nStartPos += fileAccessI.write(b, 0, nRead);
					//���ӽ�����Ϣ
					this.siteFileFetch.addWorked(nRead);
					
				}
				//Utility.log("Thread " + nThreadID + " is over!");
				bDownOver = true;
			} catch (Exception e) {
				this.siteFileFetch.errorShutDown(e);
			}finally{
				if(this.fileAccessI!=null){
					this.fileAccessI.close();
				}
			}
		}
		if(nStartPos==nEndPos){
			bDownOver = true;
		}
	}

	// ��ӡ��Ӧ��ͷ��Ϣ
	public void logResponseHead(HttpURLConnection con) {
		for (int i = 1;; i++) {
			String header = con.getHeaderFieldKey(i);
			if (header != null)
				// responseHeaders.put(header,httpConnection.getHeaderField(header));
				Utility.log(header + " : " + con.getHeaderField(header));
			else
				break;
		}
	}

	public void splitterStop() {
		bStop = true;
	}
}
