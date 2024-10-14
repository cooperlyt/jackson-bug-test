import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.BeanDeserializer
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.cooperlyt.Application
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class])
class JsonTest {



    data class SimpleStockFreezeStatusMessage(
         val businessId: Long,
         val success: Boolean,
         val tenantId: String,
    )


    private fun testJsonMessage(objectMapper: ObjectMapper) {

        val json = """{"businessId":1336504106360835,"tenantId":"first","success":false}"""
        val value = objectMapper.readValue(json, SimpleStockFreezeStatusMessage::class.java)

        println("message ser by readValue : $value")
        assert(value.tenantId == "first")

        val view: Class<*> = SimpleStockFreezeStatusMessage::class.java
        val javaType: JavaType = objectMapper.constructType(object : TypeReference<SimpleStockFreezeStatusMessage>() {})


        //this is Spring message Usage
        val value2 = objectMapper.readerWithView(view).forType(javaType).readValues<Any>(json).nextValue()
        println("message ser by readerWithView: $value2")

        assertEquals((value2 as SimpleStockFreezeStatusMessage).tenantId, "first")
    }

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun testSpringMapper() {
        testJsonMessage(objectMapper)
    }

    @Test
    fun testDefaultMapper() {
        testJsonMessage(ObjectMapper().registerKotlinModule())
    }
}