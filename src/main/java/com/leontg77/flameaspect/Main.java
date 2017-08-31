/*
 * MIT License
 *
 * Copyright (c) 2017 Leon Vaktskjold <leontg77@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.leontg77.flameaspect;

import com.leontg77.flameaspect.commands.FlameAspectCommand;
import com.leontg77.flameaspect.listeners.FireAspectListener;
import com.leontg77.flameaspect.listeners.FlameListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the plugin.
 *
 * @author LeonTG
 */
public class Main extends JavaPlugin {
    public static final String PREFIX = "§6Flame Aspect §8» §7";

    @Override
    public void onEnable() {
        FireAspectListener fire = new FireAspectListener(this);
        FlameListener flame = new FlameListener(this);

        // register command.
        getCommand("flameaspect").setExecutor(new FlameAspectCommand(this, fire, flame));
    }

    /**
     * Send the given message to all online players.
     *
     * @param message The message to send.
     */
    public void broadcast(String message) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.sendMessage(message);
        }

        Bukkit.getLogger().info(message);
    }
}