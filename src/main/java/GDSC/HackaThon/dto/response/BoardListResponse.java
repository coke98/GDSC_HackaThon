package GDSC.HackaThon.dto.response;

import GDSC.HackaThon.domain.Attachment;
import GDSC.HackaThon.domain.Board;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BoardListResponse {

    @ApiModelProperty(value = "게시글 고유 id")
    private Long id;

    @ApiModelProperty(value = "게시글 설명")
    private String content;

    @ApiModelProperty(value = "게시글에 들어가있는 자건거 고유 번호")
    private String serialNumber;

    @ApiModelProperty(value = "자전거 회사")
    private String companyName;

    @ApiModelProperty(value = "게시글 인증상태 (도난, 인증, 미인증)")
    private String state;

    @ApiModelProperty(value = "게시글을 작성한 사용자 이름")
    private String username;


    @ApiModelProperty(value = "게시글 생성 날짜")
    private LocalDateTime createdDate;

    @ApiModelProperty(value = "게시글에 포함된 자전거 사진")
    private String storedFileName;




    public static List<BoardListResponse> of(List<Board> boards) {

        return boards.stream()
                .map(board -> new BoardListResponse(board.getId(), board.getContent(),
                        board.getSerialNumber(), board.getCompany(), board.getState().getName(), board.getMember().getUsername(), board.getCreatedDate(),
                        board.getAttachedFiles().get(0).getStoreFilename()))
                .collect(Collectors.toList());
    }
}
