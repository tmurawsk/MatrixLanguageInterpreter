package tkom;

import tkom.ast.FunctionDef;
import tkom.ast.Program;
import tkom.ast.VariableCall;
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
        if (nextToken.getId() != tokenID)
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

        if (!containsReturnStatement(statementBlock))
            throw new MissingReturnStatementException(endOfFunctionDef.getPosition(), functionDef);

        return functionDef;
    }

    private void validateType(Token type) throws UnknownTypeException {
        if (!Token.isType(type.getId()))
            throw new UnknownTypeException(type);
    }

    private boolean containsReturnStatement(LinkedList<Statement> statementBlock) {
        for (Statement statement : statementBlock)
            if (statement instanceof ReturnStatement)
                return true;

        return false;
    }

    private LinkedList<Variable> parseArguments() throws UnexpectedTokenException, UnknownTypeException {
        LinkedList<Variable> arguments = new LinkedList<>();

        if (lexer.peekToken().getId() == TokenID.RoundBracketClose)
            return arguments;

        Token type = accept(TokenID.Name);
        validateType(type);
        Token name = accept(TokenID.Name);
        arguments.add(new Variable(type.getId(), name.getValue()));

        while (lexer.peekToken().getId() == TokenID.Comma) {
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
            switch (firstToken.getId()) {
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
                    Token secondToken = lexer.peekFollowingToken();
                    if (secondToken.getId() == TokenID.Assign)
                        statements.add(parseAssignStatement(parent));
                    else if (secondToken.getId() == TokenID.Name)
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
        if (parent.variableExists(name.getValue()))
            throw new DuplicateException(name.getPosition(), new Variable(type.getId(), name.getValue()));

        InitStatement initStatement = new InitStatement(parent, type.getId(), name.getValue());
        if (lexer.peekToken().getId() == TokenID.Assign) {
            accept(TokenID.Assign);
            if (lexer.peekToken().getId() == TokenID.SquareBracketOpen) {
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
            } else {
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
        VariableCall variableCall = parseVariable(parent);
        accept(TokenID.Assign);
        MathExpr expression = parseMathExpr(parent);
        //TODO validate if variableCall.type == expression.type
        accept(TokenID.Semicolon);
        return new AssignStatement(parent, variableCall, expression);
    }

    private FunctionCall parseFunctionCall(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        Token name = accept(TokenID.Name);
        accept(TokenID.RoundBracketOpen);
        FunctionCall functionCall = new FunctionCall(parent, name.getValue());

        if (lexer.peekToken().getId() != TokenID.RoundBracketClose) {
            MathExpr expr = parseMathExpr(parent);
            functionCall.addArgument(expr);
            while (lexer.peekToken().getId() == TokenID.Comma) {
                accept(TokenID.Comma);
                expr = parseMathExpr(parent);
                functionCall.addArgument(expr);
            }
        }

        accept(TokenID.RoundBracketClose);
        if (!Program.functionExists(functionCall))
            throw new NotDefinedException(name.getPosition(), functionCall);

        return functionCall;
    }

    private IfStatement parseIfStatement(Statement parent) throws UnexpectedTokenException, DuplicateException, UnknownTypeException, NotDefinedException {
        accept(TokenID.If);
        accept(TokenID.RoundBracketOpen);
        LogicExpr condition = parseLogicExpr(parent);
        accept(TokenID.RoundBracketClose);
        accept(TokenID.CurlyBracketOpen);

        IfStatement ifStatement = new IfStatement(parent, condition);
        ifStatement.setIfStatements(parseStatementBlock(ifStatement));

        accept(TokenID.CurlyBracketClose);

        if (lexer.peekToken().getId() != TokenID.Else) {
            accept(TokenID.CurlyBracketOpen);
            ifStatement.setElseStatements(parseStatementBlock(parent));
            accept(TokenID.CurlyBracketClose);
        }

        return ifStatement;
    }

    private WhileStatement parseWhileStatement(Statement parent) throws UnexpectedTokenException, DuplicateException, UnknownTypeException, NotDefinedException {
        accept(TokenID.While);
        accept(TokenID.RoundBracketOpen);
        LogicExpr condition = parseLogicExpr(parent);
        accept(TokenID.RoundBracketClose);
        accept(TokenID.CurlyBracketOpen);

        WhileStatement whileStatement = new WhileStatement(parent, condition);
        whileStatement.setStatements(parseStatementBlock(whileStatement));

        accept(TokenID.CurlyBracketClose);
        return whileStatement;
    }

    private PrintStatement parsePrintStatement(Statement parent) throws UnexpectedTokenException {
        accept(TokenID.Print);
        accept(TokenID.RoundBracketOpen);

        PrintStatement printStatement = new PrintStatement(parent);
        Token nextToken = lexer.peekToken();
        if (nextToken.getId() != TokenID.RoundBracketClose) {
            if (nextToken.getId() == TokenID.String)
                printStatement.addPrintable(accept(TokenID.String).getValue());
            else
                printStatement.addPrintable(parseMathExpr(parent));

            while (lexer.peekToken().getId() == TokenID.Comma) {
                accept(TokenID.Comma);
                if (lexer.peekToken().getId() == TokenID.String)
                    printStatement.addPrintable(accept(TokenID.String).getValue());
                else
                    printStatement.addPrintable(parseMathExpr(parent));
            }
        }

        accept(TokenID.RoundBracketClose);
        accept(TokenID.Semicolon);
        return printStatement;
    }

    private ReadStatement parseReadStatement(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        accept(TokenID.Read);
        accept(TokenID.RoundBracketOpen);

        ReadStatement readStatement = new ReadStatement(parent, parseVariable(parent));

        accept(TokenID.RoundBracketClose);
        accept(TokenID.Semicolon);
        return readStatement;
    }

    private Variable validateVariable(Statement parent, Token name) throws NotDefinedException {
        Variable variable = parent.getVariable(name.getValue());
        if (variable == null)
            throw new NotDefinedException(name.getPosition(), new Variable(TokenID.Num, name.getValue()));
        return variable;
    }

    private ReturnStatement parseReturnStatement(Statement parent) throws UnexpectedTokenException {
        accept(TokenID.Return);
        MathExpr expr = parseMathExpr(parent);
        //TODO iterate up to parents, find FunctionDef and validate if functionDef.returnType == expr.type
        return new ReturnStatement(parent, expr);
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

    private MathExpr parseMathExpr(Statement parent) throws UnexpectedTokenException {
        MathExpr mathExpr = new MathExpr(parent, parseMultExpr(parent));
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() == TokenID.Plus || nextToken.getId() == TokenID.Minus) {
            if (nextToken.getId() == TokenID.Plus) {
                accept(TokenID.Plus);
                mathExpr.addPlus(parseMultExpr(parent));
            } else {
                accept(TokenID.Minus);
                mathExpr.addMinus(parseMultExpr(parent));
            }
            nextToken = lexer.peekToken();
        }

        return mathExpr;
    }

    private MultExpr parseMultExpr(Statement parent) throws UnexpectedTokenException {
        MultExpr multExpr = new MultExpr(parent, parseBaseMathExpr(parent));
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() == TokenID.Multiply || nextToken.getId() == TokenID.Divide) {
            if (nextToken.getId() == TokenID.Multiply) {
                accept(TokenID.Multiply);
                multExpr.addMultiply(parseBaseMathExpr(parent));
            } else {
                accept(TokenID.Divide);
                multExpr.addDivide(parseBaseMathExpr(parent));
            }
            nextToken = lexer.peekToken();
        }

        return multExpr;
    }

    private BaseMathExpr parseBaseMathExpr(Statement parent) throws UnexpectedTokenException {
        Token nextToken = lexer.peekToken();
        boolean isMinus = false;
        if (nextToken.getId() == TokenID.Minus) {
            accept(TokenID.Minus);
            isMinus = true;
            nextToken = lexer.peekToken();
        }

        if (nextToken.getId() == TokenID.RoundBracketOpen)
            return new BaseMathExpr(parent, isMinus, parseBracketMathExpr(parent));

//        if (nextToken.getId())
        //TODO finish this function, change everywhere parsing variable - it should take array of arrays of Value, not MathExpr
        return null;
    }

    private MathExpr parseBracketMathExpr(Statement parent) throws UnexpectedTokenException {
        accept(TokenID.RoundBracketOpen);
        MathExpr mathExpr = parseMathExpr(parent);
        accept(TokenID.RoundBracketClose);
        return mathExpr;
    }

    private VariableCall parseVariable(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        Token name = accept(TokenID.Name);
        VariableCall variableCall = new VariableCall(validateVariable(parent, name));
        if (lexer.peekToken().getId() == TokenID.SquareBracketOpen) {
            //TODO only if variable.type == mat
            accept(TokenID.SquareBracketOpen);
            MathExpr column = parseMathExpr(parent);
            //TODO validate if column.type == num
            variableCall.setColumn(column);
            accept(TokenID.SquareBracketClose);

            accept(TokenID.SquareBracketOpen);
            MathExpr row = parseMathExpr(parent);
            //TODO validate if row.type == num
            variableCall.setRow(row);
            accept(TokenID.SquareBracketClose);
        }
        return variableCall;
    }

    private ArrayList<ArrayList<MathExpr>> parseMatrixLiteral(Statement parent) throws UnexpectedTokenException {
        accept(TokenID.SquareBracketOpen);
        ArrayList<ArrayList<MathExpr>> matrix = new ArrayList<>();
        ArrayList<MathExpr> firstRow = parseMatrixRow(parent, 0);
        int rowSizeLimit = firstRow.size();
        matrix.add(firstRow);
        while (lexer.peekToken().getId() == TokenID.Semicolon) {
            accept(TokenID.Semicolon);
            matrix.add(parseMatrixRow(parent, rowSizeLimit));
        }
        accept(TokenID.SquareBracketClose);
        return matrix;
    }

    private ArrayList<MathExpr> parseMatrixRow(Statement parent, int rowSizeLimit) throws UnexpectedTokenException {
        ArrayList<MathExpr> row = new ArrayList<>();
        MathExpr expr = parseMathExpr(parent);
        row.add(expr);
        int rowSize = 1;
        while (lexer.peekToken().getId() == TokenID.Comma && (rowSizeLimit <= 0 || rowSize < rowSizeLimit)) {
            accept(TokenID.Comma);
            row.add(parseMathExpr(parent));
            rowSize++;
        }
        return row;
    }
}
