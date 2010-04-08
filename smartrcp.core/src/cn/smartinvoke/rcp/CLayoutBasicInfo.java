package cn.smartinvoke.rcp;

import org.eclipse.ui.IMemento;

public class CLayoutBasicInfo {
	private static String key_layoutBasicInfo="smartrcp_key_layoutBasicInfo";
	//��ǰlayout��viewId����Ӧ��swf�Ƿ���ģ��swf
	public boolean isModuleSwf=true;
	//�Ƿ���FlashViewer�򿪵�ʱ���Զ�����swf
	public boolean autoLoad=false;
	//RCP��viewPart����ID
	public String viePartId=null;
	//��Ҫ���ص�ģ���Id
	public String modulePath=null;
    
	public boolean closeable = true;
	public boolean moveable = true;
	
	public String title;
	public String image=null;
	public CLayoutBasicInfo(){
	   
	}
	public boolean init(IMemento memento){
		if(memento!=null){
			String info=memento.getString(key_layoutBasicInfo);
			if(info!=null){
				String[] items=info.split("\n");
				if(items!=null){
					this.isModuleSwf=Boolean.valueOf(items[0]);
					this.autoLoad=Boolean.valueOf(items[1]);
					this.modulePath=items[2];
					this.title=items[3];
					this.image=items[4];
				}
			}
			return true;
		}else{
			return false;
		}
	}
	public void save(IMemento memento){
		if(memento!=null){
			String info=this.isModuleSwf+"\n"+this.autoLoad+"\n"+this.modulePath
			            +"\n"+this.title+"\n"+this.image;
			memento.putString(key_layoutBasicInfo, info);
		}
	}
	
	public static void main(String[] args) {
		String str="a\n b\n c\n";
		String[] items=str.split("\n");
		System.out.println(items);
	}
}
