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

    public void parseProgram() throws UnexpectedTokenException, NotDefinedException, DuplicateException, MissingReturnStatementException, UnknownTypeException {
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
        Token type = parseType();

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

    private Token parseType() throws UnknownTypeException {
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

    private LinkedList<Variable> parseArguments() throws UnexpectedTokenException, UnknownTypeException {
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
                    statements.add(parseAssignStatement(parent));
                    break;
                case Num:
                case Mat:
                    statements.add(parseInitStatement(parent));
                    break;
                default:
                    isStatementMatched = false;
            }
        }
        return statements;
    }

    private InitStatement parseInitStatement(Statement parent) throws UnexpectedTokenException, UnknownTypeException, DuplicateException, NotDefinedException {
        Token type = parseType();
        Token name = accept(TokenID.Name);
        InitStatement initStatement = new InitStatement(parent, type.getId(), name.getValue());

        if (initStatement.variableExists(name.getValue())) {
            throw new DuplicateException(name.getPosition(), new Variable(type.getId(), name.getValue()));
        }

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

        Variable newVariable = new Variable(type.getId(), name.getValue());
        if (parent != null)
            parent.addVariable(newVariable);
        else
            Program.addGlobalVariable(newVariable);

        accept(TokenID.Semicolon);
        return initStatement;
    }

    private AssignStatement parseAssignStatement(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        VariableCall variableCall = parseVariableCall(parent);
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
        FunctionDef functionDef = Program.getFunctionDef(functionCall);
        if (functionDef == null)
            throw new NotDefinedException(name.getPosition(), functionCall);

        functionCall.setFunctionDef(functionDef);
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

        if (lexer.peekToken().getId() == TokenID.Else) {
            accept(TokenID.Else);
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

    private PrintStatement parsePrintStatement(Statement parent) throws UnexpectedTokenException, NotDefinedException {
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

        ReadStatement readStatement = new ReadStatement(parent, parseVariableCall(parent));

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

    private ReturnStatement parseReturnStatement(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        accept(TokenID.Return);
        MathExpr expr = parseMathExpr(parent);
        //TODO iterate up to parents, find FunctionDef and validate if functionDef.returnType == expr.type
        accept(TokenID.Semicolon);
        return new ReturnStatement(parent, expr);
    }

    private LogicExpr parseLogicExpr(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        LogicExpr logicExpr = new LogicExpr(parent, parseAndExpr(parent));
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() == TokenID.Or) {
            accept(TokenID.Or);
            logicExpr.addAndExpression(parseAndExpr(parent));
            nextToken = lexer.peekToken();
        }

        return logicExpr;
    }

    private AndExpr parseAndExpr(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        AndExpr andExpr = new AndExpr(parent, parseRelationExpr(parent));
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() == TokenID.And) {
            accept(TokenID.And);
            andExpr.addRelationExpr(parseRelationExpr(parent));
            nextToken = lexer.peekToken();
        }

        return andExpr;
    }

    private RelationExpr parseRelationExpr(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        BaseLogicExpr leftExpression = parseBaseLogicExpr(parent);
        Token nextToken = lexer.peekToken();

        if (!Token.isRelationOperator(nextToken.getId()))
            return new RelationExpr(parent, leftExpression);

        return new RelationExpr(parent, leftExpression, accept(nextToken.getId()).getId(), parseBaseLogicExpr(parent));
    }

    private BaseLogicExpr parseBaseLogicExpr(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        Token nextToken = lexer.peekToken();
        boolean isNegation = false;

        if (nextToken.getId() == TokenID.Negation) {
            accept(TokenID.Negation);
            isNegation = true;
            nextToken = lexer.peekToken();
        }

        if (nextToken.getId() == TokenID.RoundBracketOpen)
            return new BaseLogicExpr(parent, isNegation, parseBracketLogicExpr(parent));
        else
            return new BaseLogicExpr(parent, isNegation, parseMathExpr(parent));
    }

    private LogicExpr parseBracketLogicExpr(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        accept(TokenID.RoundBracketOpen);
        LogicExpr logicExpr = parseLogicExpr(parent);
        accept(TokenID.RoundBracketClose);

        return logicExpr;
    }

    private MathExpr parseMathExpr(Statement parent) throws UnexpectedTokenException, NotDefinedException {
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

    private MultExpr parseMultExpr(Statement parent) throws UnexpectedTokenException, NotDefinedException {
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

    private BaseMathExpr parseBaseMathExpr(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        Token nextToken = lexer.peekToken();
        boolean isMinus = false;
        if (nextToken.getId() == TokenID.Minus) {
            accept(TokenID.Minus);
            isMinus = true;
            nextToken = lexer.peekToken();
        }

        switch (nextToken.getId()) {
            case RoundBracketOpen:
                return new BaseMathExpr(parent, isMinus, parseBracketMathExpr(parent));
            case Number:
                return new BaseMathExpr(parent, isMinus, new VariableCall(new Variable(new Value(Integer.valueOf(accept(TokenID.Number).getValue())))));
            case SquareBracketOpen:
                return new BaseMathExpr(parent, isMinus, new VariableCall(new Variable(parseMatrixLiteral(parent))));
            case Name:
                Token secondToken = lexer.peekFollowingToken();
                if (secondToken.getId() == TokenID.RoundBracketOpen)
                    return new BaseMathExpr(parent, isMinus, parseFunctionCall(parent));
                return new BaseMathExpr(parent, isMinus, parseVariableCall(parent));
            default:
                throw new UnexpectedTokenException(nextToken);
        }
    }

    private MathExpr parseBracketMathExpr(Statement parent) throws UnexpectedTokenException, NotDefinedException {
        accept(TokenID.RoundBracketOpen);
        MathExpr mathExpr = parseMathExpr(parent);
        accept(TokenID.RoundBracketClose);
        return mathExpr;
    }

    private VariableCall parseVariableCall(Statement parent) throws UnexpectedTokenException, NotDefinedException {
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

    private ArrayList<ArrayList<Value>> parseMatrixLiteral(Statement parent) throws UnexpectedTokenException, NotDefinedException {
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

    private ArrayList<Value> parseMatrixRow(Statement parent, int rowSizeLimit) throws UnexpectedTokenException, NotDefinedException {
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
