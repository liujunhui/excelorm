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
		// ����һ��webbook����Ӧһ��Excel�ļ�
		XSSFWorkbook wb = new XSSFWorkbook();
		// ��webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
		XSSFSheet sheet = wb.createSheet("sheet1");
		// ��sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
		createCell(data, sheet, 0);
		FileOutputStream fout = new FileOutputStream(
				ExcelFactory.PACTORYPATH + "/" + data.get(0).getClass().getSimpleName() + ".xlsx");
		wb.write(fout);
		fout.close();

	}

	public void addAll(List<? extends BaseBean> data) throws Exception {
		FileInputStream fs = new FileInputStream(
				ExcelFactory.PACTORYPATH + "/" + data.get(0).getClass().getSimpleName() + ".xlsx"); // ��ȡd://test.xls
		XSSFWorkbook wb = new XSSFWorkbook(fs);
		XSSFSheet sheet = wb.getSheetAt(0); // ��ȡ����������Ϊһ��excel�����ж��������
		XSSFRow row = sheet.getRow(0); // ��ȡ��һ�У�excel�е���Ĭ�ϴ�0��ʼ�����������Ϊʲô��һ��excel�������ֶ���ͷ���������ֶ���ͷ�����ڸ�ֵ
		System.out.println(sheet.getLastRowNum() + " " + row.getLastCellNum()); // �ֱ�õ����һ�е��кţ���һ����¼�����һ����Ԫ��

		FileOutputStream out = new FileOutputStream(
				ExcelFactory.PACTORYPATH + "/" + data.get(0).getClass().getSimpleName() + ".xlsx"); // ��d://test.xls��д����
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
			// ���к�ΪrowIndex+1һֱ���к�ΪlastRowNum�ĵ�Ԫ��ȫ������һ�У��Ա�ɾ��rowIndex��
			sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
		if (rowIndex == lastRowNum) {
			XSSFRow removingRow = sheet.getRow(rowIndex);
			if (removingRow != null)
				sheet.removeRow(removingRow);
		}
	}
}
