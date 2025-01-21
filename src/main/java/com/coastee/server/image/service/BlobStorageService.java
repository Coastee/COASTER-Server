package com.coastee.server.image.service;

import com.azure.core.util.BinaryData;
import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.image.domain.DirName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.IO_EXCEPTION;

@Service
@RequiredArgsConstructor
public class BlobStorageService {
    private final BlobContainerClient blobContainerClient;

    @Value("${spring.cloud.azure.storage.blob.account-name}")
    private String account;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String container;

    public String upload(
            final MultipartFile file,
            final DirName imageDir,
            final Long id
    ) {
        String fileName = imageDir + "/" + id;

        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        BlobParallelUploadOptions uploadOptions = getUploadOptions(file);
        blobClient.uploadWithResponse(uploadOptions, null, Context.NONE);

        return String.format("https://%s.blob.core.windows.net/%s/%s/%s", account, container, imageDir, id);
    }

    public void delete(
            final DirName imageDir,
            final Long id
    ) {
        String fileName = imageDir + "/" + id;
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        blobClient.delete();
    }

    private BlobParallelUploadOptions getUploadOptions(final MultipartFile file) {
        try {
            BlobHttpHeaders jsonHeaders = new BlobHttpHeaders()
                    .setContentType(file.getContentType());
            BinaryData data = BinaryData.fromStream(file.getInputStream(), file.getSize());
            return new BlobParallelUploadOptions(data)
                    .setRequestConditions(new BlobRequestConditions())
                    .setHeaders(jsonHeaders);
        } catch (IOException e) {
            throw new GeneralException(IO_EXCEPTION);
        }
    }
}
