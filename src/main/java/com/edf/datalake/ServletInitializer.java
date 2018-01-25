package com.edf.datalake;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApiInApplication.class);
	}

	@Bean
	public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
		UndertowEmbeddedServletContainerFactory factory =
				new UndertowEmbeddedServletContainerFactory();

		factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
			@Override
			public void customize(io.undertow.Undertow.Builder builder) {
				builder.addHttpListener(8083, "localhost");
			}
		});

		return factory;
	}

}
