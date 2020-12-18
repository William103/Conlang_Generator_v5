package conlang;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;

class Phonology
{
     Random rand = new Random(ConlangGenerator.seed);

     ArrayList<Consonant> consonantInventory = new ArrayList<>();
     ArrayList<Vowel[]> diphthongs = new ArrayList<>();
     ArrayList<Vowel> vowelInventory = new ArrayList<>();
     ArrayList<String> initialClusters = new ArrayList<>();
     ArrayList<String> medialClusters  = new ArrayList<>();
     ArrayList<String> finalClusters   = new ArrayList<>();

     int initialConsonants = 0;
     int finalConsonants = 0;
     int medialConsonants;

     String name = new String();
     String newName = new String();

     Vowel[] newDiphthong;

    public Phonology()
    {
        medialConsonants = rand.nextInt(3);
        // medialConsonants = rand.nextInt(4);
        for (int i = 0; i < medialConsonants; i++) {
            if (rand.nextInt(2) == 0) {
                finalConsonants++;
            } else {
                initialConsonants++;
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

    ArrayList<Consonant> createConsonantInventory()
    {
        HashMap<String, Double> typesOfArticulation = new HashMap<>();
        typesOfArticulation.put("plosive", 0.9);
        typesOfArticulation.put("fricative", 0.8);
        typesOfArticulation.put("nasal", 0.9);
        typesOfArticulation.put("lateral", 0.4);
        typesOfArticulation.put("retroflex", 0.05);

        HashMap<String, Double> placesOfArticulation = new HashMap<>();
        placesOfArticulation.put("alveolar", 0.9);
        placesOfArticulation.put("bilabial", 0.9);
        placesOfArticulation.put("velar", 0.9);
        placesOfArticulation.put("palatal", 0.2);
        placesOfArticulation.put("uvular", 0.2);
        placesOfArticulation.put("glottal", 0.5);

        ArrayList<String> toRemove = new ArrayList<>();
        for (String type : typesOfArticulation.keySet()) {
            if (rand.nextDouble() > typesOfArticulation.get(type)) {
                toRemove.add(type);
            }
        }
        for (String type : toRemove) {
            typesOfArticulation.remove(type);
        }
        
        toRemove.clear();
        for (String place : placesOfArticulation.keySet()) {
            if (rand.nextDouble() > placesOfArticulation.get(place)) {
                toRemove.add(place);
            }
        }
        for (String place : toRemove) {
            placesOfArticulation.remove(place);
        }
        
        boolean distinguishVoice = !(rand.nextInt(5) == 0);

        HashSet<Consonant> consonantInventory = new HashSet<>();
        Consonant temp, temp2;

        for (String placeOfArticulation : placesOfArticulation.keySet()) {
            for (String typeOfArticulation : typesOfArticulation.keySet()) {
                temp  = new Consonant(placeOfArticulation, typeOfArticulation, true);
                temp2 = new Consonant(placeOfArticulation, typeOfArticulation, false);

                if (temp.rep != ' ' && distinguishVoice) {
                    consonantInventory.add(temp);
                }
                if (temp2.rep != ' ') {
                    consonantInventory.add(temp2);
                }
            }
        }

        ArrayList<Consonant> consonantInventoryList = new ArrayList<>(consonantInventory);
        
        if (rand.nextInt(1) == 0) {
            for (int i = 0; i < rand.nextInt(consonantInventoryList.size() / 2); i++) {
                consonantInventoryList.remove(rand.nextInt(consonantInventoryList.size()));
            }
        }

        return consonantInventoryList;
    }

    ArrayList<Vowel> createVowelInventory()
    {
        ArrayList<String> backnesses = new ArrayList<>();
        backnesses.add("front");
        backnesses.add("central");
        backnesses.add("back");

        ArrayList<Vowel> vowelInventory = new ArrayList<>();

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

    String createCluster(int size)
    {
        ArrayList<Consonant> cluster = new ArrayList<Consonant>();
        if (size == 0) {
            return "";
        } else if (size == 1) {
            return "" + consonantInventory.get(rand.nextInt(consonantInventory.size())).rep;
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
        String realCluster = "";
        for (Consonant consonant : cluster) {
            realCluster += consonant.rep;
        }
        return realCluster;
    }

    String generateWord(int syllables)
    {
        String word = "";
        String choice;
        if (initialClusters.size() > 0) {
            choice = initialClusters.get(rand.nextInt(initialClusters.size()));
        } else {
            choice = "";
        }
        if (choice.length() > 0) {if (choice.charAt(0) == ' ') {choice = "";}}
        if (choice.length() > 0) {
            for (char consonant : choice.toCharArray()) {
                word += consonant;
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
            if (choice.length() > 0) {
                for (char consonant : choice.toCharArray()) {
                    word = word + consonant;
                }
            }
            word = word + vowelInventory.get(rand.nextInt(vowelInventory.size())).rep;
        }
        choice = (finalClusters.size() > 0) ? finalClusters.get(rand.nextInt(finalClusters.size())) : "";
        if (choice.length() > 0) {
            for (char consonant : choice.toCharArray()) {
                word = word + consonant;
            }
        }
        return word;
    }
}
