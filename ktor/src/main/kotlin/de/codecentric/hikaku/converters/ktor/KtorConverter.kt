package de.codecentric.hikaku.converters.ktor

import de.codecentric.hikaku.SupportedFeatures
import de.codecentric.hikaku.SupportedFeatures.Feature
import de.codecentric.hikaku.converters.AbstractEndpointConverter
import de.codecentric.hikaku.converters.ktor.extensions.hikakuHttpMethod
import de.codecentric.hikaku.endpoints.*
import io.ktor.routing.*

/**
 * Extracts and converts [Endpoint]s from ktor [Routing] object.
 * @param routing ktor routing information.
 */
class KtorConverter(private val routing: Routing): AbstractEndpointConverter() {

    override val supportedFeatures = SupportedFeatures(
            Feature.PathParameters,
            Feature.QueryParameters,
            Feature.HeaderParameters
    )

    override fun convert(): Set<Endpoint> {
        val leafs = routing.children.flatMap {
            return@flatMap findLeafs(it)
        }

        return leafs.map {
            var parent: Route? = it
            var path = ""
            var httpMethod = HttpMethod.OPTIONS
            val pathParameters = mutableSetOf<PathParameter>()
            val queryParameters = mutableSetOf<QueryParameter>()
            val headerParameters = mutableSetOf<HeaderParameter>()

            while (parent != null) {
                when {
                    parent.selector is HttpMethodRouteSelector -> httpMethod = (parent.selector as HttpMethodRouteSelector).hikakuHttpMethod()
                    parent.selector is PathSegmentConstantRouteSelector -> {
                        val pathSegment = (parent.selector as PathSegmentConstantRouteSelector).value
                        path = mergePath(path, pathSegment)
                    }
                    parent.selector is PathSegmentParameterRouteSelector -> {
                        val pathParameterName = (parent.selector as PathSegmentParameterRouteSelector).name
                        path = mergePath(path, "{$pathParameterName}")
                        pathParameters.add(PathParameter(pathParameterName))
                    }
                    parent.selector is PathSegmentOptionalParameterRouteSelector -> {
                        val pathParameterName = (parent.selector as PathSegmentOptionalParameterRouteSelector).name.replace("?", "")
                        path = mergePath(path, "{$pathParameterName}")
                        pathParameters.add(PathParameter(pathParameterName))
                    }
                    parent.selector is PathSegmentTailcardRouteSelector -> {
                        val pathParameterName = (parent.selector as PathSegmentTailcardRouteSelector).name
                        path = mergePath(path, "{$pathParameterName}")
                        pathParameters.add(PathParameter(pathParameterName))
                    }
                    parent.selector is ParameterRouteSelector -> queryParameters.add(QueryParameter((parent.selector as ParameterRouteSelector).name, true))
                    parent.selector is ConstantParameterRouteSelector -> queryParameters.add(QueryParameter((parent.selector as ConstantParameterRouteSelector).name, true))
                    parent.selector is OptionalParameterRouteSelector -> queryParameters.add(QueryParameter((parent.selector as OptionalParameterRouteSelector).name, false))
                    parent.selector is HttpHeaderRouteSelector -> headerParameters.add(HeaderParameter((parent.selector as HttpHeaderRouteSelector).name, true))
                    parent is Routing -> path = "/$path"
                }

                parent = parent.parent
            }

            return@map Endpoint(
                    path = path,
                    httpMethod = httpMethod,
                    pathParameters = pathParameters,
                    queryParameters = queryParameters,
                    headerParameters = headerParameters
            )
        }
        .groupBy { it.path }
        .map {
            it.value.reduce { a, b ->
                Endpoint(
                        path = it.key,
                        httpMethod = a.httpMethod,
                        pathParameters = a.pathParameters.union(b.pathParameters),
                        queryParameters = a.queryParameters.union(b.queryParameters),
                        headerParameters = a.headerParameters.union(b.headerParameters)
                )
            }
        }
        .toSet()
    }

    private fun mergePath(path: String, pathSegment: String): String {
        return if (path.isNotBlank()) {
            "$pathSegment/$path"
        } else {
            pathSegment
        }
    }

    private fun findLeafs(route: Route): List<Route> {
        if (route.children.isNotEmpty()) {
            return route.children.flatMap {
                return@flatMap findLeafs(it)
            }
        }

        return listOf(route)
    }
}