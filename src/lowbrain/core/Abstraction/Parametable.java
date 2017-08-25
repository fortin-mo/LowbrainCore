package lowbrain.core.Abstraction;

import lowbrain.core.commun.FunctionType;
import lowbrain.core.commun.RangeType;
import lowbrain.core.main.LowbrainCore;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;

/**
 * Created by Mooffy on 2017-06-23.
 */
public abstract class Parametable {
    protected Float max = 1F;
    protected Float min = 1F;
    protected String function;
    protected Boolean enabled = true;
    protected Float range = 0F;
    protected RangeType rangeType = RangeType.ON_MULTIPLIER;
    protected FunctionType functionType = null;
    protected HashMap<String, Float> variables = new HashMap<>();

    protected Parametable() {
        LowbrainCore.getInstance().warn("A Multiplier was created using default constructor !");
    }

    @Contract("null -> fail")
    protected Parametable(ConfigurationSection config) {
        if(config == null) throw new NullPointerException("Cannot construct Multiplier with null config !");
        setEnabled(config.getBoolean("enable"));
        setRange((float)config.getDouble("range"));
        setMax((float)config.getDouble("maximum"));
        setMin((float)config.getDouble("minimum"));
        setFunction(config.getString("function"));
        setRangeType(RangeType.get(config.getInt("range_type", 0)));

        int ftypeValue = config.getInt("function_type", -1);
        if (ftypeValue >= 0) {
            setFunctionType(FunctionType.get(ftypeValue));
        }

        ConfigurationSection vars = config.getConfigurationSection("variables");
        if(vars != null){
            for (String k :
                    vars.getKeys(false)) {
                variables.put(k,(float)vars.getDouble(k,0));
            }
        }
    }

    public Float getMax() {
        return this.max;
    }

    public Float getMin() {
        return this.min;
    }

    public String getFunction() {
        return this.function;
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    public Float getRange() {
        return this.range;
    }

    public RangeType getRangeType() {
        return this.rangeType;
    }

    public HashMap<String, Float> getVariables() {
        return this.variables;
    }

    public void setMax(Float max) {
        this.max = max;
    }

    public void setMin(Float min) {
        this.min = min;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setRange(Float range) {
        this.range = range;
    }

    public void setRangeType(RangeType rangeType) {
        this.rangeType = rangeType;
    }

    public void setFunctionType(FunctionType functionType) {
        this.functionType = functionType;
    }

    public void setVariables(HashMap<String, Float> variables) {
        this.variables = variables;
    }
}
