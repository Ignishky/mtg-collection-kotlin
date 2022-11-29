package fr.ignishky.mtgcollection.infrastructure.spi.postgres

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@Entity
@Table(name = "events")
data class EventDTO(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(columnDefinition = "bigserial")
    private val id: Long = 0,
    @Column(name = "aggregate_id")
    private val aggregateId: String,
    @Column(name = "aggregate_name")
    private val aggregateName: String,
    private val label: String,
    private val instant: LocalDateTime,
    private val payload: String,
    @Column(name = "correlation_id")
    private val correlationId: String,
) {

    constructor() : this(0, "", "", "", now(), "", "")
    constructor(
        aggregateId: String,
        aggregateName: String,
        label: String,
        payload: String,
        correlationId: String
    ) : this(0, aggregateId, aggregateName, label, now(), payload, correlationId)

}
