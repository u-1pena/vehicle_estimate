package yuichi.car.estimate.management.mapper;

import org.apache.ibatis.type.EnumTypeHandler;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import yuichi.car.estimate.management.entity.enums.PlateRegion;
import yuichi.car.estimate.management.entity.enums.Prefecture;

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
