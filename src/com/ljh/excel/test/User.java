package com.ljh.excel.test;

import com.ljh.excel.annotation.ColumnAnnotation;
import com.ljh.excel.bean.BaseBean;

//�û���
public class User extends BaseBean {
	@ColumnAnnotation("A")
	private String name;// �û�
	@ColumnAnnotation("B")
	private String department;// ����
	@ColumnAnnotation("C")
	private String hobby;// ����

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public User() {
		super();
	}

	public User(String name, String department, String hobby) {
		super();
		this.name = name;
		this.department = department;
		this.hobby = hobby;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", department=" + department + ", hobby=" + hobby + "]";
	}

}
