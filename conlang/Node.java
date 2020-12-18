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

    public Node dominatingDP()
    {
        if (parent != null) {
            if (parent.type == NodeType.D && parent.proj == Projection.Phrase) {
                return parent;
            }
            return parent.dominatingDP();
        }
        return null;
    }

    public boolean specTP()
    {
        Node DP = dominatingDP();
        Node sis = DP == null ? null : dominatingDP().getSister();
        return sis != null && sis.type == NodeType.T && sis.proj == Projection.Bar;
    }

    public boolean compVP()
    {
        Node DP = dominatingDP();
        Node sis = DP == null ? null : dominatingDP().getSister();
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
        if (proj == Projection.Bar) {
            if (specFirst)
                return leftPro + " " + rightPro;
            else
                return rightPro + " " + leftPro;
        }
        if (headFirst)
            return leftPro + " " + rightPro;
        else
            return rightPro + " " + leftPro;
    }

    public String pronounceEnglish()
    {
        if (lexeme != "" && lexeme.charAt(0) != '-' && lexeme.charAt(0) != '+') {
            return lexeme;
        }
        String leftPro  = left  != null ?  left.pronounceEnglish() : "";
        String rightPro = right != null ? right.pronounceEnglish() : "";
        if (proj == Projection.Bar) {
            return leftPro + " " + rightPro;
        }
        return leftPro + " " + rightPro;
    }
}
