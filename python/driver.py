import random
import phonology
import syntax
import lexicon

#phonology.init()
#print(phonology.consonants)
#print(phonology.vowels)

#for i in range(10):
    #syllables = random.randrange(1,5)
    #print(phonology.generate_word(syllables))

lexicon.init()
syntax.init()
#syntax.generate_fancy_sentence()
syntax.build_sentence()
with open('samples.txt','w',encoding='utf-8') as f:
    for i in range(20):
        con,eng = syntax.build_sentence()
        f.write('conlang:\n' + con + '\nEnglish:\n' + eng + '\n\n')
