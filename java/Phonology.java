import java.util.*;
class Phonology {
    ArrayList<Consonant> consonantInventory = new ArrayList<Consonant>();
		ArrayList<Vowel[]> diphthongs = new ArrayList<Vowel[]>();
    Random rand = new Random(ConlangGenerator.seed);
    ArrayList<Vowel> vowelInventory = new ArrayList<Vowel>();
    ArrayList<ArrayList<Character>> initialClusters = new ArrayList<ArrayList<Character>>();
    ArrayList<ArrayList<Character>> medialClusters = new ArrayList<ArrayList<Character>>();
    ArrayList<ArrayList<Character>> finalClusters = new ArrayList<ArrayList<Character>>();
    int initialConsonants = 0;
    int finalConsonants = 0;
    int medialConsonants;
    String name = new String();
    String newName = new String();
		Vowel[] newDiphthong;

    public Phonology() {
        medialConsonants = rand.nextInt(4) + 1;
        for (int i = 0; i < medialConsonants; i++) {
            if (rand.nextBoolean()) {
                initialConsonants++;
            } else {
                finalConsonants++;
            }
        }

        if (medialConsonants == 0) {
            initialConsonants = 1;
            medialConsonants = 1;
        }

        consonantInventory = createConsonantInventory();
        vowelInventory = createVowelInventory();
				newDiphthong = new Vowel[]{vowelInventory.get(rand.nextInt(vowelInventory.size())), vowelInventory.get(rand.nextInt(vowelInventory.size()))};
				if (rand.nextBoolean()) {
					for (int i = 0; i < rand.nextInt(6); i++) {
						if (!diphthongs.contains(newDiphthong)) {
							diphthongs.add(newDiphthong);
							newDiphthong = new Vowel[]{vowelInventory.get(rand.nextInt(vowelInventory.size())), vowelInventory.get(rand.nextInt(vowelInventory.size()))};
						}
					}
				}

        for (int i = 0; i < rand.nextInt(initialConsonants * consonantInventory.size() * 5 + 1) + initialConsonants; i++) {
            initialClusters.add(createCluster(initialConsonants));
        }

        for (int i = 0; i < rand.nextInt(medialConsonants * consonantInventory.size() * 5 + 1) + medialConsonants; i++) {
            medialClusters.add(createCluster(medialConsonants));
        }

        for (int i = 0; i < rand.nextInt(finalConsonants * consonantInventory.size() * 5 + 1) + finalConsonants; i++) {
            finalClusters.add(createCluster(finalConsonants));
        }

        name = generateWord(rand.nextInt(3) + 2);
        newName = name.substring(0, 1).toUpperCase() + name.substring(1);
        name = newName;
    }

    ArrayList<Consonant> createConsonantInventory() {
        ArrayList<String> typesOfArticulation = new ArrayList<String>();
        typesOfArticulation.add("plosive");
        typesOfArticulation.add("fricative");
        typesOfArticulation.add("nasal");
				typesOfArticulation.add("lateral");

        ArrayList<String> placesOfArticulation = new ArrayList<String>();
        placesOfArticulation.add("alveolar");
        placesOfArticulation.add("bilabial");
        placesOfArticulation.add("velar");
        placesOfArticulation.add("palatal");
        placesOfArticulation.add("uvular");

        typesOfArticulation.remove(rand.nextInt(typesOfArticulation.size()));

        int size = placesOfArticulation.size();
        for (int i = 0; i < rand.nextInt(placesOfArticulation.size()); i++) {
            placesOfArticulation.remove(rand.nextInt(size));
            size--;
        }

        boolean distinguishVoice = !(0 == rand.nextInt(10));

        ArrayList<Consonant> consonantInventory = new ArrayList<Consonant>();
				Consonant temp, temp2;

        for (String placeOfArticulation : placesOfArticulation) {
            for (String typeOfArticulation : typesOfArticulation) {
							temp = new Consonant(placeOfArticulation, typeOfArticulation, true);
							temp2 = new Consonant(placeOfArticulation, typeOfArticulation, false);
              
							if (temp.rep != ' ' && distinguishVoice && typeOfArticulation != "nasal" && typeOfArticulation != "lateral") {
								consonantInventory.add(temp);
							}
							if (temp2.rep != ' ') {
								consonantInventory.add(temp2);
							}
            }
        }

        if (rand.nextInt(1) == 0) {
            for (int i = 0; i < rand.nextInt(consonantInventory.size() / 2); i++) {
                consonantInventory.remove(rand.nextInt(consonantInventory.size()));
            }
        }

        return consonantInventory;
    }

    ArrayList<Vowel> createVowelInventory() {
        ArrayList<String> backnesses = new ArrayList<String>();
        backnesses.add("front");
        backnesses.add("central");
        backnesses.add("back");

        ArrayList<Vowel> vowelInventory = new ArrayList<Vowel>();

        for (String backness : backnesses) {
            if (backness != "central") {
                vowelInventory.add(new Vowel(backness, true, true));
                vowelInventory.add(new Vowel(backness, true, false));
                vowelInventory.add(new Vowel(backness, false, true));
                vowelInventory.add(new Vowel(backness, false, false));
            } else {
                vowelInventory.add(new Vowel("central", false, false));
            }
        }

        for (int i = 0; i < rand.nextInt(vowelInventory.size()); i++) {
            vowelInventory.remove(rand.nextInt(vowelInventory.size()));
        }

        return vowelInventory;
    }

    ArrayList<Character> createCluster(int size) {
        ArrayList<Consonant> cluster = new ArrayList<Consonant>();
        if (size == 0) {
            ArrayList<Character> temp = new ArrayList<Character>();
            return temp;
        } else if (size == 1) {
            ArrayList<Character> temp = new ArrayList<Character>();
            temp.add(consonantInventory.get(rand.nextInt(consonantInventory.size())).rep);
            return temp;
        }
        for (int i = 0; i < rand.nextInt(size - 1) + 2; i++) {
            if (i == 0) {
                cluster.add(consonantInventory.get(rand.nextInt(consonantInventory.size())));
            } else {
                if (cluster.get(i - 1).typeOfArticulation == "plosive") {
                    int j = rand.nextInt(consonantInventory.size());
                    while (consonantInventory.get(j).typeOfArticulation == "plosive") {
                        j = rand.nextInt(consonantInventory.size());
                    }
                    cluster.add(consonantInventory.get(j));
                } else {
                    cluster.add(consonantInventory.get(rand.nextInt(consonantInventory.size())));
                }
            }
        }
        ArrayList<Character> realCluster = new ArrayList<Character>();
        for (Consonant consonant : cluster) {
            realCluster.add(consonant.rep);
        }
        return realCluster;
    }

    String generateWord(int syllables) {
        String word = "";
				ArrayList<Character> choice = new ArrayList<Character>();
        ArrayList<Character> bleh = new ArrayList<Character>();
				if (initialClusters.size() > 0) {
					choice = initialClusters.get(rand.nextInt(initialClusters.size()));
				} else {
					choice = bleh;
				}
				if (choice.size() > 0) {if (choice.get(0) == ' ') {choice = bleh;}}
        if (choice.size() > 0) {
            for (char consonant : choice) {
                word = word + consonant;
            }
        }
				if (rand.nextInt(vowelInventory.size() + diphthongs.size()) < vowelInventory.size()) {
					word += vowelInventory.get(rand.nextInt(vowelInventory.size())).rep;
				} else {
					for (Vowel vowel : diphthongs.get(rand.nextInt(diphthongs.size()))) {
						word += vowel.rep;
					}
				}
        for (int i = 0; i < syllables - 1; i++) {
            choice = medialClusters.get(rand.nextInt(medialClusters.size()));
            if (choice.size() > 0) {
                for (char consonant : choice) {
                    word = word + consonant;
                }
            }
            word = word + vowelInventory.get(rand.nextInt(vowelInventory.size())).rep;
        }
        choice = (finalClusters.size() > 0) ? finalClusters.get(rand.nextInt(finalClusters.size())) : bleh;
        if (choice.size() > 0) {
            for (char consonant : choice) {
                word = word + consonant;
            }
        }
        return word;
    }
}
