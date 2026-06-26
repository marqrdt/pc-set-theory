package org.paulmarquardt.setTheory;

import java.util.Arrays;

import junit.framework.TestCase;

public class TestContour extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testContourConstructors() {
		Integer[] simpleSeq = { 2, 4, 6, 8, 10, 12 }; // [0, 2, 4, 6, 8, 10]
		Integer[] complexSeqNoDuplicates = {20, 13, 7, 17, 18, 21, 23, 18, 2, 11, 6}; // [4, 2, 6, 7, 0, 3, 5, 1]
		//Integer[] complexSeqWithDuplicates = {20, 13, 7, 17, 18, 21, 23, 18, 2, 11, 6}; // [8, 4, 2, 5, 6, 9, 10, 6, 0, 3, 1]
		// elements in order: [2, 6, 7, 11, 13, 17, 18, 20, 21, 23]
		// translated to 0:   [0, 4, 5, 9, 11, 15, 16, 18, 19, 21]
		// in original order: [18, 11, 5, 15, 16, 19, 21, 16, 0, 9, 4]
		Integer[] simpleSeqContour = {0, 2, 4, 6, 8, 10};
		Integer[] complexSeqNoDuplicatesContour = {18, 11, 5, 15, 16, 19, 21, 16, 0, 9, 4};
		//Integer[] complexSeqWithDuplicatesContour = {8, 4, 2, 5, 6, 9, 10, 6, 0, 3, 1};
		Contour simpleContour = new Contour(  new PitchSequence( simpleSeq ) );
		Contour contWithArray = new Contour();
		assert ( contWithArray.length() == 0 );
		contWithArray = new Contour( complexSeqNoDuplicates );
		assert ( contWithArray.length() == complexSeqNoDuplicates.length );
		//assert ( cont.getMembers().equals( Arrays.asList( complexContourArrayNoDuplicates ) ) );
		assertNotNull ( contWithArray.getMembers() );
		Contour contWithList = new Contour( Arrays.asList( complexSeqNoDuplicates ) );
		assertTrue( "A Contour created from an Array of Integer should equal a Contour created from a List<Integer> created from the same Array.", contWithArray.equals( contWithList ) );
		assertTrue( String.format("A Contour created from an Array %s should have Contour %s", Arrays.toString(simpleSeq), simpleContour.toString()),
				Arrays.asList( simpleSeqContour ).equals( simpleContour.getMembers() ) );
		assertTrue( String.format("A Contour created from an Array %s should have Contour %s", complexSeqNoDuplicates.toString(), complexSeqNoDuplicatesContour.toString()),
				Arrays.asList( complexSeqNoDuplicatesContour ).equals( contWithArray.getMembers() ) );
	}

	public void testBasicContourOperations() {
		Integer[] simpleContourArray = { 2, 4, 6, 8, 10, 12 };
		Integer[] complexContourArrayNoDuplicates = {9, 5, 15, 27, 2, 8, 10, 4}; // [4, 2, 6, 7, 0, 3, 5, 1]
		Integer[] complexContourArrayWithDuplicates = {20, 13, 7, 17, 18, 21, 23, 18, 2, 11, 6}; // [8, 4, 2, 5, 6, 9, 10, 6, 0, 3, 1]
		Contour complexContourNoDuplicates = new Contour(  new PitchSequence( complexContourArrayNoDuplicates ) );
		System.out.println( String.format("Contour complexContourNoDuplicates: %s", complexContourNoDuplicates.toString() ) );
		System.out.println( String.format("inversion of Contour complexContourNoDuplicates: %s", complexContourNoDuplicates.I().toString() ) );
		System.out.println( String.format("inversion of inversion of Contour complexContourNoDuplicates: %s", complexContourNoDuplicates.I().I().toString() ) );
		assertTrue( "A Contour is equal to itself.", complexContourNoDuplicates.equals( complexContourNoDuplicates ) );
		assertTrue( "A Contour is equal to the Inversion of its Inversion.", complexContourNoDuplicates.I().I().equals( complexContourNoDuplicates ) );
		assertTrue( "A Contour is equal to the Retrograde of its Retrograde.", complexContourNoDuplicates.R().R().equals( complexContourNoDuplicates ) );
		int max = 12;
		for ( int i = 0; i <= max; i++ ) {
			assertTrue( "A Contour is equal to the Inverse Transposition of its Transposition.", complexContourNoDuplicates.T( i ).T( 0 - i ).equals( complexContourNoDuplicates ) );
		}
		Contour complexContourWithDuplicates = new Contour(  new PitchSequence( complexContourArrayWithDuplicates ) );
		System.out.println( String.format("Contour complexContourWithDuplicates: %s", complexContourWithDuplicates.toString() ) );
		System.out.println( String.format("inversion of Contour complexContourWithDuplicates: %s", complexContourWithDuplicates.I().toString() ) );
		System.out.println( String.format("inversion of inversion of Contour complexContourWithDuplicates: %s", complexContourWithDuplicates.I().I().toString() ) );
		assertTrue( "A Contour is equal to itself.", complexContourWithDuplicates.equals( complexContourWithDuplicates ) );
		assertTrue( "A Contour is equal to the Inversion of its Inversion.", complexContourWithDuplicates.I().I().equals( complexContourWithDuplicates ) );
		assertTrue( "A Contour is equal to the Retrograde of its Retrograde.", complexContourWithDuplicates.R().R().equals( complexContourWithDuplicates ) );
	}

	public void testContourOperations() {
		Integer[] simpleContourArray = { 2, 4, 6, 8, 10, 12 };
		Integer[] simpleContourArray1 = { 1, 3, 5, 7, 9, 11 };
		Contour simpleContour = new Contour( simpleContourArray );
		Contour simpleContour1 = new Contour( simpleContourArray1 );
		Integer[] complexContourArrayNoDuplicates = {9, 5, 15, 27, 2, 8, 10, 4}; // [4, 2, 6, 7, 0, 3, 5, 1]
		Integer[] complexContourArrayNoDuplicates1 = {10, 6, 16, 28, 3, 9, 11, 5}; // [4, 2, 6, 7, 0, 3, 5, 1]
		Integer[] complexContourArrayWithDuplicates = {20, 13, 7, 17, 18, 21, 23, 18, 2, 11, 6}; // [8, 4, 2, 5, 6, 9, 10, 6, 0, 3, 1]
		Contour complexContourNoDuplicates = new Contour(  new PitchSequence( complexContourArrayNoDuplicates ) );
		Contour complexContourNoDuplicates1 = new Contour(  new PitchSequence( complexContourArrayNoDuplicates1 ) );
		Contour complexContourNoDuplicatesExtended = new Contour(  new PitchSequence( complexContourArrayNoDuplicates1 ) );
		
		Integer[] indicesEven = { 0, 2, 4, 6, 8, 10, 12, 14 };
		Integer[] indicesOdd = { 1, 3, 5, 7, 9, 11, 13, 15 };
		
		System.out.println( String.format("Contour complexContourNoDuplicates: %s", complexContourNoDuplicates.toString() ) );
		System.out.println( String.format("inversion of Contour complexContourNoDuplicates: %s", complexContourNoDuplicates.I().toString() ) );
		System.out.println( String.format("inversion of inversion of Contour complexContourNoDuplicates: %s", complexContourNoDuplicates.I().I().toString() ) );
		simpleContour.extend( simpleContour1 );
		assertTrue( "A Contour expended by another Contour has the expected elements", simpleContour.subSequence( 0, 6).equals( new Contour( simpleContourArray ) ) );
		assertTrue( "A Contour expended by another Contour has the expected elements", simpleContour.subSequence( 6, 6).equals( new Contour( simpleContourArray1 ) ) );
		assertTrue( "A Contour is equal to itself.", complexContourNoDuplicates.equals( complexContourNoDuplicates ) );
		assertTrue( "A Contour is equal to the Inversion of its Inversion.", complexContourNoDuplicates.I().I().equals( complexContourNoDuplicates ) );
		assertTrue( "A Contour is equal to the Retrograde of its Retrograde.", complexContourNoDuplicates.R().R().equals( complexContourNoDuplicates ) );
		int max = 12;
		for ( int i = 0; i <= max; i++ ) {
			assertTrue( "A Contour is equal to the Inverse Transposition of its Transposition.", complexContourNoDuplicates.T( i ).T( 0 - i ).equals( complexContourNoDuplicates ) );
		}
		Contour complexContourWithDuplicates = new Contour(  new PitchSequence( complexContourArrayWithDuplicates ) );
		System.out.println( String.format("Contour complexContourWithDuplicates: %s", complexContourWithDuplicates.toString() ) );
		System.out.println( String.format("inversion of Contour complexContourWithDuplicates: %s", complexContourWithDuplicates.I().toString() ) );
		System.out.println( String.format("inversion of inversion of Contour complexContourWithDuplicates: %s", complexContourWithDuplicates.I().I().toString() ) );
		assertTrue( "A Contour is equal to itself.", complexContourWithDuplicates.equals( complexContourWithDuplicates ) );
		assertTrue( "A Contour is equal to the Inversion of its Inversion.", complexContourWithDuplicates.I().I().equals( complexContourWithDuplicates ) );
		assertTrue( "A Contour is equal to the Retrograde of its Retrograde.", complexContourWithDuplicates.R().R().equals( complexContourWithDuplicates ) );
	}

}
