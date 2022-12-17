package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card

import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model.CardEntity
import jakarta.inject.Named
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface CardRepository : JpaRepository<CardEntity, String>
