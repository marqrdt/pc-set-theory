package org.paulmarquardt.setTheory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.*;

import org.paulmarquardt.setTheory.interfaces.*;
import org.apache.commons.lang.StringUtils;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectSerializers {

	public static String sequenceJsonSerializer( ISequence inSeq ) {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(inSeq);			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serialized;
	}
	
	public static PitchSequence pitchSequenceJsonDeserializer( String seqJson ) {
		PitchSequence outSeq = new PitchSequence();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			outSeq = objectMapper.readValue(seqJson, PitchSequence.class);			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outSeq;
	}
	public static PitchClassSequence pitchClassSequenceJsonDeserializer( String seqJson ) {
		PitchClassSequence outSeq = new PitchClassSequence();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			outSeq = objectMapper.readValue(seqJson, PitchClassSequence.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outSeq;
	}


	public static String setJsonSetSerializer( IPitchSet inSet ) {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(inSet);			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serialized;
	}

	/*
	public static PitchSet setJsonPitchSetDeserializer( String setJson ) {
		PitchSet outSet;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			outSet = objectMapper.readValue(setJson, PitchSet.class);			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outSet;
	}
	*/
	
	public static String compositionMatrixJsonSerializer( CompositionMatrix inCM ) {
		String serialized = new String();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(inCM);			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serialized;
	}

	public static CompositionMatrix compositionMatrixJsonDeserializer( String cmJson ) {
		CompositionMatrix outCM = new CompositionMatrix();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addDeserializer(CompositionMatrix.class, new CompositionMatrixDeserializer(CompositionMatrix.class) );
			objectMapper.registerModule( module );
			outCM = objectMapper.readValue(cmJson, CompositionMatrix.class);			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outCM;
	}

}