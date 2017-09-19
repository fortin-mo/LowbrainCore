package lowbrain.core.commun;

import lowbrain.core.abstraction.Parametable;
import lowbrain.core.rpg.LowbrainPlayer;
import lowbrain.library.fn;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;

/**
 * represents a multiplier parameter
 */
public class Multiplier extends Parametable {
    /**
     * constructor
     * @param config
     */
    @Contract("null -> fail")
    public Multiplier(ConfigurationSection config) {
        super(config);
    }

    /**
     * generate value from player's information
     * @param p LowbrainPlayer
     * @return computed value
     */
    public float compute(LowbrainPlayer p) {
        if (!enabled)
            return 1F; // returning a multiplier of 1 equals doing nothing

        return this.computeWith(p, null, null);
    }

    /**
     * generate and randomize the value from player's information
     * will only randomize if range type is set to ON_MULTIPLIER
     * @param p LowbrainPlayer
     * @return computed value
     */
    public float randomize(LowbrainPlayer p) {
        float multiplier = this.compute(p);

        if (range != 0 && rangeType == RangeType.ON_MULTIPLIER)
            // we can only take the on_multiplier range here because the value it self is not available
            multiplier = fn.randomBetween(min, max, multiplier, range, false);

        return multiplier;
    }

    /**
     * randomize a single value
     * will randomize no matter what type of range is used
     * @param value value to randomize
     * @return randomized value
     */
    public float randomize(float value) {
        if (range == 0)
            return value;

        return fn.randomBetween(min, max, value, range, false);
    }

    /**
     * randomize a single value
     * when the value is considered a multiplier
     * @param value value to randomize
     * @return randomized value
     */
    public float randomizeFromMultiplier(float value) {
        float multiplier = value;

        if (range != 0 && rangeType == RangeType.ON_MULTIPLIER)
            multiplier = fn.randomBetween(min, max, multiplier, range, false);

        return multiplier;
    }

    /**
     * randomize a single value
     * when the value is considered a "GENERATED VALUE"
     * @param value value to randomize
     * @return randomized value
     */
    public float randomizeFromValue(float value) {
        float val = value;

        if (range != 0 && rangeType == RangeType.ON_VALUE)
            val = fn.randomBetween(min, max, val, range, false);

        return val;
    }

    /**
     * use different parameters (min,max,range) to randomize a value from a player's information
     * @param p LowbrainPlayer
     * @param min minimum value
     * @param max maximum value
     * @param range range
     * @return randomized value
     */
    public float randomizeWith(LowbrainPlayer p, Float min, Float max, Float range) {
        float multiplier = this.computeWith(p, min, max);

        float useRange = range == null ? this.getRange() : range;

        if (range != 0 && rangeType == RangeType.ON_MULTIPLIER)
            // we can only take the on_multiplier range here because the value it self is not available
            multiplier = fn.randomBetween(min, max, multiplier, useRange, false);

        return multiplier;
    }

    /**
     * generate a value using different parameters (min,max) from player's information
     * @param p LowbrainPlayer
     * @param min minimum value
     * @param max maximum value
     * @return computed value
     */
    public float computeWith(LowbrainPlayer p, Float min, Float max) {
        if (!enabled)
            return 1F; // returning a multiplier of 1 equals doing nothing

        float useMax = max == null ? this.getMax() : max;
        float useMin = min == null ? this.getMin() : min;

        float result = 0F;
        if (fn.StringIsNullOrEmpty(function)) {
            result = Helper.valueFromFunction(useMax, useMin, this.getVariables(), p, this.functionType);
        } else {
            String[] st = function.split(",");
            if (st.length > 1)
                result = fn.eval(Helper.FormatStringWithValues(st, p));
            else
                result = fn.eval(st[0]);

        }

        return result;
    }

    /**
     * get multiplied and randomized value
     * randomizer is ony applied if and only if range type is ON_VALUE
     * @param p LowbrainPlayer
     * @param initialValue initial value
     * @return randomized value
     */
    public float applyRandomizer(LowbrainPlayer p, float initialValue) {
        float val = this.applyMultiplier(p, initialValue);

        if (range != 0 && rangeType == RangeType.ON_VALUE)
            // in range is apply to the value
            // min is set to zero to avoid negative values
            // max is set to Float.MAX_VALUE to avoid exeding possible float value
            val = fn.randomBetween(0, Float.MAX_VALUE, val, range, false);


        return val;
    }

    /**
     * compute multiplier and applies it to the initial value
     * @param p LowbrainPlayer
     * @param initialValue initial value
     * @return multiplied value
     */
    public float applyMultiplier(LowbrainPlayer p, float initialValue) {
        if (!enabled)
            return initialValue;

        float multiplier = this.randomize(p);
        float result = initialValue * multiplier;

        return result;
    };
}


