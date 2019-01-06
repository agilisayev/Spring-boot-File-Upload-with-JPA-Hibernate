package com.filezilla.repository;

import com.filezilla.domain.ImageStorage;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ImageStorageRepository extends CrudRepository<ImageStorage,Long> {

     List<ImageStorage> findAll();
 }
