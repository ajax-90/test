package com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.engagepoint.acceptancetest.base.pagefragments.DataTableWithSubtable;
import com.engagepoint.acceptancetest.base.pagefragments.tables.CellWebElement;
import com.engagepoint.acceptancetest.base.pagefragments.tables.ColumnWebElement;
import com.engagepoint.acceptancetest.base.pagefragments.tables.RowWebElement;
import com.engagepoint.acceptancetest.base.pagefragments.tables.TableWebElement;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import net.thucydides.core.pages.WebElementFacadeImpl;

public class PrimeFacesDataTableWithSubtable extends WebElementFacadeImpl implements DataTableWithSubtable {

	private TableWebElement table;
	private WebElement tableElement;

	public PrimeFacesDataTableWithSubtable(WebDriver driver,
			WebElement webElement, long timeoutInMilliseconds) {
		super(driver, webElement, timeoutInMilliseconds);
		this.tableElement = webElement;
		parseTable();
	}

	public WebElement findRowWithParameters(Map<String, String> rowToFind) {
		List<RowWebElement> matchedRows = Arrays.asList(table.getRows());
		return findRowWithParametrsIn(matchedRows, rowToFind);
	}

	private WebElement findRowWithParametrsIn(List<RowWebElement> matchedRows, 
			Map<String, String> rowToFind) {
		for(RowWebElement rowCandidate : matchedRows){
			Map<String, String> cellsCandidates = cellsFromRow(rowCandidate);
   			MapDifference<String, String> diff = Maps.difference(cellsCandidates, rowToFind);
   			if(diff.entriesInCommon().isEmpty()){
   				continue;
   			} else if(diff.areEqual()){
   				return rowCandidate.getRow();
   			} else if(diff.entriesOnlyOnRight().isEmpty() && diff.entriesDiffering().isEmpty()){
   				return rowCandidate.getRow();
   			} else if(rowCandidate.hasSubRows()){
   				for(String commonkey : diff.entriesInCommon().keySet()){
   					rowToFind.remove(commonkey);
   				}
   				return findRowWithParametrsIn(getSubRowsFor(rowCandidate), diff.entriesOnlyOnRight().isEmpty() ? rowToFind : diff.entriesOnlyOnRight());
   			}
   		}
   		throw new AssertionError("You should define at list one existing record value");
	}
	
	private List<RowWebElement> getSubRowsFor(RowWebElement rowCandidate) {
		int rowIndex = rowCandidate.getRowIndex();
		List<RowWebElement> subRows = Lists.newArrayList();
		for(RowWebElement row : table.getRows()){
			if(row.getParentRowIndex() == rowIndex){
				subRows.add(row);
			}
		}
		return subRows;
	}

	private Map<String, String> cellsFromRow(RowWebElement rowCandidate) {
		Map<String, String> cells = Maps.newHashMap();
		int rowIndex = rowCandidate.getRowIndex();
		ColumnWebElement[] columns = table.getColumns();
		CellWebElement[][] cellsTable = table.getTable();
		for(int j=0; j < columns.length; j++){
			cells.put(columns[j].getColumnKey(), cellsTable[rowIndex][j].getCellAsString());
		}
		return cells;
	}

	private List<WebElement> headingElements() {
		return tableElement.findElements(By.xpath(".//th[@role='columnheader' and not(@colspan)]"));
	}
	
	private List<WebElement> rowElements() {
		return tableElement.findElements(By.xpath(".//tr[td]"));
	}
	
	private boolean isSubRowElement(WebElement row) {
		return row.findElements(By.xpath(".//td[@class='ui-datatable-subtable-header']")).isEmpty();
	}
	
	private List<WebElement> getCellsFor(WebElement row){
		return row.findElements(By.xpath("./td"));
	}

	private void parseTable() {
		table = new TableWebElement();
		table.setColumns(parseColumns());
		table.setRows(parseRows());
		RowWebElement[] rows = table.getRows();
		ColumnWebElement[] columns = table.getColumns();
		CellWebElement[][] cells = new CellWebElement[rows.length][columns.length];
		for(int i=0; i < rows.length; i++){
			List<WebElement> cellsInRow = getCellsFor(rows[i].getRow());
			for(int j=0; j < columns.length; j++){
				cells[i][j] = new CellWebElement();
				WebElement cell = j >= size(cellsInRow) ? cellsInRow.get(size(cellsInRow) - 1) : cellsInRow.get(j);
				cells[i][j].setCell(cell);
				cells[i][j].setCellAsString(cell.getText());
				cells[i][j].setRowIndex(i);
				cells[i][j].setColumnIndex(j);
			}
		}
		table.setTable(cells);
	}

	private int size(List<WebElement> cellsInRow) {
		return cellsInRow.size();
	}

	private RowWebElement[] parseRows() {
		List<WebElement> rowsElements = rowElements();
		RowWebElement[] rows = new RowWebElement[size(rowsElements)];
		int rowIndex = 0;
		int parentRowIndex = -1;
		boolean workWithSubRows = false;
		for(WebElement row : rowsElements){
			rows[rowIndex] = new RowWebElement();
			rows[rowIndex].setRow(row);
			rows[rowIndex].setRowKey(row.getText());
			rows[rowIndex].setRowIndex(rowIndex);
			rows[rowIndex].setParentRowIndex(parentRowIndex);
			if(isSubRowElement(row)){
				if(!workWithSubRows){
					workWithSubRows = true;
					parentRowIndex = rowIndex !=0 ? rowIndex - 1 : rowIndex;
					rows[parentRowIndex].hasSubRows(true);
				}
			 	rows[rowIndex].setSubRow(true);
			} else if(!isSubRowElement(row) && workWithSubRows){
				workWithSubRows = false;
				parentRowIndex = -1;
			}
			rowIndex++;
		}
		return rows;
	}

	private ColumnWebElement[] parseColumns() {
		List<WebElement> headings = headingElements();
		ColumnWebElement[] columns = new ColumnWebElement[size(headings)];
		int columnIndex = 0;
		for(WebElement heading : headings){
			columns[columnIndex] = new ColumnWebElement();
			columns[columnIndex].setColumn(heading);
			columns[columnIndex].setColumnKey(heading.getText());
			columns[columnIndex].setColumnIndex(columnIndex);
			columnIndex++;
		}
		return columns;
	}

}
