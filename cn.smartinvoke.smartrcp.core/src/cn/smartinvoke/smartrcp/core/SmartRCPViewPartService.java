package cn.smartinvoke.smartrcp.core;

import java.util.HashMap;
import java.util.Map;

/**
 * ��smartrcp������ʱ����Ҫswt������flex���漯�ɣ�ͨ�����࣬���Խ�
 * �����ʵ��swt��flex����ļ���
 * @author pengzhen
 *
 */
public class SmartRCPViewPartService {
    public static final SmartRCPViewPartService Instance=new SmartRCPViewPartService();
    /**
     * swt���浥Ԫ���洢��map
     */
    private Map<String,ISWTPartUnit> viewParts=new HashMap<String, ISWTPartUnit>(5);
	private SmartRCPViewPartService() {
		
	}
	/**
	 * ��չ���룬���ô˷���ע����浥Ԫ����ʵ��swt������Flex����Ļ���
	 * @param partUnit
	 */
    public void registerSWTUnit(ISWTPartUnit partUnit){
    	if(partUnit!=null){
    		//����Ѿ�ע���ˣ���ʲôҲ����
    		if(!viewParts.containsKey(partUnit.getId())){
    		   viewParts.put(partUnit.getId(), partUnit);
    		}
    	}
    }
    /**
     * ����ȫ����ע��Ľ��浥Ԫ���÷���ֻ�ǰ��ɼ�
     * @return
     */
    Map<String,ISWTPartUnit> getSWTPartUnits(){
    	return viewParts;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String,String> viewParts=new HashMap<String, String>(1);
		for(int i=0;i<20;i++){
			viewParts.put("key"+i,"value"+i);
		}
		System.out.println(viewParts.size());
	}

}
