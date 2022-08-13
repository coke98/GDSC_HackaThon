package GDSC.HackaThon.service;

import GDSC.HackaThon.domain.Attachment;
import GDSC.HackaThon.domain.Board;
import GDSC.HackaThon.dto.request.BoardPostDto;
import GDSC.HackaThon.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final AttachmentServiceImpl attachmentServiceImpl;
    private final BoardRepository boardRepository;



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

}
