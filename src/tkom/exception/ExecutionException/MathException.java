package tkom.exception.ExecutionException;

import javafx.util.Pair;
import tkom.Position;

public class MathException extends ExecutionException {
    private MathException(Position position, String message) {
        super(position, message);
    }

    public MathException(Position position, Pair<Integer, Integer> firstDim, Pair<Integer, Integer> secondDim) {
        this(position, "Incompatible matrix sizes: " + matrixSizesString(firstDim) + " and " + matrixSizesString(secondDim));
    }

    public MathException(Position position) {
        this(position, "Dividing by zero!");
    }

    private static String matrixSizesString(Pair<Integer, Integer> sizes) {
        return sizes.getKey() + "x" + sizes.getValue();
    }

    @Override
    public ExecutionException setPosition(Position position) {
        return new MathException(position, getMessage().split("\n", 2)[1]);
    }
}
