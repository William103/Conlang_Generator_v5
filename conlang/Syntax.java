package conlang;

import java.util.Random;
import java.io.IOException;

class Syntax
{
    // specifier first
    private boolean specFirst;

    // head first
    private boolean headFirst;

    // always V to T movement
    private boolean VtoT;

    // overt complementizer
    private boolean overtC;

    private Random rand;

    public Syntax()
    {
        rand = new Random(ConlangGenerator.seed);

        // totally made up probabilities
        specFirst = rand.nextInt(10) != 0;
        headFirst = rand.nextBoolean();
        System.out.println(specFirst + " " + headFirst);
        VtoT = rand.nextInt(5) != 0;
        overtC = rand.nextInt(5) == 0;
    }

    public void printSentence(Grammar grammar, Lexicon words)
    {
        try {
            Node root = ParseTree.parse();
            root.setParents();
            System.out.println(root);
            root.handleMovement();
            root.setParents();
            System.out.println("");
            System.out.println(root);
            System.out.println(root.pronounce(grammar, words, specFirst, headFirst));
            System.out.println(root.pronounceEnglish());
            System.out.println(root.pronounceLiteral(specFirst, headFirst));
        } catch (IOException e) {
            System.err.println("Something went wrong: " + e.getMessage());
        }
    }
}
