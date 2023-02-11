package fr.ignishky.mtgcollection.infrastructure

import fr.ignishky.framework.cqrs.event.spi.postgres.EventEntity
import fr.ignishky.framework.cqrs.event.spi.postgres.EventEntityRowMapper
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.mapper.CardEntityRowMapper
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model.CardEntity
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.mapper.SetEntityRowMapper
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.SetEntity
import jakarta.inject.Named
import org.springframework.jdbc.core.JdbcTemplate

@Named
class JdbcUtils(private val template: JdbcTemplate) {

    fun dropAll() {
        template.update("DELETE FROM sets")
        template.update("DELETE FROM cards")
    }

    fun save(sets: List<Set>, cards: List<Card>) {
        sets.map { template.update("INSERT INTO sets (id, code, name) VALUES (?, ?, ?)", it.id.value, it.code.value, it.name.value) }
        cards.map { template.update("INSERT INTO cards (id, name, set_code) VALUES (?, ?, ?)", it.id.value, it.name.value, it.setCode.value) }
    }

    fun getEvents(): List<EventEntity> {
        return template.query("SELECT * FROM events", EventEntityRowMapper())
    }

    fun getSets(): List<SetEntity> {
        return template.query("SELECT * FROM sets", SetEntityRowMapper())
    }

    fun getCards(): List<CardEntity> {
        return template.query("SELECT * FROM cards", CardEntityRowMapper())
    }

}
