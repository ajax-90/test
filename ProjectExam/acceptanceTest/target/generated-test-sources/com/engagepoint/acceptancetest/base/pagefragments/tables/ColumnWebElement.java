package com.engagepoint.acceptancetest.base.pagefragments.tables;

import org.openqa.selenium.WebElement;

public class ColumnWebElement {
	
	private WebElement column;
	private String columnKey;
	private int columnIndex;

	public ColumnWebElement() {
	}

	public WebElement getColumn() {
		return column;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumn(WebElement column) {
		this.column = column;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

}