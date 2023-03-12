package fr.ignishky.mtgcollection.infrastructure

import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set

object TestFixtures {

    fun afr(): Set {
        return Set(
            SetId("e1ef87ba-ba92-4573-817f-543b996d2851"),
            SetCode("afr"),
            SetName("Adventures in the Forgotten Realms"),
            SetIcon("https://scryfall.mtgc.test/sets/afr.svg")
        )
    }

    fun plus2Mace(): Card {
        return Card(
            CardId("e882c9f9-bf30-46b6-bedc-379d2c80e5cb"),
            CardName("+2 Mace"),
            SetCode("afr"),
            listOf(CardImage("https://scryfall.mtgc.test/cards/plus2mace.svg")),
            CollectionNumber(1),
        )
    }

    fun arboreaPegasus(): Card {
        return Card(
            CardId("fc45c9d4-ecc7-4a9d-9efe-f4b7d697dd97"),
            CardName("Arborea Pegasus"),
            SetCode("afr"),
            listOf(CardImage("https://scryfall.mtgc.test/cards/arboreaPegasus.svg")),
            CollectionNumber(2),
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

    fun valorSinger(): Card {
        return Card(
            CardId("89bc162c-bdf1-43f7-882f-d8cee4f3f415"),
            CardName("Valor Singer"),
            SetCode("afr"),
            listOf(CardImage("https://scryfall.mtgc.test/cards/valorSinger.svg")),
            CollectionNumber(165),
        )
    }

    fun axgardBraggart(): Card {
        return Card(
            CardId("4de5ff64-6fe7-4fc5-be27-cdbaa14545ab"),
            CardName("Axgard Braggart"),
            SetCode("khm"),
            listOf(CardImage("https://scryfall.mtgc.test/cards/axgardBraggart.svg")),
            CollectionNumber(1),
        )
    }

    fun halvar(): Card {
        return Card(
            CardId("97502411-5c93-434c-b77b-ceb2c32feae7"),
            CardName("Halvar, God of Battle // Sword of the Realms"),
            SetCode("khm"),
            listOf(CardImage("https://scryfall.mtgc.test/cards/halvar.svg"), CardImage("https://scryfall.mtgc.test/cards/swordOfTheRealms.svg")),
            CollectionNumber(15),
        )
    }

}
