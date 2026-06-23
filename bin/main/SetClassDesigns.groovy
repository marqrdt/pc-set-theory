import org.marqrdt.notation.*
import com.newscores.setTheory.*
import com.newscores.setTheory.interfaces.*
import com.newscores.counterpoint.*

import java.util.*

//@Grab(group='com.newscores', module='setTheory', version='1.0.0-SNAPSHOT')
//@Grab(group='com.newscores', module='counterpoint', version='1.0.0-SNAPSHOT')
//@Grab(group='org.marqrdt', module='notation', version='1.0.0-SNAPSHOT')

class RegistrationManager {
	
	int baseOffset = 0
	
	RegistrationManager(  int offset ) {
		this.baseOffset = offset
	}
	
	def getPitchRegistration( int note, ISequence pcSeq ) {
		return getRegistration( note )
	}

	private getRegistration( int pc ) {
		return pc + this.baseOffset
	}
}

class SetCreator {
    def debug = false
    def aggregate = PitchClassSet.aggregate()
	def baseOffset = 0
	def registerMan = new RegistrationManager( baseOffset )
    def createDesign( int length, int memory, List inSets ) {
		def outSeq = new PitchSequence()
        PitchClassSequence pcSeq = new PitchClassSequence([0])
        Random rand = new Random()
        //pcSeq.addPitch( registerMan.getPitchRegistration( rand.nextInt( 12 ), pcSeq ) )
        if ( debug ) {
            println "pcSeq initial value ${pcSeq.toString()}"
        }
        //def candidates = []
        def pitchFrequencyMap = [:]
        // Initialize pitch frequency map
        ( 0..12 ).each {
            pitchFrequencyMap[ it ] = 0
        }
        def count = 0
        while ( count < length ) {
            // stores the least-used pcs from the PC Sequence to maintain pitch balanace and increase aggregate competion rate.
            def pcFrequencies = pitchFrequencyMap.collect { it.value }
            def leastUsedPcs = []
            pitchFrequencyMap.findAll {
                // find the entries in the lowest 1/4 of frequnecies
                it.value <=  pcFrequencies.min() + (pcFrequencies.max() - pcFrequencies.min()) / 4
            }.each {
                leastUsedPcs << it.key
            }
            def candidates = getCandidates( pcSeq, memory, inSets)
            //println "Choosing candidates from the intersection of ${candidates} and ${leastUsedPcs}"
            //candidates = candidates.intersect( leastUsedPcs )
            if ( candidates.size() > 0) {
                if ( debug ) {
                    println "Choosing next pitch from list of candidates ${candidates}"
                }
                Collections.shuffle( candidates )
                //println candidates
                pcSeq.addPitch( registerMan.getPitchRegistration( candidates[0], pcSeq )  )
            } else {
                def arbitrary = getArbitraryCandidates( pcSeq, memory, 6 )
                def pitch = arbitrary[ rand.nextInt( arbitrary.size() - 1 ) ]
                if ( debug ) {
                    println "No PCSet candidates found, choosing pitch ${pitch} from ${arbitrary}"
                }
                pcSeq.addPitch( registerMan.getPitchRegistration( pitch, pcSeq ) )
            }
            pitchFrequencyMap[ pcSeq.getMembers().get( pcSeq.length() - 1 ) % 12 ] = pitchFrequencyMap.get( pcSeq.getMembers().get( pcSeq.length() - 1) % 12) + 1
            count++
        }
        String sequenceData = Arrays.toString(pcSeq.pcVector())
        /*
        PitchClassSet pcSet = SetTheoryFactories.getPitchClassSet( pcSeq )
        if (  pcSet.equals( PitchClassSet.aggregate() ) ) {
            sequenceData = " : aggregate"
        } else {
            sequenceData = String.format(" : %d", pcSet.length() )
        }
        */
        println ( String.format( "%s: %s", pcSeq.toString(), sequenceData ) )
        return pcSeq
    }
    	
    def getCandidates( ISequence pSeq, int mem, inSets) {
        def candidates = []
        def pcSetToTest
		def pcSeq = new PitchClassSequence( pSeq )
        // given the variable mem, get the last (mem - 1) members for pcSeq
        if ( pcSeq.length() <= mem - 1 ) {
            pcSetToTest = new PitchClassSet( pcSeq.getMembers() )
        } else {
            // create a PCSet from the last memSize - 1 members of pcSeq. We are trying to determine the
            // last member of pcSetToTest, so we create it with one PC "missing"
            pcSetToTest = new PitchClassSet( pcSeq.getMembers()[ (pcSeq.length() - mem + 1)..<pcSeq.length() ] )
        }
        // get the complement of the last mem members of the existing pcSeq and calculate thier complement.
        // This complement set is the candidate pool from which we can select the actual candidates which match
        // the pitch-set qualities we're looking for.
        def candidatePool = aggregate.getMembers().minus( pcSetToTest.getMembers() )
        //println "Candidate pool is ${candidatePool}"
        // here we create a new set to test to see if it's equivalent to our master list of 'acceptable' sets.
        // 
        inSets.each {
            set ->
            def measureSet = new PitchClassSet(set)
            def setToTest
            if ( pcSeq.length() >= set.size() ) {
                // Scenario: pcSeq = <23569>, set = {467a}
                // In this scenario, setToTest will be {569}
                // if (length of pcSeq) >= (length of set), make setToTest from the last n - 1 members of pcSeq
                // where n = set.size()
                // use negative index to count from end of list!
                def tailSet = pcSeq.getMembers()[ (pcSeq.length() - set.size() + 1)..<pcSeq.length() ]
                //println "creating set from last ${pcSeq.length() - (pcSeq.length() - set.size() + 1)} members of pcSeq: ${tailSet}"
                setToTest = new PitchClassSet( tailSet )
            } else {
                // if (length of pcSeq) < (length of set), simply make setToTest from members of pcSeq
                // where n = set.size()
                setToTest = new PitchClassSet( pcSeq.getMembers() )
            }
            //println "Set is ${set}, setToTest is ${setToTest}, pcSeq is ${pcSeq.toString()}"
            candidatePool.each {
                pcCand ->
                def tempTestSet = new PitchClassSet( setToTest.getMembers() + pcCand )
                //tempTestSet.addPitch( pcCand )
                //println "Adding pitch ${pcCand} to ${setToTest.toString()} to get ${tempTestSet.toString()}"
                if ( tempTestSet.getNormalForm().isSubset( measureSet.getNormalForm() ) && candidates.indexOf(pcCand) < 0 ) {
                    //println "${tempTestSet.getNormalForm().toString()} is a subset of ${measureSet.getNormalForm().toString()}"
                    candidates << pcCand
                } else {
                    //println "${tempTestSet.getNormalForm().toString()} is NOT a subset of ${measureSet.getNormalForm().toString()}"
                }
            }
        }
        //println "Candidates inside getCandidates is ${candidates}"
        return candidates
    }
    
    def getArbitraryCandidates( ISequence inSeq, int memory, int length = 4 ) {
        def candidates = []
        def rand = new java.util.Random();
		def pcSeq = new PitchClassSequence( inSeq )
        if ( pcSeq.length() >= memory ) {
            return PitchClassSet.aggregate().getMembers().minus( pcSeq.getMembers()[ ( pcSeq.length() - memory + 1)..<pcSeq.length() ] )
        } else {
            return (1..length).collect { rand.nextInt( 11 ) }
        }
    }
}

/* class ScoreCreator {
    def defaultDur = 1.0
    def offset = 60
    def createScore( String appName, String outputFolder, pcSeqList ) {
        def cptDecorators = []
        def phraseCount = 1
        pcSeqList.each {
            pSeq ->
            def phrList = new ArrayList<Phrase>()
            for ( i in 0..11 ) {
                def newPhrase = new Phrase()
                for ( note in pSeq.getMembers() ) {
                    newPhrase.addEvent( new NoteEvent( defaultDur, note + i + offset, 50 ) )
                }
                phrList.add( newPhrase )
                //println cpt.asTimeline()
            }
            def description = pSeq.getDescriptor()
            def abstractPhraseDecorator = new LilypondAbstractPhraseDecorator( phrList )
            def cptDecorator = new LilypondAbstractCounterpointDecorator( abstractPhraseDecorator, String.format("Phrase %s", description ) )
            cptDecorators.add( cptDecorator )
            //phrList << newPhrase
            phraseCount++
        }
        LilypondScoreDecorator lpcd = new LilypondScoreDecorator( "${appName} Sets", cptDecorators, true )
        new File( "${outputFolder}/${appName}.ly").withWriter {
            out ->
            out.writeLine( lpcd.output() )
        }
    }
}
 */
def setCreator = new SetCreator()
//def scoreCreator = new ScoreCreator()
def appName = "PitchClassSetDesign"
def designSize = 48
def memory = 8
def numSequences = 20

List sets = [ [0,1,2,6], [0,1,4,6], [0,1,3,7], [0,1,5,7], [0,3,7,11], [0,4,8,11] ]
def pSeqList = []
(0..numSequences).each {
    pSeqList << setCreator.createDesign( designSize, memory, sets )
}

//scoreCreator.createScore(appName, "/Users/marqrdt/ScoreOutFiles", pSeqList)


/*
    Text of what should happen:
    Pass in a List of Sets as inSets.
    Create an empty pcSeq of type PitchClassSequence.
    determine the set memory length, i.e. the size of the sets we are creating. Store as memSize.
    Add 1 random pc to pcSeq.

    create empty List candidates
    for each pcSet in inSets:
        create an empty List candidates
        create a list pcSetToTest of the last (memSize - 1) members of pcSeq.
            we use (memSize - 1) because we're trying to calculate the final member of this set,
                i.e., there's one more member we're trying to choose.
        create a List 'pool' of all pcs that are the complement of the last memSize members of pcSeq
            'pool' is the list from which we can choose candidates. 'pool' is basically the pitches which
                are NOT members of the set we're trying to build.
        for each member poolMember of 'pool':
            create a candidate set candidateSet which is created from pcSetToTest + member
            if the set-class of candidateSet is same as pcSet, then member is a candidate
                i.e. member can be chosen as one of the possible next elements of pcSeq
                add member to candidates
    choose a random element from candidates and add it to pcSeq
    repeat the process using the newly extended pcSeq

*/