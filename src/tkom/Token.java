package tkom;

import java.util.HashMap;
import java.util.Map;

public class Token {
    public static int MAX_NAME_LENGTH = 100;

    private TokenID id;
    private String value;
    private Position position;

    private static Map<TokenID, String> nameMap = initializeNameMap();

    private static Map<String, TokenID> keywordMap = initializeKeywordMap();

    public Token() {
        this(TokenID.Invalid, new Position());
    }

    public Token(TokenID id, Position position) {
        this(id, position, getKeywordByToken(id));
    }

    public Token(TokenID id, Position position, char c) {
        this(id, position, String.valueOf(c));
    }

    public Token(TokenID id, Position position, String value) {
        this.id = id;
        this.position = position;
        this.value = value;
    }

    public TokenID getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Position getPosition() {
        return position;
    }

    public static String getKeywordByToken(TokenID tokenID) {
        String toReturn = nameMap.get(tokenID);
        return toReturn != null ? toReturn : nameMap.get(TokenID.Invalid);
    }

    public static TokenID getTokenByKeyword(String keyword) {
        TokenID toReturn = keywordMap.get(keyword);
        return toReturn != null ? toReturn : TokenID.Invalid;
    }

    private static Map<TokenID, String> initializeNameMap() {
        Map<TokenID, String> m = new HashMap<>();

        m.put(TokenID.Main,     "Main");
        m.put(TokenID.Func,     "Func");
        m.put(TokenID.If,       "If");
        m.put(TokenID.Else,     "Else");
        m.put(TokenID.While,    "While");
        m.put(TokenID.Print,    "Print");
        m.put(TokenID.Return,   "Return");
        m.put(TokenID.Num,      "Num");
        m.put(TokenID.Mat2,     "Mat2");
        m.put(TokenID.Mat3,     "Mat3");

        m.put(TokenID.Semicolon,            "Semicolon");
        m.put(TokenID.Comma,                "Comma");
        m.put(TokenID.RoundBracketOpen,     "RoundBracketOpen");
        m.put(TokenID.RoundBracketClose,    "RoundBracketClose");
        m.put(TokenID.CurlyBracketOpen,     "CurlyBracketOpen");
        m.put(TokenID.CurlyBracketClose,    "CurlyBracketClose");
        m.put(TokenID.SquareBracketOpen,    "SquareBracketOpen");
        m.put(TokenID.SquareBracketClose,   "SquareBracketClose");

        m.put(TokenID.Plus,     "Plus");
        m.put(TokenID.Minus,    "Minus");
        m.put(TokenID.Multiply, "Multiply");
        m.put(TokenID.Divide,   "Divide");
        m.put(TokenID.Assign,   "Assign");

        m.put(TokenID.Negation, "Negation");
        m.put(TokenID.Or,       "Or");
        m.put(TokenID.And,      "And");

        m.put(TokenID.Equal,                "Equal");
        m.put(TokenID.Unequal,              "Unequal");
        m.put(TokenID.Less,                 "Less");
        m.put(TokenID.Greater,              "Greater");
        m.put(TokenID.LessOrEqual,          "LessOrEqual");
        m.put(TokenID.GreaterOrEqual,       "GreaterOrEqual");

        m.put(TokenID.Name,     "Name");
        m.put(TokenID.Number,   "Number");
        m.put(TokenID.String,   "String");

        m.put(TokenID.Invalid,  "INVALID");
        m.put(TokenID.Eof,      "Eof");

        return m;
    }

    private static Map<String, TokenID> initializeKeywordMap() {
        Map<String, TokenID> m = new HashMap<>();

        m.put("main",   TokenID.Main);
        m.put("func",   TokenID.Func);
        m.put("if",     TokenID.If);
        m.put("else",   TokenID.Else);
        m.put("while",  TokenID.While);
        m.put("print",  TokenID.Print);
        m.put("return", TokenID.Return);
        m.put("num",    TokenID.Num);
        m.put("mat2",   TokenID.Mat2);
        m.put("mat3",   TokenID.Mat3);

        return m;
    }
}
