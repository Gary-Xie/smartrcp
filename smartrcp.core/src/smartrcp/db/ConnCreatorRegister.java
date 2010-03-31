package smartrcp.db;

import java.util.HashMap;
import java.util.Map;
/**
 * ���ݿ����Ӵ�����ע�������
 * @author pengzhen
 *
 */
public class ConnCreatorRegister {
	//��ʵ��
	public static final ConnCreatorRegister Instance=new ConnCreatorRegister();
    private Map<String,IConnCreator> connCreators=
    new HashMap<String, IConnCreator>();
	private ConnCreatorRegister(){
		
	}
	/**
	 * ע�����ݿ����Ӵ�����
	 * @param name
	 * @param connCreator
	 */
    public void register(String name,IConnCreator connCreator){
       if(name!=null && connCreator!=null){
    	  connCreators.put(name, connCreator);
       }
    }
    /**
     * ɾ�����ݿ�������
     * @param name
     */
    public void remove(String name){
    	if(name!=null){
    		connCreators.remove(name);
    	}
    }
    /**
     * ���ָ�����Ƶ����ݿ����Ӵ�����
     * @param name
     * @return
     */
    public IConnCreator getConnCreator(String name){
    	return connCreators.get(name);
    }
}
