package conlang;

import java.util.*;
import java.io.*;
class Lexicon {
		// Iterates through the massive dictionary adding the English term and the made up term to a HashMap. It also keeps track of the order the words were in, so the dicionary still ends up lemmatized.
    private static final String dictionaryFile = "dictionary.txt";
    private static final String outputFile = "output.txt";
    PrintWriter out = null;
    HashMap<String, String> lexicon = new HashMap<String, String>();
    ArrayList<String> englishWords = new ArrayList<String>();
    public Lexicon(Phonology phonology) throws IOException{
        BufferedReader in = null;
        Random rand = new Random(ConlangGenerator.seed);
        try {
            in = new BufferedReader(new FileReader(dictionaryFile));
            String word = new String(in.readLine());
            while (word != null) {
                lexicon.put(word, phonology.generateWord(word.length() / 2 + rand.nextInt(3)));
                // lexicon.put(word, phonology.generateWord(word.length() / 4 + rand.nextInt(3)));
                englishWords.add(word);
                word = in.readLine();
            }
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    void write(Phonology phonology, Grammar grammar, Translation translation) {
				// Exactly the same as the main method, just prints it out to a file
        try {
            out = new PrintWriter(outputFile, "utf-8");
            out.println("Seed: " + ConlangGenerator.seed);
            out.println();
            out.println("A Complete Grammar and Dictionary of " + phonology.name + ": ");
            out.println();
            out.println();
            out.println("Phonology: ");
            out.println();
            out.println("Consonants:");
            for (Consonant consonant : phonology.consonantInventory) {
                out.print(consonant.rep + ", ");
            }
            out.println();
            out.println("Vowels: ");
            for (Vowel vowel : phonology.vowelInventory) {out.print(vowel.rep + ", ");}
						for (Vowel[] diphthong : phonology.diphthongs) {out.println("" + diphthong[0].rep + diphthong[1].rep + ", ");}
            out.println();
            out.println("Phonotactics:");
            for (int i = 0; i < phonology.initialConsonants; i++) {out.print("(C)");}
            out.print('V');
            for (int i = 0; i < phonology.finalConsonants; i++) {out.print("(C)");}
            out.println();
            out.println("Possible Initial Clusters:");
            ArrayList<String> used = new ArrayList<>();
            for (String cluster : phonology.initialClusters) {
                if (cluster.length() > 0 && !(used.contains(cluster))) {
                    for (char consonant : cluster.toCharArray()) {
                        out.print(consonant);
                    }
                    used.add(cluster);
                    out.print(", ");
                }
            }
            used.clear();
            out.println();
            out.println("Possible Medial Clusters:");
            for (String cluster : phonology.medialClusters) {
                if (cluster.length() > 0 && !(used.contains(cluster))) {
                    for (char consonant : cluster.toCharArray()) {
                        out.print(consonant);
                    }
                    used.add(cluster);
                    out.print(", ");
                }
            }
            used.clear();
            out.println();
            out.println("Possible Final Clusters:");
            for (String cluster : phonology.finalClusters) {
                if (cluster.length() > 0 && !(used.contains(cluster))) {
                    for (char consonant : cluster.toCharArray()) {
                        out.print(consonant);
                    }
                    used.add(cluster);
                    out.print(", ");
                }
            }
            out.println();

            int q = 0;
            out.println();
            out.println();
            out.println("Noun Endings:");
            out.println();
            for (int i = 0; i < grammar.cases.size(); i++) {
                for (int j = 0; j < grammar.numbers.size(); j++) {
                    out.println(grammar.numbers.get(j) + ", " + grammar.cases.get(i) + ": " + grammar.nounEndings.get(q));
                    q++;
                }
            }

						q = 0;
            out.println();
            out.println();
            out.println("Pronouns:");
            out.println();
            for (int i = 0; i < grammar.cases.size(); i++) {
                for (int j = 0; j < grammar.numbers.size(); j++) {
                    out.println(grammar.numbers.get(j) + ", " + grammar.cases.get(i) + ": " + grammar.pronouns.get(q));
                    q++;
                }
            }

            int n = 0;
            out.println();
            out.println();
            out.println("Verb Endings:");
            out.println();
            out.println("Infinitive: " + grammar.infinitive);
            out.println();
            for (int i = 0; i < grammar.aspects.size(); i++) {
                for (int j = 0; j < grammar.moods.size(); j++) {
                    for (int k = 0; k < grammar.numbers.size(); k++) {
                        for (int l = 0; l < grammar.persons.size(); l++) {
                            for (int m = 0; m < grammar.tenses.size(); m++) {
                                out.print(grammar.tenses.get(m) + ", ");
                                out.print(grammar.persons.get(l) + ", ");
                                out.print(grammar.numbers.get(k) + ", ");
                                out.print(grammar.moods.get(j) + ", ");
                                out.print(grammar.aspects.get(i) + ": ");
                                out.print(grammar.verbEndings.get(n));
                                out.println();
                                n++;
                            }
                        }
                    }
                }

            }

            out.println();
            out.println();
            out.println("Derivational Morphology:");
            out.println();
            for (String key1 : grammar.derivationalMorphology.keySet()) {
                out.print(key1 + ": ");
                out.print(grammar.derivationalMorphology.get(key1));
                out.println();
            }

            out.println();
            out.println();
            out.println("Syntax:");
            out.println();
            for (String element : grammar.wordOrder) {
                out.print(element + ", ");
            }
            out.println();
            if (grammar.headFinal) {out.println("Head Final");} else {out.println("Head First");}

            out.println();
            out.println();
            out.println("English: " + translation.sentence);
            out.println();
            out.println(phonology.name + ": " + translation.newSentence);
            out.println();
            out.println("Literally: " + translation.literalSentence);
            out.println();
            out.println();
            out.println("Lexicon:");
            out.println();
            for (String englishWord : englishWords) {
                out.println(englishWord + ": " + lexicon.get(englishWord));
            }
            out.close();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (out != null) {out.close();}
        }

    }
}
