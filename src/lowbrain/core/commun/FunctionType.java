package lowbrain.core.commun;

/**
 * Created by Mooffy on 2017-06-23.
 */
public enum FunctionType {
    LINEAR,
    CUBIC,
    SQUARE;

    public static FunctionType get(int val) {
        switch (val) {
            case 0:
                return FunctionType.LINEAR;
            default:
            case 1:
                return FunctionType.CUBIC;
            case 2:
                return FunctionType.SQUARE;
        }
    }
}
