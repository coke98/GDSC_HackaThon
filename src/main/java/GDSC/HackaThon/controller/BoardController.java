package GDSC.HackaThon.controller;

import GDSC.HackaThon.domain.FileStore;
import GDSC.HackaThon.domain.Member;
import GDSC.HackaThon.domain.enums.AttachmentType;
import GDSC.HackaThon.dto.request.BoardPostDto;
import GDSC.HackaThon.dto.request.BoardPostFormDto;
import GDSC.HackaThon.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileStore fileStore;

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
     * @param boardPostFormDto 작성하고자 하는  게시글의 상세 정보를 담고 있다. -> member에 대한 정보는 아직 X
     * @param authentication 게시글을 작성한 사용자의 정보가 필요하다. 게시글은 작성한 사용자의 고유 id값이 필요하다.
     * @author LEE SOO CHAN
     */
    @PostMapping("/boardForm")
    public String save_FoodForm(@ModelAttribute BoardPostFormDto boardPostFormDto , Authentication authentication) throws IOException {

//        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
//        Long user_db_id = userDetails.getUser().getDb_id();
//        User user = userRepository.findById(user_db_id).orElse(null); // 로그인한 사용자의 entity 찾아준다.

        // 해당 게시글을 작성한 Member의 Entity가 필요하다. todo : member를 찾아와서 넣어줘야 한다.
        Member member = new Member();

        BoardPostDto boardPostDto = boardPostFormDto.createBoardPostDto(member);// member 정보를 담고 있는 BoardPostDto 만든다.

        boardService.post(boardPostDto); // 최종 등록 Db에 등록

        return "redirect:/foods";
    }

    /**
     * 맛집 게시글 혹은 맛집 게시글 상세 페이지에서 사진을 html에 보여주기 위해서 필요
     * @param filename /images/{파일이름}
     * @return 사진을 찾아주는 url
     * @throws MalformedURLException
     * @author LEE SOO CHAN
     */
    @ResponseBody
    @GetMapping("/images/{filename}")
    public UrlResource processImg(@PathVariable String filename) throws MalformedURLException {


        // file:user.dir/resources/images/UUID + .확장자

        return new UrlResource("file:" + fileStore.createPath(filename, AttachmentType.IMAGE));
    }


}
