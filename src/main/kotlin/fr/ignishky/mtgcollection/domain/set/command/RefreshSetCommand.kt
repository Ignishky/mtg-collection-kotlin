package fr.ignishky.mtgcollection.domain.set.command

import fr.ignishky.framework.cqrs.command.Command

data class RefreshSetCommand(
    private val fake: Boolean = true
) : Command
