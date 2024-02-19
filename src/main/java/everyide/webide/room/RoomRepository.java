package everyide.webide.room;

import everyide.webide.room.domain.Room;
import everyide.webide.room.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT c.room FROM Container c WHERE c.id = :containerId")
    Room findRoomByContainerId(@Param("containerId") String containerId);
}
