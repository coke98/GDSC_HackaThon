package GDSC.HackaThon.repository;

import GDSC.HackaThon.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


}
