package fr.ignishky.mtgcollection.infrastructure.spi.postgres

import jakarta.inject.Named
import org.springframework.data.jpa.repository.JpaRepository

@Named
interface EventRepository : JpaRepository<EventDTO, Int> {
}
