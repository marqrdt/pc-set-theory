package com.newscores.setTheory.interfaces;

import java.util.*;

import com.newscores.setTheory.SetIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

/**
 * The AbstractPitchSet interface defines the contract for Pitch Set classes.
 * @author marqrdt
 *
 */
@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "@type"
)
@JsonTypeIdResolver(SetIdResolver.class)
@JsonPropertyOrder({ "name", "members" })
public interface IPitchSet extends IPitchCollection {

	// The transposition operator
	public IPitchSet T(int transposition);

	public IPitchSet I();

	public IPitchSet M();

	public void addPitch(Integer pitchToAdd);

	public void addPitch(String pitch);
	
	public void extend(IPitchSet anotherSet);

	public int size();

	public boolean equals(IPitchSet anotherSet);

	public String toString();
	
	public String getName();
	
	public void setName( String name );
}
