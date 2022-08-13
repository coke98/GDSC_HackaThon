package GDSC.HackaThon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 이름
     */
    private String name;

    /**
     * 사용자 아이디
     */
    private String username;

    private String password;


    /**
     * 사용자가 게시한 게시글 목록
     */
    @OneToMany(mappedBy = "member")
    private Collection<Board> boards = new ArrayList<>();


    @Builder
    public Member(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }


}
