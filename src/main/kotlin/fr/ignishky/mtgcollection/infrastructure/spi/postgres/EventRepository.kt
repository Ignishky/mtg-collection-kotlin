package fr.ignishky.mtgcollection.infrastructure.spi.postgres

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<EventDTO, Int> {
}
