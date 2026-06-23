import java.util.*

import com.newscores.setTheory.*
import com.newscores.setTheory.utils.*
import static com.newscores.setTheory.SetTheoryFactories.*
import com.newscores.notation.*

import java.util.*

/*
	This Groovy program searched through a lrage number of random 12-tone rows and finds ones
	 that have interesting properties. These include:
	 1. Can be arranged into a large number of patterns so that the row is form can be found in embedded subsequences of itself.
	 2. Has a maximum number of Interval Classes, i.e. contains at least one of each interval class.
	 3. Contains instances of specific Set-Classes (I'm most interested in the AIC, 0,1,5,7 (4-16, + 4-5, it's M5: 0,1,2,6).
	 4. Transformations of the Row can be combined such that consecutive subsets on length N have invariance of 0.
	     A trivial example:
	     T0P: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, A, B
	     T6P: 6, 7, 8, 9, A, B, 0, 1, 2, 3, 4, 5

 */

A = 10
B = 11

// Set p to a placeholder object

def partitions = [ [ 3,4,5 ], [ 3,5,4 ], [ 4,3,5 ], [ 4,5,3 ], [ 5,3,4 ], [ 5,4,3 ], [ 3,3,3,3 ], [ 4,4,4 ], [ 6, 6] ]
def desired_SCs = ["4-5", "4-16", "4-Z15", "4-Z29"]

maxRows = 100000
n = 0
while( n < maxRows ) {
	p = RowUtils.randomRow()
	if ( checkRow(p, partitions) ) {
		println()
		println("Found cool row: ${p}")
		icVector = [0,0,0,0,0,0]
		for (n in p.intervals()) {
			if ( n <= icVector.size() ) {
				icVector[n - 1] += 1
			} else {
				icVector[(icVector.size() * 2) - n - 1] += 1
			}
			//icVector[(n % icVector.size()) - 1] += 1
		}
		println("\tIntervals: ${p.intervals()}")
		println("\tInterval Vector: ${icVector}")
	}
}

def checkRow(p, partitions) {
	//def partitions = [ [ 3,4,5 ], [ 3,5,4 ], [ 4,3,5 ], [ 4,5,3 ], [ 5,3,4 ], [ 5,4,3 ], [ 3,3,3,3 ], [ 4,4,4 ], [ 6, 6] ]
	check = true
	partitions.each { partition ->
		def cms = new CompositionMatrix()
		def seqList = p.partitionBy(partition)
		def canBePartitoned = true
		def count = 0
		seqList.each { seq ->
			seq = new PitchClassSequence(seq)
			//println seq
			def found = 0
			p.transformationIterator().each { trans ->
				def indices = trans.getEmbeddedSubsequence(seq)
				if (indices != null && !trans.getDescriptor().equals('T[0]')) {
					//println "\t${trans.getDescriptor()} : ${indices} : ${trans.toString()}"
					found++
				}
			}
			if (found == 0) {
				//println "\tNone found"
				canBePartitoned = false
				check = false
			}
			//println "+++++++++++++++++++"
		}
		//println "===================="
	}
	//println "Find all the Row Transformations on P that have no consecutive dyads in common with T[0] P"

	check = false
	p.transformationIterator().each { trans ->
		(2..6).each { setSize ->
			def hasCommonDyads = false
			(0..(12 - setSize)).each { index ->
				def a = new PitchSet(p.subSequence(index, setSize))
				def b = new PitchSet(trans.subSequence(index, setSize))
				if (a.intersection(b).size() > 0) {
					/*
			println "Rows T[0] and ${trans.getDescriptor()} have comman dyads ant index ${index}"
			println p.toString()
			println trans.toString()
	*/
					hasCommonDyads = true
				}
			}
			if (!hasCommonDyads) {
				//println "Row pair [T[0], ${trans.getDescriptor()}] form sets of size ${setSize * 2} in subsequences of size ${setSize}"
				//println p.toString()
				//println trans.toString()
				//println "============"
				if ( setSize >= 5 ) {
					//println "Row pair [T[0], ${trans.getDescriptor()}] form sets of size ${setSize * 2} in subsequences of size ${setSize}"
					check = true
				}
			}
		}
	}
	for ( interval in p.intervals() ) {
		if ( interval < 2 ) {
			check = false
		}
	}
	return check
}
