package org.paulmarquardt.setTheory.interfaces;

import java.util.List;
import java.util.Map;

import org.paulmarquardt.setTheory.SequenceIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
//import com.fasterxml.jackson.annotation.*;

/**
 * The Sequence Interface defines a contract for a sequence, or an ordered set of
 * integers such that a member of the sequence can be said to come before or after another member.
 * @author marqrdt
 *
 */

@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "@type"
)
@JsonTypeIdResolver(SequenceIdResolver.class)
@JsonPropertyOrder({ "name", "members", "transformation", "descriptor" })
public interface ISequence extends IPitchCollection {
		
	/**
	 * Returns the transposition of this, i.e. n is added to each element of this.
	 * @param transposition The value to be added to each element.
	 * @return IPitchSequence  The transposition of this.
	 */
	public ISequence T( int transposition );

	/**
	 * In a sense, the reverse of T(n). Return an IPitchSequence transposed so that the first element is start.
	 * @param transposition The value to which the first element of the returned ISequence will be transposed.
	 * @return IPitchSequence  The transposition of this.
	 */
	public ISequence transposeTo( int transposition );

	/**
	 * Returns the retrograde of this, i.e. the order of members is reversed.
	 * @return IPitchSequence  The retrograde of this.
	 */
	public ISequence R();
		
	/**
	 * Returns the inversion transformation of this, i.e. the pitch of each member is inverted around an axis.
	 * Implementations of this interface may have different ways of determining the axis. 
	 * @return IPitchSequence  The inversion transformation of this.
	 */
	public ISequence I();
	
	/**
	 * Returns the multiplication transformation of this, i.e. the pitch of each member is multiplied by mult.
	 * Implementations in Pitch Class space will most likely perform operations mod 12. 
	 * @param mult The int by which each member will be multiplied. For most PitchClass based operations, the common values are 1, 5, 7, 11.
	 * @return IPitchSequence  The multiplication transformation of this.
	 */
	public ISequence M(int mult);

	/**
	 * Return a subset of this of length length and starting at index index.
	 * @param start The index in this at which the subSequence should start.
	 * @param length  The length of the sub-sequence to return.
	 * @return The IPitchSequence starting at start with length length.
	 */
	public ISequence subSequence(int start, int length);
	
	/**
	 * Return the number of members in this.
	 * @return  The number of members in this.
	 */
	public int length();
	
	/**
	 * Return a List containing the members of this.
	 * @return  The members of this.
	 */
	//public List<Integer> getMembers();

	/**
	 * Return a boolean value indicating if this is equal to anotherSequence.
	 * Implementations of ISequence could define equality differently.
	 * @param anotherSequence  The ISequence to compare to.
	 * @return boolean  True if this equals anotherSequence, false if not.
	 */
	public boolean equals( ISequence anotherSequence );

	/**
	 * getEmbeddedSubsequence accepts an IPitchSequence inSeq. If the caller contains inSeq as an embedded subsequence, the method will
	 * return the indices in the caller's members that form inSeq. Example:
	 * IPitchSequence newSeq = new PitchClassSequence( [0,b,1,2,a,8,5,3,9,4,7,6] )
	 * IPitchSequence subSeq = new PitchClassSequence( [b,2,8,3] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = [1,3,5,7]
	 * IPitchSequence anotherSubSeq = new PitchClassSequence( [0,4,7,6] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = [0,9,10,11]
	 * 
	 * If the caller does not contain the subSequence, it will return null
	 * IPitchSequence notASubSeq = new PitchClassSequence( [6,4,3,8] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = null
	 * @param anotherSequence IPitchSequence  The IPitchSequence used for the operation.
	 * @return  A list of indices at which members of anotherPitchSequence can be found in this.
	 */
	public List<Integer> getEmbeddedSubsequence( ISequence anotherSequence );
	
	/**
	 * Returns a new PitchSequence with inSeq appended to this.
	 * This method is created to support the "+" operator in a Groovy DSL.
	 * @param  inSeq The PitchSequence to be appended to this.
	 * @return  A new PitchSequence with inSeq appended to this.
	 */
	public ISequence plus( ISequence inSeq );

	/**
	 * Sets a text name for this. Could be something like "My favorite sequence of pitches" or "Berg Violin Concerto row".
	 * @param name  The descriptor to set.
	 */
	public void setName( String name );

	/**
	 * Returns the text name for this.
	 * @return String  The text name for this.
	 */
	public String getName();

	/**
	 * Sets a text descriptor for this. Could be something like "T[9]I" or "T[4]M[7]". This distinct from the name
	 * so that users can maintain a custom name for the object while also storing transformation data.
	 * @param descriptor  The descriptor to set.
	 */
	public void setDescriptor( String descriptor );

	/**
	 * Returns the text descriptor for this.
	 * @return String  The text descriptor for this.
	 */
	public String getDescriptor();
			
}
