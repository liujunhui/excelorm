package com.ljh.excel.parser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ljh.excel.annotation.ColumnAnnotation;
import com.ljh.excel.bean.BaseBean;
import com.ljh.excel.bean.factory.ExcelFactory;

//
public class BeanOperation {
	@SuppressWarnings("deprecation")
	public void saveAll(List<? extends BaseBean> data) throws Exception {
		// 创建一个webbook，对应一个Excel文件
		XSSFWorkbook wb = new XSSFWorkbook();
		// 在webbook中添加一个sheet,对应Excel文件中的sheet
		XSSFSheet sheet = wb.createSheet("sheet1");
		// 在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		createCell(data, sheet, 0);
		FileOutputStream fout = new FileOutputStream(
				ExcelFactory.PACTORYPATH + "/" + data.get(0).getClass().getSimpleName() + ".xlsx");
		wb.write(fout);
		fout.close();

	}

	public void addAll(List<? extends BaseBean> data) throws Exception {
		FileInputStream fs = new FileInputStream(
				ExcelFactory.PACTORYPATH + "/" + data.get(0).getClass().getSimpleName() + ".xlsx"); // 获取d://test.xls
		XSSFWorkbook wb = new XSSFWorkbook(fs);
		XSSFSheet sheet = wb.getSheetAt(0); // 获取到工作表，因为一个excel可能有多个工作表
		XSSFRow row = sheet.getRow(0); // 获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值
		System.out.println(sheet.getLastRowNum() + " " + row.getLastCellNum()); // 分别得到最后一行的行号，和一条记录的最后一个单元格

		FileOutputStream out = new FileOutputStream(
				ExcelFactory.PACTORYPATH + "/" + data.get(0).getClass().getSimpleName() + ".xlsx"); // 向d://test.xls中写数据
		createCell(data, sheet, sheet.getLastRowNum() + 1);
		out.flush();
		wb.write(out);
		out.close();
		System.out.println(row.getPhysicalNumberOfCells() + " " + row.getLastCellNum());
	}

	private void createCell(List<? extends BaseBean> data, XSSFSheet sheet, int first) {
		XSSFRow row;
		int i = first;
		for (BaseBean bean : data) {
			Class c = bean.getClass();
			Field[] filds = c.getDeclaredFields();
			row = sheet.createRow(i);
			i++;
			for (Field f : filds) {
				f.setAccessible(true);
				ColumnAnnotation canno = f.getAnnotation(ColumnAnnotation.class);
				if (canno != null) {
					String fname = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
					String s1 = canno.value();
					try {
						Method m = c.getDeclaredMethod(fname, new Class[] {});
						m.setAccessible(true);
						String s2 = (String) m.invoke(bean);

						char[] c1 = s1.toCharArray();
						int index = (int) c1[0] - 65;

						row.createCell((short) index).setCellValue(s2);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void deleteByIndex(int index, Class cls) {
		try {

			FileInputStream is = new FileInputStream(ExcelFactory.PACTORYPATH + "/" + cls.getSimpleName() + ".xlsx");

			XSSFWorkbook workbook = new XSSFWorkbook(is);

			XSSFSheet sheet = workbook.getSheetAt(0);

			removeRow(sheet, index);
			FileOutputStream os = new FileOutputStream(ExcelFactory.PACTORYPATH + "/" + cls.getSimpleName() + ".xlsx");

			workbook.write(os);

			is.close();

			os.close();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void removeRow(XSSFSheet sheet, int rowIndex) {
		int lastRowNum = sheet.getLastRowNum();
		if (rowIndex >= 0 && rowIndex < lastRowNum)
			// 将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行
			sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
		if (rowIndex == lastRowNum) {
			XSSFRow removingRow = sheet.getRow(rowIndex);
			if (removingRow != null)
				sheet.removeRow(removingRow);
		}
	}
}
