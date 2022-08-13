package GDSC.HackaThon.repository;

import GDSC.HackaThon.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b join fetch b.member")
    List<Board> findAllWithMember();

    @Query("select b from Board b join fetch b.member where b.id = :id")
    Optional<Board> findByIdWithMember(@Param("id") Long id);

    @Query("select b from Board b join fetch b.member where b.serialNumber = :serialNumber")
    Optional<Board> findBySerialNumberWithMember(@Param("serialNumber") String serialNumber);
}
