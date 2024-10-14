package json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

  static class SimpleMessage {

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


  public void testJsonMessage(ObjectMapper objectMapper) throws IOException {


    var json = "{\"businessId\":1336504106360835,\"tenantId\":\"first\",\"success\":false}";
    var value = objectMapper.readValue(json, SimpleMessage.class);

    System.out.println("message ser by readValue : " + value.tenantId);

    assertEquals(value.tenantId, "first");

    Class<?> view = SimpleMessage.class;
    JavaType javaType = objectMapper.constructType(new  TypeReference<SimpleMessage>() {});

    //this is Spring message Usage
    var value2 = (SimpleMessage)objectMapper.readerWithView(view).forType(javaType).readValue(json);

    System.out.println("message ser by readerWithView: " + value2.tenantId);

    assertEquals(value2.tenantId, "first");
  }

  @Test
  public void testFails() throws IOException {
    ObjectMapper objectMapper = JsonMapper.builder().configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false).build();

    testJsonMessage(objectMapper);
  }

  @Test
  public void testSuccess() throws IOException {
    testJsonMessage(new ObjectMapper());
  }

}
