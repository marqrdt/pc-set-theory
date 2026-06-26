package org.paulmarquardt.setTheory;

import java.util.*;
import com.fasterxml.jackson.annotation.*;
import org.paulmarquardt.setTheory.interfaces.*;

/**
 * A CompositionMatrix (CM) is an n x m matrix of CMSegments. Each CMSegment contains an IPitchSequence and has
 * a specified row and column position. In many compositional models, CM rows and columns form aggregates (completed 12-note sets),
 * but this is in no way required or enforced. CMs are either created empty of created from another CM. The addSegment()
 * methods can be used to add CMSegments to the CM.
 * @author marqrdt
 *
 */

public class CompositionMatrix {

	private List<CMSegment> cmSegs;
	private static boolean REPLACE = false;
	public static final int ROW_WISE = 0;
	public static final int COLUMN_WISE = 1;
	public static final int ROW_COLUMN_WISE = 1;
	private String descriptor;
	private String name;
	
	/**
	 * Create an empty CompositionMatrix.
	 */
	public CompositionMatrix() {
		this.cmSegs = new ArrayList<CMSegment>();
		this.setName("");
		this.setDescriptor("");
	}
	
	/**
	 * Create a CompositionMatrix from another CompositionMatrix.
	 * @param inCM  The CM from which to populate this.
	 */
	public CompositionMatrix( CompositionMatrix inCM) {
		this.cmSegs = new ArrayList<CMSegment>();
		for ( CMSegment seg : inCM.getSegments() ) {
			if ( seg != null ) {
				this.addSegment( seg.getPitchSequence(), seg.getRowIndex(), seg.getColumnIndex(), true );
			}
		}
		this.setName("");
		this.setDescriptor("");
	}

        /**
	 * Create a CompositionMatrix from a List of IPitchSequences inPSeqs and a List of List&lt;Integer&gt; inSegs.
         * The length of inPSeqs must be equal to the length of inSegs. Likewise, the length of each IPitchSequence in inPSeqs
         * must be equal to the sum of the members of the corresponding member of inSegs. An example is given below:
         * inPSeqs:
         * &lt;0,1,2,3,4,5,6,7,8,9,a,b&gt;,
         * &lt;3,4,5,6,7,8,9,a,b,0,1,2&gt;,
         * &lt;7,8,9,a,b,0,1,2,3,4,5,6&gt;,
         * 
         * inSegs:
         * [3,4,5],
         * [4,5,3],
         * [5,3,4]
         * 
         * Created CM instance:
         * 012  |3456 |789ab
         * 3456 |789ab|012
         * 789ab|012  |3456
         * 
         * An IllegalArgumentException will be thrown on any of the following conditions:
         * 1. The length of inPSeqs is not equal to the length of inSegs.
         * 2. The length of each IPitchSequence in inPSeqs (Pseq[n]) is not equal to the sum of the members of the 
         *     corresponding member in inSeg (inSeg[n]).
         * @param inPSeqs A List of IPitchSequence instances used as the pitch content.
         * @param inSegs A List of List&lt;Integer&gt; instances. Each List&lt;Integer&gt; member specifies how to divide the corresponding IPitchSequence.
	 */
	public CompositionMatrix( List<ISequence> inPSeqs, List<List<Integer>> inSegs) {
            if ( inPSeqs.size() != inSegs.size() ) {
                throw( new IllegalArgumentException( "The size of List parameters must be the same size.") );
            }
			int index = 0;
            for ( List<Integer> seg : inSegs ) {
                int sum = 0;
                for ( Integer member : seg ) {
                    sum += member;
                }
                if ( sum != inPSeqs.get(index).length() ) {
                    throw( new IllegalArgumentException("The size of each IPitchSequence in inPSeqs must be equal to the sum of the corresponding member of inSegs") );
                }
                index++;
            }
            //System.out.println( String.format("%s, %s", inPSeqs.toString(), inSegs.toString()));
            this.cmSegs = new ArrayList<CMSegment>();
            int irow = 0;
            for ( ISequence pSeq : inPSeqs ) {
                int currentIndex = 0;
                int icol = 0;
                for ( Integer segLength : inSegs.get(irow) ) {
                    ISequence seq = pSeq.subSequence(currentIndex, segLength);
                    //System.out.println( String.format("%s, %d, %d", seq.toString(), irow, icol));
                    currentIndex += segLength;
                    this.addSegment(seq, irow, icol, true);
                    icol += 1;           
                }
                irow += 1;
            }
    		this.setName("");
    		this.setDescriptor("");
	}

	/**
	 * Add a segment to this at the specified coordinates.
	 * @param seq The IPitchSequence to add to this.
	 * @param irow The row index of the new segment.
	 * @param icolumn The column index of the new segment.
	 */
	public void addSegment( ISequence seq, int irow, int icolumn ) {
		this.addSegment(seq, irow, icolumn, CompositionMatrix.REPLACE);
	}
	
	/**
	 * Add a segment to this at the specified coordinates.
	 * @param seq The IPitchSequence to add to this.
	 * @param irow The row index of the new segment.
	 * @param icolumn The column index of the new segment.
	 * @param replace Set to true to replace the CMSegment at these coordinates
	 */
	
	public void addSegment( ISequence seq, int irow, int icolumn, boolean replace ) {
		if ( this.hasSegmentAtCoordinate( irow, icolumn) && ! replace) {
			if ( this.hasSegmentAtCoordinate(irow, icolumn) ) {
				throw new IllegalArgumentException("Refusing to add segment to existing position");
			}
		}
		this.cmSegs.add( new CMSegment( seq, irow, icolumn ) );
	}
	

	/**
	 * Return a CompositionMatrix with each CMSegment subject to its T(n) operation.
	 * @param transposition  Each CMSegment will be transposed by transposition.
	 * @return CompositionMatrix  Return a CompositionMatrix with each CMSegment transposed to transposition.
	 */
	public CompositionMatrix T( int transposition ) {
		CompositionMatrix outMatrix = new CompositionMatrix();
		for ( CMSegment seg : this.cmSegs ) {
			//CMSegment newSeg = new CMSegment( seg.getPitchSequence().T(transposition), seg.getRowIndex(), seg.getColumnIndex() );
			outMatrix.addSegment(seg.getPitchSequence().T(transposition), seg.getRowIndex(), seg.getColumnIndex());
		}
		return outMatrix;
	}
	
	/**
	 * Return a CompositionMatrix with each CMSegment subject to its I() operation.
	 * @return CompositionMatrix  Return a CompositionMatrix with the I() operation applied to each CMSegment.
	 */
	public CompositionMatrix I() {
		CompositionMatrix outMatrix = new CompositionMatrix();
		for ( CMSegment seg : this.cmSegs ) {
			//CMSegment newSeg = new CMSegment( seg.getPitchSequence().T(transposition), seg.getRowIndex(), seg.getColumnIndex() );
			outMatrix.addSegment(seg.getPitchSequence().I(), seg.getRowIndex(), seg.getColumnIndex());
		}
		return outMatrix;
	}
	
	/**
	 * Return a CompositionMatrix with each CMSegment subject to its M(n) operation.
	 * @param mult  Each CMSegment will have the M(mult) operation applied.
	 * @return A CompositionMatrix with the M(mult) operation applied to each CMSegment.
	 */
	public CompositionMatrix M( int mult ) {
		CompositionMatrix outMatrix = new CompositionMatrix();
		for ( CMSegment seg : this.cmSegs ) {
			//CMSegment newSeg = new CMSegment( seg.getPitchSequence().T(transposition), seg.getRowIndex(), seg.getColumnIndex() );
			outMatrix.addSegment(seg.getPitchSequence().M(mult), seg.getRowIndex(), seg.getColumnIndex());
		}
		return outMatrix;
	}

	/**
	 * Return a CompositionMatrix containing the retrograde of each row of this.
	 * As a result, the retrograde operation performs the retrograde operation on the entire CM.
	 * @return  Return a CompositionMatrix with the R() operation applied to each CMSeg. In addition,
         * the order of the CMSegs is reversed.
	 */

	public CompositionMatrix R() {
		CompositionMatrix outMatrix = new CompositionMatrix();
		for ( CMSegment seg : this.cmSegs ) {
			// Like other transformation, but set the PitchSequence to R() and reverse the order of columns
			outMatrix.addSegment(seg.getPitchSequence().R(), seg.getRowIndex(), (this.getNumberOfColumns() - 1 ) - seg.getColumnIndex() );
		}
		return outMatrix;
	}

	/**
	 * Return the CMSegment at coordinates irow and icolumn. Returns null if no CMSegment exists at the specified coordinates.
	 * @param irow  The row coordinate.
	 * @param icolumn  The column coordinate.
	 * @return  A CMSegment at the specified coordinate.
	 */
	@JsonIgnore
	public CMSegment getSegmentAtCoordinate( int irow, int icolumn ) {
		for ( CMSegment seg : this.cmSegs ) {
			if ( seg.getRowIndex() == irow && seg.getColumnIndex() == icolumn ) {
				return seg;
			}
		}
		return null;
	}
	
	/**
	 * Returns true if there exists a CMSegment at coordinates irow and icolumn, false if not.
	 * @param irow  The row coordinate.
	 * @param icolumn  The column coordinate.
	 * @return boolean  True if there exists a CMSegment at coordinates irow and icolumn, false if not.
	 */
	@JsonIgnore
	public boolean hasSegmentAtCoordinate( int irow, int icolumn ) {
		if ( this.getSegmentAtCoordinate(irow, icolumn) != null ) {
			return true;
		}
		return false;
	}
	
	/** 
	 * Returns all CMSegments in this. It returns the CMSegments in the order in which they were added. It does not
	 * guarantee any order. The coordinate of each segment determines their location in the CM.
	 * @return  A list of all segments
	 */
	public List<CMSegment> getSegments() {
		return this.cmSegs;
	}
	
	/**
	 * Returns a List of CMSegments at column icolumn.
	 * Caller must check each segment for null value, as a Column could have a null CMSegment in a give coordinate.
	 * @param icolumn  The column coordinate.
	 * @return  A list of CMSegments at column icolumn.
	 */
	public List<CMSegment> getColumn(int icolumn) {
		List<CMSegment> segs = new ArrayList<CMSegment>();
		for ( CMSegment seg : this.cmSegs ) {
			if ( seg != null && seg.getColumnIndex() == icolumn ) {
				segs.add( seg );
			}
		}
		return segs;
	}

	/**
	 * Returns a List of CMSegments at row irow.
	 * Caller must check each segment for null value, as a Row could have a null CMSegment in a give coordinate.
	 * @param irow  The row coordinate.
	 * @return   A list of CMSegments at row irow.
	 */
	public List<CMSegment> getRow(int irow) {
		List<CMSegment> segs = new ArrayList<CMSegment>();
		for ( CMSegment seg : this.cmSegs ) {
			if ( seg != null && seg.getRowIndex() == irow ) {
				segs.add( seg );
			}
		}
		return segs;
	}

	/**
	 * Returns an IPitchSequence created from the row at index irow.
	 * Caller must check each segment for null value, as a Row could have a null CMSegment in a give coordinate.
	 * @param irow  The row coordinate.
	 * @return  The specified row as an IPitchSequence.
	 */
	public PitchClassSequence getRowAsPitchClassSequence(int irow) {
		PitchClassSequence rowSeq  = new PitchClassSequence();
		for ( CMSegment seg : this.cmSegs ) {
			if ( seg != null && seg.getRowIndex() == irow ) {
				rowSeq.extend( seg.getPitchSequence() );
			}
		}
		return rowSeq;
	}

	/**
	 * Returns an IPitchSequence created from the column at index icolumn.
	 * Caller must check each segment for null value, as a Column could have a null CMSegment in a give coordinate.
	 * @param icolumn  The column coordinate.
	 * @return  The specified column as an IPitchSequence.
	 */
	public PitchClassSequence getColumnAsPitchClassSequence(int icolumn) {
		PitchClassSequence rowSeq  = new PitchClassSequence();
		for ( CMSegment seg : this.cmSegs ) {
			if ( seg != null && seg.getColumnIndex() == icolumn ) {
				rowSeq.extend( seg.getPitchSequence() );
			}
		}
		return rowSeq;
	}

	/**
	 * Return the number of rows in this CompositionMatrix.
	 * @return  The number of rows in this.
	 */
	@JsonIgnore
	public int getNumberOfRows() {
		int rowMax = 0;
		for ( CMSegment seg : this .getSegments() ) {
			if ( seg.getRowIndex() > rowMax) {
				rowMax = seg.getRowIndex();
			}
		}
		return rowMax + 1;
	}
	
	/**
	 * Return the number of columns in this CompositionMatrix.
	 * @return  The number of columns in this.
	 */
	@JsonIgnore
	public int getNumberOfColumns() {
		int colMax = 0;
		for ( CMSegment seg : this.getSegments() ) {
			if ( seg.getColumnIndex() > colMax) {
				colMax = seg.getColumnIndex();
			}
		}
		return colMax + 1;
	}

	/**
	 * Return true if the rows and columns form 12-tone Rows.
	 * @return  The true is this CM is canonical, i.e. its rows and columns form 12-tone rows.
	 */
	@JsonIgnore
	public boolean isCanonical() {
		//boolean isCanonical = true;
		return this.isCanonical(true, true);
	}

	/**
	 * Return true if the rows and columns form 12-tone Rows.
	 * @return  The true is this CM is canonical, i.e. its rows or columns form 12-tone rows
	 * depending on the criteria in the rowWise and columnWise parameters.
	 * @param rowWise If true, the method will test if CM rows are 12-tone Rows.
	 * @param columnWise If true, the method will test if CM columns are 12-tone Rows.
	 */
	@JsonIgnore
	public boolean isCanonical( boolean rowWise, boolean columnWise) {
		//boolean isCanonical = true;
		// The basic mode is to test all row and columns depending on the values passed in
		// for rowWise and columnWise. Fail immediately if any condition tests false, as there is
		// no need to test further.
		if ( rowWise ) {
			for ( int i = 0; i < this.getNumberOfRows(); i++ ) {
				PitchClassSequence seq = this.getRowAsPitchClassSequence(i);
				//System.out.println(String.format("Inside isCanonical row test, PCSeq is %s", seq.toString()));
				if ( seq.size() != PitchClassSet.MODULUS ) {
					//System.out.println(String.format("Inside isCanonical row test, set length test failed on %s ", seq.toString()));
					return false;
				}
				PitchClassSet pcset = new PitchClassSet( seq );
				if ( ! pcset.equals(PitchClassSet.aggregate()) ) {
					//System.out.println(String.format("Inside isCanonical row test, Aggregate test failed on %s ", pcset.toString()));
					return false;
				}
			}
		}
		if ( columnWise ) {
			for ( int i = 0; i < this.getNumberOfColumns(); i++ ) {
				PitchClassSequence seq = this.getColumnAsPitchClassSequence(i);
				//System.out.println(String.format("Inside isCanonical column test, PCSeq is %s", seq.toString()));
				if ( seq.size() != PitchClassSet.MODULUS ) {
					//System.out.println(String.format("Inside isCanonical column test, set length test failed on %s ", seq.toString()));
					return false;
				}
				PitchClassSet pcset = new PitchClassSet( seq );
				if ( ! pcset.equals(PitchClassSet.aggregate()) ) {
					//System.out.println(String.format("Inside isCanonical column test, Aggregate test failed on %s ", pcset.toString()));
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Accepts a PitchClassSequenceTransformation and applies it to a copy of this. While canonical transformations
	 * should be in the form of {T[n]}{M[n]}{I}, this method will parse multiple tokens, applying them
	 * in reverse order.
	 * @param pSeqTrans A String containing a sequence transformation tokens.
	 * @return CompositionMatrix
	 */
	public CompositionMatrix fromTransformation(PitchClassSequenceTransformation pSeqTrans) {
		CompositionMatrix outCM = new CompositionMatrix( this );
		if ( pSeqTrans.isInversion() ) {
			outCM = outCM.I();
		}
		outCM = outCM.M(pSeqTrans.getMultiplication());
		outCM = outCM.T(pSeqTrans.getTransposition());
		if ( pSeqTrans.isRetrograde() ) {
			outCM = outCM.R();
		}
		outCM.setDescriptor(pSeqTrans.toString());
		return outCM;
	}

	/**
	 * A short alias for fromTransformation(PitchClassSequenceTransformation pSeqTrans).
	 * @param pSeqTrans A String containing a sequence transformation tokens.
	 * @return CompositionMatrix
	 */
	public CompositionMatrix apply(PitchClassSequenceTransformation pSeqTrans) {
		return this.fromTransformation(pSeqTrans);
	}

	/**
	 * Accepts a String containing a sequence transformation tokens. While canonical transformations
	 * should be in the form of {T[n]}{M[n]}{I}, this method will parse multiple tokens, applying them
	 * in reverse order.
	 * @param transformation  A String containing a sequence transformation tokens.
	 * @return CompositionMatrix
	 */
	public CompositionMatrix fromTransformationString(String transformation) {
		CompositionMatrix outCM = new CompositionMatrix( this );
		PitchClassSequenceTransformation pSeqTrans = new PitchClassSequenceTransformation(transformation);
		if ( pSeqTrans.isInversion() ) {
			outCM = outCM.I();
		}
		outCM = outCM.M(pSeqTrans.getMultiplication());
		outCM = outCM.T(pSeqTrans.getTransposition());
		if ( pSeqTrans.isRetrograde() ) {
			outCM = outCM.R();
		}
		outCM.setDescriptor(pSeqTrans.toString());
		return outCM;
	}

	/**
	 * Creating a List&lt;CompositionMatrix&gt; by applying transformation strings from a List&lt;String&gt;.
	 * Each transformation string is applied to this and added to the returned List. This can be a powerful
	 * composition tool, allowing composers to create larger composition designs with sequences of transformations.
	 * For example, given the list of transformations "T[5]M[5]", "RT[3]", "T3I", "RT[0]M[7]", the returned list will
	 * contain four CMs, [ this.T(5).M(5), this.R().T(3), this.T(3)I, this.R().T(0).M(7) ].
	 * @param transformationList A List&lt;String&gt; of transformation tokens.
	 * @return CompositionMatrix
	 */
	public List<CompositionMatrix> apply(List<String> transformationList) {
		List<CompositionMatrix> outCMs = new ArrayList<CompositionMatrix>();
		transformationList.
			stream().
			forEach( trans -> {
				outCMs.add( this.apply(trans));
			});
		return outCMs;
	}

	/**
	 * A short alias for fromTransformation(PitchClassSequenceTransformation pSeqTrans).
	 * @param transformation A String containing a sequence transformation tokens.
	 * @return CompositionMatrix
	 */
	public CompositionMatrix apply(String transformation) {
		return this.fromTransformationString(transformation);
	}

	/**
	 * Return an Iterator that will iterate through all transformations T[n = 1..12]M[n in {1,5,7,11} ]R of this.
	 * @return  An Iterator that will cycle through all transformations of this.
	 */
	public Iterator<CompositionMatrix> transformationIterator() {
		
		final CompositionMatrix thisCM = this;
		class TransformationIterator implements Iterator<CompositionMatrix> {

			private int index = 0;
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return ( index <  96 );
			}
			public CompositionMatrix next() {
				// TODO Auto-generated method stub
				StringBuffer transformationBuffer = new StringBuffer();
				int tIndex = index / 8;
				int iIndex = index % 8;
				//System.out.println( String.format("index: %d, iIndex: %d, iIndex mod 2 = %d, iIndex >> 1 = %d",index,  iIndex, iIndex % 2, iIndex >> 1 ) );
				boolean inversion = false;
				boolean retrograde;
				int mult = 1;
				//IPitchSequence seq = thisSeq.transposeTo(tIndex);
				//if ( (iIndex >> 1) == 0) {
				//	seq = seq.transposeTo(tIndex);
				//}
				CompositionMatrix outCM = new CompositionMatrix( thisCM );
				if ( iIndex % 2 == 1 ) {
					retrograde = true;
					transformationBuffer.append("R");
					//seq = seq.R();
				} else {
					retrograde = false;
				}
				transformationBuffer.append( String.format("T[%d]", tIndex) );									
				if ( (iIndex >> 1) == 2) {
					mult = 5;
					transformationBuffer.append( String.format("M[%d]", mult) );				
					//seq = seq.M( 5 );
				}
				if ( (iIndex >> 1) == 3) {
					mult = 7;
					transformationBuffer.append( String.format("M[%d]", mult) );					
					//seq = seq.M( 7 );
				}
				if ( (iIndex >> 1) == 1) {
					inversion = true;
					transformationBuffer.append( String.format("I") );					
					//seq = seq.I();
				}
				//seq = seq.T(tIndex);
				if ( inversion ) {
					outCM = outCM.I();
				}
				if ( mult > 1 ) {
					outCM = outCM.M(mult);
				}
				outCM = outCM.T(tIndex);
				if ( retrograde ) {
					outCM = outCM.R();
				}
				outCM.setDescriptor( transformationBuffer.toString() );
				index++;
				return outCM;
			}

			public void remove() {
				// TODO Auto-generated method stub
				
			}
		}
		return new TransformationIterator();
	}

	/**
	 * Returns true if this CompositionMatrix is equal to another. Equality is defined as, for each
	 * row index i and column j, this.getSegmentAtCoordinate(i,j).equals( anotherCM.getSegmentAtCoordinatetRow(i,j).
	 * @param anotherCM The CompositionMatrix to compare to this.
	 */
	boolean equals(CompositionMatrix anotherCM ) {
		if ( this.getNumberOfRows() != anotherCM.getNumberOfRows() ) {
			return false;
		}
		if ( this.getNumberOfColumns() != anotherCM.getNumberOfColumns() ) {
			return false;
		}
		for ( int i = 0; i < this.getNumberOfRows(); i++ ) {
			for ( int j = 0; j < this.getNumberOfRows(); j++ ) {
				if ( ! this.getSegmentAtCoordinate(i, j).equals(anotherCM.getSegmentAtCoordinate(i, j)) ) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Returns a JSon String representation of this.
	 * @return  A String representation of this.
	 */
	public String toJsonString() {
		return ObjectSerializers.compositionMatrixJsonSerializer( this );
	}

	public String getDescriptor() {
		return this.descriptor;
	}

	public void setDescriptor(String description) {
		this.descriptor = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}