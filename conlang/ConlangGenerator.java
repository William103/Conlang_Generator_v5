package conlang;

import java.io.*;
import java.util.*;
class ConlangGenerator {
		// Make sure eveything uses the same seed so I can come back to ones I like, or keep the seeds of grammars or phonologies I like
    public static long seed = System.nanoTime();
    public static void main(String[] args) {
				// Instantiate all the classes, except Lexicon, which comes later
        Phonology phonology = new Phonology();
        Grammar grammar = new Grammar(phonology);
        Scanner scan = new Scanner(System.in);

        grammar.createEndings(phonology);
        grammar.createDerivationalMorphology(phonology);
				grammar.createPronouns(phonology);

				// Just outputting everything to the console
        System.out.println(phonology.newName + ": \n\n");
        System.out.println("Phonology: \n");
        System.out.println("Consonants:");
        for (Consonant consonant : phonology.consonantInventory) {
            System.out.print(consonant.rep + ", ");
        }
        System.out.println("\nVowels: ");
        for (Vowel vowel : phonology.vowelInventory) {System.out.print(vowel.rep + ", ");}
				for (Vowel[] diphthong : phonology.diphthongs) {System.out.println("" + diphthong[0].rep + diphthong[1].rep + ", ");}
        System.out.println("\nPhonotactics:");
        for (int i = 0; i < phonology.initialConsonants; i++) {System.out.print("(C)");}
        System.out.print('V');
        for (int i = 0; i < phonology.finalConsonants; i++) {System.out.print("(C)");}
        System.out.println("\nPossible Initial Clusters:");
        ArrayList<String> used = new ArrayList<>();
        for (String cluster : phonology.initialClusters) {
            if (cluster.length() > 0 && !(used.contains(cluster))) {
                for (char consonant : cluster.toCharArray()) {
                    System.out.print(consonant);
                }
                used.add(cluster);
                System.out.print(", ");
            }
        }
        used.clear();
        System.out.println("\nPossible Medial Clusters:");
        for (String cluster : phonology.medialClusters) {
            if (cluster.length() > 0 && !(used.contains(cluster))) {
                for (char consonant : cluster.toCharArray()) {
                    System.out.print(consonant);
                }
                used.add(cluster);
                System.out.print(", ");
            }
        }
        used.clear();
        System.out.println("\nPossible Final Clusters:");
        for (String cluster : phonology.finalClusters) {
            if (cluster.length() > 0 && !(used.contains(cluster))) {
                for (char consonant : cluster.toCharArray()) {
                    System.out.print(consonant);
                }
                used.add(cluster);
                System.out.print(", ");
            }
        }
        System.out.println();

        int q = 0;
        System.out.println("\n\nNoun Endings:\n");
        for (int i = 0; i < grammar.cases.size(); i++) {
            for (int j = 0; j < grammar.numbers.size(); j++) {
                System.out.println(grammar.numbers.get(j) + ", " + grammar.cases.get(i) + ": " + grammar.nounEndings.get(q));
                q++;
            }
        }

				System.out.println("\n\nPronouns:\n");
				q = 0;
				for (int i = 0; i < grammar.cases.size(); i++) {
            for (int j = 0; j < grammar.numbers.size(); j++) {
								for (int k = 0; k < 	grammar.persons.size(); k++) {
                	System.out.println(grammar.numbers.get(j) + ", " + grammar.cases.get(i) + ", " + grammar.persons.get(k) + ": " + grammar.pronouns.get(q));
                q++;
								}
            }
        }

        int n = 0;
        System.out.println("\n\nVerb Endings:\n");
        System.out.println("Infinitive: " + grammar.infinitive + "\n");
        for (int i = 0; i < grammar.aspects.size(); i++) {
            for (int j = 0; j < grammar.moods.size(); j++) {
                for (int k = 0; k < grammar.numbers.size(); k++) {
                    for (int l = 0; l < grammar.persons.size(); l++) {
                        for (int m = 0; m < grammar.tenses.size(); m++) {
                            System.out.print(grammar.tenses.get(m) + ", ");
                            System.out.print(grammar.persons.get(l) + ", ");
                            System.out.print(grammar.numbers.get(k) + ", ");
                            System.out.print(grammar.moods.get(j) + ", ");
                            System.out.print(grammar.aspects.get(i) + ": ");
                            System.out.print(grammar.verbEndings.get(n));
                            System.out.println();
                            n++;
                        }
                    }
                }
            }

        }

        System.out.println("\n\nDerivational Morphology:\n");
        for (String key1 : grammar.derivationalMorphology.keySet()) {
            System.out.print(key1 + ": ");
            System.out.print(grammar.derivationalMorphology.get(key1));
            System.out.println();
        }

        System.out.println("\n\nSyntax:\n");
        for (String element : grammar.wordOrder) {
            System.out.print(element + ", ");
        }
        System.out.println();
        if (grammar.headFinal) {System.out.println("Head Final");} else {System.out.println("Head First");}

        // The Lexicon class throws IOException, so we have to be careful about instantiating it, and Translation uses Lexicon, so that goes here too.
        try {
            Lexicon words = new Lexicon(phonology);
            Translation translation = new Translation(words, grammar);
            words.write(phonology, grammar, translation);
            System.out.println("\nSample sentence: " + translation.newSentence);
            System.out.println("\nEnglish: " + translation.sentence);
            System.out.println("\nLiterally: " + translation.literalSentence);
            translation.writeSentence(phonology);
            String input = scan.next();
            while (true) {
                System.out.println(words.lexicon.get(input));
                input = scan.next();
                if (input == "STOP") {break;}
            }
            scan.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            scan.close();
        }
    }
}
