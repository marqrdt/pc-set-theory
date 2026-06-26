package org.paulmarquardt.setTheory;

import org.paulmarquardt.setTheory.utils.SequenceUtils;
import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;

import org.paulmarquardt.setTheory.PitchClassSequence;
import org.paulmarquardt.setTheory.PitchClassSet;
import org.paulmarquardt.setTheory.PitchSequence;
import org.paulmarquardt.setTheory.interfaces.*;

import javax.sound.midi.Sequencer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class TestPitchClassSequence extends TestCase {

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

	public void testPitchSequence() {
		PitchClassSequence pSeq = new PitchClassSequence();
		assert pSeq.pitches.size() == 0;
	}

	public void testPitchSequenceIntegerArray() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		PitchClassSequence pSeq = new PitchClassSequence( testArray );
		assert pSeq.pitches.size() == testArray.length;
		int index = 0;
		for ( int pitch : pSeq.getMembers() ) {
			assertTrue( "Sequence members must match array members", pitch == testArray[ index ] );
			index++;
		}
	}

	public void testPitchClassSequenceCollectionOfInteger() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		List<Integer> testList = new ArrayList<Integer>( Arrays.asList( testArray ) );
		PitchClassSequence pSeq = new PitchClassSequence( testList );		
		assert pSeq.pitches.size() == testArray.length;
	}

	public void testAddPitch() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		List<Integer> testList = new ArrayList<Integer>( Arrays.asList( testArray ) );
		PitchClassSequence pSeq = new PitchClassSequence( testList );
		pSeq.addPitch( 1 );
		assert pSeq.pitches.size() == testArray.length + 1;
	}
	
	public void testPitchClassSequenceGetMembers() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		PitchClassSequence pSeq = new PitchClassSequence( testArray );
		assert pSeq.pitches.size() == pSeq.getMembers().size();
	}

	public void testPitchSequenceT() {
		int minSize = 2;
		int maxSize = 100;
		int minPitch = 0;
		int maxPitch = 120;
		int transpositions = 12;
		IntStream.rangeClosed(minSize, maxSize).forEach(size -> {
			for (int trans = 0; trans < transpositions; trans++) {
				PitchClassSequence pSeq = new PitchClassSequence( SequenceUtils.getRandomPitchSequence(size, minPitch, maxPitch, false) );
				PitchClassSequence transformedSeq = (PitchClassSequence) pSeq.T(trans);
				for (int element = 0; element < transformedSeq.length(); element++) {
					assertTrue("Elements of transposed set are correct", transformedSeq.getMembers().get(element) == (pSeq.getMembers().get(element) + trans) % 12);
				}
			}
		});
	}

	public void testPitchClassSequenceR() {
		int minSize = 2;
		int maxSize = 100;
		int minPitch = 0;
		int maxPitch = 120;
		int transpositions = 12;
		IntStream.rangeClosed(minSize, maxSize).forEach(size -> {
			PitchClassSequence pSeq = new PitchClassSequence( SequenceUtils.getRandomPitchSequence(size, minPitch, maxPitch, false) );
			PitchClassSequence transformedSeq = (PitchClassSequence) pSeq.R();
			for (int element = 0; element < transformedSeq.length(); element++) {
				assertTrue( "Elements of retrograde set are correct", transformedSeq.getMembers().get(element) == pSeq.getMembers().get( pSeq.length() - 1 - element ) );
			}
		});
	}

	public void testPitchClassSequenceI() {
		/*
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		PitchClassSequence pSeq = new PitchClassSequence( testArray );
		PitchClassSequence transformedSeq = (PitchClassSequence) pSeq.I();
		for ( int element = 0; element < testArray.length; element++ ) {
			assertTrue( "Elements of inverted set are correct", transformedSeq.getMembers().get(element) == (PitchClassSet.MODULUS - testArray[ element]) % 12);
		}
		 */
		int minSize = 2;
		int maxSize = 100;
		int minPitch = 0;
		int maxPitch = 120;
		int transpositions = 12;
		IntStream.rangeClosed(minSize, maxSize).forEach(size -> {
			PitchClassSequence pSeq = new PitchClassSequence( SequenceUtils.getRandomPitchSequence(size, minPitch, maxPitch, false) );
			PitchClassSequence transformedSeq = pSeq.I();
			IntStream.range(0, pSeq.length()).forEach( element -> {
				assertTrue(String.format("Elements of inverted set are correct: %d + %d = 12 given sequence %s", transformedSeq.getMembers().get(element), pSeq.getMembers().get(element), pSeq.toStringExtended()),
						PitchClassSet.mod( transformedSeq.getMembers().get(element) + pSeq.getMembers().get(element), PitchClassSet.MODULUS ) == PitchClassSet.mod(PitchClassSet.MODULUS, 12 ) );
			});
		});
	}

	public void testPitchClassSequenceM() {
		/*
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		for ( int mult = 0; mult < transpositions; mult ++ ) {
			PitchClassSequence pSeq = new PitchClassSequence( testArray );
			PitchClassSequence transformedSeq = (PitchClassSequence) pSeq.M(mult);
			for ( int element = 0; element < testArray.length; element++ ) {
				assertTrue( "Elements of transposed set are correct", transformedSeq.getMembers().get(element) == (testArray[element] * mult) % 12 );
			}
		}
		*/
		int minSize = 2;
		int maxSize = 100;
		int minPitch = 0;
		int maxPitch = 120;
		int transpositions = 12;
		IntStream.rangeClosed(minSize, maxSize).forEach(size -> {
			IntStream.rangeClosed(1, 12).forEach( mult -> {
				PitchClassSequence pSeq = new PitchClassSequence( SequenceUtils.getRandomPitchSequence(size, minPitch, maxPitch, false) );
				PitchClassSequence transformedSeq = (PitchClassSequence) pSeq.M(mult);
				for (int element = 0; element < transformedSeq.length(); element++) {
					assertTrue("Elements of multiplied set are correct", transformedSeq.getMembers().get(element) == ( pSeq.getMembers().get(element) * mult ) % PitchClassSet.MODULUS );
				}
			});
		});
	}
	
	public void testPitchSequenceEquals() {
		int minSize = 2;
		int maxSize = 100;
		int minPitch = 0;
		int maxPitch = 120;
		int transpositions = 12;
		IntStream.rangeClosed(minSize, maxSize).forEach(size -> {
			PitchClassSequence pSeq = new PitchClassSequence( SequenceUtils.getRandomPitchSequence(size, minPitch, maxPitch, false) );
			PitchClassSequence copiedSeq = new PitchClassSequence( pSeq.getMembers() );
			assertTrue( "PitchClassSequence copied from members of another is equal to this", pSeq.equals(copiedSeq) );
			copiedSeq = new PitchClassSequence( pSeq );
			assertTrue( "PitchClassSequence copied from another Sequence type is equal to this", pSeq.equals(copiedSeq) );
		});
		Integer[] emptyArray = {};
		PitchClassSequence emptySeq = new PitchClassSequence( emptyArray );
		assertTrue( "Empty PitchSequence equals() another equivalent PitchSequence returns true", emptySeq.equals( new PitchSequence(emptyArray) ) );
	}

	public void testPitchClassSequenceEquvalent() {
		int minSize = 2;
		int maxSize = 100;
		int minPitch = 0;
		int maxPitch = 120;
		int transpositions = 12;
		IntStream.rangeClosed(minSize, maxSize).forEach(size -> {
			PitchClassSequence pSeq = new PitchClassSequence(SequenceUtils.getRandomPitchSequence(size, minPitch, maxPitch, false));
			IntStream.range(0, PitchClassSet.MODULUS).forEach(trans -> {
				PitchClassSequence transposedSeq = pSeq.T(trans);
				assertTrue(String.format("PitchClassSequence.T(%d) is equivalent to this", trans), pSeq.equivalent(transposedSeq));
				assertTrue(String.format("PitchClassSequence.T(%d).I() is equivalent to this", trans), pSeq.equivalent(transposedSeq.I()));
				assertTrue(String.format("PitchClassSequence.T(%d).R() to is equivalent to this", trans), pSeq.equivalent(transposedSeq.R()));
				assertTrue(String.format("PitchClassSequence.T(%d).I().R() is equivalent to this", trans), pSeq.equivalent(transposedSeq.I().R()));
				assertTrue(String.format("PitchClassSequence.T(%d).I().R() is equivalent to this", trans), pSeq.equivalent(transposedSeq.R().I()));
			});
		});
		IntStream.rangeClosed(minSize + 1, maxSize).forEach(size -> {
			PitchClassSequence pSeq = new PitchClassSequence(SequenceUtils.getRandomPitchSequence(size, minPitch, maxPitch, false));
			IntStream.range(0, PitchClassSet.MODULUS).forEach(trans -> {
				PitchClassSequence pSeqTransposed = pSeq.T(trans);
				List<Integer> members = pSeq.getMembers();
				// There should be no case where a PC Sequence with its first and last elements transposed by 1 and 2 respectively, would be equivalent.
				members.set(0, members.get(0) + 1);
				members.set(members.size() - 1, members.get( members.size() - 1) - 2);
				PitchClassSequence mutatedSeq = new PitchClassSequence( members );
				assertFalse(String.format("PitchClassSequence %s with elements modified to %s is not equivalent under transposition", pSeq.toStringExtended(), mutatedSeq.toStringExtended()), pSeqTransposed.equivalent(mutatedSeq));
				assertFalse(String.format("PitchClassSequence %s with elements modified to %s is not equivalent under inversion", pSeq.toStringExtended(), mutatedSeq.toStringExtended()), pSeqTransposed.equivalent(mutatedSeq.I()));
				assertFalse(String.format("PitchClassSequence %s with elements modified to %s is not equivalent under retrograde", pSeq.toStringExtended(), mutatedSeq.toStringExtended()), pSeqTransposed.equivalent(mutatedSeq.R()));
				assertFalse(String.format("PitchClassSequence %s with elements modified to %s is not equivalent under retrograde inversion", pSeq.toStringExtended(), mutatedSeq.toStringExtended()), pSeqTransposed.equivalent(mutatedSeq.I().R()));
			});
		});
		Integer[] emptyArray = {};
		PitchClassSequence emptySeq = new PitchClassSequence( emptyArray );
		assertTrue( "Empty PitchSequence is equivalent to another empty PitchSequence", emptySeq.equals( new PitchSequence(emptyArray) ) );
	}

	public void testPitchClassSequencePcVector() {
		Integer[] testArray = { 0,1,2,3,4,5,6,7,8,9,10,11 };
		PitchClassSequence pSeq = new PitchClassSequence( testArray );
		Integer[] expectedVector = { 1,1,1,1,1,1,1,1,1,1,1,1 };
		assertTrue( "A PcVector for a 12-note Row equals [1,1,1,1,1,1,1,1,1,1,1,1]", Arrays.equals( pSeq.pcVector(), expectedVector ) );
		Integer[] testArray2 = { 0,2,4,6,8,10 };
		pSeq = new PitchClassSequence( testArray2 );
		Integer[] expectedVector2 = { 1,0,1,0,1,0,1,0,1,0,1,0 };
		assertTrue( "A PcVector for a 12-note Row equals [1,0,1,0,1,0,1,0,1,0,1,0]", Arrays.equals( pSeq.pcVector(), expectedVector2 ) );
	}

	public void testPitchClassSequenceIntervals() {
		Integer[] testArray = {  9,10,3,11,4,6,0,1,7,8,2,5 };
		PitchClassSequence pSeq = new PitchClassSequence( testArray );
		int distance = 1;
		Integer[] expectedVector1 = { 1,5,8,5,2,6,1,6,1,6,3 };
		assertEquals( "A intervals for a 12-note Row equals [1,5,8,5,2,6,1,6,1,6,3]", pSeq.intervals(distance), Arrays.asList(expectedVector1) );
		distance += 1;
		Integer[] expectedVector2 = { 6,1,1,7,8,7,7,7,7,9 };
		assertEquals( "A intervals for a 12-note Row equals [6,1,1,7,8,7,7,6,7,9]", pSeq.intervals(distance), Arrays.asList(expectedVector2) );
		distance += 1;
		Integer[] expectedVector3 = { 2,6,3,1,9,1,8,1,10 };
		assertEquals( "A intervals for a 12-note Row equals [2,6,3,1,9,1,8,1,10]", pSeq.intervals(distance), Arrays.asList(expectedVector3) );
		distance += 1;
		Integer[] expectedVector4 = { 7,8,9,2,3,2,2,4 };
		assertEquals( "A intervals for a 12-note Row equals [7,8,9,2,3,2,2,4]", pSeq.intervals(distance), Arrays.asList(expectedVector4) );
	}

	public void testGetEmbeddedSubsequence() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		// test with a valid sub-sequence
		Integer[] subSeqArray = { 4,3,5 };
		PitchClassSequence pSeq = new PitchClassSequence( testArray );
		PitchClassSequence subSeq = new PitchClassSequence( subSeqArray );
		List<Integer> indices =  pSeq.getEmbeddedSubsequence( subSeq );
		assertNotNull( indices );
		assertEquals( indices.size(), subSeq.length() );
		// test with a sub-sequence equal to the original. Should return a list of indices equal to the size of the original
		subSeq = new PitchClassSequence( pSeq );
		indices =  pSeq.getEmbeddedSubsequence( subSeq );
		assertNotNull( indices );
		assertEquals( indices.size(), subSeq.length() );
		assertEquals( indices.size(), pSeq.length() );
		// test with an invalid sub-sequence, i.e. the sub-sequence is not contained in the original.
		Integer[] invalidSubSeqArray = { 5,3,4 };
		PitchClassSequence invalidSeq = new PitchClassSequence( invalidSubSeqArray );
		indices =  pSeq.getEmbeddedSubsequence( invalidSeq );
		assertNull( indices );
	}

	public void testPitchClassSequenceFromString() {
		String pcString = "{ 0,4,6,3,2,5,8 }";
		Integer[] pcArray = { 0,4,6,3,2,5,8 };
		assertTrue( new PitchClassSequence( pcString ).equals( new PitchClassSequence( pcArray ) ) );
		pcString = "{ 0,B,6,3,2,5,A }";
		Integer[] pcArrayWithLetters = new Integer[]{ 0,11,6,3,2,5,10 };
		assertTrue( new PitchClassSequence( pcString ).equals( new PitchClassSequence( pcArrayWithLetters ) ) );

		// Test with 't' and 'e'
		pcString = "{ 0,e,6,3,2,5,t }";
		assertTrue( new PitchClassSequence( pcString ).equals( new PitchClassSequence( pcArrayWithLetters ) ) );

		// Test with 't' and 'e' mixed with 'a' and 'b'
		pcString = "{ 0,e,6,3,2,5,A }";
		assertTrue( new PitchClassSequence( pcString ).equals( new PitchClassSequence( pcArrayWithLetters ) ) );

		// Test with 't' and 'e' mixed with 'a' and 'b'
		pcString = "{ 0,b,6,3,2,5,t }";
		assertTrue( new PitchClassSequence( pcString ).equals( new PitchClassSequence( pcArrayWithLetters ) ) );
	}

	// A PitchClassSequence pcSeq created from its own string representation should equal itself.
	public void testPitchClassSequenceStringConversions() {
		Integer[] pcArray1 = { 0,4,6,3,2,5,8 };
		PitchClassSequence testSeq1 = new PitchClassSequence( pcArray1 );
		Iterator<PitchClassSequence> pcSeqIt = testSeq1.transformationIterator();
		while ( pcSeqIt.hasNext() ) {
			PitchClassSequence pcSeq = (PitchClassSequence) pcSeqIt.next();
			assertTrue( pcSeq.equals( PitchClassSequence.getPitchClassSequenceFromString( pcSeq.toString() ) ) );
		}
		Integer[] pcArray2 = { 0, 11, 6, 3, 2, 5, 10 };
		PitchClassSequence testSeq2 = new PitchClassSequence( pcArray2 );
		pcSeqIt = testSeq2.transformationIterator();
		while ( pcSeqIt.hasNext() ) {
			PitchClassSequence pcSeq = (PitchClassSequence) pcSeqIt.next();
			assertTrue( pcSeq.equals( PitchClassSequence.getPitchClassSequenceFromString( pcSeq.toString() ) ) );
		}
	}

	public void testPitchClassSequenceFromTransformationString() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		String transformation;
		PitchClassSequence pSeq = new PitchClassSequence( testArray );
		PitchClassSequence transformedSeq = (PitchClassSequence) pSeq.I();
		/*
		for ( int element = 0; element < testArray.length; element++ ) {
			transformation = String.format("T[%d]", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
				pSeq.T(element), transformation, pSeq.fromTransformationString(transformation) ), pSeq.T(element).equals( pSeq.getTransformationFromString(transformation) ) );
			transformation = String.format("RT[%d]", element);

			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.T(element).R(), transformation, pSeq.fromTransformationString(transformation) ), pSeq.T(element).R().equals( pSeq.getTransformationFromString(transformation) ) );

			transformation = String.format("T[%d]I", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.I().T(element), transformation, pSeq.fromTransformationString(transformation) ), pSeq.T(element).I().equals( pSeq.getTransformationFromString(transformation) ) );

			transformation = String.format("RT[%d]I", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.I().T(element).R(), transformation, pSeq.fromTransformationString(transformation) ), pSeq.I().T(element).R().equals( pSeq.getTransformationFromString(transformation) ) );

			transformation = String.format("T[%d]M[7]", element);
			System.out.println( String.format("transformation string: %s", transformation));
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.M(7).T(element), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.M(7).T(element).equals( pSeq.getTransformationFromString(transformation) ) );

			transformation = String.format("RT[%d]M[7]", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.M(7).T(element).R(), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.M(7).T(element).R().equals( pSeq.getTransformationFromString(transformation) ) );

			transformation = String.format("T[%d]M[5]", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.M(5).T(element), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.M(5).T(element).equals( pSeq.getTransformationFromString(transformation) ) );

		}
		*/
	}

	public void testPitchClassSequenceTransformationIterator() {
		Integer[] testArray = { 0,4,6,3,2,5,8,1,7 };
		PitchClassSequence pSeq = new PitchClassSequence( testArray );
		Iterator<PitchClassSequence> seqIt = pSeq.transformationIterator();
		while ( seqIt.hasNext() ) {
			ISequence seq = seqIt.next();
			System.out.println( String.format("%s: %s", seq.getDescriptor(), seq.toString() ) );
		}
	}

	public void testPitchClassSequenceSubSequence() {
		Integer[] testArray = { 0,4,6,3,2,5,8,1,7 };
		PitchClassSequence pSeq = new PitchClassSequence( testArray );
		// Boundary tests
		int boundary = 0;
		ISequence subSeq  = pSeq.subSequence(0, boundary);
		assertTrue(String.format("PitchSequence length is %d, expected length is %d elements",
				subSeq.length(), boundary ), subSeq.length() == boundary );
		boundary = pSeq.length();
		subSeq  = pSeq.subSequence(0, boundary);
		assertTrue(String.format("PitchSequence length is %d, expected length is %d elements",
				subSeq.length(), boundary ), subSeq.length() == boundary );
		// Manual test cases
		subSeq  = pSeq.subSequence( 1, 3 );
		assertTrue(String.format("PitchSequence %s should equal %s",
				subSeq.toString(), new PitchSequence( new Integer[] {4,6,3} ) ), subSeq.equals(new PitchClassSequence( new Integer[] {4,6,3} ) ) );
		subSeq  = pSeq.subSequence( 2,4 );
		assertTrue(String.format("PitchSequence %s should equal %s",
				subSeq.toString(), new PitchSequence( new Integer[] {6,3,2,5} ) ), subSeq.equals(new PitchClassSequence( new Integer[] {6,3,2,5} ) ) );
		subSeq  = pSeq.subSequence( 4,5 );
		assertTrue(String.format("PitchSequence %s should equal %s",
				subSeq.toString(), new PitchSequence( new Integer[] {2,5,8,1,7} ) ), subSeq.equals(new PitchClassSequence( new Integer[] {2,5,8,1,7} ) ) );
		for ( int index = 0; index < testArray.length - 1; index ++ ) {
			for ( int jndex = 0; jndex < testArray.length - index; jndex ++ ) {
				subSeq =  pSeq.subSequence(index, jndex);
				ISequence expected = new PitchClassSequence( Arrays.asList( testArray ).subList(index, index + jndex) );
				//System.out.println(String.format("Sequence: %s, Expected: %s", subSeq, expected ) );
				assertTrue(String.format("PitchSequence %s should equal %s", subSeq, expected ), subSeq.equals( expected ) );
			}
		}
		for ( int index = 0; index < testArray.length - 1; index ++ ) {
			ISequence firstSeq =  pSeq.subSequence(0, index);
			ISequence secondSeq = pSeq.subSequence(index, pSeq.length() - index );
			System.out.println(String.format("First seq: %s, Second seq: %s", firstSeq, secondSeq ) );
			assertTrue(String.format("Sum of PitchSequence length %d and %d should equal the size of the original set %d",
					firstSeq.length(),  secondSeq.length(), pSeq.length() ), firstSeq.length() + secondSeq.length() == pSeq.length() );
		}
	}

}
