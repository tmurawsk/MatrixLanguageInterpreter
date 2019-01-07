package tkom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Token {
    static int MAX_NAME_LENGTH = 100;

    private TokenID id;
    private String value;
    private Position position;

    private static Map<TokenID, String> nameMap = initializeNameMap();

    private static Map<String, TokenID> keywordMap = initializeKeywordMap();

    private static HashSet<TokenID> types = initializeTypeSet();

    private static HashSet<TokenID> relationOperators = initializeRelationOperatorsSet();

    Token(TokenID id, Position position) {
        this(id, position, getNameByToken(id));
    }

    Token(TokenID id, Position position, char c) {
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

    public static String getNameByToken(TokenID tokenID) {
        String toReturn = nameMap.get(tokenID);
        return toReturn != null ? toReturn : nameMap.get(TokenID.Invalid);
    }

    static TokenID getTokenByKeyword(String keyword) {
        TokenID toReturn = keywordMap.get(keyword);
        return toReturn != null ? toReturn : TokenID.Invalid;
    }

    static boolean isType(TokenID tokenID) {
        return types.contains(tokenID);
    }

    static boolean isRelationOperator(TokenID tokenID) {
        return relationOperators.contains(tokenID);
    }

    private static Map<TokenID, String> initializeNameMap() {
        Map<TokenID, String> m = new HashMap<>();

        m.put(TokenID.Func,     "Func");
        m.put(TokenID.If,       "If");
        m.put(TokenID.Else,     "Else");
        m.put(TokenID.While,    "While");
        m.put(TokenID.Print,    "Print");
        m.put(TokenID.Read,     "Read");
        m.put(TokenID.Return,   "Return");
        m.put(TokenID.Num,      "Num");
        m.put(TokenID.Mat,      "Mat");

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

        m.put("func",   TokenID.Func);
        m.put("if",     TokenID.If);
        m.put("else",   TokenID.Else);
        m.put("while",  TokenID.While);
        m.put("print",  TokenID.Print);
        m.put("read",   TokenID.Read);
        m.put("return", TokenID.Return);
        m.put("num",    TokenID.Num);
        m.put("mat",    TokenID.Mat);

        return m;
    }

    private static HashSet<TokenID> initializeTypeSet() {
        HashSet<TokenID> set = new HashSet<>();

        set.add(TokenID.Num);
        set.add(TokenID.Mat);

        return set;
    }

    private static HashSet<TokenID> initializeRelationOperatorsSet() {
        HashSet<TokenID> set = new HashSet<>();

        set.add(TokenID.Equal);
        set.add(TokenID.Unequal);
        set.add(TokenID.Less);
        set.add(TokenID.Greater);
        set.add(TokenID.LessOrEqual);
        set.add(TokenID.GreaterOrEqual);

        return set;
    }
}
