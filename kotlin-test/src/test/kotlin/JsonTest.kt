import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class JsonTest {



    data class SimpleMessage(
        val businessId: Long,
        val tenantId: String,
        val success: Boolean
    )


    private fun testJsonMessage(objectMapper: ObjectMapper) {

        val json = """{"businessId":1336504106360835,"tenantId":"first","success":false}"""
        val value = objectMapper.readValue(json, SimpleMessage::class.java)

        println("message ser by readValue : $value")
        assert(value.tenantId == "first")

        val view: Class<*> = SimpleMessage::class.java
        val javaType: JavaType = objectMapper.constructType(object : TypeReference<SimpleMessage>() {})

        //this is Spring message Usage
        val value2 = objectMapper.readerWithView(view).forType(javaType).readValue<Any>(json)
        println("message ser by readerWithView: $value2")

        assertEquals((value2 as SimpleMessage).tenantId, "first")
    }


    @Test
    fun testReaderWithView() {
        val objectMapper = JsonMapper.builder().configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false).build().registerKotlinModule()

        testJsonMessage(objectMapper)
    }

    @Test
    fun testReaderWithViewDefaultView() {
        val objectMapper = JsonMapper.builder().configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true).build().registerKotlinModule()

        testJsonMessage(objectMapper)
    }

}