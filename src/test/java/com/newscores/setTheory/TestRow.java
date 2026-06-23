package com.newscores.setTheory;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;

import com.newscores.setTheory.PitchClassSet;
import static com.newscores.setTheory.PitchClassSet.A;
import static com.newscores.setTheory.PitchClassSet.A;
import com.newscores.setTheory.PitchSequence;
import com.newscores.setTheory.Row;
import com.newscores.setTheory.interfaces.*;
import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class TestRow extends TestCase {

	List<Row> testRows = new ArrayList<Row>();
	String testFileName = "com/newscores/setTheory/TestRows.json";
	String testJson;

	protected void setUp() throws Exception {
		super.setUp();
		this.testJson = new String();
		try
        {
			String testFilePath = Thread.currentThread().getContextClassLoader().getResource(testFileName).getFile();
			testJson = new String ( Files.readAllBytes( Paths.get(testFilePath) ) );
			assertTrue("Ensure that JSON data is populated",testJson.length() > 0 );
            JSONObject rowsJson = new JSONObject(testJson);
            JSONArray rowsArray = (JSONArray) rowsJson.get("rows");
            for ( int i = 0; i < rowsArray.length(); i++ ) {
                JSONObject rowElement = (JSONObject) rowsArray.get(i);
                Row testRow = new Row( (String) rowElement.get("pitches") );
                testRow.setName( (String) rowElement.get("name"));
                testRows.add(testRow);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}

	/*
	protected void setUp() throws Exception {
		testSets = new ArrayList<Integer[]>();
		testSets.add( new Integer[] {0,11,1,2,10,8,5,3,9,4,7,6});
		testSets.add( new Integer[] {0,1,2,3,4,5,6,7,8,9,10,11});
		// Lyric Suite row!!
		testSets.add( new Integer[] {5,4,0,9,7,2,8,1,3,6,10,11});
		
		testSetsString = new ArrayList<String>();
		testSetsString.add( new String("0b12a8539476"));
		testSetsString.add( new String("9a3b46017825"));
	}
	*/

	public void testNewRow() {
		for ( Row r : this.testRows) {
			Row row = new Row( r.getMembers() );
			Integer[] s = r.getMembers().toArray( new Integer[0] );
			/**
			System.out.println( "Printing row from: " );
			for ( int index = 0 ; index < s.length; index++ ) {
				System.out.print( s[index] );
				System.out.print( "," );
			}
			System.out.println( "\nRow: " );
			for ( int member : row.getMembers() ) {
				System.out.print( member );
				System.out.print( "," );
			}
			System.out.println( "\n" );
			**/
			int index = 0;
			for ( int member : row.getMembers() ) {
				assertTrue( "Row members equals the array it was created from", member == s[ index ] );
				index++;
			}
		}
	}

	public void testRowArrayOfInteger() {
		for ( Row r : this.testRows) {
			Row row = new Row( r.getMembers() );
			Integer[] s = r.getMembers().toArray( new Integer[0] );
			int index = 0;
			for ( int member : row.getMembers() ) {
				assertTrue( "Test Row from Integer[]: members equals the array it was created from", member == s[ index ] );
				index++;
			}
		}
	}

	public void testRowCollectionOfInteger() {
		for ( Row r : this.testRows) {
			Row row = new Row( r.getMembers() );
			int index = 0;
			for ( int member : row.getMembers() ) {
				assertTrue( "Test Row from Collection<Integer>: members equals the array it was created from", member == r.getMembers().get(index) );
				index++;
			}
		}
	}

	public void testRowString() {
		for ( Row r : this.testRows) {
			try {
				Row row = new Row(r.toString());
			} catch (IllegalArgumentException iae) {
				fail(String.format("Unable to construct Row with string %s", r.toString()));
			}
		}
	}

	public void testRowT() {
		int transpositions = 12;
		for ( Row r : this.testRows) {
			for ( int trans = 0; trans < transpositions; trans ++ ) {
				Row transposed = new Row( r.T(trans).getMembers() );
				for ( int element = 0; element < r.length(); element++ ) {
					assertTrue( "Elements of transposed set are correct",
							transposed.getMembers().get(element) == PitchClassSet.mod( r.getMembers().get(element) + trans, PitchClassSet.MODULUS ) );
				}
			}
		}		
	}

	public void testRowR() {
		for ( Row r : this.testRows) {
			Row reversed = new Row( r.R() );
			for ( int index = 0; index < r.length(); index++ ) {
				//System.out.println( String.format("%d : %d", original.getMembers().get(index), reversed.getMembers().get( original.length() - index - 1) ) );
				assertTrue( "Elements of retrograde row are correct",
						reversed.getMembers().get(index) == r.getMembers().get( r.length() - index - 1 ) );
			}
		}
	}
	
	public void testRowI() {
		for ( Row r : this.testRows) {
			Row inverted = new Row( r.I() );
			for ( int index = 0; index < r.length(); index++ ) {
				//System.out.println( String.format("%d : %d", original.getMembers().get(index), reversed.getMembers().get( original.length() - index - 1) ) );
				assertTrue( "Elements of inverted row are correct",
						inverted.getMembers().get(index) == PitchClassSet.mod( 12 - r.getMembers().get(index), PitchClassSet.MODULUS ) );
			}
		}
	}
	
	public void testRowM() {
		// Only M() transformations of [1,5,7,11} will produce a 12-tone set.
		Integer[] mults = {1,5,7,11};
		for ( Row r : this.testRows) {
			for ( int mult = 0; mult < mults.length; mult++ ) {
				Row multRow = new Row( r.M( mults[mult]) );
				for ( int index = 0; index < r.length(); index++ ) {
					//System.out.println( String.format("%d : %d", original.getMembers().get(index), reversed.getMembers().get( original.length() - index - 1) ) );
					assertTrue( "Elements of M transformation row are correct",
							multRow.getMembers().get(index) == PitchClassSet.mod( r.getMembers().get(index) * mults[mult], PitchClassSet.MODULUS ) );
				}
			}
		}
	}
	
	public void testRowTransformationIterator() {
		for ( Row r: this.testRows) {
			System.out.println(String.format("Original set: %s", r.toString() ) );
			//Row original = new Row( new Integer[] {0,1,2,3,4,5,6,7,8,9,10,11} );
			Iterator<ImmutablePitchClassSequence> rowIt = r.transformationIterator();
			int count = 0;
			while ( rowIt.hasNext() ) {
				Row nextRow = new Row(rowIt.next());
				System.out.println( String.format("%s : %s", nextRow.getDescriptor(), nextRow.toString() ) );
				count++;
			}
			System.out.println("==========\n\n");
			assertTrue(String.format("Found %d transformations, expecting %d",  count, 96), count == 96 );
		}
	}
	
	public void testGetTransformation() {
		for ( Row r: this.testRows) {
			assertNotNull(r.getTransformation());
			Row transposed = r.T(4);
			//assertEquals( transposed.getTransformation().getTransposition(), 4 );
			Iterator<ImmutablePitchClassSequence> transIt = r.transformationIterator();
			while (transIt.hasNext()) {
				Row newRow = new Row(transIt.next());
				System.out.println(String.format("Transformation %s will trans from %s into %s", newRow.getTransformation().toString(), newRow, r));
			}
		}
	}
}
