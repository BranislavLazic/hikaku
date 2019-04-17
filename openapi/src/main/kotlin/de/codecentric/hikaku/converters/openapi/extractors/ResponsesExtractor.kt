package de.codecentric.hikaku.converters.openapi.extractors

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation

internal class ResponsesExtractor(private val openApi: OpenAPI) {

    operator fun invoke(operation: Operation?): Set<String> {
        return operation?.responses
                ?.keys
                .orEmpty()
    }
}