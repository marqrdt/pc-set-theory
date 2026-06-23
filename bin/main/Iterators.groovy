import com.newscores.setTheory.*
import com.newscores.setTheory.utils.*

import java.util.*

// Iterators
// PC Set Theory relies heavily on iterated operations
// Every programming language includes methods for iterations,
// and MSTL contains a powerful set of easy-to-use iterators that
// operates on various types

// Define a set and iterates through all of its transformation.
// There is no need to keep track of transposition indices or inversions,
// as the PitchClassSet type knows how to iterates throgh all its forms.

def allIntSet = new PitchClassSet( "4 5 7 b" )

allIntSet.each({
// "it" is the default variable injected into the loop representing the current value of the iteration.
    println it
    // Prove that all the sets returned are equivalent to the original set
    // Recall that the equivalent() operation compares the Normal Forms of two sets.
    assert it.equivalent(allIntSet.getNormalForm())
})

/** Output
PCSet: [457B]
PCSet: [1578]
PCSet: [0568]
PCSet: [0467]
PCSet: [1679]
...output truncated...
**/

// There is a static method for iterating through all 4096 PitchClassSets for rapid searching and evaluation.
// Note that there is no need to store them in some sort of data structure such as an array or list.

/*
PitchClassSet.allPitchClassSetsIterator().each({
    println it
})
*/
/** Output
...output truncated...
PCSet: [0145689B]
PCSet: [245689B]
PCSet: [0245689B]
PCSet: [1245689B]
PCSet: [01245689B]
...output truncated...
**/

// Using the opening phrase Schoenberg Violin Concerto,
def schv = new PitchSequence("57 58 60 61 62 61 59 58 69 70 75 83 76 66 84 73 67 80 74 65 62 61 68 60 67 65")

// An iterator on a PitchSequence iterates through all of its members.
schv.each({
    print "${it} "
})

println ""

/** Output
57 58 60 61 62 61 59 58 69 70 75 83 76 66 84 73 67 80 74 65 62 61 68 60 67 65
**/

// The transformationIterator interface in a Row produces all of its transformations under Tn, I and R
schvcRow = new Row( [ 9,10,3,11,4,6,0,1,7,8,2,5 ] )

schvcRow.transformationIterator().each({
    println it
})

/** Output
<9 a 3 b 4 6 0 1 7 8 2 5>
<5 2 8 7 1 0 6 4 b 3 a 9>
<3 2 9 1 8 6 0 b 5 4 a 7>
<7 a 4 5 b 0 6 8 1 9 2 3>
...output truncated...
**/