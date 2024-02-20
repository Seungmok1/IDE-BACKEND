package everyide.webide.room;

import everyide.webide.config.auth.exception.RoomDestroyException;
import everyide.webide.container.ContainerRepository;
import everyide.webide.container.domain.Container;
import everyide.webide.fileSystem.DirectoryService;
import everyide.webide.fileSystem.FileService;
import everyide.webide.fileSystem.domain.Directory;
import everyide.webide.room.domain.*;
import everyide.webide.room.domain.dto.RoomFixDto;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    @Value("${file.basePath}")
    private String basePath;
    private final RoomRepository roomRepository;
    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;
    private final DirectoryService directoryService;
    private final FileService fileService;

    public Room create(CreateRoomRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // JWT에서 사용자의 이메일 가져오기
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = byEmail.orElseThrow();


        // 1. 방생성
        Room room = Room.builder()
                .name(requestDto.getName())
                .isLocked(requestDto.getIsLocked())
                .password(requestDto.getPassword())
                .type(RoomType.valueOf(requestDto.getRoomType()))
                .maxPeople(requestDto.getMaxPeople())
                .usersId(new ArrayList<>())
                .owner(user)
                .build();


        // 2. 방 디렉토리 생성 (방 아이디 이름으로)
        Directory rootDirectory = directoryService.createRootDirectory(room.getId());
        if (rootDirectory != null) {
            room.setRootPath(basePath + room.getId());
            log.info("방 디렉토리 생성 완료");
        } else {
            log.info("루트 디렉토리 생성불가");
        }


        // 3. 컨테이너 생성
        String path = basePath + room.getId() + "/" + requestDto.getContainerName();

        Container newContainer = Container.builder()
                .name(requestDto.getContainerName())
                .description(requestDto.getContainerDescription())
                .path(path)
                .language(requestDto.getContainerLanguage())
                .build();

        newContainer.setRoom(room);
        containerRepository.save(newContainer);
        fileService.createDefaultFile(path, requestDto.getContainerLanguage());

        room.getUsersId().add(user.getId());

        roomRepository.save(room);

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
            throw new RuntimeException("너는 우리와 함께 할 수 없어");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // JWT에서 사용자의 이메일 가져오기
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = byEmail.orElseThrow();
        // 유저의 룸 리스트에 룸 아이디 추가
        user.getRoomsList().add(roomId);
        // 입장한 아이디를 usersId에 추가한다.
        room.getUsersId().add(user.getId());

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

    public void leaveRoom(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // JWT에서 사용자의 이메일 가져오기
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = byEmail.orElseThrow();


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
                .personCnt(room.getPersonCnt())
                .maxPeople(room.getMaxPeople())
                .ownerName(Optional.ofNullable(room.getOwner())
                        .map(User::getName)
                        .orElse("Unknown"))
                .build();
    }
}
