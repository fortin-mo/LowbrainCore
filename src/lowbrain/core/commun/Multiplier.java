package lowbrain.core.commun;

import lowbrain.core.main.LowbrainCore;
import lowbrain.core.rpg.LowbrainPlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

/**
 * Created by Mooffy on 2017-06-23.
 */
public class Multiplier extends AbstractParams {
    public Multiplier(){
        super();
    }

    public Multiplier(ConfigurationSection config) {
        super(config);
    }

    public float compute(LowbrainPlayer p) {
        if (!enabled) {
            return 1F; // returning a multiplier of 1 equals doing nothing
        }
        return this.computeWith(p, null, null);
    }

    public float randomize(LowbrainPlayer p) {
        float multiplier = this.compute(p);

        if (range != 0 && rangeType == RangeType.ON_MULTIPLIER) {
            // we can only take the on_multiplier range here because the value it self is not available
            multiplier = Helper.getRandomBetween(min, max, multiplier, range, false);
        }

        return multiplier;
    }

    public float randomizeFromMultiplier(float value) {
        float multiplier = value;

        if (range != 0 && rangeType == RangeType.ON_MULTIPLIER) {
            multiplier = Helper.getRandomBetween(min, max, multiplier, range, false);
        }

        return multiplier;
    }

    public float randomizeFromValue(float value) {
        float val = value;

        if (range != 0 && rangeType == RangeType.ON_VALUE) {
            val = Helper.getRandomBetween(min, max, val, range, false);
        }

        return val;
    }

    public float randomizeWith(LowbrainPlayer p, Float min, Float max, Float range) {
        float multiplier = this.computeWith(p, min, max);

        float useRange = range == null ? this.getRange() : range;

        if (range != 0 && rangeType == RangeType.ON_MULTIPLIER) {
            // we can only take the on_multiplier range here because the value it self is not available
            multiplier = Helper.getRandomBetween(min, max, multiplier, useRange, false);
        }

        return multiplier;
    }

    public float computeWith(LowbrainPlayer p, Float min, Float max) {
        if (!enabled) {
            return 1F; // returning a multiplier of 1 equals doing nothing
        }
        float useMax = max == null ? this.getMax() : max;
        float useMin = min == null ? this.getMin() : min;

        float result = 0F;
        if (Helper.StringIsNullOrEmpty(function)) {
            result = Helper.valueFromFunction(useMax, useMin, this.getVariables(), p, this.functionType);
        } else {
            String[] st = function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }

        return result;
    }

    public float randomizeValue(LowbrainPlayer p, float initialValue) {
        float val = this.newValue(p, initialValue);

        if (range != 0 && rangeType == RangeType.ON_VALUE) {
            // in range is apply to the value
            // min is set to zero to avoid negative values
            // max is set to Float.MAX_VALUE to avoid exeding possible float value
            val = Helper.getRandomBetween(0, Float.MAX_VALUE, val, range, false);
        }

        return val;
    }

    public float newValue(LowbrainPlayer p, float initialValue) {
        if (!enabled) {
            return initialValue;
        }

        float multiplier = this.randomize(p);
        float result = initialValue * multiplier;

        return result;
    };
}


