package everyide.webide.room;

import everyide.webide.room.domain.CreateRoomRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/api/createRoom")
    public void createRoom(@RequestBody CreateRoomRequestDto requestDto) {
        roomService.create(requestDto);
    }

    @GetMapping("/api/loadRooms")
    public ResponseEntity<?> loadRooms() {
        return ResponseEntity.ok().body(roomService.loadRooms());
    }
}
