package be.abis.exercise.repository;

import be.abis.exercise.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SessionJpaRepository extends JpaRepository<Session, Integer> {

    // JPQL: all sessions (cancelled or not) for a course
    @Query("select s from Session s where upper(s.course.shortTitle) = upper(:title)")
    List<Session> findSessionsForCourse(@Param("title") String shortTitle);

    // native: only the non-cancelled sessions for a course
    @Query(value = "select s.* from sessions s join courses c on s.s_cid = c.cid " +
            "where upper(c.cstitle) = upper(:title) and s.scancel is null order by s.sdate",
            nativeQuery = true)
    List<Session> findActiveSessionsForCourse(@Param("title") String shortTitle);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update sessions set scancel = 'C' where sno = :id", nativeQuery = true)
    int cancelSession(@Param("id") int sessionId);

}
