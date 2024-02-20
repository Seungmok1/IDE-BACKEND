package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.Directory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    Optional<Directory> findByPath(String path);

    @Modifying
    @Transactional
    @Query("DELETE FROM Directory d WHERE d.path LIKE CONCAT(:path, '/%') OR d.path = :path")
    void deleteByPathAndSubPaths(@Param("path") String path);
}
