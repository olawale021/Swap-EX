package com.example.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

public class CloudinaryConfig {

    private static final Cloudinary cloudinary;

    static {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();

        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", dotenv.get("CLOUDINARY_CLOUD_NAME"),
                "api_key", dotenv.get("CLOUDINARY_API_KEY"),
                "api_secret", dotenv.get("CLOUDINARY_API_SECRET")
        ));
    }

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }
}