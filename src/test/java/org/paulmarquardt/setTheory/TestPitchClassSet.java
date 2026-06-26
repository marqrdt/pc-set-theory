package org.paulmarquardt.setTheory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;
import java.util.logging.Logger;

//import org.paulmarquardt.setTheory.*;
import org.paulmarquardt.setTheory.PitchClassSet;

/**
 * Unit test for simple App.
 */
public class TestPitchClassSet extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */

	PitchClassSet mainSet;
	Logger log;
	public TestPitchClassSet(String testName) {
		super(testName);
		log = Logger.getLogger( this.getClass().getName() );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TestPitchClassSet.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		Integer[] testArray = { 1, 2, 6, 10, 11 };
		this.mainSet = new PitchClassSet(testArray);
		// System.out.println( "Main set: " + this.mainSet.toString() );
	}

	public void tearDown() throws Exception {
		super.tearDown();
		// some space between test runs
		System.out.print("\n");
	}

	public void testPitchClassAggregate() {
		PitchClassSet newSet = PitchClassSet.aggregate();
		// System.out.println( "Test set: " + newSet.toString() );
		assertTrue(String.format("Aggregate set has cardinailty of %d.", PitchClassSet.MODULUS),
				newSet.size() == PitchClassSet.MODULUS);
		assertTrue(String.format("Complement of aggregate set is empty set.", PitchClassSet.MODULUS),
				newSet.complement().equals(new PitchClassSet()));
	}

	public void testPitchClassMod() {
		for (int num = -1000; num <= 1000; num++) {
			assertTrue(
					String.format("mod function for number %d should always be >= %d and < %d ", num, 0,
							PitchClassSet.MODULUS),
					PitchClassSet.mod(num, PitchClassSet.MODULUS) < PitchClassSet.MODULUS
							&& PitchClassSet.mod(num, PitchClassSet.MODULUS) >= 0);
		}
	}

	public void testPitchClassConstructor() {
		Integer[] setArray1 = { 3, 4, 8, 0, 1 };
		PitchClassSet newSet = new PitchClassSet(setArray1);
		PitchClassSet newSetFromSequence;
		System.out.println(
				String.format("Test set: %s has these members: %s", newSet.toString(), newSet.getMembers().toString()));
		assertEquals(String.format("Sets created from array %s has contents %s.", newSet.toString(),
				Arrays.asList(setArray1).toString()), newSet.getMembers().size(), setArray1.length);
		// System.out.println( "PCSet created with empty array" + new PitchClassSet( new
		// Integer[]{} ).toString() );
		// Same set, but order switched around...
		newSet = new PitchClassSet(Arrays.asList(setArray1));
		// System.out.println( "Test set: " + newSet.toString() );
		assertEquals(String.format("Sets created from array %s has contents %s.", newSet.toString(),
				Arrays.asList(setArray1).toString()), newSet.getMembers().size(), setArray1.length);

		// PitchClassSet from long array in Integers
		Integer[] setArray2 = { 1, 3, 2, 4, 5, 3, 6, 8, 7, 9, 12, 12, 23, 25, 24, 35, 37, 22, 15, 24, 73, 46, 53, 64,
				49 };
		newSet = new PitchClassSet(setArray2);
		assertTrue(
				"A PitchClassSet created from an array is equal to a PitchClassSet created from a IPitchSequence created from the same array",
				newSet.equals(new PitchClassSet(new PitchSequence(setArray2))));
		System.out.println(
				String.format("Test set: %s has these members: %s", newSet.toString(), newSet.getMembers().toString()));
		// Same set, but order switched around...
		// System.out.println( "Test set: " + newSet.toString() );
		assertEquals(
				String.format("PitchClassSet created from array %s is equal to PitchClassSet created from List %s.",
						newSet.toString(), Arrays.asList(setArray2).toString()),
				newSet.getMembers(), PitchClassSet.aggregate().getMembers());

		// PitchClassSet from long array in Integers
		Integer[] setArray3 = { 10000000, 10000001, 10000002, 10000003, 10000004, 10000005, 10000006, 10000007,
				10000008 };
		newSet = new PitchClassSet(setArray3);
		System.out.println(
				String.format("Test set: %s has these members: %s", newSet.toString(), newSet.getMembers().toString()));
		assertEquals(String.format("Sets created from array %s has contents %s.", newSet.toString(),
				Arrays.asList(setArray3).toString()), newSet.getMembers().size(), setArray3.length);
		// System.out.println( "PCSet created with empty array" + new PitchClassSet( new
		// Integer[]{} ).toString() );
		// Same set, but order switched around...
		newSet = new PitchClassSet(Arrays.asList(setArray3));
		// System.out.println( "Test set: " + newSet.toString() );
		assertEquals(String.format("Sets created from array %s has contents %s.", newSet.toString(),
				Arrays.asList(setArray3).toString()), newSet.getMembers().size(), setArray3.length);

		// PitchClassSet from negative Integers
		Integer[] setArray4 = { -10000000, -10000001, -10000002, -10000003, -10000004, -10000005, -10000006, -10000007,
				-10000008 };
		newSet = new PitchClassSet(setArray4);
		System.out.println(
				String.format("Test set: %s has these members: %s", newSet.toString(), newSet.getMembers().toString()));
		assertEquals(String.format("Sets created from array %s has contents %s.", newSet.toString(),
				Arrays.asList(setArray4).toString()), newSet.getMembers().size(), setArray4.length);
		// System.out.println( "PCSet created with empty array" + new PitchClassSet( new
		// Integer[]{} ).toString() );
		// Same set, but order switched around...
		newSet = new PitchClassSet(Arrays.asList(setArray4));
		// System.out.println( "Test set: " + newSet.toString() );
		assertEquals(String.format("Sets created from array %s has contents %s.", newSet.toString(),
				Arrays.asList(setArray4).toString()), newSet.getMembers().size(), setArray4.length);

		// PitchClassSet from mixed positive and negative Integers
		Integer[] setArray5 = { -10000000, -10000001, -10000002, -10000003, -10000004, -10000005, -10000006, -10000007,
				-10000008, -10000009, -10000008, -10000010, -10000011, 10000000, 10000001, 10000002, 10000003, 10000004,
				10000005, 10000006, 10000007, 10000008, 10000009, 10000010, 10000011 };
		newSet = new PitchClassSet(setArray5);
		System.out.println(
				String.format("Test set: %s has these members: %s", newSet.toString(), newSet.getMembers().toString()));
		assertTrue(String.format("Sets created from array %s has contents %s.", newSet.toString(),
				Arrays.asList(setArray5).toString()), newSet.equivalent(PitchClassSet.aggregate()));
		// System.out.println( "PCSet created with empty array" + new PitchClassSet( new
		// Integer[]{} ).toString() );
		// Same set, but order switched around...
		newSet = new PitchClassSet(Arrays.asList(setArray5));
		// System.out.println( "Test set: " + newSet.toString() );
		assertTrue(String.format("Sets created from list %s has contents %s.", newSet.toString(),
				Arrays.asList(setArray5).toString()), newSet.equivalent(PitchClassSet.aggregate()));

		newSet = new PitchClassSet();
		// System.out.println( "Test set: " + newSet.toString() );
		assertNotNull(newSet);
		assertTrue(newSet.equals(new PitchClassSet()));

	}

	public void testPitchClassSetFromString() {
		Integer[] setArray1 = { 3, 4, 8, 0, 1 };
		String setAsString = "34801";
		PitchClassSet newSet = new PitchClassSet(setArray1);
		// System.out.println( "Test set: " + newSet.toString() );
		System.out.println("This is the set for the String: " + new PitchClassSet(setAsString).toString());
		assertTrue(String.format("Sets created from array %s is equal to set created from String '%s'.",
				newSet.toString(), setAsString), newSet.equals(new PitchClassSet(setAsString)));
		// System.out.println( "PCSet created with empty array" + new PitchClassSet( new
		// Integer[]{} ).toString() );
		// Same set, but order switched around...
		setAsString = "84310";
		// System.out.println( "Test set: " + newSet.toString() );
		assertTrue(String.format("Sets created from array %s is equal to set created from String '%s'.",
				newSet.toString(), setAsString), newSet.equals(new PitchClassSet(setAsString)));

		// Testing with set containg 10 and 11.
		Integer[] setArray2 = { 0, 2, 4, 3, 10, 11 };
		newSet = new PitchClassSet(setArray2);
		setAsString = "0243ab";
		// System.out.println( "Test set: " + newSet.toString() );
		assertTrue(String.format("Sets created from array %s is equal to set created from String '%s'.",
				newSet.toString(), setAsString), newSet.equals(new PitchClassSet(setAsString)));

		// different order
		setAsString = "ab4320";
		// System.out.println( "Test set: " + newSet.toString() );
		assertTrue(String.format("Sets created from array %s is equal to set created from String '%s'.",
				newSet.toString(), setAsString), newSet.equals(new PitchClassSet(setAsString)));

		// testing 't' and 'e' for ten and eleven.
		setAsString = "te4320";
		// System.out.println( "Test set: " + newSet.toString() );
		assertTrue(String.format("Sets created from array %s is equal to set created from String '%s'.",
				newSet.toString(), setAsString), newSet.equals(new PitchClassSet(setAsString)));

		// testing 't' and 'e' mixed with 'a' and 'b'.
		setAsString = "tb4320";
		// System.out.println( "Test set: " + newSet.toString() );
		assertTrue(String.format("Sets created from array %s is equal to set created from String '%s'.",
				newSet.toString(), setAsString), newSet.equals(new PitchClassSet(setAsString)));

		// testing 't' and 'e' mixed with 'a' and 'b'.
		setAsString = "ae4320";
		// System.out.println( "Test set: " + newSet.toString() );
		assertTrue(String.format("Sets created from array %s is equal to set created from String '%s'.",
				newSet.toString(), setAsString), newSet.equals(new PitchClassSet(setAsString)));

		setAsString = "a$b%4 * 3#2-0";
		// System.out.println( "Test set: " + newSet.toString() );
		assertTrue(String.format("Sets created from array %s is equal to set created from String '%s'.",
				newSet.toString(), setAsString), newSet.equals(new PitchClassSet(setAsString)));

	}

	public void testPitchClassSetFromInteger() {
		int bottom = (int) Math.pow(2, PitchClassSet.MODULUS) * -1;
		PitchClassSet pcSet;
		for ( int signature = bottom; signature <= (int) Math.pow(2, PitchClassSet.MODULUS) * 2; signature++ ) {
			pcSet = new PitchClassSet( signature );
			assertTrue( String.format("PitchClassSet created from signature %d equals a set created from getSignature()", signature),
				pcSet.equals( PitchClassSet.getPitchClassSetFromSignature( signature ) ) );					
		}
		pcSet = new PitchClassSet( Integer.MAX_VALUE );
		assertTrue( String.format("PitchClassSet created from signature %d equals a set created from getSignature()", Integer.MAX_VALUE),
				pcSet.equals( PitchClassSet.getPitchClassSetFromSignature( Integer.MAX_VALUE ) ) );
		pcSet = new PitchClassSet( Integer.MIN_VALUE );		
		assertTrue( String.format("PitchClassSet created from signature %d equals a set created from getSignature()", Integer.MIN_VALUE),
				pcSet.equals( PitchClassSet.getPitchClassSetFromSignature( Integer.MIN_VALUE ) ) );					
	}

	/*
	 * This Tests tests both the size() and length() methods.
	 */
	public void testPitchClassSizeAndLength() {
		for ( int i = 0; i <= 12; i++ ) {
			Map<String,PitchClassSet> pcSetList = PitchClassSetCatalog.getPitchClassSetsByCardinality( i );
			for ( String setName : pcSetList.keySet() ) {
				assertTrue("Cardinality of returned PitchClassSet is correct", PitchClassSetCatalog.getPitchClassSetByName(setName).size() == i);
			}
		}
	}

	public void testPitchClassSetSetEquals() {
		Integer[] setArray1 = { 3, 4, 8, 0, 1 };
		PitchClassSet newSet = new PitchClassSet(setArray1);
		// System.out.println( "Test set: " + newSet.toString() );
		assertTrue("Sets created from same set but order-transposed should be equal.",
				newSet.equals(new PitchClassSet(new Integer[] { 1, 0, 4, 3, 8 })));
		System.out.println("PCSet created with default constructor" + new PitchClassSet().toString());
		System.out.println("PCSet created with empty array" + new PitchClassSet(new Integer[] {}).toString());
		assertTrue("PCSet created with default constructor should be equal to PCSet created from empty array.",
				new PitchClassSet().equals(new PitchClassSet(new Integer[] {})));

		PitchClassSet aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3 });
		PitchClassSet bSet = new PitchClassSet(new Integer[] { 4, 5, 6, 7 });
		assertTrue("Unequal PCSets should not be equal.", !aSet.equals(bSet));
		// a few more equals tests with varying degrees of invariance

		aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 4 });
		bSet = new PitchClassSet(new Integer[] { 4, 5, 6, 8, 9 });
		assertTrue("Unequal PCSets should not be equal.", !aSet.equals(bSet));
		aSet = new PitchClassSet(new Integer[] { 0, 2, 5, 8, 9 });
		bSet = new PitchClassSet(new Integer[] { 5, 6, 7, 8, 10 });
		assertTrue("Unequal PCSets should not be equal.", !aSet.equals(bSet));
	}

	public void testPitchClassSetEquivalent() {
		Integer[] setArray1 = { 3, 4, 8, 0, 1 };
		PitchClassSet newSet = new PitchClassSet(setArray1);
		assertTrue("PCSet created with default constructor should be equivalent to PCSet created from empty array.",
				new PitchClassSet().equivalent(new PitchClassSet(new Integer[] {})));

		// a few tests for transposition
		PitchClassSet aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3 });
		PitchClassSet bSet = new PitchClassSet(new Integer[] { 4, 5, 6, 7 });
		assertTrue("PCSets which are transpositions of each other should be equivalent.", aSet.equivalent(bSet));

		// A few tests for inversion...
		aSet = new PitchClassSet(new Integer[] { 0, 2, 3, 4, 5, 7 });
		bSet = new PitchClassSet(new Integer[] { 4, 6, 7, 8, 9, 11 });
		assertTrue("PCSets which are transpositions of each other should be equivalent.", aSet.equivalent(bSet));

		// A few tests for inversion...
		aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 4, 5 });
		bSet = new PitchClassSet(new Integer[] { 2, 3, 5, 6, 7 });
		assertTrue("PCSets which are inversions of each other should be equivalent.", aSet.equivalent(bSet));

		aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 6, 8, 9 });
		bSet = new PitchClassSet(new Integer[] { 2, 3, 5, 9, 10, 11 });
		assertTrue("PCSets which are inversions of each other should be equivalent.", aSet.equivalent(bSet));

		// a few more equals tests with varying degrees of invariance
		aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 4 });
		bSet = new PitchClassSet(new Integer[] { 4, 5, 6, 8, 9 });
		assertFalse("Unequal PCSets should not be equivalent.", aSet.equivalent(bSet));

		aSet = new PitchClassSet(new Integer[] { 0, 2, 5, 8, 9 });
		bSet = new PitchClassSet(new Integer[] { 5, 6, 7, 8, 10 });
		assertFalse("Unequal PCSets should not be equivalent.", aSet.equivalent(bSet));

		aSet = new PitchClassSet(new Integer[] { 0, 2, 3, 4, 5, 7 });
		bSet = new PitchClassSet(new Integer[] { 4, 6, 7, 8, 10, 11 });
		assertFalse("Unequal PCSets should not be equivalent.", aSet.equivalent(bSet));
	}

	public void testPitchClassSetEquivalentOnSteriods() {
		assertTrue("PCSet created with default constructor should be equivalent to PCSet created from empty array.",
				new PitchClassSet().equivalent(new PitchClassSet(new Integer[] {})));

		/*
		 * Iterate over all 4096 PCSets and test each set against all of its
		 * transformations.
		 */
		Iterator<PitchClassSet> pcSetIt = PitchClassSet.allPitchClassSetsIterator();
		while (pcSetIt.hasNext()) {
			PitchClassSet pcSet = pcSetIt.next();
			Iterator<PitchClassSet> pcSetTransformIt = pcSet.iterator();
			while (pcSetTransformIt.hasNext()) {
				PitchClassSet transform = pcSetTransformIt.next();
				assertTrue(String.format("Transformations of set %s should be equivalent to its transformation %s",
						pcSet.toString(), transform.toString()), pcSet.equivalent(transform));
			}
		}
		// a few tests for transposition
		PitchClassSet aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3 });
		PitchClassSet bSet = new PitchClassSet(new Integer[] { 4, 5, 6, 7 });

		// a few more equals tests with varying degrees of invariance
		aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 4 });
		bSet = new PitchClassSet(new Integer[] { 4, 5, 6, 8, 9 });
		assertFalse("Unequal PCSets should not be equivalent.", aSet.equivalent(bSet));

		aSet = new PitchClassSet(new Integer[] { 0, 2, 5, 8, 9 });
		bSet = new PitchClassSet(new Integer[] { 5, 6, 7, 8, 10 });
		assertFalse("Unequal PCSets should not be equivalent.", aSet.equivalent(bSet));

		aSet = new PitchClassSet(new Integer[] { 0, 2, 3, 4, 5, 7 });
		bSet = new PitchClassSet(new Integer[] { 4, 6, 7, 8, 10, 11 });
		assertFalse("Unequal PCSets should not be equivalent.", aSet.equivalent(bSet));
	}

	public void testIsSubset() {

		PitchClassSet aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3 });
		PitchClassSet bSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4 });
		assertTrue(String.format("PitchClass Set aSet %s is a subset of bSet %s : proper subset", aSet.toString(),
				bSet.toString()), aSet.isSubset(bSet));

		aSet = new PitchClassSet(new Integer[] { 1, 2, 4, 6, 8, 9 });
		bSet = new PitchClassSet(new Integer[] { 1, 2, 3, 4, 6, 8, 9, 10 });
		assertTrue(String.format("PitchClass Set aSet %s is a subset of bSet %s : proper subset", aSet.toString(),
				bSet.toString()), aSet.isSubset(bSet));

		// a few more equals tests with varying degrees of invariance
		aSet = new PitchClassSet(new Integer[] { 0, 2, 3, 5, 7 });
		bSet = new PitchClassSet(new Integer[] { 8, 7, 6, 5, 3, 2, 0 });
		assertTrue(String.format("PitchClass Set aSet %s is a subset of bSet %s : proper subset", aSet.toString(),
				bSet.toString()), aSet.isSubset(bSet));

		aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3 });
		bSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4 });
		assertTrue(String.format("PitchClass Set aSet %s is a subset of bSet %s : proper subset", aSet.toString(),
				bSet.toString()), aSet.isSubset(bSet));

		aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 6, 7, 8 });
		bSet = new PitchClassSet(new Integer[] { 0, 1, 2, 6, 7, 8 });
		assertTrue(String.format(
				"PitchClass Set aSet %s is a subset of bSet %s : if aSet and bSet are equal, aSet.isSubset(bSet) is true",
				aSet.toString(), bSet.toString()), aSet.isSubset(bSet));

		aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 6, 7, 8 });
		bSet = new PitchClassSet(new Integer[] { 0, 1, 2, 6, 7, 8 });
		assertTrue(String.format(
				"PitchClass Set aSet %s is a subset of bSet %s : if aSet and bSet are equal, bSet.isSubset(aSet) is true",
				aSet.toString(), bSet.toString()), bSet.isSubset(aSet));

		// a few more equals tests with varying degrees of invariance
		aSet = new PitchClassSet(new Integer[] { 0, 2, 3, 5, 7 });
		bSet = new PitchClassSet(new Integer[] { 1, 2, 3, 6, 9, 10 });
		assertFalse(String.format("PitchClass Set aSet %s is a subset of bSet %s : proper subset", aSet.toString(),
				bSet.toString()), aSet.isSubset(bSet));
	}

	public void testPitchClassContainsPitch() {
		// test containsPitch() for an arbitrary set.
		Integer[] setArray1 = { 3, 4, 8, 0, 1 };
		PitchClassSet newSet = new PitchClassSet(setArray1);
		System.out.println(
				String.format("Test set: %s has these members: %s", newSet.toString(), newSet.getMembers().toString()));
		for (int pitch : newSet.getMembers()) {
			assertTrue(String.format("Test containsPitch for set %s should return true for member %d",
					newSet.toString(), pitch), newSet.containsPitch(pitch));
		}

		// test containsPitch() for another arbitrary set.
		Integer[] setArray2 = { 0, 2, 3, 4, 6, 9, 10 };
		newSet = new PitchClassSet(setArray2);
		for (int pitch : newSet.getMembers()) {
			assertTrue(String.format("Test containsPitch for set %s should return true for member %d",
					newSet.toString(), pitch), newSet.containsPitch(pitch));
		}

		// test containsPitch() for another arbitrary set.
		Integer[] setArray3 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		newSet = new PitchClassSet(setArray3);
		for (int pitch : newSet.getMembers()) {
			assertTrue(String.format("Test containsPitch for set %s should return true for member %d",
					newSet.toString(), pitch), newSet.containsPitch(pitch));
		}

		newSet = new PitchClassSet();

		// Empty set should return false for all pitch-classes
		assertNotNull(newSet);
		for (int pitch : Arrays.asList(setArray3)) {
			assertFalse(String.format("Empty set should not contain pitch-class %d", pitch),
					newSet.containsPitch(pitch));
		}

	}

	/*
	 * This test just prints out all the tranformations of the set. No actual
	 * verification performed.
	 **/
	public void testIterator() {
		PitchClassSet pcSet = new PitchClassSet("9 a 3 b 4 6");
		Iterator<PitchClassSet> pcsIt = pcSet.iterator();
		while (pcsIt.hasNext()) {
			System.out.println(pcsIt.next().toString());
		}
	}

	/*
	 * A very simple test. Every PCSet from the set of all PCSets should be equal to
	 * the PCSet created from its signature.
	 */

	public void testPitchClassSetSignature() {
		Iterator<PitchClassSet> pcsIt = PitchClassSet.allPitchClassSetsIterator();
		while (pcsIt.hasNext()) {
			PitchClassSet pcSet = pcsIt.next();
			int sig = pcSet.getSignature();
			PitchClassSet sigSet = PitchClassSet.getPitchClassSetFromSignature(sig);
			// System.out.println( String.format( "Original set is %s and set from signature
			// %d is %s", pcSet.toString(), sig, sigSet.toString() ) );
			assertTrue(pcSet.equals(sigSet));
		}
	}

	public void testTransposedSetStaticEquals() {
		Integer[] transposedArray = { 3, 4, 8, 0, 1 };
		int trans = 2;
		PitchClassSet newSet = new PitchClassSet(transposedArray);
		// System.out.println( "Test set: " + newSet.toString() );
		// System.out.println( "Transposed Test set: " + newSet.T(trans).toString() );
		assertTrue("Transposed sets should be equal.", this.mainSet.T(trans).equals(newSet));
	}

	public void testInvertedSetEquals() {
		Integer[] invertedArray = { 11, 10, 6, 2, 1 };
		int trans = 2;
		PitchClassSet newSet = new PitchClassSet(invertedArray);
		// System.out.println( "Inverted Test set: " + newSet.toString() );
		assertTrue("Inverted set should be equal to set.I().", this.mainSet.I().equals(newSet));
	}

	public void testMultiplicationSetEquals() {
		Integer[] invertedArray = { 11, 10, 6, 2, 1 };
		int trans = 2;
		PitchClassSet newSet = new PitchClassSet(invertedArray);
		// System.out.println( "Inverted Test set: " + newSet.toString() );
		assertTrue("Original set should be equal to set.M().M().", this.mainSet.equals(this.mainSet.M().M()));
	}

	public void testNormalForm() {
		// test case for set from default constructor
		PitchClassSet newSet = new PitchClassSet();
		assertTrue(String.format("Normal Form test1: normal form of %s = [0].", newSet.toString()),
				newSet.getNormalForm().equals(new PitchClassSet().getNormalForm()));

		// test case for set with one element
		newSet = new PitchClassSet(new Integer[] { 5 });
		assertTrue(String.format("Normal Form test1: normal form of %s = [0].", newSet.toString()),
				newSet.getNormalForm().equals(new PitchClassSet(new Integer[] { 0 })));

		// test case for two-element set [27] should be [05]
		newSet = new PitchClassSet(new Integer[] { 2, 7 });
		assertTrue(String.format("Normal Form test1: normal form of %s = [0].", newSet.toString()),
				newSet.getNormalForm().equals(new PitchClassSet(new Integer[] { 0, 5 })));

		// test case for all-interval tetrachord
		newSet = new PitchClassSet(new Integer[] { 2, 6, 8, 9 });
		System.out.println(
				String.format("Set: %s\tNormal Form: %s", newSet.toString(), newSet.getNormalForm().toString()));
		// System.out.println( "T(N)I of Set: " + newSet.T( 12 - Collections.max(
		// newSet.pcSetAsArrayList() ) ).I().toString() );
		// Normal form of [2689] should be [0137]
		assertTrue(String.format("Normal Form test1: normal form of %s = [0137].", newSet.toString()),
				newSet.getNormalForm().equals(new PitchClassSet(new Integer[] { 0, 1, 3, 7 })));

		// test case 5 note symmetrical set
		newSet = new PitchClassSet(new Integer[] { 2, 1, 6, 10, 11 });
		System.out.println(
				String.format("Set: %s\tNormal Form: %s", newSet.toString(), newSet.getNormalForm().toString()));
		// System.out.println( "T(N)I of Set: " + newSet.T( 12 - Collections.max(
		// newSet.pcSetAsArrayList() ) ).I().toString() );
		// Normal form of [2689] should be [0137]
		assertTrue(String.format("Normal Form test1: normal form of %s = [0137].", newSet.toString()),
				newSet.getNormalForm().equals(new PitchClassSet(new Integer[] { 0, 1, 3, 4, 8 })));

	}

	public void testIcVector() {

		// test empty set
		PitchClassSet newSet = new PitchClassSet(new Integer[] {});
		System.out.println("\n");
		System.out.println(
				String.format("IC Vector of set %s is: %s", newSet.toString(), Arrays.toString(newSet.icVector())));
		assertTrue(String.format("IC Vector test1: ic vector of %s = {0,0,0,0,0,0}.", newSet.toString()),
				Arrays.equals(newSet.icVector(), new Integer[] { 0, 0, 0, 0, 0, 0 }));

		// test set with one element. should still have IC vector all zeros
		newSet = new PitchClassSet(new Integer[] { 0 });
		assertTrue(String.format("IC Vector test1: ic vector of %s = {0,0,0,0,0,0}.", newSet.toString()),
				Arrays.equals(newSet.icVector(), new Integer[] { 0, 0, 0, 0, 0, 0 }));

		// test set with two elements {2,6}. IC vector should be {0,0,0,1,0,0}
		newSet = new PitchClassSet(new Integer[] { 2, 6 });
		/**
		 * for ( int i = 0; i < newSet.icVector().length; i++ ) { System.out.print(
		 * String.format( "%d ", newSet.icVector()[i] )); }
		 **/
		assertTrue(String.format("IC Vector test2: ic vector of %s = {0,0,0,1,0,0}.", newSet.toString()),
				Arrays.equals(newSet.icVector(), new Integer[] { 0, 0, 0, 1, 0, 0 }));

		// I like D major
		newSet = new PitchClassSet(new Integer[] { 2, 6, 9 });
		assertTrue(String.format("IC Vector test3: ic vector of %s (Major chord) = {0,0,1,1,1,0}.", newSet.toString()),
				Arrays.equals(newSet.icVector(), new Integer[] { 0, 0, 1, 1, 1, 0 }));

		// System.out.println("========================");
		newSet = new PitchClassSet(new Integer[] { 2, 6, 8, 9 });
		assertTrue(String.format("IC Vector test4: ic vector of %s (all-interval tetrachord) = {1,1,1,1,1,1}.",
				newSet.toString()), Arrays.equals(newSet.icVector(), new Integer[] { 1, 1, 1, 1, 1, 1 }));

		newSet = new PitchClassSet(new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10 });

		System.out.println("");
		assertTrue(String.format("IC Vector test5: ic vector of %s (8 note chromatic set) = {7,6,5,4,4,2}.",
				newSet.toString()), Arrays.equals(newSet.icVector(), new Integer[] { 7, 6, 5, 4, 4, 2 }));

	}

	public void testSetFromList() {
		List<Integer> pcList = new ArrayList<Integer>();
		Iterator<Integer> memberIt = this.mainSet.pcSetAsArrayList().iterator();
		while (memberIt.hasNext()) {
			pcList.add(memberIt.next());
		}
		int trans = 3;
		PitchClassSet newSet = new PitchClassSet(pcList);
		System.out.println("Set from ArrayList: " + newSet.toString());
		assertTrue("Set created from ArrayList should be equal to original set.", this.mainSet.equals(newSet));
	}

	public void testSetFromPitchSet() {
		PitchSet pSet = new PitchSet(new Integer[] { 24, 36, 30, 31 });
		int trans = 3;
		PitchClassSet newPCSet = new PitchClassSet(pSet);
		System.out.println("Set from PitchSet: " + pSet.toString());
		PitchClassSet referenceSet = new PitchClassSet(new Integer[] { 0, 6, 7 });
		assertTrue("Set created from PitchSet should be equal to original set.", newPCSet.equals(referenceSet));
	}

	public void testTransposedSetSelfEquals() {
		assertTrue(
				"A set transposed twice by two intervals should be equal to the set transpoed by the sum of the intervals.",
				this.mainSet.T(7).equals(this.mainSet.T(3).T(4)));
	}

	public void testComplement() {
		// 12-note set
		PitchClassSet aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
		// 12-note set
		PitchClassSet bSet = new PitchClassSet();
		System.out.println("aSet: " + aSet.toString() + " complement: " + aSet.complement().toString());
		System.out.println("bSet: " + bSet.toString());
		assertTrue("Complement of the 12-note set is the empty set.", aSet.complement().equals(bSet));
		assertTrue("Complement of the empty set is the 12-note set.", bSet.complement().equals(aSet));

		assertTrue("Complement set chromatic hexchord: 012345 complement is 6789AB :",
				new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4, 5 }).complement()
						.equals(new PitchClassSet(new Integer[] { 6, 7, 8, 9, 10, 11 })));
		assertTrue("Complement set whole-tone collection: 02468A complement is 13579B :",
				new PitchClassSet(new Integer[] { 0, 2, 4, 6, 8, 10 }).complement()
						.equals(new PitchClassSet(new Integer[] { 1, 3, 5, 7, 9, 11 })));
		assertTrue("Complement set arbitrary set: 4568 complement is 012379AB :",
				new PitchClassSet(new Integer[] { 4, 5, 6, 8 }).complement()
						.equals(new PitchClassSet(new Integer[] { 0, 1, 2, 3, 7, 9, 10, 11 })));

		System.out.println(String.format("Set : %s :: complement: %s",
				new PitchClassSet(new Integer[] { 1, 3, 5, 7, 9, 10, 11 }).toString(),
				new PitchClassSet(new Integer[] { 1, 3, 5, 7, 9, 10, 11 }).complement().toString()));
		assertFalse("Complement set : 13579AB complement is not 02468A :",
				new PitchClassSet(new Integer[] { 1, 3, 5, 7, 9, 10, 11 }).complement()
						.equals(new PitchClassSet(new Integer[] { 0, 2, 4, 6, 8, 10 })));
		assertFalse("Complement set arbitrary set: 2567 complement is not 01379AB :",
				new PitchClassSet(new Integer[] { 2, 5, 6, 7 }).complement()
						.equals(new PitchClassSet(new Integer[] { 0, 1, 3, 7, 8, 9, 10, 11 })));
	}

	public void testIntersection() {
		// 12-note set
		PitchClassSet aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
		// 12-note set
		PitchClassSet bSet = new PitchClassSet();
		System.out.println("aSet: " + aSet.toString() + " intersection: " + aSet.intersection(bSet).toString());
		System.out.println("bSet: " + bSet.toString());
		assertTrue("Intersection of the 12-note set and empty set is the empty set.",
				aSet.intersection(bSet).equals(bSet));
		assertTrue("Intersection of the empty set and the 12-note set is the empty set.",
				bSet.intersection(aSet).equals(bSet));
		aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4, 5 });
		bSet = new PitchClassSet(new Integer[] { 3, 4, 5, 6, 7, 8 });
		// Arbitrary intersections
		assertTrue("Intersection of PCSets 012345 and 345678 is PCSet 345 :",
				new PitchClassSet(aSet).intersection(bSet).equals(new PitchClassSet(new Integer[] { 3, 4, 5 })));
		aSet = new PitchClassSet(new Integer[] { 0, 2, 4, 6, 8, 10 });
		bSet = new PitchClassSet(new Integer[] { 1, 2, 3, 5, 6, 7, 8 });
		assertTrue("Intersection of PCSets: 02468A and 1235678 :",
				aSet.intersection(bSet).equals(new PitchClassSet(new Integer[] { 2, 6, 8 })));
		assertTrue("Complement set arbitrary set: 4568 complement is 012379AB :",
				new PitchClassSet(new Integer[] { 4, 5, 6, 8 }).complement()
						.equals(new PitchClassSet(new Integer[] { 0, 1, 2, 3, 7, 9, 10, 11 })));

		System.out.println(String.format("Set : %s :: complement: %s",
				new PitchClassSet(new Integer[] { 1, 3, 5, 7, 9, 10, 11 }).toString(),
				new PitchClassSet(new Integer[] { 1, 3, 5, 7, 9, 10, 11 }).complement().toString()));
		assertFalse("Complement set : 13579AB complement is not 02468A :",
				new PitchClassSet(new Integer[] { 1, 3, 5, 7, 9, 10, 11 }).complement()
						.equals(new PitchClassSet(new Integer[] { 0, 2, 4, 6, 8, 10 })));
		assertFalse("Complement set arbitrary set: 2567 complement is not 01379AB :",
				new PitchClassSet(new Integer[] { 2, 5, 6, 7 }).complement()
						.equals(new PitchClassSet(new Integer[] { 0, 1, 3, 7, 8, 9, 10, 11 })));
	}

	public void testUnion() {
		// 12-note set
		// 12-note set
		PitchClassSet aSet = PitchClassSet.aggregate();
		PitchClassSet bSet = new PitchClassSet();
		// System.out.println( "aSet: " + aSet.toString() + " union: " + aSet.union(
		// bSet ).toString() );
		// System.out.println( "bSet: " + bSet.toString() );
		assertTrue("Union of the 12-note set and empty set is the empty set.", aSet.union(bSet).equals(aSet));
		assertTrue("Union of the empty set and the 12-note set is the empty set.", bSet.union(aSet).equals(aSet));
		aSet = PitchClassSet.aggregate();
		bSet = new PitchClassSet(new Integer[] { 3, 4, 5, 6, 7, 8 });
		// Arbitrary intersections
		Iterator<PitchClassSet> pcSetIt = PitchClassSet.allPitchClassSetsIterator();
		while (pcSetIt.hasNext()) {
			PitchClassSet pcSet = pcSetIt.next();
			Iterator<PitchClassSet> pcSetTransformIt = pcSet.iterator();
			while (pcSetTransformIt.hasNext()) {
				PitchClassSet transform = pcSetTransformIt.next();
				assertTrue(
						String.format("Union of the aggregate and an arbitrary set %s is the aggregate",
								transform.toString()),
						PitchClassSet.aggregate().union(transform).equals(PitchClassSet.aggregate()));
			}
		}
		aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4, 5 });
		bSet = new PitchClassSet(new Integer[] { 3, 4, 5, 6, 7, 8 });
		// Arbitrary intersections
		assertTrue("Union of PCSets 012345 and 345678 is PCSet 345 :", new PitchClassSet(aSet).union(bSet)
				.equals(new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 })));
		aSet = new PitchClassSet(new Integer[] { 0, 2, 4, 6, 8, 10 }); // whole tone scale on 0
		bSet = new PitchClassSet(new Integer[] { 1, 3, 5, 7, 9, 11 }); // whole-tone scale on 1
		assertTrue("Union of PCSets: 02468A and 1235678 :",
				aSet.union(bSet).equals(new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 })));
		aSet = new PitchClassSet(new Integer[] {}); // whole tone scale on 0
		bSet = new PitchClassSet(new Integer[] {}); // whole-tone scale on 1
		assertTrue("Union of empty PCSets is empty", aSet.union(bSet).equals(new PitchClassSet(new Integer[] {})));
	}

	public void testSubsets() {
		PitchClassSet aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4, 6 });
		Iterator<PitchClassSet> subsetListIterator = aSet.subsetList().iterator();
		// Test List method
		log.info("Iterating through all subsets using List<PitchClassSet> method");
		int subsetCount = 0;
		while (subsetListIterator.hasNext()) {
			System.out.println(subsetListIterator.next().toString());
			subsetCount++;
		}
		assertTrue(String.format("Subset count %d equals expected %d", subsetCount, (int) Math.pow(2, aSet.size())),
				subsetCount == (int) Math.pow(2, aSet.size()));
		log.info("Iterating through all subsets using Iterator<PitchClassSet> method");
		// Test Iterator method
		Iterator<PitchClassSet> subsetIterator = aSet.subsetIterator();
		subsetCount = 0;
		while (subsetIterator.hasNext()) {
			log.info(subsetIterator.next().toString());
			subsetCount++;
		}
		assertTrue(String.format("Subset count %d equals expected %d", subsetCount, (int) Math.pow(2, aSet.size())),
				subsetCount == (int) Math.pow(2, aSet.size()));
		log.info(
				"Iterating through all subsets of a specified cardinality using Iterator<PitchClassSet> method");
		// Test Iterator method
		int testCardinality = aSet.size();
		while (testCardinality >= 0) {
			subsetIterator = aSet.subsetIterator(testCardinality);
			while (subsetIterator.hasNext()) {
				PitchClassSet nextSet = subsetIterator.next();
				log.info(nextSet.toString());
				//log.info("");
				assertTrue(String.format("Subset cardinality %d equals expected cardinality %d", nextSet.size(),
						testCardinality), nextSet.size() == testCardinality);
			}
			testCardinality--;
		}
	}

	public void testAbstractInclude() {
		PitchClassSet aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4, 6 });
		Iterator<PitchClassSet> subsetListIterator = aSet.subsetList().iterator();
		// Test List method
		log.info("Testing abstractIncludes() method");
		Iterator<PitchClassSet> subsetIterator = aSet.subsetIterator();
		while ( subsetListIterator.hasNext() ) {
			PitchClassSet nextSet = subsetListIterator.next();
			Iterator<PitchClassSet> pcSetIterator = nextSet.iterator();
			while ( pcSetIterator.hasNext() ) {
				assertTrue("A PCSet should abtractly include all transformations of it subsets", aSet.abstractIncludes(pcSetIterator.next()));
			}
		}
		
		/*
		 * Some edge cases
		 */
		// Empty set includes itself.
		assertTrue("The Empty set included itself.",new PitchClassSet().abstractIncludes( new PitchClassSet() ) );
		// Aggregate set includes itself.
		assertTrue("The Empty set included itself.", PitchClassSet.aggregate().abstractIncludes( PitchClassSet.aggregate() ) );
		// known false statements
		assertFalse("The Empty set does not include any non-empty set.", new PitchClassSet().abstractIncludes( new PitchClassSet("0") ) );
		assertFalse("The sets smaller than the aggregate do not include the aggregate.", aSet.abstractIncludes( PitchClassSet.aggregate() ) );
		
	}

	public void testIntervalSpread() {
		// 12-note set
		PitchClassSet aSet;
		for (int i = 0; i < PitchClassSet.MODULUS; i++) {
			aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
			// 12-note set
			assertTrue(String.format("Interval Spread of PitchClassSet %s is %d :", aSet.T(i).toString(),
					aSet.T(i).intervalSpread()), aSet.T(i).intervalSpread() == 1);
			aSet = new PitchClassSet(new Integer[] { 0, 2, 4, 6, 8, 10 });
			assertTrue(String.format("Interval Spread of PitchClassSet %s is %d :", aSet.T(i).toString(),
					aSet.T(i).intervalSpread()), aSet.T(i).intervalSpread() == 1);

			aSet = new PitchClassSet(new Integer[] { 0, 3, 6, 9 });
			assertTrue(String.format("Interval Spread of PitchClassSet %s is %d :", aSet.T(i).toString(),
					aSet.T(i).intervalSpread()), aSet.T(i).intervalSpread() == 1);
			aSet = new PitchClassSet(new Integer[] { 0, 4, 8 });
			assertTrue(String.format("Interval Spread of PitchClassSet %s is %d :", aSet.T(i).toString(),
					aSet.T(i).intervalSpread()), aSet.T(i).intervalSpread() == 1);
		}
		for (int i = 0; i < PitchClassSet.MODULUS; i++) {
			aSet = new PitchClassSet(new Integer[] { 0, 1, 3, 4, 6, 7, 9, 10 });
			// 12-note set
			assertTrue(String.format("Interval Spread of PitchClassSet %s is %d :", aSet.T(i).toString(),
					aSet.T(i).intervalSpread()), aSet.T(i).intervalSpread() == 2);
			aSet = new PitchClassSet(new Integer[] { 0, 2, 4, 5, 7, 9, 11 });
			assertTrue(String.format("Interval Spread of PitchClassSet %s is %d :", aSet.T(i).toString(),
					aSet.T(i).intervalSpread()), aSet.T(i).intervalSpread() == 2);

			aSet = new PitchClassSet(new Integer[] { 0, 1, 2, 4, 5, 6, 8, 9, 10 });
			assertTrue(String.format("Interval Spread of PitchClassSet %s is %d :", aSet.T(i).toString(),
					aSet.T(i).intervalSpread()), aSet.T(i).intervalSpread() == 2);
			aSet = new PitchClassSet(new Integer[] { 0, 2, 4, 7, 9 });
			assertTrue(String.format("Interval Spread of PitchClassSet %s is %d :", aSet.T(i).toString(),
					aSet.T(i).intervalSpread()), aSet.T(i).intervalSpread() == 2);
			aSet = new PitchClassSet(new Integer[] { 0, 3, 4, 7, 8, 11 });
			assertTrue(String.format("Interval Spread of PitchClassSet %s is %d :", aSet.T(i).toString(),
					aSet.T(i).intervalSpread()), aSet.T(i).intervalSpread() == 2);
		}
	}
}
