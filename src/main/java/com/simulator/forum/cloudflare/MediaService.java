package com.simulator.forum.cloudflare;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class MediaService {
	
	@Autowired
	private S3Client s3client;
	
	@Value("${cloudflare.r2.bucket}")
    private String bucket;
	
	@Value("${cloudflare.r2.public}")
	public String publicUrl;
	
	
	
	
	
	public String getPublicUrl() {
		return publicUrl;
	}

	private String getFileExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length()-1) {
            throw new IllegalArgumentException("Invalid file extension in filename: " + filename);
        }
        return filename.substring(idx+1);
    }

	public String uploadFile(MultipartFile file , String folder) 
	{
				
		String extension = getFileExtension(file.getOriginalFilename());
		
		StringBuilder sb = new StringBuilder(publicUrl);
		
		if("image".equals(folder)) 
		{
			boolean isImage = switch (extension) {
		    case "jpg", "jpeg", "png", "gif" -> true;
		    default -> false;};
		    
		    if(!isImage) 
		    {
		    	throw new IllegalArgumentException("Invalid file extension");
		    }
		};

		
		if("doc".equals(folder)) 
		{
			boolean isDoc = switch (extension) {
		    case "xml", "XML" -> true;
		    default -> false;};
		    
		    if(!isDoc) 
		    {
		    	throw new IllegalArgumentException("Invalid file extension");
		    }
		};
				
		
		
		String contentType = file.getContentType();
		
		String key = String.format("%s/%s.%s", folder, UUID.randomUUID(), extension);
		
		PutObjectRequest request = PutObjectRequest.builder()
	            .bucket(bucket)
	            .key(key)
	            .contentType(contentType)
	            .build();
		
		
		try {
			
			
			
			
			s3client.putObject(request, RequestBody.fromBytes(file.getBytes()));
			
		} catch (AwsServiceException | SdkClientException | IOException e) {
			
			e.printStackTrace();
		}
		
		
		return  sb.append("/").append(request.key()).toString();
	}
	
	
	
}
