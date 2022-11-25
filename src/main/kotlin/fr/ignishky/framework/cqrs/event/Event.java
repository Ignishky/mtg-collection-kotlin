package fr.ignishky.framework.cqrs.event;

import fr.ignishky.framework.domain.Aggregate;

import java.time.Instant;

public abstract class Event<I, A extends Aggregate<I>, P extends Payload> {

    protected final String id;
    protected final I aggregateId;
    private final Class<A> aggregateClass;
    protected final P payload;
    protected final Instant instant;

    protected Event(String id, I aggregateId, Class<A> aggregateClass, P payload, Instant instant) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.aggregateClass = aggregateClass;
        this.payload = payload;
        this.instant = instant;
    }

    public abstract A apply(A aggregate);

}
