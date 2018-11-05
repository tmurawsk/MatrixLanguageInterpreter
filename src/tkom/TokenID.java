package tkom;

public enum TokenID {
    Main, Func, If, Else, While, Print, Return, Num, Mat2, Mat3,
    Semicolon, Comma, RoundBracketOpen, RoundBracketClose, CurlyBracketOpen, CurlyBracketClose, SquareBracketOpen, SquareBracketClose,
    Plus, Minus, Multiply, Divide, Assign,
    Negation, Or, And,
    Equal, Unequal, Less, Greater, LessOrEqual, GreaterOrEqual,
    Name, Number, String,
    Invalid, Eof
}