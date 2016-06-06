package com.ljh.excel.test;

import com.ljh.excel.annotation.ColumnAnnotation;
import com.ljh.excel.bean.BaseBean;

//用户表
public class User extends BaseBean {
	@ColumnAnnotation("A")
	private String name;// 用户
	@ColumnAnnotation("B")
	private String department;// 部门
	@ColumnAnnotation("C")
	private String hobby;// 部门

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
