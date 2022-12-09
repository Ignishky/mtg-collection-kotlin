package fr.ignishky.mtgcollection.infrastructure.spi.postgres

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class EventDTORowMapper : RowMapper<EventDTO> {

    override fun mapRow(rs: ResultSet, rowNum: Int): EventDTO {
        return EventDTO(
            rs.getLong("id"),
            rs.getString("aggregate_id"),
            rs.getString("aggregate_name"),
            rs.getString("label"),
            rs.getTimestamp("instant").toInstant(),
            rs.getString("payload"),
            rs.getString("correlation_id")
        )
    }
}
