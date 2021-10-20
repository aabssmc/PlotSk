package me.github.reportcardsmc.plotsk5.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.plotsquared.core.plot.Plot;
import com.sk89q.worldedit.math.BlockVector3;
import me.github.reportcardsmc.plotsk5.utils.PlotSquaredUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class PlotHomeExpr extends SimpleExpression<Location> {
    static {
        Skript.registerExpression(PlotHomeExpr.class, Location.class, ExpressionType.COMBINED, "[PlotSquared] [the] home [location] of plot [with id] %string%");
    }

    private Expression<String> plotID;

    @Nullable
    @Override
    protected Location[] get(Event e) {
        Plot plot;
        if (plotID.getSingle(e) == null || (plot = PlotSquaredUtil.getPlot(plotID.getSingle(e))) == null) return null;
        BlockVector3 vector3 = plot.getHomeSynchronous().getBlockVector3();
        return new Location[]{new Location(Bukkit.getWorld(plot.getWorldName()), vector3.getX(), vector3.getY(), vector3.getZ())};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Biome of plot: " + plotID.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        plotID = (Expression<String>) exprs[0];
        return true;
    }
}