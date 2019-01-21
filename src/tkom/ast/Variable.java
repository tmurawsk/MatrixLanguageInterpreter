package tkom.ast;

import javafx.util.Pair;
import tkom.Position;
import tkom.TokenID;
import tkom.exception.ExecutionException.*;
import tkom.exception.ExecutionException.IndexOutOfBoundsException;

import java.util.ArrayList;

public class Variable {
    public String name;

    private TokenID type;

    private ArrayList<ArrayList<Value>> valueExpressions;

    private ArrayList<ArrayList<Integer>> value;

    private boolean isEvaluated;

    private Variable() {
        this.isEvaluated = false;
    }

    public Variable(TokenID type, String name) {
        this();
        this.type = type;
        this.name = name;
        this.isEvaluated = false;
    }

    public Variable(Value value) {
        this();
        ArrayList<ArrayList<Value>> matrix = new ArrayList<>();
        ArrayList<Value> column = new ArrayList<>();
        column.add(value);
        matrix.add(column);

        this.type = TokenID.Num;
        this.valueExpressions = matrix;
        initializeValuesMatrix();
    }

    public Variable(ArrayList<ArrayList<Value>> valueExpressions) {
        this();
        this.type = (valueExpressions.size() > 1 || valueExpressions.get(0).size() > 1) ? TokenID.Mat : TokenID.Num;
        this.valueExpressions = valueExpressions;
        initializeValuesMatrix();
    }

    public Variable(ArrayList<ArrayList<Integer>> value, boolean isEvaluated) {
        this();
        this.isEvaluated = isEvaluated;
        this.value = value;
        this.type = (value.size() > 1 || value.get(0).size() > 1) ? TokenID.Mat : TokenID.Num;
    }

    public Variable(int value) {
        this();
        this.type = TokenID.Num;
        set(value);
    }

    public TokenID getType() {
        return type;
    }

    void set(ArrayList<ArrayList<Integer>> value) {
        isEvaluated = true;
        this.value = value;
    }

    void set(int i, int j, int newValue) {
        isEvaluated = true;
        value.get(i).set(j, newValue);
    }

    private void set(int newValue) {
        isEvaluated = true;
        value = new ArrayList<>();
        value.add(new ArrayList<>());
        value.get(0).add(newValue);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void initSize(int height, int width) {
        valueExpressions = new ArrayList<>();
        value = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            ArrayList<Value> expressionRow = new ArrayList<>();
            ArrayList<Integer> valueRow = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                expressionRow.add(new Value(0));
                valueRow.add(0);
            }
            valueExpressions.add(expressionRow);
            value.add(valueRow);
        }

        type = (height == width && height == 1) ? TokenID.Num : TokenID.Mat;
        isEvaluated = true;
    }

    private void initializeValuesMatrix() {
        if (valueExpressions == null)
            return;

        int width = valueExpressions.get(0).size();
        int height = valueExpressions.size();
        value = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            ArrayList<Integer> valueRow = new ArrayList<>();
            for (int j = 0; j < width; j++)
                valueRow.add(0);

            value.add(valueRow);
        }
    }

    public int getThrows(int i, int j) throws ExecutionException {
        evaluate();
        if (i < 0 || i >= value.size())
            throw new IndexOutOfBoundsException(new Position(), i);
        if (j < 0 || j >= value.get(0).size())
            throw new IndexOutOfBoundsException(new Position(), j);

        return value.get(i).get(j);
    }

    private int get(int i, int j) {
        return value.get(i).get(j);
    }

    public int getInt() throws ExecutionException {
        evaluate();
        return value.get(0).get(0);
    }

    int getHeight() {
        return isEvaluated ? value.size() : valueExpressions.size();
    }

    int getWidth() {
        return isEvaluated ? value.get(0).size() : valueExpressions.get(0).size();
    }

    private Pair<Integer, Integer> getSize() {
        return new Pair<>(getHeight(), getWidth());
    }

    public String getString() {
        if (type == TokenID.Num)
            return String.valueOf(value.get(0).get(0));

        StringBuilder sb = new StringBuilder("\n[");
        int maxInt = -1000000;
        for (ArrayList<Integer> array : value)
            for (int i : array)
                maxInt = i > maxInt ? i : maxInt;

        int tabNumber = String.valueOf(maxInt).length() + 2;
        int height = getHeight();
        int width = getWidth();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i != 0 && j == 0)
                    sb.append(' ');

                int currInt = get(i, j);
                sb.append(currInt);

                int limit = tabNumber - String.valueOf(currInt).length();

                for (int k = 0; k < limit && j < width - 1; k++)
                    sb.append(' ');
            }
            if (i < height - 1)
                sb.append('\n');
        }
        sb.append("]\n");

        return sb.toString();
    }

    ArrayList<ArrayList<Integer>> evaluateAnyway() throws ExecutionException {
        return evaluate(false);
    }

    public ArrayList<ArrayList<Integer>> evaluate() throws ExecutionException {
        return evaluate(true);
    }

    private ArrayList<ArrayList<Integer>> evaluate(boolean checkIfEvaluated) throws ExecutionException {
        if (valueExpressions == null && value == null) {
            if (type == TokenID.Num)
                set(0);
            else
                throw new NotInitializedException(new Position(), this);
        }

        if ((isEvaluated && checkIfEvaluated) || valueExpressions == null)
            return value;

        for (int i = 0; i < valueExpressions.size(); i++) {
            for (int j = 0; j < valueExpressions.get(i).size(); j++) {
                Value expr = valueExpressions.get(i).get(j);
                TokenID type = expr.getType();
                if (type != TokenID.Num)
                    throw new TypeMismatchException(new Position(), TokenID.Num, type);
                set(i, j, expr.evaluate());
            }
        }
        isEvaluated = true;

        return value;
    }

    public Variable add(Variable v) throws ExecutionException {
        return addOrSub(v, false);
    }

    public Variable subtract(Variable v) throws ExecutionException {
        return addOrSub(v, true);
    }

    private Variable addOrSub(Variable v, boolean isMinus) throws ExecutionException {
        evaluate();
        v.evaluate();
        int height = getHeight();
        int width = getWidth();
        Variable result = new Variable();
        result.initSize(height, width);

        if (height != v.getHeight() || width != v.getWidth())
            throw new MathException(new Position(), getSize(), v.getSize());

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (isMinus)
                    result.set(i, j, get(i, j) - v.get(i, j));
                else
                    result.set(i, j, get(i, j) + v.get(i, j));
            }
        }
        return result;
    }

    public Variable multiply(Variable v) throws ExecutionException {
        evaluate();
        v.evaluate();

        if (getType() == TokenID.Num && v.getType() != TokenID.Num)
            return v.multiplyByNum(getInt());
        if (v.getType() == TokenID.Num && getType() != TokenID.Num)
            return multiplyByNum(v.getInt());
        if (getType() == TokenID.Num && v.getType() == TokenID.Num)
            return new Variable(getInt() * v.getInt());

        int height1 = getHeight(), width1 = getWidth();
        int height2 = v.getHeight(), width2 = v.getWidth();

        if (width1 != height2)
            throw new MathException(new Position(), getSize(), v.getSize());

        Variable result = new Variable();
        result.initSize(height1, width2);

        for (int i = 0; i < height1; i++)
            for (int j = 0; j < width2; j++)
                result.set(i, j, multiplyRowByColumn(v, i, j));

        return result;
    }

    private Variable multiplyByNum(int mul) {
        return multiplyOrDivideByNum(mul, false);
    }

    private Variable divideByNum(int div) {
        return multiplyOrDivideByNum(div, true);
    }

    private Variable multiplyOrDivideByNum(int num, boolean isDivision) {
        Variable result = new Variable();
        int height = getHeight();
        int width = getWidth();
        result.initSize(height, width);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (isDivision)
                    result.set(i, j, get(i, j) / num);
                else
                    result.set(i, j, get(i, j) * num);
            }
        }

        return result;
    }

    private int multiplyRowByColumn(Variable v, int row, int col) {
        int result = 0;
        int width = getWidth();

        for (int i = 0; i < width; i++)
            result += get(row, i) * v.get(i, col);

        return result;
    }

    public Variable divide(Variable v) throws ExecutionException {
        evaluate();
        v.evaluate();

        if (v.getType() != TokenID.Num)
            throw new TypeMismatchException(new Position(), TokenID.Num, v.getType());

        if (v.getInt() == 0)
            throw new MathException(new Position());

        if (getType() == TokenID.Num)
            return new Variable(getInt() / v.getInt());

        return divideByNum(v.getInt());
    }

    public boolean equals(Variable v) throws ExecutionException {
        evaluate();
        v.evaluate();

        if (getType() != v.getType())
            throw new TypeMismatchException(new Position(), getType(), v.getType());

        int height = getHeight();
        int width = getWidth();
        if (height != v.getHeight() || width != v.getWidth())
            return false;

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (get(i, j) != v.get(i, j))
                    return false;

        return true;
    }

    public boolean notEquals(Variable v) throws ExecutionException {
        return !equals(v);
    }

    public boolean greaterThan(Variable v) throws ExecutionException {
        evaluateAndCheckTypesNum(v);
        return getInt() > v.getInt();
    }

    public boolean lowerThan(Variable v) throws ExecutionException {
        evaluateAndCheckTypesNum(v);
        return getInt() < v.getInt();
    }

    public boolean greaterOrEqualThan(Variable v) throws ExecutionException {
        return !lowerThan(v);
    }

    public boolean lowerOrEqualThan(Variable v) throws ExecutionException {
        return !greaterThan(v);
    }

    private void evaluateAndCheckTypesNum(Variable v) throws ExecutionException {
        evaluate();
        v.evaluate();

        if (getType() != TokenID.Num)
            throw new TypeMismatchException(new Position(), TokenID.Num, getType());

        if (v.getType() != TokenID.Num)
            throw new TypeMismatchException(new Position(), TokenID.Num, v.getType());
    }
}
