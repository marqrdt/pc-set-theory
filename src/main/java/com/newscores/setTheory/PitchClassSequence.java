package com.newscores.setTheory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.commons.lang.StringUtils;

import com.newscores.setTheory.interfaces.*;

/**
 * A PitchClassSequence is an ordered set or Pitch Classes (pcs), such that each member of the PitchClassSequence
 * can be said to come before or after another member. Members are Integers between 0 and 12 and the resultant operations
 * are limited to this range.
 * 
 * @author marqrdt
 *
 */
public class PitchClassSequence extends PitchSequence implements IMutableSequence {

	static final String defaultHeadString = "<";
	static final String defaultTailString = ">";
	static final String defaultSeparatorString = " ";
	/**
	 * Create an empty PitchClassSequence.
	 */
	public PitchClassSequence() {
		super( new Integer[0] );
	}
	
	/**
	 * Create a new PitchClassSetSequence from a String representing pitch-class tokens.
	 * A PitchClassSequence is an ordered Set of pitch classes 0-11 representing each of the 
	 * pitches of the chromatic scale.
	 * @param pitchString A String containing the pitches from which to build the PitchClassSequence. Each member of this String will be converted to its PitchClass equivalent.
	 */
	public PitchClassSequence( String pitchString ) {
		this( PitchClassSequence.getPitchClassSequenceFromString( pitchString ) );
	}

	/**
	 * Create a new PitchClassSetSequence from a Array of Integer representing pitch-class numbers.
	 * A PitchClassSequence is an ordered Set of pitch classes 0-11 representing each of the 
	 * pitches of the chromatic scale.
	 * @param pitchArray An array containing the pitches from which to build the PitchClassSequence. Each member of this Array will be converted to its PitchClass equivalent.
	 */
	public PitchClassSequence( Integer[] pitchArray ) {
		this( Arrays.asList( pitchArray ) );
	}
	
	/**
	 * Create a new PitchClassSetSequence from an implementation of IPitchSequence.
	 * A PitchClassSequence is an ordered Set of pitch classes 0-11 representing each of the 
	 * pitches of the chromatic scale.
	 * @param pitchSeq  An IPitchSequence containing the pitches from which to build the PitchClassSequence. Each member of this IPitchSequence will be converted to its PitchClass equivalent.
	 */
	public PitchClassSequence( ISequence pitchSeq ) {
		this( pitchSeq.getMembers() );
		this.setDescriptor( pitchSeq.getDescriptor() );
		this.name = pitchSeq.getName();
	}

	/**
	 * Create a new PitchClassSetSequence from a Collection of Integer.
	 * A PitchClassSequence is an ordered Set of pitch classes 0-11 representing each of the 
	 * pitches of the chromatic scale.
	 * @param pitchesAsList  A List containing the pitches from which to build the PitchClassSequence. Each member of this List will be converted to its PitchClass equivalent.
	 */
	public PitchClassSequence( Collection<Integer> pitchesAsList ) {
		super(pitchesAsList);
		for ( int i = 0; i < this.getMembers().size(); i++ ) {
			this.pitches.set(i,  this.pitches.get(i) % 12 );
		}
		setName("");
	}


	public void addPitch( Integer pitch ) {
		this.pitches.add(pitch % PitchClassSet.MODULUS);
	}

	/**
	 * Return a PitchClassSequence created from this transposed by the transposition parameter.
	 * @param transposition  The integer by which the returned IPitchSequence will be transposed.
	 */
	public PitchClassSequence T( int transposition ) {
		return new PitchClassSequence( super.T(transposition) );
	}

	/**
	 * Return a PitchClassSequence created from this transposed so that the first pitch is start.
	 * @param start  The integer to which the first element of the returned IPitchSequence will be transposed.
	 */
	public PitchClassSequence transposeTo(int start) {
		//PitchClassSequence outSeq = new PitchClassSequence( this );
		if ( this.pitches.size() == 0 ) {
			return this;
		}
		return this.T( start - this.getMembers().get(0) );
	}

	/**
	 * Return the retrograde (order-reversed) form of this.
	 */
	public PitchClassSequence R() {
		// TODO Auto-generated method stub
		return new PitchClassSequence( super.R() );
	}

	/**
	 * Return the M[n] transformation of this such that all elements are multiplied by n mod 12.
	 */
	public PitchClassSequence M(int mult) {
		// TODO Auto-generated method stub
		return new PitchClassSequence( super.M(mult, PitchClassSet.MODULUS) );
	}

	/**
	 * Returns the inversion transformation of this, i.e. for each member n of this, the returned set contains m at the same index where = (12 - n) mod 12. 
	 * @return IPitchSequence  The Inversion transformation of this.
	 */
	public PitchClassSequence I() {
		// TODO Auto-generated method stub
		List<Integer> members = this.getMembers();
		PitchClassSequence pcSeq = new PitchClassSequence();
		for ( int member : members ) {
			pcSeq.addPitch( PitchClassSet.mod( 12 - member, PitchClassSet.MODULUS ) );
		}
		return pcSeq;
	}
	
	/**
	 * Return a PitchClassSequence created from this transposed by the transposition parameter and multiplied by the mult parameter.
	 * Both operations are performed mod 12.
	 * @param transposition  The integer by which the returned IPitchSequence will be transposed.
	 * @param mult  The integer by which the returned IPitchSequence will be multiplied.
	 * @return IPitchSequence This transformed by T and M.
	 */
	public PitchClassSequence TM( int transposition, int mult ) {
		PitchClassSequence outSet = new PitchClassSequence( super.M(mult, PitchClassSet.MODULUS) );
		return new PitchClassSequence( outSet.T(transposition));
	}

	/**
	 * Apply the transposition transformation, adding level to each member of this mod 12.
	 * @param level  The integer to which the first element of the returned IPitchSequence will be transposed.
	 */
	public void applyT(int level) {
		//PitchClassSequence outSeq = new PitchClassSequence( this );
		IntStream.range(0, this.size()).forEach(i -> {
			this.pitches.set(i, PitchClassSet.mod(this.pitches.get(i) + level, PitchClassSet.MODULUS));
		});
	}

	/**
	 * Apply the transposition transformation on this so that the first pitch is start.
	 * @param start  The integer to which the first element of the returned IPitchSequence will be transposed.
	 */
	public void applyTransposeTo(int start) {
		//PitchClassSequence outSeq = new PitchClassSequence( this );
		this.applyT( start - this.pitches.get(0) );
	}

	/**
	 * Transform this using the retrograde (order-reversed) operation.
	 */
	public void applyR() {
		// TODO Auto-generated method stub
		Collections.reverse( this.pitches );
	}

	/**
	 * Transform this using the M[n] transformation of this such that all elements are multiplied by n mod 12.
	 */
	public void applyM(int mult) {
		// TODO Auto-generated method stub
		IntStream.range(0, this.size()).forEach(i -> {
			this.pitches.set(i, PitchClassSet.mod(this.pitches.get(i) * mult, PitchClassSet.MODULUS));
		});
	}

	/**
	 * Apply the inversion transformation on this, i.e. for each member n of this, n = (12 - n) mod 12.
	 */
	public void applyI() {
		// TODO Auto-generated method stub
		IntStream.range(0, this.size()).forEach(i -> {
			this.pitches.set(i, PitchClassSet.mod(this.pitches.get(i) * 11, PitchClassSet.MODULUS));
		});
	}

	/**
	 * Apply the transposition and multiplication transformations on this.
	 * Both operations are performed mod 12.
	 * @param transposition  The integer by which the returned IPitchSequence will be transposed.
	 * @param mult  The integer by which the returned IPitchSequence will be multiplied.
	 */
	public void applyTM( int transposition, int mult ) {
		this.applyT(transposition);
		this.applyM(mult);
	}

	/**
	 * Returns a subsequence of this using a start index and a length.
	 * Throws an IllegalArgumentException if start index is less than 0 or the subsequence extends beyond the range of this objects List members.
	 * @param start  The index at which to start the search.
	 * @param length  The length at which to search.
	 */
	public PitchClassSequence subSequence(int start, int length) {
		PitchClassSequence outSeq = new PitchClassSequence();
		// fail fast if start or lentgh are less than 0.
		if ( start < 0 || length < 0) {
			throw new IllegalArgumentException("start and length values must be > 0.");
		}
		// fail if the desired subsequence would include indices larger than the lentgh of this.
		if ( start + length > this.getMembers().size() ) {
			throw new IllegalArgumentException("end index must be less than the length of the list.");
		}
		int index = start;
		while ( index < (start + length) ) {
			outSeq.addPitch( this.getMembers().get(index));
			index++;
		}
		return outSeq;
	}

	/**
	 * Add the members of inSeq at the end of this. This method alters the contents of this.
	 * @param inSeq  The sequence to be appended to the tail if this.
	 */
	public void extend( ISequence inSeq ) {
		this.pitches.addAll( new PitchClassSequence(inSeq.getMembers()).getMembers() );
	}

	/**
	 * Returns true if each member of this is equal to the member of anotherSeq at the same index.
	 * The comparison is performed mod 12, so that an instance of an IPitchSequence will be considered
	 * equal of if the respective members are octave transpositions of each other.
	 * @param anotherSeq  The sequence used for the comparison.
	 * @return boolean value indicating if this equals anotherSeq.
	 */
	public boolean equals( ISequence anotherSeq ) {
		// return false immediately if sizes do not match so that the membership check does not
		// fail on IndexOutOfBoundsException and to not waste time checking on objects with different lengths,
		// which will always be unequal.
		if ( this.pitches.size() != anotherSeq.getMembers().size() ) {
			return false;
		}
		for ( int index = 0; index < this.pitches.size(); index++ ) {
			if ( this.pitches.get(index) % 12 != anotherSeq.getMembers().get(index) % 12) {
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
	public Iterator<PitchClassSequence> transformationIterator() {
		
		final PitchSequence thisSeq = this;
		class TransformationIterator implements Iterator<PitchClassSequence> {

			private int index = 0;
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return ( index <  96 );
			}
			public PitchClassSequence next() {
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
				PitchClassSequence outSeq = new PitchClassSequence( thisSeq );
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
	public List<Integer> getEmbeddedSubsequence( IMutableSequence inSeq ) {
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
	 * Construct a PitchSequence from a String containing Integer tokens separated by a comma and a space ", ".
	 * Example: 12, 43, 73, 9, 34, 2, 121. Extraneous characters are ignored. Elements in the string are converted
	 * to pitch-classes using n % 12 for each n in the member string.
	 * @param pcSeqString  String containing tokens to parse.
	 * @return PitchSequence constructed from String of tokens representing pitches as Integers.
	 */
	public static PitchClassSequence getPitchClassSequenceFromString( String pcSeqString ) {
		List<Integer> pcList = new ArrayList<Integer>();
		for ( Character pc : pcSeqString.toCharArray() ) {
			if ( Character.isDigit(pc) ) {
				pcList.add( new Integer( pc ) );
			}
			else if ( pc.toString().equalsIgnoreCase("a") ) {
				pcList.add( new Integer( 10 ) );
			}
			else if ( pc.toString().equalsIgnoreCase("b") ) {
				pcList.add( new Integer( 11 ) );
			}
			else if ( pc.toString().equalsIgnoreCase("t") ) {
				//System.out.println( "Matches letter: " + pc );
				pcList.add( 10  );
			}
			else if ( pc.toString().equalsIgnoreCase("e") ) {
				//System.out.println( "Matches letter: " + pc );
				pcList.add( 11 );
			}		}
		return new PitchClassSequence( pcList );
	}

	/**
	 * Accepts a String containing a sequence transformation tokens. It should be in the form of {T[n]}{M[n]}{I},
	 * where {} represents optional tokens. For example, "RT[5]M[7]I", or "T[A]I". Warning: The tokens will always be
	 * parsed in canonical order (RTMI) even if they do not appear in that order in the String parameter.
	 * @param transformation  A String containing a sequence transformation tokens.
	 * @return IPitchSequence
	 */
	public PitchClassSequence getTransformationFromStringLegacy(String transformation) {
		PitchClassSequence outSeq = new PitchClassSequence( this );
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
	 * Accepts a PitchClassSequenceTransformation and applies it to a copy of this. While canonical transformations
	 * should be in the form of {T[n]}{M[n]}{I}, this method will parse multiple tokens, applying them
	 * in reverse order.
	 * @param pSeqTrans A String containing a sequence transformation tokens.
	 * @return IPitchSequence
	 */
	public PitchClassSequence fromTransformation(PitchClassSequenceTransformation pSeqTrans) {
		PitchClassSequence outSeq = new PitchClassSequence( this );
		if ( pSeqTrans.isInversion() ) {
			outSeq = outSeq.I();
		}
		outSeq = outSeq.M(pSeqTrans.getMultiplication());
		outSeq = outSeq.T(pSeqTrans.getTransposition());
		if ( pSeqTrans.isRetrograde() ) {
			outSeq = outSeq.R();
		}
		outSeq.setDescriptor(pSeqTrans.toString());
		return outSeq;
	}

	/**
	 * A shorthand alias for fromTransformationString(String transformation).
	 * @param pSeqTrans  A String containing a sequence transformation tokens.
	 * @return IPitchSequence
	 */
	public void apply(PitchClassSequenceTransformation pSeqTrans) {
		if ( pSeqTrans.isInversion() ) {
			this.applyI();
		}
		this.applyM(pSeqTrans.getMultiplication());
		this.applyT(pSeqTrans.getTransposition());
		if ( pSeqTrans.isRetrograde() ) {
			this.applyR();
		}
		this.setDescriptor(pSeqTrans.toString());
	}

	/**
	 * Accepts a String containing a sequence transformation tokens. While canonical transformations
	 * should be in the form of {T[n]}{M[n]}{I}, this method will parse multiple tokens, applying them
	 * in reverse order.
	 * @param transformation  A String containing a sequence transformation tokens.
	 * @return IPitchSequence
	 */
	public PitchClassSequence fromTransformationString(String transformation) {
		PitchClassSequence outSeq = new PitchClassSequence( this );
		PitchClassSequenceTransformation pSeqTrans = new PitchClassSequenceTransformation(transformation);
		if ( pSeqTrans.isInversion() ) {
			outSeq = outSeq.I();
		}
		outSeq = outSeq.M(pSeqTrans.getMultiplication());
		outSeq = outSeq.T(pSeqTrans.getTransposition());
		if ( pSeqTrans.isRetrograde() ) {
			outSeq = outSeq.R();
		}
		outSeq.setDescriptor(pSeqTrans.toString());
		return outSeq;
	}

	/**
	 * A shorthand alias for fromTransformationString(String transformation).
	 * @param transformation  A String containing a sequence transformation tokens.
	 * @return IPitchSequence
	 */
	public PitchClassSequence apply(String transformation) {
		return this.fromTransformationString(transformation);
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
	 * @param inHead A string to use as the start of the String representation of this.
	 * @param inTail A string to use as the end of the String representation of this.
	 * @param inSeparator A string to use as the separator between members in the String representation of this.
	 * 
	 * @return  A String representation of this.
	 */
	public String toString(String inHead, String inTail, String inSeparator) {
		String head = "";
		String tail = "";
		String separator = "";
		
		StringBuffer outputBuf = new StringBuffer();
		outputBuf.append(head);
		int count = 0;
		for ( Integer elem : this.pitches ) {
			outputBuf.append( Integer.toHexString(elem) );
			if ( count < this.pitches.size() - 1 ) {
				outputBuf.append(inSeparator);
			}
			count++;
		}
		outputBuf.append(tail);
		return outputBuf.toString();
	}

	/**
	 * Returns a String representation of this using the default head, tail and separators.
	 * @return  A String representation of this.
	 */
	public String toString() {
		return this.toString(PitchClassSequence.defaultHeadString, PitchClassSequence.defaultTailString, PitchClassSequence.defaultSeparatorString);
	}

	/**
	*  toString for a PitchClassSequence concatenates the members without spaces. Each member
	*  will only occupy one byte, so there is no need for spaces.
	 * @return The String representation of this prepended with its Transformation String. 
	 */
	public String toStringExtended() {
		StringBuffer outputBuf = new StringBuffer();
		outputBuf.append( StringUtils.rightPad( this.getTransformation().toString(), 9) );
		outputBuf.append(": <");
		for ( Integer elem : this.pitches ) {
			outputBuf.append( Integer.toHexString(elem) );
		}
		outputBuf.append(">");
		return outputBuf.toString();
	}
}
