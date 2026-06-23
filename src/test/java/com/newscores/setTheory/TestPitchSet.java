package com.newscores.setTheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import com.newscores.setTheory.PitchClassSet;
import com.newscores.setTheory.PitchSet;
import com.newscores.setTheory.utils.SetUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Unit test for simple App.
 */
public class TestPitchSet extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
	
	PitchSet mainSet;
	
    public TestPitchSet( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TestPitchSet.class );
    }

    public void setUp() throws Exception {
    	super.setUp();
    	Integer[] testArray = {60,71,65,54,64,70,81,74,51};
    	this.mainSet = new PitchSet( testArray );
    	System.out.println( "Main set: " + this.mainSet.toString() );
    }
    
    public void tearDown() throws Exception {
    	super.tearDown();
    	// some space between test runs
    	System.out.print("\n");
    }

    public void testSetEquals()
    {
    	//Integer[] testArray = {60,71,65,54,64,70,81,74,51};
    	//System.out.println( "Test set: " + newSet.toString() );
    	//System.out.println( "Transposed Test set: " + newSet.T(trans).toString() );
        assertTrue( "A PitchSet should be equal itself.", this.mainSet.equals( this.mainSet ) );
        assertFalse( "A PitchSet should not be equal to a non-zero transposition.", this.mainSet.equals( this.mainSet.T(4) ) );
        
    }

    public void testTransposedSetStaticEquals()
    {
    	//Integer[] testArray = {60,71,65,54,64,70,81,74,51};
    	int trans = 4;
    	PitchSet newSet = new PitchSet( new Integer[]{ 64,75,69,58,68,74,85,78,55 }  );
    	//System.out.println( "Test set: " + newSet.toString() );
    	//System.out.println( "Transposed Test set: " + newSet.T(trans).toString() );
        assertTrue( "Transposed sets should be equal.", this.mainSet.T(trans).equals(newSet) );
    }

    public void testInvertedSetEquals()
    {
    	Integer[] invertedArray = { 81,51,78,72,68,67,62,58,61 };
    	int trans = 2;
    	PitchSet newSet = new PitchSet( invertedArray );
    	//System.out.println( "Inverted Test set: " + newSet.toString() );
        assertTrue( "Inverted set should be equal to set.I().", this.mainSet.I().equals(newSet) );        
    }
        
    public void testContainsPitch()
    {
    	// test case for set with one element
    	int testP = 60;
        assertTrue( String.format( "Contains pitch test1: set %s contains pitch %d.", this.mainSet.toString(), testP ),
        		this.mainSet.containsPitch(60) );  
        testP = 61;
        assertFalse( String.format( "Contains pitch test1: set %s does not contain pitch %d.", this.mainSet.toString(), testP ),
        		this.mainSet.containsPitch(61) );        
    }
    
    public void testSetComplement()
    {
    	// test case for set with one element
    	Integer[] compArray = new Integer[]{52,53,55,56,57,58,59,61,62,63,66,67,68,69,72,73,75,76,77,78,79,80 };
    	PitchSet complementSet = new PitchSet( compArray );
        assertTrue( String.format( "Set complement test1: complement of set %s should be {2,4,5,6,9,10,11}.", this.mainSet.toString() ),
        		this.mainSet.complement().equals( complementSet ) );        
    }

    public void testSetExtend()
    {
    	// test case for set with one element
    	Integer[] array = new Integer[]{60,61,62,63,64};
    	Integer[] extendWith = new Integer[]{65,66,67,68};
    	Integer[] extendedSet = new Integer[]{60,61,62,63,64,65,66,67,68};
    	PitchSet newSet = new PitchSet( array );
    }
    
    public void testSetIntersection()
    {
    	//Integer[] testArray = {60,71,65,54,64,70,81,74,51};

    	PitchSet newSet = new PitchSet( new Integer[]{51,52,53,54,55,56,57,58,81} );

        assertTrue(
        		String.format( "Set intersection test1: intersection of set %s and set %s should be {51,54,81}.", newSet.toString(), newSet.T(2).toString() ),
        		this.mainSet.intersection( newSet ).equals( new PitchSet( new Integer[]{51,54,81} ) ) );        

        // intersection should be empty
    	newSet = new PitchSet( new Integer[]{1,2,55,73} );
        assertTrue(
        		String.format( "Set intersection test2: intersection of set %s and set %s should be {} (empty set).", newSet.toString(), newSet.T(5).toString() ),
        		newSet.intersection( newSet.T(5) ).equals( new PitchSet( new Integer[]{} ) ) );        

    	newSet = new PitchSet( new Integer[]{0} );
        assertTrue(
        		String.format( "Set complement test3: set %s with itself should be itself.", newSet.toString() ),
        		this.mainSet.intersection( this.mainSet ).equals( this.mainSet )
        );
    }

    public void testSetUion()
    {
        // baseSet is simple Major Scale on middle C.

        PitchSet baseSet = new PitchSet( new Integer[]{60,62,64,65,67,69,71} );
        PitchSet testSet = new PitchSet( new Integer[]{61,63,66,68,70} );
        PitchSet aggregateSet = new PitchSet( new Integer[]{60,61,62,63,64,65,66,67,68,69,70,71} );
        assertTrue(
                String.format( "Set union test: union of a set %s with itself should be equal to itself.", baseSet.toString() ),
                baseSet.union( baseSet ).equals( baseSet ) );

        assertTrue(String.format("Set union test: union of set %s and set %s is equal to %s. Actual: %s", baseSet.toString(), testSet.toString(), aggregateSet.toString(), baseSet.union(testSet).toString()),
                baseSet.union(testSet).equals(aggregateSet));
        // add some members not in baseSet...
        testSet = new PitchSet( new Integer[]{51,52,53,54,55,56,57,58,81} );
    }

    private int triangleFunction(int i ) {
        return (int) ( (i) * (i + 1)) / 2;
    }

    public void testIcVector()
    {
        // baseSet is simple Major Scale on middle C.
        int testMax = 32;
        Logger testLogger = Logger.getLogger( this.getClass().getName() );
        IntStream.range(0, testMax).forEach( i -> {
            PitchSet p = SetUtils.randomPitchSet( i, 0, 36);
            Integer[] icv = p.icVector();
            int icvSum = Arrays.stream(icv).reduce(0, Integer::sum);
            int triangleNum = triangleFunction( i - 1 );
            //testLogger.log(Level.INFO, String.format("ICV sum: %d, Triangle num: %d", icvSum, triangleNum));
            assertTrue("Sum of the elements of icVector is the triangle number for size n", icvSum == triangleNum );
            assertTrue( "PitchSet icVector is of the correct size", icv.length == 7);
            testLogger.log(Level.INFO, String.format("IC Vector for PitchSet %s is %s", p.toString(), Arrays.toString(p.icVector())));
        });
    }

    public void testPcVector()
    {
        // baseSet is simple Major Scale on middle C.
        int testMax = 32;
        Logger testLogger = Logger.getLogger( this.getClass().getName() );
        IntStream.range(0, testMax).forEach( i -> {
            PitchSet p = SetUtils.randomPitchSet( i, 0, 36);
            Integer[] pcv = p.pcVector();
            int pcvSum = Arrays.stream(pcv).reduce(0, Integer::sum);
            //testLogger.log(Level.INFO, String.format("ICV sum: %d, Triangle num: %d", icvSum, triangleNum));
            assertTrue("Sum of the elements of icVector is the size of the PitchSet", pcvSum == p.size() );
            assertTrue( "PitchSet icVector is of the correct size", pcv.length == 12);
            testLogger.log(Level.INFO, String.format("PC Vector for PitchSet %s is %s", p.toString(), Arrays.toString(p.pcVector())));
        });
    }

    public void testGetPitchClassSet()
    {
    	PitchSet newSet = new PitchSet( new Integer[]{30,43,56,69} );    	
    	//System.out.println( "Set from ArrayList: " + newSet.toString() );
        assertTrue( String.format("PitchClassSet of %s is %s and should be equal to %s.", newSet.toString(), newSet.getPitchClassSet().toString(), new PitchClassSet( new Integer[]{6,7,8,9} ).toString() ), newSet.getPitchClassSet().equals( new PitchClassSet( new Integer[]{6,7,8,9} ) ) );
    	newSet = new PitchSet( new Integer[]{60,50,40,30} );    	
    	//System.out.println( "Set from ArrayList: " + newSet.toString() );
        assertTrue( String.format("PitchClassSet of %s is %s and should be equal to %s.", newSet.toString(), newSet.getPitchClassSet().toString(), new PitchClassSet( new Integer[]{0,2,4,6} ).toString() ), newSet.getPitchClassSet().equals( new PitchClassSet( new Integer[]{0,2,4,6} ) ) );
    }

    public void testPitchSetCreation()
    {
    	List<Integer> emptyList = new ArrayList<Integer>();
    	PitchSet newSet = new PitchSet( emptyList );
    	//System.out.println( "Set from ArrayList: " + newSet.toString() );
        assertNotNull( String.format("Set %s created from empty ArrayList is not null.", newSet.toString() ), newSet );
        assertNotNull( String.format("Set %s created from empty ArrayList is initialized correctly.", newSet.getMembers() ), newSet );
        assertTrue( String.format("Set %s created from empty ArrayList is initialized correctly.", newSet ), newSet.getMembers().size() == 0 );
        
    	List<Integer> pcList = new ArrayList<Integer>();
    	Iterator<Integer> memberIt = this.mainSet.getMembers().iterator();
    	while ( memberIt.hasNext() ) {
    		pcList.add( memberIt.next() );
    	}
    	newSet = new PitchSet( pcList );
    	//System.out.println( "Set from ArrayList: " + newSet.toString() );
        assertNotNull( String.format("Set %s created from ArrayList is not null.", newSet.toString() ), newSet );
        assertNotNull( String.format("Set %s created from ArrayList is initialized correctly.", newSet.getMembers() ), newSet );
        assertTrue( String.format("Set %s created from ArrayList is initialized correctly.", newSet ), newSet.getMembers().equals( new ArrayList<Integer>( pcList ) ) );
    	    	
        newSet = new PitchSet( new Integer[]{} );
        assertNotNull( String.format("Set %s created from empty Integer[] array is not null.", newSet.toString() ), newSet );
        assertNotNull( String.format("Set %s created from empty Integer[] array is initialized correctly.", newSet.getMembers() ), newSet );
        assertTrue( String.format("Set %s created from empty empty Integer[] array is initialized correctly.", newSet ), newSet.getMembers().size() == 0 );
    	
    	Integer[] pcArray = new Integer[]{ 64,75,69,58,68,74,85,78,55 };
        newSet = new PitchSet( pcList );
        assertNotNull( String.format("Set %s created from ArrayList is not null.", newSet.toString() ), newSet );
        assertNotNull( String.format("Set %s created from ArrayList is initialized correctly.", newSet.getMembers() ), newSet );
        assertTrue( String.format("Set %s created from empty ArrayList is initialized correctly.", newSet ), newSet.getMembers().equals( new ArrayList<Integer>( pcList ) ) );

        String pitchSetString = "64,75,69,58,68,74,85,78,55";
        newSet = new PitchSet( pitchSetString );
        assertTrue( String.format("Set %s created from String equals PitchSet created from Array of the same members.", newSet ), newSet.equals( new PitchSet(pcArray) ) );

        pitchSetString = "64 75 69 58 68 74 85 78 55";
        newSet = new PitchSet( pitchSetString );
        assertTrue( String.format("Set %s created from String equals PitchSet created from Array of the same members.", newSet ), newSet.equals( new PitchSet(pcArray) ) );

        pitchSetString = "75 64 69 58 68 85 74 78 55";
        newSet = new PitchSet( pitchSetString );
        assertTrue( String.format("Set %s created from String equals PitchSet created from Array of the same members.", newSet ), newSet.equals( new PitchSet(pcArray) ) );

        pitchSetString = "{75 64,69 58randomjunk68]85 74weeijr78 55}";
        newSet = new PitchSet( pitchSetString );
        assertTrue( String.format("Set %s created from String equals PitchSet created from Array of the same members.", newSet ), newSet.equals( new PitchSet(pcArray) ) );
    }

    public void testAsType()
    {
    	PitchClassSet pcSet = this.mainSet.asType(PitchClassSet.class);
        assertEquals( String.format("PitchClassSet created from PitchSet using asType() is a PitchClassSet: %s is a PitchClassSet.", pcSet.toString() ), pcSet.getClass(), PitchClassSet.class );
        assertTrue( String.format("PitchClassSet created from PitchSet using asType() is correct: %s, %s.", pcSet.toString(), this.mainSet.toString() ), pcSet.equals( new PitchClassSet( new Integer[] {0,2,3,4,5,6,9,10,11} ) ) );
    }

    public void testSetFromList()
    {
    	List<Integer> pcList = new ArrayList<Integer>();
    	Iterator<Integer> memberIt = this.mainSet.getMembers().iterator();
    	while ( memberIt.hasNext() ) {
    		pcList.add( memberIt.next() );
    	}
    	int trans = 3;
    	PitchSet newSet = new PitchSet( pcList );
    	//System.out.println( "Set from ArrayList: " + newSet.toString() );
        assertTrue( String.format("Set created from ArrayList: %s should be equal to original set %s.", newSet.toString(), this.mainSet.toString() ), this.mainSet.equals(newSet) );
    }
    
    public void testGetAscendingIntervals()
    {
    	Integer[] pitchArray = new Integer[] { 81,51,78,72,68,67,62,58,61 };
    	PitchSet newSet = new PitchSet( pitchArray );
    	// { 51,58,61,62,67,68,72,78,81 }
    	Integer[] expectedIntervals = new Integer[] { 7,3,1,5,1,4,6,3 };
    	System.out.println( "Ascending intervals for PitchSet  { 81,51,78,72,68,67,62,58,61 } : " + newSet.getAscendingIntervals().toString() );
        assertTrue( "Ascending intervals should be equal to expected value.", newSet.getAscendingIntervals().equals( Arrays.asList(expectedIntervals)) );        
    }

    public void testTransposedSetSelfEquals()
    {
        assertTrue( "A set transposed twice by two intervals should be equal to the set transpoed by the sum of the intervals.", this.mainSet.T(7).equals( this.mainSet.T(3).T(4))  );
    }

}
