
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* ����Ը��ƻ��޸Ĵ˴��룬���Ǳ�����Դ����Ͷ����ư�����������Ա�ʾ���û��޸���smartinvoke�Ĵ���
* ����ϧ���ߵ��Ͷ��ɹ�* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package cn.smartinvoke.gui;

public class NumberVal {
public int  value;
	
	public NumberVal (int  value) {
		this.value = value;
	}
	public boolean equals (Object object) {
		if (object == this) return true;
		if (!(object instanceof NumberVal)) return false;
		NumberVal obj = (NumberVal)object;
		return obj.value == this.value;
	}

	public int hashCode () {
		return (int)value;
	}

}
