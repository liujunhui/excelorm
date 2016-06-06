package com.ljh.excel.bean;

import java.util.List;

import com.ljh.excel.parser.BeanOperation;
import com.ljh.excel.parser.Parser;

//bean的基类，封装bean的增，删，改，查操作
public class BaseBean {
	// 输出所有该bean的数据
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

	// 保存一个bean的数据集合，会覆盖原先的数据
	public void saveAll(List<? extends BaseBean> data) {
		BeanOperation p = new BeanOperation();
		try {
			p.saveAll(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 追加数据，不会覆盖原先的数据
	public void addAll(List<? extends BaseBean> data) {
		BeanOperation p = new BeanOperation();
		try {
			p.addAll(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 根据行索引删除行数据
	public void deleteByIndex(int index) {
		BeanOperation p = new BeanOperation();
		p.deleteByIndex(index, this.getClass());
	}
}
