package org.clever.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2017/6/12 9:28 <br/>
 */
@Configuration
@EnableSwagger2
public class SecuritySwagger2Config {
    @Bean
    public Docket createSecurityEmbedApi() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("clever-security-embed")
                // .description("description")
                // .termsOfServiceUrl("termsOfServiceUrl")
                .version("0.0.1-SNAPSHOT")
                // .license("license")
                // .licenseUrl("licenseUrl")
                // .termsOfServiceUrl("termsOfServiceUrl")
                // .contact(contact)
                // .extensions()
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .groupName("clever-security-jwt")
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.clever.security.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
