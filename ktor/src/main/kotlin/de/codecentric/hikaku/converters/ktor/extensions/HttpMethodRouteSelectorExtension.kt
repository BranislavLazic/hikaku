package de.codecentric.hikaku.converters.ktor.extensions

import de.codecentric.hikaku.endpoints.HttpMethod
import io.ktor.routing.HttpMethodRouteSelector

internal fun HttpMethodRouteSelector.httpMethod(): HttpMethod {
    return HttpMethod.valueOf(this.method.value)
}