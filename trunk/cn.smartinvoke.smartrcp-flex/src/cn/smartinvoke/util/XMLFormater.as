
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
	public class XMLFormater
	{
		private  static var quot:String="&quot;";
		private  static var apos:String="&apos;";
		private static var amp:String="&amp;";
		private static var lt:String="&lt;";
		private static var gt:String="&gt;";
    	
		public function XMLFormater()
		{
		}
        public static function changeToXML(middVal:String):String{
    	  middVal.replace(quot,'"');
    	  middVal.replace(apos,"'");
    	  middVal.replace(amp,'&');
    	  middVal.replace(lt,'<');
    	  middVal.replace(gt,'>');
    	  return middVal;
        }
	}
}