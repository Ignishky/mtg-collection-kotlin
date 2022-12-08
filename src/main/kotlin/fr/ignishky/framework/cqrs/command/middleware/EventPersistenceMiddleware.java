package fr.ignishky.framework.cqrs.command.middleware;

import fr.ignishky.framework.cqrs.command.Command;
import fr.ignishky.framework.cqrs.event.Event;
import fr.ignishky.framework.cqrs.event.EventStore;
import fr.ignishky.framework.domain.CorrelationId;

import java.util.List;

public class EventPersistenceMiddleware extends CommandMiddleware {

    private final EventStore eventStore;

    public EventPersistenceMiddleware(CommandMiddleware next, EventStore eventStore) {
        super(next);
        this.eventStore = eventStore;
    }

    @Override
    public List<Event<?, ?, ?>> handle(Command command, CorrelationId correlationId) {
        var response = next(command, correlationId);
        eventStore.store(response);
        return response;
    }

    public record Builder(EventStore eventStore) implements CommandMiddlewareBuilder {

        @Override
        public CommandMiddleware chain(CommandMiddleware next) {
            return new EventPersistenceMiddleware(next, eventStore);
        }

    }

}
