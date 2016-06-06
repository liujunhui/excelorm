package com.ljh.excel.bean;

import java.util.List;

import com.ljh.excel.parser.BeanOperation;
import com.ljh.excel.parser.Parser;

//bean�Ļ��࣬��װbean������ɾ���ģ������
public class BaseBean {
	// ������и�bean������
	public List selectAll() {
		Parser p = new Parser(this.getClass());
		try {
			List list = p.processAllSheets();
			System.out.println(list);
			return list;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// ����һ��bean�����ݼ��ϣ��Ḳ��ԭ�ȵ�����
	public void saveAll(List<? extends BaseBean> data) {
		BeanOperation p = new BeanOperation();
		try {
			p.saveAll(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ׷�����ݣ����Ḳ��ԭ�ȵ�����
	public void addAll(List<? extends BaseBean> data) {
		BeanOperation p = new BeanOperation();
		try {
			p.addAll(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����������ɾ��������
	public void deleteByIndex(int index) {
		BeanOperation p = new BeanOperation();
		p.deleteByIndex(index, this.getClass());
	}
}
