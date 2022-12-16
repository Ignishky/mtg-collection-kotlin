package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.mapper

import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.SetEntity
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class SetEntityRowMapper : RowMapper<SetEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): SetEntity {
        return SetEntity(
            rs.getString("id"),
            rs.getString("code"),
            rs.getString("name")
        )
    }

}
