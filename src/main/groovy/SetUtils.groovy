import com.newscores.setTheory.*
import com.newscores.setTheory.utils.*

import java.util.*

// Row types
// The Row types is a special form of the PitchClassSequence.
// Attempting to create a Row with either duplicated or missing PCs will throw an error, guaranteeing
// that the Row is valid.

// The Row for the Schoenberg Violin Concerto
schvcRow = new Row( [ 9,10,3,11,4,6,0,1,7,8,2,5 ] )
println schvcRow

set1 = new PitchClassSet("013")

set2 = new PitchClassSet("24678")

utils = new SetUtils()
println utils.getMaxIntersection( set1, set2 )