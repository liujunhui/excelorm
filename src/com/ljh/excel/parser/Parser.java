package com.ljh.excel.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.ljh.excel.annotation.ColumnAnnotation;
import com.ljh.excel.test.User;

public class Parser extends BaseParser {
	/**
	 * See org.xml.sax.helpers.DefaultHandler javadocs
	 */

	public Parser(Class cl) {
		cls = cl;
	}

	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		ContentHandler handler = new SheetHandler(sst);
		xmlReader.setContentHandler(handler);
		return xmlReader;
	}

	class SheetHandler extends DefaultHandler {
		private SharedStringsTable sst;
		private String lastContents;
		private boolean nextIsString;

		private SheetHandler(SharedStringsTable sst) {
			this.sst = sst;

		}

		String lastTag = "";
		String con = "";

		@Override
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			// c => cell
			if (name.equals("c")) {
				String attributest = attributes.getValue("r");
				String attributestr = attributest.substring(1);
				con = attributest.substring(0, 1);
				if (attributestr != null && !attributestr.equals("")
						&& (lastTag.equals("") || !attributestr.equals(lastTag))) {
					lastTag = attributestr;

					// System.out.println("");

					if (bean != null) {
						beanList.add(bean);
						// System.out.println("bean:" + bean);
					}
					try {
						bean = cls.newInstance();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				// Print the cell reference
				// System.out.print("(->"+attributes.getValue("r") + " - ");
				// Figure out if the value is an index in the SST
				String cellType = attributes.getValue("t");
				if (cellType != null && cellType.equals("s")) {
					nextIsString = true;
				} else {
					nextIsString = false;
				}
			}
			// Clear contents cache
			lastContents = "";
		}

		public void endElement(String uri, String localName, String name) throws SAXException {
			// Process the last contents as required.
			// Do now, as characters() may be called more than once
			if (nextIsString) {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				nextIsString = false;
			}

			// v => contents of a cell
			// Output after we've seen the string contents
			if (name.equals("v")) {
				Class c = bean.getClass();
				Field[] fs = c.getDeclaredFields();

				for (Field f : fs) {
					f.setAccessible(true);
					ColumnAnnotation canno = f.getAnnotation(ColumnAnnotation.class);
					if (canno != null && canno.value().equals(con)) {
						String fname = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
						try {
							Method m = c.getDeclaredMethod(fname, new Class[] { String.class });
							m.setAccessible(true);
							m.invoke(bean, lastContents);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}

		public void characters(char[] ch, int start, int length) throws SAXException {
			lastContents += new String(ch, start, length);
		}
	}

}
