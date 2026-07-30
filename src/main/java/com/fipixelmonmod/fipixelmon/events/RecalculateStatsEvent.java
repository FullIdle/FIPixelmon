package com.fipixelmonmod.fipixelmon.events;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import lombok.Getter;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;


@Getter
public class RecalculateStatsEvent extends Event {
    private final Stats stats;

    public RecalculateStatsEvent(Stats stats) {
        this.stats = stats;
    }

    @Cancelable
    public static class Pre extends RecalculateStatsEvent {
        public Pre(Stats stats) {
            super(stats);
        }
    }

    public static class Post extends RecalculateStatsEvent {
        public Post(Stats stats) {
            super(stats);
        }
    }
}
