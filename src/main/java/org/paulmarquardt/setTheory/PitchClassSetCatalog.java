package org.paulmarquardt.setTheory;

import java.util.*;
import org.apache.commons.lang.*;
import org.paulmarquardt.setTheory.interfaces.*;

public class PitchClassSetCatalog {

	public static final int RAHN = 0;
	public static final int FORTE = 1;
	
	/**
	 * Default to John Rahn's list, which prefers the larger number to be smaller, making the calculation of the
	 * base sets match their representation as binary digits.
	 * @return A Map of String,PitchClassSet with set names as keys and PitchClassSets as values.
	 */
	public static Map<String,PitchClassSet> allPcSets() {
		return allPcSets( PitchClassSetCatalog.RAHN );
	}
	
	/**
	 * The lists produced by John Rahn and Alan Forte differ in the normal forms of the five sets:
	 * Set       Forte Prime    Rahn Prime
	 * 5-20
	 * 6-29
	 * 6-31
	 * 7-20
	 * 8-26
	 * @param flavor The "flavor" of sets to return. Use 0 for PC Sets according to Forte, any other value to return the sets according to Rahn.
	 * This only affect the values for 5-20, 6-29, 6-Z29, 6-31, 7-20 and 8-26.
	 * @return A Map of &lt;String,Integer&gt; with PC Set names as keys and PitchClassSets as values.
	 */
	public static Map<String,PitchClassSet> allPcSets( int flavor ) {
		Map<String,PitchClassSet> pcSetCatalog = new HashMap<String,PitchClassSet>();
		pcSetCatalog.put ( "0-1", new PitchClassSet( "", "0-1" ) );
		pcSetCatalog.put ( "1-1", new PitchClassSet( "0", "1-1" ) );
		pcSetCatalog.put ( "2-1", new PitchClassSet( "01", "2-1" ) );
		pcSetCatalog.put ( "2-2", new PitchClassSet( "02", "2-2" ) );
		pcSetCatalog.put ( "2-3", new PitchClassSet( "03", "2-3" ) );
		pcSetCatalog.put ( "2-4", new PitchClassSet( "04", "2-4" ) );
		pcSetCatalog.put ( "2-5", new PitchClassSet( "05", "2-5" ) );
		pcSetCatalog.put ( "2-6", new PitchClassSet( "06", "2-6" ) );
		pcSetCatalog.put ( "3-1", new PitchClassSet( "012", "3-1" ) );
		pcSetCatalog.put ( "3-2", new PitchClassSet( "013", "3-2" ) );
		pcSetCatalog.put ( "3-3", new PitchClassSet( "014", "3-3" ) );
		pcSetCatalog.put ( "3-4", new PitchClassSet( "015", "3-4" ) );
		pcSetCatalog.put ( "3-5", new PitchClassSet( "016", "3-5" ) );
		pcSetCatalog.put ( "3-6", new PitchClassSet( "024", "3-6" ) );
		pcSetCatalog.put ( "3-7", new PitchClassSet( "025", "3-7" ) );
		pcSetCatalog.put ( "3-8", new PitchClassSet( "026", "3-8" ) );
		pcSetCatalog.put ( "3-9", new PitchClassSet( "027", "3-9" ) );
		pcSetCatalog.put ( "3-10", new PitchClassSet( "036", "3-10" ) );
		pcSetCatalog.put ( "3-11", new PitchClassSet( "037", "3-11" ) );
		pcSetCatalog.put ( "3-12", new PitchClassSet( "048", "3-12" ) );
		pcSetCatalog.put ( "4-1", new PitchClassSet( "0123", "4-1" ) );
		pcSetCatalog.put ( "4-2", new PitchClassSet( "0124", "4-2" ) );
		pcSetCatalog.put ( "4-3", new PitchClassSet( "0134", "4-3" ) );
		pcSetCatalog.put ( "4-4", new PitchClassSet( "0125", "4-4" ) );
		pcSetCatalog.put ( "4-5", new PitchClassSet( "0126", "4-5" ) );
		pcSetCatalog.put ( "4-6", new PitchClassSet( "0127", "4-6" ) );
		pcSetCatalog.put ( "4-7", new PitchClassSet( "0145", "4-7" ) );
		pcSetCatalog.put ( "4-8", new PitchClassSet( "0156", "4-8" ) );
		pcSetCatalog.put ( "4-9", new PitchClassSet( "0167", "4-9" ) );
		pcSetCatalog.put ( "4-10", new PitchClassSet( "0235", "4-10" ) );
		pcSetCatalog.put ( "4-11", new PitchClassSet( "0135", "4-11" ) );
		pcSetCatalog.put ( "4-12", new PitchClassSet( "0236", "4-12" ) );
		pcSetCatalog.put ( "4-13", new PitchClassSet( "0136", "4-13" ) );
		pcSetCatalog.put ( "4-14", new PitchClassSet( "0237", "4-14" ) );
		pcSetCatalog.put ( "4-Z15", new PitchClassSet( "0146", "4-Z15" ) );
		pcSetCatalog.put ( "4-16", new PitchClassSet( "0157", "4-16" ) );
		pcSetCatalog.put ( "4-17", new PitchClassSet( "0347", "4-17" ) );
		pcSetCatalog.put ( "4-18", new PitchClassSet( "0147", "4-18" ) );
		pcSetCatalog.put ( "4-19", new PitchClassSet( "0148", "4-19" ) );
		pcSetCatalog.put ( "4-20", new PitchClassSet( "0158", "4-20" ) );
		pcSetCatalog.put ( "4-21", new PitchClassSet( "0246", "4-21" ) );
		pcSetCatalog.put ( "4-22", new PitchClassSet( "0247", "4-22" ) );
		pcSetCatalog.put ( "4-23", new PitchClassSet( "0257", "4-23" ) );
		pcSetCatalog.put ( "4-24", new PitchClassSet( "0248", "4-24" ) );
		pcSetCatalog.put ( "4-25", new PitchClassSet( "0268", "4-25" ) );
		pcSetCatalog.put ( "4-26", new PitchClassSet( "0358", "4-26" ) );
		pcSetCatalog.put ( "4-27", new PitchClassSet( "0258", "4-27" ) );
		pcSetCatalog.put ( "4-28", new PitchClassSet( "0369", "4-28" ) );
		pcSetCatalog.put ( "4-Z29", new PitchClassSet( "0137", "4-Z29" ) );
		pcSetCatalog.put ( "5-1", new PitchClassSet( "01234", "5-1" ) );
		pcSetCatalog.put ( "5-2", new PitchClassSet( "01235", "5-2" ) );
		pcSetCatalog.put ( "5-3", new PitchClassSet( "01245", "5-3" ) );
		pcSetCatalog.put ( "5-4", new PitchClassSet( "01236", "5-4" ) );
		pcSetCatalog.put ( "5-5", new PitchClassSet( "01237", "5-5" ) );
		pcSetCatalog.put ( "5-6", new PitchClassSet( "01256", "5-6" ) );
		pcSetCatalog.put ( "5-7", new PitchClassSet( "01267", "5-7" ) );
		pcSetCatalog.put ( "5-8", new PitchClassSet( "02346", "5-8" ) );
		pcSetCatalog.put ( "5-9", new PitchClassSet( "01246", "5-9" ) );
		pcSetCatalog.put ( "5-10", new PitchClassSet( "01346", "5-10" ) );
		pcSetCatalog.put ( "5-11", new PitchClassSet( "02347", "5-11" ) );
		pcSetCatalog.put ( "5-Z12", new PitchClassSet( "01356", "5-Z12" ) );
		pcSetCatalog.put ( "5-13", new PitchClassSet( "01248", "5-13" ) );
		pcSetCatalog.put ( "5-14", new PitchClassSet( "01257", "5-14" ) );
		pcSetCatalog.put ( "5-15", new PitchClassSet( "01268", "5-15" ) );
		pcSetCatalog.put ( "5-16", new PitchClassSet( "01347", "5-16" ) );
		pcSetCatalog.put ( "5-Z17", new PitchClassSet( "01348", "5-Z17" ) );
		pcSetCatalog.put ( "5-Z18", new PitchClassSet( "01457", "5-Z18" ) );
		pcSetCatalog.put ( "5-19", new PitchClassSet( "01367", "5-19" ) );
		if ( flavor == PitchClassSetCatalog.FORTE ) {
			pcSetCatalog.put ( "5-20", new PitchClassSet( "01378", "5-20" ) );
		}
		else {
			pcSetCatalog.put ( "5-20", new PitchClassSet( "01568", "5-20" ) );
		}
		pcSetCatalog.put ( "5-21", new PitchClassSet( "01458", "5-21" ) );
		pcSetCatalog.put ( "5-22", new PitchClassSet( "01478", "5-22" ) );
		pcSetCatalog.put ( "5-23", new PitchClassSet( "02357", "5-23" ) );
		pcSetCatalog.put ( "5-24", new PitchClassSet( "01357", "5-24" ) );
		pcSetCatalog.put ( "5-25", new PitchClassSet( "02358", "5-25" ) );
		pcSetCatalog.put ( "5-26", new PitchClassSet( "02458", "5-26" ) );
		pcSetCatalog.put ( "5-27", new PitchClassSet( "01358", "5-27" ) );
		pcSetCatalog.put ( "5-28", new PitchClassSet( "02368", "5-28" ) );
		pcSetCatalog.put ( "5-29", new PitchClassSet( "01368", "5-29" ) );
		pcSetCatalog.put ( "5-30", new PitchClassSet( "01468", "5-30" ) );
		pcSetCatalog.put ( "5-31", new PitchClassSet( "01369", "5-31" ) );
		pcSetCatalog.put ( "5-32", new PitchClassSet( "01469", "5-32" ) );
		pcSetCatalog.put ( "5-33", new PitchClassSet( "02468", "5-33" ) );
		pcSetCatalog.put ( "5-34", new PitchClassSet( "02469", "5-34" ) );
		pcSetCatalog.put ( "5-35", new PitchClassSet( "02479", "5-35" ) );
		pcSetCatalog.put ( "5-Z36", new PitchClassSet( "01247", "5-Z36" ) );
		pcSetCatalog.put ( "5-Z37", new PitchClassSet( "03458", "5-Z37" ) );
		pcSetCatalog.put ( "5-Z38", new PitchClassSet( "01258", "5-Z38" ) );
		pcSetCatalog.put ( "6-1", new PitchClassSet( "012345", "6-1" ) );
		pcSetCatalog.put ( "6-2", new PitchClassSet( "012346", "6-2" ) );
		pcSetCatalog.put ( "6-Z3", new PitchClassSet( "012356", "6-Z3" ) );
		pcSetCatalog.put ( "6-Z4", new PitchClassSet( "012456", "6-Z4" ) );
		pcSetCatalog.put ( "6-5", new PitchClassSet( "012367", "6-5" ) );
		pcSetCatalog.put ( "6-Z6", new PitchClassSet( "012567", "6-Z6" ) );
		pcSetCatalog.put ( "6-7", new PitchClassSet( "012678", "6-7" ) );
		pcSetCatalog.put ( "6-8", new PitchClassSet( "023457", "6-8" ) );
		pcSetCatalog.put ( "6-9", new PitchClassSet( "012357", "6-9" ) );
		pcSetCatalog.put ( "6-Z10", new PitchClassSet( "013457", "6-Z10" ) );
		pcSetCatalog.put ( "6-Z11", new PitchClassSet( "012457", "6-Z11" ) );
		pcSetCatalog.put ( "6-Z12", new PitchClassSet( "012467", "6-Z12" ) );
		pcSetCatalog.put ( "6-Z13", new PitchClassSet( "013467", "6-Z13" ) );
		pcSetCatalog.put ( "6-14", new PitchClassSet( "013458", "6-14" ) );
		pcSetCatalog.put ( "6-15", new PitchClassSet( "012458", "6-15" ) );
		pcSetCatalog.put ( "6-16", new PitchClassSet( "014568", "6-16" ) );
		pcSetCatalog.put ( "6-Z17", new PitchClassSet( "012478", "6-Z17" ) );
		pcSetCatalog.put ( "6-18", new PitchClassSet( "012578", "6-18" ) );
		pcSetCatalog.put ( "6-Z19", new PitchClassSet( "013478", "6-Z19" ) );
		pcSetCatalog.put ( "6-20", new PitchClassSet( "014589", "6-20" ) );
		pcSetCatalog.put ( "6-21", new PitchClassSet( "023468", "6-21" ) );
		pcSetCatalog.put ( "6-22", new PitchClassSet( "012468", "6-22" ) );
		pcSetCatalog.put ( "6-Z23", new PitchClassSet( "023568", "6-Z23" ) );
		pcSetCatalog.put ( "6-Z24", new PitchClassSet( "013468", "6-Z24" ) );
		pcSetCatalog.put ( "6-Z25", new PitchClassSet( "013568", "6-Z25" ) );
		pcSetCatalog.put ( "6-Z26", new PitchClassSet( "013578", "6-Z26" ) );
		pcSetCatalog.put ( "6-27", new PitchClassSet( "013469", "6-27" ) );
		pcSetCatalog.put ( "6-Z28", new PitchClassSet( "013569", "6-Z28" ) );
		if ( flavor == PitchClassSetCatalog.FORTE ) {
			pcSetCatalog.put ( "6-Z29", new PitchClassSet( "013689", "6-Z29" ) );
		}
		else {
			pcSetCatalog.put ( "6-Z29", new PitchClassSet( "023679", "6-Z29" ) );
		}
		pcSetCatalog.put ( "6-30", new PitchClassSet( "013679", "6-30" ) );
		if ( flavor == PitchClassSetCatalog.FORTE ) {
			pcSetCatalog.put ( "6-31", new PitchClassSet( "013589", "6-31" ) );
		}
		else {
			pcSetCatalog.put ( "6-31", new PitchClassSet( "014579", "6-31" ) );
		}
		pcSetCatalog.put ( "6-32", new PitchClassSet( "024579", "6-32" ) );
		pcSetCatalog.put ( "6-33", new PitchClassSet( "023579", "6-33" ) );
		pcSetCatalog.put ( "6-34", new PitchClassSet( "013579", "6-34" ) );
		pcSetCatalog.put ( "6-35", new PitchClassSet( "02468A", "6-35" ) );
		pcSetCatalog.put ( "6-Z36", new PitchClassSet( "012347", "6-Z36" ) );
		pcSetCatalog.put ( "6-Z37", new PitchClassSet( "012348", "6-Z37" ) );
		pcSetCatalog.put ( "6-Z38", new PitchClassSet( "012378", "6-Z38" ) );
		pcSetCatalog.put ( "6-Z39", new PitchClassSet( "023458", "6-Z39" ) );
		pcSetCatalog.put ( "6-Z40", new PitchClassSet( "012358", "6-Z40" ) );
		pcSetCatalog.put ( "6-Z41", new PitchClassSet( "012368", "6-Z41" ) );
		pcSetCatalog.put ( "6-Z42", new PitchClassSet( "012369", "6-Z42" ) );
		pcSetCatalog.put ( "6-Z43", new PitchClassSet( "012568", "6-Z43" ) );
		pcSetCatalog.put ( "6-Z44", new PitchClassSet( "012569", "6-Z44" ) );
		pcSetCatalog.put ( "6-Z45", new PitchClassSet( "023469", "6-Z45" ) );
		pcSetCatalog.put ( "6-Z46", new PitchClassSet( "012469", "6-Z46" ) );
		pcSetCatalog.put ( "6-Z47", new PitchClassSet( "012479", "6-Z47" ) );
		pcSetCatalog.put ( "6-Z48", new PitchClassSet( "012579", "6-Z48" ) );
		pcSetCatalog.put ( "6-Z49", new PitchClassSet( "013479", "6-Z49" ) );
		pcSetCatalog.put ( "6-Z50", new PitchClassSet( "014679", "6-Z50" ) );
		pcSetCatalog.put ( "7-1", new PitchClassSet( "0123456", "7-1" ) );
		pcSetCatalog.put ( "7-2", new PitchClassSet( "0123457", "7-2" ) );
		pcSetCatalog.put ( "7-3", new PitchClassSet( "0123458", "7-3" ) );
		pcSetCatalog.put ( "7-4", new PitchClassSet( "0123467", "7-4" ) );
		pcSetCatalog.put ( "7-5", new PitchClassSet( "0123567", "7-5" ) );
		pcSetCatalog.put ( "7-6", new PitchClassSet( "0123478", "7-6" ) );
		pcSetCatalog.put ( "7-7", new PitchClassSet( "0123678", "7-7" ) );
		pcSetCatalog.put ( "7-8", new PitchClassSet( "0234568", "7-8" ) );
		pcSetCatalog.put ( "7-9", new PitchClassSet( "0123468", "7-9" ) );
		pcSetCatalog.put ( "7-10", new PitchClassSet( "0123469", "7-10" ) );
		pcSetCatalog.put ( "7-11", new PitchClassSet( "0134568", "7-11" ) );
		pcSetCatalog.put ( "7-Z12", new PitchClassSet( "0123479", "7-Z12" ) );
		pcSetCatalog.put ( "7-13", new PitchClassSet( "0124568", "7-13" ) );
		pcSetCatalog.put ( "7-14", new PitchClassSet( "0123578", "7-14" ) );
		pcSetCatalog.put ( "7-15", new PitchClassSet( "0124678", "7-15" ) );
		pcSetCatalog.put ( "7-16", new PitchClassSet( "0123569", "7-16" ) );
		pcSetCatalog.put ( "7-Z17", new PitchClassSet( "0124569", "7-Z17" ) );
		pcSetCatalog.put ( "7-Z18", new PitchClassSet( "0123589", "7-Z18" ) );
		pcSetCatalog.put ( "7-19", new PitchClassSet( "0123679", "7-19" ) );
		if ( flavor == PitchClassSetCatalog.FORTE ) {
			pcSetCatalog.put ( "7-20", new PitchClassSet( "0124789", "7-20" ) );
		}
		else {
			pcSetCatalog.put ( "7-20", new PitchClassSet( "0125679", "7-20" ) );
		}
		pcSetCatalog.put ( "7-21", new PitchClassSet( "0124589", "7-21" ) );
		pcSetCatalog.put ( "7-22", new PitchClassSet( "0125689", "7-22" ) );
		pcSetCatalog.put ( "7-23", new PitchClassSet( "0234579", "7-23" ) );
		pcSetCatalog.put ( "7-24", new PitchClassSet( "0123579", "7-24" ) );
		pcSetCatalog.put ( "7-25", new PitchClassSet( "0234679", "7-25" ) );
		pcSetCatalog.put ( "7-26", new PitchClassSet( "0134579", "7-26" ) );
		pcSetCatalog.put ( "7-27", new PitchClassSet( "0124579", "7-27" ) );
		pcSetCatalog.put ( "7-28", new PitchClassSet( "0135679", "7-28" ) );
		pcSetCatalog.put ( "7-29", new PitchClassSet( "0124679", "7-29" ) );
		pcSetCatalog.put ( "7-30", new PitchClassSet( "0124689", "7-30" ) );
		pcSetCatalog.put ( "7-31", new PitchClassSet( "0134679", "7-31" ) );
		pcSetCatalog.put ( "7-32", new PitchClassSet( "0134689", "7-32" ) );
		pcSetCatalog.put ( "7-33", new PitchClassSet( "012468A", "7-33" ) );
		pcSetCatalog.put ( "7-34", new PitchClassSet( "013468A", "7-34" ) );
		pcSetCatalog.put ( "7-35", new PitchClassSet( "013568A", "7-35" ) );
		pcSetCatalog.put ( "7-Z36", new PitchClassSet( "0123568", "7-Z36" ) );
		pcSetCatalog.put ( "7-Z37", new PitchClassSet( "0134578", "7-Z37" ) );
		pcSetCatalog.put ( "7-Z38", new PitchClassSet( "0124578", "7-Z38" ) );
		pcSetCatalog.put ( "8-1", new PitchClassSet( "01234567", "8-1" ) );
		pcSetCatalog.put ( "8-2", new PitchClassSet( "01234568", "8-2" ) );
		pcSetCatalog.put ( "8-3", new PitchClassSet( "01234569", "8-3" ) );
		pcSetCatalog.put ( "8-4", new PitchClassSet( "01234578", "8-4" ) );
		pcSetCatalog.put ( "8-5", new PitchClassSet( "01234678", "8-5" ) );
		pcSetCatalog.put ( "8-6", new PitchClassSet( "01235678", "8-6" ) );
		pcSetCatalog.put ( "8-7", new PitchClassSet( "01234589", "8-7" ) );
		pcSetCatalog.put ( "8-8", new PitchClassSet( "01234789", "8-8" ) );
		pcSetCatalog.put ( "8-9", new PitchClassSet( "01236789", "8-9" ) );
		pcSetCatalog.put ( "8-10", new PitchClassSet( "02345679", "8-10" ) );
		pcSetCatalog.put ( "8-11", new PitchClassSet( "01234579", "8-11" ) );
		pcSetCatalog.put ( "8-12", new PitchClassSet( "01345679", "8-12" ) );
		pcSetCatalog.put ( "8-13", new PitchClassSet( "01234679", "8-13" ) );
		pcSetCatalog.put ( "8-14", new PitchClassSet( "01245679", "8-14" ) );
		pcSetCatalog.put ( "8-Z15", new PitchClassSet( "01234689", "8-Z15" ) );
		pcSetCatalog.put ( "8-16", new PitchClassSet( "01235789", "8-16" ) );
		pcSetCatalog.put ( "8-17", new PitchClassSet( "01345689", "8-17" ) );
		pcSetCatalog.put ( "8-18", new PitchClassSet( "01235689", "8-18" ) );
		pcSetCatalog.put ( "8-19", new PitchClassSet( "01245689", "8-19" ) );
		pcSetCatalog.put ( "8-20", new PitchClassSet( "01245789", "8-20" ) );
		pcSetCatalog.put ( "8-21", new PitchClassSet( "0123468A", "8-21" ) );
		pcSetCatalog.put ( "8-22", new PitchClassSet( "0123568A", "8-22" ) );
		// This PCS is missing from the list published on lulu.eastman.edu (David Headlam's list)
		pcSetCatalog.put ( "8-23", new PitchClassSet( "0123578A", "8-23" ) );
		pcSetCatalog.put ( "8-24", new PitchClassSet( "0124568A", "8-24" ) );
		pcSetCatalog.put ( "8-25", new PitchClassSet( "0124678A", "8-25" ) );
		if ( flavor == PitchClassSetCatalog.FORTE ) {
			pcSetCatalog.put ( "8-26", new PitchClassSet( "0124579A", "8-26" ) );
		}
		else {
			pcSetCatalog.put ( "8-26", new PitchClassSet( "0134578A", "8-26" ) );
		}
		pcSetCatalog.put ( "8-27", new PitchClassSet( "0124578A", "8-27" ) );
		pcSetCatalog.put ( "8-28", new PitchClassSet( "0134679A", "8-28" ) );
		pcSetCatalog.put ( "8-Z29", new PitchClassSet( "01235679", "8-Z29" ) );
		pcSetCatalog.put ( "9-1", new PitchClassSet( "012345678", "9-1" ) );
		pcSetCatalog.put ( "9-2", new PitchClassSet( "012345679", "9-2" ) );
		pcSetCatalog.put ( "9-3", new PitchClassSet( "012345689", "9-3" ) );
		pcSetCatalog.put ( "9-4", new PitchClassSet( "012345789", "9-4" ) );
		pcSetCatalog.put ( "9-5", new PitchClassSet( "012346789", "9-5" ) );
		pcSetCatalog.put ( "9-6", new PitchClassSet( "01234568A", "9-6" ) );
		pcSetCatalog.put ( "9-7", new PitchClassSet( "01234578A", "9-7" ) );
		pcSetCatalog.put ( "9-8", new PitchClassSet( "01234678A", "9-8" ) );
		pcSetCatalog.put ( "9-9", new PitchClassSet( "01235678A", "9-9" ) );
		pcSetCatalog.put ( "9-10", new PitchClassSet( "01234679A", "9-10" ) );
		pcSetCatalog.put ( "9-11", new PitchClassSet( "01235679A", "9-11" ) );
		pcSetCatalog.put ( "9-12", new PitchClassSet( "01245689A", "9-12" ) );
		pcSetCatalog.put ( "10-1", new PitchClassSet( "0123456789", "10-1" ) );
		pcSetCatalog.put ( "10-2", new PitchClassSet( "012345678A", "10-2" ) );
		pcSetCatalog.put ( "10-3", new PitchClassSet( "012345679A", "10-3" ) );
		pcSetCatalog.put ( "10-4", new PitchClassSet( "012345689A", "10-4" ) );
		pcSetCatalog.put ( "10-5", new PitchClassSet( "012345789A", "10-5" ) );
		pcSetCatalog.put ( "10-6", new PitchClassSet( "012346789A", "10-6" ) );
		pcSetCatalog.put ( "11-1", new PitchClassSet( "0123456789A", "11-1" ) );
		pcSetCatalog.put ( "12-1", new PitchClassSet( "0123456789AB", "12-1" ) );		
		return pcSetCatalog;
	}
	
	/**
	 * Returns all Z-related sets. A set M is Z-related to set N if the IC Vector if M is identical to the IC Vector of N
	 * but M is not equivalent to N under T(n), I or M.
	 * @param flavor The "flavor" of sets to return. Use 0 for PC Sets according to Forte, any other value to return the sets according to Rahn.
	 * This only affect the values for 5-20, 6-29, 6-Z29, 6-31, 7-20 and 8-26.
	 * @return A Map with PitchClassSet names as strings and PitchClassSets as keys.
	 */
	public static Map<String,PitchClassSet> zRelatedPcSets( int flavor ) {
		Map<String,PitchClassSet> zRelatedSets = new HashMap<String,PitchClassSet>();
		Map<String,PitchClassSet> catalog = PitchClassSetCatalog.allPcSets( flavor );
		for ( String key : PitchClassSetCatalog.allPcSets( flavor ).keySet() ) {
			if ( key != null && StringUtils.contains( key, "Z" ) ) {
				zRelatedSets.put(key, catalog.get(key) );
			}
		}
		return zRelatedSets;
	}

	/**
	 * Returns all Z-related pairs. A set M is Z-related to set N if the IC Vector if M is identical to the IC Vector of N
	 * but M is not equivalent to N under T(n), I or M.
	 * @param flavor The "flavor" of sets to return. Use 0 for PC Sets according to Forte, any other value to return the sets according to Rahn.
	 * This only affect the values for 5-20, 6-29, 6-Z29, 6-31, 7-20 and 8-26.
	 * @return A Map with aPitchClassSet names as strings and Z-related PitchClassSet names as keys.
	 * Each key-value pair M,N has a complementary pair N,M.
	 */
	public static Map<String,String> zRelatedPairs( int flavor ) {
		Map<String,String> zRelatedPairs = new HashMap<String,String>();

		// Static mappings of Z-related pairs.

		zRelatedPairs.put("4-Z15", "4-Z29");
		zRelatedPairs.put("4-Z29", "4-Z15");

		zRelatedPairs.put("5-Z12", "5-Z36");
		zRelatedPairs.put("5-Z36", "5-Z12");
		
		zRelatedPairs.put("5-Z17", "5-Z37");
		zRelatedPairs.put("5-Z37", "5-Z17");
		
		zRelatedPairs.put("5-Z18", "5-Z38");
		zRelatedPairs.put("5-Z38", "5-Z18");

		zRelatedPairs.put("6-Z3", "6-Z36");
		zRelatedPairs.put("6-Z36", "6-Z3");
		
		zRelatedPairs.put("6-Z4", "6-Z37");
		zRelatedPairs.put("6-Z37", "6-Z4");
		
		zRelatedPairs.put("6-Z6", "6-Z38");
		zRelatedPairs.put("6-Z38", "6-Z6");
		
		zRelatedPairs.put("6-Z10", "6-Z39");
		zRelatedPairs.put("6-Z39", "6-Z10");

		zRelatedPairs.put("6-Z11", "6-Z40");
		zRelatedPairs.put("6-Z40", "6-Z11");
		
		zRelatedPairs.put("6-Z12", "6-Z41");
		zRelatedPairs.put("6-Z41", "6-Z12");

		zRelatedPairs.put("6-Z13", "6-Z42");
		zRelatedPairs.put("6-Z42", "6-Z13");

		zRelatedPairs.put("6-Z17", "6-Z43");
		zRelatedPairs.put("6-Z43", "6-Z17");

		zRelatedPairs.put("6-Z19", "6-Z44");
		zRelatedPairs.put("6-Z44", "6-Z19");

		zRelatedPairs.put("6-Z23", "6-Z45");
		zRelatedPairs.put("6-Z45", "6-Z23");

		zRelatedPairs.put("6-Z24", "6-Z46");
		zRelatedPairs.put("6-Z46", "6-Z24");

		zRelatedPairs.put("6-Z25", "6-Z47");
		zRelatedPairs.put("6-Z47", "6-Z25");

		zRelatedPairs.put("6-Z26", "6-Z48");
		zRelatedPairs.put("6-Z48", "6-Z26");

		zRelatedPairs.put("6-Z28", "6-Z49");
		zRelatedPairs.put("6-Z49", "6-Z28");

		zRelatedPairs.put("6-Z29", "6-Z50");
		zRelatedPairs.put("6-Z50", "6-Z29");

		zRelatedPairs.put("7-Z12", "7-Z36");
		zRelatedPairs.put("7-Z36", "7-Z12");
		
		zRelatedPairs.put("7-Z17", "7-Z37");
		zRelatedPairs.put("7-Z37", "7-Z17");

		zRelatedPairs.put("7-Z18", "7-Z38");
		zRelatedPairs.put("7-Z38", "7-Z18");
		
		zRelatedPairs.put("8-Z15", "8-Z29");
		zRelatedPairs.put("8-Z29", "8-Z15");

		return zRelatedPairs;
	}
	
	/**
	 * The getPitchClassSetByName method will return a PitchClassSet from the PCSet name
	 * passed in as a String. It will return null if no such name exists. The name must be
	 * a canonical set name matching the regular expression {[0-9]+-[0-9]?} with no extraneous characters.
	 * @param name  The canonical name of the PCSet to return.
	 * @return A PitchClassSet in Normal Form corresponding to the name passed in.
	 **/
	public static PitchClassSet getPitchClassSetByName( String name ) {
		return PitchClassSetCatalog.allPcSets().get( name );
	}

	/**
	 * The getPitchClassSetByCardinality method will return all PitchClassSets with the number of elements
	 * passed as an Integer. For numbers less than 0 and greater than 12, it returns null.
	 * @param cardinality  The cardinality used to select the PitchClassSets to return.
	 * @return A list of PitchClassSet with the same cardinality as the integer passed in.
	 **/
	public static Map<String,PitchClassSet> getPitchClassSetsByCardinality( int cardinality ) {
		Map<String,PitchClassSet> returnSets = new HashMap<String,PitchClassSet>();
		Set<String> pcSets = PitchClassSetCatalog.allPcSets().keySet();
		Iterator<String> pcSetIt = pcSets.iterator();
		while ( pcSetIt.hasNext() ) {
			String pcsetName = pcSetIt.next();
			PitchClassSet pcSet = PitchClassSetCatalog.getPitchClassSetByName( pcsetName );
			if ( pcSet.size() == cardinality ) {
				returnSets.put(pcsetName, pcSet);
			}
		}
		return returnSets;
	}

	/**
	 * The getNameByPitchClassSet method will return the PCSet name as a String for the PitchClassSet passed
	 * in as a parameter. It will work for any transformation T[n} or I of any set.
	 * @param inColl  The PitchClassSet for which thefgtjhgfdskjhbgvfdxz name will be looked up and returned.
	 * @return A String with the name of the PitchClassSet inPcSet. Can be used to lookup a PCSet name given any transformation of that set.
	 */
	public static String getNameByPitchClassSet( IPitchCollection inColl ) {
		PitchClassSet pcSet = new PitchClassSet( inColl );
		Iterator<String> pcSetIt = PitchClassSetCatalog.allPcSets().keySet().iterator();
		while ( pcSetIt.hasNext() ) {
			String pcsetName = pcSetIt.next();
			if ( PitchClassSetCatalog.allPcSets().get(pcsetName).equivalent( pcSet ) ) {
				return pcsetName;
			}
		}
		return null;
	}
}
