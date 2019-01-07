package tkom;

import tkom.ast.*;
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

    public void parseProgram() throws ParseException {
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() != TokenID.Eof) {
            switch (nextToken.getId()) {
                case Func:
                    Program.addFunction(parseFunctionDef());
                    break;
                case Num:
                case Mat:
                    Program.addInitStatement(parseInitStatement(null));
                    break;
                default:
                    throw new UnexpectedTokenException(nextToken);
            }
            nextToken = lexer.peekToken();
        }
    }

    private Token accept(TokenID tokenID) throws ParseException {
        Token nextToken = lexer.readToken();
        if (nextToken.getId() != tokenID)
            throw new UnexpectedTokenException(nextToken);
        return nextToken;
    }

    private FunctionDef parseFunctionDef() throws ParseException {
        Token firstToken = accept(TokenID.Func);
        Token name = accept(TokenID.Name);
        accept(TokenID.RoundBracketOpen);
        LinkedList<Variable> arguments = parseArguments();
        accept(TokenID.RoundBracketClose);
        Token type = parseType();

        FunctionDef functionDef = new FunctionDef(firstToken.getPosition(), name.getValue(), type.getId(), arguments);
        if (Program.isFunctionDuplicate(functionDef))
            throw new DuplicateException(functionDef.getPosition(), functionDef);

        accept(TokenID.CurlyBracketOpen);
        LinkedList<Statement> statementBlock = parseStatementBlock(functionDef);
        Token endOfFunctionDef = accept(TokenID.CurlyBracketClose);

        if (!containsReturnStatement(statementBlock))
            throw new MissingReturnStatementException(endOfFunctionDef.getPosition(), functionDef);

        functionDef.setStatements(statementBlock);
        return functionDef;
    }

    private void validateType(Token type) throws ParseException {
        if (!Token.isType(type.getId()))
            throw new UnknownTypeException(type);
    }

    private Token parseType() throws ParseException {
        Token type = lexer.readToken();
        validateType(type);
        return type;
    }

    private boolean containsReturnStatement(LinkedList<Statement> statementBlock) {
        for (Statement statement : statementBlock)
            if (statement instanceof ReturnStatement)
                return true;

        return false;
    }

    private LinkedList<Variable> parseArguments() throws ParseException {
        LinkedList<Variable> arguments = new LinkedList<>();

        if (lexer.peekToken().getId() == TokenID.RoundBracketClose)
            return arguments;

        Token type = parseType();
        Token name = accept(TokenID.Name);
        arguments.add(new Variable(type.getId(), name.getValue()));

        while (lexer.peekToken().getId() == TokenID.Comma) {
            accept(TokenID.Comma);
            type = parseType();
            name = accept(TokenID.Name);
            arguments.add(new Variable(type.getId(), name.getValue()));
        }

        return arguments;
    }

    private LinkedList<Statement> parseStatementBlock(Statement parent) throws ParseException {
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
                case Num:
                case Mat:
                    statements.add(parseInitStatement(parent));
                    break;
                case Name:
                    Token secondToken = lexer.peekFollowingToken();
                    if (secondToken.getId() == TokenID.RoundBracketOpen)
                        statements.add(parseFunctionCall(parent));
                    else
                        statements.add(parseAssignStatement(parent));
                    break;
                default:
                    isStatementMatched = false;
            }
        }
        return statements;
    }

    private InitStatement parseInitStatement(Statement parent) throws ParseException {
        Token type = parseType();
        Token name = accept(TokenID.Name);
        InitStatement initStatement = new InitStatement(parent, type.getPosition(), type.getId(), name.getValue());

        if (initStatement.variableExists(name.getValue()))
            throw new DuplicateException(name.getPosition(), new Variable(type.getId(), name.getValue()));

        if (lexer.peekToken().getId() == TokenID.Assign) {
            accept(TokenID.Assign);
            if (lexer.peekToken().getId() == TokenID.SquareBracketOpen && type.getId() == TokenID.Mat) {
                initStatement.setExpressions(parseMatrixDimension(parent), parseMatrixDimension(parent));
            } else {
                MathExpr expr = parseMathExpr(parent);
                if (type.getId() != expr.getType())
                    throw new TypeMismatchException(expr.getPosition(), type.getId(), expr.getType());
                initStatement.setExpressions(expr, null);
            }
        }

        Variable newVariable = new Variable(type.getId(), name.getValue());
        if (parent != null)
            parent.addVariable(newVariable);
        else
            Program.addGlobalVariable(newVariable);

        accept(TokenID.Semicolon);
        return initStatement;
    }

    private MathExpr parseMatrixDimension(Statement parent) throws ParseException {
        accept(TokenID.SquareBracketOpen);
        MathExpr expr = parseMathExpr(parent);
        if (expr.getType() != TokenID.Num)
            throw new TypeMismatchException(expr.getPosition(), TokenID.Num, expr.getType());
        accept(TokenID.SquareBracketClose);
        return expr;
    }

    private AssignStatement parseAssignStatement(Statement parent) throws ParseException {
        VariableCall variableCall = parseVariableCall(parent);
        accept(TokenID.Assign);
        MathExpr expression = parseMathExpr(parent);
        if (variableCall.getType() != expression.getType())
            throw new TypeMismatchException(expression.getPosition(), variableCall.getType(), expression.getType());
        accept(TokenID.Semicolon);
        return new AssignStatement(parent, variableCall.getPosition(), variableCall, expression);
    }

    private FunctionCall parseFunctionCall(Statement parent) throws ParseException {
        Token name = accept(TokenID.Name);
        accept(TokenID.RoundBracketOpen);
        FunctionCall functionCall = new FunctionCall(parent, name.getPosition(), name.getValue());

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
        FunctionDef functionDef = Program.getFunctionDef(functionCall);
        if (functionDef == null)
            throw new NotDefinedException(name.getPosition(), functionCall);

        functionCall.setFunctionDef(functionDef);
        return functionCall;
    }

    private IfStatement parseIfStatement(Statement parent) throws ParseException {
        Token firstToken = accept(TokenID.If);
        accept(TokenID.RoundBracketOpen);
        LogicExpr condition = parseLogicExpr(parent);
        accept(TokenID.RoundBracketClose);
        accept(TokenID.CurlyBracketOpen);

        IfStatement ifStatement = new IfStatement(parent, firstToken.getPosition(), condition);
        ifStatement.setStatements(parseStatementBlock(ifStatement));

        accept(TokenID.CurlyBracketClose);

        if (lexer.peekToken().getId() == TokenID.Else)
            ifStatement.setElseStatement(parseElseStatement(parent));

        return ifStatement;
    }

    private IfStatement parseElseStatement(Statement parent) throws ParseException {
        Token firstToken = accept(TokenID.Else);
        accept(TokenID.CurlyBracketOpen);
        IfStatement elseStatement = new IfStatement(parent, firstToken.getPosition(), null);
        elseStatement.setStatements(parseStatementBlock(elseStatement));
        accept(TokenID.CurlyBracketClose);
        return elseStatement;
    }

    private WhileStatement parseWhileStatement(Statement parent) throws ParseException {
        Token firstToken = accept(TokenID.While);
        accept(TokenID.RoundBracketOpen);
        LogicExpr condition = parseLogicExpr(parent);
        accept(TokenID.RoundBracketClose);
        accept(TokenID.CurlyBracketOpen);

        WhileStatement whileStatement = new WhileStatement(parent, firstToken.getPosition(), condition);
        whileStatement.setStatements(parseStatementBlock(whileStatement));

        accept(TokenID.CurlyBracketClose);
        return whileStatement;
    }

    private PrintStatement parsePrintStatement(Statement parent) throws ParseException {
        Token firstToken = accept(TokenID.Print);
        accept(TokenID.RoundBracketOpen);

        PrintStatement printStatement = new PrintStatement(parent, firstToken.getPosition());
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

    private ReadStatement parseReadStatement(Statement parent) throws ParseException {
        Token firstToken = accept(TokenID.Read);
        accept(TokenID.RoundBracketOpen);

        ReadStatement readStatement = new ReadStatement(parent, firstToken.getPosition(), parseVariableCall(parent));

        accept(TokenID.RoundBracketClose);
        accept(TokenID.Semicolon);
        return readStatement;
    }

    private Variable validateVariable(Statement parent, Token name) throws ParseException {
        Variable variable = parent.getVariable(name.getValue());
        if (variable == null)
            throw new NotDefinedException(name.getPosition(), new Variable(TokenID.Num, name.getValue()));
        return variable;
    }

    private ReturnStatement parseReturnStatement(Statement parent) throws ParseException {
        Token firstToken = accept(TokenID.Return);
        MathExpr expr = parseMathExpr(parent);
        validateReturnStatementType(expr);
        accept(TokenID.Semicolon);
        return new ReturnStatement(parent, firstToken.getPosition(), expr);
    }

    private void validateReturnStatementType(MathExpr expr) throws ParseException {
        Statement parent = expr.getParent();
        while (!(parent instanceof FunctionDef))
            parent = parent.getParent();

        FunctionDef functionDef = (FunctionDef) parent;
        if (functionDef.returnType != expr.getType())
            throw new TypeMismatchException(expr.getPosition(), functionDef.returnType, expr.getType());
    }

    private LogicExpr parseLogicExpr(Statement parent) throws ParseException {
        AndExpr andExpr = parseAndExpr(parent);
        LogicExpr logicExpr = new LogicExpr(parent, andExpr.getPosition(), andExpr);
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() == TokenID.Or) {
            accept(TokenID.Or);
            logicExpr.addAndExpression(parseAndExpr(parent));
            nextToken = lexer.peekToken();
        }

        return logicExpr;
    }

    private AndExpr parseAndExpr(Statement parent) throws ParseException {
        RelationExpr relationExpr = parseRelationExpr(parent);
        AndExpr andExpr = new AndExpr(parent, relationExpr.getPosition(), relationExpr);
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() == TokenID.And) {
            accept(TokenID.And);
            andExpr.addRelationExpr(parseRelationExpr(parent));
            nextToken = lexer.peekToken();
        }

        return andExpr;
    }

    private RelationExpr parseRelationExpr(Statement parent) throws ParseException {
        BaseLogicExpr leftExpression = parseBaseLogicExpr(parent);
        Token nextToken = lexer.peekToken();

        if (!Token.isRelationOperator(nextToken.getId()))
            return new RelationExpr(parent, leftExpression.getPosition(), leftExpression);

        return new RelationExpr(parent, leftExpression.getPosition(), leftExpression, accept(nextToken.getId()).getId(), parseBaseLogicExpr(parent));
    }

    private BaseLogicExpr parseBaseLogicExpr(Statement parent) throws ParseException {
        Token nextToken = lexer.peekToken();
        boolean isNegation = false;

        if (nextToken.getId() == TokenID.Negation) {
            accept(TokenID.Negation);
            isNegation = true;
            nextToken = lexer.peekToken();
        }

        if (nextToken.getId() == TokenID.RoundBracketOpen)
            return new BaseLogicExpr(parent, nextToken.getPosition(), isNegation, parseBracketLogicExpr(parent));
        else
            return new BaseLogicExpr(parent, nextToken.getPosition(), isNegation, parseMathExpr(parent));
    }

    private LogicExpr parseBracketLogicExpr(Statement parent) throws ParseException {
        accept(TokenID.RoundBracketOpen);
        LogicExpr logicExpr = parseLogicExpr(parent);
        accept(TokenID.RoundBracketClose);

        return logicExpr;
    }

    private MathExpr parseMathExpr(Statement parent) throws ParseException {
        MultExpr multExpr = parseMultExpr(parent);
        MathExpr mathExpr = new MathExpr(parent, multExpr.getPosition(), multExpr);
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

    private MultExpr parseMultExpr(Statement parent) throws ParseException {
        BaseMathExpr baseMathExpr = parseBaseMathExpr(parent);
        MultExpr multExpr = new MultExpr(parent, baseMathExpr.getPosition(), baseMathExpr);
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

    private BaseMathExpr parseBaseMathExpr(Statement parent) throws ParseException {
        Token nextToken = lexer.peekToken();
        boolean isMinus = false;
        if (nextToken.getId() == TokenID.Minus) {
            accept(TokenID.Minus);
            isMinus = true;
            nextToken = lexer.peekToken();
        }

        switch (nextToken.getId()) {
            case RoundBracketOpen:
                return new BaseMathExpr(parent, nextToken.getPosition(), isMinus, parseBracketMathExpr(parent));
            case Number:
                return new BaseMathExpr(parent, nextToken.getPosition(), isMinus, new VariableCall(new Variable(new Value(Integer.valueOf(accept(TokenID.Number).getValue()))), nextToken.getPosition()));
            case SquareBracketOpen:
                return new BaseMathExpr(parent, nextToken.getPosition(), isMinus, new VariableCall(new Variable(parseMatrixLiteral(parent)), nextToken.getPosition()));
            case Name:
                Token secondToken = lexer.peekFollowingToken();
                if (secondToken.getId() == TokenID.RoundBracketOpen)
                    return new BaseMathExpr(parent, nextToken.getPosition(), isMinus, parseFunctionCall(parent));
                return new BaseMathExpr(parent, nextToken.getPosition(), isMinus, parseVariableCall(parent));
            default:
                throw new UnexpectedTokenException(nextToken);
        }
    }

    private MathExpr parseBracketMathExpr(Statement parent) throws ParseException {
        accept(TokenID.RoundBracketOpen);
        MathExpr mathExpr = parseMathExpr(parent);
        accept(TokenID.RoundBracketClose);
        return mathExpr;
    }

    private VariableCall parseVariableCall(Statement parent) throws ParseException {
        Token name = accept(TokenID.Name);
        VariableCall variableCall = new VariableCall(validateVariable(parent, name), name.getPosition());
        if (lexer.peekToken().getId() == TokenID.SquareBracketOpen && variableCall.getType() == TokenID.Mat) {
            variableCall.setColumn(parseMatrixDimension(parent));
            variableCall.setRow(parseMatrixDimension(parent));
        }
        return variableCall;
    }

    private ArrayList<ArrayList<Value>> parseMatrixLiteral(Statement parent) throws ParseException {
        accept(TokenID.SquareBracketOpen);
        ArrayList<ArrayList<Value>> matrix = new ArrayList<>();
        ArrayList<Value> firstRow = parseMatrixRow(parent, 0);
        int rowSizeLimit = firstRow.size();
        matrix.add(firstRow);
        while (lexer.peekToken().getId() == TokenID.Semicolon) {
            accept(TokenID.Semicolon);
            matrix.add(parseMatrixRow(parent, rowSizeLimit));
        }
        accept(TokenID.SquareBracketClose);
        return matrix;
    }

    private ArrayList<Value> parseMatrixRow(Statement parent, int rowSizeLimit) throws ParseException {
        ArrayList<Value> row = new ArrayList<>();
        MathExpr expr = parseMathExpr(parent);
        row.add(new Value(expr));
        int rowSize = 1;
        while (lexer.peekToken().getId() == TokenID.Comma && (rowSizeLimit <= 0 || rowSize < rowSizeLimit)) {
            accept(TokenID.Comma);
            row.add(new Value(parseMathExpr(parent)));
            rowSize++;
        }
        return row;
    }
}
