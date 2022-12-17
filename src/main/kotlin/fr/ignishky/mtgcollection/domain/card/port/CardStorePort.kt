package fr.ignishky.mtgcollection.domain.card.port

import fr.ignishky.mtgcollection.domain.card.model.Card

interface CardStorePort {

    fun store(card: Card)

}
