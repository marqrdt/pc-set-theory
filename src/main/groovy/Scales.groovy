import org.marqrdt.notation.*
import com.newscores.setTheory.*
import com.newscores.setTheory.utils.*
import java.util.*
import groovy.json.*
import static com.newscores.setTheory.PitchClassSet.A
import static com.newscores.setTheory.PitchClassSet.B
import static com.newscores.setTheory.SetTheoryFactories.*

println "A walk through Scales sets"

/*
	declare setA with members using a List. A and B are imported in lines 4 and 5.
*/
def majorScale = new Scale( Scale.MAJOR, 60, 84 )

println "Major scale : ${majorScale}"
println "Major scale transposed up one whole step : ${majorScale.T(2)}"
def intersection = pcSet( majorScale.intersection(majorScale.T(2)) )
println "Here is the intersection of the Major scale and it transposition up one whole step : ${intersection}"

println "The complement of the major scale should be a black-note Pentatonic scale : ${majorScale.complement()}"
println "We can note that they are the same PCSet : ${pcSet(majorScale).complement().equivalent(intersection)}"

//def comp = new PitchClassSet( majorScale.complement() )
// Here, we'll use the shorter, DSL-friendly constructor pcSet()...
def comp = pcSet( majorScale.complement() )

def compName = PitchClassSetCatalog.getNameByPitchClassSet( comp )
println "The complement of the major scale ${majorScale.complement()} should be a black-note Pentatonic scale : ${compName}"
println""

def scalePatt = [ 1, 1, 2, 1, 1, 1, 2]
def myScale = new Scale( scalePatt, 20, 80 )
println "customScale : ${myScale}"


/*
	Besides showing the capability of this framework, 
*/
println "We'll see if customScale completes an aggregate..."
def scaleSet = pcSet( myScale )
println "The statement 'customScale completes an Aggregate' is ${scaleSet.complement().equals( pcSet() )}"

def subSeq = myScale.subSequence(0, 12)
println "Here is a subsequence of the first 12 notes of customScale ${subSeq}"
println "The statement 'The first 12 notes of customScale completes an Aggregate' is ${pcSet(subSeq).equals(PitchClassSet.aggregate())}"

println "We'll see what's the smallest subsequence of customScale that completes an aggregate..."
def index = 1
while ( index < myScale.size() ) {
	subSeq =  myScale.subSequence(0, index)
    if ( pcSet( subSeq ).equals(PitchClassSet.aggregate())  ) {
       println "The first ${index} notes of customScale completes an aggregate: ${subSeq}"
       break
    }
    index++
}
/*
	Using the Contour class, we'll recreate implementations of the "M" motif found in the music of Olivier Messiaen.
	This is the "original"melodic contour, which consists of degrees 0, 2, 3, 4 of the octatonic scale.
*/

mCont = new Contour( [ 0, 3, 2, 4, 0 ] )

println""
println "Here, we map the 'M' motif from Olivier Messiaen's music to the octatonic scale"
octoScale = new Scale( Scale.OCTATONIC, 60 )
def mMelody = mCont.applyPitchCollection( octoScale )
println mMelody

println""
println "This isn't quite right, as we need to start the scale on the next note, which is very easily done using the subSequence method."
mMelody = mCont.applyPitchCollection( octoScale.subSequence(1, 12) )
println mMelody

println""
println "We can reverse our melody very easily, noting that the 'M' motive looks much the same in reverse."
mMelody = mCont.R().applyPitchCollection( octoScale.subSequence(1, 12) )
println mMelody
// ${ }

// An arbitrary contour, just some numbers strung together...
println""
def complexContour = new Contour([ 4, 5, 9, 0, 11, 13, 12, 6, 15, 2, 15, 2, 8, 7, 18, 19 ] )
println "Here is a more complex contour: ${complexContour}."
println "Let's apply this contour to customScale: ${complexContour.applyPitchCollection(myScale)}."


def contourSeq = complexContour.applyPitchCollection(myScale)
def json = JsonOutput.toJson([name: 'ComplexContour-1', sequence: contourSeq.getMembers()])

def baseDir = "/Users/marqrdt/git/set-theory"

new File( baseDir, 'ComplexContour-1.json') << json

println "Here is a JSON string of the sequence: ${json}"