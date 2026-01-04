package ynu.edu.smart_energy.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j（OpenAPI3）配置类
 * Spring Boot 3.x + Knife4j 4.5.0 兼容
 * 分组完全对应当前项目 Controller
 */
@Configuration
public class Knife4jConfig {

    /** JWT 认证方案名称 */
    public static final String SECURITY_SCHEME_NAME = "JWT";

    /**
     * OpenAPI 基本信息 + JWT 配置
     */
    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Smart Energy 智慧能源管理系统 API 文档")
                        .description("""
                                期末项目接口文档（Spring Boot 3 + JPA + MySQL + JWT + Knife4j）
                                
                                权限说明：
                                - ADMIN：可访问所有接口（增删改查）
                                - USER：仅能访问 GET 查询接口
                                
                                JWT 使用：
                                1）POST /api/auth/login 获取 token
                                2）点击右上角 Authorize 输入：Bearer <token>
                                """)
                        .version("1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                )

                // 所有接口默认带 JWT 认证
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                // JWT SecurityScheme
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .description("JWT Token，格式：Bearer <token>")
                ));
    }

    // ==========================================================
    // 分组
    // ==========================================================

    /** 总览 */
    @Bean
    public GroupedOpenApi apiAll() {
        return GroupedOpenApi.builder()
                .group("00-all")
                .pathsToMatch("/**")
                .build();
    }


    /** 登录认证 */
    @Bean
    public GroupedOpenApi apiAuth() {
        return GroupedOpenApi.builder()
                .group("01-auth")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    /** 建筑管理 */
    @Bean
    public GroupedOpenApi apiBuilding() {
        return GroupedOpenApi.builder()
                .group("02-building")
                .pathsToMatch("/api/buildings/**")
                .build();
    }

    /** 设备管理 */
    @Bean
    public GroupedOpenApi apiDevice() {
        return GroupedOpenApi.builder()
                .group("03-device")
                .pathsToMatch("/api/devices/**")
                .build();
    }

    /** 能耗数据 */
    @Bean
    public GroupedOpenApi apiEnergy() {
        return GroupedOpenApi.builder()
                .group("04-energy")
                .pathsToMatch("/api/energy/**")
                .build();
    }

    /** 告警管理 */
    @Bean
    public GroupedOpenApi apiAlarm() {
        return GroupedOpenApi.builder()
                .group("05-alarm")
                .pathsToMatch("/api/alarms/**")
                .build();
    }

    /** 统计分析 */
    @Bean
    public GroupedOpenApi apiStats() {
        return GroupedOpenApi.builder()
                .group("06-stats")
                .pathsToMatch("/api/stats/**")
                .build();
    }
}
