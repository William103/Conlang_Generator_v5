import random

head_first = False
subject_left = False
verb_attraction = False
serial_verb = False

english_mode = False

ps_rules = {}
lexicon = {}
englexicon = {}

english_ps_rules = {
    'S': [['NP','VP']],
    'VP': [["V'",'NP'], ["V'"], ["V'",'S'], ["V'", 'PP'], ["V'", 'NP', 'PP'], ["V'", 'PP', 'S'], ["V'", 'NP', 'PP', 'S']] + [["V'"]] * 4 + [["V'", 'NP']] * 2,
    "V'": [['V'], ['V','B']] + [['V']] * 3,
    'NP': [['D', "N'"]],
    "N'": [['N'], ['A', "N'"], ["N'", 'PP']] + [['N']] * 3,
    'PP': [['P', 'NP']]
}

# basically, use the principles and parameters approach to generate
# phrase-structure rules randomly so we can use those PS-rules later to generate
# sentences
def init():
    global head_first
    global subject_left
    global verb_attraction
    global serial_verb
    global ps_rules
    global lexicon
    global englexicon
    head_first = random.random() < 0.5
    subject_left = random.random() < 0.90
    verb_attraction = random.random() < 0.2
    serial_verb = random.random() < 0.2
    if head_first and not subject_left and verb_attraction:
        verb_attraction = False
    elif not head_first and subject_left and verb_attraction:
        verb_attraction = False
    ps_rules['VP'] = [["V'"]]
    ps_rules['NP'] = []
    ps_rules['S'] = [['NP', 'VP']] if subject_left else [['VP', 'NP']]
    ps_rules["N'"] = [['N']] * 3
    ps_rules["V'"] = [['V']] * 2
    ps_rules['PP'] = []
    if serial_verb:
        ps_rules['VP'] += [['VP', 'VP']]
    ps_rules['NP'] += [['D', "N'"]]
    ps_rules["N'"] += [["N'", 'A'], ["N'", 'PP']]
    ps_rules['PP'] += [['P', 'NP']]
    ps_rules['VP'] += [["V'", 'NP'], ["V'"], ['V', 'S']]
    ps_rules["V'"] += [["V'", 'PP'], ["V'", 'B']]
    for rule in ps_rules['NP']:
        if not head_first: rule.reverse()
    for rule in ps_rules['VP']:
        if not head_first: rule.reverse()
    for rule in ps_rules["N'"]:
        if not head_first: rule.reverse()
    for rule in ps_rules["V'"]:
        if not head_first: rule.reverse()
    for rule in ps_rules['PP']:
        if not head_first: rule.reverse()
    for rule in ps_rules['NP']:
        if random.random() < 0.05: rule.reverse()
    for rule in ps_rules['VP']:
        if random.random() < 0.05: rule.reverse()
    for rule in ps_rules["N'"]:
        if random.random() < 0.05: rule.reverse()
    for rule in ps_rules["V'"]:
        if random.random() < 0.05: rule.reverse()
    for rule in ps_rules['PP']:
        if random.random() < 0.05: rule.reverse()

    with open("dictionary.txt","r",encoding='utf-8') as f:
        lextemp = f.readlines()
    for i in range(len(lextemp)):
        if i % 2 == 0:
            lexicon[lextemp[i].replace('\n','').replace(':','')] = lextemp[i+1].replace('\n','').split(',')
    with open("lexicon.txt","r",encoding='utf-8') as f:
        lextemp = f.readlines()
    for i in range(len(lextemp)):
        if i % 2 == 0:
            englexicon[lextemp[i].replace('\n','').replace(':','')] = lextemp[i+1].replace('\n','').split(',')

    if english_mode:
        ps_rules = english_ps_rules
        verb_attraction = False
        lexicon = {}
        lexicon['Intrans'] = englexicon['Intrans']
        lexicon['Trans'] = englexicon['Trans']
        lexicon['Subjunct'] = englexicon['Subjunct']
        with open('nouns.txt') as f:
            lexicon['N'] = []
            for noun in f:
                lexicon['N'].append(noun.replace('\n','').replace(' ','').replace('[','').replace(']',''))
        with open('adjcs.txt') as f:
            lexicon['A'] = []
            for adjc in f:
                lexicon['A'].append(adjc.replace('\n','').replace(' ','').replace('[','').replace(']',''))
        with open('preps.txt') as f:
            lexicon['P'] = []
            for prep in f:
                lexicon['P'].append(prep.replace('\n','').replace(' ','').replace('[','').replace(']',''))
        lexicon['D'] = ['the', 'a', 'that', 'this']
        englexicon = lexicon



# does some magic to recursively-ish generate a random tree using the PS-rules
# and moves the verb around if we're dealing with verb attraction. The
# tree starts off just "[S[]]", then we loop through the tree until there are no
# "[]" left, replacing "[]" with a possible expansion of it based on the phrase
# structure rules. For example, with English syntax, we might have "[S[]] -
# [S[NP[]VP[]]] - [S[NP[D N'[]]VP[V'[]]]] - S[NP[D N'[N ]]VP[V'[V ]]]]" for a
# simple sentence. Also, handling verb attraction, the Welsh/mirror-Welsh
# scenario, is very tricky, involving looping through the tree, replacing S's
# with S'[S[...] V] or S'[V S[...]] based on the head direction and keeping
# track of where we are in the tree and all of the children of the S node.
def build_sentence():
    nouns = lexicon['N']
    adjcs = lexicon['A']
    preps = lexicon['P']
    detrs = lexicon['D']
    sentence = ''
    verbs = []
    engverbs = []
    tree = '[S[]]'
    done = False
    breakout = True
    start_index = 0
    while not done:
        breakout = False
        for i in range(start_index, len(tree)):
            if tree[i] == '[' and tree[i+1] == ']':
                newtree = tree[:i+1]
                token = ''
                j = i-1
                while not tree[j] == '[' and not tree[j] == ' ' and not tree[j] == ']':
                    token += tree[j]
                    j -= 1
                token = token[::-1]
                if token in ps_rules:
                    rule = random.choice(ps_rules[token])
                    if token == 'VP':
                        if 'NP' in rule:
                            verbs.append(random.choice(lexicon['Trans']))
                            engverbs.append(random.choice(englexicon['Trans']))
                        elif 'S' in rule:
                            verbs.append(random.choice(lexicon['Subjunct']))
                            engverbs.append(random.choice(englexicon['Subjunct']))
                        else:
                            verbs.append(random.choice(lexicon['Intrans']))
                            engverbs.append(random.choice(englexicon['Intrans']))
                    for item in rule:
                        newtree += item
                        if item in ps_rules:
                            newtree += '[]'
                        else:
                            newtree += ' '
                newtree += tree[i+1:]
                tree = newtree
                breakout = True
                break
        start_index = i
        if not breakout:
            done = True
    if verb_attraction:
        done = False
        tree = tree.replace('V ', 'V* ')
        start_index = 0
        while not done:
            broken = False
            for i in range(start_index, len(tree)):
                if tree[i] == 'S' and not tree[i+1] == "'":
                    closing_index = i
                    braces = 1
                    length = 0
                    verb_count = 0
                    in_clause = True
                    while True:
                        if tree[closing_index] == 'V' and not tree[closing_index + 1] == "'" and not tree[closing_index + 1] == 'P' and in_clause:
                            verb_count += 1
                        elif tree[closing_index] == '[':
                            braces += 1
                        elif tree[closing_index] == ']':
                            braces -= 1
                        elif tree[closing_index] == 'S' and closing_index > i + 3:
                            in_clause = False
                        closing_index += 1
                        if braces == 0:
                            break
                    if head_first:
                        length = len("[S'" + 'V ' * verb_count)
                        tree = tree[:i] + "[S'" + 'V ' * verb_count + tree[i:closing_index] + ']' + tree[closing_index:]
                    else:
                        length = len("[S'")
                        tree = tree[:i] + "[S'" + tree[i:closing_index+1] + 'V ' * verb_count + ']' + tree[closing_index+1:]
                    broken = True
                    start_index = i + length + 1
                    break
            if not broken: done = True
    outer_s = True
    vindex = 0
    engsentence = ''
    for i in range(len(tree)):
        char = tree[i]
        if char == 'N' and tree[i+1] == ' ':
            j = random.randrange(len(nouns))
            sentence += nouns[j] + ' '
            engsentence += englexicon['N'][j] + ' '
        elif char == 'V' and tree[i+1] == ' ':
            sentence += verbs[vindex] + ' '
            engsentence += engverbs[vindex] + 's '
            vindex += 1
        elif char == 'P' and tree[i+1] == ' ':
            j = random.randrange(len(preps))
            sentence += preps[j] + ' '
            engsentence += englexicon['P'][j] + ' '
        elif char == 'D' and tree[i+1] == ' ':
            j = random.randrange(len(detrs))
            sentence += detrs[j] + ' '
            engsentence += englexicon['D'][j] + ' '
        elif char == 'A' and tree[i+1] == ' ':
            j = random.randrange(len(adjcs))
            sentence += adjcs[j] + ' '
            engsentence += englexicon['A'][j] + ' '
        elif char == 'B' and tree[i+1] == ' ':
            #j = random.randrange(len(adjcs))
            #sentence += advbs[j] + ' '
            #engsentence += advbs[j] + ' '
            pass
        elif char == 'S':
            if verb_attraction:
                if tree[i+1] == "'" and not outer_s: engsentence += 'that '
                else: outer_s = False
            else:
                if not tree[i+1] == "'" and not outer_s: engsentence += 'that '
                else: outer_s = False
    sentence = (sentence.capitalize()[:-1] + '.').replace('[','').replace(']','')
    engsentence = (engsentence.capitalize()[:-1] + '.').replace('[','').replace(']','')
    if english_mode: sentence = engsentence
    return sentence,engsentence
