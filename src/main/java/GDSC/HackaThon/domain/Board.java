package GDSC.HackaThon.domain;

import GDSC.HackaThon.domain.enums.Company;
import GDSC.HackaThon.domain.enums.State;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 번호 등록시 작성한 사용자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", foreignKey = @ForeignKey(name = "fk_board_user"))
    private Member member;

    /**
     * 인증 혹은 도난 상태
     */
    @Enumerated(EnumType.STRING)
    private State state;

    private String title;

    private String content;

    /**
     * 자전거를 만든 회사
     */
    private String company;

    /**
     * 자전거 고유 번호
     */
    private String serialNumber;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Attachment> attachedFiles = new ArrayList<>();


    /**
     * 연관 관계 편의 메서드
     * @param attachedFiles 맛집 게시글에 등록시 첨부하는 사진 목록
     */
    public void addAttachedFiles(List<Attachment> attachedFiles) {
        attachedFiles.stream().forEach(e->e.setBoard(this));
        this.attachedFiles = attachedFiles;
    }


    @Builder
    public Board(String title,State state ,String content, String company, String serialNumber, Member member,
                    List<Attachment> attachedFiles) {
        this.title = title;
        this.content = content;
        this.company = company;
        this.state = state;
        this.serialNumber = serialNumber;
        this.member = member;
        this.attachedFiles = attachedFiles;
    }

}
