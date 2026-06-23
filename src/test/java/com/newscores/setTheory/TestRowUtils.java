
package com.newscores.setTheory;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;

import com.newscores.setTheory.Row;
import com.newscores.setTheory.interfaces.*;
import com.newscores.setTheory.utils.CompositionMatrixUtils;
import com.newscores.setTheory.utils.RowUtils;
import java.util.logging.Logger;


public class TestRowUtils extends TestCase {

	List<Integer[]> testSets;
	Logger log;
	protected void setUp() throws Exception {
		super.setUp();
		testSets = new ArrayList<Integer[]>();
		testSets.add( new Integer[] {0,11,1,2,10,8,5,3,9,4,7,6});
		testSets.add( new Integer[] {0,1,2,3,4,5,6,7,8,9,10,11});
		// Lyric Suite row!!
		testSets.add( new Integer[] {5,4,0,9,7,2,8,1,3,6,10,11});
		log = Logger.getLogger( "TestRowUtils" );
	}

	public void testGetTransformationString() {
		try {
			Row p = RowUtils.randomRow();
			Row tr4 = p.T(4);
			assertEquals( RowUtils.getTransformationString( p, tr4), "T[4]" );
			assertEquals( RowUtils.getTransformationString( tr4, p), "T[8]" );		
			Iterator<ImmutablePitchClassSequence> transIt = p.transformationIterator();
			while ( transIt.hasNext() ) {
				Row newRow = new Row( transIt.next() );
				String trans = RowUtils.getTransformationString( p, newRow);
				if ( trans != null ) {
					System.out.println( String.format("Transformation %s will transfrom %s into %s", trans, newRow, p));
				}
			}
		} catch ( IllegalArgumentException exc ) {
			exc.printStackTrace();
		}
	}
	
	public void testTransformationIterator() {
		try {
			Row p = RowUtils.randomRow();
			Iterator<ImmutablePitchClassSequence> transIt = p.transformationIterator();
			while ( transIt.hasNext() ) {
                            Row newRow = new Row( transIt.next() );
                            System.out.println( String.format("Row: %s", newRow));
			}
		} catch ( IllegalArgumentException exc ) {
			exc.printStackTrace();
		}
	}

	public void testWholeToneTransformationIterator() {
		try {
			Row p = RowUtils.randomRow();
			Iterator<ImmutablePitchClassSequence> transIt = p.transformationIterator();
			while ( transIt.hasNext() ) {
                            Row newRow = new Row( transIt.next() );
                            System.out.println( String.format("Row: %s", newRow));
			}
		} catch ( IllegalArgumentException exc ) {
			exc.printStackTrace();
		}
	}

	public void testRandomRow() {
		int numberOfRandomRows = 40;
		int count = 0;
		System.out.println( String.format("Printing %d random rows using RowUtils.randomRow().", numberOfRandomRows) );
		while ( count < numberOfRandomRows ) {
			System.out.println( RowUtils.randomRow().toString() );
			count++;
		}
	}

	public void testGetMatrix() {
		try {
			Row p = RowUtils.randomRow();
			int expectedDimension = 12;
			CompositionMatrix matrixCM = RowUtils.getMatrix( p );
			assertTrue( String.format("Matrix created from Row p: %s should have %d rows", p.toString(), expectedDimension ),
					matrixCM.getNumberOfRows() == expectedDimension );
			assertTrue( String.format("Matrix created from Row p: %s should have %d columns", p.toString(), expectedDimension ),
					matrixCM.getNumberOfColumns() == expectedDimension );
			for ( int i = 0; i < expectedDimension; i++ ) {
				for ( int j = 0; i < expectedDimension; i++ ) {
					assertNotNull( String.format("Segment at row %d and column %d should not be null", i, j ),
							matrixCM.getSegmentAtCoordinate( i, j) );
					assertTrue( String.format("Segment at row %d and column %d should be a PitchClassSequence", i, j ),
							matrixCM.getSegmentAtCoordinate( i, j).getClass().equals( CMSegment.class ) );
					assertTrue( String.format("Segment at row %d and column %d should be a PitchClassSequence", i, j ),
							matrixCM.getSegmentAtCoordinate( i, j).getPitchSequence().length() == 1 );
				}
			}
			// Print it just to make sure.
			System.out.println( CompositionMatrixUtils.format( matrixCM, " ", " " ) );
			Iterator<ImmutablePitchClassSequence> transIt = p.transformationIterator();
			while ( transIt.hasNext() ) {
				Row newRow = new Row( transIt.next() );
				String trans = RowUtils.getTransformationString( p, newRow);
				if ( trans != null ) {
					System.out.println( String.format("Transformation %s will transfrom %s into %s", trans, newRow, p));
				}
			}
		} catch ( IllegalArgumentException exc ) {
			exc.printStackTrace();
		}
	}

	/*
	public void testGetCombinorialCMs() {
		int numberOfRandomRows = 20;
		int count = 0;
		int cmSize = 3;
		int maxNumCMs = 200;
		System.out.println( String.format("Printing Combinatorial CMs for %d random rows", numberOfRandomRows) );
		while ( count < numberOfRandomRows ) {
			Row testRow = RowUtils.randomRow();
			System.out.println( String.format("Printing Combinatorial CMs for row: %s", testRow.toString()) );
			List<CompositionMatrix> combinatorialCMs = RowUtils.getCombinorialCMs( (Row) testRow.transposeTo(0), 12, cmSize, 1, maxNumCMs);
			combinatorialCMs.stream().forEach( cm -> {
				System.out.println( CompositionMatrixUtils.format( cm, " ", "|", true ) );
			});
			//System.out.println( RowUtils.randomRow().toString() );
			// PRMGenerator<List<List<Integer>>> orthogonalMatrixIterator(int maxSize, int partSize) {
			//for ( List<List<Integer>> element : RowUtils.orthogonalMatrixIterator(4, 12) ) {
			//	System.out.println( element.toString() );
			//}
		}
	}
	*/
	/*
	public void testRowFormIterator() {
		Iterator<Row> rowFormIt = RowUtils.allRowFormsIterator();
		while( rowFormIt.hasNext() ) {
			log.info( rowFormIt.next().toStringExtended());
		}
	}
	 */
}
