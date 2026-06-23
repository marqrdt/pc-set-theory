
package com.newscores.setTheory;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.*;

import com.newscores.setTheory.Row;
import com.newscores.setTheory.interfaces.*;
import com.newscores.setTheory.utils.CompositionMatrixUtils;
import com.newscores.setTheory.utils.SetUtils;

public class TestSetUtils extends TestCase {

	public void testRandomSets() {
		int numberOfRandomSets = 10;
		int count = 0;
		System.out.println( "Test random pitch class sets using SetUtils.randomSet()." );
		IntStream.range(0,  PitchClassSet.MODULUS).forEach( length -> {
			IntStream.range(0, numberOfRandomSets).forEach( i ->  {
				System.out.println(
					String.format("Random pitch class sets of size %d using SetUtils.randomSet(): %s.",
						length,
						SetUtils.randomPitchClassSet(length).toString())
				);
			});
		});	
	}
}
