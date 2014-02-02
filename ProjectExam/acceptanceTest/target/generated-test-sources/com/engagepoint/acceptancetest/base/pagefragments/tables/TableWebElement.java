package com.engagepoint.acceptancetest.base.pagefragments.tables;

import org.openqa.selenium.WebElement;

public class TableWebElement {

	private WebElement tableElement;
	private CellWebElement[][] table;
	private RowWebElement[] rows;
	private ColumnWebElement[] columns;

	public WebElement getTableElement() {
		return tableElement;
	}

	public CellWebElement[][] getTable() {
		return table.clone();
	}

	public RowWebElement[] getRows() {
		return rows.clone();
	}

	public ColumnWebElement[] getColumns() {
		return columns.clone();
	}

	public void setTableElement(WebElement tableElement) {
		this.tableElement = tableElement;
	}

	public void setTable(CellWebElement[][] table) {
		this.table = table.clone();
	}

	public void setRows(RowWebElement... rows) {
		this.rows = rows.clone();
	}

	public void setColumns(ColumnWebElement... columns) {
		this.columns = columns.clone();
	}

}
