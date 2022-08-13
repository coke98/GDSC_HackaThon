package GDSC.HackaThon.dto.request;

import GDSC.HackaThon.domain.Board;
import GDSC.HackaThon.domain.Member;
import GDSC.HackaThon.domain.enums.AttachmentType;
import GDSC.HackaThon.domain.enums.Company;
import GDSC.HackaThon.domain.enums.State;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
/**
 * 실제 데이터가 이동하는 부분 user 정보까지 합쳐서
 */
public class BoardPostDto {

    private Member member;

    /**
     * 실제 데이터를 등록할때는 state 와 Company를 넣어준다.
     */
    private String company;

    /**
     * * state 값은 기본적으로는 인증 대기 상태이다.
     */
    private State state;


    private String content;

    private String serialNumber;


    private Map<AttachmentType, List<MultipartFile>> attachmentFiles = new ConcurrentHashMap<>();

    /*
        dto에는 user 정보와 , 저장할 파일 리스트를 가지고 있다.
     */
    @Builder
    public BoardPostDto(Member member, State state, String company,String serialNumber ,String content,
                        Map<AttachmentType, List<MultipartFile>> attachmentFiles) {

        this.member = member;
        this.state = state;
        this.company = company;
        this.serialNumber = serialNumber;
        this.content = content;
        this.attachmentFiles = attachmentFiles;

    }

    /**
     * 실제 repository에서 저장하기 직전 dto를 Entity로 바꿔주는 역할
     * @return
     */
    public Board createBoard() {

        return Board.builder()
                .member(member)
                .content(content)
                .serialNumber(serialNumber)
                .state(state)
                .company(company)
                .attachedFiles(new ArrayList<>())
                .build();

    }


}
