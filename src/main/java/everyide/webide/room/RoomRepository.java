package everyide.webide.room;

import everyide.webide.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findAllBy();
    List<Room> findAllByAvailableTrue();
}
