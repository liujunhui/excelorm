package com.ljh.excel.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.ljh.excel.bean.factory.ExcelFactory;

//解析excel文件的基类
public class BaseParser {
	public static ContentHandler handler;
	protected List beanList = new ArrayList();
	protected Class cls;
	protected Object bean;

	public void processFirstSheet(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);

		// To look up the Sheet Name / Sheet Order / rID,
		// you need to process the core Workbook stream.
		// Normally it's of the form rId# or rSheet#
		InputStream sheet2 = r.getSheet("rId2");
		InputSource sheetSource = new InputSource(sheet2);
		parser.parse(sheetSource);
		sheet2.close();
	}

	public List processAllSheets() throws Exception {

		OPCPackage pkg = OPCPackage.open(ExcelFactory.PACTORYPATH + "/" + cls.getSimpleName() + ".xlsx");
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);

		Iterator<InputStream> sheets = r.getSheetsData();
		while (sheets.hasNext()) {
			// System.out.println("Processing new sheet:\n");
			InputStream sheet = sheets.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
			if (beanList != null && bean != null) {
				beanList.add(bean);
			}
			// System.out.println("end-->");
		}
		return beanList;
	}

	static XMLReader xmlReader = null;
	static {
		try {
			xmlReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader xmlReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		// ContentHandler handler = new SheetHandler(sst);
		xmlReader.setContentHandler(handler);
		return xmlReader;
	}

}
