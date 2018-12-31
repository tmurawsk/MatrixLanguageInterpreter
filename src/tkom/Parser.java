package tkom;

import tkom.ast.FunctionDef;
import tkom.ast.Variable;
import tkom.ast.expression.*;
import tkom.ast.statement.*;
import tkom.exception.*;

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

    private FunctionDef parseFunctionDef() throws UnexpectedTokenException, UnknownTypeException, MissingReturnStatementException, DuplicateException, NotDefinedException {
        accept(TokenID.Func);
        Token name = accept(TokenID.Name);
        accept(TokenID.RoundBracketOpen);
        LinkedList<Variable> arguments = parseArguments();
        accept(TokenID.RoundBracketClose);
        Token type = accept(TokenID.Name);

        validateType(type);

        FunctionDef functionDef = new FunctionDef(name.getValue(), type.getId(), arguments);

        accept(TokenID.CurlyBracketOpen);
        LinkedList<Statement> statementBlock = parseStatementBlock(functionDef);
        Token endOfFunctionDef = accept(TokenID.CurlyBracketClose);

        if(!containsReturnStatement(statementBlock))
            throw new MissingReturnStatementException(endOfFunctionDef.getPosition(), functionDef);

        return functionDef;
    }

    private void validateType(Token type) throws UnknownTypeException {
        if(!Token.isType(type.getId()))
            throw new UnknownTypeException(type);
    }

    private boolean containsReturnStatement(LinkedList<Statement> statementBlock) {
        for(Statement statement : statementBlock)
            if(statement instanceof ReturnStatement)
                return true;

        return false;
    }

    private LinkedList<Variable> parseArguments() throws UnexpectedTokenException, UnknownTypeException {
        LinkedList<Variable> arguments = new LinkedList<>();

        if(lexer.peekToken().getId() == TokenID.RoundBracketClose)
            return arguments;

        Token type = accept(TokenID.Name);
        validateType(type);
        Token name = accept(TokenID.Name);
        arguments.add(new Variable(type.getId(), name.getValue()));

        while(lexer.peekToken().getId() == TokenID.Comma) {
            accept(TokenID.Comma);
            type = accept(TokenID.Name);
            validateType(type);
            name = accept(TokenID.Name);
            arguments.add(new Variable(type.getId(), name.getValue()));
        }

        return arguments;
    }

    private LinkedList<Statement> parseStatementBlock(Statement parent) throws UnexpectedTokenException, UnknownTypeException, DuplicateException, NotDefinedException {
        LinkedList<Statement> statements = new LinkedList<>();

        boolean isStatementMatched = true;
        while (isStatementMatched) {
            Token firstToken = lexer.peekToken();
            switch(firstToken.getId()) {
                case If:
                    statements.add(parseIfStatement(parent));
                    break;
                case While:
                    statements.add(parseWhileStatement(parent));
                    break;
                case Print:
                    statements.add(parsePrintStatement(parent));
                    break;
                case Read:
                    statements.add(parseReadStatement(parent));
                    break;
                case Return:
                    statements.add(parseReturnStatement(parent));
                    break;
                case Name:
                    validateType(firstToken);
                    Token secondToken = lexer.peekToken();
                    if(secondToken.getId() == TokenID.Assign)
                        statements.add(parseAssignStatement(parent));
                    else if(secondToken.getId() == TokenID.Name)
                        statements.add(parseInitStatement(parent));
                    else
                        throw new UnexpectedTokenException(secondToken);
                    break;
                default:
                    isStatementMatched = false;
            }
        }

        return statements;
    }

    private InitStatement parseInitStatement(Statement parent) throws UnexpectedTokenException, UnknownTypeException, DuplicateException {
        Token type = accept(TokenID.Name);
        validateType(type);
        Token name = accept(TokenID.Name);
        if(parent.variableExists(name.getValue()))
            throw new DuplicateException(name.getPosition(), new Variable(type.getId(), name.getValue()));

        InitStatement initStatement = new InitStatement(parent, type.getId(), name.getValue());
        if(lexer.peekToken().getId() == TokenID.Assign) {
            accept(TokenID.Assign);
            if(lexer.peekToken().getId() == TokenID.SquareBracketOpen) {
                //TODO only if type == mat (because it's matrix size initialization)
                accept(TokenID.SquareBracketOpen);
                MathExpr firstExpr = parseMathExpr(parent);
                //TODO validate if firstExpr.type == num
                accept(TokenID.SquareBracketClose);
                accept(TokenID.SquareBracketOpen);
                MathExpr secondExpr = parseMathExpr(parent);
                //TODO validate if secondExpr.type == num
                accept(TokenID.SquareBracketClose);
                initStatement.setExpressions(firstExpr, secondExpr);
            }
            else {
                MathExpr expr = parseMathExpr(parent);
                //TODO validate if type == expr.type
                initStatement.setExpressions(expr, null);
            }
        }

        parent.addVariable(new Variable(type.getId(), name.getValue()));
        accept(TokenID.Semicolon);
        return initStatement;
    }

    private AssignStatement parseAssignStatement(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        Token name = accept(TokenID.Name);
        Variable variable = parent.getVariable(name.getValue());
        if(variable == null)
            throw new NotDefinedException(name.getPosition(), new Variable(TokenID.Num, name.getValue()));

        MathExpr columnToSet = null;
        MathExpr rowToSet = null;
        if(lexer.peekToken().getId() == TokenID.SquareBracketOpen) {
            accept(TokenID.SquareBracketOpen);
            columnToSet = parseMathExpr(parent);
            //TODO validate if columnToSet.type == num
            accept(TokenID.SquareBracketClose);
            accept(TokenID.SquareBracketOpen);
            rowToSet = parseMathExpr(parent);
            //TODO validate if rowToSet.type == num
            accept(TokenID.SquareBracketClose);
        }

        accept(TokenID.Assign);
        MathExpr expression = parseMathExpr(parent);
        //TODO validate if variable.type == expression.type

        accept(TokenID.Semicolon);
        return new AssignStatement(parent, name.getValue(), columnToSet, rowToSet, expression);
    }

    private FunctionCall parseFunctionCall(Statement parent) {
        return null; //TODO
    }

    private IfStatement parseIfStatement(Statement parent) {
        return null; //TODO
    }

    private WhileStatement parseWhileStatement(Statement parent) {
        return null; //TODO
    }

    private PrintStatement parsePrintStatement(Statement parent) {
        return null; //TODO
    }

    private ReadStatement parseReadStatement(Statement parent) {
        return null; //TODO
    }

    private ReturnStatement parseReturnStatement(Statement parent) {
        return null; //TODO
    }

    private LogicExpr parseLogicExpr(Statement parent) {
        return null; //TODO
    }

    private AndExpr parseAndExpr(Statement parent) {
        return null; //TODO
    }

    private RelationExpr parseRelationExpr(Statement parent) {
        return null; //TODO
    }

    private BaseLogicExpr parseBaseLogicExpr(Statement parent) {
        return null; //TODO
    }

    private LogicExpr parseBracketLogicExpr(Statement parent) {
        return null; //TODO
    }

    private MathExpr parseMathExpr(Statement parent) {
        return null; //TODO
    }

    private MultExpr parseMultExpr(Statement parent) {
        return null; //TODO
    }

    private BaseMathExpr parseBaseMathExpr(Statement parent) {
        return null; //TODO
    }

    private MathExpr parseBracketMathExpr(Statement parent) {
        return null; //TODO
    }

    private Variable parseVariable(Statement parent) {
        return null; //TODO
    }

    private String parsePrintable(Statement parent) {
        return null; //TODO
    }

    private ArrayList<ArrayList<Integer>> parseMatrixLiteral(Statement parent) {
        return null; //TODO
    }

    private ArrayList<Integer> parseMatrixRow(Statement parent) {
        return null; //TODO
    }
}
