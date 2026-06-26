package org.paulmarquardt.setTheory.interfaces;

import java.util.List;
import com.fasterxml.jackson.annotation.*;


public interface IPitchCollection {

	/**
	 * Return a List containing the members of this.
	 * @return  The members of this.
	 */
	@JsonGetter
	public List<Integer> getMembers();

}
