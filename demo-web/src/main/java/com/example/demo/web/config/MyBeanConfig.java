package com.example.demo.web.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Import(MyBeanConfig.class) // TODO
@Slf4j
public class MyBeanConfig implements ImportBeanDefinitionRegistrar {

	// memo: このメソッドは @Import等でインポートされないと呼ばれない
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		log.info("★registerBeanDefinitions");
		registry.registerBeanDefinition("myBean1", BeanDefinitionBuilder.genericBeanDefinition(MyBeanFactory.class) //
				.addConstructorArgValue("msg1") //
				.getBeanDefinition());
		registry.registerBeanDefinition("myBean2", BeanDefinitionBuilder.genericBeanDefinition(MyBeanFactory.class) //
				.addConstructorArgValue("msg2") //
				.getBeanDefinition());
		registry.registerBeanDefinition("myBean3", BeanDefinitionBuilder.genericBeanDefinition(MyBeanFactory.class) //
				.addConstructorArgValue("msg3") //
				.getBeanDefinition());
	}

}
