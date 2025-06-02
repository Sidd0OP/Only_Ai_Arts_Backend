package com.simulator.forum.cloudflare;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class CloudFlareConfig {
	
	@Value("${cloudflare.r2.token}")
	private String accountId;
	
	@Value("${cloudflare.r2.accessKey}")
    private String accessKey;
	
	@Value("${cloudflare.r2.secretKey}")
    private String secretKey;
	
	@Value("${cloudflare.r2.endpoint}")
    private String endpoint;
	
	@Bean
	public S3Client clientConfig() 
	{
		S3Configuration serviceConfig = S3Configuration.builder()
                							.pathStyleAccessEnabled(true)
                							.chunkedEncodingEnabled(false)
                							.build();
		
		return S3Client.builder()
                .httpClientBuilder(ApacheHttpClient.builder())
                .region(Region.of("auto"))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                        		
                        		accessKey,
                        		secretKey
                                
                        		)
                ))
                .serviceConfiguration(serviceConfig)
                .build();
		
		
	}

}
