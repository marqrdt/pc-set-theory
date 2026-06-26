package org.paulmarquardt.setTheory;

import java.util.*;
import org.paulmarquardt.setTheory.interfaces.*;

/**
 * A Contour describes a shape in space and time. It is a Compositional pattern (in the sense of the Composition Design Pattern, not Music Composition) consisting
 * of a List of Integer wrapped with several useful methods. Each element of the Contour describes a location in vertical space relative
 * to all other members. For a simple example, a Contour created from the List [0,2,4,6,8,10] (an ascending whole-tone scale) would be
 * [0, 1, 2, 3, 4, 5 ]. In a more complex example, a Contour created from the List [ 12, 10, 7, 13, 16, 15 ] would be [ 2, 1, 0, 3, 5, 4 ]. 
 * In musical space, a Contour is usually interpreted as the shape of a melody.
 * The set of members of a Contour will always include the range of the Integers between 0 and the size of the List. The ordering of this
 * range reflects the shape of the contour. It can contain duplicates where duplicate elements represent elements at the same horizontal slot (often interpreted as pitch).
 */
public class Contour extends BaseSequence implements ISequence {

	private List<Integer> members;
	private String descriptor;
	private String name;
	
	/**
	 * Create a default empty Contour.
	 */
	public Contour() {
		this.members = new ArrayList<Integer>();
		this.name = "";
		this.descriptor = "";
	}
	
	/**
	 * Create a Contour from an ISequence, mostly likely another counter.
	 * @param inSeq  The ISequence instance used to create this Contour.
	 */
	public Contour( ISequence inSeq ) {
		this( inSeq.getMembers() );		
	}

	/**
	 * Create a Contour from an Array of Integer. The relative 
	 * @param inArray  The Array of Integer used to create this Contour.
	 */
	public Contour( Integer[] inArray ) {
		this( Arrays.asList( inArray ) );		
	}

	/**
	 * Create a Contour from an List of Integer.
	 * @param inList  Create a Contour from a List of Integer.
	 */
	public Contour( List<Integer> inList ) {
		this.members = new ArrayList<Integer>();
		this.name = "";
		this.descriptor = "";
		this.createContour(inList);
	}

	/** 
	 * A private method to construct the contour members from a List of Integer. In summary, make a copy (sortedItems) of the
	 * original list (items) and sort it. Iterate through original list and append the index of each element as it appears
	 * in the sortedList to the List this.members. The List is normalized so the the minimum element is translated to 0.
	 * @param items  The items from which to exact the contour.
	 */
	private void createContour( List<Integer> items ) {
		List<Integer> sortedItems = new ArrayList<Integer>( items );
		if ( items.size() == 0 ) {
			return;
		}
		Collections.sort(sortedItems);
		/*
		 * In the previous iteration of Contour, tiems were packed together in a continuous space.
		 * In this model, there could be no "leaps". For example, a Contour created from the
		 * PitchSequence <12, 14, 15, 10, 7> would create a Contour with members <2, 3, 4, 1, 0)
		for ( Integer item : items ) {
			this.members.add( sortedItems.indexOf( item ) );
		}
		*/
		/*
		 * In the model below, the members are not packed together in the underlying List, so it
		 * is possible to have gaps. For example, a Contour created from the
		 * PitchSequence <12, 14, 15, 10, 7> would create a Contour with members <5, 7, 8, 3, 0)
		 * which excludes the elements in the List [ 1, 2, 4, 6 ]. This allows a contour to be mapped
		 * into either a chromatic space or sparse PitchSequence that does not represent a continous space.
		 * This allows, for example, mapping a chromatic sequence, like a Row, to a scale, such as
		 * a major scale, etc.
		 */
		int offset = sortedItems.get(0);
		for ( Integer item : items ) {
			this.members.add( item - offset );
		}		
	}
	
	/**
	 * Return a Contour created from this with all of its members packed into a continuous space.
	 * The returned Contour contains no gaps or "leaps", representing the a contour where all members
	 * are considered equi-distant. This can be useful in translating a Contour between Pitch spaces,
	 * i.e. chromatic to diatonic or other custom spaces.
	 * @return  A Contour created from this with all of its members packed into a continuous space.
	 */
	public Contour getAbstractContour() {
		Contour outContour = new Contour();
		List<Integer> sortedItems = new ArrayList<Integer>( this.getMembers() );	
		Collections.sort(sortedItems);
		for ( Integer item : sortedItems ) {
			outContour.addElement( sortedItems.indexOf( item ) );
		}
		return outContour;
	}

	/**
	 * Return a Contour that is a transposition of this. For each element m in this.members, the new Contour contains
	 * element n such that n = (m + transposition) % this.members.size(). In this case, elements transposed past the highest element
	 * in the the original list are wrapped (mod n) where n = this.members.size.
	 * @param transposition  The amount added to each element of this.
	 * @return  A version of this transposed by transposition.
	 */
	public Contour T(int transposition) {
		Contour outContour = new Contour();
		int index = 0;
		for ( int element : this.getMembers() ) {
			outContour.addElement( (element + transposition) );
			index++;
		}
		return outContour;
	}

	/**
	 * In a sense, the reverse of T(n). Return an IPitchSequence transposed so that the first element is start.
	 * @param start  The pitch to which the first pitch of this will be transposed.
	 * @return A copy of this transposed so that the starting pitch is start.
	 */
	public Contour transposeTo(int start) {
		// TODO Auto-generated method stub
		if ( this.length() == 0 ) {
			return this;
		}
		return this.T( start - this.getMembers().get(0) );
	}

	/**
	 * Return a Contour that is the retrograde of this of this. 
	 * @return  An order-reversed version of this.
	 */
	public Contour R() {
		// TODO Auto-generated method stub
		List<Integer> reverseList = new ArrayList<Integer>( this.members );
		Collections.reverse(reverseList);
		return new Contour( reverseList );
	}

	/**
	* Return a Contour inverted around its central element(s). Essentially an upside-down version of this.
	* @return  A copy of this flipped around its axis.
	*/
	public Contour I() {
		Contour outContour = new Contour();
		int index = 0;
		for ( int element : this.getMembers() ) {
			outContour.addElement( this.getMembers().size() - this.getMembers().get(index) - 1 );
			index++;
		}
		return outContour;
	}

	/**
	* Return a Contour with each member multiplied by mult takem modulus the length of the Contour.
	* @return  A copy of this flipped around its axis.
	*/
	public Contour M(int mult) {
		Contour outContour = new Contour();
		int index = 0;
		for ( int element : this.getMembers() ) {
			outContour.addElement( (this.getMembers().get(index) * mult) % this.getMembers().size() );
			index++;
		}
		return outContour;
	}

	/**
	 * Add the members of anotherSequence at the end of this.  This alters the content of this.
	 * @param anotherSequence The ISequence to append to this.
	*/
	public void extend(ISequence anotherSequence) {
		// TODO Auto-generated method stub
		this.members.addAll( new Contour( anotherSequence.getMembers() ).getMembers() );
	}

	/**
	 * Append a single element to this.
	 * @param element  The element to append to this.
	 */
	public void addElement( Integer element ) {
		this.members.add( element );
	}
	
	/**
	 * Returns a subsequence of this using a start index and a length.
	 * Throws an IllegalArgumentException if start index is less than 0 or the subsequence extends beyond the range of this objects List members.
	 * @param start  The index at which to start the search.
	 * @param length  The length at which to search.
	 * @return A sub-sequence of length length starting at the start index.
	 */
	public Contour subSequence(int start, int length) {
		Contour outContour = new Contour();
		if ( start < 0 || length < 0) {
			throw new IllegalArgumentException("start and length values must be > 0.");
		}
		if ( start + length > this.members.size() ) {
			throw new IllegalArgumentException("end index must be less than the length of the list.");
		}
		int index = start;
		while ( index < (start + length) ) {
			outContour.addElement( this.members.get(index));
			index++;
		}
		return outContour;
	}

	/**
	 * Returns a subsequence of this sequence using an array of indices. Throws an IllegalArgumentException if the
	 * array contains a negative number or a number larger than the size of this objects List members.
	 * @param indices Integer[]  The indices to determine membership in the returned set.
	 * @return ISequence  An ISequence containing the members of this specified by indices.
	 * @throws IllegalArgumentException  Throws IllegalArgumentException if the array contains a negative index or
	 * an index greater than the size of this.
	 */
	public Contour subSequence( Integer[] indices ) {
		Contour outContour = new Contour();
		int max = Integer.MIN_VALUE;
		for ( int index = 0; index < indices.length; index++ ) {
			if ( indices[index] >= max ) {
				max = indices[index];
			}
			if ( indices[index] < 0 ) {
				throw new IllegalArgumentException("Passed in array may not contain negative index.");
			}
		}
		if ( max >= this.length() || max < 0 ) {
			throw new IllegalArgumentException("Passed in array contains an index not contained in this Contour.");
		}
		for ( int index = 0; index < indices.length; index++ ) {
			outContour.addElement( this.getMembers().get(index) );
		}
		return outContour;
	}

	/**
	 * Returns a subsequence of this sequence using a List of Integer. Throws an IllegalArgumentException if the
	 * List contains a negative number or a number larger than the size of this objects List members.
	 * @param inCollection  AN ICollection on which to map members of this Contour..
	 * @return  An ISequence containing the members of this specified by indices.
	 * @throws IllegalArgumentException  Throws IllegalArgumentException if the List contains a negative index or
	 * an index greater than the size of this.
	 */
	public PitchSequence applyPitchCollection( IPitchCollection inCollection ) {
		int maxElement = Collections.max( this.getMembers() );
		if ( Collections.max( this.getMembers() ) > inCollection.getMembers().size() ) {
			throw new IllegalArgumentException( String.format("Cannot create a PitchSeqeunce from an IPitchCollection of size %d using a Contour of span %d", inCollection.getMembers().size(), maxElement) );
		}
		PitchSequence outSeq = new PitchSequence();
		for ( int member : this.getMembers() ) {
			outSeq.addPitch( inCollection.getMembers().get( member ));
		}
		return outSeq;
	}
	
	/**
	 * Returns a subsequence of this sequence using a List of Integer. Throws an IllegalArgumentException if the
	 * List contains a negative number or a number larger than the size of this objects List members.
	 * @param indices  A List of Integer of indices to determine membership in the returned set.
	 * @return  An ISequence containing the members of this specified by indices.
	 * @throws IllegalArgumentException  Throws IllegalArgumentException if the List contains a negative index or
	 * an index greater than the size of this.
	 */
	public Contour subSequence( List<Integer> indices ) {
		return this.subSequence( indices.toArray( new Integer[0] ) );
	}

	/**
	 * Return the length of the members of this.
	 * @return  The length of the members of this.
	 */
	public int length() {
		// TODO Auto-generated method stub
		return this.members.size();
	}

	/**
	 * Return a List of Integer comprising the members of this.
	 */
	public List<Integer> getMembers() {
		// TODO Auto-generated method stub
		return this.members;
	}

	/**
	 * Returns whether this is equal to another ISequence. Returns true this and anotherSequence are member-wise equal, i.e.
	 * every member of this is equal the member of anotherSequence at the same index.
	 * @return  True if this 
	 */
	public boolean equals(ISequence anotherSequence) {
		// TODO Auto-generated method stub
		return this.getMembers().equals( anotherSequence.getMembers() );
	}

	/**
	 * getEmbeddedSubsequence accepts an IPitchSequence inSeq. If the caller contains inSeq as an embedded subsequence, the method will
	 * return the indices in the caller's members that form inSeq. Example:
	 * IPitchSequence newSeq = new PitchClassSequence( [0,b,1,2,a,8,5,3,9,4,7,6] )
	 * IPitchSequence subSeq = new PitchClassSequence( [b,2,8,3] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = List 1,3,5,7
	 * IPitchSequence anotherSubSeq = new PitchClassSequence( [0,4,7,6] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = List 0,9,10,11
	 * 
	 * If the caller does not contain the subSequence, it will return null
	 * IPitchSequence notASubSeq = new PitchClassSequence( [6,4,3,8] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = null
	 *  
	 * @param anotherPitchSequence  The IPitchSequence used for the operation.
	 * @return  A list of indices at which members of anotherPitchSequence can be found in this.
	 */
	public List<Integer> getEmbeddedSubsequence( ISequence anotherPitchSequence ) {
		List<Integer> indices = new ArrayList<Integer>();
		int counter = 0;
		for ( int index = 0; index < this.length(); index++ ) {
			int member = this.getMembers().get(index);
			if ( anotherPitchSequence.getMembers().get(counter) == member ) {
				indices.add( index );
				counter++;
			}
		}
		// If all elements in inSeq were found in this objects members.
		if ( indices.size() == anotherPitchSequence.length() ) {
			return indices;
		}
		return null;
	}

	/**
	 * Returns a new Contour with inSeq appended to this.
	 * This method is created to support the "+" operator in a Groovy DSL.
	 * @param  inSeq The PitchSequence to be appended to this.
	 * @return  A new PitchSequence with inSeq appended to this.
	 */
	public Contour plus( ISequence inSeq ) {
		Contour cont = new Contour( this );
		cont.extend(inSeq);
		return cont;
	}

	/**
	 * Returns a String representing this contour.
	 * @return The string representation of this.
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append( String.format("Contour %s:[ ",this.getDescriptor() ) );
		Iterator<Integer> memberIt = this.getMembers().iterator();
		while ( memberIt.hasNext() ) {
			buf.append( memberIt.next() );
			if ( memberIt.hasNext() ) {
				buf.append( ", " );
			}
		}
		buf.append(" ]");
		return buf.toString();
	}

	/**
	 * Sets a text name for this. Could be something like "My favorite sequence of pitches" or "Berg Violin Concerto row".
	 * @param name  The descriptor to set.
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * Returns the text name for this.
	 * @return String  The text name for this.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets a text descriptor for this. Could be something like "My favorite contour" or "Happy Birthday contour".
	 * @param descriptor  The text descriptor to set.
	 */
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Returns the text descriptor for this.
	 * @return String  The text descriptor for this.
	 */
	public String getDescriptor() {
		// TODO Auto-generated method stub
		return this.descriptor;
	}

	@Override
	public void addPitch(Integer pitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSequenceAtIndex(ISequence inSeq, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSequence(ISequence inSeq) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
