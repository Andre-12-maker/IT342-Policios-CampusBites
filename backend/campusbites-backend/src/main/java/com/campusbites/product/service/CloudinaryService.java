package com.campusbites.product.service;

import com.campusbites.common.exception.AppException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "cloudinary.enabled", havingValue = "true")
public class CloudinaryService {

    private final Cloudinary cloudinary;
    public CloudinaryService(Cloudinary cloudinary) { this.cloudinary = cloudinary; }

    @SuppressWarnings("unchecked")
    public String uploadProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) throw AppException.badRequest("Image file is required");
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) throw AppException.badRequest("File must be an image");
        if (file.getSize() > 5 * 1024 * 1024) throw AppException.badRequest("Image must be under 5MB");
        try {
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "campusbites/products",
                            "transformation", "w_800,h_600,c_fill,q_auto,f_auto",
                            "allowed_formats", new String[]{"jpg","jpeg","png","webp"}));
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw AppException.badRequest("Upload failed: " + e.getMessage());
        }
    }
}