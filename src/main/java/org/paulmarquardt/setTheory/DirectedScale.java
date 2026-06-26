package org.paulmarquardt.setTheory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.paulmarquardt.setTheory.interfaces.IPitchSet;

/**
 * The Scale class extends the PitchSet class by offering a different set of constructors that use a set
 * of intervals rather than the set of pitchclass members. Though the superclass is PitchSet, the Scale
 * extends the PitchSet model to include the concept of order, which is intentionally omitted from the
 * PitchSet. It also offers the ability to create ascending and descending forms. Several utility methods offer
 * the capability to return PitchSequences based on slices of the scale.
 * Several common scales can be constructed using a String constructor instead of an array of intervals.
 * @author Paul Marquardt
 */
public class DirectedScale extends Scale {

	public static final int MODULUS = 12;
	private ArrayList<Integer> members;
	private ArrayList<Integer> ascending;
	private ArrayList<Integer> descending;	
	private String name;
	
	/**
	 * The default constructor, creating an empty Scale.
	 */
	public DirectedScale() {
		this.members = new ArrayList<Integer>();
		this.ascending = members;
		this.descending = members;
	}

	/**
	 * Constructor creating a Scale from an array of Integers representing intervals between members.
	 * @param ascendingArray  The Array of Integer from which the ascending scale will be constructed.
	 * @param descendingArray  The Array of Integer from which the descending scale will be constructed.
	 * @param lowestNote  The lowest note of the scale.
	 * @param highestNote  The highest note of the scale.
	 */
	public DirectedScale(Integer[] ascendingArray, Integer[] descendingArray, int lowestNote, int highestNote ) {
		// TODO Auto-generated constructor stub
		if ( lowestNote > highestNote ) {
			throw new IllegalArgumentException("The value of lowestNote must be less than the value of highestNote.");
		}
		for ( int interval : ascendingArray ) {
			if ( interval < 1 ) {
				throw new IllegalArgumentException("A Scale cannot have intervals less than 1.");
			}
		}
		for ( int interval : descendingArray ) {
			if ( interval < 1 ) {
				throw new IllegalArgumentException("A Scale cannot have intervals less than 1.");
			}
		}
		this.members = new ArrayList<Integer>();
		int count = 0;
		int noteToAdd = lowestNote;
		this.members.add( noteToAdd );
		if ( ascendingArray.length == 0 ) {
			this.setName("");
			return;
		}
		while ( noteToAdd + ascendingArray[count] <= highestNote ) {
			noteToAdd =+ ascendingArray[count]; 
			this.members.add(noteToAdd);
			this.ascending.add(noteToAdd);
			count++;
			if ( count == ascendingArray.length ) {
				count = 0;
			}
		}
		if ( descendingArray.length == 0 ) {
			this.setName("");
			return;
		}
		count = 0;
		noteToAdd = members.get( members.size() - 1 );
		while ( noteToAdd - descendingArray[count] >= highestNote ) {
			noteToAdd =+ ascendingArray[count]; 
			this.members.add(noteToAdd);
			this.descending.add(noteToAdd);
			count++;
			if ( count == descendingArray.length ) {
				count = 0;
			}
		}
		this.setName("");
	}

	public Scale getAscending() {
		Scale out = new Scale();
		for ( int member : this.ascending ) {
			out.addPitch(member);
		}
		return out;
	}

	public Scale getDescending() {
		Scale out = new Scale();
		for ( int member : this.descending ) {
			out.addPitch(member);
		}
		return out;
	}
}
