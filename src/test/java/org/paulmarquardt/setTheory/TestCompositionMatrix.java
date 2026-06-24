package com.newscores.setTheory;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.newscores.setTheory.CMSegment;
import com.newscores.setTheory.CompositionMatrix;
import com.newscores.setTheory.PitchClassSequence;
import com.newscores.setTheory.PitchClassSet;
import com.newscores.setTheory.PitchSequence;
import com.newscores.setTheory.interfaces.ISequence;
import com.newscores.setTheory.utils.CompositionMatrixUtils;
import java.util.*;
import java.util.Arrays;

public class TestCompositionMatrix {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCompositionMatrixNewSimplePitchSequence() {
		Integer[] array00 = {0,1,2,3,4,5};
		PitchSequence seq00 = new PitchSequence( array00 );
		seq00.setDescriptor("seq00");
		
		Integer[] array10 = {6,7,8,9,10,11};
		PitchSequence seq10 = new PitchSequence( array10 );
		seq10.setDescriptor("seq10");
		
		// create a simple CM with 1 Row and 2 Columns
		CompositionMatrix cm1x2 = new CompositionMatrix();
		
		cm1x2.addSegment(seq00, 0, 0);
		cm1x2.addSegment(seq10, 0, 1);
		// test getSegments
		/**
		for ( CMSegment seg : cm1x2.getSegments() ) {
			System.out.println(
					String.format("Segment: %s at %d,%d",
							seg.getPitchSequence().toString(),
							seg.getRowIndex(),
							seg.getColumnIndex()
					)
			);
		}
		**/
		CMSegment seg = cm1x2.getSegmentAtCoordinate(0, 0);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,0), cm1x2.getSegmentAtCoordinate(0, 0) );
		assertEquals(seg.getDescriptor(), "seq00");
		seg = cm1x2.getSegmentAtCoordinate(0, 1);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,1), cm1x2.getSegmentAtCoordinate(0, 1) );
		assertEquals(seg.getDescriptor(), "seq10");
		System.out.println( CompositionMatrixUtils.format( cm1x2 ) );
		assertNotNull("Composition Matrix should not be null", cm1x2);
		assertEquals("CM must have the correct number of segments", cm1x2.getSegments().size(), 2);
		assertEquals("CM must have the correct number of rows", cm1x2.getNumberOfRows(), 1);
		assertEquals("CM must have the correct number of columns", cm1x2.getNumberOfColumns(), 2);
		
	}

	@Test
	public void testCompositionMatrix2x2PitchSequence() {
		Integer[] array00 = {0,1,2,3,4,5};
		PitchSequence seq00 = new PitchSequence( array00 );
		seq00.setDescriptor("seq00");
		
		Integer[] array01 = {6,7,8,9,10,11};
		PitchSequence seq01 = new PitchSequence( array01 );
		seq01.setDescriptor("seq01");
		
		Integer[] array10 = {11,10,9,8,7,6};
		PitchSequence seq10 = new PitchSequence( array10 );
		seq10.setDescriptor("seq10");
		
		Integer[] array11 = {5,4,3,2,1,0};
		PitchSequence seq11 = new PitchSequence( array11 );
		seq11.setDescriptor("seq11");

		// create a simple CM with 1 Row and 2 Columns
		CompositionMatrix cm2x2 = new CompositionMatrix();
		
		cm2x2.addSegment(seq00, 0, 0);
		cm2x2.addSegment(seq01, 0, 1);
		cm2x2.addSegment(seq10, 1, 0);
		cm2x2.addSegment(seq11, 1, 1);
		
		// test getSegments
		for ( CMSegment seg : cm2x2.getSegments() ) {
			System.out.println( seg.getDescriptor() );
			assertTrue("CMSeqment description is set properly", seg.getDescriptor().startsWith("seq"));
		}
		System.out.println( CompositionMatrixUtils.format( cm2x2, " ", "|", true ) );

		CMSegment seg = cm2x2.getSegmentAtCoordinate(0, 0);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,0), cm2x2.getSegmentAtCoordinate(0, 0) );
		seg = cm2x2.getSegmentAtCoordinate(0, 1);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,1), cm2x2.getSegmentAtCoordinate(0, 1) );
		assertNotNull("Composition Matrix should not be null", cm2x2);
		assertEquals("CM must have the correct number of segments", cm2x2.getSegments().size(), 4);
		assertEquals("CM must have the correct number of rows", cm2x2.getNumberOfRows(), 2);
		assertEquals("CM must have the correct number of columns", cm2x2.getNumberOfColumns(), 2);	
	}
	
	@Test
	public void testCompositionMatrixNewSimplePitchClassSequence() {
		Integer[] array00 = {0,1,2,3,4,5};
		PitchClassSequence seq00 = new PitchClassSequence( array00 );
		
		Integer[] array10 = {6,7,8,9,10,11};
		PitchClassSequence seq10 = new PitchClassSequence( array10 );
		
		// create a simple CM with 1 Row and 2 Columns
		CompositionMatrix cm1x2 = new CompositionMatrix();
		
		cm1x2.addSegment(seq00, 0, 0);
		cm1x2.addSegment(seq10, 0, 1);
		// test getSegments
		/**
		for ( CMSegment seg : cm1x2.getSegments() ) {
			System.out.println(
					String.format("Segment: %s at %d,%d",
							seg.getPitchSequence().toString(),
							seg.getRowIndex(),
							seg.getColumnIndex()
					)
			);
		}
		**/
		CMSegment seg = cm1x2.getSegmentAtCoordinate(0, 0);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,0), cm1x2.getSegmentAtCoordinate(0, 0) );
		seg = cm1x2.getSegmentAtCoordinate(0, 1);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,1), cm1x2.getSegmentAtCoordinate(0, 1) );

		System.out.println( CompositionMatrixUtils.format( cm1x2 ) );
		assertNotNull("Composition Matrix should not be null", cm1x2);
		assertEquals("CM must have the correct number of segments", cm1x2.getSegments().size(), 2);
		assertEquals("CM must have the correct number of rows", cm1x2.getNumberOfRows(), 1);
		assertEquals("CM must have the correct number of columns", cm1x2.getNumberOfColumns(), 2);
		
	}

	@Test
	public void testCompositionMatrix2x2PitchClassSequence() {
		Integer[] array00 = {0,1,2,3,4,5};
		PitchClassSequence seq00 = new PitchClassSequence( array00 );
		
		Integer[] array01 = {6,7,8,9,10,11};
		PitchClassSequence seq01 = new PitchClassSequence( array01 );
		
		Integer[] array10 = {11,10,9,8,7,6};
		PitchClassSequence seq10 = new PitchClassSequence( array10 );
		
		Integer[] array11 = {5,4,3,2,1,0};
		PitchClassSequence seq11 = new PitchClassSequence( array11 );

		// create a simple CM with 1 Row and 2 Columns
		CompositionMatrix cm2x2 = new CompositionMatrix();
		
		cm2x2.addSegment(seq00, 0, 0);
		cm2x2.addSegment(seq01, 0, 1);
		cm2x2.addSegment(seq10, 1, 0);
		cm2x2.addSegment(seq11, 1, 1);
		
		// test getSegments
		/**
		for ( CMSegment seg : cm1x2.getSegments() ) {
			System.out.println(
					String.format("Segment: %s at %d,%d",
							seg.getPitchSequence().toString(),
							seg.getRowIndex(),
							seg.getColumnIndex()
					)
			);
		}
		**/
		System.out.println( CompositionMatrixUtils.format( cm2x2 ) );

		CMSegment seg = cm2x2.getSegmentAtCoordinate(0, 0);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,0), cm2x2.getSegmentAtCoordinate(0, 0) );
		seg = cm2x2.getSegmentAtCoordinate(0, 1);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,1), cm2x2.getSegmentAtCoordinate(0, 1) );
		assertNotNull("Composition Matrix should not be null", cm2x2);
		assertEquals("CM must have the correct number of segments", cm2x2.getSegments().size(), 4);
		assertEquals("CM must have the correct number of rows", cm2x2.getNumberOfRows(), 2);
		assertEquals("CM must have the correct number of columns", cm2x2.getNumberOfColumns(), 2);	
	}
	
	@Test
	public void testCompositionMatrixFromPitchClassSequenceAndSizeMatrix() {
                List<ISequence> pcSeqs = new ArrayList<ISequence>();
		Integer[] array1 = {0,1,2,3,4,5,6,7,8,9,10,11};
		PitchClassSequence seq1 = new PitchClassSequence( array1 );
		pcSeqs.add(seq1);
                
		Integer[] array2 = {3,4,5,6,7,8,9,10,11,0,1,2};
		PitchClassSequence seq2 = new PitchClassSequence( array2 );
		pcSeqs.add(seq2);
		
		Integer[] array3 = {7,8,9,10,11,0,1,2,3,4,5,6};
		PitchClassSequence seq3 = new PitchClassSequence( array3 );
 		pcSeqs.add(seq3);
               
                List<List<Integer>> indexMatrix = new ArrayList<List<Integer>>();
                
                indexMatrix.add( Arrays.asList( new Integer[] {3,4,5}) );
                indexMatrix.add( Arrays.asList( new Integer[] {4,5,3}) );
                indexMatrix.add( Arrays.asList( new Integer[] {5,3,4}) );
                //indexMatrix.add( Arrays.asList( new Integer[] {5,5,5}) );
                
		// create a simple CM with 1 Row and 2 Columns
		CompositionMatrix newCM = new CompositionMatrix( pcSeqs, indexMatrix);
		System.out.println( CompositionMatrixUtils.format( newCM ));
                PitchClassSequence pcs = new PitchClassSequence( newCM.getColumnAsPitchClassSequence(0) );
                assert new PitchClassSet(newCM.getColumnAsPitchClassSequence(0)).equals( PitchClassSet.aggregate());
		CMSegment seg = newCM.getSegmentAtCoordinate(0, 0);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,0), newCM.getSegmentAtCoordinate(0, 0) );
		seg = newCM.getSegmentAtCoordinate(0, 1);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,1), newCM.getSegmentAtCoordinate(0, 1) );
		assertNotNull("Composition Matrix should not be null", newCM);
		assertEquals("CM must have the correct number of segments", newCM.getSegments().size(), 9);
		assertEquals("CM must have the correct number of rows", newCM.getNumberOfRows(), 3);
		assertEquals("CM must have the correct number of columns", newCM.getNumberOfColumns(), 3);
                
                indexMatrix = new ArrayList<List<Integer>>();
                indexMatrix.add( Arrays.asList( new Integer[] {12,0,0}) );
                indexMatrix.add( Arrays.asList( new Integer[] {0,12,0}) );
                indexMatrix.add( Arrays.asList( new Integer[] {0,0,12}) );
		newCM = new CompositionMatrix( pcSeqs, indexMatrix);
		System.out.println( CompositionMatrixUtils.format( newCM ));
		seg = newCM.getSegmentAtCoordinate(0, 0);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,0), newCM.getSegmentAtCoordinate(0, 0) );
		seg = newCM.getSegmentAtCoordinate(0, 1);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,1), newCM.getSegmentAtCoordinate(0, 1) );
		assertNotNull("Composition Matrix should not be null", newCM);
                assert new PitchClassSet(newCM.getSegmentAtCoordinate(0, 0).getPitchSequence()).equals( PitchClassSet.aggregate());
                assert new PitchClassSet(newCM.getSegmentAtCoordinate(1, 1).getPitchSequence()).equals( PitchClassSet.aggregate());
                assert new PitchClassSet(newCM.getSegmentAtCoordinate(2, 2).getPitchSequence()).equals( PitchClassSet.aggregate());
		assertEquals("CM must have the correct number of segments", newCM.getSegments().size(), 9);
		assertEquals("CM must have the correct number of rows", newCM.getNumberOfRows(), 3);
		assertEquals("CM must have the correct number of columns", newCM.getNumberOfColumns(), 3);
               
	}

        @Test
	public void testCompositionMatrixTransformations() {
		Integer[] array00 = {5,4,0,9,7,2};
		PitchClassSequence seq00 = new PitchClassSequence( array00 );
		
		Integer[] array01 = {8,1,3,6,10,11};
		PitchClassSequence seq01 = new PitchClassSequence( array01 );
		
		Integer[] array10 = {10,11,3,6,8,1};
		PitchClassSequence seq10 = new PitchClassSequence( array10 );
		
		Integer[] array11 = {7,2,0,9,5,4};
		PitchClassSequence seq11 = new PitchClassSequence( array11 );

		// create a simple CM with 1 Row and 2 Columns
		CompositionMatrix cm2x2 = new CompositionMatrix();
		
		cm2x2.addSegment(seq00, 0, 0);
		cm2x2.addSegment(seq01, 0, 1);
		cm2x2.addSegment(seq10, 1, 0);
		cm2x2.addSegment(seq11, 1, 1);
		
		// test getSegments
		/**
		for ( CMSegment seg : cm1x2.getSegments() ) {
			System.out.println(
					String.format("Segment: %s at %d,%d",
							seg.getPitchSequence().toString(),
							seg.getRowIndex(),
							seg.getColumnIndex()
					)
			);
		}
		**/
		System.out.println( CompositionMatrixUtils.format( cm2x2 ) );
		System.out.println( "Printing CompositionMatrix transformations..." );
		for ( int trans = 0; trans < 12; trans++ ) {
			System.out.println( String.format("CompositionMatrix transposition %d:", trans ) );
			System.out.println( CompositionMatrixUtils.format( cm2x2.T( trans ) ) );
		}
		System.out.println( "===========================" );
		System.out.println( CompositionMatrixUtils.format( cm2x2 ) );
		for ( int trans = 0; trans < 12; trans++ ) {
			System.out.println( String.format("CompositionMatrix retrograde transposition %d:", trans ) );
			System.out.println( CompositionMatrixUtils.format( cm2x2.R().T( trans ) ) );			
		}

	}

	@Test
	public void testCompositionMatrix3x3PitchClassSequence() {
		Integer[] array00 = {0,1,2};
		PitchClassSequence seq00 = new PitchClassSequence( array00 );
		
		Integer[] array01 = {3,4,5,6};
		PitchClassSequence seq01 = new PitchClassSequence( array01 );

		Integer[] array02 = {7,8,9,10,11};
		PitchClassSequence seq02 = new PitchClassSequence( array02 );

		Integer[] array10 = {3,4,5,6};
		PitchClassSequence seq10 = new PitchClassSequence( array10 );
		
		Integer[] array11 = {7,8,9,10,11};
		PitchClassSequence seq11 = new PitchClassSequence( array11 );

		Integer[] array12 = {0,1,2};
		PitchClassSequence seq12 = new PitchClassSequence( array12 );

		Integer[] array20 = {7,8,9,10,11};
		PitchClassSequence seq20 = new PitchClassSequence( array20 );
		
		Integer[] array21 = {0,1,2};
		PitchClassSequence seq21 = new PitchClassSequence( array21 );

		Integer[] array22 = {3,4,5,6};
		PitchClassSequence seq22 = new PitchClassSequence( array22 );

		// create a simple CM with 1 Row and 2 Columns
		CompositionMatrix cm3x3 = new CompositionMatrix();
		
		cm3x3.addSegment(seq00, 0, 0);
		cm3x3.addSegment(seq01, 0, 1);
		cm3x3.addSegment(seq02, 0, 2);

		cm3x3.addSegment(seq10, 1, 0);
		cm3x3.addSegment(seq11, 1, 1);
		cm3x3.addSegment(seq12, 1, 2);

		cm3x3.addSegment(seq20, 2, 0);
		cm3x3.addSegment(seq21, 2, 1);
		cm3x3.addSegment(seq22, 2, 2);
		
		// test getSegments
		/**
		for ( CMSegment seg : cm1x2.getSegments() ) {
			System.out.println(
					String.format("Segment: %s at %d,%d",
							seg.getPitchSequence().toString(),
							seg.getRowIndex(),
							seg.getColumnIndex()
					)
			);
		}
		**/
		System.out.println( CompositionMatrixUtils.format( cm3x3 ) );

		CMSegment seg = cm3x3.getSegmentAtCoordinate(0, 0);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,0), cm3x3.getSegmentAtCoordinate(0, 0) );
		seg = cm3x3.getSegmentAtCoordinate(0, 1);
		assertNotNull(String.format("Segment at %d,%d should not be null", 0,1), cm3x3.getSegmentAtCoordinate(0, 1) );
		assertNotNull("Composition Matrix should not be null", cm3x3);
		assertEquals("CM must have the correct number of segments", cm3x3.getSegments().size(), 9);
		assertEquals("CM must have the correct number of rows", cm3x3.getNumberOfRows(), 3);
		assertEquals("CM must have the correct number of columns", cm3x3.getNumberOfColumns(), 3);	
		System.out.println( CompositionMatrixUtils.format( cm3x3 ) );
		System.out.println( "Printing CompositionMatrix transformations toJsopnString()..." );
		for ( int trans = 0; trans < 12; trans++ ) {
			System.out.println( String.format("CompositionMatrix transposition %d toJsonString():", trans ) );
			System.out.println( cm3x3.T( trans ).toJsonString() );
		}
		System.out.println( "===========================" );
		System.out.println( CompositionMatrixUtils.format( cm3x3 ) );
		for ( int trans = 0; trans < 12; trans++ ) {
			System.out.println( String.format("CompositionMatrix retrograde transposition %d toJsonString():", trans ) );
			System.out.println( cm3x3.R().T( trans ).toJsonString() );			
		}
	}
	
	@Test
	public void testCompositionMatrixTransformationIterator() {
		Integer[] array00 = {5,4,0,9,7,2};
		PitchClassSequence seq00 = new PitchClassSequence( array00 );
		
		Integer[] array01 = {8,1,3,6,10,11};
		PitchClassSequence seq01 = new PitchClassSequence( array01 );
		
		Integer[] array10 = {10,11,3,6,8,1};
		PitchClassSequence seq10 = new PitchClassSequence( array10 );
		
		Integer[] array11 = {7,2,0,9,5,4};
		PitchClassSequence seq11 = new PitchClassSequence( array11 );

		// create a simple CM with 1 Row and 2 Columns
		CompositionMatrix cm2x2 = new CompositionMatrix();
		
		cm2x2.addSegment(seq00, 0, 0);
		cm2x2.addSegment(seq01, 0, 1);
		cm2x2.addSegment(seq10, 1, 0);
		cm2x2.addSegment(seq11, 1, 1);
		
		// test getSegments
		/**
		for ( CMSegment seg : cm1x2.getSegments() ) {
			System.out.println(
					String.format("Segment: %s at %d,%d",
							seg.getPitchSequence().toString(),
							seg.getRowIndex(),
							seg.getColumnIndex()
					)
			);
		}
		**/
		System.out.println( CompositionMatrixUtils.format( cm2x2 ) );
		System.out.println( "Printing CompositionMatrix transformations from transformationIterator..." );
		Iterator<CompositionMatrix> itCM = cm2x2.transformationIterator();
		int numCMs = 0;
		while ( itCM.hasNext() ) {
			CompositionMatrix nextCM = itCM.next();
			System.out.println( String.format("%s:\n%s", nextCM.getDescriptor(),CompositionMatrixUtils.format( nextCM )) );
			numCMs++;
		}
		assertEquals(String.format("CM iterator should iterate through 96 CMs, actual amount: %d", numCMs), numCMs, 96);
	}

	@Test
	public void testCompositionMatrixIsCanonical() {
		PitchClassSequence seq00 = new PitchClassSequence( "0 1 2 3 4 5" );
		PitchClassSequence seq01 = new PitchClassSequence( "6 7 8 9 A B" );
		PitchClassSequence seq10 = new PitchClassSequence( "6 7 8 9 A B" );		
		PitchClassSequence seq11 = new PitchClassSequence( "0 1 2 3 4 5" );

		// create a simple CM with 1 Row and 2 Columns
		CompositionMatrix cm2x2 = new CompositionMatrix();
		
		cm2x2.addSegment(seq00, 0, 0);
		cm2x2.addSegment(seq01, 0, 1);
		cm2x2.addSegment(seq10, 1, 0);
		cm2x2.addSegment(seq11, 1, 1);
		
		// test getSegments
		/**
		for ( CMSegment seg : cm1x2.getSegments() ) {
			System.out.println(
					String.format("Segment: %s at %d,%d",
							seg.getPitchSequence().toString(),
							seg.getRowIndex(),
							seg.getColumnIndex()
					)
			);
		}
		**/
		System.out.println( CompositionMatrixUtils.format( cm2x2 ) );
		System.out.println( "Testing known canonical CM..." );
		assertTrue("Known canonical CM tests true",cm2x2.isCanonical(true, true));

		// Test a CM known to be non-canonical.
		seq00 = new PitchClassSequence( "6 1 2 3 4 5" );
		seq01 = new PitchClassSequence( "6 7 8 9 A B" );
		seq10 = new PitchClassSequence( "0 1 2 3 4 5" );
		seq11 = new PitchClassSequence( "0 7 8 9 A B" );

		// create a simple CM with 1 Row and 2 Columns
		cm2x2 = new CompositionMatrix();
		
		cm2x2.addSegment(seq00, 0, 0);
		cm2x2.addSegment(seq01, 0, 1);
		cm2x2.addSegment(seq10, 1, 0);
		cm2x2.addSegment(seq11, 1, 1);
		System.out.println( CompositionMatrixUtils.format( cm2x2 ) );
		System.out.println( "Testing known non-canonical CM..." );
		assertFalse("Known non-canonical CM tests false",cm2x2.isCanonical(true, true));
		
		
	}
}
