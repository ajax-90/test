package com.engagepoint.acceptancetest.base.pagefragments;

import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesConfirmDialog;
import net.thucydides.core.annotations.ImplementedBy;

@ImplementedBy(PrimeFacesConfirmDialog.class)
public interface ModalWidget {
	
	public void close();
	
	public void clickOn(String buttonText);
	
	public String getContentText();

	public boolean isVisible();

}
