package wander.wise.application.service.api;

import org.junit.jupiter.api.Test;

public class StorageServiceImplTest {

    // Tests for uploadFile

    @Test
    public void uploadFile_ValidMultipartFile_ReturnsCorrectUrl() {
        // Test when the MultipartFile is valid and the upload is successful
    }

    @Test
    public void uploadFile_EmptyMultipartFile_ThrowsException() {
        // Test when the MultipartFile is empty
    }

    @Test
    public void uploadFile_NullMultipartFile_ThrowsException() {
        // Test when the MultipartFile is null
    }

    @Test
    public void uploadFile_S3ClientThrowsSdkClientException_ThrowsStorageServiceException() {
        // Test when the S3 client throws an SdkClientException during upload
    }

    @Test
    public void uploadFile_FileConversionFailure_ThrowsException() {
        // Test when the MultipartFile cannot be converted to File
    }

    // Tests for deleteFile

    @Test
    public void deleteFile_ValidFileName_ReturnsFileNameRemoved() {
        // Test when the file name is valid and the deletion is successful
    }

    @Test
    public void deleteFile_EmptyFileName_ThrowsException() {
        // Test when the file name is empty
    }

    @Test
    public void deleteFile_NullFileName_ThrowsException() {
        // Test when the file name is null
    }

    @Test
    public void deleteFile_S3ClientThrowsSdkClientException_ThrowsStorageServiceException() {
        // Test when the S3 client throws an SdkClientException during deletion
    }

    @Test
    public void deleteFile_FileNotFound_ThrowsException() {
        // Test when the file does not exist in the S3 bucket
    }
}
