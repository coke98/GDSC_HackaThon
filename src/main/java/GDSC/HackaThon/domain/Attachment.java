package GDSC.HackaThon.domain;


import GDSC.HackaThon.domain.enums.AttachmentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long id;

    private String originFilename;

    /**
     * 실제 서버에 저장될 파일이름이다. : UUID + .확장자
     */
    private String storeFilename;

    /**
     * 사진이 속한 사진
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;


    /**
     *  사진의 유형
     */
    @Enumerated(EnumType.STRING)
    private AttachmentType attachmentType;


    @Builder
    public Attachment(String originFileName, String storePath, AttachmentType attachmentType) {
        this.originFilename = originFileName;
        this.storeFilename = storePath;
        this.attachmentType = attachmentType;
    }
}

