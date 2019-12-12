import random
import phonology

lexicon = {}

def init():
    phonology.init()
    with open("lexicon.txt","r") as f:
        lextemp = f.readlines()
    for i in range(len(lextemp)):
        if i % 2 == 0:
            lexicon[lextemp[i].replace('\n','').replace(':','')] = lextemp[i+1].replace('\n','').split(',')
    with open("dictionary.txt","w",encoding='utf-8') as f:
        for pos in lexicon:
            f.write(pos + ":\n")
            for i in range(len(lexicon[pos])):
                word = lexicon[pos][i]
                word2 = phonology.generate_word(max(len(word) // 3 + random.randrange(-2,3),1))
                f.write(word2)
                if i < len(lexicon[pos])-1:
                    f.write(',')
            f.write('\n')
