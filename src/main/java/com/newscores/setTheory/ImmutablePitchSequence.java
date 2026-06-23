 package com.newscores.setTheory;

import java.util.*;
import java.util.regex.*;

import com.newscores.setTheory.interfaces.*;

public class ImmutablePitchSequence implements ISequence, Iterable {
	List<Integer> pitches;
	String descriptor;
	Map<String,Integer> history;
	PitchClassSequenceTransformation transformation;
	String name;
	
	/**
	 * Create a default empty PitchSequence.
	 * A PitchSequence is defined as an ordered list of pitches represented as Integers.
	 */
	public ImmutablePitchSequence() {
		this.pitches = new ArrayList<Integer>();
		this.name = "";
		this.descriptor = "P";
		this.transformation = new PitchClassSequenceTransformation();
		//this( new Integer[0] );
	}
	
	/**
	 * Create a PitchSequence from a String containing Integer tokens separated by a comma and a space ", ".
	 * Example: 12, 43, 73, 9, 34, 2, 121. Extraneous characters are ignored.
	 * A PitchSequence is defined as an ordered list of pitches represented as Integers.
	 * @param pitchString String  The String containing pitch tokens as Integers.
	 */
	public ImmutablePitchSequence( String pitchString ) {
		this( PitchSequence.getPitchSequenceFromString( pitchString ) );
	}

	// call main constructor with List from Integer[] array
	/**
	 * Create a PitchSequence from another implementation of the IPitchSequence interface.
	 * @param pitchSeq An implementation of the IPitchSequence interface.
	 */
	public ImmutablePitchSequence( ISequence pitchSeq ) {
		pitches = new ArrayList<Integer>();
		this.name = "";
		this.descriptor = pitchSeq.getDescriptor();
		this.transformation = new PitchClassSequenceTransformation();
		for ( Integer i : pitchSeq.getMembers() ) {
			this.pitches.add( i );
		}
	}

	// call main constructor with List from Integer[] array
	/**
	 * Create a PitchSequence from an array of Integer.
	 * @param pitchArray  The array of Integers from which to construct the new PitchSequence.
	 */
	public ImmutablePitchSequence( Integer[] pitchArray ) {
		this( Arrays.asList( pitchArray ) );
	}
	
	// This is the main constructor. Others should call this.
	/**
	 * Create a PitchSequence from an Collection of Integer.
	 * @param pitchesAsList  The pitches of this as a List.
	 */
	public ImmutablePitchSequence( Collection<Integer> pitchesAsList ) {
		this.descriptor = new String("P");
		pitches = new ArrayList<Integer>();
		this.name = "";
		//System.out.println( String.format("getMembers in %s = %s", this.getClass().getName(), this.getMembers() ) );
		this.transformation = new PitchClassSequenceTransformation();
		for ( Integer i : pitchesAsList ) {
			this.pitches.add( i );
		}
	}

	/**
	 * Constructor creating a PitchSet from an ISequence type.
	 * @param objectType  The object returned is created based on the object type using its getMembers() method.
	 * @return Return a IPitchSet created from the members of this.
	 */
	public IPitchSet asType( Class objectType ) {
		// TODO Auto-generated constructor stub
		if ( objectType.equals( PitchClassSet.class ) ) {
			return new PitchClassSet( this.getMembers() );
		}
		else if ( objectType.equals( PitchSet.class ) ) {
			return new PitchSet( this.getMembers() );
		}
		else {
			throw new IllegalArgumentException( "PitchSequence may only be converted to a PitchSet or a PitchClassSet");
		}
	}

	/**
	 * Return the length of this. This method is added as a synonym for size().
	 * @return  The length of the member pitches of this.
	 */
	public int length() {
		return this.getMembers().size();
	}
	
	/**
	 * Return the size of this. A simple wrapper on the size of the enclosed List&lt;Integer&gt; pitches.
	 * @return An int with the size of this.
	 */
    public int size() {
        // TODO Auto-generated method stub
        return this.getMembers().size();
    }

    /**
	 * Return an Iterator that will cycle through members of this.
	 * @return Iterator  An Iterator that will cycle through the member pitches of this.
	 */
	public Iterator<Integer> iterator() {
		// TODO Auto-generated method stub
		return this.getMembers().iterator();
	}
	
	/**
	 * Return a new IPitchSequence transposed by transposition, this is, for each element n in this, n + transposition.
	 * @param transposition  The transposition level.
	 * @return A copy of this transposed by transposition.
	 */
	public ImmutablePitchSequence T( int transposition ) {
		PitchSequence outSeq = new PitchSequence();
		for ( Integer elem : this.pitches ) {
			outSeq.addPitch( elem + transposition );
		}
		//System.out.println( String.format("Setting T history to %s", (this.history.get("T") + transposition) % 12 ) );
		return new ImmutablePitchSequence(outSeq);
	}

	/**
	 * In a sense, the reverse of T(n). Return an IPitchSequence transposed so that the first element is start.
	 * @param start  The pitch to which the first pitch of this will be transposed.
	 * @return A copy of this transposed so that the starting pitch is start.
	 */
	public ImmutablePitchSequence transposeTo(int start) {
		ImmutablePitchSequence outSeq = new ImmutablePitchSequence( this.getMembers() );
		if ( this.pitches.size() == 0 ) {
			return outSeq;
		}
		return outSeq.T( start - this.getMembers().get(0) );
	}
 
	/**
	 * Return the retrograde of this.
	 * @return  An order-reversed version of this.
	 */
	public ImmutablePitchSequence R() {
		PitchSequence outSeq = new PitchSequence();
		List<Integer> reverseList = new ArrayList<Integer>();
		for ( int index = this.pitches.size() - 1; index >= 0; index-- ) {
			outSeq.addPitch( this.pitches.get( index ) );
		}
		return new ImmutablePitchSequence(outSeq);
	}

	/**
	 * Return the a new PitchClassSequence created by taking the member at each index (m * n) mod 12
	 * of this for each n in 0,this.length().
	 * If index == 1, it will return the original set.
	 * If index == this.length() - 1, it will return the retrograde set.
	 * If index == 5, it will return a set created by taking every 5th element (mod this.length()) of this.
	 * If index == 1, it will return the original set.
	 * If index and this.length() are not relatively prime, the returned set will contain duplicates.
	 * @param index  The pitch to which the first pitch of this will be transposed.
	 * @return  An order-reversed version of this.
	 */
	public ImmutablePitchSequence O(int index) {
		PitchSequence outSeq = new PitchSequence();
		List<Integer> reverseList = new ArrayList<Integer>();
		int currentIndex = 0;
		for ( int i = 0; i <= this.pitches.size() - 1; i++ ) {
			outSeq.addPitch( this.pitches.get( (currentIndex) % this.pitches.size() ) );
			currentIndex += index;
		}
		return new ImmutablePitchSequence(outSeq);
	}

	/**
	 * Return the M(mult) of this, that is, n * mult applied to every n element of this.
	 * Possibly of limited use in conventional musical interpretation with a limited gamut of pitches.
	 * @param mult  An int by which all pitches in this will be multiplied.
	 * @return  The M[n] transformation of this, i.e. all members will be multiplied by mult.
	 * 
	 */
	public ImmutablePitchSequence M(int mult) {
		PitchSequence outSeq = new PitchSequence();
		for ( Integer elem : this.pitches ) {
			outSeq.addPitch( elem * mult );
		}
		return new ImmutablePitchSequence(outSeq);
	}

	/**
	 * Return the M(mult, modulus) of this, that is, (n * mult) % modulus applied to every n element of this.
	 * This offers subclasses a useful interface for mod n spaces (PC sets and sequences, etc)
	 * @param mult  An int by which all pitches in this will be multiplied.
	 * @param modulus All members of the returned set will be reduced to n mod modulus.
	 * @return  The M[n] transformation of this, i.e. all members will be multiplied by mult and reduced by mod modulus.
	 * 
	 */
	public ImmutablePitchSequence M(int mult, int modulus) {
		PitchSequence outSeq = new PitchSequence();
		for ( Integer elem : this.pitches ) {
			outSeq.addPitch( PitchClassSet.mod(elem * mult, modulus) );
		}
		return new ImmutablePitchSequence(outSeq);
	}

	/**
	* The I() operator has a slightly different meaning than in PitchClassSet.
	* Here, it will return
	* a PitchSet inverted around its minimum and maximum elements. As a result, the minimum and maximum elements
	* will always be members of the returned set. This makes it more useful
	* compositionally, since the returned set will be in the same range as the
	* original instead of being flipped around 0.
	* @return  A copy of this flipped around its axis so that the highest and lowest pitches remain constant
	*/
	public ImmutablePitchSequence I() {
		Iterator<Integer> memberIt = this.pitches.iterator();
		PitchSequence newSeq = new PitchSequence();
		// save instead of recalculating -- memory/CPU tradeoff. Memory wins
		// here.
		Integer max = this.maxPitch();
		Integer min = this.minPitch();
		while (memberIt.hasNext()) {
			Integer pitch = memberIt.next();
			newSeq.addPitch(max - pitch + min);
		}
		return new ImmutablePitchSequence(newSeq);
	}
	
	/**
	 * Returns a subsequence of this using a start index and a length.
	 * Throws an IllegalArgumentException if start index is less than 0 or the subsequence extends beyond the range of this objects List members.
	 * @param start  The index at which to start the search.
	 * @param length  The length at which to search.
	 */
	public ImmutablePitchSequence subSequence(int start, int length) {
		PitchSequence outSeq = new PitchSequence();
		if ( start < 0 || length < 0) {
			throw new IllegalArgumentException("start and length values must be > 0.");
		}
		if ( start + length > this.pitches.size() ) {
			throw new IllegalArgumentException("end index must be less than the length of the list.");
		}
		int index = start;
		while ( index < (start + length) ) {
			outSeq.addPitch( this.pitches.get(index));
			index++;
		}
		return new ImmutablePitchSequence(outSeq);
	}

	/**
	 * Returns a subsequence of this using a start index and a length.
	 * This is a convenience method supporting the getAt(n) method in Groovy, allowing this to be indexed directly.
	 * Throws an IllegalArgumentException if start index is less than 0 or the subsequence extends beyond the range of this objects List members.
	 * @param index  The index at which the returned set will start.
	 * @return  A PitchSequence of length 1 representing the member at index.
	 */
	public ImmutablePitchSequence getAt(int index) {
		return this.subSequence(index, 1);
	}

	/**
	 * Returns a subsequence of this using a start index and a length.
	 * This is a convenience method supporting the getAt(n) method in Groovy, allowing this to be indexed directly.
	 * Throws an IllegalArgumentException if start index is less than 0 or the subsequence extends beyond the range of this objects List members.
	 * @param indexList  The index at which the returned set will start.
	 * @return  A PitchSequence created from this using the indices in indexList.
	 */
	public ImmutablePitchSequence getAt( List<Integer> indexList ) {
		return this.subSequence(indexList);
	}

	/**
	 * Returns a subsequence of this sequence using an array of indices. Throws an IllegalArgumentException if the
	 * array contains a negative number or a number larger than the size of this objects List members.
	 * @param indices Integer[]  The indices to determine membership in the returned set.
	 * @return IPitchSequence  An IPitchSequence containing the members of this specified by indices.
	 */
	public ImmutablePitchSequence subSequence( Integer[] indices ) {
		PitchSequence outSeq = new PitchSequence();
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
			throw new IllegalArgumentException("Passed in array contains an index not contained in List this.pitches.");
		}
		for ( int index : indices ) {
			outSeq.addPitch( this.getMembers().get(index) );
		}
		return new ImmutablePitchSequence(outSeq);
	}

	/*
	 * A convenience alias for the subSequence method using a List<Integer>. Calls the Integer[] signature of this method.
	 * 
	 * (non-Javadoc)
	 * @see org.marqrdt.setTheory.IPitchSequence#subSequence(int, int)
	 */
	/**
	 * Returns a subsequence of this sequence using an List of indices. Throws an IllegalArgumentException if the
	 * array contains a negative number or a number larger than the size of this objects List members. This method is
	 * useful in combination with the getEmbeddedSubSequence() method to create embedded Sequences.
	 * @param indices  The indices to determine membership in the returned set.
	 * @return  An IPitchSequence containing the members of this specified by indices.
	 */
	public ImmutablePitchSequence subSequence( List<Integer> indices ) {
		return this.subSequence( indices.toArray( new Integer[0] ) );
	}
	
	/**
	 * Divides this into an ArrayList of IPitchSequence using the passed-in Integer[] array.
	 * Throws an IllegalArgumentException if the array contains a negative number or 
	 * a number larger than the size of this.
	 * Example:
	 * Using Row p = new Row("0b12a8539476");
	 * p.partitionBy( new Integer[] { 3, 4, 5 } ) will return a of List&lt;IPitchSequence&gt; 
	 * consisting of 3 elements: &lt;0b1&gt; &lt;2a85&gt; &lt;39476&gt;
	 * @param slices Integer[]  The indices to determine how to divide this.
	 * @return IPitchSequence  A List of IPitchSequence partitioned by the passed-in Integer[] array .
	 */
	public List<IMutableSequence> partitionBy( Integer[] slices ) {
		List<IMutableSequence> seqList = new ArrayList<IMutableSequence>();
		int inLength = 0;
		//System.out.println( String.format("partitioning by %s", Arrays.toString(slices)));
		for ( int i = 0; i < slices.length; i++ ) {
			if (slices[i] < 0 || slices[i] > this.size() ) {
				throw new IllegalArgumentException(String.format("Element: %d : %d may not be less than 0 or greater than the size of this (%d).", i, slices[i], this.length() ) );
			}
			inLength += slices[i];
		}
		if ( this.length() != inLength ) {
			throw new IllegalArgumentException(String.format("Sum of the array passed in: %d does not equal the size of this: %d.", inLength, this.size() ) );
		}
		int currentIndex = 0;
		for ( int i = 0; i < slices.length; i++ ) {
			seqList.add( new PitchSequence( this.subSequence(currentIndex, slices[i]).getMembers() ) );
			currentIndex += slices[i];
		}
		return seqList;
	}

	/**
	 * Divides this into an ArrayList of IPitchSequence using the passed-in List&lt;Integer&gt;.
	 * Throws an IllegalArgumentException if the array contains a negative number or 
	 * a number larger than the size of this.
	 * Example:
	 * Using Row p = new Row("0b12a8539476");
	 * p.partitionBy( new Integer[] { 3, 4, 5 } ) will return p partitioned
	 * into 3 IPitchSequence instances: [ &lt;0 b 1&gt;, &lt;2 a 8 5&gt;, &lt;3 9 4 7 6&gt; ].
	 * @param slicesList List&lt;Integer&gt;  A List&lt;Integer&gt; with indices to determine how to divide this.
	 * @return IPitchSequence  A List of IPitchSequence partitioned by the passed-in Integer[] array .
	 */
	public List<IMutableSequence> partitionBy( List<Integer> slicesList ) {
		List<ISequence> seqList = new ArrayList<ISequence>();
		Integer[] slices = slicesList.toArray( new Integer[0]);
		return this.partitionBy(slices);
	}
	
	/**
	 * Returns a List of this.getMembers() - 1. The list contains the distances between successive elements of this.
	 * The members of the List can be positive or negative depending on the contour of this.
	 * @return  A list of the distances between successive elements of this.
	 */
	public List<Integer> intervalVector() {
		List<Integer> vector = new ArrayList<Integer>();
		for ( int i = 1; i < this.pitches.size(); i++ ) {
			vector.add( this.pitches.get(i) - this.pitches.get( i - 1) );
		}
		return vector;
	}
		
	/**
	 * Returns true if each member of this is equal the member of anotherSeq at the same index.
	 * @param anotherSeq  The sequence used for the comparison.
	 */
	public boolean equals( ISequence anotherSeq ) {
		// return false immediately if sizes do not match so that the membership check does not
		// fail on IndexOutOfBoundsException.
		if ( this.pitches.size() != anotherSeq.getMembers().size() ) {
			return false;
		}
		for ( int index = 0; index < this.pitches.size(); index++ ) {
			if ( this.pitches.get(index) != anotherSeq.getMembers().get(index) ) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * getEmbeddedSubsequence accepts an IPitchSequence inSeq. If the caller contains inSeq as an embedded subsequence, the method will
	 * return the indices in the caller's members that form inSeq. Example:
	 * IPitchSequence newSeq = new PitchClassSequence( [0,b,1,2,a,8,5,3,9,4,7,6] )
	 * IPitchSequence subSeq = new PitchClassSequence( [b,2,8,3] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = List 1,3,5,7
	 * IPitchSequence anotherSubSeq = new PitchClassSequence( [0,4,7,6] )
	 * newSeq.getEmbeddedSubsequence( anotherSubSeq ) = List 0,9,10,11
	 * 
	 * If the caller does not contain the subSequence, it will return null
	 * IPitchSequence notASubSeq = new PitchClassSequence( [6,4,3,8] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = null
	 *  
	 *  The List object returned form this method can be used with the subSequence() method to create
	 *  a PitchSequence from the List of indices returned by this method.
	 * @param inSeq  The IPitchSequence used for the operation.
	 * @return  A list of indices at which members of anotherPitchSequence can be found in this.
	 */
	public List<Integer> getEmbeddedSubsequence( ISequence inSeq ) {
		List<Integer> indices = new ArrayList<Integer>();
		int counter = 0;
		for ( int index = 0; index < this.length(); index++ ) {
			int member = this.getMembers().get(index);
			if ( inSeq.getMembers().get(counter) == member ) {
				indices.add( index );
				counter++;
			}
		}
		// If all elements in inSeq were found in this objects members.
		if ( indices.size() == inSeq.length() ) {
			return indices;
		}
		return null;
	}

	/**
	 * Returns a new PitchSequence with inSeq appended to this.
	 * This method is created to support the "+" operator in a Groovy DSL.
	 * @param  inSeq The PitchSequence to be appended to this.
	 * @return  A new PitchSequence with inSeq appended to this.
	 */
	public PitchClassSequence plus( ISequence inSeq ) {
		PitchClassSequence pSeq = new PitchClassSequence( this );
		pSeq.extend(inSeq);
		return pSeq;
	}

	/**
	 * Returns the lowest pitch in this.
	 * @return  The lowest pitch in this.
	 */
	public int minPitch() {
		return Collections.min(this.getMembers());
	}

	/**
	 * Returns the highest pitch in this.
	 * @return  The highest pitch in this.
	 */
	public int maxPitch() {
		return Collections.max(this.getMembers());
	}

	/**
	 * Return the distance between the highest and lowest pitches of this.
	 * @return  The distance between the highest and lowest pitches of this.
	 */
	public int range() {
		if ( this.getMembers().size() < 1 ) {
			return 0;
		}
		return this.maxPitch() - this.minPitch();
	}

	/**
	 * Constructs a PitchSequence from a String containing Integer tokens separated by a comma and a space ", ".
	 * Example: 12, 43, 73, 9, 34, 2, 121. Extraneous characters are ignored.
	 * @param pcSeqString  String containing tokens to parse.
	 * @return  A PitchSequence constructed from String of tokens representing pitches as Integers.
	 */
	public static ImmutablePitchSequence getPitchSequenceFromString( String pcSeqString ) {
		List<Integer> pcList = new ArrayList<Integer>();
		final String sepRegex = "[,\\s+]";
		Pattern splitPattern = Pattern.compile( sepRegex );
		//Matcher pitchStringMatcher = pitchStringPattern.matcher( pcSeqString );
		String[] items = splitPattern.split( pcSeqString );
		for ( String pc : items ) {
			Pattern numberPattern = Pattern.compile( "\\d+" );
			if ( numberPattern.matcher( pc ).matches() ) {
				pcList.add( Integer.parseInt( pc ) );
			}
		}
		return new ImmutablePitchSequence( pcList );
	}

	/**
	 * Accepts a String containing a sequence transformation tokens. It should be in the form of {T[n]}{M[n]}{I},
	 * where {} represents optional tokens. For example, "RT[5]M[7]I", or "T[A]I". Warning: The tokens will always be
	 * parsed in canonical order (RTMI) even if they do not appear in that order in the String parameter.
	 * @param transformation  A String containing a sequence transformation tokens.
	 * @return IPitchSequence
	 */
	public ImmutablePitchSequence getTransformationFromString(String transformation) {
		PitchSequence outSeq = new PitchSequence( this.getMembers() );
		int tIndex = 0;
		boolean retrograde = false;
		int mIndex = 1;
		boolean inversion = false;
		// It may be slightly less efficient, but until I know each Pattern works, we'll
		// keep the pattern matches separate for each of T(n), M, I, R
		Pattern transpositionPattern = Pattern.compile(".*T\\[*([0-9]+)\\]*");
		Matcher transMatcher = transpositionPattern.matcher(transformation);
		if ( transMatcher.matches() ) {
			if ( transMatcher.groupCount() > 0 ) {
				//System.out.println( String.format("Found a match %s in %s", transMatcher.group(1), transformation) );
				tIndex = Integer.parseInt( transMatcher.group(1) );
			}
		}
		Pattern retrogradePattern = Pattern.compile(".*R.*");
		Matcher retrogradeMatcher = retrogradePattern.matcher(transformation);
		if ( retrogradeMatcher.matches() ) {
			retrograde = true;
		}
		Pattern multPattern = Pattern.compile(".*M\\[*([0-9]+)\\]*");
		Matcher multMatcher = multPattern.matcher(transformation);
		if ( multMatcher.matches() ) {
			if ( multMatcher.groupCount() > 0 ) {
				// parse in base 16 to allow canonical form A, B, etc.
				mIndex = Integer.parseInt( multMatcher.group(1), 16 );
			} else {
				mIndex = 1;
			}
		}
		if ( inversion ) {
			outSeq = outSeq.I();
		}
		outSeq = outSeq.M(mIndex);
		outSeq = outSeq.T(tIndex);
		if ( retrograde ) {
			outSeq = outSeq.R();
		}
		outSeq.setDescriptor(transformation);
		return new ImmutablePitchSequence(outSeq);
	}
	
	/**
	 * Possibly unused or not useful.
	 * @return The PitchClassSequenceTransformation applied to this. Caller must check for null value;
	 */
    public PitchClassSequenceTransformation getTransformation() {
        // TODO Auto-generated method stub
        return this.transformation;
    }

    /**
	 * Possibly unused or not useful.
	 * @param inTrans  Set the PitchClassSequenceTransformation of this to inTrans.
	 */
    public void setTransformation( PitchClassSequenceTransformation inTrans) {
        // TODO Auto-generated method stub
        this.transformation = inTrans;
    }
	
	/**
	 * Returns a String representation of this.
	 * @return  A String representation of this.
	 */
	public String toString() {
		StringBuffer outputBuf = new StringBuffer();
		outputBuf.append( "Pitch Sequence <" );
		int count = 0;
		for ( Integer elem : this.pitches ) {
			outputBuf.append( Integer.toString(elem) );
			if ( count < this.pitches.size() - 1 ) {
				outputBuf.append(" ");
			}
			count++;
		}
		outputBuf.append(">");
		return outputBuf.toString();
	}

	/**
	 * Sets a text name for this. Could be something like "My favorite sequence of pitches" or "Berg Violin Concerto row".
	 * @param name The name to set.
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * Returns the text name for this.
	 * @return String The text name for this.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets a text descriptor for this. Could be something like "My favorite sequence of pitches" or "Berg Violin Concerto row".
	 * Several transformation operations manipulate the descriptor with text such as "T[m]M[n]".
	 * @param descriptor  The text descriptor of this.
	 */
    public void setDescriptor(String descriptor) {
		// TODO Auto-generated method stub
		this.descriptor = descriptor;
	}

	/**
	 * Returns the text descriptor for this.
	 * @return String  The text descriptor for this.
	 */
	public String getDescriptor() {
		return this.descriptor;
	}

	/**
	 * Returns the members of this as a List.
	 * @return  A List containing the members of this.
	 */
	public List<Integer> getMembers() {
		// TODO Auto-generated method stub
		return this.pitches;
	}
}
