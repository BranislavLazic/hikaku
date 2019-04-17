package de.codecentric.hikaku.converters.openapi

import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class OpenApiConverterResponsesTest {

    @Test
    fun `200 status code`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("responses/responses_single_200.yaml").toURI())
        val implementation = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = HttpMethod.GET,
                        produces = setOf("application/json"),
                        responses = setOf("200")
                )
        )

        //when
        val specification = OpenApiConverter(file)

        //then
        Assertions.assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }

    @Test
    fun `multiple status codes`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("responses/responses_multiple.yaml").toURI())
        val implementation = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = HttpMethod.GET,
                        produces = setOf("application/json"),
                        responses = setOf("200")
                )
        )

        //when
        val specification = OpenApiConverter(file)

        //then
        Assertions.assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }

    @Test
    fun `default - no data`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("responses/responses_no_data.yaml").toURI())
        val implementation = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = HttpMethod.GET,
                        produces = setOf("text/plain"),
                        responses = setOf("default")
                )
        )

        //when
        val specification = OpenApiConverter(file)

        //then
        Assertions.assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }
}