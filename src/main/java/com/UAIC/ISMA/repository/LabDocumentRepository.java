package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.dao.LabDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabDocumentRepository extends JpaRepository<LabDocument, Long> {
}
