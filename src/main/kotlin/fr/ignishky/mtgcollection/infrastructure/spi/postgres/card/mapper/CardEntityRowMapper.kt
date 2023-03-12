package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.mapper

import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model.CardEntity
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class CardEntityRowMapper : RowMapper<CardEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): CardEntity {
        return CardEntity(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("set_code"),
            rs.getString("images"),
            rs.getInt("collection_number"),
        )
    }

}
