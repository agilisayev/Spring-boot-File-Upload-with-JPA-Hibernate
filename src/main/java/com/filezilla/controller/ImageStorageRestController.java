package com.filezilla.controller;

import com.filezilla.domain.ImageStorage;
import com.filezilla.service.FileService;
import com.filezilla.service.ImageStorageService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/fileZilla")
public class ImageStorageRestController {
    private final ImageStorageService imageStorageService;
    private final FileService fileService;

    @Autowired
    public ImageStorageRestController(ImageStorageService imageStorageService, FileService fileService) {
        this.imageStorageService = imageStorageService;
        this.fileService = fileService;
    }

    @Value("${upload.storage.directory}")
    private String FILE_DIRECTORY = ".";

    @GetMapping(value = "/file/{id}")
    public @ResponseBody
    byte[] findByTaskFileId(HttpServletResponse response,
                            @PathVariable("id") Long id) throws IOException {
        ImageStorage imageStorage = imageStorageService.findById(id);
        if (imageStorage != null) {
            File file = fileService.getFile(response, imageStorage.getFilePath(), FILE_DIRECTORY);
            InputStream in = new FileInputStream(file);
            byte[] v = IOUtils.toByteArray(in);
            imageStorage.setViewCount(imageStorage.getViewCount() + 1);
            imageStorageService.saveOrUpdate(imageStorage);
            return v;
        }
        throw new IllegalArgumentException("Image not found!");
    }

    @PostMapping("/model/save")
    public ImageStorage saveModel(@RequestBody ImageStorage imageStorage) {
        return imageStorageService.saveOrUpdate(imageStorage);
    }

    @GetMapping("/model/{id}")
    public ImageStorage getFile(@PathVariable Long id) {
        return imageStorageService.findById(id);
    }


    @GetMapping("/model/findAll")
    public List<ImageStorage> findAll() {
        return imageStorageService.findAll();
    }

 /*
    @PostMapping("/uploadFile")
    public ImageStorage uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new ImageStorage(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
    */

    @DeleteMapping("/model/delete/{id}")
    public void delete(@PathVariable Long id) {
        imageStorageService.delete(id);
    }

    /**
     * Bu apide yalniz file gonderilir.
     * This API send only File
     */
    @Transactional
    @PostMapping("/file/save")
    @ResponseBody
    public ImageStorage saveImage(
            @RequestParam("fileData") MultipartFile fileData) {
        return save(fileData, new ImageStorage());
    }

    /**
     * Bu apide fayl ile yanasi ImageStorage de gonderile biler
     * This API send File and ImageStorage
     */
    @Transactional
    @PostMapping(value = "/saveWithData", consumes = {"multipart/form-data"})
    @ResponseBody
    public ImageStorage saveImage(
            @RequestPart("imageStorage") ImageStorage imageStorage,
            @RequestPart("fileData") MultipartFile fileData) {
        return save(fileData, imageStorage);
    }

    private ImageStorage save(MultipartFile fileData, ImageStorage imageStorage) {
        if (fileData != null && fileData.getOriginalFilename() != null && fileData.getOriginalFilename().length() > 0) {
            try {
                String filePath = fileService.uploadFile(fileData, FILE_DIRECTORY);
                imageStorage.setFilePath(filePath);
                imageStorageService.saveOrUpdate(imageStorage);
                return imageStorage;
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("File is empity");
        }
    }

}