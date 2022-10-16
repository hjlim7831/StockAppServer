package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("StockAppServer")
				.description("Stock App API ����")
//				.termsOfServiceUrl("")
//				.contact(new Contact(name,url,email))
				.build();
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(this.apiInfo())
			.useDefaultResponseMessages(false) // ���ʿ��� �����ڵ�, ���� ����
//			.groupName("groupName1") // api �������� ������ ���� ���, group �̸� �����ϱ�
			.select()
			.apis(RequestHandlerSelectors.any()) // ���� RequestMapping���� �Ҵ�� ��� URL ����Ʈ�� ����
//			.paths(PathSelectors.ant("/api/**")) // �� �� /api/**�� URL�鸸 ���͸�
			.build();
	}
}