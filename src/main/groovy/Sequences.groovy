import com.newscores.setTheory.*
import com.newscores.setTheory.utils.*
import java.util.*

// This line will not be needed after my libraries are deployed to the Central Maven repository at 
//@GrabResolver(name='sonatype-snapshots', root='https://oss.sonatype.org/content/repositories/snapshots/')
// This line imports the Set Theory libraries into the current workspace, allowing us
// to use the objects. Beyond this point, there is no software installation necessary.
// TODO: Redact real groupId for proposal submission
//@Grab(group='com.newscores', module='setTheory', version='1.0.1-SNAPSHOT')

// Sequence types
// Sequence types shared most of the canonical operators of Sets, such as T(n), M, I.
// In addition, operations on order are added, including R (retrograde) and several
// powerful and intuitive sub-sequencing operations.

// We will define a PitchSequence. This is the opening phrase phrase of the Schoenberg Violin Concerto, mm 1-12,
// divied into 3 sub-sequences determined by rests. We will later combine them.
// It is expressed as Pitch Numbers, with Middle C = 60

// Create an empty List called sets
def sets = []

// Append the PitchSequencse to sets. Groovy provides the simple << operator for appending items to a list.
sets << new PitchSequence("57 58 60 61 62 61 59 58")
sets << new PitchSequence("69 70 75 83 76 66")
sets << new PitchSequence("84 73 67 80 74 65")
sets << new PitchSequence("62 61 68 60 67 65")

// Create an empty PitchClassSequence "phrase" to contain the concatentation of sequences above
def phrase = new PitchSequence()

// Very simple interface for list iteration
sets.each({
    // "it" is the placeholder for the current value of the list in the loop
    def pcset = new PitchClassSet( it )
    def setName = PitchClassSetCatalog.getNameByPitchClassSet( pcset )
    println it
    println "${pcset} has Normal Form ${pcset.getNormalForm()} and is a ${setName}"
    // Append each PitchClassSequence to phrase using a simple plus-equals shorthand operator.
    // When the loop completes, the variable "phrase" will be a PitchSequence with the members of each Sequence.
    //phrase.extend( it )
    phrase += it
    assert it != null
    assert phrase != null
    //phrase = (PitchSequence) phrase + (PitchSequence) it
    //phrase = phrase.plus( (PitchSequence) it)    
})

phrase.each({
    println it
})

/** Output:
PCSet: [0129AB] has Normal Form PCSet: [012345] and is a 6-1
PCSet: [3469AB] has Normal Form PCSet: [012578] and is a 6-18
PCSet: [012578] has Normal Form PCSet: [012578] and is a 6-18
PCSet: [012578] has Normal Form PCSet: [012578] and is a 6-18
**/

println "The PitchSequence for the entire opening phrase is ${phrase}"
println "Its retrograde is ${phrase.R()}"
/** Output:
The PitchClassSequence for the entire opening phrase is <57 58 60 61 62 61 59 58 69 70 75 83 76 66 84 73 67 80 74 65 62 61 68 60 67 65>
**/

// Create a PitchSet in PitchSpace (where pitches are not equivalent mod 12) and use the intervals() method to find
// the intervals between adjacent pitches in the Set. Note that these pitches are not necessarily adjacent in the Sequence.
// Using this simple inspection, we can immediately see some interesting symmetries, where indices 1-13 form a mirror inversion.
// TODO: Create constructor PitchSet( ISequence ) to create a PitchSet from any Sequence type.

def phrasePSet = new PitchSet( phrase )
//def phrasePSet = phrase as PitchSet

println "Notes from the opening phrase stacked in Pitch space: ${phrasePSet.getAscendingIntervals()}"

/** Output
Notes from the opening phrase stacked in Pitch space: [1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 4, 3, 1]
**/

// Create a PitchClassSet from any Sequence type in one statement.
// Here, we store it in variable phrasePcSet.
def phrasePcSet = phrase as PitchClassSet

// Illustrate that the violin melody of the first period of the Schoenberg Violin Concerto completes an aggregate.
assert phrasePcSet == PitchClassSet.aggregate()
// No errors here indicate that the asserted statement is true: The phrase completes an aggregate.

// Create a new PitchClassSequence from phrase and store in the variabe schvPcSeq.
def schvPcSeq = new PitchClassSequence( phrase )
print "The intervals between adjacent members of schvPcSeq is ${schvPcSeq.intervals()}"
/** Output
The intervals between adjacent members of schvPcSeq is [1, 2, 1, 1, 11, 10, 11, 11, 1, 5, 8, 5, 2, 6, 1, 6, 1, 6, 3, 9, 11, 7, 4, 7, 10]
**/