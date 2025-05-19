package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.entity.Laboratory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {

    @Query("""
    SELECT new com.UAIC.ISMA.dto.LaboratoryDTO(
        l.id, l.labName, l.description, l.location
    )
    FROM Laboratory l
    WHERE (:name IS NULL OR LOWER(l.labName) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:location IS NULL OR LOWER(l.location) LIKE LOWER(CONCAT('%', :location, '%')))
""")
    Page<LaboratoryDTO> searchLaboratoryByNameAndLocation(
            @Param("name") String name,
            @Param("location") String location,
            Pageable pageable
    );
}
