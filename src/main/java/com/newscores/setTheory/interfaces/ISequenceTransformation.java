package com.newscores.setTheory.interfaces;


/**
 * The ISequenceTransformation interfaces defines the contract for transforming a sequence, adding a boolean flag indicating
 * whether the transformation is a retrograde.
 * @author marqrdt
 *
 */
public interface ISequenceTransformation extends ITransformation {

	/**
	 * Indicated whether the transformation is a retrograde.
	 * @return  The boolean flag indicating whether this is a retrograde.
	 */
	public boolean isRetrograde();
	
	/**
	 * Set the retrograde flag on the transformation.
	 * @param inRetrograde  A boolean used to indicate whether this is a retrograde.
	 */
	public void setRetrograde( boolean inRetrograde );
}
