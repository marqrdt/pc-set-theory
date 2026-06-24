package com.newscores.setTheory;

import com.newscores.setTheory.interfaces.*;
import com.newscores.setTheory.*;
import com.fasterxml.jackson.annotation.*;

/**
 * A CMSegment is a type of IPitchSequence that exists within a CompositionMatrix. As a member of a CompositionMatrix,
 * it has the added dimensions of vertical and horizontal position, described as indices, such that two CMSegments with 
 * the same Column index can be said to be coexist in the same vertical position, often interpreted as time, and two
 * CMSegments in the same Row index can be said to occupy the same linear element, often interpreted as register or
 * instrumentation. A CMSegment can be created and exist outside the context of a CompositionMatrix.
*/
@JsonPropertyOrder({ "name", "columnIndex", "rowIndex", "pitchSeq", "transformation", "descriptor" })
public class CMSegment implements Comparable<CMSegment> {
	private int rowIndex, columnIndex;
	private ISequence pitchSeq;
	private String descriptor;
	private String name;
	
	/**
	 * Create a empty CMSegment with at row:column coordinates 0,0.
	 */
	public CMSegment() {
		// cast seq to a PitchSequence, since CM elements work in PitchClass space.
		//this.pitchSeq =  new PitchSequence( pseq );
		this.pitchSeq =  SetTheoryFactories.getPitchClassSequence();
		this.setDescriptor( "" );
		this.setName( "Empty CMSegment" );
		//System.out.println("Inside CMSegment constructor, descriptor is " + this.getDescriptor());
		this.rowIndex = 0;
		this.columnIndex = 0;
	}

	/**
	 * Create a CMSegment with content pseq at row irow, column icol.
	 * @param pseq The IPitchSequence this CMSegment represents.
	 * @param irow The row index at which to insert it.
	 * @param icolumn The column index at which to insert it.
	 */
	public CMSegment( ISequence pseq, int irow, int icolumn ) {
		// cast seq to a PitchSequence, since CM elements work in PitchClass space.
		//this.pitchSeq =  new PitchSequence( pseq );
		this.pitchSeq =  pseq;
		this.setDescriptor( pseq.getDescriptor() );
		this.setName( pseq.getName() );
		//System.out.println("Inside CMSegment constructor, descriptor is " + this.getDescriptor());
		this.rowIndex = irow;
		this.columnIndex = icolumn;
	}

	/**
	 * Returns the row index of this.
	 * @return int  The row index of this.
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * Sets the row index of this.
	 * @param rowIndex int  The Row index to set.
	 */
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * Return the column index of this.
	 * @return int  The column index of this.
	 */
	public int getColumnIndex() {
		return columnIndex;
	}
	
	/**
	 * Set the column index of this.
	 * @param columnIndex  The column index of this.
	 */
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	/**
	 * Return the IPitchSequence of this. Could return null.
	 * @return IPitchSequence  The IPitchSequence this CMSegment represents.
	 */
	public ISequence getPitchSequence() {
		return pitchSeq;
	}
	
	/**
	 * Set the IPitchSequence of this.
	 * @param pitchSeq  The IPitchSequence to set.
	 */
	public void setPitchSequence(IMutableSequence pitchSeq) {
		this.pitchSeq = pitchSeq;
	}

	/**
	 * This method is required under the Comparable contract, enabling the equals() method
	 * on objects of this class. It returns whether cmSeg occurs before, after or in the same position as this.
	 * @param cmSeg  The CMSegment for comparison.
	 * @return  The result of the comparison. -1 if cmSeg occurs in an earlier row or column position.
	 */
	public int compareTo(CMSegment cmSeg) {
		// TODO Auto-generated method stub
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
		if ( cmSeg.getRowIndex() < this.getRowIndex() ) {
			return BEFORE;
		}
		if ( cmSeg.getRowIndex() > this.getRowIndex() ) {
			return AFTER;
		}
		if ( cmSeg.getColumnIndex() < this.getColumnIndex() ) {
			return BEFORE;
		}
		if ( cmSeg.getColumnIndex() > this.getColumnIndex() ) {
			return AFTER;
		}
		return EQUAL;
	}
	
	/**
	 * Whether this is equal to anotherCMSeg. Equality defined as the result of the equals() method
	 * of the contained IPitchSequence.
	 * @param anotherCMSeg  The CMSegment to compare to.
	 * @return boolean  The result of the comparison of the contained IPitchSequence.
	 */
	public boolean equals( CMSegment anotherCMSeg ) {
		if ( this.getPitchSequence().equals(anotherCMSeg.getPitchSequence() ) ) {
			return true;
		}
		return false;
	}
	
	/**
	 * Set a String descriptor for this. Can be used for naming CMs in output, etc.
	 * @param desc The descriptor to set.
	 */
	public void setDescriptor( String desc ) {
		this.descriptor = desc;
	}

	/**
	 * Returns the name of this.
	 * @return String  Returns the name of this.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set a String name for this.
	 * @param name The descriptor to set.
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * Returns the descriptor of this.
	 * @return String  Returns the descriptor of this.
	 */
	public String getDescriptor() {
		return this.descriptor;
	}
}
