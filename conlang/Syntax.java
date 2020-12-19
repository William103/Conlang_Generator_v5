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

    // subject leaves VP
    public boolean VPExternalSubject;

    private Random rand;

    public Syntax()
    {
        rand = new Random(ConlangGenerator.seed);

        // totally made up probabilities
        specFirst = rand.nextInt(10) != 0;
        headFirst = rand.nextBoolean();
        VtoT = rand.nextInt(5) != 0;
        overtC = rand.nextInt(5) == 0;
        VPExternalSubject = false;
        // VPExternalSubject = rand.nextInt(10) != 0;
        System.out.println("specFirst:\t\t" + specFirst);
        System.out.println("headFirst:\t\t" + headFirst);
        System.out.println("VtoT:\t\t\t" + VtoT);
        System.out.println("VPExternalSubject:\t" + VPExternalSubject);
    }

    public void handleMovement(Node root)
    {
        if (VtoT) {
            root.handleMovement(NodeType.T, Projection.Word,
                                NodeType.V, Projection.Word,
                                null, null);
            root.setParents();
        }

        if (VPExternalSubject) {
            root.handleMovement(NodeType.T, Projection.Phrase,
                                NodeType.D, Projection.Phrase,
                                NodeType.V, Projection.Phrase);
            root.setParents();
        }
    }

    public void printSentence(Node root, Grammar grammar, Lexicon words)
    {
        root.setParents();
        handleMovement(root);
        System.out.println("\n" + root);
        System.out.println("");
        System.out.println(root.pronounce(this, grammar, words, specFirst, headFirst));
        System.out.println(root.pronounceEnglish());
        System.out.println(root.pronounceLiteral(this, specFirst, headFirst));
    }
}
