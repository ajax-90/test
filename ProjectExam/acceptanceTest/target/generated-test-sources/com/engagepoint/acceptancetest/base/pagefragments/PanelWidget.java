package com.engagepoint.acceptancetest.base.pagefragments;

import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesPanel;
import net.thucydides.core.annotations.ImplementedBy;

@ImplementedBy(PrimeFacesPanel.class)
public interface PanelWidget {
	
	public void close();
	
	public void toggle();
	
	public void chooseOption(String option);
	
	public boolean isContentVisible();
	
}
