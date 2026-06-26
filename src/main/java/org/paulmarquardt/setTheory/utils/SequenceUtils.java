package org.paulmarquardt.setTheory.utils;

import org.paulmarquardt.setTheory.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.*;
import java.time.Instant;

import org.paulmarquardt.setTheory.interfaces.ISequence;
import org.apache.commons.lang.math.IntRange;
import org.paukov.combinatorics3.*;

public class SequenceUtils {

	/**
	 * Given a length n, return a random PitchClassSequence of length n. The if the unique parameter is true, 
	 * the PitchClassSequence will not contains duplicate pcs. If unique is true and length &gt; 12, an
	 * IllegalArgumentException will be thrown.
	 * may contain duplicate pcs.
	 * @param length  The length of the returned PitchClassSequence.
	 * @param unique  If true, the generated sequence will not have any PC duplications.
	 * @return  A PitchClassSequence of the given length parameter.
	 */
	static public PitchClassSequence getRandomPitchClassSequence( int length, boolean unique ) throws IllegalArgumentException {
		PitchClassSequence pcSeq = SetTheoryFactories.getPitchClassSequence();
		Random randGen = new Random();
		if ( unique ) {
			if ( length > 12 ) {
				throw new IllegalArgumentException( String.format("A sequence of %d unique pitch classes is not possible", length));
			}
			List<Integer> aggregateSet = PitchClassSet.aggregate().getMembers();
			Collections.shuffle(aggregateSet);
			IntStream.range(0, length).forEach( i -> {
				pcSeq.addPitch(aggregateSet.get(i));
			});

		} else {
			randGen.ints(length, 0, 12).forEach( i -> {  pcSeq.addPitch(i); });
		}
		return pcSeq;
	}
		
	/**
	 * Given a length n, return a random PitchSequence of length n. The returned PitchSequence
	 * may contain duplicate pcs.
	 * @param length  The length of the returned PitchClassSequence.
	 * @param minPitch  The minimum pitch number (inclusive) that the sequence could contain.
	 * @param maxPitch  The maximum pitch number (inclusive) that the sequence could contain.
	 * @param unique  If true, the generated sequence will not have any PC duplications.
	 * @return  A PitchClassSequence of the given length parameter.
	 */
	static public PitchSequence getRandomPitchSequence( int length, int minPitch, int maxPitch, boolean unique ) {
		PitchSequence pSeq = SetTheoryFactories.getPitchSequence();
		Random randGen = new Random();
		if ( unique ) {
			List<Integer> rangeSet = new ArrayList<Integer>();
			IntStream.rangeClosed(minPitch, maxPitch).forEach( rangeSet::add );
			Collections.shuffle(rangeSet);
			while ( rangeSet.size() > 0 ) {
				int i = randGen.nextInt( rangeSet.size() );
				pSeq.addPitch( rangeSet.get( i ));
				rangeSet.remove(i);
			}
		} else {
			randGen.ints(length, minPitch, maxPitch + 1).forEach( i -> {  pSeq.addPitch(i); });
		}
		return pSeq;
	}

	/**
	 * getEmbeddedSubsequences accepts an ISequence sourceSeq and ISequence testSeq.
	 * It returns a List S of &lt;List&lt;Integer&gt;. Each List&lt;Integer&gt; in S contains the indices
     * at which testSeq is embedded in sourceSeq.
	 * If testSeq is not embedded in sourceSeq, it returns null.
     * Comparison operations on all members use plain mathematical equality, so if the caller
     * is comparing a PitchClassSequence against a PitchSequence, the comparisons will not be performed using mod 12.
	 * Example:
	 * IPitchSequence newSeq = new PitchClassSequence( [0,b,1,2,a,8,5,3,9,4,7,6] )
	 * IPitchSequence subSeq = new PitchClassSequence( [b,2,8,3] )
	 * newSeq.getEmbeddedSubsequences( subSeq ) = &lt;List 1,3,5,7&gt;
	 * IPitchSequence anotherSubSeq = new PitchClassSequence( [0,4,7,6] )
	 * newSeq.getEmbeddedSubsequences( subSeq ) returns List&lt;0,9,10,11&gt;
	 * If the caller does not contain the subSequence, it will return null
	 * IPitchSequence notASubSeq = new PitchClassSequence( [6,4,3,8] )
	 * newSeq.getEmbeddedSubsequence( subSeq ) = null
	 *
	 * @param sourceSeq  The ISequence in which the search is being preformed.
	 * @param testSeq  The ISequence which is searched for inclusion in sourceSeq.
	 * @return  A list of indices at which members of anotherPitchSequence can be found in this.
	 */
	public static List<List<Integer>> getEmbeddedSubsequences( ISequence sourceSeq, ISequence testSeq ) {
		// fail fast if inSeq has more members than this, making it impossible to be contained as a subsequence.
        List<List<Integer>> embeddedSequences = new ArrayList<>();
        List<List<Integer>> selections = new ArrayList<>();

		Logger bugLogger = Logger.getLogger( SequenceUtils.class.getName());
		bugLogger.info( String.format( "Testing AT you sukc at: %s", SequenceUtils.class.getName() ) );
		if ( sourceSeq.length() > testSeq.length() ) {
			return null;
		}
		testSeq.getMembers().stream().forEach( item ->
                {
					bugLogger.info( String.format( "Testing item %d", item ) );
                    List<Integer> indices = new ArrayList<>();
                    IntStream.range(0, sourceSeq.length()).forEach ( index -> {
                                if ( sourceSeq.getMembers().get(index).equals(item) ) {
                                    indices.add(index);
									bugLogger.info( String.format( "%d: found index at %d", item, index));
                                }
                            }
                    );
                    // only add the list of indices if it is non-empty.
                    if ( indices.size() > 0) {
						selections.add(indices);
					}
                }
        );
		//Generator.cartesianProduct( selections ).stream().forEach(
		//@SuppressWarnings("unchecked")
		ArrayList<Integer>[] listAsArray = new ArrayList[selections.size()];
		IntStream.range(0, selections.size()).forEach( index -> {
			listAsArray[index] = (ArrayList<Integer>) selections.get(index);
			//bugLogger.info(listAsArray[index].toString());
		});
		Generator.cartesianProduct( selections.toArray( listAsArray) ).stream().forEach(
				item -> {
					bugLogger.info(item.toString());
					if ( SequenceUtils.isAscending( item ) ) {
						embeddedSequences.add( item );
					}
				}
        );
		// ).collect( Collectors.toList() );
		return embeddedSequences;
	}

	private static boolean isAscending( List<Integer> items ) {
		boolean retVal = true;
		if ( items.size() < 2 ) {
			return true;
		}
		int index = 1;
		while ( index < items.size() ) {
			if  (items.get(index) < items.get(index - 1)) {
				return false;
			}
			index++;
		}
		return retVal;
	}
}

