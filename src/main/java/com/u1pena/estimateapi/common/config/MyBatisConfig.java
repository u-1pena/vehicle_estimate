package com.u1pena.estimateapi.common.config;

import com.u1pena.estimateapi.common.enums.CarWashSize;
import com.u1pena.estimateapi.common.enums.PlateRegion;
import com.u1pena.estimateapi.common.enums.Prefecture;
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
      configuration.getTypeHandlerRegistry()
          .register(CarWashSize.class, EnumTypeHandler.class);
    };
  }
}
