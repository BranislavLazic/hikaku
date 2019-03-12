package de.codecentric.hikaku.converters.ktor

import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.GET
import de.codecentric.hikaku.endpoints.HttpMethod.POST
import de.codecentric.hikaku.endpoints.QueryParameter
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.routing.*
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class KtorConverterQueryParameterTest {

    @Nested
    inner class SimplePathTests {

        @Test
        fun `method function within route function containing path`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todos") {
                    method(Get) {
                        optionalParam("filter") { }
                        param("limit") { }
                    }
                }
            }

            val specification = setOf(
                    Endpoint(
                            path = "/todos",
                            httpMethod = GET,
                            queryParameters = setOf(
                                    QueryParameter("limit", true),
                                    QueryParameter("filter", false)
                            )
                    )
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `route function having both path and http method`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todos", Get) {
                    optionalParam("filter") { }
                    param("limit") { }
                }
            }

            val specification = setOf(
                    Endpoint(
                            path = "/todos",
                            httpMethod = GET,
                            queryParameters = setOf(
                                    QueryParameter("limit", true),
                                    QueryParameter("filter", false)
                            )
                    )
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `route function declared within method function`() {
            //given
            val routing = Routing(mockk()).apply {
                method(Get) {
                    route("/todos") {
                        optionalParam("filter") { }
                        param("limit") { }
                    }
                }
            }

            val specification = setOf(
                    Endpoint(
                            path = "/todos",
                            httpMethod = GET,
                            queryParameters = setOf(
                                    QueryParameter("limit", true),
                                    QueryParameter("filter", false)
                            )
                    )
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }
    }

    @Nested
    inner class NestedPathDeclarationTests {

        @Test
        fun `method function within nested route function containing path`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todo") {
                    route("/list") {
                        method(Get) {
                            optionalParam("filter") { }
                            param("limit") { }
                        }
                    }
                }
            }

            val specification = setOf(
                    Endpoint(
                            path = "/todo/list",
                            httpMethod = GET,
                            queryParameters = setOf(
                                    QueryParameter("limit", true),
                                    QueryParameter("filter", false)
                            )
                    )
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `extract GET with parameters and POST declared in method function within nested route declarations`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todo") {
                    method(Post) { }
                    route("/list") {
                        method(Get) {
                            optionalParam("filter") { }
                            param("limit") { }
                        }
                    }
                }
            }

            val specification = setOf(
                    Endpoint(
                            path = "/todo/list",
                            httpMethod = GET,
                            queryParameters = setOf(
                                    QueryParameter("limit", true),
                                    QueryParameter("filter", false)
                            )
                    ),
                    Endpoint("/todo", POST)
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `nested route function having both path and http method`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todo") {
                    route("/list", Get) {
                        optionalParam("filter") { }
                        param("limit") { }
                    }
                }
            }

            val specification = setOf(
                    Endpoint(
                            path = "/todo/list",
                            httpMethod = GET,
                            queryParameters = setOf(
                                    QueryParameter("limit", true),
                                    QueryParameter("filter", false)
                            )
                    )
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }
    }
}