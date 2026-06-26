package org.paulmarquardt.setTheory;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;

import org.paulmarquardt.setTheory.PitchClassSet;
import org.paulmarquardt.setTheory.PitchSequence;
import org.paulmarquardt.setTheory.Row;
import org.paulmarquardt.setTheory.interfaces.*;

public class TestScale extends TestCase {

	Map<String,Integer[]> testScales;
	protected void setUp() throws Exception {
		super.setUp();
		testScales = new HashMap<String,Integer[]>();
		// Major scale.
		testScales.put("Major", new Integer[] { 2, 2, 1, 2, 2, 2, 1});
		testScales.put("Octatonic", new Integer[] { 2, 1 });
		testScales.put("Chromatic", new Integer[] { 1 });
		testScales.put("Whole-Tone", new Integer[] { 2 });
		testScales.put("Messiaen-4", new Integer[] { 1, 1, 2 });
		testScales.put("Messiaen-6", new Integer[] { 2, 2, 1, 1 });
		testScales.put("Custom-1", new Integer[] { 2, 1, 1, 2, 1, 1, 1, 2, 1 });
	}

	public void testScaleConstructor() {
		Scale majorScale = new Scale ( testScales.get("Major"), 24, 72 );
		System.out.println( majorScale.toString() );
		Scale octatonicScale = new Scale ( testScales.get("Octatonic"), 24, 72 );
		System.out.println( octatonicScale.toString() );
		Scale chromatic = new Scale ( testScales.get("Chromatic"), 24, 72 );
		System.out.println( chromatic.toString() );
		Scale wholetone = new Scale ( testScales.get("Whole-Tone"), 24, 72 );
		System.out.println( wholetone.toString() );
		Scale messiaen4 = new Scale ( testScales.get("Messiaen-4"), 24, 72 );
		System.out.println( messiaen4.toString() );
		Scale messiaen6 = new Scale ( testScales.get("Messiaen-6"), 24, 72 );
		System.out.println( messiaen6.toString() );
		Scale custom1 = new Scale ( testScales.get("Custom-1"), 24, 72 );
		System.out.println( custom1.toString() );
	}
	
	public void testScaleConstructorFromStatics() {
		Scale myScale = new Scale ( Scale.MAJOR, 24, 72 );
		System.out.println( myScale.toString() );
		myScale = new Scale ( Scale.HARMONIC, 24, 72 );
		System.out.println( myScale.toString() );
		myScale = new Scale ( Scale.HARMONIC_MINOR, 24, 72 );
		System.out.println( myScale.toString() );
		myScale = new Scale ( Scale.MESSIAEN1, 24, 72 );
		System.out.println( myScale.toString() );
	}

	public void testGetPitchSetAtDegree() {
		Scale myScale = new Scale ( Scale.MAJOR, 24, 72 );
		List<Integer> degrees = Arrays.asList( new Integer[] {0,2,4} );
		PitchSet pSet = myScale.getPitchSetAtDegrees( degrees );
		List<Integer> expectedPitches = Arrays.asList( new Integer[] {24,28,31} );
		assertEquals("Members of scale at degrees retun expected result", pSet.getMembers(), new ArrayList<Integer>( expectedPitches ) );

		myScale = new Scale ( Scale.OCTATONIC, 24, 72 );
		degrees = Arrays.asList( new Integer[] {0,1,2,3,4,5} );
		pSet = myScale.getPitchSetAtDegrees( degrees );
		expectedPitches = Arrays.asList( new Integer[] {24,26,27,29,30,32} );
		assertEquals("Members of scale at degrees retun expected result", pSet.getMembers(), new ArrayList<Integer>( expectedPitches ) );
	}
	
}
