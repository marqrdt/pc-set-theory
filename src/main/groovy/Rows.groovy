@GrabConfig(systemClassLoader = true)
@Grab('log4j:log4j:1.2.17')
@Grab('org.apache.ivy:ivy:2.5.0')

import java.util.*

import com.newscores.setTheory.*
import com.newscores.setTheory.utils.*
import static com.newscores.setTheory.SetTheoryFactories.*
import com.newscores.notation.*

import java.util.*

A = 10
B = 11


// Set p to a placeholder object
def p = "bogus"
//p = new Row([ 0, B, 1, 2, A, 8, 5, 3, 9, 4, 7, 6 ])

/**
def getInput = { prompt->
	println prompt
	Scanner scan = new Scanner(System.in);
	def input = scan.nextLine()
	return input
	//return "1 2 3 4 6 5 7 8 a 9 b"
}

while ( p == null || ! p.getClass().getCanonicalName().equals(Row.getCanonicalName()) ) {
	def rowString = getInput("Please enter a 12-tone row and hit enter. Enter numbers 0-9 and a for 10, b for 11, q to quit.")
	if ( rowString.toLowerCase().startsWith("q") ) {
		println "Good bye!"
		System.exit(0)
	}
	try {
		p = row(rowString)
	} catch ( IllegalArgumentException exc) {
		p == null
		exc.printStackTrace()
	}
	//println Row.getCanonicalName()
}
**/
// Philip Glass Music in 12 Parts
//p = new Row([ 0, 3, 1, 2, B, 6, 4, 5, 7, 9, 8, A ])

// Violin Piece
p = new Row([ 0, B, 1, 2, A, 8, 5, 3, 9, 4, 7, 6 ])

println "Row is ${p}"

println "The Row matrix is:\n${CompositionMatrixUtils.format(RowUtils.getMatrix(p))}"

def transposeTo = 5
println "\nThe Row  transposed to ${transposeTo}is:\n${p.transposeTo(transposeTo)}"

def o = 5
println "The Row transformed by O[${o}] is:\n${p.O(o)}"
o = 7
println "The Row transformed by O[${o}] is:\n${p.O(o)}"

/** Output
<a 9 4 8 3 1 7 6 0 b 5 2>
**/
// The transposeTo() operation can be used to transpose a Sequence type so that it starts on any PC.
// transposeTo(0) is useful for producing the prime form of a Row

def hex1 = pcSet( p.subSequence(0,6))
def hex2 = pcSet( p.subSequence(6,6))

println "Divided into hexachords, the set-classes of each is ${PitchClassSetCatalog.getNameByPitchClassSet(hex1)} and ${PitchClassSetCatalog.getNameByPitchClassSet(hex2)}"

def setEquality = hex1.equivalent(hex2) ? "equivalent" : "not equivalent"
println "The hexachords are ${setEquality}"


(1..11).each({
    println p.intervals(it)
})

def partitions = [ [ 3,4,5 ], [ 3,5,4 ], [ 4,3,5 ], [ 4,5,3 ], [ 5,3,4 ], [ 5,4,3 ], [ 3,3,3,3 ], [ 4,4,4 ], [ 6, 6] ]
println "One interested way to extend 12-note Rows into larger designs is to embed the Row into linear arrangements of its transformations."
println "The code below enumerates all the ways the Row p can be embedded into itself given a fixed number of partitions."

partitions.each{ partition ->
	def cms = new CompositionMatrix()
	def seqList = p.partitionBy( partition )
	def canBePartitoned = true
	def count = 0
	seqList.each { seq ->
			seq = new PitchClassSequence( seq )
			println seq
			def found = 0
			p.transformationIterator().each { trans ->
				def indices = trans.getEmbeddedSubsequence( seq )
			if ( indices != null && ! trans.getDescriptor().equals('T[0]') ) {
					println "\t${trans.getDescriptor()} : ${indices} : ${trans.toString()}"
					found++
			}
			}
			if ( found == 0 ) {
				println "\tNone found"
				canBePartitoned = false
			}
			println "+++++++++++++++++++"
	}
	println "===================="
}
println "Find all the Row Transformations on P that have no consecutive dyads in common with T[0] P"

p.transformationIterator().each { trans ->
	(2..6).each {setSize ->
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
			println "Row pair [T[0], ${trans.getDescriptor()}] form sets of size ${setSize * 2} in subsequences of size ${setSize}"
			println p.toString()
			println trans.toString()
			println "============"
		}
	}
}

println "Applying the Wholetone transformation, which transposes the row members in a wholetone scale by an even interval."
println "There are 12 Wholetone forms. The set of Wholetone transformations have interesting properties as shown by examining"
println "the hexachords of each transformation"
(0..6).each { index ->
	(0..1).each { pol ->
		def w = p.W(index, pol)
		w.setDescriptor("W[${index % 12},${pol}]")
		hex1 = pcSet( w.subSequence(0,6))
		hex2 = pcSet( w.subSequence(6,6))
		println w.toStringExtended()
		println "The consecutive intervals are ${w.intervals(1)}."
		println "The set-classes of each hexachords are ${PitchClassSetCatalog.getNameByPitchClassSet(hex1)} and ${PitchClassSetCatalog.getNameByPitchClassSet(hex2)}\n"
	}
}

def maxNumCMs = 500
def cmSize = 3
println "Composers working with 12-tone music are often interested in all of the combinatorial combinations of a Row."
println "Common arrangements are hexachordal (2x2), tetrachordal (3x3), and trichordal (4x4)."
println "However, a vastly greater number of uneven combinations may exist."
println "The getCombinatorialCMs method in RowUtils calculates these for various CM sizes."
println "The remainder of this exercise will print the first ${maxNumCMs} combinatorial CMs of size ${cmSize} from Row p: ${p}."

// When setting the max number of combinations, the stream is not processed on parallel threads
//List<CompositionMatrix> combinatorialCMs = RowUtils.getCombinorialCMs(p.transposeTo(0), 12, cmSize, cmSize, maxNumCMs)
List<CompositionMatrix> combinatorialCMs = RowUtils.getCombinorialCMs(p.transposeTo(0), 12, cmSize, 0)

def outFilePath = "/home/marqrdt/counterpoint-${cmSize}-rows-output.txt"
new File(outFilePath).withWriter('utf-8') { writer ->
	combinatorialCMs.each{ cm ->
		def formattedCM = CompositionMatrixUtils.format( cm, " ", "|", true )
		println formattedCM
		writer.writeLine formattedCM
	}
}

/**
def lilypondOutfilePath = "/Users/marqrdt/counterpoint-${cmSize}-rows.ly"
def cmDecorator = new LilypondAbstractCMDecorator(combinatorialCMs)
//println (cmDecorator.output())

new File(lilypondOutfilePath).withWriter('utf-8') {
	writer -> writer.writeLine cmDecorator.output()
}
 **/


