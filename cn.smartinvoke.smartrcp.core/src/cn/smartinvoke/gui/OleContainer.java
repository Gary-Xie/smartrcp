
/*******************************************************************************
* Copyright (c) 2009 SmartInvoke.
* ����Ը��ƻ��޸Ĵ˴��룬���Ǳ�����Դ����Ͷ����ư�����������Ա�ʾ���û��޸���smartinvoke�Ĵ���
* ����ϧ���ߵ��Ͷ��ɹ�* ������ͨ�� 
*  ��վ:http://smartinvoke.cn/ 
*  �ʼ�:pzxiaoxiao130130@gmail.com
*  QQ��90636900*  ��ϵ������^_^ 
*******************************************************************************/ 
package cn.smartinvoke.gui;

import org.eclipse.swt.widgets.Composite;
public abstract class OleContainer extends Composite {

	public OleContainer(Composite parent, int style) {
		super(parent, style);
	}

	public abstract void addHookInterceptor(OleHookInterceptor interceptor);
	public abstract OleHookInterceptor getHookInterceptor();
}
