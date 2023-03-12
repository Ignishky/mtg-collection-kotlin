package fr.ignishky.mtgcollection.domain.card.model

import fr.ignishky.framework.domain.Aggregate
import fr.ignishky.mtgcollection.domain.set.model.SetCode

data class Card(
    internal val id: CardId,
    internal val name: CardName,
    internal val setCode: SetCode,
    internal val prices: Prices,
    internal val images: List<CardImage>,
    internal val collectionNumber: CollectionNumber,
) : Aggregate<CardId> {

    constructor() : this(
        CardId(""),
        CardName(""),
        SetCode(""),
        Prices(Price(0, 0, 0, 0)),
        emptyList(),
        CollectionNumber(""),
    )

    override fun id(): CardId {
        return id
    }

}
