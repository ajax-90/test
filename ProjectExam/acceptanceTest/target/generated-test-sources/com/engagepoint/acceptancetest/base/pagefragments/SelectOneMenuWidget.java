package com.engagepoint.acceptancetest.base.pagefragments;

import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesSelectOneMenu;
import net.thucydides.core.annotations.ImplementedBy;

@ImplementedBy(PrimeFacesSelectOneMenu.class)
public interface SelectOneMenuWidget {
	
	public void select(String option);
	
	public String getSelected();

}
