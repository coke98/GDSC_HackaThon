package GDSC.HackaThon.controller;

import GDSC.HackaThon.domain.Attachment;
import GDSC.HackaThon.domain.Board;
import GDSC.HackaThon.domain.FileStore;
import GDSC.HackaThon.domain.Member;
import GDSC.HackaThon.domain.enums.AttachmentType;
import GDSC.HackaThon.dto.request.BoardPostDto;
import GDSC.HackaThon.dto.request.BoardPostFormDto;
import GDSC.HackaThon.dto.request.BoardPostUpdateDto;
import GDSC.HackaThon.dto.response.BoardListResponse;
import GDSC.HackaThon.dto.response.BoardResponse;
import GDSC.HackaThon.repository.AttachmentRepository;
import GDSC.HackaThon.repository.BoardRepository;
import GDSC.HackaThon.repository.MemberRepository;
import GDSC.HackaThon.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import static GDSC.HackaThon.domain.enums.State.nameToEnum;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "Board Controller : 게시글 CRUD")
public class BoardController {

    private final BoardService boardService;
    private final FileStore fileStore;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;


    /**
     * 게시글 작성
     */
    @PostMapping(value = "/boardForm" , produces = "application/json")
    @ApiOperation(value="게시글 등록하는 Controller" , notes = "해당 api로 요청시 게시글을 작성한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imageFiles" , value = "등록하고자 하는 게시글의 사진" ),
            @ApiImplicitParam(name = "content" , value = "게시글 설명 입력" ),
            @ApiImplicitParam(name = "serialNumber" , value = "자전거 고유 번호" ),
            @ApiImplicitParam(name = "companyName" , value = "자전거 회사 이름" )

    })
    public ResponseEntity<String> save_BoardForm(@RequestPart("imageFiles") List<MultipartFile> imageFiles,
                                        @RequestParam("content") String content,
                                        @RequestParam("serialNumber") String serialNumber,
                                        @RequestParam("companyName") String companyName) throws IOException {

        Member member = memberRepository.findById(1L).orElse(null);

        BoardPostFormDto build = BoardPostFormDto.builder()
                .content(content)
                .serialNumber(serialNumber)
                .companyName(companyName)
                .imageFiles(imageFiles).build();


        BoardPostDto boardPostDto = build.createBoardPostDto(member);// member 정보를 담고 있는 BoardPostDto 만든다.

        boardService.post(boardPostDto);// 최종 등록 Db에 등록

        return ResponseEntity.ok("게시글 등록에 성공하였습니다.");
    }



    /**
     * 모든 게시글 가져오기
     */
    @GetMapping(value = "/boards", produces = "application/json")
    @ApiOperation(value="모든 게시글 조회하기" , notes = "해당 api로 요청시 모든 게시글을 조회한다.")
    public List<BoardListResponse> getBoards() {

        List<BoardListResponse> allBoards = boardService.findAllBoards();

        return allBoards;
    }

    /**
     * 게시글 상세 페이지 조회
     */
    @GetMapping(value = "/boards/{id}", produces = "application/json")
    @ApiOperation(value="게시글의 특정 상세 페이지로 이동한다." , notes = "해당 api로 요청시 id에 해당하는 게시글 상세 페이지로 이동한다.")
    @ApiImplicitParam(name = "id" , value = "게시글 고유 id")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long id) {
        BoardResponse responseDto = boardService.findById(id);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 자전거 고유 번호로 게시글 찾기
     */
    @GetMapping(value = "/boards/serialNumber/{serialNumber}", produces = "application/json")
    @ApiOperation(value="자전거 고유 번호로 게시글 조회하기" , notes = "해당 api로 자전거 고유 번호로 게시글 조회하기")
    @ApiImplicitParam(name = "serialNumber" , value = "자전거 시리얼 넘버")
    public ResponseEntity<BoardResponse> getBoardBySerialNumber(@PathVariable String serialNumber) {

        BoardResponse responseDto = boardService.findBySerialNumber(serialNumber);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping(value = "/boards/{id}", produces = "application/json")
    @ApiOperation(value="게시글 삭제하기" , notes = "해당 api로 요청시 id에 해당하는 게시글을 삭제한다.")
    @ApiImplicitParam(name = "id" , value = "게시글 고유 id")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
        boardService.deleteById(id);
        return ResponseEntity.ok("게시글 삭제에 성공하였습니다.");
    }

    /**
     * 게시글 수정
     */
    @PutMapping(value = "/boards/{id}", produces = "application/json")
    @ApiOperation(value="게시글 수정하기" , notes = "해당 api로 요청시 id에 해당하는 게시글을 수정한다.")
    @ApiImplicitParam(name = "id" , value = "게시글 고유 id")
    public ResponseEntity<String> updateBoard(@PathVariable Long id,
                                              @RequestPart("imageFiles") List<MultipartFile> imageFiles,
                                              @RequestParam("content") String content,
                                              @RequestParam("state") String state,
                                              @RequestParam("serialNumber") String serialNumber,
                                              @RequestParam("companyName") String companyName) throws IOException {


        BoardPostUpdateDto updateDto = BoardPostUpdateDto.builder()
                .content(content)
                .state(nameToEnum(state))
                .serialNumber(serialNumber)
                .company(companyName)
                .imageFiles(imageFiles).build();

        Board update = boardService.update(id, updateDto);


        return ResponseEntity.ok("게시글 수정에 성공하였습니다.");
    }




    /**
     * 서버에 있는 사진 불러오기
     */
    @GetMapping(value="/images/{filename}" , produces = MediaType.IMAGE_JPEG_VALUE)
    @ApiImplicitParam(name = "filename" , value = "가져오고자 하는 게시글 사진의 파일이름, 확장자 합쳐서")
    public UrlResource processImg(@PathVariable String filename) throws MalformedURLException {

        // /home/ubuntu/hackathon/resources/images/

        // file:user.dir/resources/images/UUID + .확장자
        log.info("fileDir:{}", System.getProperty("user.dir"));

        UrlResource urlResource = new UrlResource("file:" + fileStore.createPath(filename, AttachmentType.IMAGE));

        log.info("urlResource:{}", urlResource);

        return urlResource;
    }


}
