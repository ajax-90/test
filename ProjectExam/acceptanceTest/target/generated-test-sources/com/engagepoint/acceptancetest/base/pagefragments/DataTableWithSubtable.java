package com.engagepoint.acceptancetest.base.pagefragments;

import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesDataTableWithSubtable;
import net.thucydides.core.annotations.ImplementedBy;
import org.openqa.selenium.WebElement;

import java.util.Map;

@ImplementedBy(PrimeFacesDataTableWithSubtable.class)
public interface DataTableWithSubtable {

	public WebElement findRowWithParameters(Map<String, String> rowToFind);
}
