package org.paulmarquardt.setTheory.utils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.paulmarquardt.setTheory.interfaces.ISequence;
import org.apache.commons.collections4.iterators.*;

import java.util.logging.Logger;


/* Combinatorics library */
//import org.paukov.combinatorics.composition.*;
//import org.paukov.combinatorics.*;
import org.paukov.combinatorics3.Generator;

//import org.paulmarquardt.setTheory.CMSegment;
import org.paulmarquardt.setTheory.*;

public class RowUtils {
    // NUMBER_OF_TRANSFORMATIONS is the total number of row transformations T(12) x M(4) x R(2)
    // 12 transpositions x 4 M forms (1,5,7,11) x 2 R forms (forward and retrograde)
    public static final int NUMBER_OF_TRANSFORMATIONS = 96;

    /**
     * @param inRow The Row used to create the List of transformations.
     * @return A List of Row objects each representing each of the transformations of Row using T[n], M[n], R.
     */
    public static List<ISequence> allTransformations(Row inRow) {
        List<ISequence> transformations = new ArrayList<>();
        Iterator<ImmutablePitchClassSequence> rowTransformIt = inRow.transformationIterator();
        while (rowTransformIt.hasNext()) {
            transformations.add(new Row(rowTransformIt.next()));
        }
        return transformations;
    }

    /**
     * @return Returns a random 12-tone Row. Just an alias for SequenceUtils.getRandomPitchClassSequence(12 , true).
     */
    public static Row randomRow() {
        return new Row(SequenceUtils.getRandomPitchClassSequence(12, true));
    }

    /**
     * Returns an Iterator that iterates through the whole-tone transformations of this.
     * The Whole-tone transformation in a transformation of P such that every even-numbered
     * (or odd-numbered) pc is increased by an even number mod 12, usually by 2.
     * For example, given the Row:
     * &lt;0 b 1 2 A 8 5 3 9 4 7 6&gt; , the first Whole-tone transformation is:
     * &lt;0 1 3 2 A 8 7 5 B 4 9 6&gt;
     * The Whole-tone transformations have interesting properties in relation to
     * each other and the original row.
     *
     * @param inRow The Row on which to perform the WholeTone transformations.
     * @return An Iterator that iterates through every transformation of this.
     */
    public Iterator<ImmutablePitchClassSequence> WholeToneTransformationIterator(Row inRow) {

        //final ImmutablePitchClassSequence thisSeq = new ImmutablePitchClassSequence( this.getMembers() );
        class TransformationIterator implements Iterator<ImmutablePitchClassSequence> {

            private int index = 0;

            public boolean hasNext() {
                // TODO Auto-generated method stub
                return (index <= 5);
            }

            public ImmutablePitchClassSequence next() {
                // TODO Auto-generated method stub
                StringBuilder transformationBuffer = new StringBuilder();
                Row outRow = new Row(inRow.W(index));
                transformationBuffer.append(String.format("W[%d]", index));
                outRow.setDescriptor(transformationBuffer.toString());
                index += 1;
                return outRow;
            }

            public void remove() {
                // TODO Auto-generated method stub

            }
        }
        return new TransformationIterator();
    }

    /**
     * getTransformationString( Row original, Row formToCompare )
     * returns the transformation string that would transform the Row formToCompare
     * into the Row original. If formToCompare is not a transformation of original,
     * the method will return null.
     * <p>
     * Examples:
     * original =  [0123456789ab]
     * formToCompare = [23456789ab01]
     * getTransformationString( original, formToCompare ) would return "T[2]"
     * <p>
     * original =  [0123456789ab]
     * formToCompare = [76543210ba98]
     * getTransformationString( original, formToCompare ) would return "T[8]I"
     * <p>
     * original =  [0123456789ab]
     * formToCompare = [7309b24a5861]
     * getTransformationString( original, formToCompare ) would return null
     * <p>
     * In the case that a transformation maps into more than 1 form of the row, it
     * will return the first one found
     *
     * @param original      The source row for the comparison.
     * @param formToCompare The destination row, i.e. the row that original will compare itself to.
     * @return A String representation of the PitchClassSequenceTransformation which will transform original into formToCompare.
     */
    public static String getTransformationString(Row original, Row formToCompare) {
        String transString = null;
        Iterator<ImmutablePitchClassSequence> transIt = original.transformationIterator();
        while (transIt.hasNext()) {
            Row row = new Row(transIt.next());
            if (row.equals(formToCompare)) {
                return row.getDescriptor();
            }
        }
        return transString;
    }

    /**
     * getTransformation( Row original, Row formToCompare )
     * returns the transformation that would transform the Row formToCompare
     * into the Row original. If formToCompare is not a transformation of original,
     * the method will return null.
     * <p>
     * Examples:
     * original =  [0123456789ab]
     * formToCompare = [23456789ab01]
     * getTransformationString( original, formToCompare ) would return new PitchClassSequenceTransformation( 2, false, false, 1 )
     * <p>
     * original =  [0123456789ab]
     * formToCompare = [76543210ba98]
     * getTransformationString( original, formToCompare ) would new PitchClassSequenceTransformation( 8, true, false, 1 )
     * <p>
     * original =  [0123456789ab]
     * formToCompare = [7309b24a5861]
     * getTransformationString( original, formToCompare ) would return null
     * <p>
     * In the case that a transformation maps into more than 1 form of the row, it
     * will return the first one found
     *
     * @param original      The first Row in the comparison.
     * @param formToCompare The second Row in the comparison.
     * @return The PitchClassSequenceTransformation that will transform original to formToCompare. Null if no transformation exists, i.e. the rows are not isomorphic.
     */
    public static PitchClassSequenceTransformation getTransformation(Row original, Row formToCompare) {
        PitchClassSequenceTransformation trans = null;
        Iterator<ImmutablePitchClassSequence> transIt = original.transformationIterator();
        while (transIt.hasNext()) {
            Row row = new Row(transIt.next());
            if (row.equals(formToCompare)) {
                return row.getTransformation();
            }
        }
        return trans;
    }

    /**
     * Returns a List of CompositionMatrix objects representing the hexachordally combinatorial forms of inRow.
     *
     * @param inRow The Row for which the combinatorial CMs will be produced.
     * @return A List of CompositionMatrix, where each CompositionMatrix is a hexachordally combination of inRow.
     */
    public static List<CompositionMatrix> combinatorialCMs(Row inRow) {
        List<CompositionMatrix> combinatorialCMs = new ArrayList<>();
        Iterator<ImmutablePitchClassSequence> rowTransformIt = inRow.transformationIterator();
        while (rowTransformIt.hasNext()) {
            Row nextRow = (Row) rowTransformIt.next();
            PitchClassSet firstPcSet = new PitchClassSet(inRow.subSequence(0, 6).getMembers());
            PitchClassSet secondPcSet = new PitchClassSet(nextRow.subSequence(6, 6).getMembers());
            if (firstPcSet.equals(secondPcSet)) {
                CompositionMatrix cm = new CompositionMatrix();
                cm.addSegment(inRow.subSequence(0, 6), 0, 0);
                cm.addSegment(inRow.subSequence(6, 6), 0, 1);
                cm.addSegment(nextRow.subSequence(0, 6), 1, 0);
                cm.addSegment(nextRow.subSequence(6, 6), 1, 1);
                combinatorialCMs.add(cm);
            }
        }
        return combinatorialCMs;
    }

    /**
     * Returns a Row transformed by PitchClassSequenceTransformation with signature index.
     *
     * @param inRow The Row on which to operate.
     * @param index The index of the transformation.
     * @return A Row transformed by the transformation described by index.
     */
    public static Row rowFromTransformationIndex(Row inRow, int index) {
        Row outRow;
        Integer[] mForms = {1, 11, 5, 7};
        index = index % RowUtils.NUMBER_OF_TRANSFORMATIONS;
        outRow = inRow.T(index % 12);
        index /= 12;
        outRow = outRow.M(mForms[index % 4]);
        index /= 4;
        if (index % 2 == 1) {
            outRow = outRow.R();
        }
        return outRow;
    }

    public static Iterator<Row> allRowFormsIterator() {
        class RowFormIterator implements Iterator<Row> {
            private boolean doneProcessing = false;
            private List<Integer> intList;
            private PermutationIterator permIt;
            Logger log;
            Map<String, PitchClassSet> hexachords;
            List<String> zHexachords;

            /*
            Some notes:
                Iterating row forms can be broken into separate tasks for each the 50 hexachord SetClasses.
            For any given PC Sequence, no transformation within the set of [T(n), M(11), R] will transform a
            sequence a one SetClass into another SetClass. Given this fact, we can divide the task of finding all the row forms into
            a set of 50 tasks, one for each hexachord SetClass. We can further divide the tasks into two categories, one
            for 'Z' hexachords, and one for non-Z hexachords.
                For 'Z' hexachords, no transformation of [T(n), M(11), R] will transform it into its complement.
            Given this postulate, we can calculate a Row form for a 'Z' hexachord by combining the 120 permutations of hexachord A (starting with 0),
            with the 720 permutations of hexachord B, giving 86400 Row forms for each of the 50 'Z' hexachords.
            There are (N=)30 Z-related hexachords. However, it is only necessary to calculate these permutation operations for the
            first (N/2=)15 of these. This is because, for each Row form with hexachord A in the set of 'Z' hexachords 1-15,
            its R() form is a unique form in the set of 'Z' hexachords 16-30, so "calculating" the Row forms for these hexachords
            can be replaced with injecting the R() form to the Iterator.
            For the remaining 20 non-'Z' hexachords, any sequence formed from the hexachord can be transformed into its complement
            using T(n), M(11) or R(). The Row forms will still be calculated using the permutations of the first hexachord and its
            complement. But in this case, the Iterator will need to store each of the forms and then check each permutation to check if
            it is equivalent under [T(n), M(11), R()] to a Row form already stored. If a permutation is not equivalent to an form in the list,
            it is added, otherwise, it is skipped and discarded.
                Rather than create a single iterator for both 'Z' and non-'Z' hexachords, I found it cleaner to create a separate Iterator for each.
            With separate Iterators, one can avoid using logic within the Iterator to identify the different hexachord types,
            potentially avoiding redundant logic branches eliminated by using a separate Class.

             */
            private RowFormIterator() {
                this.intList = new ArrayList<>();
                IntStream.rangeClosed(0, 11).forEach(n -> {
                    this.intList.add(n);
                });
                permIt = new PermutationIterator(intList);
                this.log = Logger.getLogger(this.getClass().getName());
                this.hexachords = PitchClassSetCatalog.getPitchClassSetsByCardinality(6);
                this.zHexachords.add("6-Z3");
                this.zHexachords.add("6-Z4");
                this.zHexachords.add("6-Z6");
                this.zHexachords.add("6-Z10");
                this.zHexachords.add("6-Z11");
                this.zHexachords.add("6-Z12");
                this.zHexachords.add("6-Z13");
                this.zHexachords.add("6-Z17");
                this.zHexachords.add("6-Z19");
                this.zHexachords.add("6-Z23");
                this.zHexachords.add("6-Z24");
                this.zHexachords.add("6-Z25");
                this.zHexachords.add("6-Z26");
                this.zHexachords.add("6-Z28");
                this.zHexachords.add("6-Z29");
            }

            private Iterator<Row> zRelatedRowFormIterator(PitchClassSet inSet) {

                class ZRowFormIterator implements Iterator<Row> {

                    private PermutationIterator<Integer> firstHexachordIt;
                    private PermutationIterator secondHexachordIt;
                    private Row lastRow;
                    private Integer index;

                    public ZRowFormIterator(PitchClassSet inSet) {
                        //PitchClassSequence firstHexachordSeq = SetTheoryFactories.getPitchClassSequence( inSet.getMembers() );
                        this.firstHexachordIt = new PermutationIterator<Integer>(inSet.getMembers().subList(1, inSet.length() - 1));
                        this.secondHexachordIt = new PermutationIterator<Integer>(inSet.complement().getMembers());
                        this.index = 0;
                    }

                    @Override
                    public boolean hasNext() {
                        if (index % 2 == 0) {
                            return true;
                        } else {
                            return firstHexachordIt.hasNext() || secondHexachordIt.hasNext();
                        }
                    }

                    public Row next() {
                        List<Integer> firstHexachord = firstHexachordIt.next();
                        firstHexachord.add(0, 0);
                        List<Integer> secondHexachord = secondHexachordIt.next();
                        if (!firstHexachord.addAll(secondHexachord)) {
                            throw new IllegalStateException("Unable to add contents of Second Hexachord to First Hexachord");
                        }
                        Row zRow = SetTheoryFactories.getRow(firstHexachord);
                        this.lastRow = zRow;
                        index++;
                        return zRow;
                    }

                }
                return new ZRowFormIterator(inSet);
            }

            public boolean hasNext() {
                // TODO Auto-generated method stub
                return permIt.hasNext();
                //return ( ! doneProcessing );
            }

            public Row next() {
                // TODO Auto-generated method stub
                Map<String, PitchClassSet> hexachords = PitchClassSetCatalog.getPitchClassSetsByCardinality(6);
                Map<String, String> zPairs = PitchClassSetCatalog.zRelatedPairs(PitchClassSetCatalog.FORTE);
                List<Integer> pitches = permIt.next();
                //log.info( "Pitch list for Row: " + pitches);
                Row outRow = SetTheoryFactories.row(pitches);
                return outRow;
            }

            public void remove() {
                // TODO Auto-generated method stub
            }
        }
        return new RowFormIterator();
    }

    /**
     * Returns a canonical row matrix with the T0 form in the first row and the
     * T(0)I form in the first column. Each subsequent row is the T(n) form with n
     * equal to each member of T(0)I. Each CMSegment contains exactly one PC.
     *
     * @param inSeq The Row on which to operate.
     * @return A canonical row matrix.
     */
    public static CompositionMatrix getMatrix(Row inSeq) {
        CompositionMatrix matrixCM = new CompositionMatrix();
        int rowIndex = 0;
        for (int trans : inSeq.I().transposeTo(0).getMembers()) {
            int colIndex = 0;
            for (int member : inSeq.T(trans).getMembers()) {
                PitchClassSequence pcSeq = new PitchClassSequence();
                pcSeq.addPitch(member);
                matrixCM.addSegment(pcSeq, rowIndex, colIndex++);
            }
            rowIndex++;
        }
        return matrixCM;
    }


    /**
     * Static method getCombinorialCMs calculates exhaustively the combinatorial CMs for inRow, where each
     * column and row form aggregates with no repeated pcs. Combinatoriality here is not limited to the
     * common patterns of hexachordal, tetrachordal or trichordal divisions where each cell contains the same
     * number of pcs. All Row forms are included, including M transformations.
     * For example, given the row: 0 b 1 2 a 8 5 3 9 4 7 6
     * Some combinatorial CMs for 3 rows include this even tetrachordal combination:
     * 0 b 1 2 | a 8 5 3 | 9 4 7 6
     * 4 3 5 6 | 2 0 9 7 | 1 8 b a
     * 8 7 9 a | 6 4 1 b | 5 0 3 2
     * <p>
     * But also this unevenly shaped CM:
     * 0 b 1 2 a   | 8 5 3       | 9 4 7 6
     * 7 6 8 9 5 3 | 0 a 4       | b 2 1
     * 4           | 9 6 7 1 b 2 | 0 8 3 5 a
     * As well as this one, which contains empty cells:
     * 0 b 1 2 a     | 8 5 3     | 9 4 7 6
     * 6 7 4 9 3 5 8 | a 2 1 b   | 0
     * | 9 4 7 6 0 | 2 b 1 5 a 8 3
     * <p>
     * This calculation can be expensive and is know to have long running times for values of maxSize &gt; 3.
     *
     * @param inRow           The Row used to form the combinatorial CMs
     * @param partSize        The partition size. Should always be 12.
     * @param maxSize         The dimensions of the CMs. All CMs returned will be a maxSize x maxSize CM.
     * @param maxEmptySlots   The maximum number of empty slots per column or row. Passed the same value as maxSize, each row or column will have no more than one empty slot.
     * @param maxCombinations The maximum number of maxSize x maxSize layouts to generate for searching. Large numbers will greatly increase run times. Try starting with 1000 and see.
     * @return A List&lt;CompositionMatrix&gt; of combinatorial CMs calculated for inRow.
     */
    public static List<CompositionMatrix> getCombinorialCMs(Row inRow, int partSize, int maxSize, int maxEmptySlots, int maxCombinations) {
        CopyOnWriteArrayList<CompositionMatrix> compositionCMs = new CopyOnWriteArrayList<>();
        final class BreakException extends Exception {
            public BreakException() {
                super();
            }
        }
        /*
         * The list of row transformations is static, so we can calculate once and store it here.
         */
        List<ISequence> rowTransformations = RowUtils.allTransformations(inRow);
        if (rowTransformations.size() == 0) {
            return compositionCMs;
        }
        /*
         * Remove the first transformation, which is T[0] P, and store it as 'prime'.
         * We will take the combinations of length 'maxSize' of the remaining transformations,
         * which is now the list rowTransformations (with length 95). In each test, we will add 'prime'
         * back to the List of (maxSize - 1) Rows at index 0. This allows us to test only CMs
         * with the Prime form at Row 0, reducing the number of tests exponentially. The CM class
         * has methods to reconstruct any transformation.
         */
        ISequence prime = rowTransformations.remove(0);
        List<List<Integer>> compositions = new ArrayList<>();
        Generator.partition(partSize).
                stream().
                filter(part -> part.size() <= maxSize).
                forEach(part -> {
                    while (part.size() < maxSize) {
                        part.add(0);
                    }
                    Generator.permutation(part).
                            simple().
                            stream().
                            forEach(p -> compositions.add(p));
                });
		/*
		List<List<List<Integer>>> allCombinations = Generator.combination(compositions).
				multi(maxSize).
				stream().
				filter( comb -> 
						(RowUtils.emptyColumnCount(comb) <= maxEmptySlots) && RowUtils.isOrthogonal( comb, partSize )
				).
				collect(Collectors.toList());
		*/
        /*
        Generator.combination(compositions).
                multi(maxSize).
                stream().
                filter(combination ->
                        (RowUtils.emptyColumnCount(combination) <= maxEmptySlots) && RowUtils.isOrthogonal(combination, partSize)
                ).map(comb -> (List<List<Integer>>) comb).forEach(comb -> {
            if (compositionCMs.size() >= maxCombinations) {
                return;
            }
            Generator.combination(rowTransformations).
                    simple(maxSize - 1).
                    stream().
                    forEach(rowComb -> {
                        rowComb.add(0, prime);
                        CompositionMatrix combCM = CompositionMatrixUtils.getCompositionMatrix(
                                rowComb, comb
                        );
                        if (combCM.isCanonical(false, true)) {
                            //System.out.println("++++++++++++++++++++");
                            //System.out.println(comb);
                            //System.out.println( CompositionMatrixUtils.format(combCM, " ", "|", true) );
                            compositionCMs.add(combCM);
                        }
                    });
        });
        */

        for (List<List<Integer>> compositionCombination : Generator.combination(compositions).multi(maxSize - 1)) {
            Integer[] completionArray = new Integer[compositionCombination.size()];
            int bound = compositionCombination.size();
            for (int i = 0; i < bound; i++) {
                System.out.println(compositionCombination);
                int rowIndex = i;
                int bound1 = compositionCombination.get(rowIndex).size();
                for (int columnIndex = 0; columnIndex < bound1; columnIndex++) {
                    System.out.println(String.format("rowIndex = %d, columnIndex = %d", rowIndex, columnIndex));
                    completionArray[columnIndex] += compositionCombination.get(rowIndex).get(columnIndex);
                }
            }
            System.out.println("NPE is below");
            List<Integer> completionList = Arrays.asList(completionArray);
            if (completionList.stream().mapToInt(Integer::intValue).sum() == partSize) {
                Generator.combination(rowTransformations).
                        simple(maxSize - 1).
                        stream().
                        forEach(rowComb -> {
                            rowComb.add(0, prime);
                            CompositionMatrix combCM = CompositionMatrixUtils.getCompositionMatrix(
                                    rowComb, compositionCombination
                            );
                            if (combCM.isCanonical(false, true)) {
                                //System.out.println("++++++++++++++++++++");
                                //System.out.println(comb);
                                //System.out.println( CompositionMatrixUtils.format(combCM, " ", "|", true) );
                                compositionCMs.add(combCM);
                            }
                        });
            }
        }
        //long emptySlots = 0;
		/*
		allCombinations.
			parallelStream().
			forEach( comb -> {
				if ( compositionCMs.size() >= maxCombinations ) {
					return;
				}
				Generator.combination(rowTransformations).
				simple(maxSize - 1).
				stream().
				forEach( rowComb -> {
					rowComb.add(0, prime);
					CompositionMatrix combCM = CompositionMatrixUtils.getCompositionMatrix(
							rowComb, comb
					);
					if ( combCM.isCanonical( false, true ) ) {
						//System.out.println("++++++++++++++++++++");
						//System.out.println(comb);
						//System.out.println( CompositionMatrixUtils.format(combCM, " ", "|", true) );
						compositionCMs.add(combCM);
					}
				});
			});
			*/
        return compositionCMs;
    }

    /**
     * Static method getCombinorialCMs calculates exhaustively the combinatorial CMs for inRow, where each
     * column and row form aggregates with no repeated pcs. Combinatoriality here is not limited to the
     * common patterns of hexachordal, tetrachordal or trichordal divisions where each cell contains the same
     * number of pcs. All Row forms are included, including M transformations.
     * For example, given the row: 0 b 1 2 a 8 5 3 9 4 7 6
     * Some combinatorial CMs for 3 rows include this even tetrachordal combination:
     * 0 b 1 2 | a 8 5 3 | 9 4 7 6
     * 4 3 5 6 | 2 0 9 7 | 1 8 b a
     * 8 7 9 a | 6 4 1 b | 5 0 3 2
     * <p>
     * But also this unevenly shaped CM:
     * 0 b 1 2 a   | 8 5 3       | 9 4 7 6
     * 7 6 8 9 5 3 | 0 a 4       | b 2 1
     * 4           | 9 6 7 1 b 2 | 0 8 3 5 a
     * As well as this one, which contains empty cells:
     * 0 b 1 2 a     | 8 5 3     | 9 4 7 6
     * 6 7 4 9 3 5 8 | a 2 1 b   | 0
     * | 9 4 7 6 0 | 2 b 1 5 a 8 3
     * <p>
     * This calculation can be expensive and is know to have long running times for values of maxSize &gt; 3.
     *
     * @param inRow         The Row used to form the combinatorial CMs
     * @param partSize      The partition size. Should always be 12.
     * @param maxSize       The dimensions of the CMs. All CMs returned will be a maxSize x maxSize CM.
     * @param maxEmptySlots The maximum number of empty slots per column or row. Passed the same value as maxSize, each row or column will have no more than one empty slot.
     * @return A List&lt;CompositionMatrix&gt; of combinatorial CMs calculated for inRow.
     */
    public static List<CompositionMatrix> getCombinorialCMs(Row inRow, int partSize, int maxSize, int maxEmptySlots) {
        CopyOnWriteArrayList<CompositionMatrix> compositionCMs = new CopyOnWriteArrayList<CompositionMatrix>();
        final class BreakException extends Exception {
            public BreakException() {
                super();
            }
        }
        /*
         * The list of row transformations is static, so we can calculate once and store it here.
         */
        List<ISequence> rowTransformations = RowUtils.allTransformations(inRow);
        if (rowTransformations.size() == 0) {
            return compositionCMs;
        }
        /*
         * Remove the first transformation, which is T[0] P, and store it as 'prime'.
         * We will take the combinations of length 'maxSize' of the remaining transformations,
         * which is now the list rowTransformations (with length 95). In each test, we will add 'prime'
         * back to the List of (maxSize - 1) Rows at index 0. This allows us to test only CMs
         * with the Prime form at Row 0, reducing the number of tests exponentially. The CM class
         * has methods to reconstruct any transformation.
         */
        ISequence prime = rowTransformations.remove(0);
        List<List<Integer>> compositions = new ArrayList<>();
        Generator.partition(partSize).
                stream().
                filter(part -> part.size() <= maxSize).
                forEach(part -> {
                    while (part.size() < maxSize) {
                        part.add(0);
                    }
                    Generator.permutation(part).
                            simple().
                            stream().
                            forEach(p -> compositions.add(p));
                });
        List<List<List<Integer>>> allCombinations = Generator.combination(compositions).
                multi(maxSize).
                stream().
                filter(comb ->
                        (RowUtils.emptyColumnCount(comb) <= maxEmptySlots) && RowUtils.isOrthogonal(comb, partSize)
                ).
                collect(Collectors.toList());
        long emptySlots = 0;
        allCombinations.
                parallelStream().
                forEach(comb -> {
                    System.out.println(comb);
                    Generator.combination(rowTransformations).
                            simple(maxSize - 1).
                            stream().
                            forEach(rowComb -> {
                                rowComb.add(0, prime);
                                CompositionMatrix combCM = CompositionMatrixUtils.getCompositionMatrix(
                                        rowComb, comb
                                );
                                if (combCM.isCanonical(false, true)) {
                                    //System.out.println("++++++++++++++++++++");
                                    //System.out.println(comb);
                                    //System.out.println( CompositionMatrixUtils.format(combCM, " ", "|", true) );
                                    compositionCMs.add(combCM);
                                }
                            });
                });
        return compositionCMs;
    }

    /**
     * Returns the number of elements in the List which are equal to zero.
     *
     * @param inList A List of List&lt;Integer&gt; to examine.
     * @return An int representing the number of elements equal to zero.
     **/
    private static long emptyColumnCount(List<List<Integer>> inList) {
        long numEmptyColumns = 0;
        for (List<Integer> row : inList) {
            for (int item : row) {
                if (item == 0) {
                    numEmptyColumns++;
                }
            }
        }
        return numEmptyColumns;
    }

    /**
     * Returns true if all rows and columns in inList sum to expectedSum
     * and all rows are the same length and all columns are the same length.
     *
     * @param inList      A List of List&lt;Integer&gt; to examine.
     * @param expectedSum The sum for which tests will return true.
     **/
    private static boolean isOrthogonal(List<List<Integer>> inList, int expectedSum) {
        //boolean orthogonal = true;
        if (inList.size() == 0) {
            return true;
        }
        int rowCount = inList.get(0).size();
        for (int i = 0; i < inList.size(); i++) {
            if (inList.get(i).size() != rowCount) {
                return false;
            }
            final int index = i;
            int colSum = inList.stream().
                    map(p -> p.get(index)).
                    collect(Collectors.toList()).
                    stream().
                    mapToInt(Integer::intValue).
                    reduce(0, (a, b) -> a + b);
            if (colSum != expectedSum) {
                return false;
            }

        }
        return true;
    }

}