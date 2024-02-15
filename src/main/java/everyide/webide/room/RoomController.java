package everyide.webide.room;

import everyide.webide.room.domain.CreateRoomRequestDto;
import everyide.webide.room.domain.Room;
import everyide.webide.room.domain.dto.RoomFixDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/api/community")
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomRequestDto requestDto) {
        Room room = roomService.create(requestDto);
        return ResponseEntity.ok(room.getId());
    }
    // 1. 방 생성 후에는 그 방에 입장해야 맞다고 생각함. 방장의 방 생성 = 입장/ 방장의 퇴장 = 방 폭파
    // 1-1. 아무도 없는 유령방을 방지하기 위해 인원수 이슈를 생각해서 방 폭파 결정 (방나가기할때 현재 인원이 0 이면 방 비활성화함)
    // 2. 때문에 프론트에서 입장 할 수 있도록 방의 id를 return
    // 3. 방 생성시 현재인원 / 최대인원 추가 (생성당시 현재인원 = 1 / 최대인원 = n)

    @GetMapping("/api/communities")
    public ResponseEntity<?> loadAllRooms() {
        return ResponseEntity.ok().body(roomService.loadAllRooms());
    }

    @PatchMapping("/api/community/{roomId}/settings")
    public void updateRoom(@PathVariable("roomId") String roomId, @RequestBody RoomFixDto roomfixDto) {
        roomService.fixRoom(roomId, roomfixDto);
    }
    // 방 수정이 가능한것은? name, password, isLocked 정도만 수정하기

    @GetMapping("/api/community/{roomId}")
    public ResponseEntity<?> enterRoom(@PathVariable("roomId") String roomId) {
        Room room = roomService.enteredRoom(roomId);
        return ResponseEntity.ok(room);
    }
    // 방에 들어갔을때 어떤 것들을 띄워야하는지 상의하기 일단 방만 띄움

    @GetMapping("/api/community/{roomId}/exit")
    public void communityOut(@PathVariable("roomId") String roomId) {
        roomService.exitRoom(roomId);
    }
}
