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

def glassRow = SetTheoryFactories.getRow( [0,3,1,2,11,6,4,5,7,9,8,10] )
def transformList = []

def transformationStrings = ["T[0]M[5]", "RT[10]M[5]", "RT[5]I"]
transformationStrings.forEach( {
	transformList.add(new PitchClassSequenceTransformation(it))
})

//println "PCST List: ${transformList}"
Iterator<PitchClassSequenceTransformation> allTransforms = PitchClassSequenceTransformation.transformationIterator()
while ( allTransforms.hasNext() ) {
	def trans = allTransforms.next()
	println "Applying ${trans.toString()} to list ${transformList}"
	def transformProducts = trans.performTransformationOnList(transformList)
	println transformProducts
	println "======================"
}