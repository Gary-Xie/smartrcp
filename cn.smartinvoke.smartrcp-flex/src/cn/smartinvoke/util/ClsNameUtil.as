
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* ����Ը��ƻ��޸Ĵ˴��룬���Ǳ�����Դ����Ͷ����ư�����������Ա�ʾ���û��޸���smartinvoke�Ĵ���
* ����ϧ���ߵ��Ͷ��ɹ�* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package cn.smartinvoke.util
{
	public class ClsNameUtil
	{
		public function ClsNameUtil()
		{
		}
        public static function  addRightSpace(str:String, len:int):String {
		    var orgLen:int = str.length;
		    var spaceCount:int = len - orgLen;
		    if (spaceCount > 0) {
			  for (var i:int = 0; i < spaceCount; i++) {
                str+=" ";
			  }
		    } else {
			  str = str.substring(0, len);
		    }
		    return str;
	   }
	   public static function getMiddValClsName(clsName:String):String{
	   	 var middValClsName:String=clsName.replace("::",".");
	   	 return middValClsName;
	   }
	}
}