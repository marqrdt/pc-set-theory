import com.newscores.setTheory.*
import com.newscores.setTheory.utils.*
import static com.newscores.setTheory.SetTheoryFactories.*
import java.util.*

A = 10
B = 11

//p = new Row([ 0, B, 1, 2, A, 8, 5, 3, 9, 4, 7, 6 ])
// Philip Glass Music in 12 Parts

// Set p to a placeholder object
def p = "bogus"

def getInput = { prompt->
	println prompt
	Scanner scan = new Scanner(System.in)
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
	//println ("You entered ${rowString}")
	try {
		p = row(rowString)
	} catch ( IllegalArgumentException exc) {
		p == null
		exc.printStackTrace()
	}
	//println Row.getCanonicalName()
}
//p = new Row([ 0, 3, 1, 2, B, 6, 4, 5, 7, 9, 8, A ])

println "Row is ${p}"

println "The Row matrix is:\n${CompositionMatrixUtils.format(RowUtils.getMatrix(p))}"
def partitions = [ [ 3,4,5 ], [ 3,5,4 ], [ 4,3,5 ], [ 4,5,3 ], [ 5,3,4 ], [ 5,4,3 ], [ 3,3,3,3 ], [ 4,4,4 ], [ 6, 6] ]

partitions.each{ partition ->
	def cms = new CompositionMatrix()
	def seqList = p.partitionBy( partition )
	def canBePartitoned = true
	def count = 0
    seqList.each { seq ->
			//println partition
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
