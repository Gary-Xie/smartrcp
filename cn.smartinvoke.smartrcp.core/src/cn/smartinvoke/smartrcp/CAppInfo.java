package cn.smartinvoke.smartrcp;

import java.util.LinkedList;
import java.util.List;

/**
 * smartrcpӦ�ó�����Ϣ��
 * @author pengzhen
 *
 */
public class CAppInfo {
	//Ӧ�ó������Ʊ�������ļ����У����淶
    public String name;
    //�汾��
    public double version;
    //Ӧ�ó���˵��
    public String describe;
    /**swf��ַ
    */
    public String mainSwfPath;//��swf��ַ
    public String splashSwfPath;//splash���������ַ
    public List<String> modules=null;//swfģ���ļ�·������
    /**
    * 
    */
	public CAppInfo(){
	    
	}
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
