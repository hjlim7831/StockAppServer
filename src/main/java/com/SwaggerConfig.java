package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Stock App API Docs ")
				.description("Stock App API 설명")
//				.termsOfServiceUrl("")
//				.contact(new Contact(name,url,email))
				.build();
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(this.apiInfo())
			.useDefaultResponseMessages(false) // 불필요한 응답코드, 설명 제거
//			.groupName("groupName1") // api 페이지를 여러개 만들 경우, group 이름 지정하기
			.select()
			.apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
//			.paths(PathSelectors.ant("/api/**")) // 그 중 /api/**인 URL들만 필터링
			.build();
	}
}

