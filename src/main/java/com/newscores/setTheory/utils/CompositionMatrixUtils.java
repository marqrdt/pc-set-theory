package com.newscores.setTheory.utils;

import java.util.ArrayList;
import java.util.List;

import com.newscores.setTheory.CMSegment;
import com.newscores.setTheory.CompositionMatrix;
import com.newscores.setTheory.PitchClassSet;
import com.newscores.setTheory.PitchClassSequenceTransformation;
import com.newscores.setTheory.interfaces.*;
import org.apache.commons.lang.StringUtils;

public class CompositionMatrixUtils {
	static final String spacerString = new String(" ");
	static final String separatorString = new String("|");

	/**
	 * Return the PitchClassSet created from a List. In combination with the getColumn( index ) and
	 * get getRow( index ) methods of CompositionMatrix, it can be useful for quickly getting the PitchClassSet
	 * from a segment of a CompositionMatrix.
	 * @param cmSegs  The CMSegments from which to create this.
	 * @return  A PitchClassSet created from members of the CMSegments in cmSegs.
	 */
	public static PitchClassSet getPitchClassSet( List<CMSegment> cmSegs ) {
		PitchClassSet pcSet = new PitchClassSet();
		for ( CMSegment seg : cmSegs ) {
			for ( int pc : seg.getPitchSequence().getMembers() ) {
				pcSet.addPitch( pc );
			}
		}
		return pcSet;
	}
	
	/**
	 * Create a CompositionMatrix from an array of IPitchSequence and an array of coordinates at which to place them.
	 * @param inRows  An array of IPitchSequence from which to construct the CompositionMatrix.
	 * @param layout  An array of position pairs at which to place them.
	 * @return  A CompositionMatrix created from inRows and layout.
	 */
	public static CompositionMatrix getCompositionMatrix( ISequence[] inRows, Integer[][] layout) {
		CompositionMatrix outCM = new CompositionMatrix();
		for ( int i = 0; i < layout.length; i++ ) {
			int columnCounter = 0;
			for ( int j = 0; j < layout[i].length; j++ ) {
				outCM.addSegment( inRows[i].subSequence(columnCounter, layout[i][j]) , i, j);
				columnCounter += layout[i][j];
			}
		}
		return outCM;
	}

	/**
	 * Create a CompositionMatrix from an array of IPitchSequence and an array of coordinates at which to place them.
	 * @param inRows  An List of IPitchSequence from which to construct the CompositionMatrix.
	 * @param layout  An List of List&lt;Integer&gt; of position pairs at which to place them.
	 * @return  A CompositionMatrix created from inRows and layout.
	 */
	public static CompositionMatrix getCompositionMatrix( List<ISequence> inRows, List<List<Integer>> layout) {
		CompositionMatrix outCM = new CompositionMatrix();
		for ( int i = 0; i < layout.size(); i++ ) {
			int columnCounter = 0;
			for ( int j = 0; j < layout.get(i).size(); j++ ) {
				ISequence subSeq = inRows.get(i).subSequence(columnCounter, layout.get(i).get(j));
				// Here, we will assume all CMSegments in the same row have the same descriptor.
				// This allows preservation of descriptors for rows created from subsequences of a single ISequence.
				// It's a utility-- if you don't like it, write your own method :-)
				subSeq.setDescriptor( inRows.get(i).getDescriptor() );
				outCM.addSegment( subSeq ,i, j);
				columnCounter += layout.get(i).get(j);
			}
		}
		return outCM;
	}

	/**
	The interlace method is a specialized function used to create larger CM. Starting with a N x N
	combinatorial CM, where each row and column form an aggregate, the method returns a 2N x 2N CM
	with the rows and columns extended in the following manner:
	Original CM:
	012345 | 6789AB
	BA9876 | 543210
	
	Extended CM:
	012345 |       | 6789AB
               |012345 |        | 6789AB
	BA9876 |       | 543210 
               |BA9876 |        | 543210 
		
	The main purpose of creating the extended CM is as an intermediate step to producing
	a larger CM where "end sets" can be swapped for more interesting patterns, like:
	012345 |    67 | 89AB  |
               |01234  |       | 56789AB
	BA9876 |     5 | 43210 |
               |BA98   | 765   | 43210
           
    @param inCMs  The CMs to interlace.
    @return An interlaced CompositionMatrix
	*/
	public static CompositionMatrix interlace( List<CompositionMatrix> inCMs ) {
		CompositionMatrix outCM = new CompositionMatrix();
		int cmCounter = 0;
		for ( CompositionMatrix cm : inCMs ) {
			for ( CMSegment seg : cm.getSegments() ) {
				for ( int j = 0; j < cm.getNumberOfColumns(); j++ ) {
					outCM.addSegment(
							//(PitchClassSequence)
							cm.getSegmentAtCoordinate(
								seg.getRowIndex(), seg.getColumnIndex() ).getPitchSequence(),
								seg.getRowIndex() * 2 + cmCounter,
								seg.getColumnIndex() * 2 + cmCounter,
								true
						);
				}
			}
			cmCounter++;
		}
		return outCM;
	}
		
	/**
	 * Formats a CompositionMatrix as a String with columns formatted to a common width. Example:
	012345 |    67 | 89AB  |
           | 01234 |       | 56789AB
	BA9876 |     5 | 43210 |
           | BA98  | 765   | 43210
	 * @param cm  The CompositionMatrix to format.
	 * @return  The CompositionMatrix formatted as a String, using the default values for the spacer and separator strings.
	 */
	public static String format( CompositionMatrix cm ) {
		return CompositionMatrixUtils.format(cm, CompositionMatrixUtils.spacerString, CompositionMatrixUtils.separatorString);
	}

	/**
	 * Formats a CompositionMatrix as a String with columns formatted to a common width. Example:
	012345 |    67 | 89AB  |
           | 01234 |       | 56789AB
	BA9876 |     5 | 43210 |
           | BA98  | 765   | 43210
	 * @param cm  The CompositionMatrix to format.
	 * @param inSpacerString  The String used to separate pcs within each segment.
	 * @param inSeparatorString  The String used to separate each segment.
	 * @return  The CompositionMatrix formatted as a String.
	 */
	public static String format( CompositionMatrix cm, String inSpacerString, String inSeparatorString ) {
		StringBuffer outputBuf = new StringBuffer();
		int segmentCount = 0;
		List<CMSegment> sortedSegs = cm.getSegments();
		String spacerString = "";
		if ( inSpacerString == null) {
			spacerString = CompositionMatrixUtils.spacerString;
		} else {
			spacerString = inSpacerString;
		}
		String separatorString = "";
		if ( inSeparatorString == null) {
			separatorString = CompositionMatrixUtils.separatorString;
		} else {
			separatorString = inSeparatorString;
		}
		//Collections.sort( sortedSegs );
		List<Integer> columnLengths = new ArrayList<Integer>();
		for ( int icolumn = 0; icolumn < cm.getNumberOfColumns(); icolumn++ ) {
			List<CMSegment> columns = cm.getColumn( icolumn );
			int columnLengthMax = 0;
			for ( CMSegment column : columns ) {
				if ( column != null && column.getPitchSequence().toString().length() >columnLengthMax ) {
					columnLengthMax = column.getPitchSequence().toString().length();
				}
			}
			columnLengths.add( columnLengthMax );
		}
		for ( int irow = 0; irow < cm.getNumberOfRows(); irow++ ) {
			for ( int icol = 0; icol < cm.getNumberOfColumns(); icol++ ) {
				CMSegment seg = cm.getSegmentAtCoordinate(irow, icol);
				if ( seg != null ) {
					outputBuf.append( seg.getPitchSequence().toString() );
					int stringLen = seg.getPitchSequence().toString().length();
					while ( stringLen < columnLengths.get(icol) ) {
						outputBuf.append( spacerString );
						stringLen++;
					}
				} else {
					outputBuf.append( StringUtils.repeat( CompositionMatrixUtils.spacerString, columnLengths.get(icol) ) );
				}
				if ( icol < cm.getNumberOfColumns() - 1) {
					outputBuf.append( separatorString );
				}
			}
			outputBuf.append("\n");
		}
		return outputBuf.toString();
	}

	/**
	 * Formats a CompositionMatrix as a String with columns formatted to a common width. Example:
	012345 |    67 | 89AB  |
           | 01234 |       | 56789AB
	BA9876 |     5 | 43210 |
           | BA98  | 765   | 43210
	 * @param cm  The CompositionMatrix to format.
	 * @param inSpacerString  The String used to separate pcs within each segment.
	 * @param inSeparatorString  The String used to separate each segment.
	 * @param extendedOutput If true, the description field of each row will be prepended to the row content.
	 * @return  The CompositionMatrix formatted as a String.
	 */
	public static String format( CompositionMatrix cm, String inSpacerString, String inSeparatorString, boolean extendedOutput ) {
		StringBuffer outputBuf = new StringBuffer();
		int segmentCount = 0;
		List<CMSegment> sortedSegs = cm.getSegments();
		String spacerString = "";
		if ( inSpacerString == null) {
			spacerString = CompositionMatrixUtils.spacerString;
		} else {
			spacerString = inSpacerString;
		}
		String separatorString = "";
		if ( inSeparatorString == null) {
			separatorString = CompositionMatrixUtils.separatorString;
		} else {
			separatorString = inSeparatorString;
		}
		//Collections.sort( sortedSegs );
		List<Integer> columnLengths = new ArrayList<Integer>();
		for ( int icolumn = 0; icolumn < cm.getNumberOfColumns(); icolumn++ ) {
			List<CMSegment> columns = cm.getColumn( icolumn );
			int columnLengthMax = 0;
			for ( CMSegment column : columns ) {
				if ( column != null && column.getPitchSequence().toString().length() >columnLengthMax ) {
					columnLengthMax = column.getPitchSequence().toString().length();
				}
			}
			columnLengths.add( columnLengthMax );
		}
		for ( int irow = 0; irow < cm.getNumberOfRows(); irow++ ) {
			for ( int icol = 0; icol < cm.getNumberOfColumns(); icol++ ) {
				CMSegment seg = cm.getSegmentAtCoordinate(irow, icol);
				if ( extendedOutput && icol == 0 ) {
					outputBuf.append( StringUtils.rightPad( seg.getDescriptor(), PitchClassSequenceTransformation.MAX_TRANSFORMATION_LENGTH) + ": " );
				}
				if ( seg != null ) {
					outputBuf.append( seg.getPitchSequence().toString() );
					int stringLen = seg.getPitchSequence().toString().length();
					while ( stringLen < columnLengths.get(icol) ) {
						outputBuf.append( spacerString );
						stringLen++;
					}
				} else {
					outputBuf.append( StringUtils.repeat( CompositionMatrixUtils.spacerString, columnLengths.get(icol) ) );
				}
				if ( icol < cm.getNumberOfColumns() - 1) {
					outputBuf.append( separatorString );
				}
			}
			outputBuf.append("\n");
		}
		return outputBuf.toString();
	}
}
