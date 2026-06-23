import java.util.*

import com.newscores.setTheory.*
import com.newscores.setTheory.utils.*
import com.newscores.setTheory.interfaces.*
import com.newscores.setTheory.SetTheoryFactories
import com.newscores.notation.*

import java.util.*


A = 10
B = 11

//p = new Row([ 0, B, 1, 2, A, 8, 5, 3, 9, 4, 7, 6 ])
// Philip Glass Music in 12 Parts

def pSeqs = new ArrayList<PitchClassSequence>()
//def pSeqs = []

pSeqs << getPitchClassSequence( [ 0,3,1,2,B,6,4,5,7,9,8,A] )

pSeqs << getPitchClassSequence( [ 2,5,7,0,9,8,A,3,1,B,6,4] )

pSeqs << getPitchClassSequence( [ 5,7,6,8,A,B,9,4,1,2,0,3] )

//def arrangement = new ArrayList<ArrayList<Integer>>()
def arrangement = []

arrangement << [3,4,5]
arrangement << [1,7,4]
arrangement << [8,1,3]

//CompositionMatrix cm = new CompositionMatrix( pSeqs, arrangement)

//CompositionMatrix cm = new SetTheoryFactories.getCompositionMatrix()
CompositionMatrix cm = new CompositionMatrix()
cm.addSegment( pcSeq( [0,3,1] ), 0, 0 )
cm.addSegment( pcSeq( [2,B,6,4] ), 0, 1 )
cm.addSegment( pcSeq( [5,7,9,8,A] ), 0, 2 )

cm.addSegment( pcSeq( [2] ), 1, 0 )
cm.addSegment( pcSeq( [5,7,0,9,8,A,3] ), 1, 1 )
cm.addSegment( pcSeq( [1,B,6,4] ), 1, 2 )

cm.addSegment( pcSeq( [5,7,6,8,A,B,9,4] ), 2, 0 )
cm.addSegment( pcSeq( [1] ), 2, 1 )
cm.addSegment( pcSeq( [2,0,3] ), 2, 2 )

def design = ["RT[2]M[5]","T[3]","RT[7]M[B]", "RT[3]","T[7]M[B]"]

println( "Prime Form:")
println( CompositionMatrixUtils.format( cm ) )
println()
println( "Applying design ${design}..." )
println()

def cmDesign = cm.apply( design )
cmDesign.forEach({ t -> 
	println CompositionMatrixUtils.format(t)
})


println cm.toJsonString()

/*
def outfilePath = '/Users/marqrdt/cm-design.ly'
def cmDecorator = new LilypondAbstractCMDecorator(cmDesign)
//println (cmDecorator.output())

new File(outfilePath).withWriter('utf-8') {
	writer -> writer.writeLine cmDecorator.output()
 }
*/
