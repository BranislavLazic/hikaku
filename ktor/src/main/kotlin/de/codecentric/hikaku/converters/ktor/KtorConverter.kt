package de.codecentric.hikaku.converters.ktor

import de.codecentric.hikaku.SupportedFeatures
import de.codecentric.hikaku.SupportedFeatures.Feature
import de.codecentric.hikaku.converters.AbstractEndpointConverter
import de.codecentric.hikaku.converters.ktor.HttpMethodDeclaration.POST_DECLARATION
import de.codecentric.hikaku.converters.ktor.HttpMethodDeclaration.PRE_DECLARATION
import de.codecentric.hikaku.converters.ktor.extensions.hikakuHttpMethod
import de.codecentric.hikaku.converters.ktor.extensions.hikakuPathParameters
import de.codecentric.hikaku.endpoints.Endpoint
import io.ktor.routing.HttpMethodRouteSelector
import io.ktor.routing.Route
import io.ktor.routing.Routing

/**
 * Extracts and converts [Endpoint]s from ktor [Routing] object.
 * @param routing Ktor routing information.
 */
class KtorConverter(private val routing: Routing): AbstractEndpointConverter() {

    override val supportedFeatures = SupportedFeatures(
            Feature.PathParameters
    )

    override fun convert(): Set<Endpoint> {
        return routing.children.flatMap {
            return@flatMap findEndpoints(it)
        }
        .toSet()
    }

    private fun findEndpoints(route: Route): List<Endpoint> {
        if (route.children.isNotEmpty()) {
            return route.children.flatMap {
                return@flatMap findEndpoints(it)
            }
        }

        println(route.toString())

        when {
            route.selector is HttpMethodRouteSelector -> POST_DECLARATION
            route.parent?.selector is HttpMethodRouteSelector -> PRE_DECLARATION
            else ->null
        }?.let {
            return listOf(
                        createEndpoint(route, it)
            )
        }

        return emptyList()
    }

    private fun createEndpoint(route: Route, declarationType: HttpMethodDeclaration): Endpoint {
        return when(declarationType) {
            PRE_DECLARATION -> {
                Endpoint(
                        path = normalizePath(route.parent?.parent.toString() + route.selector.toString()),
                        httpMethod = (route.parent?.selector as HttpMethodRouteSelector).hikakuHttpMethod(),
                        pathParameters = route.hikakuPathParameters()
                )
            }
            POST_DECLARATION -> {
                Endpoint(
                        path = normalizePath(route.parent.toString()),
                        httpMethod = (route.selector as HttpMethodRouteSelector).hikakuHttpMethod(),
                        pathParameters = route.hikakuPathParameters()
                )
            }
        }
    }

    /**
     * Removes ? from optional path parameters, because an optional path parameter will lead to two endpoints.
     * Example:
     *      /todos/{id?}
     * will become
     *      /todos
     *      /todos/{id}
     */
    private fun normalizePath(path: String) = path.replace("?", "")
}

private enum class HttpMethodDeclaration {
    PRE_DECLARATION, POST_DECLARATION
}