package everyide.webide.room;

import everyide.webide.room.domain.Room;
import everyide.webide.room.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findAllBy();
    List<Room> findAllByAvailableTrue();

    List<Room> findAllByNameContainingAndType(String name, RoomType type);


    List<Room> findAllByType(RoomType type);

    List<Room> findAllByNameContaining(String name);

}
