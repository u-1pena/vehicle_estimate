package example.maintenance.estimate.customer.helper;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;

/**
 * MockMvc のカスタマイズクラス 日本語の文字化けを防ぐため、レスポンスに UTF-8 を設定
 */
@Component
public class CustomizedMockMvc implements MockMvcBuilderCustomizer {

  @Override
  public void customize(ConfigurableMockMvcBuilder<?> builder) {
    builder.alwaysDo(result -> result.getResponse().setCharacterEncoding("UTF-8"));
  }
}

