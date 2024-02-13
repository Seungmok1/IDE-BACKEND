package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
