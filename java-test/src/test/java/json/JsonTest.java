package json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cooperlyt.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = Main.class)
public class JsonTest {

  @Autowired
  private  ObjectMapper objectMapper;

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

  @Test
  public void testByDefaultMapper() throws IOException {
    testSpringJsonMessage(new ObjectMapper());
  }


  @Test
  public void testBySpringMapper() throws IOException {
    testSpringJsonMessage(objectMapper);
  }


}
