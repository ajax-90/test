package com.engagepoint.acceptancetest.base.pagefragments;

import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesRadioButtonGroup;
import net.thucydides.core.annotations.ImplementedBy;

import java.util.List;

@ImplementedBy(PrimeFacesRadioButtonGroup.class)
public interface RadioButtonGroupWidget {

	public void select(String option);

	public List<String> getAllRadioBtnLabels();

	public boolean isSelected(String label);

}
