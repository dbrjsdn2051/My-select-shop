package org.example.myselectshop.repository;

import org.example.myselectshop.entity.Folder;
import org.example.myselectshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUserAndNameIn(User user, List<String> folderNames);


    List<Folder> findAllByUser(User user);
}
