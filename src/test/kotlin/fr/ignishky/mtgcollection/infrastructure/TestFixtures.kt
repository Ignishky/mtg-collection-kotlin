package fr.ignishky.mtgcollection.infrastructure

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardName
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set

object TestFixtures {

    fun snc(): Set {
        return Set(
            SetId("df837242-8c15-42e4-b049-c933a02dc501"),
            SetCode("snc"),
            SetName("Streets of New Capenna"),
            SetIcon("https://scryfall.mtgc.test/sets/snc.svg")
        )
    }

    fun afr(): Set {
        return Set(
            SetId("e1ef87ba-ba92-4573-817f-543b996d2851"),
            SetCode("afr"),
            SetName("Adventures in the Forgotten Realms"),
            SetIcon("https://scryfall.mtgc.test/sets/afr.svg")
        )
    }

    fun khm(): Set {
        return Set(
            SetId("43057fad-b1c1-437f-bc48-0045bce6d8c9"),
            SetCode("khm"),
            SetName("Kaldheim"),
            SetIcon("https://scryfall.mtgc.test/sets/khm.svg")
        )
    }

    fun plus2Mace(): Card {
        return Card(
            CardId("e882c9f9-bf30-46b6-bedc-379d2c80e5cb"),
            CardName("+2 Mace"),
            SetCode("afr")
        )
    }

    fun arboreaPegasus(): Card {
        return Card(
            CardId("fc45c9d4-ecc7-4a9d-9efe-f4b7d697dd97"),
            CardName("Arborea Pegasus"),
            SetCode("afr")
        )
    }

    fun valorSinger(): Card {
        return Card(
            CardId("89bc162c-bdf1-43f7-882f-d8cee4f3f415"),
            CardName("Valor Singer"),
            SetCode("afr")
        )
    }

    fun axgardBraggart(): Card {
        return Card(
            CardId("4de5ff64-6fe7-4fc5-be27-cdbaa14545ab"),
            CardName("Axgard Braggart"),
            SetCode("khm")
        )
    }

}
