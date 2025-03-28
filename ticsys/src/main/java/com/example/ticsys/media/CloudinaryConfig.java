package com.example.ticsys.media;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary()
    {
        return new Cloudinary(dotenv().get("CLOUDINARY_URL"));
    }
    @Bean
    public Dotenv dotenv()
    {
        return Dotenv.load();
    }
}
