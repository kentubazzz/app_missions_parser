package com.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI missionArchiveOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Архив миссий Токийского магического колледжа")
                        .description("""
                                API для управления архивом миссий.
                                
                                **Возможности:**
                                * Загрузка миссий в форматах JSON, XML, TXT, YAML, Event (pipe-разделитель)
                                * Просмотр архива миссий
                                * Генерация отчетов (простой, детализированный, анализ рисков)
                                * Управление миссиями (CRUD операции)
                                
                                **Форматы входных данных:**
                                * JSON, XML, TXT, YAML, Event
                                
                                **Типы отчетов:**
                                * SIMPLE - краткая информация
                                * DETAILED - детализированный отчет
                                * RISK - анализ рисков
                                """))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Локальный сервер")
                ));
    }
}
