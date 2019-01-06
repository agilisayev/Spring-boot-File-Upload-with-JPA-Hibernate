package com.filezilla.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.InvalidMimeTypeException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Service
public class FileService {

    public String upload(String extension, MultipartFile file, String path) throws IOException {
        String filename = UUID.randomUUID().toString();
        if (!file.isEmpty()) {
            File parentDir = new File(path);
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            try {
                File newFile;
                do {
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    newFile = new File(parentDir,
                            filename + "_" + dateFormat.format(new Date(System.currentTimeMillis())) + "." + extension);
                } while (newFile.exists());

                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(newFile))) {
                    FileCopyUtils.copy(file.getInputStream(), stream);
                    stream.close();
                }
                return newFile.getName();
            } catch (IOException e) {
                throw new IOException("File " + filename + " upload failed:" + e.getMessage());
            }
        } else {
            throw new IOException("FILE_SIZE_ZERO");
        }
    }

    public void delete(String fullFileName, String directory) {
        File file = new File(directory + "/" + fullFileName);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String uploadFile(MultipartFile file, String DIRECTORY) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (file.getSize() > 10240000L)
            throw new UnsupportedOperationException("File size exceeds 10MB");
        return upload(extension, file, DIRECTORY);
    }

    public String getFileExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    @Deprecated
    private String getFileExtension(MultipartFile file) {
        if (file.getContentType().equalsIgnoreCase(MediaType.IMAGE_JPEG_VALUE))
            return "jpeg";
        else if (file.getContentType().equalsIgnoreCase(MediaType.IMAGE_PNG_VALUE))
            return "png";
        else if (file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE))
            return "pdf";
        else
            throw new InvalidMimeTypeException(file.getContentType(), "extension not supported");
    }

    public File getFile(HttpServletResponse response, String fileName, String DOCUMENTS_DIRECTORY) throws IOException {
        File file = new File(DOCUMENTS_DIRECTORY + "/" + fileName);
        if (!file.exists()) {
            String errorMessage = "Sorry.The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
        }
        return file;
    }
}
