package br.edu.fatecpg.loja;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class ImageConfig implements WebMvcConfigurer {
    @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry .addResourceHandler("/imagens/**") .addResourceLocations("file:uploads/");
    }
}
