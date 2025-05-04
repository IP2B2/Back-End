package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.dao.RequestDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestDocumentRepository extends JpaRepository<RequestDocument, Long> {
}
