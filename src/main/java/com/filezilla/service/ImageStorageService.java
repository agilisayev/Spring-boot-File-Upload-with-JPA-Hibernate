package com.filezilla.service;

import com.filezilla.domain.ImageStorage;
import com.filezilla.repository.ImageStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ImageStorageService  {

   private final ImageStorageRepository imageStorageRepository;

    @Autowired
    public ImageStorageService(ImageStorageRepository imageStorageRepository) {
        this.imageStorageRepository = imageStorageRepository;
    }

    public ImageStorage findById(Long id){
        return  imageStorageRepository.findById(id).orElse(null);
    }

    public ImageStorage saveOrUpdate(ImageStorage imageStorage){
        return  imageStorageRepository.save(imageStorage);
    }

    public List<ImageStorage> findAll(){
      return   imageStorageRepository.findAll();
    }

    public void delete(Long id){
        imageStorageRepository.deleteById(id);
    }

}
