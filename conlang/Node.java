package conlang;

import java.util.Set;
import java.util.HashSet;

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

    public void handleMovement(NodeType targetType, Projection targetProj, 
                               NodeType movingType, Projection movingProj,
                               NodeType sourceType, Projection sourceProj)
    {
        if (left != null) {
            left.handleMovement(targetType, targetProj,
                                movingType, movingProj,
                                sourceType, sourceProj);
        }
        if (right != null) {
            right.handleMovement(targetType, targetProj,
                                 movingType, movingProj,
                                 sourceType, sourceProj);
        }
        if (type != movingType || proj != movingProj) return;
        if (parent == null && sourceType != null && sourceProj != null) return;
        if (sourceType != null && sourceProj != null && parent.type !=
                sourceType && parent.proj != sourceProj) return;
        move(targetType, targetProj);
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

    public boolean specVP()
    {
        Node DP = dominatingNode(NodeType.D, Projection.Phrase);
        Node sis = DP == null ? null : DP.getSister();
        return sis != null && sis.type == NodeType.V && sis.proj == Projection.Bar;
    }

    public boolean compVP()
    {
        Node DP = dominatingNode(NodeType.D, Projection.Phrase);
        Node sis = DP == null ? null : DP.getSister();
        return sis != null && sis.type == NodeType.V && sis.proj == Projection.Word;
    }

    public String pronounce(Syntax s, Grammar gram,
            Lexicon lex, boolean specFirst, boolean headFirst)
    {
        if (lexeme != "" && lexeme.charAt(0) != '-' && lexeme.charAt(0) != '+') {
            String res = lex.getWord(lexeme);
            if (compVP()) {
                res += gram.findNounEnding("accusative", "singular");
            }
            if (s.VPExternalSubject) {
                if (specTP()) {
                    res += gram.findNounEnding("nominative", "singular");
                }
            } else if (specVP()) {
                res += gram.findNounEnding("nominative", "singular");
            }
            return res;
        }
        String leftPro  = left  != null ?
            left.pronounce(s, gram, lex, specFirst, headFirst) : "";
        String rightPro = right != null ?
            right.pronounce(s, gram, lex, specFirst, headFirst) : "";

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

    public String pronounceLiteral(Syntax s, boolean specFirst, boolean headFirst)
    {
        if (lexeme != "" && lexeme.charAt(0) != '-' && lexeme.charAt(0) != '+') {
            String res = lexeme;
            if (compVP()) {
                res += "-acc";
            }
            if (s.VPExternalSubject) {
                if (specTP()) {
                    res += "-nom";
                }
            } else if (specVP()) {
                res += "-nom";
            }
            return res;
        }
        String leftPro  = left  != null ?  left.pronounceLiteral(s, specFirst, headFirst) : "";
        String rightPro = right != null ? right.pronounceLiteral(s, specFirst, headFirst) : "";
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
        switch (targetLevel) {
            // move to specifier
            case Phrase:
                Node target = dominatingNode(targetType, targetLevel);
                if (target == null) return;
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
                break;

            // head to head movement
            case Word:
                headMove(targetType);
                break;

            // invalid movement?
            case Bar:
                // System.err.println("Invalid movement");
                break;
        }
    }

    public Set<Node> dominatedNodes()
    {
        HashSet<Node> ret = new HashSet<>();
        if (left != null) {
            ret.add(left);
            ret.addAll(left.dominatedNodes());
        }
        if (right != null) {
            ret.add(right);
            ret.addAll(right.dominatedNodes());
        }
        return ret;
    }

    public void headMove(NodeType type)
    {
        System.out.println("HEAD MOVE");
        Node current = parent;
        Set<Node> dominated = dominatedNodes();
        while (current != null) {
            Set<Node> candidates = current.dominatedNodes();
            candidates.removeAll(dominated);
            for (Node candidate : candidates) {
                if (candidate.proj == Projection.Word && candidate.type == type) {
                    candidate.lexeme = lexeme;
                    lexeme = "<" + lexeme + ">";
                    return;
                }
            }
            current = current.parent;
        }
    }
}
