import random

consonants = []
vowels = []

phonotactics = {}
syllable_structure = ''

def init(): # data taken from WALS
    # decide consonant inventory size
    consonant_inventory_size = 0
    size = random.randrange(563)
    if size < 89: # small consonant inventory
        consonant_inventory_size = random.randrange(6,15)
    elif size < 89 + 122: # moderately small
        consonant_inventory_size = random.randrange(15,19)
    elif size < 89 + 122 + 201: # moderately large
        consonant_inventory_size = random.randrange(26,33)
    else: # large
        consonant_inventory_size = random.randrange(34,50) # the upper limit is a guess, Ubykh has 84 without clicks

    # now decide whether or not we voice plosives and/or fricatives
    voice_in_plosives = True
    voice_in_fricatives = True
    randval = random.randrange(567)
    if randval < 182:
        voice_in_plosives = False
        voice_in_fricatives = False
    elif randval < 182 + 189:
        voice_in_plosives = True
        voice_in_fricatives = False
    elif randval < 182 + 189 + 38:
        voice_in_plosives = False
        voice_in_fricatives = True
    else:
        voice_in_plosives = True
        voice_in_fricatives = True

    # decide what if any uvulars are present
    randval = random.randrange(567)
    if randval < 470:
        pass
    elif randval < 470 + 38:
        consonants.append('q')
        if voice_in_plosives:
            consonants.append('ɢ')
    elif randval < 470 + 38 + 11:
        consonants.append('χ')
        if voice_in_fricatives:
            consonants.append('ʁ')
    else:
        consonants.append('q')
        if voice_in_plosives:
            consonants.append('ɢ')
        consonants.append('χ')
        if voice_in_fricatives:
            consonants.append('ʁ')

    # laterals
    randval = random.randrange(567)
    if randval < 95:
        pass
    elif randval < 95 + 388:
        consonants.append('l')
    elif randval < 95 + 388 + 29:
        consonants.append('ɺ')
    elif randval < 95 + 388 + 29 + 47:
        consonants.append('l')
        consonants.append('ɬ')
        if voice_in_fricatives:
            consonants.append('ɮ')
    else:
        consonants.append('ɬ')
        if voice_in_fricatives:
            consonants.append('ɮ')

    # velar nasals
    randval = random.randrange(469)
    if randval < 147:
        consonants.append('ŋ')
        phonotactics['initial velar nasal'] = True
    elif randval < 147 + 87:
        consonants.append('ŋ')
        phonotactics['initial velar nasal'] = False
    else:
        pass

    # plosives; note: 'other' is taken into account by voice_in_plosives
    randval = random.randrange(325)
    if randval < 255:
        consonants.append('p')
        consonants.append('t')
        consonants.append('k')
        if voice_in_plosives:
            consonants.append('b')
            consonants.append('d')
            consonants.append('g')
    elif randval < 255 + 33:
        consonants.append('t')
        consonants.append('k')
        if voice_in_plosives:
            consonants.append('b')
            consonants.append('d')
            consonants.append('g')
    elif randval < 255 + 33 + 34:
        consonants.append('p')
        consonants.append('t')
        consonants.append('k')
        if voice_in_plosives:
            consonants.append('b')
            consonants.append('d')
    else:
        consonants.append('t')
        consonants.append('k')
        if voice_in_plosives:
            consonants.append('b')
            consonants.append('d')

    # fricatives, nasals
    consonants.append('f')
    consonants.append('s')
    consonants.append('x')
    consonants.append('m')
    consonants.append('n')
    if voice_in_fricatives:
        consonants.append('v')
        consonants.append('z')
        consonants.append('ɣ')
    randval = random.randrange(567)
    if randval < 503:
        pass
    elif randval < 503 + 4:
        if 'm' in consonants: consonants.remove('m')
        if 'p' in consonants: consonants.remove('p')
        if 'b' in consonants: consonants.remove('b')
    elif randval < 503 + 4 + 48:
        if 'v' in consonants: consonants.remove('v')
        if 'z' in consonants: consonants.remove('z')
        if 'ɣ' in consonants: consonants.remove('ɣ')
        consonants.remove('f')
        consonants.remove('s')
        consonants.remove('x')
    elif randval < 503 + 4 + 48 + 10:
        if 'm' in consonants: consonants.remove('m')
        if 'n' in consonants: consonants.remove('n')
        if 'ŋ' in consonants: consonants.remove('ŋ')
    elif randval < 503 + 4 + 48 + 10 + 1:
        if 'm' in consonants: consonants.remove('m')
        if 'n' in consonants: consonants.remove('n')
        if 'ŋ' in consonants: consonants.remove('ŋ')
        if 'm' in consonants: consonants.remove('m')
        if 'p' in consonants: consonants.remove('p')
        if 'b' in consonants: consonants.remove('b')
    else:
        if 'm' in consonants: consonants.remove('m')
        if 'n' in consonants: consonants.remove('n')
        if 'ŋ' in consonants: consonants.remove('ŋ')
        if 'v' in consonants: consonants.remove('v')
        if 'z' in consonants: consonants.remove('z')
        if 'ɣ' in consonants: consonants.remove('v')
        consonants.remove('f')
        consonants.remove('s')
        consonants.remove('x')

    # rare sounds; I'm ignoring clicks and labio-velars
    randval = random.randrange(512)
    if randval < 449:
        pass
    elif randval < 449 + 21:
        consonants.append('ħ')
        if voice_in_fricatives:
            consonants.append('ʕ')
    elif randval < 449 + 21 + 40:
        consonants.append('θ')
        if voice_in_fricatives:
            consonants.append('ð')
    elif randval < 449 + 21 + 40 + 2: 
        consonants.append('θ')
        if voice_in_fricatives:
            consonants.append('ð')
        consonants.append('ħ')
        if voice_in_fricatives:
            consonants.append('ʕ')

    # WARNING: LEAVING WALS AND ENTERING MY WHOLLY UNQUALIFIED AND BARELY RESEARCHED SPECULATION
    
    # rhotics
    randval = random.random()
    if randval < 0.8:
        randval2 = random.random()
        if randval2 < 0.4:
            consonants.append('r')
        elif randval2 < 0.8:
            consonants.append('ɾ')
        elif randval2 < 0.9:
            consonants.append('ɹ')
        else:
            consonants.append('r')
            consonants.append('ɾ')

    # alveo-palatals
    randval = random.random()
    if randval < 0.7:
        randval2 = random.random()
        if randval2 < 0.4:
            consonants.append('ʃ')
            if voice_in_fricatives:
                consonants.append('ʒ')
        elif randval2 < 0.8:
            consonants.append('ɕ')
            if voice_in_fricatives:
                consonants.append('ʑ')
        elif randval2 < 0.9:
            consonants.append('ʂ')
            if voice_in_fricatives:
                consonants.append('ʐ')
        else:
            consonants.append('ʃ')
            if voice_in_fricatives:
                consonants.append('ʒ')
            consonants.append('ɕ')
            if voice_in_fricatives:
                consonants.append('ʑ')
            consonants.append('ʂ')
            if voice_in_fricatives:
                consonants.append('ʐ')

    # affricates
    randval = random.random()
    if randval < 0.85:
        randval2 = random.random()
        if randval2 < 0.3:
            consonants.append('ʦ')
            if voice_in_fricatives and voice_in_plosives and random.random() < 0.3:
                consonants.append('ʣ')
        elif randval2 < 0.55:
            consonants.append('ʧ')
            if voice_in_fricatives and voice_in_plosives and random.random() < 0.3:
                consonants.append('ʤ')
        elif randval2 < 0.8:
            consonants.append('ʨ')
            if voice_in_fricatives and voice_in_plosives and random.random() < 0.3:
                consonants.append('ʥ')
        elif randval2 < 0.9:
            consonants.append('tɬ')
            if voice_in_fricatives and voice_in_plosives and random.random() < 0.3:
                consonants.append('dɮ')
        elif randval2 < 0.95:
            consonants.append('pf')
            if voice_in_fricatives and voice_in_plosives and random.random() < 0.3:
                consonants.append('bv')
        else:
            randval3 = random.random()
            if randval3 < 0.33:
                consonants.append('ʦ')
                if voice_in_fricatives and voice_in_plosives and random.random() < 0.3:
                    consonants.append('ʣ')
            elif randval3 < 0.67:
                consonants.append('ʧ')
                if voice_in_fricatives and voice_in_plosives and random.random() < 0.3:
                    consonants.append('ʤ')
            else:
                consonants.append('ʨ')
                if voice_in_fricatives and voice_in_plosives and random.random() < 0.3:
                    consonants.append('ʥ')
            if random.random() < 0.5:
                consonants.append('tɬ')
                if voice_in_fricatives and voice_in_plosives and random.random() < 0.3:
                    consonants.append('dɮ')
            else:
                consonants.append('pf')
                if voice_in_fricatives and voice_in_plosives and random.random() < 0.3:
                    consonants.append('bv')

    # aspiration
    if not voice_in_plosives and random.random() < 0.5 or voice_in_plosives and random.random() < 0.2:
        for cons in consonants:
            if cons == 'p':
                consonants.append('pʰ')
            elif cons == 't':
                consonants.append('tʰ')
            elif cons == 'k':
                consonants.append('kʰ')
            elif cons == 'q':
                consonants.append('qʰ')

    # glottal consonants
    if random.random() < 0.7:
        consonants.append('h')
        if voice_in_fricatives and random.random() < 0.3:
            consonants.append('ɦ')
        if random.random() < 0.7:
            consonants.append('ʔ')

    # glides
    if random.random() < 0.9:
        if random.random() < 0.7:
            consonants.append('j')
        if random.random() < 0.5:
            consonants.append('w')
            if random.random() < 0.1:
                consonants.append('ʍ')
        if random.random() < 0.2:
            consonants.append('ɥ')

    # glottalized consonants: some data from WALS
    randval = random.randrange(567)
    if randval < 409:
        pass
    elif randval < 409 + 58:
        for cons in consonants: # this part is made up
            if cons == 'p' and random.random() < 0.6:
                consonants.append("p'")
            elif cons == 't' and random.random() < 0.7:
                consonants.append("t'")
            elif cons == 'k' and random.random() < 0.9:
                consonants.append("k'")
            elif cons == 'q' and random.random() < 0.8:
                consonants.append("q'")
            elif cons == 'ʦ' and random.random() < 0.7:
                consonants.append("ʦ'")
            elif cons == 'ʧ' and random.random() < 0.7:
                consonants.append("ʧ'")
            elif cons == 'ʨ' and random.random() < 0.7:
                consonants.append("ʨ'")
            elif cons == 'tɬ' and random.random() < 0.7:
                consonants.append("tɬ'")
    elif randval < 409 + 58 + 55:
        for cons in consonants:
            if cons == 'b':
                consonants.append('ɓ')
            elif cons == 'd':
                consonants.append('ɗ')
            elif cons == 'g':
                consonants.append('ɠ')
            elif cons == 'ɢ':
                consonants.append('ʛ')
    elif randval < 409 + 58 + 55 + 4:
        for cons in consonants:
            if cons == 'm':
                consonants.append('mˀ')
            elif cons == 'n':
                consonants.append('nˀ')
            elif cons == 'ŋ':
                consonants.append('ŋˀ')
            elif cons == 'l':
                consonants.append('lˀ')
            elif cons == 'j':
                consonants.append('jˀ')
            elif cons == 'w':
                consonants.append('wˀ')
    elif randval < 409 + 58 + 55 + 4 + 14:
        for cons in consonants: # this part is made up
            if cons == 'p' and random.random() < 0.6:
                consonants.append("p'")
            elif cons == 't' and random.random() < 0.7:
                consonants.append("t'")
            elif cons == 'k' and random.random() < 0.9:
                consonants.append("k'")
            elif cons == 'q' and random.random() < 0.8:
                consonants.append("q'")
            elif cons == 'ʦ' and random.random() < 0.7:
                consonants.append("ʦ'")
            elif cons == 'ʧ' and random.random() < 0.7:
                consonants.append("ʧ'")
            elif cons == 'ʨ' and random.random() < 0.7:
                consonants.append("ʨ'")
            elif cons == 'tɬ' and random.random() < 0.7:
                consonants.append("tɬ'")
            elif cons == 'b':
                consonants.append('ɓ')
            elif cons == 'd':
                consonants.append('ɗ')
            elif cons == 'g':
                consonants.append('ɠ')
            elif cons == 'ɢ':
                consonants.append('ʛ')
    elif randval < 409 + 58 + 55 + 4 + 14 + 20:
        for cons in consonants: # this part is made up
            if cons == 'p' and random.random() < 0.6:
                consonants.append("p'")
            elif cons == 't' and random.random() < 0.7:
                consonants.append("t'")
            elif cons == 'k' and random.random() < 0.9:
                consonants.append("k'")
            elif cons == 'q' and random.random() < 0.8:
                consonants.append("q'")
            elif cons == 'ʦ' and random.random() < 0.7:
                consonants.append("ʦ'")
            elif cons == 'ʧ' and random.random() < 0.7:
                consonants.append("ʧ'")
            elif cons == 'ʨ' and random.random() < 0.7:
                consonants.append("ʨ'")
            elif cons == 'tɬ' and random.random() < 0.7:
                consonants.append("tɬ'")
            elif cons == 'm':
                consonants.append('mˀ')
            elif cons == 'n':
                consonants.append('nˀ')
            elif cons == 'ŋ':
                consonants.append('ŋˀ')
            elif cons == 'l':
                consonants.append('lˀ')
            elif cons == 'j':
                consonants.append('jˀ')
            elif cons == 'w':
                consonants.append('wˀ')
    elif randval < 409 + 58 + 55 + 4 + 14 + 20 + 4:
        for cons in consonants: # this part is made up
            if cons == 'b':
                consonants.append('ɓ')
            elif cons == 'd':
                consonants.append('ɗ')
            elif cons == 'g':
                consonants.append('ɠ')
            elif cons == 'ɢ':
                consonants.append('ʛ')
            elif cons == 'm':
                consonants.append('mˀ')
            elif cons == 'n':
                consonants.append('nˀ')
            elif cons == 'ŋ':
                consonants.append('ŋˀ')
            elif cons == 'l':
                consonants.append('lˀ')
            elif cons == 'j':
                consonants.append('jˀ')
            elif cons == 'w':
                consonants.append('wˀ')
    else:
        for cons in consonants: # this part is made up
            if cons == 'p' and random.random() < 0.6:
                consonants.append("p'")
            elif cons == 't' and random.random() < 0.7:
                consonants.append("t'")
            elif cons == 'k' and random.random() < 0.9:
                consonants.append("k'")
            elif cons == 'q' and random.random() < 0.8:
                consonants.append("q'")
            elif cons == 'ʦ' and random.random() < 0.7:
                consonants.append("ʦ'")
            elif cons == 'ʧ' and random.random() < 0.7:
                consonants.append("ʧ'")
            elif cons == 'ʨ' and random.random() < 0.7:
                consonants.append("ʨ'")
            elif cons == 'tɬ' and random.random() < 0.7:
                consonants.append("tɬ'")
            elif cons == 'b':
                consonants.append('ɓ')
            elif cons == 'd':
                consonants.append('ɗ')
            elif cons == 'g':
                consonants.append('ɠ')
            elif cons == 'ɢ':
                consonants.append('ʛ')
            elif cons == 'm':
                consonants.append('mˀ')
            elif cons == 'n':
                consonants.append('nˀ')
            elif cons == 'ŋ':
                consonants.append('ŋˀ')
            elif cons == 'l':
                consonants.append('lˀ')
            elif cons == 'j':
                consonants.append('jˀ')
            elif cons == 'w':
                consonants.append('wˀ')

    if len(consonants) > consonant_inventory_size:
        random.shuffle(consonants)
        for i in range(random.randrange(len(consonants)-consonant_inventory_size)):
            consonants.pop()

    # vowels
    # from WALS, mostly not made up, but kinda
    number_of_vowels = 0
    randval = random.randrange(564)

    if random.randrange(562) >= 525:
        vowels.append('y')

    if randval < 93:
        if random.random() < 0.3: # made up statistic
            if 'y' in vowels:
                vowels.remove('y')
            if random.random() < 0.3: # made up
                vowels.append('e')
            else:
                vowels.append('a')
            if random.random() < 0.3:
                vowels.append('o')
            else:
                vowels.append('u')
        else:
            number_of_vowels = random.randrange(2,4)
            if 'y' in vowels:
                vowels.remove('y')
            if random.random() < 0.3: # made up
                vowels.append('e')
            else:
                vowels.append('a')
            if random.random() < 0.3:
                vowels.append('o')
            else:
                vowels.append('u')
            vowels.append('i')
    elif randval < 93 + 287:
        if random.random() < 0.8: # also made up statistic
            if 'y' in vowels:
                vowels.remove('y')
            vowels.append('a') # made up stuff
            vowels.append('e')
            vowels.append('i')
            vowels.append('o')
            vowels.append('u')
        else:
            if 'y' in vowels:
                vowels.remove('y')
            vowels.append('a')
            vowels.append('e')
            vowels.append('i')
            vowels.append('o')
            vowels.append('u')
            if random.random() < 0.5:
                vowels.append('ɑ')
            else:
                vowels.append('ɒ')
    else:
        number_of_vowels = random.randrange(7,15)
        if number_of_vowels < 10:
            vowels.append('a')
            vowels.append('e')
            vowels.append('i')
            vowels.append('o')
            vowels.append('u')
            if random.random() < 0.5:
                vowels.append('ɑ')
            else:
                vowels.append('ɒ')
            if 'y' not in vowels:
                if random.random() < 0.5:
                    vowels.append('ɛ')
                    if number_of_vowels == 8:
                        vowels.append('ɔ')
                else:
                    vowels.append('ɔ')
                    if number_of_vowels == 8:
                        vowels.append('ɛ')
            else:
                if random.random() < 0.5:
                    vowels.append('ɛ')
                else:
                    vowels.append('ɔ')
        else:
            if number_of_vowels % 2 == 1:
                number_of_vowels -= 1
            vowels.append('a')
            vowels.append('e')
            vowels.append('i')
            vowels.append('o')
            vowels.append('u')
            available_vowels = ['ɛ', 'ɑ']
            while (len(vowels) < number_of_vowels / 2):
                vowel = random.choice(available_vowels)
                if vowel not in vowels:
                    if vowel == 'ɛ':
                        vowels.append('ɛ' if random.random() < 0.5 else 'ɔ')
                    else:
                        vowels.append('ɑ' if random.random() < 0.5 else 'ɒ')
            for vowel in vowels:
                if vowel == 'a':
                    vowels.append('æ')
                elif vowel == 'e':
                    if 'ɛ' in vowels:
                        vowels.append('ɔ')
                    else:
                        vowels.append('ɛ')
                elif vowel == 'i':
                    vowels.append('ɪ')
                elif vowel == 'y':
                    vowels.append('ʏ')
                elif vowel == 'o':
                    vowels.append('ʌ')
                elif vowel == 'u':
                    vowels.append('ʊ')
                elif vowel == 'ɛ' or vowel == 'ɔ':
                    vowels.append('ə')
                elif vowel == 'ɑ' or vowel == 'ɒ':
                    vowels.append('ɐ')

    # phonotactics
    for i in range(len(consonants) // 2):
        consonants.append('')

    randval = random.randrange(486)
    global syllable_structure
    if randval < 61:
        syllable_structure = 'CV'
    elif randval < 61 + 274:
        randval2 = random.random()
        if randval2 < 0.33:
            if random.random() < 0.8:
                if random.random() < 0.5:
                    syllable_structure = 'CLV'
                else:
                    syllable_structure = 'CGV'
            else:
                syllable_structure = 'CCV'
        elif randval2 < 0.66:
            syllable_structure = 'CVC'
        else:
            if random.random() < 0.8:
                if random.random() < 0.5:
                    syllable_structure = 'CLVC'
                else:
                    syllable_structure = 'CGVC'
            else:
                syllable_structure = 'CCVC'
    else:
        syllable_structure = 'C' * random.randrange(3) + 'F' * random.randrange(3) + 'R' * random.randrange(3) + 'N' * random.randrange(2) + 'L' * random.randrange(2) + 'G' * random.randrange(2) + 'V' + 'G' * random.randrange(2) + 'L' * random.randrange(2) + 'N' * random.randrange(2) + 'R' * random.randrange(3) + 'F' * random.randrange(3) + 'C' * random.randrange(3)

    for i in range(random.randrange(len(consonants)//3)):
        consonants.append('')

def generate_word(syllables):
    word = '['
    for i in range(syllables):
        for char in syllable_structure:
            if char == 'C':
                if len(consonants) > 0:
                    choice = random.choice(consonants)
                    while choice == word[-1]:
                        choice = random.choice(consonants)
                    word += random.choice(consonants)
            elif char == 'L':
                choice = random.choice(consonants)
                if 'l' in consonants or 'r' in consonants or 'ɺ' in consonants or 'ɾ' in consonants or 'ɹ' in consonants:
                    while not (choice == 'l' or choice == 'r' or choice == 'ɺ' or choice == 'ɾ' or choice == 'ɹ' or choice == '') or choice == word[-1]:
                        choice = random.choice(consonants)
                    word += choice
            elif char == 'G':
                if 'j' in consonants or 'w' in consonants:
                    choice = random.choice(consonants)
                    while not (choice == 'j' or choice == 'w' or choice == '') or choice == word[-1]:
                        choice = random.choice(consonants)
                    word += choice
            elif char == 'V':
                word += random.choice(vowels)
            elif char == 'R':
                choice = random.choice(consonants)
                if 'l' in consonants or 'r' in consonants or 'ɺ' in consonants or 'ɾ' in consonants or 'ɹ' in consonants or 'm' in consonants or 'n' in consonants or 'ŋ' in consonants or 'j' in consonants or 'w' in consonants:
                    while not (choice == 'l' or choice == 'r' or choice == 'ɺ' or choice == 'ɾ' or choice == 'ɹ' or choice == '' or choice == 'm' or choice == 'n' or choice == 'ŋ' or choice == 'j' or choice == 'w') or choice == word[-1]:
                        choice = random.choice(consonants)
                    word += choice
            elif char == 'N':
                choice = random.choice(consonants)
                if 'm' in consonants or 'n' in consonants or 'ŋ':
                    while not (choice == 'm' or choice == 'n' or choice == 'ŋ') or choice == word[-1]:
                        choice = random.choice(consonants)
                    word += choice

        if not i == syllables - 1: word += '.'
    word += ']'
    return word
