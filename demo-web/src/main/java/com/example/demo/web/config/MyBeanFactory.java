package com.example.demo.web.config;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyBeanFactory extends AbstractFactoryBean<MyBean> {
	private final String msg;

	@Override
	public Class<?> getObjectType() {
		return MyBean.class;
	}

	@Override
	protected MyBean createInstance() throws Exception {
		var b = new MyBean();
		b.setMsg(msg);
		return b;
	}
}
