package lowbrain.core.commun;

/**
 * Created by Mooffy on 2017-06-23.
 */
public enum RangeType {
    ON_MULTIPLIER,
    ON_VALUE;

    public static RangeType get(int val) {
        switch (val) {
            default:
            case 0:
                return RangeType.ON_MULTIPLIER;
            case 1:
                return RangeType.ON_VALUE;
        }
    }
}
