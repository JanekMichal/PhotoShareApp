package com.janek.photoShareApp.repository;

import com.janek.photoShareApp.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByFollowerId(Long followerId);

    List<Follow> findAllByFollowingId(Long followingId);

    int countByFollowerId(Long followerId);

    int countByFollowingId(Long followingId);

    Optional<Follow> findFollowByFollowerIdAndFollowingId(Long followerId, Long followingId);

    void deleteFollowByFollowerIdAndFollowingId(Long followerId, Long followingId);
}

//	Create table following_t (
//		user_id bigint not null,
//		followed_user_id bigint,
//	--username varchar(20),
//	Primary key (user_id, followed_user_id),
//	FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE
//		,FOREIGN KEY (followed_user_id) REFERENCES users(id) ON UPDATE CASCADE
//		)
