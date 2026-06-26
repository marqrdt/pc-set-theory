package org.paulmarquardt.setTheory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.paulmarquardt.setTheory.interfaces.*;

/**
 * A PitchClassSequence is an ordered set or Pitch Classes (pcs), such that each member of the PitchClassSequence
 * can be said to come before or after another member. Members are Integers between 0 and 12 and the resultant operations
 * are limited to this range.
 * 
 * @author marqrdt
 *
 */
public class ImmutablePitchClassSequence extends ImmutablePitchSequence implements ISequence {

	/**
	 * Create an empty PitchClassSequence.
	 */
	public ImmutablePitchClassSequence() {
		super.pitches = new ArrayList<Integer>();
	}
	
	/**
	 * Create a new PitchClassSetSequence from a String representing pitch-class tokens.
	 * A PitchClassSequence is an ordered Set of pitch classes 0-11 representing each of the 
	 * pitches of the chromatic scale.
	 * @param pitchString A String containing the pitches from which to build the PitchClassSequence. Each member of this String will be converted to its PitchClass equivalent.
	 */
	public ImmutablePitchClassSequence( String pitchString ) {
		this( ImmutablePitchClassSequence.getPitchClassSequenceFromString( pitchString ) );
	}

	/**
	 * Create a new PitchClassSetSequence from a Array of Integer representing pitch-class numbers.
	 * A PitchClassSequence is an ordered Set of pitch classes 0-11 representing each of the 
	 * pitches of the chromatic scale.
	 * @param pitchArray An array containing the pitches from which to build the PitchClassSequence. Each member of this Array will be converted to its PitchClass equivalent.
	 */
	public ImmutablePitchClassSequence( Integer[] pitchArray ) {
		this( Arrays.asList( pitchArray ) );
	}
	
	/**
	 * Create a new PitchClassSetSequence from an implementation of IPitchSequence.
	 * A PitchClassSequence is an ordered Set of pitch classes 0-11 representing each of the 
	 * pitches of the chromatic scale.
	 * @param pitchSeq  An IPitchSequence containing the pitches from which to build the PitchClassSequence. Each member of this IPitchSequence will be converted to its PitchClass equivalent.
	 */
	public ImmutablePitchClassSequence( ISequence pitchSeq ) {
		this( pitchSeq.getMembers() );
		this.setDescriptor( pitchSeq.getDescriptor() );
	}

	/**
	 * Create a new PitchClassSetSequence from a Collection of Integer.
	 * A PitchClassSequence is an ordered Set of pitch classes 0-11 representing each of the 
	 * pitches of the chromatic scale.
	 * @param pitchesAsList  A List containing the pitches from which to build the PitchClassSequence. Each member of this List will be converted to its PitchClass equivalent.
	 */
	public ImmutablePitchClassSequence( Collection<Integer> pitchesAsList ) {
		super();
		super.pitches = new ArrayList<Integer>();
		if ( pitchesAsList == null) {
			System.out.println( "pitchesAsList is null");
		}
		//System.out.println( String.format("pitchesAsList is a %s", pitchesAsList.toString()));
		for ( Integer i : pitchesAsList ) {
			super.pitches.add( PitchClassSet.mod(i, PitchClassSet.MODULUS ) );
		}
	}

	/**
	 * Return a PitchClassSequence created from this transposed by the transposition parameter.
	 * @param transposition  The integer by which the returned IPitchSequence will be transposed.
	 */
	public ImmutablePitchClassSequence T( int transposition ) {
		return new ImmutablePitchClassSequence( super.T(transposition) );
	}

	/**
	 * Return a PitchClassSequence created from this transposed so that the first pitch is start.
	 * @param start  The integer to which the first element of the returned IPitchSequence will be transposed.
	 */
	public ImmutablePitchClassSequence transposeTo(int start) {
		//PitchClassSequence outSeq = new PitchClassSequence( this );
		if ( this.getMembers().size() == 0 ) {
			return this;
		}
		return this.T( start - this.getMembers().get(0) );
	}

	/**
	 * Return the retrograde (order-reversed) form of this.
	 */
	public ImmutablePitchClassSequence R() {
		// TODO Auto-generated method stub
		return new ImmutablePitchClassSequence( super.R() );
	}

	/**
	 * Return the a new PitchClassSequence created by taking the member at each index (m * n) mod 12
	 * of this for each n in 0,this.length().
	 * If index == 1, it will return the original set.
	 * If index == 11, it will return the retrograde set.
	 * If index == 5, it will return a set created by taking every 5th element (mod set.length()) of this.
	 * If index == 1, it will return the original set.
	 * If index and this.length() are not relatively prime, the returned set will contaon duplicates.
	 * @param index  The pitch to which the first pitch of this will be transposed.
	 * @return  An order-reversed version of this.
	 */
	public ImmutablePitchClassSequence O(int index) {
		PitchClassSequence outSeq = new PitchClassSequence();
		int currentIndex = 0;
		for ( int i = 0; i <= this.getMembers().size() - 1; i++ ) {
			outSeq.addPitch( this.getMembers().get( (currentIndex) % this.getMembers().size() ) );
			currentIndex += index;
		}
		return new ImmutablePitchClassSequence(outSeq);
	}

	/**
	 * Return the M[n] transformation of this such that all elements are multiplied by n mod 12.
	 */
	public ImmutablePitchClassSequence M(int mult) {
		// TODO Auto-generated method stub
		return new ImmutablePitchClassSequence( super.M(mult, PitchClassSet.MODULUS) );
	}

	/**
	 * Returns the inversion transformation of this, i.e. for each member n of this, the returned set contains m at the same index where = (12 - n) mod 12. 
	 * @return IPitchSequence  The Inversion transformation of this.
	 */
	public ImmutablePitchClassSequence I() {
		// TODO Auto-generated method stub
		List<Integer> members = this.getMembers();
		PitchClassSequence pcSeq = new PitchClassSequence();
		for ( int member : members ) {
			pcSeq.addPitch( PitchClassSet.mod( 12 - member, PitchClassSet.MODULUS ) );
		}
		return new ImmutablePitchClassSequence( pcSeq );
	}
	
	/**
	 * Returns an ImmutablePitchClassSequence transformed by the Whole-tone transformation.
	 * The Whole-tone transformation in a transformation of P such that every even-numbered
	 * (or odd-numbered) pc is increased by an even number mod 12, usually by 2.
	 * For example, given the Row p:
	 * &lt;0 b 1 2 A 8 5 3 9 4 7 6&gt; , p.W(1) is:
	 * &lt;0 1 3 2 A 8 7 5 B 4 9 6&gt;
	 * The Whole-tone transformations have interesting properties in relation to
	 * each other and the original row. This single-argument method transforms the members of the
	 * Wholetone scale beginning on 0.
	 * @param index The index at which the members of the Whole-tone scale on 0 will be transformed.
	 * @return An ImmutablePitchClassSequence transformed by the Whole-tone transformation at index n.
	 */
	public ImmutablePitchClassSequence W( int index ) {
		return this.W(index, 0);
	}

	/**
	 * Returns an ImmutablePitchClassSequence transformed by the Whole-tone transformation.
	 * The Whole-tone transformation in a transformation of P such that every even-numbered
	 * (or odd-numbered) pc is increased by an even number mod 12, usually by 2.
	 * For example, given the Row p:
	 * &lt;0 b 1 2 A 8 5 3 9 4 7 6&gt; , p.W(1) is:
	 * &lt;0 1 3 2 A 8 7 5 B 4 9 6&gt;
	 * The Whole-tone transformations have interesting properties in relation to
	 * each other and the original row.
	 * @param index  The index at which the members of the Whole-tone scale on 0 will be transformed.
	 * @param polarity  If polarity is even, the members of the Wholetone scale beginning on 0 are transformed. If odd, the
	 * members of the Wholetone scale beginning in 1 are transformed.
	 * @return An ImmutablePitchClassSequence transformed by the Whole-tone transformation at index n.
	 */
	public ImmutablePitchClassSequence W( int index, int polarity ) {
		PitchClassSequence outSeq = new PitchClassSequence();
		for ( int p : this.getMembers() ) {
			if ( p % 2 ==  polarity % 2 ) {
				outSeq.addPitch( (p + index * 2)  % PitchSet.MODULUS);
			} else {
				outSeq.addPitch( p );
			}
		}
		return new ImmutablePitchClassSequence( outSeq );
	}

	/**
	 * Return a PitchClassSequence created from this transposed by the transposition parameter and multiplied by the mult parameter.
	 * Both operations are performed mod 12.
	 * @param transposition  The integer by which the returned IPitchSequence will be transposed.
	 * @param mult  The integer by which the returned IPitchSequence will be multiplied.
	 * @return IPitchSequence This transformed by T and M.
	 */
	public ImmutablePitchClassSequence TM( int transposition, int mult ) {
		ImmutablePitchClassSequence outSet = new ImmutablePitchClassSequence( super.M(mult, PitchClassSet.MODULUS) );
		return new ImmutablePitchClassSequence( outSet.T(transposition));
	}

	/**
	 * Returns true if each member of this is equal the member of anotherSeq at the same index.
	 * @param anotherSeq  The sequence used for the comparison.
	 */
	public boolean equals( ISequence anotherSeq ) {
		// return false immediately if sizes do not match so that the membership check does not
		// fail on IndexOutOfBoundsException.
		if ( this.getMembers().size() != anotherSeq.getMembers().size() ) {
			return false;
		}
		for ( int index = 0; index < this.pitches.size(); index++ ) {
			if ( this.getMembers().get(index) != PitchClassSet.mod( anotherSeq.getMembers().get(index), PitchClassSet.MODULUS ) ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if anotherSeq can be transformed in this under any combination or T[n], I or R.
	 * The comparison is performed mod 12, so that an instance of an IPitchSequence will be considered
	 * equal of if the respective members are octave transpositions of each other. It essentially compares
	 * the interval sequence of this and anotherSeq.
	 * @param anotherSeq  The sequence used for the comparison.
	 * @return boolean value indicating if anotherSeq can be transformed into this under any combination of T, I or R.
	 */
	public boolean equivalent( ISequence anotherSeq ) {
		// return false immediately if sizes do not match so that the membership check does not
		// fail on IndexOutOfBoundsException and to not waste time checking on objects with different lengths,
		// which will always be unequal.
		if ( this.pitches.size() != anotherSeq.getMembers().size() ) {
			return false;
		}
		/*
			Treat the interval sequence just as a PitchClassSequence even though we're not using the
			members as pitch classes..
		 */
		PitchClassSequence anotherPcSeq = new PitchClassSequence( anotherSeq );
		// Store intervals() to avoid recalculation.
		List<Integer> intervalsThis = this.intervals();
		if ( intervalsThis.equals( anotherPcSeq.intervals() ) ) {
			return true;
		}
		if ( intervalsThis.equals( anotherPcSeq.I().intervals() ) ) {
			return true;
		}
		if ( intervalsThis.equals( anotherPcSeq.R().intervals() ) ) {
			return true;
		}
		if ( intervalsThis.equals( anotherPcSeq.R().I().intervals() ) ) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a PitchClass vector representing this PitchClassSequence. The vector is an Array of size 12
	 * with each index representing a count of PCs at the index. For example, for the given sequences:
	 * [0,1,0,2,0,3,0,4,0,5] pcVector would be [5,1,1,1,1,1,0,0,0,0,0,0]
	 * A twelve-tone row would be [1,1,1,1,1,1,1,1,1,1,1,1]
	 * @return Array of Integer representing the PC Vector of this PitchClassSequence.
	 */
	public Integer[] pcVector() {
		Integer[] vector = new Integer[12];
		for ( int i = 0; i < PitchClassSet.MODULUS; i++ ) {
			vector[i] = 0;
		}
		for ( Integer member : this.getMembers() ) {
			vector[ member ]++;
		}
		return vector;
	}

	/**
	 * Returns a  vector representing the interval between consecutive members of this sequence.
	 * @return List of Integer representing consecutive intervals between consecutive members of this.
	 */
	public List<Integer> intervals() {
		return this.intervals(1);
	}

	/**
	 * Returns a  vector representing the interval between consecutive members of this sequence.
	 * @param distance An integer specifying the distance between members to measure. For example, a distance
	 * of 1 will measure the distance between consecutive members. A distance of 2 will measure the distance between every other member.
	 * @return List of Integer representing consecutive intervals between consecutive members of this.
	 */
	public List<Integer> intervals( int distance ) {
		List<Integer> vector = new ArrayList<Integer>();
		for ( int i = 0; i < this.getMembers().size() - distance; i++ ) {
			vector.add( PitchClassSet.mod( this.getMembers().get( i + distance ) - this.getMembers().get( i ), PitchClassSet.MODULUS ) );
		}
		return vector;
	}

	/**
	 * Returns an Iterator that iterates through every transformation of this.
	 * Includes all 96 T, M[n] for n in[1,5,7,11] and R transformations. I() transformations are effected using M[11].
	 * @return An Iterator that iterates through every transformation of this.
	 */
	public Iterator<ImmutablePitchClassSequence> transformationIterator() {
		
		final ImmutablePitchClassSequence thisSeq = new ImmutablePitchClassSequence( this.getMembers() );
		class TransformationIterator implements Iterator<ImmutablePitchClassSequence> {

			private int index = 0;
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return ( index <  96 );
			}
			public ImmutablePitchClassSequence next() {
				// TODO Auto-generated method stub
				StringBuffer transformationBuffer = new StringBuffer();
				int tIndex = index / 8;
				int iIndex = index % 8;
				//System.out.println( String.format("index: %d, iIndex: %d, iIndex mod 2 = %d, iIndex >> 1 = %d",index,  iIndex, iIndex % 2, iIndex >> 1 ) );
				boolean inversion = false;
				boolean retrograde = false;
				int mult = 1;
				//IPitchSequence seq = thisSeq.transposeTo(tIndex);
				//if ( (iIndex >> 1) == 0) {
				//	seq = seq.transposeTo(tIndex);
				//}
				ImmutablePitchClassSequence outSeq = new ImmutablePitchClassSequence( thisSeq );
				if ( iIndex % 2 == 1 ) {
					retrograde = true;
					transformationBuffer.append("R");
					//seq = seq.R();
				} else {
					retrograde = false;
				}
				transformationBuffer.append( String.format("T[%d]", tIndex) );									
				if ( (iIndex >> 1) == 2) {
					mult = 5;
					transformationBuffer.append( String.format("M[%d]", mult) );				
					//seq = seq.M( 5 );
				}
				if ( (iIndex >> 1) == 3) {
					mult = 7;
					transformationBuffer.append( String.format("M[%d]", mult) );					
					//seq = seq.M( 7 );
				}
				if ( (iIndex >> 1) == 1) {
					inversion = true;
					transformationBuffer.append( String.format("I") );					
					//seq = seq.I();
				}
				//seq = seq.T(tIndex);
				if ( inversion ) {
					outSeq = outSeq.I();
				}
				if ( mult > 1 ) {
					outSeq = outSeq.M(mult);
				}
				outSeq = outSeq.T(tIndex);
				if ( retrograde ) {
					outSeq = outSeq.R();
				}
				outSeq.setDescriptor( transformationBuffer.toString() );
				index++;
				return outSeq;
			}

			public void remove() {
				// TODO Auto-generated method stub
				
			}
		}
		return new TransformationIterator();
	}
	
	/**
	 * Returns a subsequence of this using a start index and a length.
	 * Throws an IllegalArgumentException if start index is less than 0 or the subsequence extends beyond the range of this objects List members.
	 * @param start  The index at which to start the search.
	 * @param length  The length at which to search.
	 */
	public ImmutablePitchClassSequence subSequence(int start, int length) {
		PitchSequence outSeq = new PitchClassSequence();
		if ( start < 0 || length < 0) {
			throw new IllegalArgumentException("start and length values must be > 0.");
		}
		if ( start + length > this.getMembers().size() ) {
			throw new IllegalArgumentException("end index must be less than the length of the list.");
		}
		int index = start;
		while ( index < (start + length) ) {
			outSeq.addPitch( this.getMembers().get(index));
			index++;
		}
		return new ImmutablePitchClassSequence( outSeq );
	}

	/**
	 * getEmbeddedSubsequence accepts an IPitchSequence inSeq. If this contains inSeq as an embedded subsequence, 
	 * the method will return the indices in the caller's members that form inSeq.
	 * Example:
	 * IPitchSequence newSeq = new PitchClassSequence( [0,b,1,2,a,8,5,3,9,4,7,6] )
	 * IPitchSequence subSeq = new PitchClassSequence( [b,2,8,3] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = &lt;List 1,3,5,7&gt;
	 * IPitchSequence anotherSubSeq = new PitchClassSequence( [0,4,7,6] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) returns List&lt;0,9,10,11&gt; 
	 * If the caller does not contain the subSequence, it will return null
	 * IPitchSequence notASubSeq = new PitchClassSequence( [6,4,3,8] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = null
	 *  
	 * @param inSeq  The IPitchSequence used for the operation.
	 * @return  A list of indices at which members of anotherPitchSequence can be found in this.
	 */
	public List<Integer> getEmbeddedSubsequence( ISequence inSeq ) {
		// fail fast if inSeq has more members than this, making it impossible to be contained as a subsequence.
		if ( inSeq.length() > this.length() ) {
			return null;
		}
		List<Integer> indices = new ArrayList<Integer>();
		int counter = 0;
		for ( int index = 0; index < this.length(); index++ ) {
			int member = this.getMembers().get(index);
			//System.out.println( String.format("getSubSequence => getMembers(%d) = %d", index, member) );
			if ( inSeq.getMembers().get(counter) % PitchClassSet.MODULUS == member ) {
				//System.out.println( String.format("getSubSequence => found match in %sat %d", inSeq.toString(), index) );
				indices.add( index );
				counter++;
				if ( indices.size() == inSeq.length() ) {
					return indices;
				}
			}
		}
		// If all elements in inSeq were found in this objects members.
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
	 * Construct a PitchSequence from a String containing Integer tokens separated by a comma and a space ", ".
	 * Example: 12, 43, 73, 9, 34, 2, 121. Extraneous characters are ignored. Elements in the string are converted
	 * to pitch-classes using n % 12 for each n in the member string.
	 * @param pcSeqString  String containing tokens to parse.
	 * @return PitchSequence constructed from String of tokens representing pitches as Integers.
	 */
	public static ImmutablePitchClassSequence getPitchClassSequenceFromString( String pcSeqString ) {
		List<Integer> pcList = new ArrayList<Integer>();
		for ( Character pc : pcSeqString.toCharArray() ) {
			if ( Character.isDigit(pc) ) {
				pcList.add( new Integer( pc ) );
			}
			if ( pc.toString().equalsIgnoreCase("a") ) {
				pcList.add( new Integer( 10 ) );
			}
			if ( pc.toString().equalsIgnoreCase("b") ) {
				pcList.add( new Integer( 11 ) );
			}
			if ( pc.toString().equalsIgnoreCase("t") ) {
				pcList.add( new Integer( 10 ) );
			}
			if ( pc.toString().equalsIgnoreCase("e") ) {
				pcList.add( new Integer( 11 ) );
			}
		}
		return new ImmutablePitchClassSequence( pcList );
	}

	/**
	 * Accepts a String containing a sequence transformation tokens. It should be in the form of {T[n]}{M[n]}{I},
	 * where {} represents optional tokens. For example, "RT[5]M[7]I", or "T[A]I". Warning: The tokens will always be
	 * parsed in canonical order (RTMI) even if they do not appear in that order in the String parameter.
	 * @param transformation  A String containing a sequence transformation tokens.
	 * @return IPitchSequence
	 */
	public ImmutablePitchClassSequence getTransformationFromString(String transformation) {
		ImmutablePitchClassSequence outSeq = new ImmutablePitchClassSequence( this );
		int tIndex = 0;
		boolean retrograde = false;
		int mIndex = 1;
		boolean inversion = false;
		// It may be slightly less efficient, but until I know each Pattern works, we'll
		// keep the pattern matches separate for each of T(n), M, I, R
		Pattern transpositionPattern = Pattern.compile(".*T\\[*([a-bA-B0-9]?)\\]*.*");
		Matcher transMatcher = transpositionPattern.matcher(transformation);
		if ( transMatcher.matches() ) {
			if ( transMatcher.groupCount() > 0 ) {
				//System.out.println( String.format("Found a match %s in %s", transMatcher.group(1), transformation) );
				tIndex = Integer.parseInt( transMatcher.group(1), 16 );
			}
		}
		
		Pattern retrogradePattern = Pattern.compile(".*R.*");
		Matcher retrogradeMatcher = retrogradePattern.matcher(transformation);
		if ( retrogradeMatcher.matches() ) {
			retrograde = true;
		}
		
		Pattern inversionPattern = Pattern.compile(".*I.*");
		Matcher inversionMatcher = inversionPattern.matcher(transformation);
		if ( inversionMatcher.matches() ) {
			//System.out.println( "Matching Inversion operator" );
			inversion = true;
		}
		
		Pattern multPattern = Pattern.compile(".*M\\[*([a-bA-B0-9]+)\\]*");
		Matcher multMatcher = multPattern.matcher(transformation);
		if ( multMatcher.matches() ) {
			if ( multMatcher.groupCount() > 0 ) {
				mIndex = Integer.parseInt( multMatcher.group(1), 16 );
			} else {
				mIndex = 7;
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
		return outSeq;
	}

	/**
	 * Create a new PitchClassSet from this. A convenience method in lieu of casting
	 * or object creation syntax.
	 * @return  A PitchClassSet created from this.
	 */
	public PitchClassSet asPCSet() {
		return new PitchClassSet( this );
	}

	/**
	 * Returns a String representation of this.
	 * @return  A String representation of this.
	 */
	public String toString() {
		StringBuffer outputBuf = new StringBuffer();
		outputBuf.append("<");
		int count = 0;
		for ( Integer elem : super.pitches ) {
			outputBuf.append( Integer.toHexString(elem) );
			if ( count < this.getMembers().size() - 1 ) {
				outputBuf.append(" ");
			}
			count++;
		}
		outputBuf.append(">");
		return outputBuf.toString();
	}

	/**
	*  toString for a PitchClassSequence concatenates the members without spaces. Each member
	*  will only occupy one byte, so there is no need for spaces.
	 * @return The String representation of this prepended with its Transformation String. 
	 */
	public String toStringExtended() {
		StringBuffer outputBuf = new StringBuffer();
		outputBuf.append( StringUtils.rightPad( this.getDescriptor().toString(), 10) );
		outputBuf.append(": <");
		for ( Integer elem : this.getMembers() ) {
			outputBuf.append( Integer.toHexString(elem) );
		}
		outputBuf.append(">");
		return outputBuf.toString();
	}

	public List<Integer> getMembers() {
		// TODO Auto-generated method stub
		return super.pitches;
	}

	public void setDescriptor(String descriptor) {
		// TODO Auto-generated method stub
		this.descriptor = descriptor;
	}

	public String getDescriptor() {
		// TODO Auto-generated method stub
		return this.descriptor;
	}
}
