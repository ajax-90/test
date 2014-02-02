package com.engagepoint.acceptancetest.base.pagefragments;

import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesBasicCalendar;
import net.thucydides.core.annotations.ImplementedBy;

@ImplementedBy(PrimeFacesBasicCalendar.class)
public interface CalendarWidget {
	
	public void chooseDate(String date);

	public void verifyThatDate(String expectedDate);

	public void useDatePickerForChooseDate(String date);

}
