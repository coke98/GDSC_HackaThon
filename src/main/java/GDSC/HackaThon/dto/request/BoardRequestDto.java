package GDSC.HackaThon.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    @ApiModelProperty(value = "게시글 제목")
    private String title;
    @ApiModelProperty(value = "게시글 설명")
    private String content;
    @ApiModelProperty(value = "자전거 시리얼 넘버")
    private String serialNumber;
    @ApiModelProperty(value = "자전거 회사 이름")
    private String companyName;
}
