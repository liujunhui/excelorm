package com.ljh.excel.bean.factory;

public class ExcelFactory {
	public static String PACTORYPATH = "";

	private ExcelFactory() {
	}

	public static void instance(String path) {
		PACTORYPATH = path;
	}
}
