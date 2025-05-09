package example.maintenance.estimate.customer.mapper;

import example.maintenance.estimate.customer.entity.enums.PlateRegion;
import example.maintenance.estimate.customer.entity.enums.Prefecture;
import org.apache.ibatis.type.EnumTypeHandler;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

public class MyBatisConfig {

  @Bean // このメソッドがSpring Beanとして登録される
  public ConfigurationCustomizer configurationCustomizer() {
    return configuration -> {
      // TypeHandlerRegistryに EnumTypeHandler を登録する
      configuration.getTypeHandlerRegistry()
          .register(PlateRegion.class, EnumTypeHandler.class);
      configuration.getTypeHandlerRegistry()
          .register(Prefecture.class, EnumTypeHandler.class);
    };
  }
}
