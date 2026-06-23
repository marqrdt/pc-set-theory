package com.newscores.setTheory.interfaces;

public interface ITransformation {

	/**
	 * Set the transposition value.
	 * @param transposition  The transposition to set.
	 */
	public void setTransposition( int transposition );
	
	/**
	 * Return the transposition value.
	 * @return  The transposition value;
	 */
	public int getTransposition();
	
	/**
	 * Set the inversion flag.
	 * @param inversion  Set the inversion flag to inversion.
	 */
	public void setInversion( boolean inversion );
	
	/**
	 * Return the inversion flag.
	 * @return  The inversion flag.
	 */
	public boolean isInversion();
	
	/**
	 * Set the multiplication value.
	 * @param inMult  The multiplication value to set.
	 */
	public void setMultiplication( int inMult );

	/**
	 * Return the multiplication value.
	 * @return  The multiplication value;
	 */
	public int getMultiplication();
	
	/**
	 * Return a String value representing the transformation in canonical form. Used in conjunction with
	 * the getTransformationFromString method, it can be used to return set transformations based on the
	 * canonical form T[n]M[n]I.
	 * @return String  The String representation of this. Some form of "T5M7I".
	 */
	public String toString();
	
}
