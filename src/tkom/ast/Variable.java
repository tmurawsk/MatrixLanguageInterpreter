package tkom.ast;

import javafx.util.Pair;
import tkom.Position;
import tkom.TokenID;
import tkom.exception.ExecutionException.ExecutionException;
import tkom.exception.ExecutionException.MathException;
import tkom.exception.ExecutionException.TypeMismatchException;

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

    public TokenID getType() {
        return type;
    }

    public void setValue(ArrayList<ArrayList<Integer>> value) {
        isEvaluated = true;
        this.value = value;
    }

    public void setValue(int i, int j, int newValue) {
        isEvaluated = true;
        value.get(i).set(j, newValue);
    }

    public void setValue(int newValue) {
        isEvaluated = true;
        value = new ArrayList<>();
        value.set(0, new ArrayList<>());
        value.get(0).set(0, newValue);
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

    public int get(int i, int j) {
        if (value == null || i > value.size() || j > value.get(0).size())
            return 0; //TODO throw exception?

        return value.get(i - 1).get(j - 1);
    }

    public int getInt() {
        return get(1, 1);
    }

    public int getHeight() {
        return isEvaluated ? value.size() : valueExpressions.size();
    }

    public int getWidth() {
        return isEvaluated ? value.get(0).size() : valueExpressions.get(0).size();
    }

    public Pair<Integer, Integer> getSize() {
        return new Pair<>(getHeight(), getWidth());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int maxInt = -1000000;
        for (ArrayList<Integer> array : value)
            for (int i : array)
                maxInt = i > maxInt ? i : maxInt;

        int tabNumber = String.valueOf(maxInt).length() / 4 + 1;
        for (int i = 0; i < value.size(); i++) {
            for (int j = 0; j < value.get(0).size(); j++) {
                int currInt = get(j, i);
                sb.append(currInt);
                for (int k = 0; k < tabNumber - String.valueOf(currInt).length() / 4; k++)
                    sb.append('\t');
            }
        }

        return sb.toString();
    }

    public ArrayList<ArrayList<Integer>> evaluate() throws ExecutionException {
        if (isEvaluated)
            return value;

        for (int i = 0; i < valueExpressions.size(); i++) {
            for (int j = 0; j < valueExpressions.get(i).size(); j++) {
                Value v = valueExpressions.get(i).get(j);
                TokenID type = v.getType();
                if (type != TokenID.Num)
                    throw new TypeMismatchException(new Position(), TokenID.Num, type);
                value.get(i).set(j, v.evaluate());
            }
        }

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
        ArrayList<ArrayList<Integer>> toReturn = new ArrayList<>();
        int height = getHeight();
        int width = getWidth();

        if (height != v.getHeight() || width != v.getWidth())
            throw new MathException(new Position(), getSize(), v.getSize());

        for (int i = 0; i < height; i++) {
            toReturn.set(i, new ArrayList<>());
            ArrayList<Integer> row = toReturn.get(i);
            for (int j = 0; j < width; j++) {
                if (isMinus)
                    row.set(j, value.get(i).get(j) - v.evaluate().get(i).get(j));
                else
                    row.set(j, value.get(i).get(j) + v.evaluate().get(i).get(j));
            }
        }
        return new Variable(toReturn, true);
    }

    public Variable multiply(Variable v) {
        return null; //TODO
    }

    public Variable divide(Variable v) {
        return null; //TODO
    }

    public boolean equals(Variable v) throws ExecutionException {
        if (getType() != v.getType())
            throw new TypeMismatchException(new Position(), getType(), v.getType());

        int height = getHeight();
        int width = getWidth();
        if (height != v.getHeight() || width != v.getWidth())
            return false;

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (!value.get(i).get(j).equals(v.evaluate().get(i).get(j)))
                    return false;

        return true;
    }

    public boolean notEquals(Variable v) throws ExecutionException {
        return !equals(v);
    }

    public boolean greaterThan(Variable v) throws ExecutionException {
        evaluateAndCheckTypes(v);
        return getInt() > v.getInt();
    }

    public boolean lowerThan(Variable v) throws ExecutionException {
        evaluateAndCheckTypes(v);
        return getInt() < v.getInt();
    }

    public boolean greaterOrEqualThan(Variable v) throws ExecutionException {
        return !lowerThan(v);
    }

    public boolean lowerOrEqualThan(Variable v) throws ExecutionException {
        return !greaterThan(v);
    }

    private void evaluateAndCheckTypes(Variable v) throws ExecutionException {
        evaluate();
        v.evaluate();

        if (getType() != TokenID.Num)
            throw new TypeMismatchException(new Position(), TokenID.Num, getType());

        if (v.getType() != TokenID.Num)
            throw new TypeMismatchException(new Position(), TokenID.Num, v.getType());
    }
}
