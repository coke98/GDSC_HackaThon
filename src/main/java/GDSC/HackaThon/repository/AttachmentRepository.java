package GDSC.HackaThon.repository;

import GDSC.HackaThon.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query(value="delete from Attachment a where a.board.id = :boardId")
    void deleteByBoardId(@Param("boardId") Long id);
}
