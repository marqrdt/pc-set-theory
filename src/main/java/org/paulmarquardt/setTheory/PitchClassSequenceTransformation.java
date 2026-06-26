package org.paulmarquardt.setTheory;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import org.paulmarquardt.setTheory.interfaces.ISequenceTransformation;
import org.apache.commons.lang3.StringUtils;

/**
 * A PitchClassSequenceTransformation packages a transformation of a PitchClassSequence as a stateful object.
 * An PitchClassSequenceTransformation applied to a IPitchSequence describes a unique transformation of the object,
 * with the capability to modify the T(), M(), I() or R() properties of the sequence. An IPitchSequence transformed
 * with a PitchClassSequenceTransformation can be guaranteed to have its transformations RT[n]M[n]I.
 * applied in canonical order.
 * @author marqrdt
 *
 */
public class PitchClassSequenceTransformation implements ISequenceTransformation {

	private int transposition = 0;
	private boolean inversion = false;
	private boolean retrograde = false;
	private int multiplication = 1;
	public static final int MAX_TRANSFORMATION_LENGTH = 12;
	public static final int DEFAULT_TRANSPOSITION = 0;
	public static final boolean DEFAULT_INVERSION = false;
	public static final int DEFAULT_MULTIPLICATION = 1;
	public static final boolean DEFAULT_RETROGRADE = false;
	//private static final String transformationPatternString = "(R|T\\[[0-9]+|[a-f].\\]*|M\\[[0-9]+|[a-f].\\]*|I)";
	//private static final String transposePatternString = "T\\[*([0-9]+|[a-f].)\\]";
	//private static final String multPatternString = "M\\[([0-9]+|[a-f].)\\]";
	private static final String transformationPatternString = "(R|T\\[[0-9|a-b]+\\]*|M\\[[0-9|a-b]+\\]*|I)";
	private static final String transposePatternString = "T\\[*([0-9|a-b]+)\\]";
	private static final String multPatternString = "M\\[([0-9|a-b]+)\\]";

	
	/**
	 * Construct a default PitchClassSequenceTransformation. Default values are T = 0, Inversion = false, Retrograde = false, M = 1.
	 */
	public PitchClassSequenceTransformation() {
		this.transposition = PitchClassSequenceTransformation.DEFAULT_TRANSPOSITION;
		this.inversion = PitchClassSequenceTransformation.DEFAULT_INVERSION;
		this.retrograde = PitchClassSequenceTransformation.DEFAULT_RETROGRADE;
		this.multiplication = PitchClassSequenceTransformation.DEFAULT_MULTIPLICATION;
	}

	/**
	 * Construct a PitchClassSequenceTransformation from values passed in.
	 * A PitchClassSequenceTransformation can be used as a standard for describing transformation of a PitchSequence.
	 * An IPitchSequence transformed with a PitchClassSequenceTransformation can be guaranteed to have its transformations RT[n]M[n]I.
	 * applied in canonical order.
	 * @param inTrans The transposition value.
	 * @param inInversion The inversion switch. Determines whether to apply I().
	 * @param inRetrograde The retrograde switch. Determines whether to apply R().
	 * @param inMult The value for the M transformation.
	 */
	public PitchClassSequenceTransformation(int inTrans, boolean inInversion, boolean inRetrograde, int inMult ) {
		this.transposition = inTrans;
		this.inversion = inInversion;
		this.retrograde = inRetrograde;
		this.multiplication = inMult;
	}

	/**
	 * Construct a PitchClassSequenceTransformation from another PitchClassSequenceTransformation.
	 * A PitchClassSequenceTransformation can be used as a standard for describing transformation of a PitchSequence.
	 * An IPitchSequence transformed with a PitchClassSequenceTransformation can be guaranteed to have its transformations T[n]M[n]IR
	 * applied in canonical order.
	 * @param inPSeqTransformation The PitchClassSequenceTransformation from which this will be created.
	 */
	public PitchClassSequenceTransformation(PitchClassSequenceTransformation inPSeqTransformation ) {
		this( inPSeqTransformation.getTransposition(), inPSeqTransformation.isInversion(),
				inPSeqTransformation.isRetrograde(), inPSeqTransformation.getMultiplication() );
	}

	/**
	 * Construct a PitchClassSequenceTransformation. Default values are T = 0, Inversion = false, Retrograde = false, M = 1.
	 * A PitchClassSequenceTransformation can be used as a standard for describing transformation of a PitchSequence.
	 * An IPitchSequence transformed with a PitchClassSequenceTransformation can be guaranteed to have its transformations RT[n]M[n]I
	 * applied in canonical order.
	 * @param inTrans The transposition value.
	 * @param inInversion The inversion switch. Determines whether to apply I().
	 * @param inRetrograde The retrograde switch. Determines whether to apply R().
	 */
	public PitchClassSequenceTransformation(int inTrans, boolean inInversion, boolean inRetrograde ) {
		this( inTrans, inInversion, inRetrograde, 1 );
	}

	/**
	 * Construct a default PitchClassSequenceTransformation created from a String representation of a transformation,
	 * for example "RT[5]I" or "T[10]M[7]
	 * An IPitchSequence transformed with a PitchClassSequenceTransformation can be guaranteed to have its transformations T[n]M[n}IR
	 * applied in canonical order.
	 * @param transformationString Create a PitchClassSequenceTransformation from the String.
	 */
	public PitchClassSequenceTransformation(String transformationString ) {
		this.applyTransformationString(transformationString);
	}


	/**
	 * Return the integer value of the transposition.
	 * @return  The transposition value of this.
	 */
	public int getTransposition() {
		return this.transposition;
	}
	
	/**
	 * Set the integer value of the transposition.
	 * @param transposition The transposition value to set.
	 */
	public void setTransposition(int transposition) {
		this.transposition = PitchClassSet.mod(transposition, PitchClassSet.MODULUS);
	}
	
	/**
	 * Return boolean indicating if this has inversion set.
	 * @return  The inversion flag of this.
	 */
	public boolean isInversion() {
		return this.inversion;
	}
	
	/**
	 * Sets the inversion flag, i.e. whether to apply the I() operation.
	 * @param inversion  The inversion value to set.
	 */
	public void setInversion(boolean inversion) {
		this.inversion = inversion;
	}
	
	/**
	 * Returns a new PitchClassSequenceTransformation with the T() value transposed by trans.
	 * @param trans The value by which the returned PitchClassSequenceTransformation will be transposed.
	 * @return A copy of this transposed by trans.
	 */
	public PitchClassSequenceTransformation T(int trans ) {
		PitchClassSequenceTransformation pSeqTrans = new PitchClassSequenceTransformation(this.getTransposition() + trans, this.isInversion(), this.isRetrograde(), this.getMultiplication() );
		return pSeqTrans;
	}

	/**
	 * Returns a new PitchClassSequenceTransformation with the negation of the inversion of this.
	 * @return A copy of this with the negation of the inversion of this.
	 */
	public PitchClassSequenceTransformation I() {
		PitchClassSequenceTransformation pSeqTrans = new PitchClassSequenceTransformation(this.getTransposition(), ! this.isInversion(), this.isRetrograde(), this.getMultiplication() );
		return pSeqTrans;
	}

	/**
	 * Returns a new PitchClassSequenceTransformation with the negation of the retrograde of this.
	 * @return A copy of this with the negation of the inversion of this.
	 */
	public PitchClassSequenceTransformation R() {
		PitchClassSequenceTransformation pSeqTrans = new PitchClassSequenceTransformation(this.getTransposition(), this.isInversion(), ! this.isRetrograde(), this.getMultiplication() );
		return pSeqTrans;
	}

	/**
	 * Returns a new PitchClassSequenceTransformation with the M() value multiplied by mult.
	 * @param mult The value by which the returned PitchClassSequenceTransformation will be multiplied.
	 * @return A copy of this multiplied by mult.
	 */
	public PitchClassSequenceTransformation M(int mult ) {
		PitchClassSequenceTransformation pSeqTrans = new PitchClassSequenceTransformation(this.getTransposition(), this.isInversion(), this.isRetrograde(), this.getMultiplication() * mult );
		return pSeqTrans;
	}

	/**
	 * Whether the R() operation will be applied.
	 * @return  The value of the retrograde flag.
	 */
	public boolean isRetrograde() {
		return this.retrograde;
	}
	
	/**
	 * Sets the retrograde flag, i.e. whether to apply the R() operation.
	 * @param retrograde  The retrograde value to set.
	 */
	public void setRetrograde(boolean retrograde) {
		this.retrograde = retrograde;
	}
	
	/**
	 * Return the integer value of the multiplication.
	 * @return  The multiplication value of this.
	 */
	public int getMultiplication() {
		return this.multiplication;
	}

	/**
	 * Set the integer value of the multiplication.
	 * @param inMult The multiplication value to set.
	 */	
	public void setMultiplication( int inMult ) {
		this.multiplication = inMult % PitchClassSet.MODULUS;
	}

	/**
	 * Creates and returns a new PitchClassSequenceTransformation which is the Inverse of this.
	 * An Inverse operation "undoes" and operation. They are described in
	 * "Composition with Pitch Classes" (Morris), Chap. 4 pp.127-128.
	 * @return A new PitchClassSequenceTransformation which is the Inverse operation on this.
	 */
	public PitchClassSequenceTransformation inverse() {
		PitchClassSequenceTransformation outTrans = new PitchClassSequenceTransformation();
		outTrans.setTransposition( -1 * this.getMultiplication() * this.getTransposition() );
		outTrans.setMultiplication( this.getMultiplication() );

		if ( this.isRetrograde() ) {
			outTrans.setRetrograde(true);
		}
		return outTrans;
	}

	/**
	 * Returns whether this is equal to the PitchClassSequenceTransformation inTrans. Equality is defined by
	 * all non-static fields (transposition, multiplication, inversion, retrograde) are equal.
	 * @return  A boolean value returning true if all field of this are equal to the fields of inTrans.
	 * @param inTrans A PitchClassSequenceTransformation for the comparison.
	 */
	public boolean equals( PitchClassSequenceTransformation inTrans ) {
		return ( this.getTransposition() == inTrans.getTransposition() ) &&
				( this.getMultiplication() == inTrans.getMultiplication() ) &&
				( this.isInversion() == inTrans.isInversion() ) &&
				( this.isRetrograde() == inTrans.isRetrograde() );
	}

	/**
	 * Returns an Iterator that iterates through every PitchClassSequenceTransformation with T[n]M[n]R.
	 * Includes all 96 T, M[n] for n in[1,5,7,11] and R transformations. I() transformations are effected using M[11].
	 * @return An Iterator that iterates through every transformation of this.
	 */
	public static Iterator<PitchClassSequenceTransformation> transformationIterator() {

		class TransformationIterator implements Iterator<PitchClassSequenceTransformation> {
			private int index = 0;
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return ( index <  96 );
			}
			public PitchClassSequenceTransformation next() {
				// TODO Auto-generated method stub
				StringBuffer transformationBuffer = new StringBuffer();
				PitchClassSequenceTransformation outTransformation = new PitchClassSequenceTransformation();
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
				if ( iIndex % 2 == 1 ) {
					retrograde = true;
					outTransformation.setRetrograde(true);
					//seq = seq.R();
				} else {
					outTransformation.setRetrograde(false);
				}
				//transformationBuffer.append( String.format("T[%d]", tIndex) );
				outTransformation.setTransposition(tIndex);
				if ( (iIndex >> 1) == 2) {
					mult = 5;
					outTransformation.setMultiplication(5);
					//seq = seq.M( 5 );
				}
				if ( (iIndex >> 1) == 3) {
					mult = 7;
					outTransformation.setMultiplication(7);
					//seq = seq.M( 7 );
				}
				if ( (iIndex >> 1) == 1) {
					inversion = true;
					//transformationBuffer.append( String.format("I") );
					outTransformation.setMultiplication(11);
					//seq = seq.I();
				}
				//seq = seq.T(tIndex);
				/*
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
				 */
				index++;
				return outTransformation;
			}

			public void remove() {
				// TODO Auto-generated method stub
			}
		}
		return new TransformationIterator();
	}

	/**
	 * Given a String representing a transformation in canonical form,
	 * return a PitchClassSequenceTransformation that models the transformation string.
	 * Currently, the transformation string must be in canonical form T[n]M[n]I.
	 * @return  A PitchClassSequenceTransformation from a canonical transformation string.
	 * @param inTrans A transformation string in canonical form.
	 */
	public static PitchClassSequenceTransformation getTransformationFromString(String inTrans ) {
		PitchClassSequenceTransformation outTrans = new PitchClassSequenceTransformation();
		outTrans.applyTransformationString(inTrans);
		return outTrans;
	}
	
	public static PitchClassSequenceTransformation getTransformationFromList(List<String> inTransStringList ) {
		PitchClassSequenceTransformation outTrans = new PitchClassSequenceTransformation();
		// Use Streams to iterate through the List in reverse order.
		inTransStringList.stream()
			.collect(Collectors.toCollection(LinkedList::new))
			.descendingIterator()
			.forEachRemaining( outTrans:: applyTransformationString);
		return outTrans;
	}

	/**
	 * Given a PitchClassSequenceTransformation,
	 * transform this by the transformation. This method modifies the caller.
	 * Currently, the transformation string must be in canonical form T[n]M[n]I.
	 */
	public void applyTransformation( PitchClassSequenceTransformation inTrans ) {
		this.setTransposition( this.getTransposition() + inTrans.getTransposition() );
		this.setMultiplication( this.getMultiplication() * inTrans.getMultiplication() );
		if ( inTrans.isInversion() ) {
			this.setMultiplication( this.getMultiplication() * (PitchClassSet.MODULUS - 1) );
		}
		if ( inTrans.isRetrograde() ) {
			this.setRetrograde( ! this.isRetrograde() );
		}
	}

	/**
	 * Given a String representing a transformation in canonical form,
	 * transform this by the transformation string. This method modifies the caller.
	 * Currently, the transformation string must be in canonical form T[n]M[n]I.
	 */
	public void applyTransformationString( String inTrans ) {
		Pattern transformationPattern = Pattern.compile( this.transformationPatternString, Pattern.CASE_INSENSITIVE);
		Matcher patternMatcher = transformationPattern.matcher(inTrans);
		int groupCount = 1;
		String remaining = inTrans;
		boolean hasMatch = patternMatcher.find();
		//String token = "";
		List<String> tokens = new ArrayList<String>();
		while ( hasMatch ) {
		    String token = patternMatcher.group(1);
			if ( token != null ) {
				remaining = StringUtils.strip( remaining.substring( token.length() ) );
				// Add them in reverse order of appearance, as they're processed from the end.
				tokens.add(0, token);
			} 
			else if ( remaining.length() > 0) {
				remaining = StringUtils.strip( remaining.substring( 1 ) );
			}
			//println "remaining string is ${remaining}"
		    patternMatcher = transformationPattern.matcher(remaining);
		    hasMatch = patternMatcher.lookingAt();
			if ( remaining.length() == 0) {
				hasMatch = false;
			}
		}
		for ( String token : tokens ) {
			Pattern transposePattern = Pattern.compile(transposePatternString, Pattern.CASE_INSENSITIVE);
			Matcher transposeMatcher = transposePattern.matcher(token);
			Pattern multPattern = Pattern.compile(multPatternString, Pattern.CASE_INSENSITIVE);
			Matcher multMatcher = multPattern.matcher(token);
			Pattern hexPattern = Pattern.compile("[a-f].", Pattern.CASE_INSENSITIVE);
			if ( transposeMatcher.find() ) {;
				int t;
				// Ugly code alert...
				Matcher hexMatcher = hexPattern.matcher(token);
				// If the token contains any hex characters, parse it as a Hex string
				if (hexMatcher.matches()) {
					t = Integer.parseInt(transposeMatcher.group(1), 16);
				}
				// If not, it should only contain decimal numbers, so parse as a decimal.
				else {
					t = Integer.parseInt(transposeMatcher.group(1));
				}
				this.setTransposition( this.getTransposition() + t );
				//System.out.println( String.format("token %s is a transposition token at T[%s]", token, t ));
			} else if ( multMatcher.find() ) {
				int m;
				// Ugly code alert...
				Matcher hexMatcher = hexPattern.matcher(token);
				// If the token contains any hex characters, parse it as a Hex string
				if (hexMatcher.matches()) {
					m = Integer.parseInt(multMatcher.group(1), 16);
				}
				// If not, it should only contain decimal numbers, so parse as a decimal.
				else {
					m = Integer.parseInt(multMatcher.group(1));
				}
				//System.out.println( String.format("token %s is a multiplication token at M[%s]", token, m ));
				this.setMultiplication( this.getMultiplication() * m );
			// setting the boolean value of retrograde just undoes whatever is the current value.
			} else if ( token.toLowerCase().startsWith("r") ) {
				this.setRetrograde( ! this.isRetrograde() );
				//System.out.println(String.format("token %s is a retrograde", token));
			// setting the boolean value of inversion just undoes whatever is the current value.
			} else if ( token.toLowerCase().startsWith("i") ) {
				this.setInversion( ! this.isInversion() );
				//System.out.println(String.format("token %s is an inversion", token));
			} else {
				throw new IllegalArgumentException( String.format("Unable to parse transformation token %s", token));
			}
		}
	}

	/**
	 * Given a List of PitchClassSequenceTransformations, apply this to each PitchClassSequenceTransformation in the list.
	 * return a List of PitchClassSequenceTransformations that transforms  by the transformation string.
	 * Currently, the transformation string must be in canonical form T[n]M[n]I.
	 * @return  A PitchClassSequenceTransformation from a canonical transformation string.
	 * @param inTransList A transformation string in canonical form.
	 */
	public List<PitchClassSequenceTransformation> performTransformationOnList(List<PitchClassSequenceTransformation> inTransList ) {
		List<PitchClassSequenceTransformation> outPseqTransformList = new ArrayList<PitchClassSequenceTransformation>();
		inTransList.stream().forEach( pseqTransform -> {
			PitchClassSequenceTransformation pSeqTrans = new PitchClassSequenceTransformation(this);
			pSeqTrans.applyTransformation(pseqTransform);
			outPseqTransformList.add(pSeqTrans);
		});
		return outPseqTransformList;
	}

	/**
	 * Return a String value representing the transformation in canonical form. Used in conjunction with
	 * the getTransformationFromString method, it can be used to return set transformations based on the
	 * canonical form T[n]M[n]I.
	 * @return  A String representing the canonical transformation of this 
	 */
	public String toString() {
		StringBuffer outBuf = new StringBuffer();
		if ( this.isRetrograde() ) {
			outBuf.append("R");
		}
		outBuf.append( String.format("T[%s]", Integer.toHexString( this.getTransposition() ).toUpperCase() ) );
		if ( this.getMultiplication() > 1 ) {
			outBuf.append( String.format("M[%s]", Integer.toHexString( this.getMultiplication() ).toUpperCase() ) );
		}
		if ( this.isInversion() ) {
			outBuf.append("I");
		}
		return outBuf.toString();
	}
}
