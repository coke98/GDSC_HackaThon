package GDSC.HackaThon.service;

import GDSC.HackaThon.domain.Attachment;
import GDSC.HackaThon.domain.Board;
import GDSC.HackaThon.domain.FileStore;
import GDSC.HackaThon.domain.enums.AttachmentType;
import GDSC.HackaThon.dto.request.BoardPostDto;
import GDSC.HackaThon.dto.request.BoardPostFormDto;
import GDSC.HackaThon.dto.request.BoardPostUpdateDto;
import GDSC.HackaThon.dto.response.BoardListResponse;
import GDSC.HackaThon.dto.response.BoardResponse;
import GDSC.HackaThon.repository.AttachmentRepository;
import GDSC.HackaThon.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final AttachmentServiceImpl attachmentServiceImpl;
    private final BoardRepository boardRepository;

    private final AttachmentRepository attachmentRepository;

    private final FileStore fileStore;



    @Transactional
    public Board post(BoardPostDto boardPostDto) throws IOException {

        // 여기서는 아직 첨부파일이 DB에 저장되지 않았다. board가 저장될때 cascade 옵션으로 넣는다.
        List<Attachment> attachments = attachmentServiceImpl.saveAttachments(boardPostDto.getAttachmentFiles());

        // 사용자가 웹에서 작성한 데이터 + 사용자 정보를 담은 dto에서 createBoard()를 하면
        // 해당 정보 모두들 담은 Entity 객체 FoodBoard를 반환 ( 해당 Entity 안에는 파일 리스트가 공백인 상태)
        Board board = boardPostDto.createBoard();
        board.addAttachedFiles(attachments); // 연관 관계 편의 설정 -> cascade 옵션 적극 활용


        // Cascade.ALL안에 포함되어 있는 Cascade.Persist를 통해 같이 영속화되어 저장된다.
        return boardRepository.save(board);
    }

    public List<BoardListResponse> findAllBoards() {

        List<Board> boards = boardRepository.findAllWithMember();

        List<BoardListResponse> boardListResponses = BoardListResponse.of(boards);

        return boardListResponses;
    }

    public BoardResponse findById(Long id) {

        Board board = boardRepository.findByIdWithMember(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        BoardResponse boardResponse = BoardResponse.of(board);
        return boardResponse;
    }

    public BoardResponse findBySerialNumber(String serialNumber) {

        Board board = boardRepository.findBySerialNumberWithMember(serialNumber).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        BoardResponse boardResponse = BoardResponse.of(board);
        return boardResponse;
    }

    @Transactional
    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public Board update(Long id, BoardPostUpdateDto updateDto) throws IOException {

        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        // 수정하고자 하는 파일이 있으면 , 게시글이 원래 가지고 있던 사진들 삭제 처리
        // 수정하고자 하는 파일이 없으면 , 기존의 사진들을 삭제하지 않는다.
        if (updateDto.getImageFiles().get(0).getBytes().length != 0) {
            attachmentRepository.deleteByBoardId(id);
        }

        List<MultipartFile> imageFiles = updateDto.getImageFiles();
        List<Attachment> attachments = fileStore.storeFiles(imageFiles, AttachmentType.IMAGE);


        attachments.stream().forEach(e-> e.setBoard(board));

        Board changed = board.updateEntity(updateDto, attachments);


        return changed;

    }
}
