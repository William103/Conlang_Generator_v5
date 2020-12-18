package conlang;

import java.io.FileInputStream;
import java.io.IOException;

class ParseTree
{
    private static class ParseError extends RuntimeException
    {
        public ParseError(String msg)
        {
            super();
            System.err.println(msg);
        }
    }
    private static enum TokenType { OpenBracket, CloseBracket, Node, None }
    private static class Token {
        public TokenType type;
        public String val;
        public int line;
        public Token(TokenType t, String v, int l) {
            type = t; val = v; line = l;
        }
    }

    private static final String treePath = "tree.txt";

    private static FileInputStream in;
    private static int line = 1;
    private static int subtrees = 1;

    public static Node parse() throws IOException
    {
        try {
            in = new FileInputStream(treePath);

            return subtree();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private static Node subtree() throws IOException
    {
        Token t = nextToken();
        if (t.type != TokenType.OpenBracket) {
            throw new ParseError("Invalid syntax on line " + t.line + "!");
        }
        t = nextToken();
        if (t.type == TokenType.CloseBracket) {
            return null;
        }
        
        NodeType type;
        switch (t.val.charAt(0)) {
            case 'C':
                type = NodeType.C;
                break;
            case 'T':
                type = NodeType.T;
                break;
            case 'D':
                type = NodeType.D;
                break;
            case 'N':
                type = NodeType.N;
                break;
            case 'P':
                type = NodeType.P;
                break;
            case 'A':
                type = NodeType.A;
                break;
            case 'V':
                type = NodeType.V;
                break;
            default:
                System.err.println(t.val);
                throw new ParseError("Invalid node type " + t.val.charAt(0));
        }

        if (t.val.length() == 1) {
            t = nextToken();
            if (t.type == TokenType.CloseBracket)
                return new Node(type, Projection.Word, null, null);
            if (t.type == TokenType.OpenBracket)
                throw new ParseError("Invalid syntax on line " + t.line);
            consume(TokenType.CloseBracket);
            return new Node(type, Projection.Word, null, null, t.val);
        }
        if (t.val.charAt(1) == 'P') {
            Node left = subtree();
            Node right = subtree();
            consume(TokenType.CloseBracket);
            return new Node(type, Projection.Phrase, left, right);
        } else if (t.val.charAt(1) == '\'') {
            Node left = subtree();
            Node right = subtree();
            consume(TokenType.CloseBracket);
            return new Node(type, Projection.Bar, left, right);
        }
        throw new ParseError("Invalid projection " + t.val + 
                "\nline " + t.line);
    }

    private static Token nextToken() throws IOException
    {
        String s = "";
        int c = in.read();
        if (c == -1) {
            return new Token(TokenType.None, "", line);
        }
        while (Character.isWhitespace(c)) {
            if (c == '\n') line++;
            c = in.read();
        }

        if (c == '[') {
            return new Token(TokenType.OpenBracket, null, line);
        } else if (c == ']') {
            return new Token(TokenType.CloseBracket, null, line);
        }

        while (!Character.isWhitespace(c) && c != '[' && c != ']') {
            s += (char)c;
            c = in.read();
        }
        return new Token(TokenType.Node, s, line);
    }

    private static void consume(TokenType type) throws IOException
    {
        Token t = nextToken();
        if (t.type != type) {
            throw new ParseError("Invalid syntax on line " + t.line);
        }
    }

    public static void main(String[] args)
    {
        try {
            Node root = parse();
            root.setParents();
            System.out.println(parse());
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
