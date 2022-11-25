package fr.ignishky.framework.domain

interface Aggregate<I> {
    fun id(): I
}