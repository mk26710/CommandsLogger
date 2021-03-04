package dev.moretsu.commandslogger.commands

import dev.moretsu.commandslogger.Main
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandsLogger(private val plugin: Main) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name.equals("commandslogger", ignoreCase = true)) {
            if (sender !is Player) {
                return true
            }

            val player: Player = sender

            if (args.isEmpty()) {
                player.sendMessage(plugin.colorize("&cYou didn't specify any subcommand!"))
                return true
            }

            val subcommand = args[0]
            val currentTargets = plugin.loggerTargets[player.uniqueId]

            if (subcommand.equals("showtargets", ignoreCase = true)) {
                if (currentTargets.isNullOrEmpty()) {
                    player.sendMessage(plugin.colorize("&cYou don't have any targets."))
                } else {
                    player.sendMessage(plugin.colorize("&cList of your targets:"))
                    currentTargets.forEach { trgt -> player.sendMessage(plugin.colorize("&c- &f$trgt")) }
                }
            }

            // /commandslogger cleartargets
            if (subcommand.equals("cleartargets", ignoreCase = true)) {
                val updatedSet = HashSet<String>()

                if (currentTargets.isNullOrEmpty()) {
                    player.sendMessage(plugin.colorize("&cYou're not watching for anyone's commands!"))
                    return true
                } else {
                    plugin.loggerTargets[player.uniqueId] = updatedSet
                    player.sendMessage(plugin.colorize("&aYou have cleared your commands logger targets list."))
                }
            }

            // /commandslogger target
            if (subcommand.equals("target", ignoreCase = true)) {
                if (args.size < 2) {
                    player.sendMessage(plugin.colorize("&cYou didn't specify whether you'd like to add or remove a target."))
                    return true
                }

                val secondary_subcommand = args[1]

                // /commandslogger target add <name>
                if (secondary_subcommand.equals("add", ignoreCase = true)) {
                    if (args.size < 3) {
                        player.sendMessage(plugin.colorize("&cYou didn't specify target's name."))
                        return true
                    }

                    val target: Player? = Bukkit.getPlayerExact(args[2])

                    if (target == null) {
                        player.sendMessage("&f${args[2]} &cis offline.")
                        return true
                    }

                    var updatedSet = HashSet<String>()

                    if (currentTargets == null) {
                        updatedSet.add(target.name)
                    } else {
                        if (currentTargets.contains(target.name)) {
                            player.sendMessage(plugin.colorize("&cYou are alredy watching &f${target.name} &ccommands!"))
                            return true
                        }

                        updatedSet = currentTargets.toHashSet()
                        updatedSet.add(target.name)
                    }

                    plugin.loggerTargets[player.uniqueId] = updatedSet
                    player.sendMessage(plugin.colorize("&aYou are now watching for &f${target.name} &acommands."))
                }

                // /commandslogger target remove <name>
                if (secondary_subcommand.equals("remove", ignoreCase = true)) {
                    if (args.size < 3) {
                        player.sendMessage(plugin.colorize("&cYou didn't specify target's name."))
                        return true
                    }
                    val target = args[2]

                    val updatedSet: HashSet<String>

                    if (currentTargets == null) {
                        player.sendMessage(plugin.colorize("&cYou're not watching for anyone's commands!"))
                        return true
                    } else {
                        if (!currentTargets.contains(target)) {
                            player.sendMessage(plugin.colorize("&cYou're not watching for &f$target &ccommands."))
                            return true
                        }

                        updatedSet = currentTargets.toHashSet()
                        updatedSet.remove(target)
                    }

                    plugin.loggerTargets[player.uniqueId] = updatedSet
                    player.sendMessage(plugin.colorize("&aYou are no longer watching for &f${target} &acommands."))
                }
            }

            return true
        }

        return false
    }
}