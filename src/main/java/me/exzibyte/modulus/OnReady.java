package me.exzibyte.modulus;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class OnReady extends ListenerAdapter {

    public void onReady(ReadyEvent event) {
        Game[] games = { Game.playing("with modular bits!"), Game.watching("Project Modulus"), Game.watching("TV"), Game.streaming("Modularity", "https://twitch.tv/exzibyte") };
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int game = random.nextInt(games.length);
                event.getJDA().getPresence().setGame(games[game]);
            }
        }, 0, 2, TimeUnit.MINUTES);
    }
}
