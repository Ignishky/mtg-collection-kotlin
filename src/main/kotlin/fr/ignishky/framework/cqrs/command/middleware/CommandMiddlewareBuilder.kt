package fr.ignishky.framework.cqrs.command.middleware

interface CommandMiddlewareBuilder {

    fun chain(next: CommandMiddleware): CommandMiddleware

    companion object {
        fun build(builders: List<CommandMiddlewareBuilder>): CommandMiddleware {
            return builders.foldRight(
                CircuitBreakerMiddleware(),
                { builder: CommandMiddlewareBuilder, next: CommandMiddleware -> builder.chain(next) }
            )
        }
    }
}