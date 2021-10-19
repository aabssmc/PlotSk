package me.github.reportcardsmc.plotsk5.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.plotsquared.core.plot.Plot;
import me.github.reportcardsmc.plotsk5.utils.PlotSquaredUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class PlayerIsTrustedCond extends Condition {

    static {
        Skript.registerCondition(PlayerIsTrustedCond.class, "[plotsquared] %offlineplayer% (1¦is|2¦is(n't| not)) trusted in [plot] [with id] %string%");
    }

    private Expression<OfflinePlayer> playerExpression;
    private Expression<String> plotID;

    @Override
    public boolean check(Event e) {
        String id = plotID.getSingle(e);
        OfflinePlayer player = playerExpression.getSingle(e);
        Plot plot;
        if (id == null || player == null || (plot = PlotSquaredUtil.getPlot(id)) == null) return false;
        boolean value = plot.isOwner(player.getUniqueId()) || plot.getTrusted().stream().anyMatch((v) -> v == player.getUniqueId());
        if (isNegated()) return !value;
        return value;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return playerExpression.toString(e, debug) + " is trusted in " + plotID.toString(e, debug) + "?";
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpression = (Expression<OfflinePlayer>) exprs[0];
        plotID = (Expression<String>) exprs[1];
        setNegated(parseResult.mark == 2);
        return true;
    }
}