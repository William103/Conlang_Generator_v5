package conlang;

import java.util.*;
import java.io.*;
class Translation {
    private static final String samplesFile = "javaSamples.txt";
    String sentence = "I would have so loved to have had lunch with him on the Champs-Elysées.";
    String newSentence = "";
    String literalSentence = "";
    String derivMorph = new String();
    boolean addEnding = false;
    boolean hasSubjunctive;
    boolean hasPerfective;
    boolean addVery = false;
    boolean hasComitative;
    boolean hasDative;
    boolean hasPrepositional;
    boolean hasLocative;
    public Translation(Lexicon lexicon, Grammar grammar) {
        hasLocative = grammar.cases.contains("locative");
        hasSubjunctive = grammar.moods.contains("subjunctive");
        hasPerfective = grammar.aspects.contains("perfective");
        hasComitative = grammar.cases.contains("comitative");
        hasDative = grammar.cases.contains("dative");
        hasPrepositional = grammar.cases.contains("prepositional");
        for (String phrase : grammar.wordOrder) {
            switch (phrase) {
                case ("Subject"):
								if (grammar.cases.contains("ergative")) {
									newSentence += grammar.findPronoun("ergative", "singular", "first");
									literalSentence += "I-erg";
								} else {
									newSentence += grammar.findPronoun("nominative", "singular", "first");
									literalSentence += "I";
								}
                break;
                case ("Verb"):
                if (!hasSubjunctive && grammar.headFinal) {
                    newSentence += lexicon.lexicon.get("if");
                    newSentence += " ";
                    literalSentence += "would ";
                }
                if (!hasPerfective && grammar.headFinal) {
                    newSentence += lexicon.lexicon.get("finish");
                    newSentence += " ";
                    literalSentence += "finish ";
                }
                if (grammar.derivationalMorphology.keySet().contains("Augmentative")) {
                    derivMorph = grammar.derivationalMorphology.get("Augmentative");
                    addVery = false;
                    if (derivMorph.charAt(0) == '-') {
                        derivMorph = derivMorph.substring(1, derivMorph.length());
                        addEnding = true;
                    } else {
                        newSentence += derivMorph.substring(0, derivMorph.length() - 1);
                        literalSentence += "very-";
                        addEnding = false;
                    }
                } else if (grammar.headFinal) {
                    newSentence += lexicon.lexicon.get("very");
                    newSentence += " ";
                    literalSentence += "very ";
                    addVery = false;
                    addEnding = false;
                } else {addVery = true;}
                newSentence += lexicon.lexicon.get("love");
                literalSentence += "love";
                if (addEnding) {
                    newSentence += derivMorph;
                    literalSentence += "-very";
                }
                newSentence += grammar.findVerbEnding("perfective", "subjunctive", "singular", "first", "past");
                literalSentence += "-ed";
                if (addVery) {
                    newSentence += " ";
                    newSentence += lexicon.lexicon.get("very");
                    literalSentence += " very";
                }
                if (!hasSubjunctive && !grammar.headFinal) {
                    newSentence += " ";
                    newSentence += lexicon.lexicon.get("if");
                    literalSentence += " would";
                }
                if (!hasPerfective && !grammar.headFinal) {
                    newSentence += " ";
                    newSentence += lexicon.lexicon.get("finish");
                    literalSentence += " finish";
                }
                break;
                case ("Object"):
                if (grammar.derivationalMorphology.keySet().contains("verb to noun")) {
                    derivMorph = grammar.derivationalMorphology.get("verb to noun");
                    if (derivMorph.charAt(0) == '-') {
                        newSentence += lexicon.lexicon.get("lunch");
                        newSentence += grammar.infinitive;
                        newSentence += derivMorph.substring(1);
                        literalSentence += "to-eat-lunch-noun";
                        if (grammar.cases.contains("absolutive")) {
                            newSentence += grammar.findNounEnding("absolutive", "singular");
                            literalSentence += "-abs";
                        } else if (grammar.cases.contains("accusative")) {
                            newSentence += grammar.findNounEnding("accusative", "singular");
                            literalSentence += "-acc";
                        }
                    } else {
                        newSentence += derivMorph.substring(0, derivMorph.length()-1);
                        newSentence += lexicon.lexicon.get("lunch");
                        newSentence += grammar.infinitive;
                        literalSentence += "noun-to-eat-lunch";
                        if (grammar.cases.contains("absolutive")) {
                            newSentence += grammar.findNounEnding("absolutive", "singular");
                            literalSentence += "-abs";
                        } else if (grammar.cases.contains("accusative")) {
                            newSentence += grammar.findNounEnding("accusative", "singular");
                            literalSentence += "-acc";
                        }
                    }
                } else {
                    newSentence += lexicon.lexicon.get("lunch");
                    newSentence += grammar.infinitive;
                    literalSentence += "to-eat-lunch";
                    if (grammar.inflectsParticiples) {
                        if (grammar.cases.contains("absolutive")) {
                            newSentence += grammar.findNounEnding("absolutive", "singular");
                            literalSentence += "-abs";
                        } else if (grammar.cases.contains("accusative")) {
                            newSentence += grammar.findNounEnding("accusative", "singular");
                            literalSentence += "-acc";
                        }
                    }
                }
                break;
                case ("Complement"):
                if (grammar.headFinal) {
                    if (!hasComitative) {
                        newSentence += lexicon.lexicon.get("with");
                        newSentence += " ";
                        literalSentence += "with ";
                    }
                    newSentence += lexicon.lexicon.get("he");
                    literalSentence += "he";
                    if (hasComitative) {
                        newSentence += grammar.findNounEnding("comitative", "singular");
                        literalSentence += "-with ";
                    } else if (hasDative){
                        newSentence += grammar.findNounEnding("dative", "singular");
                        literalSentence += "-dat ";
                    }
                    newSentence += " ";
                    if (!hasPrepositional) {
                        newSentence += lexicon.lexicon.get("on");
                        newSentence += " ";
                        literalSentence += " on ";
                    }
                    if (grammar.derivationalMorphology.keySet().contains("Definitive")) {
                        derivMorph = grammar.derivationalMorphology.get("Definitive");
                        if (derivMorph.charAt(0) == '-') {
                            derivMorph = derivMorph.substring(1, derivMorph.length());
                            addEnding = true;
                        } else {
                            newSentence += derivMorph.substring(0, derivMorph.length() - 1);
                            literalSentence += "the-";
                            addEnding = false;
                        }
                    }
                    newSentence += "Champs-Elysées";
                    literalSentence += "Champs-Elysées";
                    if (addEnding) {
                        newSentence += derivMorph;
                        literalSentence += "-the";
                    }
                    if (hasLocative) {
                        newSentence += grammar.findNounEnding("locative", "singular");
                        literalSentence += "-loc";
                    } else if (hasPrepositional) {
                        newSentence += grammar.findNounEnding("prepositional", "singular");
                        literalSentence += "-prep";
                    } else if (hasDative) {
                        newSentence += grammar.findNounEnding("dative", "singular");
                        literalSentence += "-dat";
                    }
                } else {
                    if (grammar.derivationalMorphology.keySet().contains("Definitive")) {
                        derivMorph = grammar.derivationalMorphology.get("Definitive");
                        if (derivMorph.charAt(0) == '-') {
                            derivMorph = derivMorph.substring(1, derivMorph.length());
                            addEnding = true;
                        } else {
                            newSentence += derivMorph.substring(0, derivMorph.length() - 1);
                            literalSentence += "the-";
                            addEnding = false;
                        }
                    }
                    newSentence += "Champs-Elysées";
                    literalSentence += "Champs-Elysées";
                    if (addEnding) {
                        newSentence += derivMorph;
                        literalSentence += "-the";
                    }
                    if (hasLocative) {
                        newSentence += grammar.findNounEnding("locative", "singular");
                        literalSentence += "-loc";
                    } else if (hasPrepositional) {
                        newSentence += grammar.findNounEnding("prepositional", "singular");
                        literalSentence += "-prep";
                    } else if (hasDative) {
                        newSentence += grammar.findNounEnding("dative", "singular");
                        literalSentence += "-dat";
                    }
                    newSentence += " ";
                    if (!hasPrepositional) {
                        newSentence += lexicon.lexicon.get("on");
                        newSentence += " ";
                        literalSentence += " on";
                    }
                    newSentence += lexicon.lexicon.get("he");
                    literalSentence += " he";
                    if (hasComitative) {
                        newSentence += grammar.findNounEnding("comitative", "singular");
                        literalSentence += "-with";
                    } else if (hasDative){
                        newSentence += grammar.findNounEnding("dative", "singular");
                        newSentence += " ";
                        newSentence += lexicon.lexicon.get("with");
                        literalSentence += "-dat with";
                    } else {
                        newSentence += " ";
                        newSentence += lexicon.lexicon.get("with");
                        literalSentence += " with"; 
                    }
                }
                break;
                default:
                break;
            }
            if (grammar.wordOrder.indexOf(phrase) == grammar.wordOrder.size() - 1) {
                newSentence += ".";
                literalSentence += ".";
            } else {
                newSentence += " ";
                literalSentence += " ";
            }
        }
        newSentence = newSentence.substring(0, 1).toUpperCase() + newSentence.substring(1);
        literalSentence = literalSentence.substring(0, 1).toUpperCase() + literalSentence.substring(1);
    }

    void writeSentence(Phonology phonology) {
        FileWriter fw = null;
        PrintWriter out = null;
        try {
            fw = new FileWriter(samplesFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
						out.println("Seed: " + ConlangGenerator.seed);
            out.println(phonology.name + ": " + newSentence);
            out.println();
            out.println("Literally: " + literalSentence);
            out.println();
            out.println();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            out.close();
        }
    }
}
