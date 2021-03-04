package dev.moretsu.commandslogger.listeners

import dev.moretsu.commandslogger.Main
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandsUsed(private val plugin: Main) : Listener {
    @EventHandler
    fun onUsedCommand(event: PlayerCommandPreprocessEvent) {
        val player: Player = event.player

        Bukkit.getServer().onlinePlayers.forEach { p ->
            if (p.hasPermission("commandslogger.notify")) {
                val playerTargets = plugin.loggerTargets[p.uniqueId]

                if (playerTargets != null) {
                    if (playerTargets.contains(player.name)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7${player.name} used a command: ${event.message}"))
                    }
                }
            }
        }
    }
}