package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.entity.LabDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabDocumentRepository extends JpaRepository<LabDocument, Long> {

    List<LabDocument> findByLab_IdAndArchivedFalse(Long labId);
}
