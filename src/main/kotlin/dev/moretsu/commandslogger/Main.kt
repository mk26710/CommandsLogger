package dev.moretsu.commandslogger

import dev.moretsu.commandslogger.commands.CommandsLogger
import dev.moretsu.commandslogger.completers.CommandsLoggerCompleter
import dev.moretsu.commandslogger.listeners.CommandsUsed
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.HashMap

class Main : JavaPlugin() {
    val loggerTargets = HashMap<UUID, HashSet<String>>()

    companion object {
        val logger = Bukkit.getLogger()
    }

    override fun onEnable() {
        loadListener(CommandsUsed(this))
        loadCommand("commandslogger", CommandsLogger(this), CommandsLoggerCompleter())
    }

    override fun onDisable() {
        loggerTargets.clear()
    }

    fun colorize(msg: String): String? {
        return ChatColor.translateAlternateColorCodes('&', msg)
    }

    private fun loadCommand(command: String, Executor: CommandExecutor, Completer: TabCompleter?) {
        this.getCommand(command)?.executor = Executor
        this.getCommand(command)?.tabCompleter = Completer
    }

    private fun loadListener(listener: Listener) {
        this.server.pluginManager.registerEvents(listener, this)
    }
}
