package GDSC.HackaThon.dto.request;

import GDSC.HackaThon.domain.BaseTimeEntity;
import GDSC.HackaThon.domain.Member;
import GDSC.HackaThon.domain.enums.AttachmentType;
import GDSC.HackaThon.domain.enums.Company;
import GDSC.HackaThon.domain.enums.State;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 해당 dto는 단순히 등록 웹 맛집 등록 폼에서 보여지는 값만 가져온다.
 * 추후 DTO로 변환을 통해 누가 업로드 했는지 알기 위해
 * user 추가할 예정 BoardPostDto에서..
 */
@Getter
@NoArgsConstructor
public class BoardPostFormDto extends BaseTimeEntity {

    @ApiModelProperty(value = "게시글 설명")
    private String content;

    /**
     * 실제 웹 맛집 등록 폼으로부터 입력 받는 값들만 선언
     * 어느 회사에서 만든 자전거인지 한글 회사명 넘어온다.
     */
    @ApiModelProperty(value = "게시글에 작성한 자전거의 회사")
    private String companyName;


    /**
     * 자전거 고유 번호
     */
    @ApiModelProperty(value = "자전거 고유 번호")
    private String serialNumber;


    /**
     * 추가적으로 해당 이미지 파일 과 일반 파일도 list로 함께 웹 폼에서 받는다.
     */
    /**
     * client에서 multipart를 받을 수 있을지 모르겠지만
     * 단순하게 해당 사용자의 "파일이름.확장자"이름만 있으면 될거 같다.
     *
     * 대신 MultipartFile말고 특정한 클래스를 만들어야 할거 같다.
     * 그 안에는 OriginName이 필요할거 같다.
     * getter도 필요할거 같다.
     * .getOriginalFilename()
     *
     */
    @ApiModelProperty(value = "자전거 이미지 목록")
    private List<MultipartFile> imageFiles;

    /**
     * 게시글 등록을 원하는 요청 같은 경우에는 state필드가 아직 인증되지 않음으로 초기 DB에 저장되어야 한다.
     */



    /**
     *  우선 살펴보면 dto를 만들때, 일반 파일에 아무 값도 들어가 있지 않으면 그냥 null인 list가 담긴다.
     *  추가적으로 단순히 웹 폼에서의 데이터를 가져오기 때문에 user의 정보가 저장 X
     *
     *  바인딩의 목적으로 만들어졌다.
     */
    @Builder
    public BoardPostFormDto(String companyName,String content,String serialNumber ,
                            List<MultipartFile> imageFiles ) {
        this.companyName = companyName;
        this.content = content;
        this.serialNumber = serialNumber;
        this.imageFiles = (imageFiles != null) ? imageFiles : new ArrayList<>() ;
    }


    /**
     * web에서 입력 값을 다시 user 정보가 담긴 dto로 다시 생성
     *
     *  웹에서 값을 입력 후 controller로 보내면 해당 웹 폼 dto가 바인딩 되어서 받는다..
     *  바인딩이 되더라도, user 정보가 없기 떄문에 user 정보를 담을 dto를 생성할 필요가 있다.
     *
     *  해당 BoardPostDto은 Entity로 전환되기 직전의 dto이므로 모든 정보를 담고 있어야 한다. (user 정보까지)
     */
    public BoardPostDto createBoardPostDto(Member member) {

        // 프론트에서 날라온 row multipartFile을 우리가 저장하고자하는 AttachmentType에 따라서 map 형식으로 넣는다.
        Map<AttachmentType, List<MultipartFile>> attachments = getAttachmentTypeListMap();

        return BoardPostDto.builder()
                .member(member)
                .company(companyName) //string을 enum으로 바꿔야 한다.
                .state(State.AUTHENTICATED) // 초기 board를 작성할떄는 간리자가 등록을 인증을 해줘야 한다. 그러므로 인증되지 않은 상태를 의미하는 Enum을 넣어준다.
                .content(content)
                .serialNumber(serialNumber)
                .attachmentFiles(attachments)
                .build();
    }

    /**
     * 일반 사진 파일과 첨부 파일을 하나의 MAP형태로 묶어주는 역할
     * @return
     */
    private Map<AttachmentType, List<MultipartFile>> getAttachmentTypeListMap() {

        Map<AttachmentType, List<MultipartFile>> attachments = new ConcurrentHashMap<>();

        attachments.put(AttachmentType.IMAGE, imageFiles);
        //attachments.put(AttachmentType.GENERAL, generalFiles);

        return attachments;
    }






}
