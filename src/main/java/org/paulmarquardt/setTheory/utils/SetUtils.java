package org.paulmarquardt.setTheory.utils;

import org.paulmarquardt.setTheory.PitchClassSet;
import org.paulmarquardt.setTheory.PitchSet;
import java.util.*;
import java.util.stream.*;
import java.time.Instant;

public class SetUtils {

	/**
	 * SetTheoryHelper exposes a set of utility operations that do not belong directly on the base classes.
	 */
	public SetUtils() {

	}

	/**
	 * Returns a random PitchClassSet of the given size.
	 * @param size  The size of the random PitchClassSet to return.
	 * @return  A PitchClassSet consisting of the maximum intersection of the two PitchClassSets.
	 */
	static public PitchClassSet randomPitchClassSet( int size ) {
		PitchClassSet pSet = new PitchClassSet();
		Random randGen = new Random();
		// size is taken mod 12. Failure to do this would cause an infinite loop.
		while ( pSet.size() < size % 12 ) {
			int i = randGen.nextInt( PitchClassSet.MODULUS );
			pSet.addPitch( i );
		}
		return pSet;
	}


	/**
	 * Returns a random PitchClassSet of the given size.
	 * @param size  The size of the random PitchClassSet to return.
	 * @return  A PitchClassSet consisting of the maximum intersection of the two PitchClassSets.
	 */
	static public PitchSet randomPitchSet( int size, int minPitch, int range ) {
		PitchSet pSet = new PitchSet();
		Random randGen = new Random();
		// size is taken mod 12. Failure to do this would cause an infinite loop.
		while ( pSet.size() < size ) {
			int i = randGen.nextInt( range + 1 ) + minPitch ;
			pSet.addPitch( i );
		}
		return pSet;
	}

	/**
	 * Given two PitchClassSets, return the PitchClassSet created from the maximal intersection of set1 and set2.
	 * The comparison is made against all transformations of the set, not just the forms passed in.
	 * @param set1  The first PitchClassSet used for the comparison.
	 * @param set2  The second PitchClassSet used for the comparison.
	 * @return  A PitchClassSet consisting of the maximum intersection of the two PitchClassSets.
	 */
	static public PitchClassSet getMaxIntersection(PitchClassSet set1,
			PitchClassSet set2) {
		PitchClassSet intersectionSet = new PitchClassSet();
		int intersection = 0;

		Iterator<PitchClassSet> pcsIt = set2.iterator();
		while (pcsIt.hasNext()) {
			PitchClassSet currentSet = (PitchClassSet) pcsIt.next();
			if (set1.intersection(currentSet).size() > intersection) {
				intersectionSet = set1.intersection(currentSet);
				intersection += 1;
			}
		}
		return intersectionSet;
	}
	
}
