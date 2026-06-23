package com.newscores.setTheory;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;

import com.newscores.setTheory.PitchClassSequence;
import com.newscores.setTheory.PitchClassSet;
import com.newscores.setTheory.PitchSequence;
import com.newscores.setTheory.interfaces.*;

public class TestPitchSequenceTransformation extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testPitchSequenceTransformation() {
		PitchSequenceTransformation tSeq = new PitchSequenceTransformation();
		assert ( tSeq.getTransposition() == PitchSequenceTransformation.DEFAULT_TRANSPOSITION );
		assertEquals( tSeq.getMultiplication(), PitchSequenceTransformation.DEFAULT_MULTIPLICATION );
		assert ( ! tSeq.isInversion() ); // Inversion should default to false.
		assert ( ! tSeq.isRetrograde() ); // Retrograde should default to false.
		tSeq.setInversion(true);
		assert ( tSeq.isInversion() );
		tSeq.setRetrograde(true);
		assert ( tSeq.isRetrograde() );
		tSeq.setTransposition( 1 );
		assertEquals( tSeq.getTransposition(), 1 );
	}

	public void testPitchSequenceFromTransformationString() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		PitchClassSequence pSeq = new PitchClassSequence( Arrays.asList( testArray ) );
		PitchClassSequence anotherSeq = pSeq;
		Iterator<PitchClassSequence> seqIt = pSeq.transformationIterator();
		while ( seqIt.hasNext() ) {
			PitchClassSequence seq = seqIt.next();
			//System.out.println( String.format("Original Seq :: %s : %s", seq.getDescriptor(), seq.toString() ) );
			PitchClassSequence compareSeq = pSeq.fromTransformationString(seq.getDescriptor());
			//System.out.println( String.format("Sequence transformed by %s :: %s : %s", seq.getDescriptor(), compareSeq.getDescriptor(), compareSeq.toString() ) );
			assertTrue(
					String.format("PCSeq %s: %s should be equal a PCSeq transformed by the string %s: %s",
							seq.toString(), seq.getDescriptor(), seq.getDescriptor(), compareSeq.toString()),
					seq.equals(compareSeq));
		}
	}
	
	public void testPitchSequenceFromTransformation() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		PitchClassSequence pSeq = new PitchClassSequence( Arrays.asList( testArray ) );
		PitchClassSequence anotherSeq = pSeq;
		Iterator<PitchClassSequence> seqIt = pSeq.transformationIterator();
		while ( seqIt.hasNext() ) {
			PitchClassSequence seq = seqIt.next();
			System.out.println( String.format("Original Seq :: %s : %s", seq.getDescriptor(), seq.toString() ) );
			PitchClassSequenceTransformation pSeqTrans = new PitchClassSequenceTransformation(seq.getDescriptor());
			PitchClassSequence compareSeq = pSeq.fromTransformation(pSeqTrans);
			System.out.println( String.format("Sequence transformed by %s :: %s : %s", seq.getDescriptor(), compareSeq.getDescriptor(), compareSeq.toString() ) );
			assertTrue(
					String.format("PCSeq %s: %s should be equal a PCSeq transformed by the string %s: %s",
							seq.toString(), seq.getDescriptor(), seq.getDescriptor(), compareSeq.toString()),
					seq.equals(compareSeq));
		}
	}

}
