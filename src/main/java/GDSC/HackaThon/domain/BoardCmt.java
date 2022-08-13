package GDSC.HackaThon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class BoardCmt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 댓글 작성자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", foreignKey = @ForeignKey(name = "fk_board_cmt_member"))
    private Member member;


    /**
     * 어떤 게시글에 댓글을 작성했는지
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id", foreignKey = @ForeignKey(name = "fk_board_cmt_board"))
    private Board board;

    /**
     * 댓글 내용
     */
    private String cmt;

    @Builder
    public BoardCmt(Member member, Board board, String cmt) {
        this.member = member;
        this.board = board;
        this.cmt = cmt;
    }

}
