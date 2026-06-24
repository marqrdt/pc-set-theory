package com.newscores.setTheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.newscores.setTheory.interfaces.*;

public class Row extends ImmutablePitchClassSequence implements ISequence {

	/**
	 * Constructs a Row from an array of Integer. Throws an IllegalArgumentException if the set of pcs in the array
	 * either has duplicates or does not complete an aggregate, i.e. does not contain all 12 pcs.
	 * @param pitchArray  An Array of Integer from which to construct the Row.
	 */
	public Row( Integer[] pitchArray ) {
		this( Arrays.asList( pitchArray ) );
	}

	/**
	 * Create a new Row from a String representing pitch-class tokens.
	 * A Row is an ordered Set of pitch classes 0-11 representing each of the pitches of the chromatic scale.
	 * A Row must contain each pitch-class exactly once and will throw an IllegalArgumentException if this
	 * constraint is violated.
	 * @param pitchString A String containing the pitches from which to build the PitchClassSequence. Each member of this String will be converted to its PitchClass equivalent.
	 */
	public Row( String pitchString ) {
		super(pitchString);
		//PitchClassSequence pcSeq = new PitchClassSequence(pitchString);
		PitchClassSet pcSet = new PitchClassSet( this );
		if ( ! pcSet.equals(PitchClassSet.aggregate()) ) {
			throw new IllegalArgumentException(String.format("PitchClass Set from %s does not represent an aggregate", pitchString));
		}
		PitchClassSequence rowPcSeq = new PitchClassSequence( pitchString );
		if ( rowPcSeq.size() !=  PitchClassSet.aggregate().size() ) {
			throw new IllegalArgumentException(String.format("Got %d elements. Number of elements in %s must equal %d", pcSet.size(), pitchString, PitchClassSet.MODULUS));
		}
	}

	/**
	 * Constructs a Row from an Collection of Integer. Throws an IllegalArgumentException if the set of pcs in the Collection
	 * either has duplicates or does not complete an aggregate, i.e. does not contain all 12 pcs.
	 * @param pitchesAsList  A List of Integer from which to construct the Row.
	 */
	public Row( Collection<Integer> pitchesAsList ) throws IllegalArgumentException {
		super( pitchesAsList );
		if ( this.pitches.size() != PitchClassSet.MODULUS ) {
			throw new IllegalArgumentException( String.format("A Row must contain exactly %d elements", PitchClassSet.MODULUS ) );
		}
		PitchClassSequence rowPcSeq = new PitchClassSequence( this.pitches );
		if ( rowPcSeq.size() !=  PitchClassSet.aggregate().size() ) {
			throw new IllegalArgumentException( String.format("A Row must contain exactly %d elements and not contain any pitch class repetitions", PitchClassSet.MODULUS ) );
		}
	}
	
	/**
	 * Constructs a Row from an IPitchSequence. Throws an IllegalArgumentException if the set of pcs in the IPitchSequence
	 * either has duplicates or does not complete an aggregate, i.e. does not contain all 12 pcs.
	 * @param pitchSequence  An IPitchSequence from which to construct the Row.
	 */
	public Row( ISequence pitchSequence ) {
		this( pitchSequence.getMembers() );
		this.setDescriptor( pitchSequence.getDescriptor() );
	}
	
	public Row T( int transposition ) {
		return new Row( super.T( transposition) );
	}

	public Row M( int mult ) {
		return new Row( super.M( mult) );
	}
	
	public Row I() {
		return new Row( super.I() );
	}

	public Row R() {
		return new Row( super.R() );
	}

	public Row O( int o ) {
		return new Row( super.O( o ) );
	}


}
