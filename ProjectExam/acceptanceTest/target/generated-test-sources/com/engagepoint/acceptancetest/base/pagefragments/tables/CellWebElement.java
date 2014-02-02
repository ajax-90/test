package com.engagepoint.acceptancetest.base.pagefragments.tables;

import org.openqa.selenium.WebElement;

public class CellWebElement {
	
	private WebElement cell;
	private String cellAsString = "";
	private int columnIndex;
	private int rowIndex;

	public CellWebElement() {
	}

	public WebElement getCell() {
		return cell;
	}

	public String getCellAsString() {
		return cellAsString;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setCell(WebElement cell) {
		this.cell = cell;
	}

	public void setCellAsString(String cellAsString) {
		this.cellAsString = cellAsString;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

}