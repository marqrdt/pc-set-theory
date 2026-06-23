import java.util.*;

import com.newscores.setTheory.*
import com.newscores.setTheory.utils.*
import static com.newscores.setTheory.SetTheoryFactories.*
import com.newscores.notation.*

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.*;

A = 10
B = 11

//p = new Row([ 0, B, 1, 2, A, 8, 5, 3, 9, 4, 7, 6 ])
// Philip Glass Music in 12 Parts

// Set p to a placeholder object

def p = new Row([ 0, 3, 1, 2, B, 6, 4, 5, 7, 9, 8, A ])

println "Row is ${p}"

println "The Row matrix is:\n${CompositionMatrixUtils.format(RowUtils.getMatrix(p))}"

def hex1 = pcSet( p.subSequence(0,6))
def hex2 = pcSet( p.subSequence(6,6))

println "Divided into hexachords, the set-classes of each is ${PitchClassSetCatalog.getNameByPitchClassSet(hex1)} and ${PitchClassSetCatalog.getNameByPitchClassSet(hex2)}"

def setEquality = hex1.equivalent(hex2) ? "equivalent" : "not equivalent"
println "The hexachords are ${setEquality}"


def partitions = [ [ 3,4,5 ], [ 3,5,4 ], [ 4,3,5 ], [ 4,5,3 ], [ 5,3,4 ], [ 5,4,3 ], [ 3,3,3,3 ], [ 4,4,4 ], [ 6, 6] ]
println "One interested way to extend 12-note Rows into larger designs is to embed the Row into linear arrangements of its transformations."
println "The code below enumerates all the ways the Row p can be embedded into itself given a fixed number of partitions."

def partitionCMs = []

outLilypondFilePath = "/Users/marqrdt/Music/composition/PRMMusic/partitions-CM${cmIndex}.ly"

partitions.each{ partition ->
	def cms = new CompositionMatrix();
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
					CompositionMatrix partCM = new CompositionMatrix()
					seq.setDescriptor("${indices}")
					partCM.addSegment(seq, 0, 0)
					partCM.addSegment(trans, 0, 1)
					partitionCMs.add(partCM)
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


def maxNumCMs = 200
def cmSize = 4
def maxEmpty = 3
println "Composers working with 12-tone music are often interested in all of the combinatorial combinations of a Row."
println "Common arrangements are hexachordal (2x2), tetrachordal (3x3), and trichordal (4x4)."
println "However, a vastly greater number of uneven combinations may exist."
println "The getCombinatorialCMs method in RowUtils calculates these for various CM sizes."
println "The remainder of this exercise will print the first ${maxNumCMs} combinatorial CMs of size ${cmSize} from Row p: ${p}."

// When setting the max number of combinations, the stream is not processed on parallel threads
//List<CompositionMatrix> combinatorialCMs = RowUtils.getCombinorialCMs(p.transposeTo(0), 12, cmSize, cmSize, maxNumCMs)


List<CompositionMatrix> combinatorialCMs = RowUtils.getCombinorialCMs(p.transposeTo(0), 12, cmSize, maxEmpty)

def outLilypondFilePath = ""
def outTextFilePath = ""
combinatorialCMs.eachWithIndex{ cm, index ->
	def cmIndex = sprintf('%03d', index)
	outTextFilePath = "/Users/marqrdt/Music/composition/PRMMusic/counterpoint-${cmSize}-rows-CM${cmIndex}-output.txt"
	outLilypondFilePath = "/Users/marqrdt/Music/composition/PRMMusic/counterpoint-${cmSize}-rows-CM${cmIndex}.ly"
	List<CompositionMatrix> cmTransformations = new ArrayList<CompositionMatrix>()
	new File(outTextFilePath).withWriter('utf-8') { writer ->
		Iterator<CompositionMatrix> cmTransformationIt = cm.transformationIterator()
		while ( cmTransformationIt.hasNext() ) {
			CompositionMatrix newCM = cmTransformationIt.next()
			def formattedCM = CompositionMatrixUtils.format( newCM, " ", "|", true );
			def cmJson = newCM.toJsonString()
			newCM.setName( "CM${index}-${newCM.getDescriptor()}" )
			cmTransformations.add(newCM)
			println formattedCM
			writer.writeLine formattedCM
			println cmJson
			writer.writeLine cmJson
			writer.writeLine "==================================\n"
		}
	}
	/*
	def cmDecorator = new LilypondAbstractCMDecorator(cmTransformations)
	new File(outLilypondFilePath).withWriter('utf-8') {
		writer -> writer.writeLine cmDecorator.output()
	}
	*/
}

/**
def lilypondOutfilePath = "/Users/marqrdt/counterpoint-${cmSize}-rows.ly"
def cmDecorator = new LilypondAbstractCMDecorator(combinatorialCMs)
new File(lilypondOutfilePath).withWriter('utf-8') {
	writer -> writer.writeLine cmDecorator.output()
}
**/

