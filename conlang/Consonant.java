package conlang;

class Consonant {
    String placeOfArticulation;
    String typeOfArticulation;
    char rep;
    boolean isVoiced;

    public Consonant(String a, String b, boolean c) {
        placeOfArticulation = a;
        typeOfArticulation = b;
        isVoiced = c;
        rep = findString();
    }

    char findString() {
        // For example, if I want a voiceless, bilabial plosive, it should
        // return 'p'. I just used a bunch of switch statements to do this. If
        // something unexpected comes up, like a voiced glottal stop for
        // example, it will return ' ' so the phonology class can filter it out
        switch (placeOfArticulation) {
            case "bilabial":
                switch (typeOfArticulation) {
                    case "plosive":
                        if (isVoiced) {
                            return 'b';
                        } else {
                            return 'p';
                        }
                    case "fricative":
                        if (isVoiced) {
                            return 'v';
                        } else {
                            return 'f';
                        }
                    case "nasal":
                        return 'm';
                }
            case "alveolar":
                switch (typeOfArticulation) {
                    case "plosive":
                        if (isVoiced) {
                            return 'd';
                        } else {
                            return 't';
                        }
                    case "fricative":
                        if (isVoiced) {
                            return 'z';
                        } else {
                            return 's';
                        }
                    case "nasal":
                        return 'n';
                    case "lateral":
                        return 'l';
                    case "retroflex":
                        if (isVoiced) {
                            //return 0x0256;
                            return '\u0256';
                        } else {
                            return 0x0288;
                            //return '\u0288';
                        }
                }
            case "velar":
                switch (typeOfArticulation) {
                    case "plosive":
                        if (isVoiced) {
                            return 'g';
                        } else {
                            return 'k';
                        }
                    case "fricative":
                        if (isVoiced) {
                            return 0x0263;
                            //return '\u0263';
                        } else {
                            return 'x';
                        }
                    case "nasal":
                        return 0x014B;
                        //return '\u014B';
                    case "lateral":
                        return 'ɫ';
                }
            case "palatal":
                switch (typeOfArticulation) {
                    case "plosive":
                        if (isVoiced) {
                            return '\u025F';
                        } else {
                            return 'c';
                        }
                    case "fricative":
                        if (isVoiced) {
                            return '\u0292';
                        } else {
                            return 'j';
                        }
                    case "nasal":
                        return '\u00F1';
                    case "lateral":
                        return '\u028E';
                }
            case "uvular":
                switch (typeOfArticulation) {
                    case "plosive":
                        if (isVoiced) {
                            return 'G';
                        } else {
                            return 'q';
                        }
                    case "fricative":
                        if (isVoiced) {
                            return 'r';
                        } else {
                            return '\u03C7';
                        }
                    case "nasal":
                        return '\u0274';
                }
            case "glottal":
                switch (typeOfArticulation) {
                    case "plosive":
                        return '\'';
                    case "fricative":
                        if (isVoiced) {
                            return '\u0266';
                        } else {
                            return 'h';
                        }
                }
        }
        return ' ';
    }
}
