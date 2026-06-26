package org.paulmarquardt.setTheory;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;

import org.paulmarquardt.setTheory.PitchClassSequence;
import org.paulmarquardt.setTheory.PitchClassSet;
import org.paulmarquardt.setTheory.PitchSequence;
import org.paulmarquardt.setTheory.interfaces.*;

public class TestImmutablePitchClassSequence extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testPitchSequence() {
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence();
		assert pSeq.pitches.size() == 0;
	}

	public void testPitchSequenceIntegerArray() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
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
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testList );		
		assert pSeq.pitches.size() == testArray.length;
	}
	
	public void testPitchClassSequenceGetMembers() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
		assertTrue( String.format("The size() method returns the same size as the size of internal pitches List: %d", pSeq.getMembers().size()), pSeq.getMembers().size() == pSeq.getMembers().size() );
		assertTrue( String.format("The size() method returns the same size as the size of the array from which it is created: %d", testArray.length), pSeq.getMembers().size() == testArray.length );		
	}

	public void testPitchClassSequenceTestLengthAndSize() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
		assertTrue( String.format("The size() method returns the same size as the size of List from getMembers(): %d", pSeq.getMembers().size()), pSeq.size() == testArray.length );
		assertTrue( String.format("The size() method returns the same size as the size of the array from which it is created: %d", testArray.length), pSeq.size() == testArray.length );		
		assertTrue( String.format("The length() method returns the same size (%d) as the size of List from getMembers(): %d", pSeq.length(), pSeq.getMembers().size()), pSeq.length() == pSeq.getMembers().size() );
		assertTrue( String.format("The length() method returns the same size as the size of the array from which it is created: %d", testArray.length), pSeq.length() == testArray.length );		
		assertTrue( String.format("The size() and length() methods returns the same values: size() = %d, length() = %d", pSeq.size(), pSeq.length()), pSeq.size() == pSeq.length());
		assertTrue( String.format("The size() method returns the same size as the size of the array from which it is created: %d", testArray.length), pSeq.size() == testArray.length );		
	}

	
	public void testPitchSequenceT() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		for ( int trans = 0; trans < transpositions; trans ++ ) {
			ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
			ImmutablePitchClassSequence transformedSeq = pSeq.T(trans);
			for ( int element = 0; element < testArray.length; element++ ) {
				assertTrue( "Elements of transposed set are correct", transformedSeq.getMembers().get(element) == (testArray[element] + trans) % 12);
			}
		}
		
	}

	public void testPitchClassSequenceR() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
		ImmutablePitchClassSequence transformedSeq = pSeq.R();
		for ( int element = 0; element < testArray.length; element++ ) {
			assertTrue( "Elements of retrograde set are correct", transformedSeq.getMembers().get(element) == testArray[ (testArray.length - 1) - element]);
		}
	}

	public void testPitchClassSequenceI() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
		ImmutablePitchClassSequence transformedSeq = pSeq.I();
		for ( int element = 0; element < testArray.length; element++ ) {
			assertTrue( "Elements of inverted set are correct", transformedSeq.getMembers().get(element) == (PitchClassSet.MODULUS - testArray[ element]) % 12);
		}
	}

	public void testPitchClassSequenceM() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		for ( int mult = 0; mult < transpositions; mult ++ ) {
			ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
			ImmutablePitchClassSequence transformedSeq = pSeq.M(mult);
			for ( int element = 0; element < testArray.length; element++ ) {
				assertTrue( "Elements of transposed set are correct", transformedSeq.getMembers().get(element) == (testArray[element] * mult) % 12 );
			}
		}
	}
	
	public void testPitchSequenceEquals() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
		assertTrue( "A PitchSequence equals() itself returns true", pSeq.equals( pSeq ) );
		Integer[] anotherArray = testArray;
		ImmutablePitchClassSequence anotherSeq = new ImmutablePitchClassSequence( anotherArray );
		assertTrue( "A PitchSequence equals() another equivalent PitchSequence returns true", pSeq.equals( anotherSeq ) );
		assertTrue( "A PitchSequence equals() another equivalent PitchSequence returns true", pSeq.equals( new PitchSequence(anotherArray) ) );
		// test edge cases.
		Integer[] emptyArray = {};
		ImmutablePitchClassSequence emptySeq = new ImmutablePitchClassSequence( emptyArray );
		assertTrue( "Empty PitchSequence equals() another equivalent PitchSequence returns true", emptySeq.equals( new PitchSequence(emptyArray) ) );
	}

	public void testPitchClassSequencePcVector() {
		Integer[] testArray = { 0,1,2,3,4,5,6,7,8,9,10,11 };
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
		Integer[] expectedVector = { 1,1,1,1,1,1,1,1,1,1,1,1 };
		assertTrue( "A PcVector for a 12-note Row equals [1,1,1,1,1,1,1,1,1,1,1,1]", Arrays.equals( pSeq.pcVector(), expectedVector ) );
		Integer[] testArray2 = { 0,2,4,6,8,10 };
		pSeq = new ImmutablePitchClassSequence( testArray2 );
		Integer[] expectedVector2 = { 1,0,1,0,1,0,1,0,1,0,1,0 };
		assertTrue( "A PcVector for a 12-note Row equals [1,0,1,0,1,0,1,0,1,0,1,0]", Arrays.equals( pSeq.pcVector(), expectedVector2 ) );
	}

	public void testPitchClassSequenceIntervals() {
		Integer[] testArray = {  9,10,3,11,4,6,0,1,7,8,2,5 };
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
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
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
		ImmutablePitchClassSequence subSeq = new ImmutablePitchClassSequence( subSeqArray );
		List<Integer> indices =  pSeq.getEmbeddedSubsequence( subSeq );
		assertNotNull( indices );
		assertEquals( indices.size(), subSeq.length() );
		// test with a sub-sequence equal to the original. Should return a list of indices equal to the size of the original
		subSeq = new ImmutablePitchClassSequence( pSeq );
		indices =  pSeq.getEmbeddedSubsequence( subSeq );
		assertNotNull( indices );
		assertEquals( indices.size(), subSeq.length() );
		assertEquals( indices.size(), pSeq.length() );
		// test with an invalid sub-sequence, i.e. the sub-sequence is not contained in the original.
		Integer[] invalidSubSeqArray = { 5,3,4 };
		ImmutablePitchClassSequence invalidSeq = new ImmutablePitchClassSequence( invalidSubSeqArray );
		indices =  pSeq.getEmbeddedSubsequence( invalidSeq );
		assertNull( indices );
	}

	public void testPitchClassSequenceFromString() {
		String pcString = "{ 0,4,6,3,2,5,8 }";
		Integer[] pcArray = { 0,4,6,3,2,5,8 };
		assertTrue( new ImmutablePitchClassSequence( pcString ).equals( new ImmutablePitchClassSequence( pcArray ) ) );
		pcString = "{ 0,B,6,3,2,5,A }";
		Integer[] pcArrayWithLetters = { 0,11,6,3,2,5,10 };		
		assertTrue( new ImmutablePitchClassSequence( pcString ).equals( new ImmutablePitchClassSequence( pcArrayWithLetters ) ) );		
	}

	// A PitchClassSequence pcSeq created from its own string representation should equal itself.
	public void testPitchClassSequenceStringConversions() {
		Integer[] pcArray1 = { 0,4,6,3,2,5,8 };
		ImmutablePitchClassSequence testSeq1 = new ImmutablePitchClassSequence( pcArray1 );
		Iterator<ImmutablePitchClassSequence> pcSeqIt = testSeq1.transformationIterator();
		while ( pcSeqIt.hasNext() ) {
			ImmutablePitchClassSequence pcSeq = pcSeqIt.next();
			assertTrue( pcSeq.equals( ImmutablePitchClassSequence.getPitchClassSequenceFromString( pcSeq.toString() ) ) );
		}
		Integer[] pcArray2 = { 0, 11, 6, 3, 2, 5, 10 };
		ImmutablePitchClassSequence testSeq2 = new ImmutablePitchClassSequence( pcArray2 );
		pcSeqIt = testSeq2.transformationIterator();
		while ( pcSeqIt.hasNext() ) {
			ImmutablePitchClassSequence pcSeq = pcSeqIt.next();
			assertTrue( pcSeq.equals( ImmutablePitchClassSequence.getPitchClassSequenceFromString( pcSeq.toString() ) ) );
		}
	}

	public void testPitchClassSequenceFromTransformationString() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		String transformation;
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
		ImmutablePitchClassSequence transformedSeq = pSeq.I();
		for ( int element = 0; element < testArray.length; element++ ) {
			transformation = String.format("T[%d]", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
				pSeq.T(element), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.T(element).equals( pSeq.getTransformationFromString(transformation) ) );
			transformation = String.format("RT[%d]", element);

			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.T(element).R(), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.T(element).R().equals( pSeq.getTransformationFromString(transformation) ) );

			transformation = String.format("T[%d]I", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.I().T(element), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.I().T(element).equals( pSeq.getTransformationFromString(transformation) ) );

			transformation = String.format("RT[%d]I", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.I().T(element).R(), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.I().T(element).R().equals( pSeq.getTransformationFromString(transformation) ) );

			transformation = String.format("T[%d]M[7]", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.M(7).T(element), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.M(7).T(element).equals( pSeq.getTransformationFromString(transformation) ) );

			transformation = String.format("RT[%d]M[7]", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.M(7).T(element).R(), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.M(7).T(element).R().equals( pSeq.getTransformationFromString(transformation) ) );

			transformation = String.format("T[%d]M[5]", element);
			assertTrue(String.format("PitchClassSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.M(5).T(element), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.M(5).T(element).equals( pSeq.getTransformationFromString(transformation) ) );
		}
	}

	public void testPitchClassSequenceTransformationIterator() {
		Integer[] testArray = { 0,4,6,3,2,5,8,1,7 };
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
		Iterator<ImmutablePitchClassSequence> seqIt = pSeq.transformationIterator();
		while ( seqIt.hasNext() ) {
			ImmutablePitchClassSequence seq = seqIt.next();
			System.out.println( String.format("ImmutablePitchClassSequence: %s", seq.toStringExtended() ) );
		}
	}

	public void testPitchClassSequenceSubSequence() {
		Integer[] testArray = { 0,4,6,3,2,5,8,1,7 };
		ImmutablePitchClassSequence pSeq = new ImmutablePitchClassSequence( testArray );
		assertTrue(String.format("PitchSequence length is the expected length: %d", testArray.length), pSeq.length() == testArray.length );
		// Boundary tests
		int boundary = 0;
		ImmutablePitchClassSequence subSeq  = pSeq.subSequence(0, boundary);
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
				ImmutablePitchClassSequence expected = new ImmutablePitchClassSequence( Arrays.asList( testArray ).subList(index, index + jndex) );
				//System.out.println(String.format("Sequence: %s, Expected: %s", subSeq, expected ) );
				assertTrue(String.format("PitchSequence %s should equal %s", subSeq, expected ), subSeq.equals( expected ) );
			}
		}
		for ( int index = 0; index < testArray.length - 1; index ++ ) {
			System.out.println(String.format("Using index = %d, length = %d", index, pSeq.length() - index ) );
			ImmutablePitchClassSequence firstSeq =  pSeq.subSequence(0, index);
			ImmutablePitchClassSequence secondSeq = pSeq.subSequence(index, pSeq.length() - index );
			System.out.println(String.format("First seq: %s, Second seq: %s", firstSeq, secondSeq ) );
			assertTrue(String.format("Sum of PitchSequence length %d and %d should equal the size of the original set %d",
					firstSeq.length(),  secondSeq.length(), pSeq.length() ), firstSeq.length() + secondSeq.length() == pSeq.length() );
		}
	}

}
