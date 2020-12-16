package conlang;

import java.util.*;
class Grammar {
    ArrayList<String> pronouns = new ArrayList<String>();
    Random rand = new Random(ConlangGenerator.seed);
    ArrayList<String> cases = new ArrayList<String>();
    boolean isErgative = rand.nextInt(5) == 0;
    boolean headFinal = rand.nextBoolean();
    boolean inflectsParticiples = rand.nextBoolean();
    ArrayList<String> wordOrder = new ArrayList<String>();
    ArrayList<String> partsOfSpeech = new ArrayList<String>();
    HashMap<String, String> derivationalMorphology = new HashMap<String, String>();
    ArrayList<String> moods = new ArrayList<String>();
    ArrayList<String> tenses = new ArrayList<String>();
    ArrayList<String> aspects = new ArrayList<String>();
    ArrayList<String> persons = new ArrayList<String>();
    ArrayList<String> numbers = new ArrayList<String>();
    ArrayList<String> nounEndings = new ArrayList<String>();
    ArrayList<String> verbEndings = new ArrayList<String>();
    String infinitive;

    public Grammar(Phonology phonology) {
        // Deciding the word order by literally shuffling it
        wordOrder.add("Subject");
        wordOrder.add("Verb");
        wordOrder.add("Object");
        wordOrder.add("Complement");
        Collections.shuffle(wordOrder, rand);

        // Setting up all the grammar stuff so I can cut it later
        if (isErgative) {
            cases.add("ergative");
            cases.add("absolutive");
        } else {
            cases.add("nominative");
            cases.add("accusative");
        }
        cases.add("genitive");
        cases.add("dative");
        cases.add("ablative");
        cases.add("vocative");
        cases.add("prepositional");
        cases.add("comitative");
        cases.add("instrumental");
        cases.add("locative");
        cases.add("addessive");
        cases.add("allative");

        tenses.add("present");
        tenses.add("past");
        tenses.add("future");
        tenses.add("recent past");
        tenses.add("near future");

        moods.add("indicative");
        moods.add("imperative");
        moods.add("interrogative");
        moods.add("subjunctive");
        moods.add("optative");

        aspects.add("imperfective");
        aspects.add("perfective");
        aspects.add("progressive");
        aspects.add("habitual");
        aspects.add("inceptive");
        aspects.add("momentane");

        numbers.add("singular");
        numbers.add("plural");
        numbers.add("dual");
        numbers.add("collective");
        numbers.add("paucal");

        persons.add("first");
        persons.add("second");
        persons.add("third");

        smartRemove(cases);
        smartRemove(tenses);
        smartRemove(moods);
        smartRemove(aspects);
        smartRemove(numbers);

        partsOfSpeech.add("noun");
        partsOfSpeech.add("verb");
        partsOfSpeech.add("adjective");
        partsOfSpeech.add("adverb");

        infinitive = phonology.generateWord(1);
    }

    // Crazy, overcomplicated way to randomly remove cases or tenses or whatever without ending up with only past, optative, momentane endings or whatever. It should be a lot simpler, but it was being weird about forgetting the length of the list 
    void smartRemove(ArrayList<String> list) {
        ArrayList<Integer> mem = new ArrayList<Integer>();
        int marker = rand.nextInt(list.size());
        int size = list.size();
        while (size > marker) {
            list.remove(size - 1);
            size --;
        }
        int placeHolder;
        boolean noDuplicates;
        if (rand.nextInt(2) == 1 && size > 0) {   
            for (int i = 0; i < rand.nextInt(size); i++) {
                placeHolder = rand.nextInt(size);
                noDuplicates = true;
                for (int val : mem) {
                    if (val == placeHolder) {
                        noDuplicates = false;
                    }
                }
                if (noDuplicates) {
                    mem.add(placeHolder);
                } else {
                    i--;
                }
            }
        }
        int j = 0;
        for (int i = 0; i < mem.size(); i++) {
            if (i == 0) {
                list.remove(mem.get(i));
            } else {
                if (mem.get(i - 1) < mem.get(i)) {
                    j++;
                    list.remove(mem.get(i - j));
                } else {
                    list.remove(mem.get(i - j));
                }
            }
        }
        if (list.size() == 0) {
            list.add("");
        }
    }

    void createDerivationalMorphology(Phonology phonology) {
        // Derivational morphology is how you turn words into other words. For example, the -or suffix in "actor" means "one who ___," or the -ly suffix in "badly" changes the adjective into an adverb. What this program does, is it picks parts of speech at random and creates either prefixes or suffixes to change parts of speech besides a list of common derivational morphologies.
        String key;
        String value;
        for (int i = 0; i < rand.nextInt(10); i++) {
            key = partsOfSpeech.get(rand.nextInt(partsOfSpeech.size())) + " to " + partsOfSpeech.get(rand.nextInt(partsOfSpeech.size()));
            value = phonology.generateWord(1);
            derivationalMorphology.put(key, value);
        }

        if (rand.nextInt(2) == 0) {derivationalMorphology.put("Agent", phonology.generateWord(1));}
        if (rand.nextInt(2) == 0) {derivationalMorphology.put("Diminutive", phonology.generateWord(1));}
        if (rand.nextInt(2) == 0) {derivationalMorphology.put("Augmentative", phonology.generateWord(1));}
        if (rand.nextInt(2) == 0) {derivationalMorphology.put("Comparative", phonology.generateWord(1));}
        if (rand.nextInt(2) == 0) {derivationalMorphology.put("Superlative", phonology.generateWord(1));}
        if (rand.nextInt(2) == 0) {derivationalMorphology.put("Negative", phonology.generateWord(1));}
        if (rand.nextInt(2) == 0) {derivationalMorphology.put("Indefinitive", phonology.generateWord(1));}
        if (rand.nextInt(2) == 0) {derivationalMorphology.put("Definitive", phonology.generateWord(1));}

        for (String replace : derivationalMorphology.keySet()) {
            if (rand.nextInt(2) == 0) {
                value = derivationalMorphology.get(replace) + "-";
                derivationalMorphology.put(replace, value);
            } else {
                value = "-" + derivationalMorphology.get(replace);
                derivationalMorphology.put(replace, value);
            }
        }
    }

    void createEndings(Phonology phonology) {
        // Iterates through all the grammar stuff creating endings. I may change this later to avoid the massive numbers of endings.
        for (int i = 0; i < cases.size(); i++) {
            for (int j = 0; j < numbers.size(); j++) {
                nounEndings.add(phonology.generateWord(1));
            }
        }

        for (int i = 0; i < aspects.size(); i++) {
            for (int j = 0; j < moods.size(); j++) {
                for (int k = 0; k < numbers.size(); k++) {
                    for (int l = 0; l < persons.size(); l++) {
                        for (int m = 0; m < tenses.size(); m++) {
                            verbEndings.add(phonology.generateWord(1));
                        }
                    }
                }
            }
        }
    }

    String findVerbEnding(String aspect, String mood, String number, String person, String tense) {
        // Manages to find a specific ending in the unmarked list of endings by crazy math that would take a while to explain
        return verbEndings.get(tenses.size() * persons.size() * numbers.size() * moods.size() * checkIfExists(aspects, aspect) + tenses.size() * persons.size() * numbers.size() * checkIfExists(moods, mood) + tenses.size() * persons.size() * checkIfExists(numbers, number) + tenses.size() * checkIfExists(persons, person) + checkIfExists(tenses, tense));
    }

    String findNounEnding(String nounCase, String number) {
        return nounEndings.get(numbers.size() * checkIfExists(cases, nounCase) + checkIfExists(numbers, number));
    }

    int checkIfExists (ArrayList<String> list, String checker) {
        return (list.contains(checker)) ? list.indexOf(checker) : 0;
    }

    void createPronouns(Phonology phonology) {
        // Iterates through cases, numbers, and persons to create pronouns, with the possibility of just adding an ending to a stem
        String templates[] = {phonology.generateWord(1), phonology.generateWord(1), phonology.generateWord(1)};
        for (int i = 0; i < cases.size(); i++) {
            for (int j = 0; j < numbers.size(); j++) {
                for (int k = 0; k < persons.size(); k++){
                    if (rand.nextInt(20) == 1) {
                        // if (rand.nextBoolean()) {
                        pronouns.add(phonology.generateWord(rand.nextInt(2) + 1));
                    } else {
                        pronouns.add(templates[k] + findNounEnding(cases.get(i), numbers.get	(j)));
                    }
                    }
                }
            }
        }

        String findPronoun(String pronounCase, String number, String person) {
            return pronouns.get(persons.size() * numbers.size() * checkIfExists(persons, person) + persons.size() * checkIfExists(numbers, number) + checkIfExists(cases, pronounCase));
        }
    }
