package org.paulmarquardt.setTheory.utils;

import java.util.*;

import org.paulmarquardt.setTheory.Contour;
import org.paulmarquardt.setTheory.PitchClassSequence;
import org.paulmarquardt.setTheory.PitchClassSet;
import org.paulmarquardt.setTheory.PitchSequence;
import org.paulmarquardt.setTheory.interfaces.*;

/**
 * A Contour describes a shape in space and time. It is a Compositional pattern (in the sense of the Composition Design Pattern, not Music Composition) consisting
 * of a List of Integer wrapped with several useful methods. Each element of the Contour describes a location in vertical space relative
 * to all other members. For a simple example, a Contour created from the List [0,2,4,6,8,10] (an ascending whole-tone scale) would be
 * [0, 2, 3, 4, 5 ]. In a more complex example, a Contour created from the List [ 12, 10, 7, 13, 16, 15 ] would be [ 2, 1, 0, 3, 5, 4 ]. 
 * In musical space, a Contour is usually interpreted as the shape of a melody.
 * The set of members of a Contour will always include the range of the Integers between 0 and the size of the List. The ordering of this
 * range reflects the shape of the contour. It can contain duplicates where duplicate elements represent elements at the same horizontal slot (often interpreted as pitch).
 */
public class ContourUtils {

	private List<Integer> members;
	private String descriptor;

	public static PitchSequence mapContour( Contour inContour, PitchClassSequence inPCSeq ) {
		//PitchSequence outSeq = new PitchSequence();
		Integer[] pitchArray = new Integer[ inPCSeq.length() ];
		for ( int elem : pitchArray ) {
			pitchArray[ elem ] = 0;
		}
		// special case to get the first/lowest element in the Contour.
		int index = inContour.getMembers().indexOf( 0 );
		pitchArray[ index ] = inPCSeq.getMembers().get( index );
		int highest = pitchArray[ index ];
		for ( int m = 1; m < inContour.length(); m++ ) {
			index = inContour.getMembers().indexOf( m );
			if ( inPCSeq.getMembers().get( index ) > highest ) {
				pitchArray[ index ] = inPCSeq.getMembers().get( index );
			} else {
				pitchArray[ index ] = inPCSeq.getMembers().get( index ) + PitchClassSet.MODULUS;
				highest = pitchArray[ index ];
			}
		}
		return new PitchSequence( pitchArray );
	}

}
