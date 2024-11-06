package ru.raggies.notification.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

@Component
class TestWebClient(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    fun <T> requestList(
        clazz: Class<T>,
        prepareRequest: (MockMvc) -> ResultActions
    ): List<T> = request(
        ParameterizedTypeReference.forType(
            objectMapper.typeFactory.constructCollectionLikeType(ArrayList::class.java, clazz),
        ),
        prepareRequest
    )

    fun <T> requestOne(
        clazz: Class<T>,
        prepareRequest: (MockMvc) -> ResultActions
    ): T = request(ParameterizedTypeReference.forType(clazz), prepareRequest)

    private fun <T> request(
        bodyTypeReference: ParameterizedTypeReference<T>,
        prepareRequest: (MockMvc) -> ResultActions
    ): T {
        val response = prepareRequest.invoke(mockMvc)
            .andReturn()
            .response
            .contentAsString

        val javaType = objectMapper.typeFactory.constructType(bodyTypeReference.type)
        return objectMapper.readValue(response, javaType)
    }

}