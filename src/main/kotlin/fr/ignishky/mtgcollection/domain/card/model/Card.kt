package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.framework.domain.Aggregate

data class Card(
    internal val id: CardId,
    internal val name: CardName
) : Aggregate<CardId> {

    constructor() : this(CardId(""), CardName(""))

    override fun id(): CardId {
        return id
    }

}
