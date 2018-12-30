package tkom;

import tkom.exception.UnexpectedTokenException;

public class Parser {
    private Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void parseProgram() {
//        do {
//
//        } while ();
    }

    private Token accept(TokenID tokenID) throws UnexpectedTokenException {
        Token nextToken = lexer.readToken();
        if(nextToken.getId() != tokenID)
            throw new UnexpectedTokenException(nextToken);
        return nextToken;
    }
}
