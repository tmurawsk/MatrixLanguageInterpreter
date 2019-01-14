package tkom;

import javafx.util.Pair;
import tkom.ast.*;
import tkom.ast.expression.*;
import tkom.ast.statement.*;
import tkom.exception.ParseException.*;

import java.util.*;

class Parser {
    private Lexer lexer;

    private ArrayList<FunctionCall> functionCallsToValidate;

    private LinkedList<HashSet<String>> localVariablesStack;

    Parser(Lexer lexer) {
        this.lexer = lexer;
        functionCallsToValidate = new ArrayList<>();
        localVariablesStack = new LinkedList<>();
        localVariablesStack.add(new HashSet<>());
    }

    void parseProgram() throws ParseException {
        functionCallsToValidate.clear();
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() != TokenID.Eof) {
            switch (nextToken.getId()) {
                case Func:
                    Program.addFunction(parseFunctionDef());
                    break;
                case Num:
                    Program.addInitStatement(parseInitStatement());
                    break;
                default:
                    throw new UnexpectedTokenException(nextToken);
            }
            nextToken = lexer.peekToken();
        }
        validateFunctionCalls();
    }

    private void validateFunctionCalls() throws NotDefinedException {
        for (FunctionCall functionCall : functionCallsToValidate) {
            FunctionDef functionDef = Program.getFunctionDef(functionCall, false);
            if (functionDef == null)
                throw new NotDefinedException(functionCall.getPosition(), functionCall);
        }
    }

    private Token accept(TokenID tokenID) throws ParseException {
        Token nextToken = lexer.readToken();
        if (nextToken.getId() != tokenID)
            throw new UnexpectedTokenException(nextToken);
        return nextToken;
    }

    private void pushStackLevel() {
        localVariablesStack.add(new HashSet<>());
    }

    private void popStackLevel() {
        localVariablesStack.removeLast();
    }

    private void addVariable(String name) {
        localVariablesStack.getLast().add(name);
    }

    private void addVariables(Collection<String> arguments) {
        if (arguments != null)
            for (String name : arguments)
                addVariable(name);
    }

    private boolean variableExists(String name) {
        for (HashSet<String> variables : localVariablesStack)
            if (variables.contains(name))
                return true;

        return false;
    }

    private FunctionDef parseFunctionDef() throws ParseException {
        Token firstToken = accept(TokenID.Func);
        Token name = accept(TokenID.Name);
        accept(TokenID.RoundBracketOpen);
        LinkedList<Pair<TokenID, String>> arguments = parseArguments();
        accept(TokenID.RoundBracketClose);
        Token type = parseType();

        FunctionDef functionDef = new FunctionDef(firstToken.getPosition(), name.getValue(), type.getId(), arguments);
        if (Program.isFunctionDuplicate(functionDef))
            throw new DuplicateException(functionDef.getPosition(), functionDef);

        accept(TokenID.CurlyBracketOpen);
        LinkedList<Statement> statementBlock = parseStatementBlock(getValuesFromPairs(arguments));
        Token endOfFunctionDef = accept(TokenID.CurlyBracketClose);

        if (!containsReturnStatement(statementBlock))
            throw new MissingReturnStatementException(endOfFunctionDef.getPosition(), functionDef);

        functionDef.setStatements(statementBlock);
        return functionDef;
    }

    private Collection<String> getValuesFromPairs(Collection<Pair<TokenID, String>> pairs) {
        LinkedList<String> values = new LinkedList<>();
        for (Pair<TokenID, String> pair : pairs)
            values.add(pair.getValue());
        return values;
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

    private LinkedList<Pair<TokenID, String>> parseArguments() throws ParseException {
        LinkedList<Pair<TokenID, String>> arguments = new LinkedList<>();

        if (lexer.peekToken().getId() == TokenID.RoundBracketClose)
            return arguments;

        Token type = parseType();
        Token name = accept(TokenID.Name);
        arguments.add(new Pair<>(type.getId(), name.getValue()));

        while (lexer.peekToken().getId() == TokenID.Comma) {
            accept(TokenID.Comma);
            type = parseType();
            name = accept(TokenID.Name);
            arguments.add(new Pair<>(type.getId(), name.getValue()));
        }

        return arguments;
    }

    private LinkedList<Statement> parseStatementBlock() throws ParseException {
        return parseStatementBlock(null);
    }

    private LinkedList<Statement> parseStatementBlock(Collection<String> initialVariableNames) throws ParseException {
        pushStackLevel();
        addVariables(initialVariableNames);
        LinkedList<Statement> statements = new LinkedList<>();

        boolean isStatementMatched = true;
        while (isStatementMatched) {
            Token firstToken = lexer.peekToken();
            switch (firstToken.getId()) {
                case If:
                    statements.add(parseIfStatement());
                    break;
                case While:
                    statements.add(parseWhileStatement());
                    break;
                case Print:
                    statements.add(parsePrintStatement());
                    break;
                case Read:
                    statements.add(parseReadStatement());
                    break;
                case Return:
                    statements.add(parseReturnStatement());
                    break;
                case Num:
                case Mat:
                    statements.add(parseInitStatement());
                    break;
                case Name:
                    Token secondToken = lexer.peekFollowingToken();
                    if (secondToken.getId() == TokenID.RoundBracketOpen)
                        statements.add(parseFunctionCall());
                    else
                        statements.add(parseAssignStatement());
                    break;
                default:
                    isStatementMatched = false;
            }
        }
        popStackLevel();
        return statements;
    }

    private InitStatement parseInitStatement() throws ParseException {
        Token type = parseType();
        Token name = accept(TokenID.Name);
        InitStatement initStatement = new InitStatement(type.getPosition(), type.getId(), name.getValue());

        if (variableExists(name.getValue()))
            throw new DuplicateException(name.getPosition(), new Variable(type.getId(), name.getValue()));

        if (lexer.peekToken().getId() == TokenID.Assign) {
            accept(TokenID.Assign);
            if (lexer.peekToken().getId() == TokenID.SquareBracketOpen && type.getId() == TokenID.Mat) {
                initStatement.setExpressions(parseMatrixDimension(), parseMatrixDimension());
            } else {
                MathExpr expr = parseMathExpr();
//                if (type.getId() != expr.getType())
//                    throw new TypeMismatchException(expr.getPosition(), type.getId(), expr.getType());
                initStatement.setExpressions(expr, null);
            }
        }

        addVariable(name.getValue());

        accept(TokenID.Semicolon);
        return initStatement;
    }

    private MathExpr parseMatrixDimension() throws ParseException {
        accept(TokenID.SquareBracketOpen);
        MathExpr expr = parseMathExpr();
//        if (expr.getType() != TokenID.Num)
//            throw new TypeMismatchException(expr.getPosition(), TokenID.Num, expr.getType());
        accept(TokenID.SquareBracketClose);
        return expr;
    }

    private AssignStatement parseAssignStatement() throws ParseException {
        VariableCall variableCall = parseVariableCall();
        accept(TokenID.Assign);
        MathExpr expression = parseMathExpr();
//        if (variableCall.getType() != expression.getType())
//            throw new TypeMismatchException(expression.getPosition(), variableCall.getType(), expression.getType());
        accept(TokenID.Semicolon);
        return new AssignStatement(variableCall.getPosition(), variableCall, expression);
    }

    private FunctionCall parseFunctionCall() throws ParseException {
        Token name = accept(TokenID.Name);
        accept(TokenID.RoundBracketOpen);
        FunctionCall functionCall = new FunctionCall(name.getPosition(), name.getValue());

        if (lexer.peekToken().getId() != TokenID.RoundBracketClose) {
            MathExpr expr = parseMathExpr();
            functionCall.addArgument(expr);
            while (lexer.peekToken().getId() == TokenID.Comma) {
                accept(TokenID.Comma);
                expr = parseMathExpr();
                functionCall.addArgument(expr);
            }
        }

        accept(TokenID.RoundBracketClose);
        functionCallsToValidate.add(functionCall);

        return functionCall;
    }

    private IfStatement parseIfStatement() throws ParseException {
        Token firstToken = accept(TokenID.If);
        accept(TokenID.RoundBracketOpen);
        LogicExpr condition = parseLogicExpr();
        accept(TokenID.RoundBracketClose);
        accept(TokenID.CurlyBracketOpen);

        IfStatement ifStatement = new IfStatement(firstToken.getPosition(), condition);
        ifStatement.setStatements(parseStatementBlock());

        accept(TokenID.CurlyBracketClose);

        if (lexer.peekToken().getId() == TokenID.Else)
            ifStatement.setElseStatement(parseElseStatement());

        return ifStatement;
    }

    private IfStatement parseElseStatement() throws ParseException {
        Token firstToken = accept(TokenID.Else);
        accept(TokenID.CurlyBracketOpen);
        IfStatement elseStatement = new IfStatement(firstToken.getPosition(), null);
        elseStatement.setStatements(parseStatementBlock());
        accept(TokenID.CurlyBracketClose);
        return elseStatement;
    }

    private WhileStatement parseWhileStatement() throws ParseException {
        Token firstToken = accept(TokenID.While);
        accept(TokenID.RoundBracketOpen);
        LogicExpr condition = parseLogicExpr();
        accept(TokenID.RoundBracketClose);
        accept(TokenID.CurlyBracketOpen);

        WhileStatement whileStatement = new WhileStatement(firstToken.getPosition(), condition);
        whileStatement.setStatements(parseStatementBlock());

        accept(TokenID.CurlyBracketClose);
        return whileStatement;
    }

    private PrintStatement parsePrintStatement() throws ParseException {
        Token firstToken = accept(TokenID.Print);
        accept(TokenID.RoundBracketOpen);

        PrintStatement printStatement = new PrintStatement(firstToken.getPosition());
        Token nextToken = lexer.peekToken();
        if (nextToken.getId() != TokenID.RoundBracketClose) {
            if (nextToken.getId() == TokenID.String)
                printStatement.addPrintable(accept(TokenID.String).getValue());
            else
                printStatement.addPrintable(parseMathExpr());

            while (lexer.peekToken().getId() == TokenID.Comma) {
                accept(TokenID.Comma);
                if (lexer.peekToken().getId() == TokenID.String)
                    printStatement.addPrintable(accept(TokenID.String).getValue());
                else
                    printStatement.addPrintable(parseMathExpr());
            }
        }

        accept(TokenID.RoundBracketClose);
        accept(TokenID.Semicolon);
        return printStatement;
    }

    private ReadStatement parseReadStatement() throws ParseException {
        Token firstToken = accept(TokenID.Read);
        accept(TokenID.RoundBracketOpen);

        ReadStatement readStatement = new ReadStatement(firstToken.getPosition(), parseVariableCall());

        accept(TokenID.RoundBracketClose);
        accept(TokenID.Semicolon);
        return readStatement;
    }

    private void validateVariable(Token name) throws ParseException {
        if (!variableExists(name.getValue()))
            throw new NotDefinedException(name.getPosition(), new Variable(TokenID.Num, name.getValue()));
    }

    private ReturnStatement parseReturnStatement() throws ParseException {
        Token firstToken = accept(TokenID.Return);
        MathExpr expr = parseMathExpr();
//        validateReturnStatementType(expr);
        accept(TokenID.Semicolon);
        return new ReturnStatement(firstToken.getPosition(), expr);
    }

    /*private void validateReturnStatementType(MathExpr expr) throws ParseException {
        Statement parent = expr.getParent();
        while (!(parent instanceof FunctionDef))
            parent = parent.getParent();

        FunctionDef functionDef = (FunctionDef) parent;
        if (functionDef.returnType != expr.getType())
            throw new TypeMismatchException(expr.getPosition(), functionDef, expr.getType());
    }*/

    private LogicExpr parseLogicExpr() throws ParseException {
        AndExpr andExpr = parseAndExpr();
        LogicExpr logicExpr = new LogicExpr(andExpr.getPosition(), andExpr);
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() == TokenID.Or) {
            accept(TokenID.Or);
            logicExpr.addAndExpression(parseAndExpr());
            nextToken = lexer.peekToken();
        }

        return logicExpr;
    }

    private AndExpr parseAndExpr() throws ParseException {
        RelationExpr relationExpr = parseRelationExpr();
        AndExpr andExpr = new AndExpr(relationExpr.getPosition(), relationExpr);
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() == TokenID.And) {
            accept(TokenID.And);
            andExpr.addRelationExpr(parseRelationExpr());
            nextToken = lexer.peekToken();
        }

        return andExpr;
    }

    private RelationExpr parseRelationExpr() throws ParseException {
        BaseLogicExpr leftExpression = parseBaseLogicExpr();
        Token nextToken = lexer.peekToken();

        if (!Token.isRelationOperator(nextToken.getId()))
            return new RelationExpr(leftExpression.getPosition(), leftExpression);

        return new RelationExpr(leftExpression.getPosition(), leftExpression, accept(nextToken.getId()).getId(), parseBaseLogicExpr());
    }

    private BaseLogicExpr parseBaseLogicExpr() throws ParseException {
        Token nextToken = lexer.peekToken();
        boolean isNegation = false;

        if (nextToken.getId() == TokenID.Negation) {
            accept(TokenID.Negation);
            isNegation = true;
            nextToken = lexer.peekToken();
        }

        if (nextToken.getId() == TokenID.RoundBracketOpen)
            return new BaseLogicExpr(nextToken.getPosition(), isNegation, parseBracketLogicExpr());
        else
            return new BaseLogicExpr(nextToken.getPosition(), isNegation, parseMathExpr());
    }

    private LogicExpr parseBracketLogicExpr() throws ParseException {
        accept(TokenID.RoundBracketOpen);
        LogicExpr logicExpr = parseLogicExpr();
        accept(TokenID.RoundBracketClose);

        return logicExpr;
    }

    private MathExpr parseMathExpr() throws ParseException {
        MultExpr multExpr = parseMultExpr();
        MathExpr mathExpr = new MathExpr(multExpr.getPosition(), multExpr);
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() == TokenID.Plus || nextToken.getId() == TokenID.Minus) {
            if (nextToken.getId() == TokenID.Plus) {
                accept(TokenID.Plus);
                mathExpr.addPlus(parseMultExpr());
            } else {
                accept(TokenID.Minus);
                mathExpr.addMinus(parseMultExpr());
            }
            nextToken = lexer.peekToken();
        }

        return mathExpr;
    }

    private MultExpr parseMultExpr() throws ParseException {
        BaseMathExpr baseMathExpr = parseBaseMathExpr();
        MultExpr multExpr = new MultExpr(baseMathExpr.getPosition(), baseMathExpr);
        Token nextToken = lexer.peekToken();

        while (nextToken.getId() == TokenID.Multiply || nextToken.getId() == TokenID.Divide) {
            if (nextToken.getId() == TokenID.Multiply) {
                accept(TokenID.Multiply);
                multExpr.addMultiply(parseBaseMathExpr());
            } else {
                accept(TokenID.Divide);
                multExpr.addDivide(parseBaseMathExpr());
            }
            nextToken = lexer.peekToken();
        }

        return multExpr;
    }

    private BaseMathExpr parseBaseMathExpr() throws ParseException {
        Token nextToken = lexer.peekToken();
        boolean isMinus = false;
        if (nextToken.getId() == TokenID.Minus) {
            accept(TokenID.Minus);
            isMinus = true;
            nextToken = lexer.peekToken();
        }

        switch (nextToken.getId()) {
            case RoundBracketOpen:
                return new BaseMathExpr(nextToken.getPosition(), isMinus, parseBracketMathExpr());
            case Number:
                return new BaseMathExpr(nextToken.getPosition(), isMinus, new VariableCall(new Variable(new Value(Integer.valueOf(accept(TokenID.Number).getValue()))), nextToken.getPosition()));
            case SquareBracketOpen:
                return new BaseMathExpr(nextToken.getPosition(), isMinus, new VariableCall(new Variable(parseMatrixLiteral()), nextToken.getPosition()));
            case Name:
                Token secondToken = lexer.peekFollowingToken();
                if (secondToken.getId() == TokenID.RoundBracketOpen)
                    return new BaseMathExpr(nextToken.getPosition(), isMinus, parseFunctionCall());
                return new BaseMathExpr(nextToken.getPosition(), isMinus, parseVariableCall());
            default:
                throw new UnexpectedTokenException(nextToken);
        }
    }

    private MathExpr parseBracketMathExpr() throws ParseException {
        accept(TokenID.RoundBracketOpen);
        MathExpr mathExpr = parseMathExpr();
        accept(TokenID.RoundBracketClose);
        return mathExpr;
    }

    private VariableCall parseVariableCall() throws ParseException {
        Token name = accept(TokenID.Name);
        validateVariable(name);
        VariableCall variableCall = new VariableCall(name.getValue(), name.getPosition());
        if (lexer.peekToken().getId() == TokenID.SquareBracketOpen) {
            variableCall.setColumn(parseMatrixDimension());
            variableCall.setRow(parseMatrixDimension());
        }
        return variableCall;
    }

    private ArrayList<ArrayList<Value>> parseMatrixLiteral() throws ParseException {
        accept(TokenID.SquareBracketOpen);
        ArrayList<ArrayList<Value>> matrix = new ArrayList<>();
        ArrayList<Value> firstRow = parseMatrixRow(0);
        int rowSizeLimit = firstRow.size();
        matrix.add(firstRow);
        while (lexer.peekToken().getId() == TokenID.Semicolon) {
            accept(TokenID.Semicolon);
            matrix.add(parseMatrixRow(rowSizeLimit));
        }
        accept(TokenID.SquareBracketClose);
        return matrix;
    }

    private ArrayList<Value> parseMatrixRow(int rowSizeLimit) throws ParseException {
        ArrayList<Value> row = new ArrayList<>();
        MathExpr expr = parseMathExpr();
        row.add(new Value(expr));
        int rowSize = 1;
        while (lexer.peekToken().getId() == TokenID.Comma && (rowSizeLimit <= 0 || rowSize < rowSizeLimit)) {
            accept(TokenID.Comma);
            row.add(new Value(parseMathExpr()));
            rowSize++;
        }
        return row;
    }
}
