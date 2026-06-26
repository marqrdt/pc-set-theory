 package org.paulmarquardt.setTheory;

import java.io.IOException;
import java.util.*;

import org.paulmarquardt.setTheory.interfaces.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

 @JsonPropertyOrder({ "name", "members", "transformation", "descriptor" })
@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "@type")
		@JsonSubTypes({ 
		  @Type(value = PitchClassSequence.class, name = "pitch_class_sequence"), 
		  @Type(value = PitchSequence.class, name = "pitch_sequence"),
		})

/**
 * The Abstract Base Class for Sequence types.
 * @author marqrdt
 *
 */
public abstract class BaseSequence implements IMutableSequence, Iterable {
	List<Integer> pitches;
	String descriptor;
	Map<String,Integer> history;
	PitchClassSequenceTransformation transformation;
	String name;
		
	/**
	 * Returns a String representation of this.
	 * @return  A String representation of this.
	 */
	@Override
	public String toString() {
		StringBuffer outputBuf = new StringBuffer();
		outputBuf.append( "<" );
		int count = 0;
		for ( Integer elem : this.getMembers() ) {
			outputBuf.append( Integer.toString(elem) );
			if ( count < this.getMembers().size() - 1 ) {
				outputBuf.append(" ");
			}
			count++;
		}
		outputBuf.append(">");
		return outputBuf.toString();
	}

	/**
	 * Returns a JSon String representation of this.
	 * @return  A String representation of this.
	 */
	public String toJsonString() {
		String outJson = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			outJson = objectMapper.writeValueAsString(this);			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outJson;
	}

	/**
	 * Sets a text name for this. Could be something like "My favorite sequence of pitches" or "Berg Violin Concerto row".
	 * @param name  The descriptor to set.
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * Returns the text name for this.
	 * @return String  The text name for this.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets a text descriptor for this. Could be something like "My favorite sequence of pitches" or "Berg Violin Concerto row".
	 * Several transformation operations manipulate the descriptor with text such as "T[m]M[n]".
	 * @param descriptor  The text descriptor of this.
	 */
    public void setDescriptor(String descriptor) {
		// TODO Auto-generated method stub
		this.descriptor = descriptor;
	}

	/**
	 * Returns the text descriptor for this.
	 * @return String  The text descriptor for this.
	 */
	public String getDescriptor() {
		return this.descriptor;
	}

	/**
	 * Returns the members of this as a List.
	 * @return  A List containing the members of this.
	 */
	public List<Integer> getMembers() {
		// TODO Auto-generated method stub
		return this.pitches;
	}
}
