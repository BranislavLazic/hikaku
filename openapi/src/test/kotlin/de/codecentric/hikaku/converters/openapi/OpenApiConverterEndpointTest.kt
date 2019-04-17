package de.codecentric.hikaku.converters.openapi

import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class OpenApiConverterEndpointTest {

    @Test
    fun `extract two different paths`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("endpoints/endpoints_two_different_paths.yaml").toURI())
        val implementation = setOf(
                Endpoint("/todos", GET, responses = setOf("200")),
                Endpoint("/tags", GET, responses = setOf("200"))
        )

        //when
        val specification = OpenApiConverter(file)

        //then
        assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }

    @Test
    fun `extract two paths of which one is nested`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("endpoints/endpoints_two_nested_paths.yaml").toURI())
        val implementation = setOf(
                Endpoint("/todos", GET, responses = setOf("200")),
                Endpoint("/todos/query", GET, responses = setOf("200"))
        )

        //when
        val specification = OpenApiConverter(file)

        //then
        assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }

    @Test
    fun `extract all http methods`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("endpoints/endpoints_all_http_methods.yaml").toURI())
        val implementation = setOf(
                Endpoint("/todos", GET, responses = setOf("200")),
                Endpoint("/todos", POST, responses = setOf("200")),
                Endpoint("/todos", PUT, responses = setOf("200")),
                Endpoint("/todos", PATCH, responses = setOf("200")),
                Endpoint("/todos", DELETE, responses = setOf("200")),
                Endpoint("/todos", HEAD, responses = setOf("200")),
                Endpoint("/todos", OPTIONS, responses = setOf("200")),
                Endpoint("/todos", TRACE, responses = setOf("200"))
        )

        //when
        val specification = OpenApiConverter(file)

        //then
        assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }
}