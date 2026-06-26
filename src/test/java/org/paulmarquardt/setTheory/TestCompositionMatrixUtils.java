package org.paulmarquardt.setTheory;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;

import org.paulmarquardt.setTheory.CompositionMatrix;
import org.paulmarquardt.setTheory.Row;
import org.paulmarquardt.setTheory.utils.CompositionMatrixUtils;

public class TestCompositionMatrixUtils extends TestCase {

	List<Integer[]> testSets;
	protected void setUp() throws Exception {
		super.setUp();
		testSets = new ArrayList<Integer[]>();
		testSets.add( new Integer[] {0,11,1,2,10,8,5,3,9,4,7,6});
		testSets.add( new Integer[] {0,1,2,3,4,5,6,7,8,9,10,11});
		// Lyric Suite row!!
		testSets.add( new Integer[] {5,4,0,9,7,2,8,1,3,6,10,11});
	}

	public void testGetCompositionMatrix() {
		for ( Integer[] s : this.testSets) {
			Row row = new Row( s );
			for ( int i = 0; i <= 12; i++ ) {
				Integer[][] layout = { {i, 12 - i}, {12 - i, i} } ;
				Row[] rows = { row, row.R() };
				CompositionMatrix newCM = CompositionMatrixUtils.getCompositionMatrix(rows, layout);
				System.out.println( CompositionMatrixUtils.format( newCM ) );
			}
		}
	}	
}