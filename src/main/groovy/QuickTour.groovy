import org.marqrdt.notation.*
import com.newscores.setTheory.*
import com.newscores.setTheory.utils.*
import java.util.*
import static com.newscores.setTheory.PitchClassSet.A
import static com.newscores.setTheory.PitchClassSet.B
import static com.newscores.setTheory.SetTheoryFactories.*

println "A walk through PitchClass sets"

/*
	declare setA with members using a List. A and B are imported in lines 4 and 5.
*/
println "The formal creation method using the 'new' constructor."
def setA = new PitchClassSet([ 0,1,2,3,4,6,A ])

println setA

println setA.NF()

println ""
println "An easier way using the shorter DSL methods from SetTheoryFactories."
setA = pcSet([ 0,1,2,3,4,6,A ])
println setA
println setA.NF()

/*
	declare setA with members using a String. A and B are imported in lines 4 and 5.
*/
setA = new PitchClassSet("012346A")
println "Print out some basic set information..."
println "aSet: ${setA}"
println "Normal Form: ${setA.NF()}"
// Print its Set Class. The Set Class name is not contained in the PitchClassSet
// object itelf, but rather statically defined in PitchClassSetCatalog.
// Looking up a Set Class is a fairly expensive operation.
println "Set Class: ${PitchClassSetCatalog.getNameByPitchClassSet(setA)}"
println "IC Vector: ${setA.icVector()}"
println ""
println "Print out some transformations..."
println "Transposition:  setA.T(2): ${setA.T(2)}"
println "Inversion: setA.I(): ${setA.I()}"
println "Multiplication: setA.M(5): ${setA.M(5)}"
println "Transposition and Multiplication: setA.T(2).M(5): ${setA.T(2).M(5)}"
println ""
println "Some isolated membership operations..."
println "Intersection of setA and setA.T(2): ${setA.intersection(setA.T(2))}"
println "Union of setA and setA.T(2): ${setA.union(setA.T(2))}"
println "Complement of setA: ${setA.complement()}"
println "Complement of the union of setA and setA.T(2): ${setA.union(setA.T(2)).complement()}"
println ""
println "Some more elaborate membership operations."
println "Iterate through all transformations of setA and the intersection of"
println "setA and the transformation. Only print the set if the intersection is greater than 3"
setA.each {
	// The default variable created inside a Closure is "it".
	// Recall that the intersection operator returns a PitchClassSet object, not an integer.
	// Groovy does not help here. It's length() method returns the size of PitchClassSet's iterator, which is 24.
	if ( setA.intersection(it).length() > 4 ) {
		println "Intersection of ${setA} and ${it.getDescription()}: ${setA.intersection(it)}"	                       
	}
}

println ""
println "Print the set transformation which transform setA into itself"
setA.each {
	if ( setA.equals(it) ) {
		println "setA ${setA} maps into itself under ${it.getDescription()}"	                       
	}
}

println ""
println "The Row class provides a class from working with 12-tone rows."
println "Since a Row is defined as an ordered sequence of the 12 pitch-classes,"
println "this contract is enforced at creation. The Row is similar to PitchClassSequence,."
println "but all methods that modify its contents are removed."
println ""
println "Defining our Row and storing it in p."
def p = new Row([0,B,1,2,A,8,5,3,9,4,7,6])

println p
println ""
println "A common operation is to get a Row matrix. This is assigned to the RowUtils"
println "utility class, as a Row matrix is not a property of a Row, but more of a "
println "convenient way of displaying it. Formatting it for printing is the job of"
println "CompositionMatrixUtils, which will format general Composition Matrices, of which a Row matrix is an instance."

def matrixCM = RowUtils.getMatrix( p )
println ""
println CompositionMatrixUtils.format( matrixCM, " ", " " )

println ""
println "Now we can move onto the more complex possibilities of this framework..."
println "We will declare a PitchSequence of 400 random pitches from 32 to 96 and store it in randomSeq."
println "A PitchSequence is in P-space, where, for example, pitch 12 and 24 are not considered equivalent."

//def p = new Row([0,B,1,2,A,8,5,3,9,4,7,6])
println "Using Row ${p}, we will find all the non-overlapping subsequences of randomSeq which contain Row p as an embedded subsequence"

// creating  PitchSequence, where members are in Pitch space, not Pitch-Class space.
def seqSize = 300

def randomSeq = new PitchSequence()
Random random = new Random()

(1..seqSize).each {
    randomSeq.addPitch( random.nextInt(64) + 32 )
}

println "Our random sequence of pitches is: ${randomSeq}."

def randomPCSeq = new PitchClassSequence( randomSeq )
println randomPCSeq
println "Using PitchSequence randomSeq, we create from it a PitchClassSequence randomPCSeq: ${randomPCSeq}"

def myRow = new Row([ 0,B,1,2,A,8,5,3,9,4,7,6 ])
def finished = false
index = 0
def subSeq = new PitchClassSequence( randomPCSeq.subSequence(index, randomPCSeq.size() ) )
while ( ! finished ) {
	def subSeqLength = subSeq.size() - index
	subSeq = new PitchClassSequence( randomPCSeq.subSequence(index, subSeqLength ) )
	//def subSeq = randomPCSeq.subSequence(index, 50) 
	def embedded = subSeq.getEmbeddedSubsequence( myRow )
	if ( embedded == null ) {
		finished = true
		println "Nothing should happen past here..."
	} else {
	    println "Index: ${index}, Subsequence length: ${subSeqLength}"
		println "PitchSequence ${p} is embedded in PitchSequence ${subSeq} at indices ${embedded}"
	    index = embedded[ 1 ] + index
	}
}


// ${}