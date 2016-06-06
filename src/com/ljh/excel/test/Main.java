package com.ljh.excel.test;

import java.util.ArrayList;
import java.util.List;
import com.ljh.excel.bean.factory.ExcelFactory;

public class Main {
	public static void main(String[] args) {

		try {

			ExcelFactory.instance("D:/exceldata");

			List list = new ArrayList();
			User user1 = new User("������1","������","���");
			User user2 = new User("������2","������","���");
			User user3 = new User("������3","������","���");
			User user4 = new User("������4","������","���");
			list.add(user1);
			list.add(user2);
			list.add(user3);
			list.add(user4);
			
			user1.saveAll(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
