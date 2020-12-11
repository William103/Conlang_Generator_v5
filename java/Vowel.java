class Vowel {
    String backness;
    boolean isHigh;
    boolean isRounded;
    boolean isVoiced = true;
    char rep;

    public Vowel(String a, boolean b, boolean c) {
        backness = a;
        isHigh = b;
        isRounded = c;
        rep = findString();
    }

    char findString() {
        switch (backness) {
            case "back":
            if (isHigh) {
                if (isRounded) {
                    return 'u';
                } else {
                    return 0x026F;
                }
            } else {
                if (isRounded) {
                    return 'o';
                } else {
                    return 'a';
                }
            }
            case "central":
            return 0x0259;
            case "front":
            if (isHigh) {
                if (isRounded) {
                    return 'y';
                } else {
                    return 'i';
                }
            } else {
                if (isRounded) {
                    return 'a';
                    //return 'œ';
                } else {
                    return 'e';
                }
            }
						case "near-front":
						if (isHigh) {
							if (isRounded) {
                  return 'a';
							} else {
                  return 'a';
								  //return 'ɪ';
							}
						} else {
							if (isRounded) {
                  return 'a';
								//return 'ɶ';
							} else {
                  return 'a';
								//return 'æ';
							}
						}
        }
        return ' ';
    }
}
