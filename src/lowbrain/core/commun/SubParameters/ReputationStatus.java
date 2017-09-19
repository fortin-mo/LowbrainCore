package lowbrain.core.commun.SubParameters;

import lowbrain.core.commun.Helper;
import lowbrain.library.fn;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;

/**
 * represents a configurable status
 */
public class ReputationStatus {
    private String name;
    private int from;
    private int to;

    @Contract("null -> fail")
    public ReputationStatus(ConfigurationSection config, String name) {
        if (config == null)
            throw new Error("failed to initiate ReputationStatus : null config");

        if (fn.StringIsNullOrEmpty(name))
            throw new Error("failed to initiate ReputationStatus: null name");

        this.name = name;

        String sFrom = config.getString("from");
        if (sFrom == "-inf" || sFrom == "-infinite")
            from = -Integer.MAX_VALUE;
        else if (sFrom == "+inf" || sFrom == "+infinite")
            from = Integer.MAX_VALUE;
        else from = config.getInt("from", 0);

        String sTo = config.getString("to");
        if (sTo == "-inf" || sTo == "-infinite")
            to = -Integer.MAX_VALUE;
        else if (sTo == "+inf" || sTo == "+infinite")
            to = Integer.MAX_VALUE;
        else to = config.getInt("to", 0);
    }

    public String getName() {
        return name;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
