package com.example.ticsys.media;

import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;
    @Autowired
    public CloudinaryService(Cloudinary cloudinary)
    {
        this.cloudinary = cloudinary;
    }
    public String uploadFile(MultipartFile file)
    {
        try{
            Map<?,?> param = ObjectUtils.asMap(
                "use_filename", false
            );
            Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(), param);
            String filePath = (String) uploadResult.get("url");
            return filePath;
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }
    public String deleteFile(String filePath)
    {
        if(filePath == null || filePath.isEmpty())
        {
            return "invalid file path";
        }

        try{
            Map<?,?> deleteResult = cloudinary.uploader().destroy(filePath, ObjectUtils.emptyMap());
            return (String) deleteResult.get("result");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }


}
