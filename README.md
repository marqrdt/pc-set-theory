# README #

The mission of this project is to provide a consistent, intuitive and easily-consumed software library that models many of the essential concepts and objects in Musical Set Theory as described in the works of theorists such as Robert Morris and Daniel Starr. The values of this library extends to pure Java applications as well as JVM-based languages such as Jython, Scala, Groovy, Kotlin, and Clojure. Object creation factories offer shortened, DSL-friendly creation methods, as illustrated in the sample scripts.

This framework models the basic components of Musical Set Theory, including *PitchSet*, *PitchClassSet*, *PitchSequence*, *PitchClassSequence*, *Row*, *CompositionMatrix* (CM), *Contour*, and *Scale*. Sample programs Groovy scripts are provided.

Here is a basic walk through of the framework

*PitchClassSet* :: The PitchClassSet (PCSet) is defined in the literature as an unordered collection of pitch classes without regard to vertical or horizontal ordering or spacing. In mathematical terms, it is a Set whose members include elements of the 12-note chromatic scale, without regard to octave/registral assingment. The members of a PitchClassSet will consist of 0 <= n >= 12 unique elements of the integers from 0 to 11. A common example would be the Major chord, where two instances, for example the C Major chord and D-flat Major chord, share no common notes, but consist of the same "sound" and are members of the same "class". The PCSet extends this concept to any group of notes, such as the octatonic or pentatonic scale or the collection consisting of the notes D,A and E. Neither of these three collections is equivalent to the other, but subsets of each can be found in the other. A robust model of the PCSet will allow users to create instances of them and transpose, invert and analyze their relationship to other PCSets using a well-defined interface.

*PitchSet* :: The PitchSet (PSet) is a more specific form of the PCSet. It consists of a set of pitches fixed in register or vertical space, but with no regard to horizontal or temporal ordering. A common definition and useful model would be a "closed-voicing, root position Major chord", which retains its identity if transposed, but loses its identity if inverted in the classical sense of rotating the vertical ordering of the notes (root position, 1st inversion, etc), or if expanded to an open voicing. Using a well-defined interface, we can transpose, invert and perform other operations on them. In addition, we can inspect their nature as PCSets (see above). For example, it would be quite simple to determine of a PCSet was a Major chord regardless of its transposition or vertical distribution.

*PitchClassSequence* :: The PitchClassSequence (PCSeq) is similar to the definition of PCSet; however, the concept of order is injected, such that an element of a PCSeq can be considered to come before of after another element in a PCSeq. In this class, we add operations on order such as R() (retrograde) and interpolation (injecting another PCSeq as a specific location into another PCSeq). Again, a well-designed interface will allow one to reduce a PCSeq into a PSet or PCSet.

*PitchSequence* :: The PitchSequence (PSeq) is similar to the definition of PSet; however, the concept of order is injected, such that an element of a PSeq can be considered to come before of after another element in a PSeq. In this class, we add operations on order such as R() (retrograde) and interpolation (injecting another PSeq as a specific location into another PSeq). Again, a well-designed interface will allow one to create a PCSet or PSet from a PSeq using a single operation.

*Row* :: The Row is a subclass of PitchClassSequence which models a twelve-tone row. Its element are not modifiable after creation and it enforces membership as an ordering of the 12 pitch classes, i.e. not containing duplicates or omissions. It can be created from an ISequece. If the model ISequence does not enforce the contract of a twelve-tone row, an IllegalArgumentException is thrown.

*CompositionMatrix* :: The CompositionMatrix is a vertical and horizontal composition of ISequence objects. CM's can be created from an initial array of ISequence, or can be created as an empty CM, where members can be added at specific row and column coordinates. The CM does not need to be "filled in", and there can be empty slots. CMs offer T(), M(), I() and R() operations. The R() operation reverses the order each row, but does not perform the R() operation on the member ISequences. Each row and column can be returned as an ISequence or IPitchCollection, allowing the user to inspect the CM rows and columns using those interfaces.

*Contour* :: The Contour is an abstract shape in time and pitch space, in a sense, a abstract melodic shape. The Counter offers T(), I(), R() and M() operations. It offers methods to create a concrete PSeq from a PCSeq.

*Scale* :: The Scale, like the Contour, is an abstract shape in time and pitch space, in a sense, a abstract musical scale. It is distinguished from a Contour by the fact that its elements are arranged in ascending or descending order. There are methods for create a PSeq from a Contour and starting element. It is under development and not all possibilities are explored.


### What is this repository for? ###

* This repository is for developers who are interested [ Musical Pitch Set Theory ](https://en.wikipedia.org/wiki/Set_theory_(music))

### How do I get set up? ###

* The Set Theory project uses Maven as a build tool.
* I try to keep dependencies at a minimum. Currently, it uses only junit and commons-lang
* Dependencies are managed my Maven and there should be no need to manage them manually.

### How do I use these libraries? ###

* You do not need the Java code to use the library. In your build tool (Gradle, Maven, Leningen, etc), just add the depdendency for com.newscores.setTheory.
* Browse the [ Maven repository](https://search.maven.org/) for the latest version. Javadocs are available [here](http://newscores-public-web.s3-website-us-east-1.amazonaws.com/overview-summary.html).
* A build.gradle and sample Groovy scripts are included in src/main/groovy.

### Contribution guidelines ###

* All new functionality must be accompanied by unit tests

### Who do I talk to? ###

* Paul Marquardt (marqrdt at gmail)

### License Information ###
* Copyright [2016] [Paul Marquardt]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

  