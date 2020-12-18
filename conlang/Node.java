package conlang;

class Node
{
    private NodeType type;
    private Projection proj;
    private String lexeme;

    private Node left;      // specifier/complement
    private Node right;     // bar/head
    private Node parent;

    public Node(NodeType t, Projection p, Node l, Node r)
    {
        type = t;
        proj = p;
        left = l;
        right = r;
        parent = null;
        lexeme = "";
    }

    public Node(NodeType t, Projection p, Node l, Node r, String lex)
    {
        type = t;
        proj = p;
        left = l;
        right = r;
        lexeme = lex;
        parent = null;
    }

    public void setLexeme(String lex)
    {
        lexeme = lex;
    }

    @Override
    public String toString()
    {
        String res = "[" + type.name() + " " + proj.name() + " " + lexeme + " ";
        if (left != null) {
            res += left.toString();
            res += " ";
        }
        if (right != null) {
            res += right.toString();
        }
        return res + "]";
    }

    public void setParents()
    {
        if (left != null) {
            left.parent = this;
            left.setParents();
        }
        if (right != null) {
            right.parent = this;
            right.setParents();
        }
    }

    public void handleMovement()
    {
        if (type == NodeType.V && proj == Projection.Word) {
            move(NodeType.T, Projection.Word);
        }
        if (type == NodeType.D && proj == Projection.Phrase) {
            move(NodeType.T, Projection.Phrase);
        }
        if (left != null)
            left.handleMovement();
        if (right != null)
            right.handleMovement();
    }

    public Node getSister()
    {
        if (parent != null)
        {
            if (parent.left == this)
                return parent.right;
            else
                return parent.left;
        }
        return null;
    }

    public Node dominatingNode(NodeType type, Projection proj)
    {
        if (parent != null) {
            if (parent.type == type && parent.proj == proj) {
                return parent;
            }
            return parent.dominatingNode(type, proj);
        }
        return null;
    }

    public boolean specTP()
    {
        Node DP = dominatingNode(NodeType.D, Projection.Phrase);
        Node sis = DP == null ? null : DP.getSister();
        return sis != null && sis.type == NodeType.T && sis.proj == Projection.Bar;
    }

    public boolean compVP()
    {
        Node DP = dominatingNode(NodeType.D, Projection.Phrase);
        Node sis = DP == null ? null : DP.getSister();
        return sis != null && sis.type == NodeType.V && sis.proj == Projection.Word;
    }

    public String pronounce(Grammar gram, Lexicon lex, boolean specFirst, boolean headFirst)
    {
        if (lexeme != "" && lexeme.charAt(0) != '-' && lexeme.charAt(0) != '+') {
            String res = lex.getWord(lexeme);
            if (specTP()) {
                res += gram.findNounEnding("nominative", "singular");
            } else if (compVP()) {
                res += gram.findNounEnding("accusative", "singular");
            }
            return res;
        }
        String leftPro  = left  != null ?  left.pronounce(gram, lex, specFirst, headFirst) : "";
        String rightPro = right != null ? right.pronounce(gram, lex, specFirst, headFirst) : "";
        if (proj == Projection.Phrase) {
            if (specFirst)
                return leftPro + " " + rightPro;
            else
                return rightPro + " " + leftPro;
        }
        if (headFirst)
            return rightPro + " " + leftPro;
        else
            return leftPro + " " + rightPro;
    }

    public String pronounceEnglish()
    {
        if (lexeme != "" && lexeme.charAt(0) != '-' && lexeme.charAt(0) != '+') {
            return lexeme;
        }
        String leftPro  = left  != null ?  left.pronounceEnglish() : "";
        String rightPro = right != null ? right.pronounceEnglish() : "";
        return leftPro + " " + rightPro;
    }

    public String pronounceLiteral(boolean specFirst, boolean headFirst)
    {
        if (lexeme != "" && lexeme.charAt(0) != '-' && lexeme.charAt(0) != '+') {
            String res = lexeme;
            if (specTP()) {
                res += "-nom";
            } else if (compVP()) {
                res += "-acc";
            }
            return res;
        }
        String leftPro  = left  != null ?  left.pronounceLiteral(specFirst, headFirst) : "";
        String rightPro = right != null ? right.pronounceLiteral(specFirst, headFirst) : "";
        if (proj == Projection.Phrase) {
            if (specFirst)
                return leftPro + " " + rightPro;
            else
                return rightPro + " " + leftPro;
        }
        if (headFirst)
            return rightPro + " " + leftPro;
        else
            return leftPro + " " + rightPro;
    }

    public void move(NodeType targetType, Projection targetLevel)
    {
        Node target = dominatingNode(targetType, targetLevel);
        if (target == null) {
            // System.err.println("Target not found");
            return;
        }
        switch (targetLevel) {
            // move to specifier
            case Phrase:
                if (target.left == null) {
                    target.left = this;
                    // parent = target;
                } else if (target.right == null) {
                    target.right = this;
                    // parent = target;
                } else {
                    // System.err.println("Node already there");
                    return;
                }
                if (parent.left == this) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }

            // head to head movement
            case Word:
                target.lexeme = lexeme;
                lexeme = "";
                break;

            // invalid movement?
            case Bar:
                // System.err.println("Invalid movement");
                break;
        }
    }
}
