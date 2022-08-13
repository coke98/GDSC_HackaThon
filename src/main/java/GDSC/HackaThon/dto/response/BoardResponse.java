package GDSC.HackaThon.dto.response;

import GDSC.HackaThon.domain.Board;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardResponse {

    @ApiModelProperty(value = "게시글 고유 id")
    private Long id;

    @ApiModelProperty(value = "게시글 설명")
    private String content;

    @ApiModelProperty(value = "자전거 일렬 번호")
    private String serialNumber;

    @ApiModelProperty(value = "자전거 회사 이름")
    private String companyName;

    @ApiModelProperty(value = "게시글 상태(인증, 미인증, 도난)")
    private String state;

    @ApiModelProperty(value = "게시글 작성자 이름")
    private String username;

    @ApiModelProperty(value = "게시글 작성 시간")
    private LocalDateTime createdDate;

    @ApiModelProperty(value = "게시글 사진 이름")
    private String storedFileName;


    public static BoardResponse of(Board board) {

        return new BoardResponse(board.getId(), board.getContent(),
                board.getSerialNumber(), board.getCompany(), board.getState().getName(), board.getMember().getUsername(), board.getCreatedDate(),
                board.getAttachedFiles().get(0).getStoreFilename());
    }
}
