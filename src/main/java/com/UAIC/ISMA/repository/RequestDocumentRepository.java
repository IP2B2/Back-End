package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.entity.RequestDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDocumentRepository extends JpaRepository<RequestDocument, Long> {
}
