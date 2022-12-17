package fr.ignishky.mtgcollection.infrastructure

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardName
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.model.SetId
import fr.ignishky.mtgcollection.domain.set.model.SetName

object TestFixtures {

    fun snc(): Set {
        return Set(
            SetId("df837242-8c15-42e4-b049-c933a02dc501"),
            SetCode("snc"),
            SetName("Streets of New Capenna")
        )
    }

    fun afr(): Set {
        return Set(
            SetId("e1ef87ba-ba92-4573-817f-543b996d2851"),
            SetCode("afr"),
            SetName("Adventures in the Forgotten Realms")
        )
    }

    fun khm(): Set {
        return Set(
            SetId("43057fad-b1c1-437f-bc48-0045bce6d8c9"),
            SetCode("khm"),
            SetName("Kaldheim")
        )
    }

    fun angelicObserver(): Card {
        return Card(
            CardId("e6cce4d3-e6d8-4c6f-9d9c-c0a8a607a42f"),
            CardName("Angelic Observer")
        )
    }

}
