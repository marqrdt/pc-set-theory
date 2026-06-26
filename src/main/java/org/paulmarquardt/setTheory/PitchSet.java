package org.paulmarquardt.setTheory;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.paulmarquardt.setTheory.interfaces.IPitchSet;
import org.paulmarquardt.setTheory.interfaces.IPitchCollection;
import org.paulmarquardt.setTheory.interfaces.ISequence;
import org.paulmarquardt.setTheory.PitchClassSet;

/**
 * PitchSet class represents a set of pitches in vertical musical space, i.e. a unordered space that preserves
 * the identity of members across registers. In a PitchSet, the member 60 ("middle C" on MIDI scale) would be considered
 * distinct from a member 72, a note on octave higher in the Western 12-note chromatic scale.
 * The pitches are implemented as Integers. A PitchSet does does implement any sort of horizontal ordering, such that
 * one member would not be considered as coming before or after another member.
 * @author Paul Marquardt
 */
public class PitchSet implements IPitchSet {

	public static final int MODULUS = 12;
	private TreeSet<Integer> members;
	private String name;
	
	/**
	 * The default constructor, creating an empty PitchSet.
	 */
	public PitchSet() {
		this.members = new TreeSet<Integer>();
	}

	/**
	 * Create a PitchSet from a Set of Integers.
	 * @param inSet  The Set of Integer from which this PitchSet will be constructed.
	 */
	public PitchSet(Set<Integer> inSet) {
		// TODO Auto-generated constructor stub
		this.members = new TreeSet<Integer>();
		this.members.addAll(inSet);
		this.setName("");
	}

	/**
	 * Create a PitchSet from a Set of Integers and a String name.
	 * @param inSet  The Set of Integer from which this PitchSet will be constructed.
	 */
	public PitchSet(Set<Integer> inSet, String name) {
		// TODO Auto-generated constructor stub
		this.members = new TreeSet<Integer>();
		this.members.addAll(inSet);
		this.setName(name);
	}

	/**
	 * Constructor creating a PitchSet from an array of Integers.
	 * @param inArray  The Array of Integer from which this PitchSet will be constructed.
	 */
	public PitchSet(Integer[] inArray) {
		// TODO Auto-generated constructor stub
		this.members = new TreeSet<Integer>();
		for (int index = 0; index < inArray.length; index++) {
			this.members.add(inArray[index]);
		}
		this.setName("");
	}

	/**
	 * Create a PitchSet from a List of Integers.
	 * @param inList  The List of Integer from which this PitchSet will be constructed.
	 */
	public PitchSet(List<Integer> inList) {
		// TODO Auto-generated constructor stub
		this.members = new TreeSet<Integer>();
		this.members.addAll(inList);
		this.setName("");
	}

	/**
	 * Constructor creating a PitchSet from an ISequence type.
	 * @param inSeq  This will be created from the members of this ISequence using its getMembers() method.
	 */
	public PitchSet( IPitchCollection inSeq) {
		// TODO Auto-generated constructor stub
		this.members = new TreeSet<Integer>();
		this.members.addAll(inSeq.getMembers());
	}

	/**
	 * Create a PitchSet from a List of Integers.
	 * @param inArray  This will be created from the memebrs of this array of Integer.
	 * @param name  This will be named name.
	 */
	public PitchSet(Integer[] inArray, String name) {
		// TODO Auto-generated constructor stub
		this.members = new TreeSet<Integer>();
		for (int index = 0; index < inArray.length; index++) {
			this.members.add(inArray[index]);
		}
		this.setName(name);
	}

	/**
	 * Constructor creating a PitchSet from an array of Integers and a String name.
	 * @param inList  This will be created from the members of this List of Integer.
	 * @param name  This will be named name.
	 */
	public PitchSet(List<Integer> inList, String name) {
		// TODO Auto-generated constructor stub
		this.members = new TreeSet<Integer>();
		this.members.addAll(inList);
		this.setName(name);
	}

	/**
	 * Constructor creating a PitchSet from an ISequence type.
	 * @param inSeq  This will be created from the members of this ISequence using its getMembers() method.
	 * @param name  This will be named name.
	 */
	public PitchSet( ISequence inSeq, String name) {
		// TODO Auto-generated constructor stub
		this.members = new TreeSet<Integer>();
		this.members.addAll(inSeq.getMembers());
		this.setName(name);
	}

	/**
	 * Constructor creating a PitchSet from a String representation and a String name. The string is searched for numbers,
	 * ignoring all other characters. Examples below will create the PitchSet[12, 31, 52, 46, 25]
	 * "12, 31, 52, 46, 25",
	 * "12 31junk52-46,morejunk 25",
	 * "12 31  52  46  25",
	 * "{12, 31, 52, 46, 25}"
	 * @param seqString  This will be created from the members of this String .
	 * @param name  This will be named name.
	 */
	public PitchSet( String seqString, String name) {
		// TODO Auto-generated constructor stub
		this( PitchSet.getPitchSetFromString(seqString).getMembers(), name);
		//this.setName(name);
	}

	/**
	 * Constructor creating a PitchSet from a String representation and a String name. The string is searched for numbers,
	 * ignoring all other characters. Examples below will create the PitchSet[12, 31, 52, 46, 25]
	 * "12, 31, 52, 46, 25",
	 * "12 31junk52-46,morejunk 25",
	 * "12 31  52  46  25",
	 * "{12, 31, 52, 46, 25}"
	 * @param seqString  This will be created from the members of this ISequence using its getMembers() method.

	 */
	public PitchSet( String seqString) {
		// TODO Auto-generated constructor stub
		this(PitchSet.getPitchSetFromString(seqString).getMembers(), "");
	}

	/**
	 * Constructor creating a PitchClassSet from members of this.
	 * @param objectType  The object returned is created based on the object type using its getMembers() method.
	 * @return Return a PitchClassSet created from the members of this.
	 */
	public PitchClassSet asType( Class objectType ) {
		// TODO Auto-generated constructor stub
		if ( objectType.equals( PitchClassSet.class ) ) {
			return new PitchClassSet( this.getMembers() );
		}
		else {
			throw new IllegalArgumentException( "PitchSet may only be converted to a PitchClassSet");
		}
	}

	/**
	 * The T method on PitchSet returns the PitchSet transposed by the passed int value,
	 * i.e. transposition is added to each member.
	 * @param transposition  The level to which the returned PitchSet will be transposed.
	 * @return PitchSet  The transposed PitchSet.
	 */
	public PitchSet T(int transposition) {
		Iterator<Integer> memberIt = this.members.iterator();
		PitchSet newSet = new PitchSet();
		while (memberIt.hasNext()) {
			Integer pitch = memberIt.next();
			newSet.addPitch((pitch + transposition));
		}
		return newSet;
	}

	/**
	* The I() operator has a slightly different meaning than in PitchClassSet.
	* Here, it will return
	* a PitchSet inverted around its minimum and maximum elements. As a result, the minimum and maximum elements
	* will always be members of the returned set. This makes it more useful
	* compositionally, since the returned set will be in the same range as the
	* original instead of being flipped around 0.
	* @return  A copy of this flipped around its axis so that the highest and lowest pitches remain constant
	*/
	public PitchSet I() {
		Iterator<Integer> memberIt = this.members.iterator();
		PitchSet newSet = new PitchSet();
		// save instead of recalculating -- memory/CPU tradeoff. Memory wins
		// here.
		Integer max = this.maxPitch();
		Integer min = this.minPitch();
		while (memberIt.hasNext()) {
			Integer pitch = memberIt.next();
			newSet.addPitch(max - pitch + min);
		}
		return newSet;
	}

	/**
	 * 
	 */
	public PitchSet M() {
		Iterator<Integer> memberIt = this.members.iterator();
		PitchSet newSet = new PitchSet();
		while (memberIt.hasNext()) {
			Integer pitch = memberIt.next();
			newSet.addPitch((pitch * 5));
		}
		return newSet;
	}

	public PitchSet intersection(PitchSet inPitchSet) {
		Iterator<Integer> memberIt = this.members.iterator();
		PitchSet newSet = new PitchSet();
		while (memberIt.hasNext()) {
			Integer pitch = memberIt.next();
			if (inPitchSet.getMembers().contains(pitch)) {
				newSet.addPitch(pitch);
			}
		}
		return newSet;
	}

	public PitchSet union(PitchSet inPitchSet) {
		Iterator<Integer> memberIt = inPitchSet.members.iterator();
		PitchSet newSet = new PitchSet( this );
		while (memberIt.hasNext()) {
			Integer pitch = memberIt.next();
			newSet.addPitch(pitch);
		}
		return newSet;
	}

	/**
	 * Return an array of the interval class vector of this. The interval class vector is an array of 7 integers, 0 through 6.
	 * The icVector() function for PitchSet differs from that of PitchSet Class in that it contains an entry for 0, which
	 * can be used to enumerate how many octave duplications the set contains, as a Set will never contains pitch duplications.
	 * The value at each index n is the count of interval-class n is contained in this.
	 * @return  The array representing the interval class vector of this.
	 */
	public Integer[] icVector() {
		Integer[] vector = new Integer[(PitchClassSet.MODULUS / 2) + 1];
		for (int index = 0; index < vector.length; index++) {
			vector[index] = 0;
		}
		List<Integer> pitchList = this.getMembers();
		for (int i = 0; i < pitchList.size(); i++) {
			for (int j = i + 1; j < pitchList.size(); j++) {
				int pitch1 = pitchList.get(i) % 12;
				int pitch2 = pitchList.get(j) % 12;
				if (Math.abs(pitch2 - pitch1) >= 0) {
					if (Math.abs(pitch2 - pitch1) >= 6) {
						vector[(MODULUS - Math.abs(pitch2 - pitch1))] += 1;
					} else {
						vector[(Math.abs(pitch2 - pitch1))] += 1;
					}
				}
			}
		}
		return vector;
	}

	/**
	 * Return an array of the pitch class vector of this. The pitch class vector is an array of 12 integers, 0 through 11.
	 * The value at each index n is the count of the pitch-class n is contained in this. Like the icVector function,
	 * it can be used to enumerate the number of octave duplications the set contains, as a Set will never contains pitch duplications.
	 * @return  The array representing the pitch-class class vector of this.
	 */
	public Integer[] pcVector() {
		Integer[] vector = new Integer[PitchClassSet.MODULUS];
		for (int index = 0; index < vector.length; index++) {
			vector[index] = 0;
		}
		for ( int pitch : this.getMembers()) {
			vector[ pitch % PitchClassSet.MODULUS] += 1;
		}
		return vector;
	}

	// A convenience method to return the normal form of this PitchSet
	public PitchClassSet getPitchClassSet() {
		// PitchClassSet normalForm;
		return new PitchClassSet(new ArrayList<Integer>(this.members));
	}

	// A convenience method to return the normal form of this PitchSet
	public PitchClassSet getNormalForm() {
		// PitchClassSet normalForm;
		return new PitchClassSet(new ArrayList<Integer>(this.members))
				.getNormalForm();
	}

	/** return a PitchSet made of Pitches between this.minPitch and this.maxPitch which are not members of this set.
	* @return  A PitchSet containing all the pitches between this.minPitch and this.maxPitch which are not a member of this.
	*/
	public PitchSet complement() {
		PitchSet newSet = new PitchSet();
		// If the set contains one element, we would need a special test to
		// avoid an infinite loop,
		// since i < this.maxPitch would never return true, because minPitch and maxPitch would be
		// the same value for the single-element set and i is set to minPitch + 1.
		// Testing for i < this.maxPitch() + 1 will work in this case, eliminating the need
		// for a special test case. It will only add a single boolean evaluation to the routine.
		for (int i = this.minPitch() + 1; i < this.maxPitch() + 1; i++) {
			if (!this.members.contains(i)) {
				newSet.addPitch(i);
			}
		}
		return newSet;
	}

	public Integer minPitch() {
		return Collections.min(this.members);
	}

	public Integer maxPitch() {
		return Collections.max(this.members);
	}

	/**
	 * Return the distance between the highest and lowest pitches of this.
	 * @return  The distance between the highest and lowest pitches of this.
	 */
	public int range() {
		if ( this.getMembers().size() < 1 ) {
			return 0;
		}
		return this.maxPitch() - this.minPitch();
	}

	public void addPitch(Integer pitchToAdd) {
		// return this.members.add( pitchToAdd );
		this.members.add(pitchToAdd);
	}

	public void addPitch(String pitch) {
		//boolean retVal = false;
		try {
			/*
			 * if ( pitch.equalsIgnoreCase("a") ) { retVal = this.members.add(
			 * 10 ); this.membersArray[ 10 ] = 1; } else if (
			 * pitch.equalsIgnoreCase("b") ) { retVal = this.members.add( 11 );
			 * this.membersArray[ 11 ] = 1; } else {
			 */
			this.members.add(Integer.decode(pitch));
			//retVal = this.members.add(Integer.decode(pitch));
			// }
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// ("Illegal pitch name: " + pitch);
		}
		// return retVal;
	}
	
	public boolean containsPitch(Integer member) {
		// TODO Auto-generated method stub
		return this.members.contains(member);
	}

	public List<Integer> getMembers() {
		return new ArrayList<Integer>( this.members );
	}

	public int size() {
		return this.members.size();
	}

	public boolean equals(PitchSet anotherSet) {
		return this.getMembers().equals(anotherSet.getMembers());
	}

	public List<Integer> getAscendingIntervals() {
		List<Integer> intervals = new ArrayList<Integer>();
		List<Integer> members = new ArrayList<Integer>( this.members );
		Collections.sort(members);
		for ( int i = 0; i < members.size() - 1; i++ ) {
			intervals.add( members.get( i + 1 ) - members.get( i ) );
		}
		return intervals;
	}

	/**
	 * Construct a PitchSequence from a String containing Integer tokens separated by a comma and a space ", ".
	 * Example: 12, 43, 73, 9, 34, 2, 121. Extraneous characters are ignored. Elements in the string are converted
	 * to pitch-classes using n % 12 for each n in the member string.
	 * @param pSeqString  String containing tokens to parse.
	 * @return PitchSequence constructed from String of tokens representing pitches as Integers.
	 */
	public static PitchSet getPitchSetFromString( String pSeqString ) {
		Set<Integer> pcSet = new TreeSet();
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(pSeqString);
		while(m.find()) {
			pcSet.add( Integer.parseInt(m.group()));
		}
		return new PitchSet(pcSet);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("PitchSet: [");
		Iterator<Integer> memberIt = this.members.iterator();
		//PitchSet newSet = new PitchSet();
		int count = 0;
		while (memberIt.hasNext()) {
			sb.append(memberIt.next());
			if ( count + 1 < this.members.size() ) {
				sb.append(",");
			}
			count++;
		}
		sb.append("]");
		return sb.toString();
	}

	public boolean equals(IPitchSet anotherSet) {
		// TODO Auto-generated method stub
		boolean retValue = true;
		if (this.size() != anotherSet.size()) {
			return false;
		}
		for (Integer member : this.getMembers()) {
			if (!anotherSet.getMembers().contains(member)) {
				return false;
			}
		}
		return retValue;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName( String inName ) {
		this.name = inName;
	}

	public void extend( IPitchSet pSet ) {
		this.members.addAll( pSet.getMembers() );
	}	
}
