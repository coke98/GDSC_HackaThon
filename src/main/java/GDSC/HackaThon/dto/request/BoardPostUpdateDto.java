package GDSC.HackaThon.dto.request;

import GDSC.HackaThon.domain.enums.AttachmentType;
import GDSC.HackaThon.domain.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@AllArgsConstructor
@Builder
public class BoardPostUpdateDto {

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


    private List<MultipartFile> imageFiles = new ArrayList<>();


}
