package GDSC.HackaThon.controller;

import GDSC.HackaThon.domain.FileStore;
import GDSC.HackaThon.domain.Member;
import GDSC.HackaThon.domain.enums.AttachmentType;
import GDSC.HackaThon.dto.request.BoardPostDto;
import GDSC.HackaThon.dto.request.BoardPostFormDto;
import GDSC.HackaThon.repository.MemberRepository;
import GDSC.HackaThon.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileStore fileStore;

    private final MemberRepository memberRepository;



    /**
     * 새롭게 작성한  게시글 폼을 등록한다.
     *
     * 사진 같은 경우는 "사진이름.확장자" 형식으로 들어오면 될거 같다.
     *
     * 사진을 반환할때는 "UUID" 파일번호만 반환해주면
     * 아래의 GetMapping을 통해서
     * /images/{filename} filename에 storefilename을 넣어주면 된다 곧 UUID를 보내주면 된다.
     * 그러면 해당 GetMapping을 통해서 서버의 /resources/images에 접근해서 해당 사진을 찾아줄것이다.
     *
     *
     *
     * @author LEE SOO CHAN
     */
    @PostMapping(value = "/boardForm" , produces = "application/json")
    @ResponseBody
    public String save_FoodForm(@RequestPart("imageFiles") List<MultipartFile> imageFiles,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("serialNumber") String serialNumber,
                                @RequestParam("companyName") String companyName) throws IOException {

        Member member = memberRepository.findById(1L).orElse(null);

        BoardPostFormDto build = BoardPostFormDto.builder()
                .title(title)
                .content(content)
                .serialNumber(serialNumber)
                .companyName(companyName)
                .imageFiles(imageFiles).build();


        BoardPostDto boardPostDto = build.createBoardPostDto(member);// member 정보를 담고 있는 BoardPostDto 만든다.

        boardService.post(boardPostDto); // 최종 등록 Db에 등록

        return "good";
    }


    @ResponseBody
    @GetMapping("/images/{filename}")
    public UrlResource processImg(@PathVariable String filename) throws MalformedURLException {


        // file:user.dir/resources/images/UUID + .확장자

        return new UrlResource("file:" + fileStore.createPath(filename, AttachmentType.IMAGE));
    }


}
