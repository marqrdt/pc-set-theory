import java.util.regex.*
import org.apache.commons.lang3.StringUtils

String transformationPatternString = "(R|T\\[*[0-9]+\\]*|M\\[*[0-9]+\\]*|I)"
//String transPatternString = "(R)|(T\\[[0-9+]\\])|(M\\[[0-9+]\\])|(I)"
String transposePatternString = "T\\[*([0-9]+)\\]"
String multPatternString = "M\\[*([0-9]+)\\]"

Pattern transformationPattern = Pattern.compile( transformationPatternString, Pattern.CASE_INSENSITIVE)

String testString = " RT[2] M[5] RT[3]M[11]I"
//testString = " RT[2] M[5] RT[3]M[11]I"
//testString = " R"

Matcher patternMatcher = transformationPattern.matcher(testString)

int groupCount = 1
println "Found ${patternMatcher.groupCount()} groups"
def remaining = testString
//while ( groupCount <= patternMatcher.groupCount() ) {
def hasMatch = patternMatcher.find()
//def token = ""
while ( hasMatch ) {
    //if ( patternMatcher.group(groupCount) != null ) {
    String token = patternMatcher.group(1)
	if ( token != null ) {
		println "group ${groupCount}: ${token}"
		remaining = StringUtils.strip( remaining.substring( token.length() ) )
		Pattern transposePattern = Pattern.compile(transposePatternString, Pattern.CASE_INSENSITIVE)
		Matcher transposeMatcher = transposePattern.matcher(token)
		Pattern multPattern = Pattern.compile(multPatternString, Pattern.CASE_INSENSITIVE)
		Matcher multMatcher = multPattern.matcher(token)
		if ( transposeMatcher.find() ) {
			Integer t = Integer.valueOf( transposeMatcher.group(1) )
			println "token ${token} is a transpositon token at T${t}"
		} else if ( multMatcher.find() ) {
			Integer m = Integer.valueOf( multMatcher.group(1) )
			println "token ${token} is a mult token at M${m}"
		}
	} 
	else if ( remaining.length() > 0) {
		remaining = StringUtils.strip( remaining.substring( 1 ) )
	}
	println "remaining string is ${remaining}"
    patternMatcher = transformationPattern.matcher(remaining)
    //patternMatcher.reset(remaining)
    hasMatch = patternMatcher.lookingAt()
	if ( remaining.length() == 0) {
		hasMatch = false
	}
}
