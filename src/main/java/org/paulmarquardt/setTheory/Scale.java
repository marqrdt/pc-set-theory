package org.paulmarquardt.setTheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.paulmarquardt.setTheory.interfaces.IPitchSet;
import org.paulmarquardt.setTheory.interfaces.IPitchCollection;


/**
 * The Scale class extends the PitchSet class by offering a different set of constructors that use a set
 * of intervals rather than the set of pitchclass members. Though the superclass is PitchSet, the Scale
 * extends the PitchSet model to include the concept of order, which is intentionally omitted from the
 * PitchSet. It can be considered a cross between the PitchSet and PitchSequence; however, in the Scale,
 * its members are arranged in ascending order. Several utility methods offer
 * the capability to return PitchSequences based on slices of the scale.
 * Several common scales can be constructed using a String constructor instead of an array of intervals.
 * @author Paul Marquardt
 */
public class Scale extends PitchSequence {

	public static final int MODULUS = 12;
	// A reasonable limit to the max number of members a scale can have.
	// Such a large number could be useful for searching for patterns, etc.
	public static final int MEMBERMAX = 4096;
	private List<Integer> members;
	private List<Integer> intervals;	
	private String name;
	public static Integer[] MAJOR = new Integer[] {2,2,1,2,2,2,1};
	public static Integer[] NATURAL_MINOR = new Integer[] {2,1,2,2,1,2,2};
	public static Integer[] HARMONIC_MINOR = new Integer[] {2,1,2,2,1,2,2};
	public static Integer[] HARMONIC = new Integer[] {2,2,2,1,2,1,2};
	public static Integer[] WHOLETONE = new Integer[] {2,2,2,2,2,2};
	public static Integer[] CHROMATIC = new Integer[] {1,1,1,1,1,1,1,1,1,1,1,1};
	public static Integer[] OCTATONIC = new Integer[] {2,1,2,1,2,1,2,1};
	public static Integer[] MESSIAEN1 = new Integer[] {2,2,2,2,2,2};
	public static Integer[] MESSIAEN2 = new Integer[] {2,1,2,1,2,1,2,1};
	public static Integer[] MESSIAEN3 = new Integer[] {2,1,1,2,1,1,2,1,1};
	public static Integer[] MESSIAEN4 = new Integer[] {1,1,3,1,1,1,3,1};
	public static Integer[] MESSIAEN5 = new Integer[] {1,4,1,1,4,1};
	public static Integer[] MESSIAEN6 = new Integer[] {2,2,1,1,2,2,1,1};
	public static Integer[] MESSIAEN7 = new Integer[] {1,1,1,2,1,1,1,1,2,1};
	public static Integer[] IONIAN = Scale.MAJOR;
	public static Integer[] DORIAN = new Integer[] {2,1,2,2,2,1,2};
	public static Integer[] PHRYGIAN = new Integer[] {1,2,2,2,1,2,2};
	public static Integer[] LYDIAN = new Integer[] {2,2,2,1,2,2,1};
	public static Integer[] MIXOLYDIAN = new Integer[] {2,2,1,2,2,1,2};
	public static Integer[] AEOLIAN = new Integer[] {2,1,2,2,1,2,2};
	public static Integer[] LOCRIAN = new Integer[] {1,2,2,1,2,2,2};
	
	/**
	 * The default constructor, creating an empty Scale.
	 */
	public Scale() {
		this.members = new ArrayList<Integer>();
	}

	/**
	 * Constructor creating a Scale from an array of Integers representing intervals between members.
	 * @param inArray  The Array of Integer from which this PitchSet will be constructed.
	 * @param lowestNote  The lowest note of the scale.
	 * @param highestNote  The highest note of the scale.
	 */
	public Scale(Integer[] inArray, int lowestNote, int highestNote ) {
		// TODO Auto-generated constructor stub
		super();
		this.intervals = Arrays.asList(inArray);
		this.members = new ArrayList<Integer>();
		if ( lowestNote > highestNote ) {
			throw new IllegalArgumentException("The value of lowestNote must be less than the value of highestNote.");
		}
		for ( int interval : inArray ) {
			if ( interval < 1 ) {
				throw new IllegalArgumentException("A Scale cannot have intervals less than 1.");
			}
		}
		int count = 0;
		int noteToAdd = lowestNote;
		this.members.add( noteToAdd );
		if ( inArray.length == 0 ) {
			this.setName("");
			return;
		}
		while ( noteToAdd < highestNote ) {
			noteToAdd = noteToAdd + inArray[count % inArray.length]; 
			this.members.add(noteToAdd);
			count++;
		}
		// Set the ascending List to the set of members.
		this.setName("");
	}

	/**
	 * Constructor creating a Scale from an array of Integers representing intervals between members.
	 * This will create a Scale with members from startingNote to and including Scale.MEMBERMAX.
	 * This constructor is useful if the application does not want to specify a highest element,
	 * as the default max element should be sufficient for most applications.
	 * @param inArray  The Array of Integer from which this PitchSet will be constructed.
	 * @param startingNote  The lowest note of the scale.
	 */
	public Scale(Integer[] inArray, int startingNote ) {
		// TODO Auto-generated constructor stub
		super();
		this.intervals = Arrays.asList(inArray);
		this.members = new ArrayList<Integer>();
		//int maxInterval = 0;
		for ( int interval : inArray ) {
			if ( interval < 1 ) {
				throw new IllegalArgumentException("A Scale cannot have intervals less than 1.");
			}
			//maxInterval = Integer.max(interval, maxInterval);
		}
		int noteToAdd = startingNote;
		this.members.add( noteToAdd );
		if ( inArray.length == 0 ) {
			this.setName("");
			return;
		}
		int count = 0;
		while ( noteToAdd <= Scale.MEMBERMAX ) {
			noteToAdd = noteToAdd + inArray[count % inArray.length]; 
			this.members.add(noteToAdd);
			count++;
		}
		// Set the ascending List to the set of members.
		this.setName("");
	}

	/**
	 * Constructor creating a Scale from an array of Integers representing intervals between members.
	 * @param inList  The List of Integer from which this PitchSet will be constructed.
	 * @param lowestNote  The lowest note of the scale.
	 * @param highestNote  The highest note of the scale.
	 */
	public Scale(List<Integer> inList, int lowestNote, int highestNote ) {
		// TODO Auto-generated constructor stub
		super();
		this.intervals = inList;
		this.members = new ArrayList<Integer>();
		if ( lowestNote > highestNote ) {
			throw new IllegalArgumentException("The value of lowestNote must be less than the value of highestNote.");
		}
		for ( int interval : inList ) {
			if ( interval < 1 ) {
				throw new IllegalArgumentException("A Scale cannot have intervals less than 1.");
			}
		}
		int count = 0;
		int noteToAdd = lowestNote;
		this.members.add( noteToAdd );
		if ( inList.size() == 0 ) {
			this.setName("");
			return;
		}
		while ( noteToAdd < highestNote ) {
			noteToAdd = noteToAdd + inList.get(count); 
			this.members.add(noteToAdd);
			count++;
			if ( count == inList.size() ) {
				count = 0;
			}
		}
		// Set the ascending List to the set of members.
		this.setName("");
	}

	/**
	 * Constructor creating a Scale from an array of Integers representing intervals between members.
	 * @param inList  The List of Integer from which this PitchSet will be constructed.
	 * @param startingNote  The lowest note of the scale.
	 */
	public Scale(List<Integer> inList, int startingNote ) {
		// TODO Auto-generated constructor stub
		super();
		this.intervals = inList;
		this.members = new ArrayList<Integer>();
		for ( int interval : inList ) {
			if ( interval < 1 ) {
				throw new IllegalArgumentException("A Scale cannot have intervals less than 1.");
			}
		}
		int count = 0;
		int noteToAdd = startingNote;
		this.members.add( noteToAdd );
		if ( inList.size() == 0 ) {
			this.setName("");
			return;
		}
		while ( noteToAdd <= Scale.MEMBERMAX ) {
			noteToAdd = noteToAdd + inList.get(count); 
			this.members.add(noteToAdd);
			count++;
			if ( count == inList.size() ) {
				count = 0;
			}
		}
		// Set the ascending List to the set of members.
		this.setName("");
	}

	/**
	 * Constructor creating a Scale from an existing PitchCollection. The members of the IPitchCollection
	 * will be sorted before being added to guarantee the elements of the underlying members List are
	 * in ascending order.
	 * @param pSet  The PitchSet from which this Scale will be constructed.
	 */
	public Scale( IPitchCollection pSet ) {
		this.intervals = new ArrayList<Integer>();
		this.members = new ArrayList<Integer>();
		//Set<Integer> memberSet= new TreeSet<Integer>( pSet.getMembers() );
		List<Integer> scaleMembers = new ArrayList<Integer>( pSet.getMembers() );
		Collections.sort( scaleMembers );
		this.members.addAll( scaleMembers );
		for ( int i = 1; i < this.members.size(); i++ ) {
			this.intervals.add( this.members.get( i ) - this.members.get( i - 1 ));
		}
	}

	/**
	 * Returns this Scale transposed by transposition. The transposition parameter may be negative.
	 * @param transposition  Return the pitch number at this scale degree.
	 * @return  An Scale created from this transposed by transposition.
	 */
	public Scale T( int transposition ) {
		List<Integer> scaleMembers = new ArrayList<Integer>();
		for ( int member : this.getMembers() ) {
			scaleMembers.add( member + transposition );
		}
		return new Scale( new PitchSet( scaleMembers ) );
	}

	/**
	 * @param degree  Return the pitch number at this scale degree.
	 * @return  An integer representing the pitch number of this scale degree.
	 */
	Integer getMemberAtDegree( int degree ) {
		if ( degree >= this.members.size() ) {
			throw new IllegalArgumentException("The degree must be less than the number of elements.");
		}
		return 0;
	}
	
	/**
	 * Return a PitchSet with members form this at degrees supplied in the parameter degrees. For example,
	 * if this was a one-octave C Major Scale with members &lt;24, 26, 28, 29, 31, 33, 35, 36&gt;, 
	 * and degrees was the list [0, 2, 4], getPitchSetAtDegrees(degrees) would return the
	 * PitchSet &lt;24, 28, 31&gt;, or a C Major triad.
	 * @param  degrees List&lt;Integer&gt; of degrees from which to construct the PitchSet
	 * @return  An PitchSet with containing pitches from this at the indices supplied in degrees.
	 */
	
	PitchSet getPitchSetAtDegrees( List<Integer> degrees ) {
		PitchSet outSet = new PitchSet();
		for ( Integer member : degrees ) {
			if ( member > this.getMembers().size() || member < 0 ) {
				throw new IllegalArgumentException(String.format("Illegal degree at %d", member) );
			}
			outSet.addPitch( this.getMembers().get(member) );
		}
		return outSet;
	}

	/**
	 * Return a PitchSet with members from loweestNote to highestNote.
	 * Parameter lowestNote will always be the first note in the returned PitchSet.
	 * If highestNote is not a member of the Scale, the largest member of the returned PitchSet 
	 * will be the last member of the Scale that is less than highestNote.
	 * @param  lowestNote The bottom note on which to start the Scale.
	 * @param  highestNote The largest member of the returned PitchSet will either contain 
	 * 	highestNote or be less than highestNote.
	 * @return  A PitchSet representing the range described by the lowestNote and highestNote parameters .
	 */
	
	PitchSet getPitchSetWithRange( int lowestNote, int highestNote ) {
		if ( lowestNote > highestNote ) {
			throw new IllegalArgumentException( String.format("Parameter lowestNote %d may not be greater than highestNote %d", lowestNote, highestNote));
		}
		PitchSet outSet = new PitchSet();
		// If lowestNote is less than or equals highestNote, it will always contain lowestNote.
		outSet.addPitch(lowestNote);
		int currentNote = lowestNote;
		int scaleIndex = 0;
		while ( currentNote <= highestNote ) {
			scaleIndex = (scaleIndex + 1 ) % this.getMembers().size();
			if ( currentNote + this.getMembers().get(scaleIndex) <= highestNote ) {
				outSet.addPitch( currentNote + this.getMembers().get(scaleIndex) ); 
			}
			currentNote += this.getMemberAtDegree(scaleIndex);
		}
		return outSet;
	}

	/**
	 * Return a PitchSet with members from loweestNote to highestNote.
	 * Parameter lowestNote will always be the first note in the returned PitchSet.
	 * If highestNote is not a member of the Scale, the largest member of the returned PitchSet 
	 * will be the last member of the Scale that is less than highestNote.
	 * @param  startingDegree The degree of the Scale on which to start.
	 * @param  lowestNote The bottom note on which to start the Scale.
	 * @param  highestNote The largest member of the returned PitchSet will either contain 
	 * 	highestNote or be less than highestNote.
	 * @return  A PitchSet representing the range described by the lowestNote and highestNote parameters .
	 */
	
	PitchSet getPitchSetWithRange( int lowestNote, int highestNote, int startingDegree ) {
		if ( lowestNote > highestNote ) {
			throw new IllegalArgumentException( String.format("Parameter lowestNote %d may not be greater than highestNote %d", lowestNote, highestNote));
		}
		PitchSet outSet = new PitchSet();
		// If lowestNote is less than or equals highestNote, it will always contain lowestNote.
		outSet.addPitch(lowestNote);
		int currentNote = lowestNote;
		int scaleIndex = startingDegree;
		while ( currentNote <= highestNote ) {
			scaleIndex = (scaleIndex + 1 ) % this.getMembers().size();
			if ( currentNote + this.getMembers().get(scaleIndex) <= highestNote ) {
				outSet.addPitch( currentNote + this.getMembers().get(scaleIndex) ); 
			}
			currentNote += this.getMemberAtDegree(scaleIndex);
		}
		return outSet;
	}

	/**
	 * Return a Scale whose members consist of the intersection of this and an IPitchCollection.
	 * @param  pSeq The IPitchCollection to be used to form the intersection.
	 * @return A Scale containing the logical intersection of this and an IPitchCollection.
	 */
	
	Scale intersection( IPitchCollection pSet ) {
		List<Integer> intersection = new ArrayList<Integer>();
		for ( Integer member : pSet.getMembers() ) {
			if ( this.getMembers().contains( member ) ) {
				intersection.add( member );
			}
		}
		return new Scale( new PitchSet( intersection ) ); 
	}

	/**
	 * Return a Scale whose members consist of the logical union of this and an IPitchCollection.
	 * @param  pSeq The IPitchCollection to be used to form the intersection.
	 * @return A Scale containing the logical union of this and an IPitchCollection.
	 */
	
	Scale union( IPitchCollection pSet ) {
		List<Integer> union = this.getMembers();
		for ( Integer member : pSet.getMembers() ) {
			union.add( member );
		}
		return new Scale( new PitchSet( union ) ); 
	}

	/**
	 * Return a Scale formed from the logical exclusive or (XOR) of this and an IPitchCollection.
	 * @param  pSeq The IPitchCollection to be used to form the logical exclusive or.
	 * @return A Scale containing the logical logical exclusive or (XOR) of this and an IPitchCollection.
	 */
	
	Scale xor( IPitchCollection pSet ) {
		Set<Integer> xorSet = new TreeSet<Integer>( this.getMembers() );
		xorSet.addAll( pSet.getMembers() );
		List<Integer> outSet = new ArrayList<Integer>();
		for ( Integer member : xorSet ) {
			if ( !( pSet.getMembers().contains( member ) && this.getMembers().contains( member ) ) ) {
				outSet.add( member );
			}
		}
		return new Scale( new PitchSet( outSet ) ); 
	}

	/**
	 * Return a Scale formed from the logical exclusive or (XOR) of this and an IPitchCollection.
	 * @param  pSeq The IPitchCollection to be used to form the logical exclusive or.
	 * @return A Scale containing the logical logical exclusive or (XOR) of this and an IPitchCollection.
	 */
	
	Scale complement( IPitchCollection pSet ) {
		List<Integer> outSet = new ArrayList<Integer>();
		for ( int i = this.minPitch(); i <= this.maxPitch(); i++ ) {
			if ( ! this.getMembers().contains( i ) ) {
				outSet.add( i );
			}
		}
		return new Scale( new PitchSet( outSet ) ); 
	}

	/**
	 * @return  The String representation of this.
	 */
	public String toString() {
		StringBuffer stringBuf = new StringBuffer();
		if ( this.name != null ) {
			stringBuf.append( this.getName() + " ");
		}
		stringBuf.append( "Scale: [ ");
		int count = 0;
		for ( int member : this.getMembers() ) {
			stringBuf.append( String.format("%d", member) );
			if ( count < this.getMembers().size() - 1) {
				stringBuf.append(", ");
			}
			count++;
		}
		stringBuf.append(" ]");
		return stringBuf.toString();
	}
	
	/**
	 * 
	 * @return  A List of Integer containing the members of this.
	 */
	public List<Integer> getMembers() {
		return this.members;
	}
	
	/**
	 * 
	 * @return  A List of Integer containing the members of this.
	 */
	public List<Integer> getPattern() {
		return this.intervals;
	}

	/**
	 * 
	 * @param name  Set the Scale's name.
	 */
	public void setName( String name ) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return  Return the Scale's name.
	 */
	public String getName() {
		return this.name;
	}

	public boolean isScaleLike() {
		boolean scaleLike = false;
		if ( this.intervals.size() < 2) {
			return false;
		}


		return scaleLike;
	}

}
