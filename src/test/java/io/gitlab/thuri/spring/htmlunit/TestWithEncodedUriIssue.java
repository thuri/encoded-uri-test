package io.gitlab.thuri.spring.htmlunit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@WebMvcTest(controllers = MainController.class)
@WithMockUser
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestWithEncodedUriIssue {

  private static final String TEST_URL_UNENCODED = "http://localhost/test/Frühling Sommer Herbst Winter";
  private static final String TEST_URL_ENCODED = "http://localhost/test/Fr%C3%BChling%20Sommer%20Herbst%20Winter";

  private WebClient webClient;

  @Autowired
  private MockMvc mockMvc;
  
  @BeforeEach
  void setup() {
    webClient = MockMvcWebClientBuilder
      .mockMvcSetup(mockMvc)
      .build();
  }
  
  @Test
  @Order(1)
  void webClientTestStringNoEncoding() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
    String url = TEST_URL_UNENCODED;
    System.out.println("Calling webclient with String "+url);
    HtmlPage page = webClient.getPage(url);
    assertNotNull(page);
    System.out.println(" result="+page.asNormalizedText());
  }
  
  @Test
  @Order(2)
  void webClientTestStringWithEncoding() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
    String url = TEST_URL_ENCODED;
    System.out.println("Calling webclient with String "+url);
    HtmlPage page = webClient.getPage(url);
    assertNotNull(page);
    System.out.println(" result="+page.asNormalizedText());
  }
  
  @Test
  @Order(3)
  void webClientTestURLNoEncoding() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
    URL url = new URL(TEST_URL_UNENCODED);
    System.out.println("Calling webclient with URL "+url);
    HtmlPage page = webClient.getPage(url);
    assertNotNull(page);
    System.out.println(" result="+page.asNormalizedText());
  }
  
  @Test
  @Order(4)
  void webClientTestURLWithEncoding() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
    URL url = new URL(TEST_URL_ENCODED);
    System.out.println("Calling webclient with URL "+url);
    HtmlPage page = webClient.getPage(url);
    assertNotNull(page);
    System.out.println(" result="+page.asNormalizedText());
  }
  
  @Test
  @Order(5)
  void mockMvcTestStringNoEncoding() throws Exception {
    String url = TEST_URL_UNENCODED;
    System.out.println("Calling mockMvc with String "+url);
    var result = mockMvc.perform(get(url))
      .andExpect(status().is(200))
      .andReturn();
    System.out.println(" result="+result.getResponse().getContentAsString());
  }
  
  @Test
  @Order(6)
  void mockMvcTestStringWithEncoding() throws Exception {
    String url = TEST_URL_ENCODED;
    System.out.println("Calling mockMvc with String "+url);
    var result = mockMvc.perform(get(url))
      .andExpect(status().is(200))
      .andReturn();
    System.out.println(" result="+result.getResponse().getContentAsString());
  }
  
  @Test
  @Order(7)
  void mockMvcTestURI() throws Exception {
    URI url = URI.create(TEST_URL_ENCODED);
    System.out.println("Calling mockMvc with URI "+url.toString());
    var result = mockMvc.perform(get(url).characterEncoding(StandardCharsets.UTF_8))
      .andExpect(status().is(200))
      .andReturn();
    System.out.println(" result="+result.getResponse().getContentAsString());
  }
}
