package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.File;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByPath(String path);

    @Modifying
    @Transactional
    @Query("DELETE FROM File f WHERE f.path LIKE CONCAT(:path, '/%') OR f.path = :path")
    void deleteByPathAndSubPaths(@Param("path") String path);
}
