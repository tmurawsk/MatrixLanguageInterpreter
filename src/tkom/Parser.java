package tkom;

import tkom.ast.FunctionDef;
import tkom.ast.Variable;
import tkom.ast.expression.*;
import tkom.ast.statement.*;
import tkom.exception.UnexpectedTokenException;

import java.util.ArrayList;
import java.util.LinkedList;

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

    private FunctionDef parseFunctionDef() {
        return null; //TODO
    }

    private LinkedList<Variable> parseArguments() {
        return null; //TODO
    }

    private LinkedList<Statement> parseStatementBlock() {
        return null; //TODO
    }

    private InitStatement parseInitStatement() {
        return null; //TODO
    }

    private AssignStatement parseAssignStatement() {
        return null; //TODO
    }

    private FunctionCall parseFunctionCall() {
        return null; //TODO
    }

    private IfStatement parseIfStatement() {
        return null; //TODO
    }

    private WhileStatement parseWhileStatement() {
        return null; //TODO
    }

    private PrintStatement parsePrintStatement() {
        return null; //TODO
    }

    private ReadStatement parseReadStatement() {
        return null; //TODO
    }

    private ReturnStatement parseReturnStatement() {
        return null; //TODO
    }

    private LogicExpr parseLogicExpr() {
        return null; //TODO
    }

    private AndExpr parseAndExpr() {
        return null; //TODO
    }

    private RelationExpr parseRelationExpr() {
        return null; //TODO
    }

    private BaseLogicExpr parseBaseLogicExpr() {
        return null; //TODO
    }

    private LogicExpr parseBracketLogicExpr() {
        return null; //TODO
    }

    private MathExpr parseMathExpr() {
        return null; //TODO
    }

    private MultExpr parseMultExpr() {
        return null; //TODO
    }

    private BaseMathExpr parseBaseMathExpr() {
        return null; //TODO
    }

    private MathExpr parseBracketMathExpr() {
        return null; //TODO
    }

    private Variable parseVariable() {
        return null; //TODO
    }

    private String parsePrintable() {
        return null; //TODO
    }

    private ArrayList<ArrayList<Integer>> parseMatrixLiteral() {
        return null; //TODO
    }

    private ArrayList<Integer> parseMatrixRow() {
        return null; //TODO
    }
}
