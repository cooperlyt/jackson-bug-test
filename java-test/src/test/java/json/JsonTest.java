package json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.github.cooperlyt.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.boot.jackson.JsonMixinModule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = Main.class)
public class JsonTest {



  static class SimpleStockFreezeStatusMessage {

    private Long businessId;
    private Boolean success;
    private String tenantId;

    public Long getBusinessId() {
      return businessId;
    }

    public void setBusinessId(Long businessId) {
      this.businessId = businessId;
    }

    public Boolean getSuccess() {
      return success;
    }

    public void setSuccess(Boolean success) {
      this.success = success;
    }

    public String getTenantId() {
      return tenantId;
    }

    public void setTenantId(String tenantId) {
      this.tenantId = tenantId;
    }
  }

  public void testSpringJsonMessage(ObjectMapper objectMapper) throws IOException {

    System.out.println("Registered modules: " + objectMapper.getRegisteredModuleIds());
    System.out.println("Deserialization features: ");
    for (DeserializationFeature feature : DeserializationFeature.values()) {
      boolean isEnabled = objectMapper.getDeserializationConfig().isEnabled(feature);
      System.out.println(feature + ": " + isEnabled);
    }

    System.out.println("Serialization features: ");
    for (SerializationFeature feature : SerializationFeature.values()) {
      boolean isEnabled = objectMapper.getSerializationConfig().isEnabled(feature);
      System.out.println(feature + ": " + isEnabled);
    }

    var json = "{\"businessId\":1336504106360835,\"tenantId\":\"first\",\"success\":false}";
    var value = objectMapper.readValue(json, SimpleStockFreezeStatusMessage.class);

    System.out.println("message ser by readValue : " + value.tenantId);

    assertEquals(value.tenantId, "first");

    Class<?> view = SimpleStockFreezeStatusMessage.class;
    JavaType javaType = objectMapper.constructType(new  TypeReference<SimpleStockFreezeStatusMessage>() {});

    //this is Spring message Usage
    var value2 = (SimpleStockFreezeStatusMessage)objectMapper.readerWithView(view).forType(javaType).readValue(json);

    System.out.println("message ser by readerWithView: " + value2.tenantId);

    assertEquals(value2.tenantId, "first");
  }


  @Autowired
  private  ObjectMapper objectMapper;

  //Fails
  @Test
  public void testBySpringMapper() throws IOException {
    testSpringJsonMessage(objectMapper);
  }

  @Autowired
  private Jackson2ObjectMapperBuilder objectMapperBuilder;

  //Fails
  @Test
  public void testBySpringMapperBuilder() throws IOException {
    testSpringJsonMessage(objectMapperBuilder.build());
  }

  //Success
  @Test
  public void testByDefaultMapper() throws IOException {
    testSpringJsonMessage(new ObjectMapper());
  }

  //Success
  @Test
  public void testMockSpringMapper() throws IOException {
    var ob = new ObjectMapper();
    ob.registerModule(new Jdk8Module());
    ob.registerModule(new ParameterNamesModule());
    ob.registerModule(new JSR310Module());
    ob.registerModule(new JsonMixinModule());
    ob.registerModule(new JsonComponentModule());

    ob = ob.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    ob = ob.configure(WRITE_DATES_AS_TIMESTAMPS, false);
    ob = ob.configure(WRITE_DURATIONS_AS_TIMESTAMPS, false);
    ob = ob.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);

    testSpringJsonMessage(ob);
  }


}
