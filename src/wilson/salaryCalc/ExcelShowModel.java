package wilson.salaryCalc;

public class ExcelShowModel {
	public int rows = 0 ;
	public int colc = 0 ; 
	String[][] content= {};
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getColc() {
		return colc;
	}
	public void setColc(int colc) {
		this.colc = colc;
	}
	public String[][] getContent() {
		return content;
	}
	public void setContent(String[][] content) {
		this.content = content;
	}
}
