package io.gitlab.thuri.spring.htmlunit;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

  @RequestMapping("/test/{parameter}")
  public ResponseEntity<?> test(@PathVariable String parameter) {
    return ResponseEntity.ok(parameter);
  }
}
