package lowbrain.core.abstraction;

import lowbrain.core.commun.RangeType;
import lowbrain.core.main.LowbrainCore;
import lowbrain.library.FunctionType;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;

/**
 * Created by Mooffy on 2017-06-23.
 */
public abstract class Parametrise {
    protected double max = 1F;
    protected double min = 1F;
    protected String function;
    protected Boolean enabled = true;
    protected double range = 0F;
    protected RangeType rangeType = RangeType.ON_MULTIPLIER;
    protected FunctionType functionType = null;
    protected HashMap<String, Double> variables = new HashMap<>();

    protected Parametrise() {
        LowbrainCore.getInstance().warn("A Multiplier was created using default constructor !");
    }

    @Contract("null -> fail")
    protected Parametrise(ConfigurationSection config) {
        if(config == null)
            throw new NullPointerException("Cannot construct Multiplier with null config !");

        setEnabled(config.getBoolean("enable", true));
        setRange(config.getDouble("range"));
        setMax(config.getDouble("maximum"));
        setMin(config.getDouble("minimum"));
        setFunction(config.getString("function"));
        setRangeType(RangeType.get(config.getInt("range_type", 0)));

        int ftypeValue = config.getInt("function_type", -1);
        if (ftypeValue >= 0)
            setFunctionType(FunctionType.get(ftypeValue));


        ConfigurationSection vars = config.getConfigurationSection("variables");
        if(vars != null){
            for (String k : vars.getKeys(false))
                variables.put(k,vars.getDouble(k,0));
        }
    }

    public double getMax() {
        return this.max;
    }

    public double getMin() {
        return this.min;
    }

    public String getFunction() {
        return this.function;
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    public double getRange() {
        return this.range;
    }

    public RangeType getRangeType() {
        return this.rangeType;
    }

    public HashMap<String, Double> getVariables() {
        return this.variables;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setRangeType(RangeType rangeType) {
        this.rangeType = rangeType;
    }

    public void setFunctionType(FunctionType functionType) {
        this.functionType = functionType;
    }

    public void setVariables(HashMap<String, Double> variables) {
        this.variables = variables;
    }
}
