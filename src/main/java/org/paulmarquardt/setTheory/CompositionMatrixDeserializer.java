package org.paulmarquardt.setTheory;

import org.paulmarquardt.setTheory.*;
import org.paulmarquardt.setTheory.interfaces.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.*;

import java.io.IOException;

public class CompositionMatrixDeserializer extends StdDeserializer<CompositionMatrix> {

    public CompositionMatrixDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CompositionMatrix deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
    	CompositionMatrix cm = SetTheoryFactories.getCompositionMatrix();
    	ObjectMapper mapper = new ObjectMapper();
        JsonNode nodeTop = parser.getCodec().readTree(parser);
        Iterator<JsonNode> nodeIt = nodeTop.elements();
        while( nodeIt.hasNext() ) {
        	JsonNode node = nodeIt.next();
        	if ( node.isArray() ) {
        		for ( JsonNode n : node ) {
                	System.out.println( String.format("Segment %s", n.toString() ));
                	ISequence pSeq = mapper.readValue(n.toString(), PitchClassSequence.class);
        		}
        	}
        }
        return cm;
    }
}
