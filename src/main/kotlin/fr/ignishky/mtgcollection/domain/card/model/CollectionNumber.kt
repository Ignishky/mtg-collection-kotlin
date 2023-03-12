package fr.ignishky.mtgcollection.domain.card.model

data class CollectionNumber(internal val value: String) : Comparable<CollectionNumber> {

    override fun compareTo(other: CollectionNumber): Int {
        val a = value.toIntOrNull()
        val b = other.value.toIntOrNull()
        if (a == b) return 0
        if (a == null) return 1
        if (b == null) return -1

        return a.compareTo(b)
    }
}
