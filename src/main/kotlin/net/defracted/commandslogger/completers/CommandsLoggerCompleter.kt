package net.defracted.commandslogger.completers

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class CommandsLoggerCompleter : TabCompleter {
    override fun onTabComplete(sender: CommandSender, cmd: Command, label: String, args: Array<String?>): List<String> {
        val completions: MutableList<String> = ArrayList()
        val possibleSubcommands: MutableList<String> = ArrayList()

        if (args.size == 1) {
            possibleSubcommands.add("target")
            possibleSubcommands.add("cleartargets")
            possibleSubcommands.add("showtargets")

            StringUtil.copyPartialMatches(args[0]!!, possibleSubcommands, completions)
            possibleSubcommands.clear()
            completions.sort()
        }

        if (args.size == 2 && args[0].equals("target", ignoreCase = true)) {
            possibleSubcommands.add("add")
            possibleSubcommands.add("remove")

            StringUtil.copyPartialMatches(args[1]!!, possibleSubcommands, completions)
            possibleSubcommands.clear()
            completions.sort()
        }

        if (args.size == 2 && (args[0].equals("cleartarget", ignoreCase = true) || args[0].equals("showtargets", ignoreCase = true))) {
            StringUtil.copyPartialMatches(args[1]!!, possibleSubcommands, completions)
            possibleSubcommands.clear()
            completions.sort()
        }

        if (args.size == 3 && (args[1].equals("add", ignoreCase = true) || args[1].equals("remove", ignoreCase = true))) {
            Bukkit.getServer().onlinePlayers.forEach { p: Player? -> possibleSubcommands.add(p!!.name) }

            StringUtil.copyPartialMatches(args[2]!!, possibleSubcommands, completions)
            possibleSubcommands.clear()
            completions.sort()
        }

        return completions
    }
}