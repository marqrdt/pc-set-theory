package org.paulmarquardt.setTheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.*;

import org.paulmarquardt.setTheory.interfaces.*;

public class PitchClassSet implements IPitchSet,Iterable<PitchClassSet>,Comparable<PitchClassSet> {

	// If you're using some musical system with more or less than twelve pitches
	// to the octave
	public static final int MODULUS = 12;
	// private TreeSet<Integer> members
	private Integer[] membersArray;
	private String name;
	private String description;
	private int modulus = PitchClassSet.MODULUS;
	public static int A = 10;
	public static int B = 11;
	
	/**
	 * Constructor using a String containing pitch class tokens, which include single digits [0-9] and the letters 'a','b','A','B', with A = 10 amd B = 11
	 * @param pitchClassString  The String representing the PCs from which this will be constructed.
	 */
	public PitchClassSet( String pitchClassString ) {
		// this.members = new TreeSet<Integer>();
		// default PitchClassSet has no members.
		this( PitchClassSet.getPitchClassSetFromString( pitchClassString ).getMembers() );
		//PitchClassSet pcSet = PitchClassSet.getPitchClassSetFromString( pitchClassString );
		//System.out.println( String.format("Set from string %s is %s", pitchClassString, pcSet.toString() ) );
	}

	/**
	 * Constructor using a String containing pitch class tokens, which include single digits [0-9] and the letters 'a','b','A','B',
	 * with A = 10 amd B = 11, and a set name.
	 * @param pitchClassString  A String from which to construct the PitchClassSet.
	 * @param name  A name for this.
	 */
	public PitchClassSet( String pitchClassString, String name ) {
		// this.members = new TreeSet<Integer>();
		// default PitchClassSet has no members.
		this( PitchClassSet.getPitchClassSetFromString( pitchClassString ).getMembers() );
		PitchClassSet pcSet = PitchClassSet.getPitchClassSetFromString( pitchClassString );
		//System.out.println( String.format("Set from string %s is %s", pitchClassString, pcSet.toString() ) );
	}

	/**
	 * The default constructor, creating an empty PitchClassSet.
	 */
	public PitchClassSet() {
		// this.members = new TreeSet<Integer>();
		// default PitchClassSet has no members.
		this.initMembersArray();
		this.setName("");
		this.setDescription("");
		//this.modulus = PitchClassSet.MODULUS;
	}

	/**
	 * Constructor using a array of Integers representing the pitch numbers of the members.
	 * The numbers do not need to be constrained to the set of integers 0-11.
	 * @param pitches An Array of Integer containing the pitches from which this will be constructed.
	 */
	public PitchClassSet(Integer[] pitches) {
		// this.members = new TreeSet<Integer>();
		// assign membersArray
		this.initMembersArray();
		for (int index = 0; index < pitches.length; index++) {
			//this.membersArray[ mod(pitches[index], PitchClassSet.MODULUS ) ] = 1;
			//System.out.println(  pitches.toString() );
			//System.out.println( String.format("%d mod %d is %d. Can not believe it got to this point...", pitches[index], PitchClassSet.MODULUS, mod(pitches[index], PitchClassSet.MODULUS ) ) );
			this.membersArray[ mod(pitches[index], PitchClassSet.MODULUS ) ] = 1;
		}
	}

	/**
	 * Constructor using an Integer for which the 1-bits in its binary representation form the members of the PitchClassSet.
	 * The numbers do not need to be constrained to the set of integers 0-11.
	 * @param fromSignature An Integer from which the 1-bits of its binary representation form the members of this.
	 */
	public PitchClassSet( int fromSignature) {
		// this.members = new TreeSet<Integer>();
		// assign membersArray
		this( PitchClassSet.getPitchClassSetFromSignature(fromSignature) );
	}

	/**
	 * Constructor using any class implementing the IPitchSet interface.
	 * Note that the name and description attributes are not inherited from inSet.
	 * @param inSet  An IPitchSet implementation containing the pitches from which this will be constructed.
	 */
	public PitchClassSet( IPitchCollection inSet ) {
		this( inSet.getMembers() );
	}
	
	// Create a PitchClassSet instance from a List of Integers.
	// Create an array with the elements of List and call the PitchClassSet
	// array constructor.
	// It's much safer to have only one constructor that does the construction
	// logic.
	/**
	* Create a PitchClassSet instance from a List of Integers.
	 * @param pitchesAsList  An Collection of Integer containing the pitches from which this will be constructed.
	 */
	public PitchClassSet(Collection<Integer> pitchesAsList) {
		this( pitchesAsList, "");
	}

	/**
	* Create a PitchClassSet instance from a List of Integers and a String name.
	 * @param pitchesAsList  An Collection of Integer containing the pitches from which this will be constructed.
	 * @param name  A String identifier assigned to this.
	 */
	public PitchClassSet(Collection<Integer> pitchesAsList, String name) {
		//this.modulus = PitchClassSet.MODULUS;
		this.initMembersArray();
		Iterator<Integer> memberIt = pitchesAsList.iterator();
		while (memberIt.hasNext()) {
			Integer index = mod(memberIt.next(), PitchClassSet.MODULUS);
			// System.out.println( "Setting array index " + index.toString() +
			// " to 1");
			this.membersArray[index] = 1;
		}
		this.setName(name);
		this.setDescription("");
	}

	/**
	 * Return a new PitchClassSet transposed by transposition.
	 * @param transposition  The amount by which to transpose this.
	 * @return PitchClassSet transposed by transposition.
	 */
	public PitchClassSet T(int transposition) {
		List<Integer> arrayList = new ArrayList<Integer>();
		for (int index = 0; index < this.membersArray.length; index++) {
			// System.out.println( this.membersArray[index] );
			if (this.membersArray[index] == 1) {
				arrayList.add(mod(index + transposition, PitchClassSet.MODULUS));
			}
		}
		return new PitchClassSet(arrayList);
	}

	private void initMembersArray() {
		this.membersArray = new Integer[PitchClassSet.MODULUS];
		for (int index = 0; index < PitchClassSet.MODULUS; index++) {
			this.membersArray[index] = 0;
		}
	}

	/**
	 * Return a new PitchClassSet which is the inversion of this.
	 * Inversion is defined as: for each element e in this, this.I() contains member p such that e + p = 0 % 12.
	 * @return  The inversion of this, defined as the PitchClassSet containing j where j = (12 - i) % 12 for each i in this. 
	 */
	public PitchClassSet I() {
		List<Integer> arrayList = new ArrayList<Integer>();
		for (int index = 0; index < this.membersArray.length; index++) {
			// System.out.println( this.membersArray[index] );
			if (this.membersArray[index] == 1) {
				arrayList.add(mod(12 - index, PitchClassSet.MODULUS));
			}
		}
		return new PitchClassSet(arrayList);
	}

	/**
	 * Return a new PitchClassSet which is the M(5) transformation of this, considered in this case as the default M transformation.
	 * M transformation is defined as: for each element e in this, this.I() contains member p such that p = e * mult % 12.
	 * @return PitchClassSet  A PitchClassSet with the M(5) operation applied.
	 */
	public PitchClassSet M() {
		// Iterator<Integer> memberIt = this.members.iterator();
		return this.M(5);
	}

	/**
	 * Return a new PitchClassSet which is the M transformation of this.
	 * M transformation is defined as: for each element e in this, this.I() contains member p such that p = e * mult % 12.
	 * @param mult  the number by which all members will be multiplied mod 12.
	 * @return PitchClassSet  A PitchClassSet with the M(mult) % 12 operation applied.
	 */
	public PitchClassSet M(int mult) {
		// Iterator<Integer> memberIt = this.members.iterator();
		List<Integer> arrayList = new ArrayList<Integer>();
		for (int index = 0; index < this.membersArray.length; index++) {
			// System.out.println( this.membersArray[index] );
			if (this.membersArray[index] == 1) {
				arrayList.add(mod(index * mult, PitchClassSet.MODULUS));
			}
		}
		return new PitchClassSet(arrayList);
	}

	/**
	 * An alias of the M transformation, defined as: for each element e in this, this.I() contains member p such that p = e * mult % 12.
	 * Included in support of the overloaded "*" operator in a Groovy DSL.
	 * @param mult  the number by which all members will be multiplied mod 12.
	 * @return PitchClassSet  A PitchClassSet with the M(mult) % 12 operation applied.
	 */
	public PitchClassSet multiply(int mult) {
		return this.M(mult);
	}

	/**
	 * Return a new PitchClassSet that is the logical intersection of this and inPitchClassSet.
	 * This set will contain all elements that are in both sets. 
	 * @param currentSet  The PitchClassSet that will be intersected with this to form the returned Object.
	 * @return PitchClassSet  A PitchClassSet that is the intersection of this and inPitchClassSet
	 */
	public PitchClassSet intersection(PitchClassSet currentSet) {
		// Iterator<Integer> memberIt = this.members.iterator();
		PitchClassSet newSet = new PitchClassSet();
		for (int i = 0; i < this.membersArray.length; i++) {
			if (currentSet.containsPitch(i) && this.containsPitch(i)) {
				newSet.addPitch(i);
			}
		}
		return newSet;
	}

	/**
	 * Return a new PitchClassSet that is the logical intersection of this and inPitchClassSet.
	 * This set will contain all elements that are in both sets. 
	 * This method is an alias of intersection() method in support of the overloaded "&amp;" operator in a Groovy DSL.
	 * @param currentSet  The PitchClassSet that will be intersected with this to form the returned Object.
	 * @return PitchClassSet  A PitchClassSet that is the intersection of this and inPitchClassSet
	 */
	public PitchClassSet and(PitchClassSet currentSet) {
		return this.intersection(currentSet);
	}

	/**
	 * Return a new PitchClassSet that is the logical union of this and inPitchClassSet.
	 * This set will contain elements that are in either set. 
	 * @param inPitchClassSet  The PitchClassSet that will be joined with this to form the returned Object.
	 * @return PitchClassSet  A PitchClassSet that is the union of this and inPitchClassSet
	 */
	public PitchClassSet union(PitchClassSet inPitchClassSet) {
		// Iterator<Integer> memberIt = this.members.iterator();
		PitchClassSet newSet = new PitchClassSet( this );
		for ( int member : inPitchClassSet.getMembers() ) {
			newSet.addPitch(member);
		}
		return newSet;
	}

	/**
	 * Return a new PitchClassSet that is the logical union of this and inPitchClassSet.
	 * This set will contain elements that are in either set. 
	 * This method is an alias of union() method in support of the overloaded "|" operator in a Groovy DSL.
	 * @param inPitchClassSet  The PitchClassSet that will be joined with this to form the returned Object.
	 * @return PitchClassSet  A PitchClassSet that is the union of this and inPitchClassSet
	 */
	public PitchClassSet or(PitchClassSet inPitchClassSet) {
		return this.union(inPitchClassSet);
	}

	/**
	 * Return a new PitchClassSet that is the complement of this.
	 * Complement is defined as all PCs that are not a member of this.
	 * @return A PitchClassSet that is the complement of this PitchClassSet
	 */
	public PitchClassSet complement() {
		PitchClassSet newSet = new PitchClassSet();
		for (int i = 0; i < this.membersArray.length; i++) {
			if (this.membersArray[i] == 0) {
				newSet.addPitch(i);
			}
		}
		return newSet;
	}

	/**
	 * Return a new PitchClassSet that is the complement of this.
	 * Complement is defined as all PCs that are not a member of this.
	 * This method is an alias of complement() method in support of the overloaded "~" operator in a Groovy DSL.
	 * @return A PitchClassSet that is the complement of this PitchClassSet
	 */
	public PitchClassSet bitwiseNegate() {
		return this.complement();
	}

	/**
	 * Return a new PitchClassSet that is the XOR of this.
	 * XOR is defined as all PCs that are not a member of this.
	 * This method is an alias of complement() method in support of the overloaded "~" operator in a Groovy DSL.
	 * @param inPitchClassSet  The PitchClassSet that will be xor'ed with this to form the returned Object.
	 * @return A PitchClassSet that is the complement of this PitchClassSet
	 */
	public PitchClassSet xor(PitchClassSet inPitchClassSet) {
		return this.union(inPitchClassSet).complement();
	}

	/**
	 * Return a new PitchClassSet that is the complement of this.
	 * Complement is defined as all PCs that are not a member of this.
	 * This method is an alias of complement() method in support of the overloaded "~" operator in a Groovy DSL.
	 * @return A PitchClassSet that is the complement of this PitchClassSet
	 */
	public PitchClassSet bitWiseNegate() {
		return this.complement();
	}

	/**
	 * Return a PitchClassSet that contains all PCs. As it is useful for many calculations and requires not state data, it is implemented as a static method.
	 * @return A PitchClassSet containing all PCs.
	 */
	public static PitchClassSet aggregate() {
		return new PitchClassSet( new Integer[] {0,1,2,3,4,5,6,7,8,9,10,11} );
	}

	/**
	 * Return the set of all subsets of this as a List&lt;PitchClassSet&gt;.
	 * @return List&lt;PitchClassSet&gt; of all subsets of this, including this and {} (the empty set).
	 */
	public List<PitchClassSet> subsetList() {
		Logger log = Logger.getLogger( this.getName() );
		List<PitchClassSet> subsets = new ArrayList<PitchClassSet>();
		int powerSetRange = (int) Math.pow(2, this.getMembers().size()) - 1;
		int index = 0;
		while ( index <= powerSetRange ) {
			int num = index;
			PitchClassSet pcSet = new PitchClassSet();
			int setIndex = 0;
			while ( num > 0 ) {
				//log.info(String.format("num = %d, setIndex = %d", num, setIndex));					
				if ( (num % 2) == 1) {
					//log.info(String.format("Adding PCSet member %d", this.pcSetAsArrayList().get(setIndex)));
					pcSet.addPitch( this.pcSetAsArrayList().get(setIndex) );
				}
				num = num>>>1;
				setIndex++;
			}
			index++;
			subsets.add(pcSet);
		}
		// add the null set...
		return subsets;
	}

	/**
	 * Returns an Iterator&lt;PitchClassSet&gt; that will iterate through all subsets of this including this and {} (the empty set}.
	 * @return Iterator&lt;PitchClassSet&gt; of all subsets of this, including this and {} (the empty set).
	 */
	public Iterator<PitchClassSet> subsetIterator() {
		// TODO Auto-generated method stub
		final PitchClassSet thisSet = this;
		Iterator<PitchClassSet> setIt = new Iterator<PitchClassSet>() {
			private int currentIndex = 0;
			private boolean inversionSwitch = false;
			public boolean hasNext() {
				return currentIndex < (int) Math.pow(2, thisSet.size());
			}
			public PitchClassSet next() {
				PitchClassSet pcSet = new PitchClassSet();
				int num = currentIndex;
				int setIndex = 0;
				while ( num > 0 ) {
					//log.info(String.format("num = %d, setIndex = %d", num, setIndex));					
					if ( (num % 2) == 1) {
						//log.info(String.format("Adding PCSet member %d", this.pcSetAsArrayList().get(setIndex)));
						pcSet.addPitch( thisSet.pcSetAsArrayList().get(setIndex) );
					}
					num = num>>>1;
					setIndex++;
				}
				currentIndex++;
				return pcSet;
			}
		};
		return setIt;
	}

	/**
	 * Returns an Iterator&lt;PitchClassSet&gt; that will iterate through all subsets of this of the size of
	 * cardinality parameter, including this and {} (the empty set}.
	 * @return Iterator&lt;PitchClassSet&gt; of all subsets of this, including this and {} (the empty set).
	 * @param cardinality Iterate through subsets only of this cardinality.
	 */
	public Iterator<PitchClassSet> subsetIterator(int cardinality) {
		// TODO Auto-generated method stub
		final PitchClassSet thisSet = this;
		final int setCardinality = cardinality;
		Iterator<PitchClassSet> setIt = new Iterator<PitchClassSet>() {
			Logger log = Logger.getLogger( thisSet.getName() );
			private int currentIndex = 0;
			private boolean foundAll = false;
			List<Integer> matchingBitCounts = new ArrayList<Integer>();
			public boolean hasNext() {
				/*
				 * if currentIndex == 0, this is the first call in the method.
				 * Populate matchingBitCounts with all integers between 0 and the size 
				 * of thisSet's powerset ( 2 ** thisSet.size() ) that have a bitCount
				 * equal to setCardinality.
				*/
				if ( currentIndex == 0 ) {
					for ( int i = 0; i < (int) Math.pow(2, thisSet.size()); i++ ) {
						if ( Integer.bitCount(i) == setCardinality ) {
							matchingBitCounts.add(i);
						}
					}
				}
				if ( currentIndex < matchingBitCounts.size() ) {
					return true;
				} else {
					//log.info( String.format( "++++++++++++ hasNext() returned false at %d ++++++++++++", currentIndex));
					return false;
				}
			}
			public PitchClassSet next() {
				PitchClassSet pcSet = new PitchClassSet();
				int num = matchingBitCounts.get(currentIndex);
				int setIndex = 0;
				//while ( setIndex < thisSet.size() ) {
				while ( num > 0 && setIndex < thisSet.size()) {
					//log.info(String.format("num = %d, setIndex = %d", num, setIndex));					
					if ( (num % 2) == 1) {
						pcSet.addPitch( thisSet.pcSetAsArrayList().get(setIndex) );
					}
					num = num>>>1;
					setIndex++;
				}
				//log.info("============= Finished next() method =============");
				if ( pcSet.equals(thisSet) && setCardinality != pcSet.size() ) {
					//log.info( String.format("Illegal state: currentIndex = %d, Bit Count = %d", currentIndex, Integer.bitCount(currentIndex)));
					throw new IllegalStateException( String.format("Illegal state: set size = %d, cardinality = %d, currentIndex = %d, Bit Count = %d", pcSet.size(), setCardinality, currentIndex, Integer.bitCount(currentIndex)));
				}
				currentIndex++;
				return pcSet;
			}
		};
		return setIt;
	}

	/**
	 * Adds a PC to this. If the PC already exists, it is silently ignored.
	 * @param pitchToAdd  The PC to add to this.
	 */
	public void addPitch(Integer pitchToAdd) {
		// return this.members.add( pitchToAdd );
		this.membersArray[mod(pitchToAdd, PitchClassSet.MODULUS)] = 1;
	}

	/**
	 * Removes a PC from this. If the PC does not exist, it is silently ignored.
	 * @param pitchToRemove  The PC to remove from this.
	 */
	public void removePitch(Integer pitchToRemove) {
		// return this.members.add( pitchToAdd );
		this.membersArray[mod(pitchToRemove, PitchClassSet.MODULUS)] = 0;
	}

	/**
	 * Returns true if this contains pitch, false if not.
	 * @param pitch  The pitch that membership will be tested against.
	 * @return boolean  true if this contains pitch, false if not.
	 */
	public boolean containsPitch(Integer pitch) {
		return this.membersArray[mod(pitch, PitchClassSet.MODULUS)] == 1;
	}

	
	/**
	 * Adds a PC to this using a String value for pitch. If the PC already exists, it is silently ignored.
	 * @param pitch  The String representing the PC to add to this.
	 */
	public void addPitch(String pitch) {
		try {
			if (pitch.equalsIgnoreCase("a")) {
				this.membersArray[10] = 1;
			} else if (pitch.equalsIgnoreCase("b")) {
				this.membersArray[11] = 1;
			} else {
				this.membersArray[mod(Integer.decode(pitch), PitchClassSet.MODULUS)] = 1;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// ("Illegal pitch name: " + pitch);
		}
	}

	/**
	 * There are a few normal form algorithms. This implementation returns the normal form that is the minimum binary representation of this.
	 * @return  A PitchClassSet representing the Normal Form of this.
	 */
	public PitchClassSet getNormalForm() {
		int minPc = 0;
		PitchClassSet returnPCSet = new PitchClassSet(); // = new
															// PitchClassSet();
		ArrayList<Integer> pitchClassSetAsList = this.pcSetAsArrayList();
		// get a List containing the pcs of this instance.
		// account for the null PCSet -- a set with no members.
		// In this case, the PitchClassSet of the empty set will return null.
		if (pitchClassSetAsList.size() == 0) {
			return new PitchClassSet();
		}
		// Account for the case that the PitchClass set has only one member.
		// In this case, return PitchClassSet(0), since a PCSet with one member
		// is equivalent to [0]
		if (pitchClassSetAsList.size() == 1) {
			Integer[] zeroSet = { 0 };
			return new PitchClassSet(zeroSet);
		}
		// Account for the case that the PitchClassSet has two members.
		// In this case, return this transposed by 0 minus the first element,
		// i.e. transposed to zero.
		//if (pitchClassSetAsList.size() == 2) {
		//	return this.T(0 - pitchClassSetAsList.get(0));
		//}
		// Declare an Integer array
		Integer[] pitches = new Integer[PitchClassSet.MODULUS];
		Map<PitchClassSet, Integer> pcSetMap = new HashMap<PitchClassSet, Integer>();
		// Initialize to zero
		for (int index = 0; index < pitches.length; index++) {
			pitches[index] = 0;
		}
		// set signature to the maximum possible value
		int signature = (int) Math.pow(2.0, PitchClassSet.MODULUS);
		// Find the minimum pc in this.membersArray (this first non-zero
		// element).
		// Entry<PitchClassSet, Integer> min = null;
		List<Integer> tempList = pitchClassSetAsList;
		PitchClassSet tempPcSet = new PitchClassSet(pitchClassSetAsList)
				.T(12 - (mod(pitchClassSetAsList.get(0), PitchClassSet.MODULUS)));
		PitchClassSet invertedSet = tempPcSet
				.T(12 - pitchClassSetAsList.get(0)).I();
		pcSetMap.put(tempPcSet, tempPcSet.getSignature());
		if (invertedSet.getSignature() < tempPcSet.getSignature()) {
			pcSetMap.put(invertedSet, invertedSet.getSignature());
		}
		int minSignature = Collections.min(pcSetMap.values());
		for (int i = 0; i < tempList.size(); i++) {
			Integer temp = tempList.get(0);
			tempList.remove(0);
			// aList.r
			tempList.add(temp + 12);
			// System.out.println( new PitchClassSet( aList ).toString() );
			tempPcSet = new PitchClassSet(tempList).T(12 - (mod(
					tempList.get(0), PitchClassSet.MODULUS)));
			// System.out.println( "Rotation set: " + tempPcSet.toString() +
			// "\tSignature:" + tempPcSet.getSignature().toString() );
			invertedSet = tempPcSet.I().T(
					Collections.max(tempPcSet.pcSetAsArrayList()) - PitchClassSet.MODULUS);
			// System.out.println( "Inverted rotation set: " +
			// invertedSet.toString() + "\tSignature:" +
			// invertedSet.getSignature().toString() );
			if (invertedSet.getSignature() < tempPcSet.getSignature()) {
				tempPcSet = invertedSet;
				// System.out.println( "Rotated set inverted:" +
				// tempPcSet.toString() + " :" +
				// tempPcSet.getSignature().toString() );
			}
			if (tempPcSet.getSignature() <= minSignature) {
				pcSetMap.put(tempPcSet, tempPcSet.getSignature());
				// System.out.println(
				// "Set has minimum signature, adding to HashMap:" +
				// tempPcSet.toString() + " :" +
				// tempPcSet.getSignature().toString() );
				minSignature = tempPcSet.getSignature();
			}
		}
		for (Entry<PitchClassSet, Integer> entry : pcSetMap.entrySet()) {
			// System.out.println( "Entry: " + entry.getKey().toString() +
			// "\tSignature: " + entry.getValue().toString() );
			if (entry.getValue() <= minSignature) {
				returnPCSet = entry.getKey();
			}
		}
		return returnPCSet;
	}

	/**
	 * A simple, shortened alias for getNormalForm().
	 * @return  A PitchClassSet representing the Normal Form of this.
	 */
	public PitchClassSet NF() {
		return this.getNormalForm();
	}
	
	/**
	 * Return an array of the interval class vector of this. The interval class vector is an array of 6 integers.
	 * The value at each index n is the count of interval-class n is contained in this.
	 * @return  The array representing the interval class vector of this.
	 */
	public Integer[] icVector() {
		Integer[] vector = new Integer[PitchClassSet.MODULUS / 2];
		for (int index = 0; index < vector.length; index++) {
			vector[index] = 0;
		}
		List<Integer> pcList = this.pcSetAsArrayList();
		for (int i = 0; i < pcList.size(); i++) {
			for (int j = i + 1; j < pcList.size(); j++) {
				if (Math.abs(pcList.get(j) - pcList.get(i)) > 0) {
					if (Math.abs(pcList.get(j) - pcList.get(i)) >= 6) {
						vector[(MODULUS - Math.abs(pcList.get(j)
								- pcList.get(i))) - 1] += 1;
					} else {
						vector[(Math.abs(pcList.get(j) - pcList.get(i))) - 1] += 1;
					}
				}
			}
		}
		return vector;
	}

	/**
	 * Return a List of the distances between consecutive elements of this, sorted in natural (ascending) order.
	 * @return  The array representing the distances between elements of this, sorted in natural (ascending) order.
	 */
	public List<Integer> consecutiveIntervalVector() {
		List<Integer> intervals = new ArrayList<Integer>();
		// If this has no members, don't bother with any calculations.
		if ( this.getMembers().size() < 1 ) {
			return intervals;
		}
		List<Integer> membersList = new ArrayList<Integer>( this.getMembers() );
		Collections.sort( membersList );
		for ( int i = 0; i < membersList.size() - 1; i++ ) {
			intervals.add( PitchClassSet.distance( membersList.get(i), membersList.get(i + 1) ) );
		}
		intervals.add( membersList.get(0) + PitchClassSet.MODULUS - membersList.get(membersList.size() -1 ) );
		return intervals;
	}

	/**
	 * Return the distance between two numbers, which is always a positive number.
	 * @param a  The a value.
	 * @param b  The b value.
	 * @return The distance between the two numbers
	 */
	public static int distance( int a, int b ) {
		return Math.abs( a - b );
	}
	
	/**
	 * This is a convenience method returning the number of unique distances in this.consecutiveIntervalVector().
	 * It can be useful in determining whether this is an instance of a scale or other interval-class constrained set.
	 * @return  The number of unique distances in this.consecutiveIntervalVector().
	 */
	public int intervalSpread() {
		return new TreeSet<Integer>( this.consecutiveIntervalVector() ).size();
	}
	
	/**
	 * Returns a 12-element array of Integer. Array element n = 1 if this contains PC n, otherwise, n = 0.
	 * @return  An array of members of this.
	 */
	public Integer[] getMembersArray() {
		return this.membersArray;
	}

	/**
	 * Returns a List containing all PCs in this. The returned List will always be sorted in ascending order.
	 * @return  A List containing all PCs in this.
	 */
	public ArrayList<Integer> pcSetAsArrayList() {
		ArrayList<Integer> pcArrayList = new ArrayList<Integer>();
		for (int index = 0; index < this.membersArray.length; index++) {
			if (this.membersArray[index] == 1) {
				pcArrayList.add(index);
			}
		}
		return pcArrayList;
	}

	/**
	 * Calculates the signature for an vector where vector element n = 1 if this contains PC n, otherwise, n = 0.
	 * The signature is the sum of 2 ** list[n] for each index in list, where list is an array or integers in the set [0,1].
	 * Each unique set of PCs will map into exactly one signature and the signature can be used as a unique index
	 * of a set of PCs. For example, the transpositionally related PC sets [0,3,6,9] and [1,4,7,A] have different signatures.
	 * @param list  An array to be converted to a signature. See above.
	 * @return  An Integer representing the signature
	 */
	public static Integer getSignature(Integer[] list) {
		Integer signature = 0;
		if (list.length == 0) {
			return 0;
		}
		for ( int index = 0; index < list.length; index++ ) {
			if ( list[ index ] > 0 ) {
				signature += (int) Math.pow( 2.0, index );
			}
		}
		return signature;
	}

	/**
	 * A non-static form of getSignature(). See above.
	 * @return  An Integer representing the signature
	 */
	public Integer getSignature() {
		return PitchClassSet.getSignature(this.membersArray);
	}

	/**
	 * Return a PitchClassSet created from an Integer signature. The @param signature is converted to binary form.
	 * For each element n in the binary representation, if n == 1, signature is incremented by 2 ** n.
	 * @param signature  The signature from the PitchClassSet will be constructed.
	 * @return PitchClassSet  A PitchClassSet created from a signature.
	 */
	public static PitchClassSet getPitchClassSetFromSignature(Integer signature) {
		Integer[] pcArray = new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0};
		//System.out.println( String.format("Calling getPitchClassSetFromSignature() with signature %d", signature ) );
		List<Integer> pcList = new ArrayList<Integer>();
		int temp = signature % 4096;
		int count = 0;
		
		while ( temp > 0 ) {
			int val = temp % 2;
			//System.out.println( String.format("Inside getPitchClassSetFromSignature. pcArray[%d] = %d", count, val) );
			if ( val > 0 ) {
				pcList.add( count );
			}
			count++;
			temp = temp / 2;
		}
		return new PitchClassSet( pcList );
	}

	/**
	 * Returns true if this is equal to another PitchClassSet anotherPcSet. False if not.
	 * Equality is defined as this contains all PCs in anotherPcSet and anotherPcSet contains all elements in this.
	 * @param anotherPcSet  The PitchClassSet used for the comparison.
	 * @return Whether this is equal to anotherPcSet.
	 */
	public boolean equals(PitchClassSet anotherPcSet) {
		/*
		for (int index = 0; index < this.membersArray.length; index++) {
			if (this.membersArray[index] != anotherPcSet.getMembersArray()[index]) {
				return false;
			}
		}
		return true;
		*/
		return this.getMembers().equals( anotherPcSet.getMembers() );
	}

	/**
	 * Returns true if this is less than or greater than another PitchClassSet anotherPcSet.
	 * Equality is defined as this contains all PCs in anotherPcSet and anotherPcSet contains all elements in this.
	 * @param pitchClassSet  The PitchClassSet used for the comparison.
	 * @return Whether this is less than or greater than to anotherPcSet.
	 */
	@Override
	public int compareTo(PitchClassSet pitchClassSet) {
		if ( this.getSignature() < pitchClassSet.getSignature() ) {
			return -1;
		}
		if ( this.getSignature() == pitchClassSet.getSignature() ) {
			return 0;
		}
		return 1;
	}
	/**
	 * Returns an Integer representing the similarity of this to another PitchClassSet anotherPcSet.
	 * Similarity is defined as the cardinality of the largest subset of anotherPcSet that is abstractly included in this.
	 * @param anotherPcSet  The PitchClassSet used for the comparison.
	 * @return The cardinality of the largest subset of anotherPcSet that is abstractly included in this.
	 */
	public int similarity(PitchClassSet anotherPcSet) {
		int similarity = this.size();
		boolean found = false;
		int cardinality = this.size();
		while( cardinality >= 0 ) {
			Iterator<PitchClassSet> subsetIterator = this.subsetIterator( cardinality );
			while ( subsetIterator.hasNext() ) {
				if ( subsetIterator.next().abstractIncludes( anotherPcSet) ) {
					return similarity;
				}
			}
			cardinality--;
		}
		return similarity;
	}

	/**
	 * Returns true if this is equal to another PitchClassSet anotherPcSet. False if not.
	 * Equality is defined as this contains all PCs in anotherPcSet and anotherPcSet contains all elements in this.
	 * @param anotherPcSet  The PitchClassSet used for the comparison.
	 * @return Whether this is equal to anotherPcSet.
	 */
	public boolean equals(IPitchSet anotherPcSet) {
		/*
		for (int index = 0; index < this.membersArray.length; index++) {
			if (this.membersArray[index] != anotherPcSet.getMembersArray()[index]) {
				return false;
			}
		}
		return true;
		*/
		return this.getSignature().equals( new PitchClassSet(anotherPcSet).getSignature() );
	}

	/** 
	 * Returns true if this.getNormalForm() of this equals anotherPcSet.normalForm(). Essentially, this will return whether
	 * set anotherPcSet will map into this under T(n) or I().
	 * @param anotherPcSet  The PitchClassSet used for the comparison.
	 * @return boolean  True if this.getNoralForm() of this equals anotherPcSet.normalForm()
	 */
	public boolean equivalent( PitchClassSet anotherPcSet ) {
		return this.getNormalForm().equals( anotherPcSet.getNormalForm() );
	}

	/** 
	 * Returns true if this contains any transformation of anotherPcSet.
	 * @param anotherPcSet  The PitchClassSet used for the comparison.
	 * @return boolean True if this contains any transformation of anotherPcSet.
	 */
	public boolean abstractIncludes( PitchClassSet anotherPcSet ) {
		/*
		 * A PCSet will always contain the empty set.
		 */
		if ( anotherPcSet.size() == 0) {
			return true;
		}
		Iterator<PitchClassSet> pcSetIt = anotherPcSet.iterator();
		while ( pcSetIt.hasNext() ) {
			/*
			 * If this contains all members of anotherPcSet, their union will equal this.
			 */
			if ( this.union( pcSetIt.next() ).equals( this ) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Two pitch-class sets are z-related if their IC vectors are equal. 6-note sets (hexachords)
	 * will always be z-related to their complement.
	 * @param anotherPcSet  PitchClassSet used for the Z-relation comparison.
	 * @return boolean  True if the IcVectors of this and anotherPcSet are equal.
	 */
	public boolean zRelated( PitchClassSet anotherPcSet ) {
		return Arrays.equals( this.icVector(), anotherPcSet.icVector() );
	}

	// returns true if this set is subset of inPitchClassSet.
	// Another way of saying: The method returns true if inPitchClassSet contains all the members of this set
	/**
	 * Returns true if this is a subset of inPitchClassSet, this is if every member of this is contained in inPitchClassSet.
	 * @param inPitchClassSet  The PitchClassSet used for the comparison.
	 * @return boolean  True if inPitchClassSet contains all members of this, false otherwise.
	 */
	public boolean isSubset(PitchClassSet inPitchClassSet) {
		// fail fast if this is larger than inPitchClassSet, since a set cannot be a subset of
		// a set with a smaller cardinality
		if ( this.size() > inPitchClassSet.size() ) {
			return false;
		}
		for (int i = 0; i < this.membersArray.length; i++) {
			//if ( ! inPitchClassSet.containsPitch( this.membersArray[i] ) ) {
			// the test essentially says that this.membersArray has a bit set where inPitchClassSet.membersArray does not.
			if ( this.membersArray[i] == 1 && inPitchClassSet.membersArray[i] == 0 ) {
				return false;
			}
		}
		return true;
	}

	// returns true if this set is subset of inPitchClassSet.
	// Another way of saying: The method returns true if inPitchClassSet contains all the members of this set
	/**
	 * Return true if this contains all the members of inPitchClassSet.
	 * @param inPitchClassSet  The PitchClassSet used for the comparison.
	 * @return boolean  True if this contains all members of inPitchClassSet, false otherwise.
	 */
	public boolean contains(PitchClassSet inPitchClassSet) {
		// fail fast if this is larger than inPitchClassSet, since a set cannot be a subset of
		// a set with a smaller cardinality
		if ( this.size() < inPitchClassSet.size() ) {
			return false;
		}
		// Iterator<Integer> memberIt = this.members.iterator();
		for ( int i = 0; i < PitchClassSet.MODULUS; i++ ) {
			if ( inPitchClassSet.T( i ).isSubset( this ) || inPitchClassSet.T( i ).I().isSubset( this ) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a PitchClassString constructed from a String containing pitch class tokens, which include single digits [0-9] and the letters 'a','b','A','B',
	 * with A = 10 and B = 11. Any token not included in the set above is silently ignored.
	 * This method can be very useful in reading PCSets from external sources.
	 * @param pcSetString  The String from which the returned PitchClassSet will be constructed.
	 * @return  A new PitchClassSet created from the String pcSetString.
	 */
	public static PitchClassSet getPitchClassSetFromString( String pcSetString ) {
		List<Integer> pcList = new ArrayList<Integer>();
		for ( Character pc : pcSetString.toCharArray() ) {
			if ( pc.toString().matches("[0-9]") ) {
				//System.out.println( "Matches digit: " + pc );
				pcList.add( Integer.parseInt( pc.toString(), 16 ) );
			}
			else if ( pc.toString().equalsIgnoreCase("a") ) {
				//System.out.println( "Matches letter: " + pc );
				pcList.add( Integer.parseInt( pc.toString(), 16 ) );
			}
			else if ( pc.toString().equalsIgnoreCase("b") ) {
				//System.out.println( "Matches letter: " + pc );
				pcList.add( Integer.parseInt( pc.toString(), 16 ) );
			}
			else if ( pc.toString().equalsIgnoreCase("t") ) {
				//System.out.println( "Matches letter: " + pc );
				pcList.add( 10  );
			}
			else if ( pc.toString().equalsIgnoreCase("e") ) {
				//System.out.println( "Matches letter: " + pc );
				pcList.add( 11 );
			}
		}
		//System.out.println( "Contents of pcList: " + pcList.toString() );		
		return new PitchClassSet( pcList );
	}
	
	/**
	 * Return the String representation of this. Currently: given set [0123AB], "[0123AB]".
	 * @return  The String representation of this.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int index = 0; index < this.membersArray.length; index++) {
			if (this.membersArray[index] == 1) {
				sb.append(Integer.toHexString(index).toUpperCase());
				/**
				 * if ( index < this.membersArray.length ) { sb.append(", "); }
				 **/
			}
		}
		sb.append("]");
		if ( this.getDescription() != null && this.getDescription() != "" ) {
			sb.append(" ");
			sb.append( this.getDescription() );
		}
		return sb.toString();
	}

	/**
	 * Return the String representation of this. Currently: given set [0123AB], "PitchSet: [0123AB]".
	 * @return  The String representation of this.
	 */
	public String toStringExtended() {
		StringBuffer sb = new StringBuffer();
		sb.append("PCSet: [");
		for (int index = 0; index < this.membersArray.length; index++) {
			if (this.membersArray[index] == 1) {
				sb.append(Integer.toHexString(index).toUpperCase());
				/**
				 * if ( index < this.membersArray.length ) { sb.append(", "); }
				 **/
			}
		}
		sb.append("]");
		if ( this.getDescription() != null && this.getDescription() != "" ) {
			sb.append(" ");
			sb.append( this.getDescription() );
		}
		return sb.toString();
	}

	/**
	 * A custom mod function that returns useful values for negative numbers.
	 * @param n The integer to which the mod operator will be applied.
	 * @param m The modulus for the operation.
	 * @return An Integer that is the result of n % m.
	 */
	public static Integer mod(Integer n, Integer m) {
		//Integer returnVal = 0;
		if (n >= 0) {
			return n % m;
		} else {
			return ( m + ( n % m ) ) % m;
		}
		//return returnVal;
	}

	/**
	 * Required under the contract of the Iterable inerface. Possibly unused.
	 * @return  Whether there are more elements in the iterator.
	 */
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Required under the contract of the Iterable inerface. Possibly unused.
	 * @return  The next Object in the iterator..
	 */
	public Object next() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Required under the contract of the Iterable inerface. Possibly unused.
	*/
	public void remove() {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns an Iterator that will cycle through all transformation of this, which include T(n) and I().
	 * @return  An Iterator that will iterate through all elements in this.
	 */
	public Iterator<PitchClassSet> iterator() {
		// TODO Auto-generated method stub
		final PitchClassSet thisSet = this;
		Iterator<PitchClassSet> setIt = new Iterator<PitchClassSet>() {

			private int currentIndex = 0;
			private boolean inversionSwitch = false;

			public boolean hasNext() {
				return currentIndex < PitchClassSet.MODULUS;
			}

			public PitchClassSet next() {
				if (!inversionSwitch) {
					inversionSwitch = !inversionSwitch;
					PitchClassSet returnSet = new PitchClassSet(thisSet.T(currentIndex));
					returnSet.setDescription( String.format(" T(%d) (%s)", currentIndex, thisSet.toString() ) );					
					return returnSet;
				} else {
					inversionSwitch = !inversionSwitch;
					PitchClassSet returnSet = new PitchClassSet(thisSet.I().T(PitchClassSet.MODULUS - currentIndex));
					returnSet.setDescription( String.format(" T(%d)I (%s)", currentIndex, thisSet.toString() ) );
					currentIndex++;
					return returnSet;
				}
			}

			public void remove() {

			}
		};
		return setIt;
	}

	/**
	 * Returns an Iterator that will iterate through all 4096 PCSets. Essentially every combination of 12 pcs.
	 * @return Iterator  An Iterator that will iterate through all 4096 possible combinations of 12 PCs.
	 */
	public static Iterator<PitchClassSet> allPitchClassSetsIterator() {
		// TODO Auto-generated method stub
		Iterator<PitchClassSet> setIt = new Iterator<PitchClassSet>() {

			int signature = -1;
			public boolean hasNext() {
				return signature < 4096;
			}

			public PitchClassSet next() {
				signature += 1;
				//System.out.println( String.format("Calling next() with value %d", signature ) ); 
				return PitchClassSet.getPitchClassSetFromSignature(signature);
			}

			public void remove() {

			}
		};
		return setIt;
	}

	/**
	 * Returns a Set of Integer containing the member PCs of this.
	 * @return  Return a Set of the member PCs of this.
	 */
	public List<Integer> getMembers() {
		//return new TreeSet<Integer>( Arrays.asList( this.membersArray ) );
		List<Integer> pcSet = new ArrayList<Integer>();
		for (int index = 0; index < this.membersArray.length; index++) {
			if (this.membersArray[index] == 1) {
				pcSet.add( index );
				/**
				 * if ( index < this.membersArray.length ) { sb.append(", "); }
				 **/
			}
		}
		return pcSet;
	}

	/**
	 * Return the size of this.
	 * @return The size of this.
	 */
	public int size() {
		return this.getMembers().size();
	}

	/**
	 * Returns the name of this PC set. If null, it will look it up using the PitchClassSetCatalog class.
	 * The lookup is computationally expensive, so it is not implemented in the common toString() method and only used optionally.
	 * @return  A String representing the common name of this according to the Rahn PCSet names, such as 8-23.
	 */

	/**
	 * Return the size of this. The length() method is an alias for the size() method. It is added here to
	 * accomodate Groovy script that might use this class, since using size() in Groovy returns the length of an
	 * Object's iterator, where is always 96 for a PitchClassSet. I will eventually refactor this code so the Interator
	 * for PitchClassSet returns an iterator on its elements, not its transformations.
	 * @return The size of this.
	 */
	public int length() {
		return this.getMembers().size();
	}

	/**
	 * Returns the name of this PC set. If null, it will look it up using the PitchClassSetCatalog class.
	 * The lookup is computationally expensive, so it is not implemented in the common toString() method and only used optionally.
	 * @return  A String representing the common name of this according to the Rahn PCSet names, such as 8-23.
	 */

	public String getName() {
		String catalogName = "";
		if ( this.name != null ) {
			return this.name;
		} else {
			return PitchClassSetCatalog.getNameByPitchClassSet(this);
		}
	}
	
	/**
	* Sets the name of the PitchClassSet.
	* @param inName The String name to set.
	*/
	public void setName( String inName ) {
		this.name = inName;
	}

	/**
	 * Returns a String with the description of this. Could be something like "Petroushka CHord" or "Lydian all-interval tetrachord".
	 * @return String  The description of this. Could be something like "Petroushka CHord" or "Lydian all-interval tetrachord".
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Sets the description of this to inDescription.
	 * @param inDescription  The String description to set.
	 */
	public void setDescription( String inDescription ) {
		this.description = inDescription;
	}
	
	/**
	 * Experimental code for setting a modulus other than 12. I'm not interested in pursuing this, but leave it open for extension, etc.
	 * @return  The modulus for this. Currently, it is statically set to 12. Subclasses could be modified to create PitchCLassSets with a modulus other than 12.
	 */
	public int getModulus() {
		return PitchClassSet.MODULUS;
	}

	public void extend(IPitchSet anotherSet) {
		for ( int member : anotherSet.getMembers() ) {
			this.addPitch( member );
		}
	}
}
