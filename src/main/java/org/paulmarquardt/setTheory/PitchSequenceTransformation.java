package com.newscores.setTheory;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.*;
import java.util.stream.Collectors;

import com.newscores.setTheory.interfaces.ISequenceTransformation;
import org.apache.commons.lang3.StringUtils;

/**
 * A PitchSequenceTransformation packages a transformation of a PitchSequence. An PitchSequenceTransformation
 * applied to a IPitchSequence describes a unique transformation of the object. An IPitchSequence transformed
 * with a PitchSequenceTransformation can be guaranteed to have its transformations T[n]M[n}IR
 * applied in canonical order.
 * @author marqrdt
 *
 */
public class PitchSequenceTransformation implements ISequenceTransformation {

	private int transposition = 0;
	private boolean inversion = false;
	private boolean retrograde = false;
	private int multiplication = 1;
	public static final int MAX_TRANSFORMATION_LENGTH = 12;
	public static final int DEFAULT_TRANSPOSITION = 0;
	public static final boolean DEFAULT_INVERSION = false;
	public static final int DEFAULT_MULTIPLICATION = 1;
	public static final boolean DEFAULT_RETROGRADE = false;
	private static final String transformationPatternString = "(R|T\\[*[0-9]+\\]*|M\\[*[0-9]+\\]*|I)";
	private static final String transposePatternString = "T\\[*([0-9a-f]+)\\]";
	private static final String multPatternString = "M\\[*([0-9a-f]+)\\]";

	
	/**
	 * Construct a default PitchSequenceTransformation. Default values are T = 0, Inversion = false, Retrograde = false, M = 1.
	 */
	public PitchSequenceTransformation() {
		this.transposition = PitchSequenceTransformation.DEFAULT_TRANSPOSITION;
		this.inversion = PitchSequenceTransformation.DEFAULT_INVERSION;
		this.retrograde = PitchSequenceTransformation.DEFAULT_RETROGRADE;
		this.multiplication = PitchSequenceTransformation.DEFAULT_MULTIPLICATION;
	}

	/**
	 * Construct a default PitchSequenceTransformation. Default values are T = 0, Inversion = false, Retrograde = false, M = 1.
	 * A PitchSequenceTransformation can be used as a standard for describing transformation of a PitchSequence.
	 * An IPitchSequence transformed with a PitchSequenceTransformation can be guaranteed to have its transformations T[n]M[n}IR
	 * applied in canonical order.
	 * @param inTrans The transposition value.
	 * @param inInversion The inversion switch. Determines whether to apply I().
	 * @param inRetrograde The retrograde switch. Determines whether to apply R().
	 * @param inMult The value for the M transformation.
	 */
	public PitchSequenceTransformation( int inTrans, boolean inInversion, boolean inRetrograde, int inMult ) {
		this.transposition = inTrans;
		this.inversion = inInversion;
		this.retrograde = inRetrograde;
		this.multiplication = inMult;
	}

	/**
	 * Construct a default PitchSequenceTransformation. Default values are T = 0, Inversion = false, Retrograde = false, M = 1.
	 * A PitchSequenceTransformation can be used as a standard for describing transformation of a PitchSequence.
	 * An IPitchSequence transformed with a PitchSequenceTransformation can be guaranteed to have its transformations T[n]M[n}IR
	 * applied in canonical order.
	 * @param inTrans The transposition value.
	 * @param inInversion The inversion switch. Determines whether to apply I().
	 * @param inRetrograde The retrograde switch. Determines whether to apply R().
	 */
	public PitchSequenceTransformation( int inTrans, boolean inInversion, boolean inRetrograde ) {
		this( inTrans, inInversion, inRetrograde, 1 );
	}

	/**
	 * Construct a default PitchSequenceTransformation created from a String representation of a transformation,
	 * for example "RT[5]I" or "T[10]M[7]
	 * An IPitchSequence transformed with a PitchSequenceTransformation can be guaranteed to have its transformations T[n]M[n}IR
	 * applied in canonical order.
	 * @param transformationString Create a PitchSequenceTransformation from the String.
	 */
	public PitchSequenceTransformation( String transformationString ) {
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
		this.transposition = transposition % PitchClassSet.MODULUS;
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
	 * Returns a new PitchSequenceTransformation with the T() value transposed by trans.
	 * @param trans The value by which the returned PitchSequenceTransformation will be transposed. 
	 * @return A copy of this transposed by trans.
	 */
	public PitchSequenceTransformation T( int trans ) {
		PitchSequenceTransformation pSeqTrans = new PitchSequenceTransformation(this.getTransposition() + trans, this.isInversion(), this.isRetrograde(), this.getMultiplication() );
		return pSeqTrans;
	}

	/**
	 * Returns a new PitchSequenceTransformation with the negation of the inversion of this.
	 * @return A copy of this with the negation of the inversion of this.
	 */
	public PitchSequenceTransformation I() {
		PitchSequenceTransformation pSeqTrans = new PitchSequenceTransformation(this.getTransposition(), ! this.isInversion(), this.isRetrograde(), this.getMultiplication() );
		return pSeqTrans;
	}

	/**
	 * Returns a new PitchSequenceTransformation with the negation of the retrograde of this.
	 * @return A copy of this with the negation of the inversion of this.
	 */
	public PitchSequenceTransformation R() {
		PitchSequenceTransformation pSeqTrans = new PitchSequenceTransformation(this.getTransposition(), this.isInversion(), ! this.isRetrograde(), this.getMultiplication() );
		return pSeqTrans;
	}

	/**
	 * Returns a new PitchSequenceTransformation with the M() value multiplied by mult.
	 * @param mult The value by which the returned PitchSequenceTransformation will be multiplied. 
	 * @return A copy of this multiplied by mult.
	 */
	public PitchSequenceTransformation M( int mult ) {
		PitchSequenceTransformation pSeqTrans = new PitchSequenceTransformation(this.getTransposition(), this.isInversion(), this.isRetrograde(), this.getMultiplication() * mult );
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
	 * Returns whether this is equal to the PitchSequenceTransformation inTrans. Equality is defined by 
	 * all non-static fields (transposition, multiplication, inversion, retrograde) are equal.
	 * @return  A boolean value returning true if all field of this are equal to the fields of inTrans.
	 * @param inTrans A PitchSequenceTransformation for the comparison.
	 */
	public boolean equals( PitchSequenceTransformation inTrans ) {
		return ( this.getTransposition() == inTrans.getTransposition() ) &&
				( this.getMultiplication() == inTrans.getMultiplication() ) &&
				( this.isInversion() == inTrans.isInversion() ) &&
				( this.isRetrograde() == inTrans.isRetrograde() );
	}

	/**
	 * Given a String representing a transformation in canonical form,
	 * return a PitchSequenceTransformation that models the transformation string.
	 * Currently, the transformation string must be in canonical form T[n]M[n]I.
	 * @return  A PitchSequenceTransformation from a canonical transformation string.
	 * @param inTrans A transformation string in canonical form.
	 */
	public static PitchSequenceTransformation getTransformationFromString( String inTrans ) {
		PitchSequenceTransformation outTrans = new PitchSequenceTransformation();
		return outTrans.applyTransformationString(inTrans);
	}
	
	public static PitchSequenceTransformation getTransformationFromString( List<String> inTransStringList ) {
		PitchSequenceTransformation outTrans = new PitchSequenceTransformation();
		// Use Streams to iterate through the List in reverse order.
		inTransStringList.stream()
			.collect(Collectors.toCollection(LinkedList::new))
			.descendingIterator()
			.forEachRemaining( outTrans:: applyTransformationString);
		return outTrans;
	}

	public PitchSequenceTransformation applyTransformationString( String inTrans ) {
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
			Pattern hexPattern = Pattern.compile("[a-f]+", Pattern.CASE_INSENSITIVE);
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
		return this;
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
