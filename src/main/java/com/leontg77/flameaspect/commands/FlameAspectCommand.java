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

package com.leontg77.flameaspect.commands;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.Lists;
import com.leontg77.flameaspect.Main;
import com.leontg77.flameaspect.listeners.FireAspectListener;
import com.leontg77.flameaspect.listeners.FlameListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.util.StringUtil;

import java.util.List;

/**
 * Flame Aspect command class.
 *
 * @author LeonTG
 */
public class FlameAspectCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION = "flameaspect.manage";

    private final FireAspectListener fireListener;
    private final FlameListener flameListener;

    private final Main plugin;

    public FlameAspectCommand(Main plugin, FireAspectListener fireListener, FlameListener flameListener) {
        this.plugin = plugin;

        this.flameListener = flameListener;
        this.fireListener = fireListener;
    }

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    private boolean fireaspect = false;
    private boolean flame = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Main.PREFIX + "Usage: /flameaspect <info/fireaspect/flame>");
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(Main.PREFIX + "Plugin creator: §aLeonTG");
            sender.sendMessage(Main.PREFIX + "Version: §a" + plugin.getDescription().getVersion());
            sender.sendMessage(Main.PREFIX + "Description:");
            sender.sendMessage("§8» §f" + plugin.getDescription().getDescription());
            return true;
        }

        if (args[0].equalsIgnoreCase("fireaspect")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            this.fireaspect = !fireaspect;

            if (fireaspect) {
                Bukkit.getPluginManager().registerEvents(fireListener, plugin);
                manager.addPacketListener(fireListener);

                plugin.broadcast(Main.PREFIX + "Fire Aspect has been enabled.");
            } else {
                manager.removePacketListener(fireListener);
                HandlerList.unregisterAll(fireListener);

                plugin.broadcast(Main.PREFIX + "Fire Aspect has been disabled.");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("flame")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            this.flame = !flame;

            if (flame) {
                Bukkit.getPluginManager().registerEvents(flameListener, plugin);
                manager.addPacketListener(flameListener);

                plugin.broadcast(Main.PREFIX + "Flame has been enabled.");
            } else {
                manager.removePacketListener(flameListener);
                HandlerList.unregisterAll(flameListener);

                plugin.broadcast(Main.PREFIX + "Flame has been disabled.");
            }
            return true;
        }

        sender.sendMessage(Main.PREFIX + "Usage: /flameaspect <info/fireaspect/flame>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> toReturn = Lists.newArrayList();

        if (args.length != 1) {
            return toReturn;
        }

        toReturn.add("info");

        if (sender.hasPermission(PERMISSION)) {
            toReturn.add("fireaspect");
            toReturn.add("flame");
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], toReturn, Lists.newArrayList());
    }
}