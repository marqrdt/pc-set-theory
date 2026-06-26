package org.paulmarquardt.setTheory.interfaces;


/**
 * The IMutablePitchSequence Interface defines a contract for a mutable PitchSequence. A Pitch sequence is an ordered set of
 * pitches such that a member of the sequence can be said to come before or after another member. The members
 * are pitches such that a member can be said to be higher, lower or the same pitch as another member, as opposed
 * to pitch-classes. The IMutableSequence interface extends ISequence, adding the addPitch, addSequenceAtIndex and plus methods.
 * @author marqrdt
 *
 */
public interface IMutableSequence extends ISequence {
	
	/**
	 * Add a pitch to this.
	 * @param pitch Integer  pitch to add to this.
	 */
	public void addPitch( Integer pitch );

	/**
	 * Inserts the members of inSeq at index. This alters the content of this.
	 * @param inSeq  The IPitchSequence to insert at index
	 * @param index  The index at which to insert inSeq
	*/
	public void addSequenceAtIndex( ISequence inSeq, int index );
	
	/**
	 * Inserts the members of inSeq at index. This alters the content of this.
	 * @param inSeq  The IPitchSequence to insert at index
	*/
	public void addSequence( ISequence inSeq );
	
	/**
	 * Add the members of inSeq at the end of this. This method alters the contents of this.
	 * @param inSeq  The sequence to be appended to the tail if this.
	 */
	public void extend( ISequence inSeq );
	
}
