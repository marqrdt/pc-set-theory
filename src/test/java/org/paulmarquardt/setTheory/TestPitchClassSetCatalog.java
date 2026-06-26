package org.paulmarquardt.setTheory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;
import java.util.logging.Logger;

import org.paulmarquardt.setTheory.*;

/**
 * Unit test for simple App.
 */
public class TestPitchClassSetCatalog 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
	
	PitchClassSet mainSet;
    public TestPitchClassSetCatalog( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TestPitchClassSetCatalog.class );
    }

    public void setUp() throws Exception {
    	super.setUp();
    	Integer[] testArray = {1,2,6,10,11};
    	this.mainSet = new PitchClassSet( testArray );
    	//System.out.println( "Main set: " + this.mainSet.toString() );
    }
    
    public void tearDown() throws Exception {
    	super.tearDown();
    	// some space between test runs
    	System.out.print("\n");
    }
    
    public void testGetNameByPitchClassSet() {
    	Iterator<PitchClassSet> pcSetIt = PitchClassSet.allPitchClassSetsIterator();
    	while ( pcSetIt.hasNext() ) {
    		PitchClassSet pcset = pcSetIt.next();
    		// My implementation of PitchClassSet allows for empty sets, but there is not FOrte number for the empty set.
    		// looking up PitchClassSet names is terribly expensive. Here. we'll just find a few.
    		if ( Math.random() > 0.98 ) {
    			System.out.println( String.format("%s is a %s set", pcset, PitchClassSetCatalog.getNameByPitchClassSet(pcset) ) );
    			assertNotNull( String.format("PitchClassSet name should not be null for real PitchClassSet %s", pcset.toString() ), PitchClassSetCatalog.getNameByPitchClassSet(pcset) );
    		}
    	}
    }
    
}
