package com.engagepoint.acceptancetest.base.pagefragments;

import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesCheckBox;
import net.thucydides.core.annotations.ImplementedBy;

@ImplementedBy(PrimeFacesCheckBox.class)
public interface CheckBoxWidget {
	
	public boolean isChecked();
	
	public void click();

}
