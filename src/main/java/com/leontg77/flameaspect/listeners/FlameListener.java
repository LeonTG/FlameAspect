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

package com.leontg77.flameaspect.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.leontg77.flameaspect.Main;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

/**
 * Flame listener class.
 *
 * @author LeonTG
 */
public class FlameListener extends PacketAdapter implements Listener {
    private static final Enchantment ENCHANTMENT = Enchantment.ARROW_FIRE;

    public FlameListener(Main plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.CRAFT_PROGRESS_BAR);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (!event.getPacketType().equals(PacketType.Play.Server.CRAFT_PROGRESS_BAR)) {
            return;
        }

        StructureModifier<Integer> integers = event.getPacket().getIntegers();
        int property = integers.read(1);

        if (property >= 4) {
            int enchant = integers.read(2);

            if (property >= 7) {
                if (enchant == ENCHANTMENT.getId()) {
                    integers.write(2, 48);
                }
            } else {
                if (enchant == ENCHANTMENT.getId()) {
                    integers.write(2, 3);
                }
            }
        }
    }

    @EventHandler
    public void on(EnchantItemEvent event) {
        Map<Enchantment, Integer> toAdd = event.getEnchantsToAdd();

        if (!toAdd.containsKey(ENCHANTMENT)) {
            return;
        }

        toAdd.remove(ENCHANTMENT);

        if (toAdd.containsKey(Enchantment.ARROW_DAMAGE)) {
            return;
        }

        toAdd.put(Enchantment.ARROW_DAMAGE, 3);
    }

    @EventHandler
    public void on(PlayerFishEvent event) {
        Entity caught = event.getCaught();

        if (caught == null) {
            return;
        }

        if (!(caught instanceof Item)) {
            return;
        }

        Item item = (Item) caught;

        ItemStack stack = item.getItemStack();
        ItemMeta meta = stack.getItemMeta();

        if (meta.hasEnchant(ENCHANTMENT)) {
            meta.removeEnchant(ENCHANTMENT);
        }

        stack.setItemMeta(meta);
    }
}