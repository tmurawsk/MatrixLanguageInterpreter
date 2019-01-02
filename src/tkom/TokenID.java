package tkom;

public enum TokenID {
    Func, If, Else, While, Print, Read, Return, Num, Mat,
    Semicolon, Comma, RoundBracketOpen, RoundBracketClose, CurlyBracketOpen, CurlyBracketClose, SquareBracketOpen, SquareBracketClose,
    Plus, Minus, Multiply, Divide, Assign,
    Negation, Or, And,
    Equal, Unequal, Less, Greater, LessOrEqual, GreaterOrEqual,
    Name, Number, String,
    Invalid, Eof
}