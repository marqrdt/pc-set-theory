package com.newscores.setTheory;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.*;
import com.newscores.setTheory.PitchClassSequence;
import com.newscores.setTheory.PitchSequence;
import com.newscores.setTheory.PitchClassSet;
import com.newscores.setTheory.PitchSequence;
import com.newscores.setTheory.ObjectSerializers;
import com.newscores.setTheory.utils.*;;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.newscores.setTheory.interfaces.*;
import com.newscores.setTheory.utils.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestObjectSerializers extends TestCase {

	List<PitchClassSequence> testPSeqs = new ArrayList<PitchClassSequence>();
	String testFileName = "com/newscores/setTheory/TestPitchClassSequenceData.json";
	
	protected void setUp() throws Exception {
		super.setUp();
		String testJson = new String();
		try
        {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Path testFilePath = Paths.get(classLoader.getResource(testFileName).toURI());
			testJson = new String ( Files.readAllBytes( testFilePath ) );
			assertTrue("Ensure that JSON data is populated",testJson.length() > 0 );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}

	public void testPitchClassSequenceSerializer() {
		PitchClassSequence pSeq = SequenceUtils.getRandomPitchClassSequence( 8, true);
		pSeq.setName("A random PitchClassSequence");
		Iterator<PitchClassSequence> pcSeqIt = pSeq.transformationIterator();
		while ( pcSeqIt.hasNext() ) {
			PitchClassSequence seq = pcSeqIt.next();
			String serialzed = ObjectSerializers.sequenceJsonSerializer(seq);
			System.out.println( serialzed);
			assertEquals("Json generated from the ObjectSerializer static method and the object's toJsonString() method should be equal",
					serialzed, seq.toJsonString() );
			PitchClassSequence outSeq = new PitchClassSequence();
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				outSeq = objectMapper.readValue(serialzed, PitchClassSequence.class);
				assertTrue("PitchClassSequence deserialized from Json should be equal to the source",
						seq.equals(outSeq) );
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public void testPitchSequenceSerializer() {
		int numTests = 100;
		IntStream.range(0, numTests).
			forEach( p -> {
				PitchSequence pSeq = SequenceUtils.getRandomPitchSequence( 100, 0, 80, true );
				pSeq.setName("A random PitchSequence");
				String serialzed = ObjectSerializers.sequenceJsonSerializer(pSeq);
				System.out.println( serialzed);
				assertEquals("Json generated from the ObjectSerializer static method and the object's toJsonString() method should be equal",
					serialzed, pSeq.toJsonString() );
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					PitchSequence outSeq = objectMapper.readValue(serialzed, PitchSequence.class);
					assertTrue("PitchSequence deserialized from Json should be equal to the source",
							pSeq.equals(outSeq) );
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	}

	public void testCompositionMatrixToJson() {
		Integer[] array00 = {0,1,2};
		PitchClassSequence seq00 = new PitchClassSequence( array00 );
		
		Integer[] array01 = {3,4,5,6};
		PitchClassSequence seq01 = new PitchClassSequence( array01 );

		Integer[] array02 = {7,8,9,10,11};
		PitchClassSequence seq02 = new PitchClassSequence( array02 );

		Integer[] array10 = {3,4,5,6};
		PitchClassSequence seq10 = new PitchClassSequence( array10 );
		
		Integer[] array11 = {7,8,9,10,11};
		PitchClassSequence seq11 = new PitchClassSequence( array11 );

		Integer[] array12 = {0,1,2};
		PitchClassSequence seq12 = new PitchClassSequence( array12 );

		Integer[] array20 = {7,8,9,10,11};
		PitchClassSequence seq20 = new PitchClassSequence( array20 );
		
		Integer[] array21 = {0,1,2};
		PitchClassSequence seq21 = new PitchClassSequence( array21 );

		Integer[] array22 = {3,4,5,6};
		PitchClassSequence seq22 = new PitchClassSequence( array22 );

		// create a simple CM with 1 Row and 2 Columns
		CompositionMatrix cm3x3 = new CompositionMatrix();
		
		cm3x3.addSegment(seq00, 0, 0);
		cm3x3.addSegment(seq01, 0, 1);
		cm3x3.addSegment(seq02, 0, 2);

		cm3x3.addSegment(seq10, 1, 0);
		cm3x3.addSegment(seq11, 1, 1);
		cm3x3.addSegment(seq12, 1, 2);

		cm3x3.addSegment(seq20, 2, 0);
		cm3x3.addSegment(seq21, 2, 1);
		cm3x3.addSegment(seq22, 2, 2);
		
		Iterator<CompositionMatrix> cmIt = cm3x3.transformationIterator();
		while ( cmIt.hasNext() ) {
			try {
				CompositionMatrix cm = cmIt.next();
				ObjectMapper objectMapper = new ObjectMapper();
				String cmJson = objectMapper.writeValueAsString(cm);
				System.out.println( String.format("Original CM as Json is:\n%s\n%s",
						cmJson,
						CompositionMatrixUtils.format(cm)
						));
				//CompositionMatrix testCM = ObjectSerializers.compositionMatrixJsonDeserializer(cmJson);
				CompositionMatrix testCM = objectMapper.readValue(cmJson, CompositionMatrix.class);
				System.out.println( String.format("Deserialized CM as Json is:\n%s\n%s",
						objectMapper.writeValueAsString(testCM),
						CompositionMatrixUtils.format(testCM)
						));
				
				assertTrue("CompositionMatrix serialized to Json and back should be equal", cm.equals(testCM));
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}