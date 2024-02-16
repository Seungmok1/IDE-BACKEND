package everyide.webide.room;

import everyide.webide.config.auth.exception.RoomDestroyException;
import everyide.webide.container.ContainerRepository;
import everyide.webide.container.domain.Container;
import everyide.webide.room.domain.*;
import everyide.webide.room.domain.dto.RoomFixDto;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;

    public Room create(CreateRoomRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // JWT에서 사용자의 이메일 가져오기
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = byEmail.orElseThrow();

        Room room = Room.builder()
                .name(requestDto.getName())
                .isLocked(requestDto.getIsLocked())
                .password(requestDto.getPassword())
                .type(RoomType.valueOf(requestDto.getRoomType()))
                .maxPeople(requestDto.getMaxPeople()) // 최대 입장 허용 인원을 프론트에서 막을지.. 백에서도 막아야되는지..?
                .usersId(new ArrayList<>())
                .fullRoom(false)
                .owner(user)
                .personCnt(1)
                .build();
        Container container = Container.builder()
                .name(room.getName())
                .room(room)
                .build();

        room.getUsersId().add(user.getId());

        roomRepository.save(room);
        containerRepository.save(container);

        return room;
    }

    public List<RoomResponseDto> loadAllRooms() {
        return roomRepository.findAllByAvailableTrue()
                .stream()
                .map(this::toRoomResponseDto)
                .collect(Collectors.toList());
    }

    public Room enteredRoom(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        // 1. 만약 현재인원과 총 인원이 같으면 들어갈 수 없다. 현재 인원은 무조건 총 인원보다 작아야 한다.
        //    만약 그렇지 않다면 예외처리
        if (room.getPersonCnt().equals(room.getMaxPeople())) {
            room.setfullRoom(true);
            throw new RuntimeException("너는 우리와 함께 할 수 없어");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // JWT에서 사용자의 이메일 가져오기
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = byEmail.orElseThrow();

        // 2. 예외에 걸리지 않았다고 할 때, 현재 인원수에 1을 더한다 (내가 들어갔으니까)
        //    그리고 입장한 아이디를 usersId에 추가한다.
        room.setPersonCnt(room.getPersonCnt() + 1);
        room.getUsersId().add(user.getId()); // userId는 메소드 인자로 받거나 다른 방식으로 결정해야 함

        // 데이터베이스 업데이트 (room 엔티티 저장)
        roomRepository.save(room);

        // 3. 나머지는 그냥 반환해준다
        return room;
    }

    public void fixRoom(String roomId, RoomFixDto roomFixDto) {
        roomRepository.findById(roomId).ifPresent(room -> {
            // name 업데이트 (null이 아닌 경우에만)
            if (roomFixDto.getName() != null) {
                room.setName(roomFixDto.getName());
            }

            // password와 isLocked 업데이트
            if (roomFixDto.getPassword() != null) {
                room.setPassword(roomFixDto.getPassword());
                room.setIsLocked(true);
            } else if (roomFixDto.getIsLocked() != null && !roomFixDto.getIsLocked()) {
                // password가 null이고, isLocked가 명시적으로 false로 설정된 경우
                room.setIsLocked(false);
                room.setPassword(null); // isLocked가 false일 때 password도 null로 설정
            }

            // 변경사항을 데이터베이스에 저장
            roomRepository.save(room);
        });
    }

    public void exitRoom(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // JWT에서 사용자의 이메일 가져오기
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = byEmail.orElseThrow();

        if (room.getFullRoom()) {
            room.setfullRoom(false);
        }
        // exitRoom을 실행하는 user가 Owner라면 방은 폭파됨. 참가 인원은 모두 삭제되고
        // UsersId에 있는 모든 유저들을 로비로 리다이렉트 해야함
        if(user.equals(room.getOwner())) {
            room.setAvailable(false);
            room.getUsersId().clear();
            room.setPersonCnt(0);
            room.setOwner(null);
            user.setRoom(null);
            roomRepository.save(room);
            userRepository.save(user);
            throw new RoomDestroyException("도비는 이제 자유에요.");
        } else {
            room.setPersonCnt(room.getPersonCnt() - 1);
            room.getUsersId().remove(user.getId());
        }

        roomRepository.save(room);
    }

    public List<RoomResponseDto> searchRooms(String name, RoomType type) {
        if (name == null) {
            return roomRepository.findAllByType(type)
                    .stream()
                    .map(this::toRoomResponseDto)
                    .collect(Collectors.toList());
        }
        if (type == null) {
            return roomRepository.findAllByNameContaining(name)
                    .stream()
                    .map(this::toRoomResponseDto)
                    .collect(Collectors.toList());
        }
        return roomRepository.findAllByNameContainingAndType(name, type)
                .stream()
                .map(this::toRoomResponseDto)
                .collect(Collectors.toList());
    }

    private RoomResponseDto toRoomResponseDto(Room room) {
        return RoomResponseDto.builder()
                .roomId(room.getId())
                .name(room.getName())
                .isLocked(room.getIsLocked())
                .type(room.getType())
                .available(room.getAvailable())
                .fullRoom(room.getFullRoom())
                .personCnt(room.getPersonCnt())
                .maxPeople(room.getMaxPeople())
                .ownerName(Optional.ofNullable(room.getOwner())
                        .map(User::getName)
                        .orElse("Unknown"))
                .build();
    }
}
