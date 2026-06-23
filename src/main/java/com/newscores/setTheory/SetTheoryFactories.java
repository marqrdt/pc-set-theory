package com.newscores.setTheory;

import com.newscores.setTheory.*;
import com.newscores.setTheory.interfaces.*;

import java.util.*;
import java.util.stream.IntStream;

/*
 * These static methods are used to comply with Inversion of Control and Dependency Injection patterns.
 * They make it much easier to implement the practice of "coding to an Interface".
 * These creation methods offer a formal "long" form name along with a shorter, more concise name useful in
 * DSLs, such as Groovy, etc.

 */
public class SetTheoryFactories {

	public static PitchSet getPitchSet() {
		return new PitchSet();
	}

	public static PitchSet pSet() {
		return new PitchSet();
	}

	public static PitchSet pSet( Integer[] inArray ) {
		return new PitchSet( inArray );
	}

	public static PitchSet pSet( List<Integer> inList ) {
		return new PitchSet();
	}

	public static PitchClassSet getPitchClassSet() {
		return new PitchClassSet();
	}

	public static PitchClassSet pcSet() {
		return new PitchClassSet();
	}

	public static PitchClassSet getPitchClassSet( Integer[] inArray ) {
		return new PitchClassSet( inArray );
	}

	public static PitchClassSet pcSet( Integer[] inArray ) {
		return new PitchClassSet( inArray );
	}

	public static PitchClassSet getPitchClassSet( List<Integer> inList ) {
		return new PitchClassSet( inList );
	}

	public static PitchClassSet pcSet( List<Integer> inList ) {
		return new PitchClassSet( inList );
	}

	public static PitchClassSet getPitchClassSet( IPitchCollection inColl ) {
		return new PitchClassSet( inColl.getMembers() );
	}

	public static PitchClassSet pcSet( IPitchCollection inColl ) {
		return new PitchClassSet( inColl.getMembers() );
	}

	public static PitchSequence getPitchSequence() {
		return new PitchSequence();
	}

	public static PitchSequence pSeq() {
		return new PitchSequence();
	}

	public static PitchSequence getPitchSequence( ISequence pSeq ) {
		return new PitchSequence( pSeq );
	}

	public static PitchSequence pSeq( ISequence pSeq ) {
		return new PitchSequence( pSeq );
	}

	public static PitchSequence getPitchSequence( List<Integer> pitchList ) {
		return new PitchSequence( pitchList );
	}

	public static PitchSequence pSeq( List<Integer> pitchList ) {
		return new PitchSequence( pitchList );
	}

	public static PitchSequence pSeq( String pitchListString ) {
		return new PitchSequence( pitchListString );
	}

	public static PitchClassSequence getPitchClassSequence() {
		return new PitchClassSequence();
	}

	public static PitchClassSequence pcSeq() {
		return new PitchClassSequence();
	}

	public static PitchClassSequence getPitchClassSequence( ISequence pSeq ) {
		return new PitchClassSequence( pSeq.getMembers() );
	}

	public static PitchClassSequence pcSeq( ISequence pSeq ) {
		return new PitchClassSequence( pSeq.getMembers() );
	}

	public static PitchClassSequence pcSeq( String pcSeqString ) {
		return new PitchClassSequence( pcSeqString );
	}

	public static PitchClassSequence getPitchClassSequence( Integer[] inArray ) {
		return new PitchClassSequence( inArray );
	}

	public static PitchClassSequence pcSeq( Integer[] inArray ) {
		return new PitchClassSequence( inArray );
	}

	public static PitchClassSequence getPitchClassSequence( List<Integer> pitchList ) {
		return new PitchClassSequence( pitchList );
	}

	public static PitchClassSequence pcSeq( List<Integer> pitchList ) {
		return new PitchClassSequence( pitchList );
	}

	public static Row getRow( Integer[] elements ) {
		return new Row ( elements );
	}
        
	public static Row row( Integer[] elements ) {
		return new Row ( elements );
	}

	public static Row getRow( List<Integer> pitchList ) {
		return new Row ( pitchList );
	}
        
	public static Row row( List<Integer> pitchList ) {
		return new Row ( pitchList );
	}

	public static Row getRow( ISequence elements ) {
		return new Row ( elements );
	}

	public static Row row( ISequence elements) {
		return new Row ( elements );
	}

	public static Row row( String rowString) {
		return new Row ( rowString );
	}

	public static Scale getScale( Integer[] elements, int lowestNote, int highestNote ) {
		return new Scale ( elements, lowestNote, highestNote );
	}
        
	public static Scale scale( Integer[] elements, int lowestNote, int highestNote ) {
		return new Scale ( elements, lowestNote, highestNote );
	}

	public static Scale getScale( Integer[] elements, int startingNote ) {
		return new Scale ( elements, startingNote );
	}
        
	public static Scale scale( Integer[] elements, int startingNote ) {
		return new Scale ( elements, startingNote );
	}

	public static Scale getScale( List<Integer> elementList, int lowestNote, int highestNote ) {
		return new Scale ( elementList, lowestNote, highestNote );
	}
        
	public static Scale scale( List<Integer> elementList, int lowestNote, int highestNote ) {
		return new Scale ( elementList, lowestNote, highestNote );
	}

	public static Scale getScale( List<Integer> elementList, int startingNote ) {
		return new Scale ( elementList, startingNote );
	}
        
	public static Scale scale( List<Integer> elementList, int startingNote ) {
		return new Scale ( elementList, startingNote );
	}
	
	public static Scale getScale( IPitchCollection pSet ) {
		return new Scale ( pSet );
	}
        
	public static Scale scale( IPitchCollection pSet ) {
		return new Scale ( pSet );
	}

	public static Contour getContour() {
		return new Contour ();
	}
        
	public static Contour contour() {
		return new Contour ();
	}
	
	public static Contour getContour( ISequence inSeq ) {
		return new Contour ();
	}
        
	public static Contour contour( ISequence inSeq ) {
		return new Contour ( inSeq.getMembers() );
	}
	
	public static Contour getContour( Integer[] inArray ) {
		return new Contour ( Arrays.asList( inArray ) );
	}
        
	public static Contour contour( Integer[] inArray ) {
		return new Contour ( Arrays.asList( inArray ) );
	}
	
	public static Contour getContour( List<Integer> elementList ) {
		return new Contour ( elementList );
	}
        
	public static Contour contour( List<Integer> elementList ) {
		return new Contour ( elementList );
	}
	
	public static CompositionMatrix getCompositionMatrix() {
		return new CompositionMatrix();
	}

	/*
		Wraps a List&lt;PitchSequence&gt; as a single-row CM with the sequences as columns.
		This can be very useful in applying a single transformation, or a list of transformations
		to the sequences using the .apply() method of the CompositionMatrix class
		@param seqList A List of IPitchSequence instances used to creates the CM columns.
	*/
	public static CompositionMatrix getCompositionMatrix( List<PitchSequence> seqList ) {
		CompositionMatrix outCM = new CompositionMatrix();
		IntStream.range(0, seqList.size() ).forEach( index -> {
			outCM.addSegment( seqList.get(index), 0, index );
		});
		return outCM;
	}

	/*
    Wraps a PitchSequence as a single-row CM with one row and one column.
    This can be very useful in applying a single transformation or a list of
    transformations to the sequences using the .apply() method of the CompositionMatrix class
    @param seqList A List of IPitchSequence instances used to creates the CM columns.
	*/
	public static CompositionMatrix getCompositionMatrix( PitchSequence inSequence ) {
		CompositionMatrix outCM = new CompositionMatrix();
		outCM.addSegment(inSequence, 0, 0);
		return outCM;
	}
}
