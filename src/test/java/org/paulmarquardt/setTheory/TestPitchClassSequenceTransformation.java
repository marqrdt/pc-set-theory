package org.paulmarquardt.setTheory;

import org.paulmarquardt.setTheory.utils.SequenceUtils;

import junit.framework.TestCase;

import java.util.*;

import org.paulmarquardt.setTheory.PitchClassSequence;
import org.paulmarquardt.setTheory.PitchClassSequenceTransformation;
import org.paulmarquardt.setTheory.interfaces.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;


public class TestPitchClassSequenceTransformation extends TestCase {

	List<PitchClassSequence> testPSeqs = new ArrayList<PitchClassSequence>();
	String testFileName = "com/newscores/setTheory/TestPitchClassSequenceData.json";
	String testJson;

	protected void setUp() throws Exception {
		super.setUp();
		this.testJson = new String();
		try
		{
			String testFilePath = Thread.currentThread().getContextClassLoader().getResource(testFileName).getFile();
			testJson = new String ( Files.readAllBytes( Paths.get(testFilePath) ) );
			assertTrue("Ensure that JSON data is populated",testJson.length() > 0 );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void testPitchClassSequenceTransformation() {
		PitchClassSequenceTransformation tSeq = new PitchClassSequenceTransformation();
		assert ( tSeq.getTransposition() == PitchClassSequenceTransformation.DEFAULT_TRANSPOSITION );
		assertEquals( tSeq.getMultiplication(), PitchClassSequenceTransformation.DEFAULT_MULTIPLICATION );
		assert ( ! tSeq.isInversion() ); // Inversion should default to false.
		assert ( ! tSeq.isRetrograde() ); // Retrograde should default to false.
		tSeq.setInversion(true);
		assert ( tSeq.isInversion() );
		tSeq.setRetrograde(true);
		assert ( tSeq.isRetrograde() );
		tSeq.setTransposition( 1 );
		assertEquals( tSeq.getTransposition(), 1 );
	}

	public void testPitchClassSequenceTransformationIterator() {
		Iterator<PitchClassSequenceTransformation> pseqTransformIt = PitchClassSequenceTransformation.transformationIterator();
		while( pseqTransformIt.hasNext() ) {
			PitchClassSequenceTransformation pseqTransform = pseqTransformIt.next();
			System.out.println(pseqTransform.toString());
		}
	}

	public void testPitchClassSequenceFromTransformationList() {
		int numSequences = 500;
		String[] transStringList = new String[] { "T[0]", "T[2]M[11]", "RT[3]M[5]", "RT[9]I" };
		System.out.println(String.format("Testing getTransformationFromList", numSequences));
		IntStream.range(0, numSequences).forEach(s -> {
			IntStream.range(0, PitchClassSet.MODULUS).forEach(l -> {
				PitchClassSequence pSeq = SequenceUtils.getRandomPitchClassSequence(l, true);
				Iterator<PitchClassSequence> seqIt = pSeq.transformationIterator();
				while (seqIt.hasNext()) {
					PitchClassSequence seq = seqIt.next();
					//System.out.println( String.format("Original Seq :: %s : %s", seq.getDescriptor(), seq.toString() ) );
					PitchClassSequence compareSeq = pSeq.fromTransformationString(seq.getDescriptor());
					//System.out.println( String.format("Sequence transformed by %s :: %s : %s", seq.getDescriptor(), compareSeq.getDescriptor(), compareSeq.toString() ) );
					assertTrue(
							String.format("PCSeq %s: %s should be equal a PCSeq transformed by the string %s: %s",
									seq.toString(), seq.getDescriptor(), seq.getDescriptor(), compareSeq.toString()),
							seq.equals(compareSeq));
				}
			});
		});
	}

	public void testPitchClassSequenceTransformationInverse() {
		int numSequences = 500;
		String[] transStringList = new String[] { "T[0]", "T[2]M[11]", "RT[3]M[5]", "RT[9]I" };
		System.out.println(String.format("Testing getTransformationInverse", numSequences));
		IntStream.range(0, numSequences).forEach(s -> { //for( int s = 0; s < numSequences; s++) {
			IntStream.range(0, PitchClassSet.MODULUS).forEach(l -> { //for( int l = 0; l <= PitchClassSet.MODULUS; l++)  {
				PitchClassSequence pSeq = SequenceUtils.getRandomPitchClassSequence(l, true);
				PitchClassSequence compareSeq = new PitchClassSequence(pSeq);
				//System.out.println(String.format("pSeq: %s :: compareSeq: %s", pSeq.toString(), compareSeq.toString()));
				Iterator<PitchClassSequence> seqIt = pSeq.transformationIterator();
				Iterator<PitchClassSequenceTransformation> pseqTransformIt = PitchClassSequenceTransformation.transformationIterator();
				while (pseqTransformIt.hasNext()) {
					PitchClassSequenceTransformation transform = pseqTransformIt.next();
					assertTrue(
							String.format("The Inverse of a Transformation %s of PSeq %s is equal to %s",
									transform.toString(), pSeq.toString(), pSeq.toString()),
							compareSeq.equals( pSeq.fromTransformation(transform).fromTransformation(transform.inverse())));
					//System.out.println(
					//		String.format("Original Seq: %s :: Inverse of Transformation %s: %s = %s: %s",
					//				pSeq.toString(), transform.toString(), pSeq.fromTransformation(transform).toString(),
					//				transform.inverse().toString(), pSeq.fromTransformation(transform.inverse()).fromTransformation(transform).toString()));
				}
			});
		});
	}

	public void testPitchClassSequenceTransformationR() {
		int numSequences = 1000;
		System.out.println(String.format("Testing %d unique random PitchClassSequences", numSequences));
		IntStream.rangeClosed( 0, numSequences).forEach( s -> {
			IntStream.range( 0, PitchSet.MODULUS ).forEach( t -> {
				PitchClassSequence seq = SequenceUtils.getRandomPitchClassSequence(PitchSet.MODULUS, true);
				String transString = String.format(String.format("RT[%d]", t));
				PitchClassSequenceTransformation trans = new PitchClassSequenceTransformation(transString);
				PitchClassSequence compareSeq = seq.fromTransformation(trans);
				assertTrue( String.format("Retrograde of PCSeq %s should be equal a PCSeq transformed by the string %s", seq.toString(), transString),
						//compareSeq.equals( seq.T(t)).R() );
						compareSeq.equals(seq.T(t).R()));
			});
		});
		System.out.println(String.format("Testing %d non-unique random PitchClassSequences", numSequences));
		IntStream.rangeClosed( 0, numSequences).forEach( s -> {
			IntStream.range( 0, PitchSet.MODULUS * 2 ).forEach( t -> {
				PitchClassSequence seq = SequenceUtils.getRandomPitchClassSequence(24, false);
				String transString = String.format(String.format("RT[%d]", t));
				PitchClassSequenceTransformation trans = new PitchClassSequenceTransformation(transString);
				PitchClassSequence compareSeq = seq.fromTransformation(trans);
				assertTrue( String.format("Retrograde of PCSeq %s should be equal a PCSeq transformed by the string %s", seq.toString(), transString),
						//compareSeq.equals( seq.T(t)).R() );
						compareSeq.equals( seq.T(t).R()));
			});
		});
	}

	public void testPitchClassSequenceFromTransformationString() {
		int numSequences = 500;
		System.out.println(String.format("Testing %d unique random PitchClassSequences", numSequences));
		IntStream.rangeClosed( 0, numSequences).forEach( s -> {
			IntStream.range( 0, PitchClassSet.MODULUS).forEach( l -> {
				PitchClassSequence pSeq = SequenceUtils.getRandomPitchClassSequence(l, true);
				Iterator<PitchClassSequence> seqIt = pSeq.transformationIterator();
				while (seqIt.hasNext()) {
					PitchClassSequence seq = seqIt.next();
					//System.out.println( String.format("Original Seq :: %s : %s", seq.getDescriptor(), seq.toString() ) );
					PitchClassSequence compareSeq = pSeq.fromTransformationString(seq.getDescriptor());
					//System.out.println( String.format("Sequence transformed by %s :: %s : %s", seq.getDescriptor(), compareSeq.getDescriptor(), compareSeq.toString() ) );
					assertTrue(
							String.format("PCSeq %s: %s should be equal a PCSeq transformed by the string %s: %s",
									seq.toString(), seq.getDescriptor(), seq.getDescriptor(), compareSeq.toString()),
							seq.equals(compareSeq));
				}
			});
		});
		System.out.println(String.format("Testing %d non-unique random PitchClassSequences", numSequences));
		IntStream.rangeClosed( 0, numSequences).forEach( s -> {
			IntStream.range( 0, PitchClassSet.MODULUS * 2).forEach( l -> {
				PitchClassSequence pSeq = SequenceUtils.getRandomPitchClassSequence(l, false);
				Iterator<PitchClassSequence> seqIt = pSeq.transformationIterator();
				while (seqIt.hasNext()) {
					PitchClassSequence seq = seqIt.next();
					//System.out.println( String.format("Original Seq :: %s : %s", seq.getDescriptor(), seq.toString() ) );
					PitchClassSequence compareSeq = pSeq.fromTransformationString(seq.getDescriptor());
					//System.out.println( String.format("Sequence transformed by %s :: %s : %s", seq.getDescriptor(), compareSeq.getDescriptor(), compareSeq.toString() ) );
					assertTrue(
							String.format("PCSeq %s: %s should be equal a PCSeq transformed by the string %s: %s",
									seq.toString(), seq.getDescriptor(), seq.getDescriptor(), compareSeq.toString()),
							seq.equals(compareSeq));
				}
			});
		});
	}
	
	public void testPitchClassSequenceFromTransformation() {
		int numSequences = 500;
		System.out.println(String.format("Testing %d unique random PitchClassSequences", numSequences));
		IntStream.rangeClosed( 0, numSequences).forEach( s -> {
			IntStream.range( 0, PitchClassSet.MODULUS).forEach( l -> {
				PitchClassSequence pSeq = SequenceUtils.getRandomPitchClassSequence(l, true);
				Iterator<PitchClassSequence> seqIt = pSeq.transformationIterator();
				while (seqIt.hasNext()) {
					PitchClassSequence seq = seqIt.next();
					//System.out.println( String.format("Original Seq :: %s : %s", seq.getDescriptor(), seq.toString() ) );
					PitchClassSequenceTransformation pSeqTrans = new PitchClassSequenceTransformation(seq.getDescriptor());
					PitchClassSequence compareSeq = pSeq.fromTransformation(pSeqTrans);
					//System.out.println( String.format("Sequence transformed by %s :: %s : %s", seq.getDescriptor(), compareSeq.getDescriptor(), compareSeq.toString() ) );
					assertTrue(
							String.format("PCSeq %s: %s should be equal a PCSeq transformed by the string %s: %s",
									seq.toString(), seq.getDescriptor(), seq.getDescriptor(), compareSeq.toString()),
							seq.equals(compareSeq));
				}
			});
		});
	}

	/*
	public static PitchClassSequenceTransformation getTransformationFromList( List<String> inTransStringList ) {
		PitchClassSequenceTransformation outTrans = new PitchClassSequenceTransformation();
		// Use Streams to iterate through the List in reverse order.
		inTransStringList.stream()
				.collect(Collectors.toCollection(LinkedList::new))
				.descendingIterator()
				.forEachRemaining( outTrans:: applyTransformationString);
		return outTrans;
	}
	*/
}
