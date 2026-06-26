package org.paulmarquardt.setTheory;

import junit.framework.TestCase;
import junit.framework.Test;

import java.lang.reflect.Array;
import java.util.*;
import org.paulmarquardt.setTheory.PitchClassSet;
import org.paulmarquardt.setTheory.ImmutablePitchSequence;
import org.paulmarquardt.setTheory.interfaces.*;

public class TestImmutablePitchSequence extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testImmutablePitchSequence() {
		ImmutablePitchSequence pSeq = new ImmutablePitchSequence();
		assert pSeq.pitches.size() == 0;
	}

	public void testPitchSequenceIntegerArray() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testArray );
		assert pSeq.size() == testArray.length;
		int index = 0;
		for ( int pitch : pSeq.getMembers() ) {
			assertTrue( "Sequence members must match array members", pitch == testArray[ index ] );
			index++;
		}
	}

	public void testPitchSequenceCollectionOfInteger() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		List<Integer> testList = new ArrayList<Integer>( Arrays.asList( testArray ) );
		ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testList );		
		assert pSeq.size() == testArray.length;
	}

	public void testPitchSequenceISequence() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		PitchSequence pSeq = new PitchSequence(testArray);
		ImmutablePitchSequence impSeq = new ImmutablePitchSequence( pSeq );		
		assert impSeq.size() == testArray.length;
	}
	
	public void testPitchSequenceGetMembers() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testArray );
		assertTrue( String.format("New ImmutablePitchSequence is the right length: %d", testArray.length), pSeq.size() == testArray.length );
		assert pSeq.pitches.size() == pSeq.getMembers().size();
	}

	public void testPitchSequenceT() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		for ( int trans = 0; trans < transpositions; trans ++ ) {
			ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testArray );
			ImmutablePitchSequence transformedSeq = pSeq.T(trans);
			for ( int element = 0; element < testArray.length; element++ ) {
				assertTrue( "Elements of transposed set are correct", transformedSeq.getMembers().get(element) == testArray[element] + trans );
			}
		}
		
	}

	public void testPitchSequenceR() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testArray );
		ImmutablePitchSequence transformedSeq = (ImmutablePitchSequence) pSeq.R();
		for ( int element = 0; element < testArray.length; element++ ) {
			assertTrue( "Elements of retrograde set are correct", transformedSeq.getMembers().get(element) == testArray[ (testArray.length - 1) - element] );
		}
	}

	public void testPitchSequenceI() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testArray );
		ImmutablePitchSequence transformedSeq = pSeq.I();
		assertTrue("Highest pitch in Original and Inverted Sequence are equal", new PitchSet( pSeq.getMembers() ).maxPitch() == new PitchSet( transformedSeq.getMembers() ).maxPitch() );
		assertTrue("Lowest pitch in Original and Inverted Sequence are equal", new PitchSet( pSeq.getMembers() ).minPitch() == new PitchSet( transformedSeq.getMembers() ).minPitch() );
		for ( int element = 0; element < testArray.length; element++ ) {
			//assertTrue( "Elements of inverted set are correct", transformedSeq.getMembers().get(element) == PitchClassSet.MODULUS - testArray[ element] );
		}
	}

	public void testPitchSequenceO() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		int index = 1;
		ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testArray );
		ImmutablePitchSequence transformedSeq = pSeq.O(index);
		System.out.println( "pSeq.O(1): " + transformedSeq.toString() );
		assert transformedSeq.equals(pSeq);

		Integer[] testArray2 = { 0,6,2,8,4,3,5 };
		index = 2;
		transformedSeq = pSeq.O(index);
		System.out.println( "pSeq.O(2): " + transformedSeq.toString() );
		assert transformedSeq.equals( new PitchSequence(testArray2));

		Integer[] testArray3 = { 0,3,8,6,5,4,2 };
		index = 3;
		transformedSeq = pSeq.O(index);
		System.out.println( "pSeq.O(2): " + transformedSeq.toString() );
		assert transformedSeq.equals( new PitchSequence(testArray3));

		Integer[] testArray4 = { 0,2,4,5,6,8,3 };
		index = 4;
		transformedSeq = pSeq.O(index);
		System.out.println( "pSeq.O(2): " + transformedSeq.toString() );
		assert transformedSeq.equals( new PitchSequence(testArray4));

		Integer[] testArray6 = { 0,8,5,2,3,6,4 };
		index = 6;
		index = pSeq.length() - 1;
		transformedSeq = pSeq.O(index);
		System.out.println( "pSeq.O(6): " + transformedSeq.toString() );
		assert transformedSeq.equals( new PitchSequence(testArray6) );
	}

	public void testPitchSequenceM() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		int transpositions = 12;
		for ( int mult = 0; mult < transpositions; mult ++ ) {
			ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testArray );
			ImmutablePitchSequence transformedSeq = pSeq.M(mult);
			for ( int element = 0; element < testArray.length; element++ ) {
				assertTrue( "Elements of transposed set are correct", transformedSeq.getMembers().get(element) == testArray[element] * mult );
			}
		}
	}
	
	public void testPitchSequenceEquals() {
		Integer[] testArray = { 0,4,6,3,2,5,8 };
		ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testArray );
		assertTrue( "A PitchSequence equals() itself returns true", pSeq.equals( pSeq ) );
		Integer[] anotherArray = testArray;
		ImmutablePitchSequence anotherSeq = new ImmutablePitchSequence( anotherArray );
		assertTrue( "A PitchSequence equals() another equivalent PitchSequence returns true", pSeq.equals( anotherSeq ) );
		assertTrue( "A PitchSequence equals() another equivalent PitchSequence returns true", pSeq.equals( new PitchSequence(anotherArray) ) );
		// test edge cases.
		Integer[] emptyArray = {};
		ImmutablePitchSequence emptySeq = new ImmutablePitchSequence( emptyArray );
		assertTrue( "Empty PitchSequence equals() another equivalent PitchSequence returns true", emptySeq.equals( new PitchSequence(emptyArray) ) );
		// Check transformations
		for ( int index = 0; index < 12; index++ ) {
			assertTrue( "A PitchSequence equals() another equivalent PitchSequence returns true", pSeq.T(index).equals( pSeq.T(index) ) );
		}
		// Check inverse operations...
		assertTrue( "PitchSequence R().R() equals the original set", pSeq.R().R().equals( pSeq ) );
		assertTrue( "PitchSequence I().I() equals the original set", pSeq.I().I().equals( pSeq ) );
	}

	public void testPitchSequenceSubSequence() {
		Integer[] testArray = { 0,4,6,3,2,5,8,1,7 };
		ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testArray );
		List<Integer> indices = Arrays.asList( new Integer[] { 0, 1, 2 } );
		assertTrue("Test PitchSequence subsequence using List<Integer> of indices", pSeq.subSequence( indices ).equals( new PitchSequence( new Integer[]  { 0,4,6 } ) ) );
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
				subSeq.toString(), new PitchSequence( new Integer[] {4,6,3} ) ), subSeq.equals(new PitchSequence( new Integer[] {4,6,3} ) ) );
		subSeq  = pSeq.subSequence( 2,4 );
		assertTrue(String.format("PitchSequence %s should equal %s",
				subSeq.toString(), new PitchSequence( new Integer[] {6,3,2,5} ) ), subSeq.equals(new PitchSequence( new Integer[] {6,3,2,5} ) ) );
		subSeq  = pSeq.subSequence( 4,5 );
		assertTrue(String.format("PitchSequence %s should equal %s",
				subSeq.toString(), new PitchSequence( new Integer[] {2,5,8,1,7} ) ), subSeq.equals(new PitchSequence( new Integer[] {2,5,8,1,7} ) ) );
		for ( int index = 0; index < testArray.length - 1; index ++ ) {
			for ( int jndex = 0; jndex < testArray.length - index; jndex ++ ) {
				subSeq =  pSeq.subSequence(index, jndex);
				ISequence expected = new PitchSequence( Arrays.asList( testArray ).subList(index, index + jndex) );
				System.out.println(String.format("Sequence: %s, Expected: %s", subSeq, expected ) );
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

	public void testPartitionBy() {
		ImmutablePitchSequence p = new ImmutablePitchSequence("0 11 1 2 10 8 5 3 9 4 7 6");
	    System.out.println(String.format("Using ImmutablePitchSequence %s", p.toString()));
	    assert p.length() == 12;
	    Integer[] slices = new Integer[] { 3, 4, 5 };
	    List<ISequence> expectedSeqs = new ArrayList<ISequence>();
	    expectedSeqs.add( new ImmutablePitchSequence("0 11 1") );
	    expectedSeqs.add( new ImmutablePitchSequence("2 10 8 5") );
	    expectedSeqs.add( new ImmutablePitchSequence("3 9 4 7 6") );
	    List<IMutableSequence> partionedSeqs = p.partitionBy(slices);
	    for ( int i = 0; i < slices.length; i++) {
	    		assertTrue( String.format("ImmutablePitchSequence %s partitioned by %s matches expected results %s", p.toString(), slices.toString(), expectedSeqs.toString()), partionedSeqs.get(i).equals(expectedSeqs.get(i)));
	    }
	    // Using a PitchClassSequence
	    ImmutablePitchSequence pcSeq = new ImmutablePitchSequence("3 9 4 7 6 2 10 8 5 0 11 1");
	    System.out.println(String.format("Using ImmutablePitchSequence %s", p.toString()));
	    assert p.length() == 12;
	    slices = new Integer[] { 5, 4, 3 };
	    List<ISequence> expectedPCSeqs = new ArrayList<ISequence>();
	    expectedPCSeqs.add( new ImmutablePitchSequence("3 9 4 7 6") );
	    expectedPCSeqs.add( new ImmutablePitchSequence("2 10 8 5") );
	    expectedPCSeqs.add( new ImmutablePitchSequence("0 11 1") );
	    partionedSeqs = pcSeq.partitionBy(slices);
	    for ( int i = 0; i < slices.length; i++) {
	    		assertTrue( String.format("ImmutablePitchSequence %s partitioned by %d matches expected results %s", p.toString(), slices[i], expectedPCSeqs.get(i).toString()), partionedSeqs.get(i).equals(expectedPCSeqs.get(i)));
	    }

	}
	
/*
	@Test(expected = IllegalArgumentException.class)
	public void testPartitionByExceptionIncorrectSize() {
	    PitchSequence p = new PitchSequence("0b12a8539476");
	    Integer[] slices = new Integer[] { 3, 4, 6 };
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPartitionByExceptionNegativeIndex() {
	    PitchSequence p = new PitchSequence("0b12a8539476");
	    Integer[] slices = new Integer[] { -1, 3, 4, 5, 1 };
	}
*/
	
	public void testPitchSequenceFromString() {
		Integer[] pitchArray = { 4, 10, 17, 20, 21, 32, 39, 42 };
		String pitchString = "{ 4 10 17 20 21 32 39 42 }";
		//System.out.println( String.format("PSequence from Array: %s", new PitchSequence(pitchArray).toString() ) );
		//System.out.println( String.format("PSequence from String: %s", new PitchSequence(pitchString).toString() ) );
		assertTrue( new PitchSequence(pitchArray).equals( new PitchSequence(pitchString) ) );
		
		pitchString = "{ 4,10,17,20,21,32,39,42 }";
		//System.out.println( String.format("PSequence from Array: %s", new PitchSequence(pitchArray).toString() ) );
		//System.out.println( String.format("PSequence from String: %s", new PitchSequence(pitchString).toString() ) );
		assertTrue( new PitchSequence(pitchArray).equals( new PitchSequence(pitchString) ) );

		pitchString = "{ 4, 10 ,17,20, 21  ,32, 39, 42 }";
		System.out.println( String.format("PSequence from Array: %s", new PitchSequence(pitchArray).toString() ) );
		System.out.println( String.format("PSequence from String: %s", new PitchSequence(pitchString).toString() ) );
		assertTrue( new ImmutablePitchSequence(pitchArray).equals( new ImmutablePitchSequence(pitchString) ) );

	}

	public void testTransformationFromString() {
		Integer[] testArray = { 0,4,6,3,2,5,8,1,7 };
		ImmutablePitchSequence pSeq = new ImmutablePitchSequence( testArray );
		String transformation;
		for ( int index = 0; index < 12; index++ ) {
			transformation = String.format("T[%d]", index);
			assertTrue(String.format("PitchSequence %s equals expected value produced by transformation string '%s': %s",
				pSeq.T(index), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.T(index).equals( pSeq.getTransformationFromString(transformation) ) );
			transformation = String.format("RT[%d]", index);
			assertTrue(String.format("PitchSequence %s equals expected value produced by transformation string '%s': %s",
					pSeq.T(index).R(), transformation, pSeq.getTransformationFromString(transformation) ), pSeq.T(index).R().equals( pSeq.getTransformationFromString(transformation) ) );
		}
	}

}
