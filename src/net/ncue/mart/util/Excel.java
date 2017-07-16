package net.ncue.mart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.ncue.conf.Configure;
import net.ncue.mart.model.Sale;
import net.ncue.util.database.Database;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
	Database database = new Database();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	KMAKwd kMAKwd = new KMAKwd();
	
	public String normalizeProductName(String str) {
		if (str==null || str.trim().equals("")) return "";
		str = str.replace("EXP_MB)", "");
		str = str.replace("EXP)", "");
		str = str.replace("청우)", "");
		str = str.replace("ST)", "");
		str = str.replace("FE)", "");
		str = str.replace("GD)", "");
		str = str.replace("D)", "");
		str = str.replace("E)", "");
		str = str.replace("P)", "");
		str = str.replace("_송이", "");
		str = str.replace("_개", "");
		str = str.replace("_팩", "");
		str = str.replace("_봉", "");
		
		str = str.replaceAll("--.*", "");
		str = str.replaceAll("\\(.*\\)", "");
		str = str.replaceAll("_", " ").trim();

		return str;
	}
	
	public String getCategory(String mcategory) {
		String sql = "select category from category where mcategory='"+mcategory+"'";

		String[][] arr = this.database.getRSet(sql);
		if (arr==null || arr.length<1) {
			return "";
		} else {
			return arr[0][0];
		}
	}
	
	public String getCellData(XSSFCell cell) {
		return getCellData(cell, false);	
	}
	public String getCellData(XSSFCell cell, boolean isDate) {
		if (cell==null) return "";
		switch(cell.getCellType()){
		case Cell.CELL_TYPE_NUMERIC:
			if (isDate) return sdf.format(cell.getDateCellValue());
			else return Integer.toString((int)cell.getNumericCellValue());
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_FORMULA :
			return cell.getCellFormula();
		case Cell.CELL_TYPE_BLANK :
			return "";
		default:
			return "";
		}
	}
	
	public String getCellData(HSSFCell cell) {
		return getCellData(cell, false);	
	}
	public String getCellData(HSSFCell cell, boolean isDate) {
		if (cell==null) return "";
		switch(cell.getCellType()){
		case Cell.CELL_TYPE_NUMERIC:
			if (isDate) return sdf.format(cell.getDateCellValue());
			else return Integer.toString((int)cell.getNumericCellValue());
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_FORMULA :
			return cell.getCellFormula();
		case Cell.CELL_TYPE_BLANK :
			return "";
		default:
			return "";
		}
	}
	
	public List<Sale> processXls(String excelFile) {
		List<Sale> saleList = new ArrayList<Sale>();
		try{
			this.database = new Database(Configure.DBHOST, Configure.DBPORT, "mart", Configure.DBID, Configure.DBPASSWORD, false);
			HSSFWorkbook workBook  =  new HSSFWorkbook(new FileInputStream(new File(excelFile)));
			HSSFSheet sheet    =  null;
			HSSFRow row     =  null;
			
			int sheetNum    =  workBook.getNumberOfSheets();
			if (sheetNum<1) {
				workBook.close();
				return saleList;
			}
			sheet     =  workBook.getSheetAt(0);
			int rows    =  sheet.getPhysicalNumberOfRows();
			for (int r=1; r<rows; r++) {
				row     =  sheet.getRow(r);
				HSSFCell mcategoryCell			= row.getCell(0);
				HSSFCell productCell			= row.getCell(1);
				HSSFCell headCell				= row.getCell(2);
				HSSFCell countCell				= row.getCell(3);
				HSSFCell morecountCell			= row.getCell(4);
				HSSFCell isaddCell				= row.getCell(5);
				HSSFCell priceCell				= row.getCell(6);
				HSSFCell conditionalpriceCell	= row.getCell(7);
				HSSFCell originalpriceCell		= row.getCell(8);
				HSSFCell sDateCell				= row.getCell(9);
				HSSFCell eDateCell				= row.getCell(10);
				HSSFCell descCell				= row.getCell(11);
				if (mcategoryCell==null && productCell==null && headCell==null && countCell==null && morecountCell==null && isaddCell==null && priceCell==null && conditionalpriceCell==null && originalpriceCell==null && sDateCell==null && eDateCell==null && descCell==null) break;
				
				String mcategory			= this.getCellData(mcategoryCell);
				String category				= this.getCategory(mcategory);
				String product				= this.getCellData(productCell);
				String productIdx			= this.normalizeProductName(product);
				String head					= this.getCellData(headCell);
				String idx1					= kMAKwd.analyzeProduct(productIdx);
				String count				= this.getCellData(countCell);
				String morecount			= this.getCellData(morecountCell);
				String isadd				= this.getCellData(isaddCell);
				String price				= this.getCellData(priceCell);
				String conditionalprice		= this.getCellData(conditionalpriceCell);
				String originalprice		= this.getCellData(originalpriceCell);
				String sDate				= this.getCellData(sDateCell, true);
				String eDate				= this.getCellData(eDateCell, true);
				String desc					= this.getCellData(descCell);
				saleList.add(new Sale(mcategory, category, product, productIdx, head, idx1, count, morecount, isadd, price, conditionalprice, originalprice, desc, sDate, eDate));
			}
			workBook.close();
			this.database.close();
			return saleList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return saleList;
	}

	public List<Sale> processXlsx(String excelFile) {
		List<Sale> saleList = new ArrayList<Sale>();
		try{
			this.database = new Database(Configure.DBHOST, Configure.DBPORT, "mart", Configure.DBID, Configure.DBPASSWORD, false);

			XSSFWorkbook workBook  =  new XSSFWorkbook(new FileInputStream(new File(excelFile)));
			XSSFSheet sheet    =  null;
			XSSFRow row     =  null;
			   
			int sheetNum    =  workBook.getNumberOfSheets();
			if (sheetNum<1) {
				workBook.close();
				return saleList;
			}
			sheet     =  workBook.getSheetAt(0);
			int rows    =  sheet.getPhysicalNumberOfRows();
			for (int r=1; r<rows; r++) {
				row     =  sheet.getRow(r);
				XSSFCell mcategoryCell     		= row.getCell(0);
				XSSFCell productCell       		= row.getCell(1);
				XSSFCell headCell          		= row.getCell(2);
				XSSFCell countCell         		= row.getCell(3);
				XSSFCell morecountCell     		= row.getCell(4);
				XSSFCell isaddCell         		= row.getCell(5);
				XSSFCell priceCell         		= row.getCell(6);
				XSSFCell conditionalpriceCell 	= row.getCell(7);
				XSSFCell originalpriceCell 		= row.getCell(8);
				XSSFCell sDateCell         		= row.getCell(9);
				XSSFCell eDateCell         		= row.getCell(10);
				XSSFCell descCell          		= row.getCell(11);
				if (mcategoryCell==null && productCell==null && headCell==null && countCell==null && morecountCell==null && isaddCell==null && priceCell==null && conditionalpriceCell==null && originalpriceCell==null && sDateCell==null && eDateCell==null && descCell==null) break;
				
				String mcategory			= this.getCellData(mcategoryCell);
				String category				= this.getCategory(mcategory);
				String product				= this.getCellData(productCell);
				String productIdx			= this.normalizeProductName(product);
				String head					= this.getCellData(headCell);
				String idx1					= kMAKwd.analyzeProduct(product);
				String count				= this.getCellData(countCell);
				String morecount			= this.getCellData(morecountCell);
				String isadd				= this.getCellData(isaddCell);
				String price				= this.getCellData(priceCell);
				String conditionalprice		= this.getCellData(conditionalpriceCell);
				String originalprice		= this.getCellData(originalpriceCell);
				String sDate				= this.getCellData(sDateCell, true);
				String eDate				= this.getCellData(eDateCell, true);
				String desc					= this.getCellData(descCell);
				saleList.add(new Sale(mcategory, category, product, productIdx, head, idx1, count, morecount, isadd, price, conditionalprice, originalprice, desc, sDate, eDate));
			}
			workBook.close();
			this.database.close();
			return saleList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return saleList;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String excelFile = "D:/Documents/사업 아이디어/마트엔/mart/10488_홈마트/12월29일~1월13일 공산품 행사내용.xlsx";
		
		Excel excel = new Excel();
		try{
			XSSFWorkbook workBook  =  new XSSFWorkbook(new FileInputStream(new File(excelFile)));
			XSSFSheet sheet    =  null;
			XSSFRow row     =  null;
			   
			int sheetNum    =  workBook.getNumberOfSheets();
			if (sheetNum<1) {
				workBook.close();
				return;
			}
			sheet     =  workBook.getSheetAt(0);
			int rows    =  sheet.getPhysicalNumberOfRows();
			for (int r=1; r<rows; r++) {
				row     =  sheet.getRow(r);
				XSSFCell mcategoryCell     		= row.getCell(0);
				XSSFCell productCell       		= row.getCell(1);
				XSSFCell headCell          		= row.getCell(2);
				XSSFCell countCell         		= row.getCell(3);
				XSSFCell morecountCell     		= row.getCell(4);
				XSSFCell isaddCell         		= row.getCell(5);
				XSSFCell priceCell         		= row.getCell(6);
				XSSFCell conditionalpriceCell 	= row.getCell(7);
				XSSFCell originalpriceCell 		= row.getCell(8);
				XSSFCell sDateCell         		= row.getCell(9);
				XSSFCell eDateCell         		= row.getCell(10);
				XSSFCell descCell          		= row.getCell(11);
				if (mcategoryCell==null && productCell==null && headCell==null && countCell==null && morecountCell==null && isaddCell==null && priceCell==null && conditionalpriceCell==null && originalpriceCell==null && sDateCell==null && eDateCell==null && descCell==null) break;
				
				String mcategory			= excel.getCellData(mcategoryCell);
				String category				= excel.getCategory(mcategory);
				String product				= excel.getCellData(productCell);
				String productIdx			= excel.normalizeProductName(product);
				String head					= excel.getCellData(headCell);
				String idx1					= "";
				String count				= excel.getCellData(countCell);
				String morecount			= excel.getCellData(morecountCell);
				String isadd				= excel.getCellData(isaddCell);
				String price				= excel.getCellData(priceCell);
				String conditionalprice		= excel.getCellData(conditionalpriceCell);
				String originalprice		= excel.getCellData(originalpriceCell);
				String sDate				= excel.getCellData(sDateCell, true);
				String eDate				= excel.getCellData(eDateCell, true);
				String desc					= excel.getCellData(descCell);
				System.out.println(new Sale(mcategory, category, product, productIdx, head, idx1, count, morecount, isadd, price, conditionalprice, originalprice, desc, sDate, eDate).toString());
			}
			workBook.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
