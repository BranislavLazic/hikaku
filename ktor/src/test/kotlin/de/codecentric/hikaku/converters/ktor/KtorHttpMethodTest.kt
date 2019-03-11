package de.codecentric.hikaku.converters.ktor

import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.*
import io.ktor.http.HttpMethod.Companion.Delete
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Head
import io.ktor.http.HttpMethod.Companion.Options
import io.ktor.http.HttpMethod.Companion.Patch
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpMethod.Companion.Put
import io.ktor.routing.*
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class KtorHttpMethodTest {

    @Nested
    inner class SimplePathTests {

        @Test
        fun `path defined by http method functions`() {
            //given
            val routing = Routing(mockk()).apply {
                get("/todos") { }
                post("/todos") { }
                put("/todos") { }
                patch("/todos") { }
                delete("/todos") { }
                head("/todos") { }
                options("/todos") { }
            }

            val specification = setOf(
                    Endpoint("/todos", GET),
                    Endpoint("/todos", POST),
                    Endpoint("/todos", PUT),
                    Endpoint("/todos", PATCH),
                    Endpoint("/todos", DELETE),
                    Endpoint("/todos", HEAD),
                    Endpoint("/todos", OPTIONS)
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `http method function within route function containing path`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todos") {
                    get { }
                    post { }
                    put { }
                    patch { }
                    delete { }
                    head { }
                    options { }
                }
            }

            val specification = setOf(
                    Endpoint("/todos", GET),
                    Endpoint("/todos", POST),
                    Endpoint("/todos", PUT),
                    Endpoint("/todos", PATCH),
                    Endpoint("/todos", DELETE),
                    Endpoint("/todos", HEAD),
                    Endpoint("/todos", OPTIONS)
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `method function within route function containing path`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todos") {
                    method(Get) { }
                    method(Post) { }
                    method(Put) { }
                    method(Patch) { }
                    method(Delete) { }
                    method(Head) { }
                    method(Options) { }
                }
            }

            val specification = setOf(
                    Endpoint("/todos", GET),
                    Endpoint("/todos", POST),
                    Endpoint("/todos", PUT),
                    Endpoint("/todos", PATCH),
                    Endpoint("/todos", DELETE),
                    Endpoint("/todos", HEAD),
                    Endpoint("/todos", OPTIONS)
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
                route("/todos", Get) { }
                route("/todos", Post) { }
                route("/todos", Put) { }
                route("/todos", Patch) { }
                route("/todos", Delete) { }
                route("/todos", Head) { }
                route("/todos", Options) { }
            }

            val specification = setOf(
                    Endpoint("/todos", GET),
                    Endpoint("/todos", POST),
                    Endpoint("/todos", PUT),
                    Endpoint("/todos", PATCH),
                    Endpoint("/todos", DELETE),
                    Endpoint("/todos", HEAD),
                    Endpoint("/todos", OPTIONS)
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
                    route("/todos") { }
                }
                method(Post) {
                    route("/todos") { }
                }
                method(Put) {
                    route("/todos") { }
                }
                method(Patch) {
                    route("/todos") { }
                }
                method(Delete) {
                    route("/todos") { }
                }
                method(Head) {
                    route("/todos") { }
                }
                method(Options) {
                    route("/todos") { }
                }
            }

            val specification = setOf(
                    Endpoint("/todos", GET),
                    Endpoint("/todos", POST),
                    Endpoint("/todos", PUT),
                    Endpoint("/todos", PATCH),
                    Endpoint("/todos", DELETE),
                    Endpoint("/todos", HEAD),
                    Endpoint("/todos", OPTIONS)
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }
    }

    @Nested
    inner class NestedPathSameDeclarationLevelTests {

        @Test
        fun `path defined by http method functions`() {
            //given
            val routing = Routing(mockk()).apply {
                get("/todo/list") { }
                post("/todo/list") { }
                put("/todo/list") { }
                patch("/todo/list") { }
                delete("/todo/list") { }
                head("/todo/list") { }
                options("/todo/list") { }
            }

            val specification = setOf(
                    Endpoint("/todo/list", GET),
                    Endpoint("/todo/list", POST),
                    Endpoint("/todo/list", PUT),
                    Endpoint("/todo/list", PATCH),
                    Endpoint("/todo/list", DELETE),
                    Endpoint("/todo/list", HEAD),
                    Endpoint("/todo/list", OPTIONS)
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `http method function within route function containing path`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todo/list") {
                    get { }
                    post { }
                    put { }
                    patch { }
                    delete { }
                    head { }
                    options { }
                }
            }

            val specification = setOf(
                    Endpoint("/todo/list", GET),
                    Endpoint("/todo/list", POST),
                    Endpoint("/todo/list", PUT),
                    Endpoint("/todo/list", PATCH),
                    Endpoint("/todo/list", DELETE),
                    Endpoint("/todo/list", HEAD),
                    Endpoint("/todo/list", OPTIONS)
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `method function within route function containing path`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todo/list") {
                    method(Get) { }
                    method(Post) { }
                    method(Put) { }
                    method(Patch) { }
                    method(Delete) { }
                    method(Head) { }
                    method(Options) { }
                }
            }

            val specification = setOf(
                    Endpoint("/todo/list", GET),
                    Endpoint("/todo/list", POST),
                    Endpoint("/todo/list", PUT),
                    Endpoint("/todo/list", PATCH),
                    Endpoint("/todo/list", DELETE),
                    Endpoint("/todo/list", HEAD),
                    Endpoint("/todo/list", OPTIONS)
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
                route("/todo/list", Get) { }
                route("/todo/list", Post) { }
                route("/todo/list", Put) { }
                route("/todo/list", Patch) { }
                route("/todo/list", Delete) { }
                route("/todo/list", Head) { }
                route("/todo/list", Options) { }
            }

            val specification = setOf(
                    Endpoint("/todo/list", GET),
                    Endpoint("/todo/list", POST),
                    Endpoint("/todo/list", PUT),
                    Endpoint("/todo/list", PATCH),
                    Endpoint("/todo/list", DELETE),
                    Endpoint("/todo/list", HEAD),
                    Endpoint("/todo/list", OPTIONS)
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
        fun `path defined by nested http method functions`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todo") {
                    get("/list") { }
                    post("/list") { }
                    put("/list") { }
                    patch("/list") { }
                    delete("/list") { }
                    head("/list") { }
                    options("/list") { }
                }
            }

            val specification = setOf(
                    Endpoint("/todo/list", GET),
                    Endpoint("/todo/list", POST),
                    Endpoint("/todo/list", PUT),
                    Endpoint("/todo/list", PATCH),
                    Endpoint("/todo/list", DELETE),
                    Endpoint("/todo/list", HEAD),
                    Endpoint("/todo/list", OPTIONS)
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `http method function within nested route function containing path`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todo") {
                    route("/list") {
                        get { }
                        post { }
                        put { }
                        patch { }
                        delete { }
                        head { }
                        options { }
                    }
                }
            }

            val specification = setOf(
                    Endpoint("/todo/list", GET),
                    Endpoint("/todo/list", POST),
                    Endpoint("/todo/list", PUT),
                    Endpoint("/todo/list", PATCH),
                    Endpoint("/todo/list", DELETE),
                    Endpoint("/todo/list", HEAD),
                    Endpoint("/todo/list", OPTIONS)
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `method function within nested route function containing path`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todo") {
                    route("/list") {
                        method(Get) { }
                        method(Post) { }
                        method(Put) { }
                        method(Patch) { }
                        method(Delete) { }
                        method(Head) { }
                        method(Options) { }
                    }
                }
            }

            val specification = setOf(
                    Endpoint("/todo/list", GET),
                    Endpoint("/todo/list", POST),
                    Endpoint("/todo/list", PUT),
                    Endpoint("/todo/list", PATCH),
                    Endpoint("/todo/list", DELETE),
                    Endpoint("/todo/list", HEAD),
                    Endpoint("/todo/list", OPTIONS)
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }

        @Test
        fun `extract GET and POST declared in method function within nested route declarations`() {
            //given
            val routing = Routing(mockk()).apply {
                route("/todo") {
                    method(Post, {})
                    route("/list") {
                        method(Get, {})
                    }
                }
            }

            val specification = setOf(
                    Endpoint("/todo/list", GET),
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
                    route("/list", Get) { }
                    route("/list", Post) { }
                    route("/list", Put) { }
                    route("/list", Patch) { }
                    route("/list", Delete) { }
                    route("/list", Head) { }
                    route("/list", Options) { }
                }
            }

            val specification = setOf(
                    Endpoint("/todo/list", GET),
                    Endpoint("/todo/list", POST),
                    Endpoint("/todo/list", PUT),
                    Endpoint("/todo/list", PATCH),
                    Endpoint("/todo/list", DELETE),
                    Endpoint("/todo/list", HEAD),
                    Endpoint("/todo/list", OPTIONS)
            )

            //when
            val implementation = KtorConverter(routing).conversionResult

            //then
            assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
        }
    }
}