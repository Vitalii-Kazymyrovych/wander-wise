package wander.wise.application.service.api.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wander.wise.application.exception.custom.StorageServiceException;

@Service
public class StorageServiceImpl implements StorageService {
    public static final String BASE_URL = "https://ww-images.s3.us-east-1.amazonaws.com/";
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;

    @Override
    public String uploadFile(MultipartFile file) {
        File fileObj = multipartFileToFile(file);
        String filename = System.currentTimeMillis() + "_"
                + file.getOriginalFilename();
        try {
            s3Client.putObject(new PutObjectRequest(bucketName, filename, fileObj));
            fileObj.delete();
            return BASE_URL + filename;
        } catch (SdkClientException e) {
            throw new StorageServiceException(
                    "An error occurred when saving file to the cloud storage", e);
        }
    }

    @Override
    public String deleteFile(String fileName) {
        try {
            s3Client.deleteObject(bucketName, fileName);
            return fileName + " removed";
        } catch (SdkClientException e) {
            throw new StorageServiceException(
                    "An error occurred when deleting file from the cloud storage", e);
        }
    }

    private File multipartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new StorageServiceException("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
