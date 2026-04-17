package com.socialmedia.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploads folder (images, videos, files) from project root
        String projectUploads = "file:" + System.getProperty("user.dir") + "/uploads/";
        
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations(projectUploads)
            .setCachePeriod(3600)
            .resourceChain(true);
        
        System.out.println("Static resource configured: /uploads/** -> " + projectUploads);
    }
}
