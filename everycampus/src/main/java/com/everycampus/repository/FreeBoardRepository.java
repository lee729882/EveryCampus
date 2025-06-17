package com.everycampus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.everycampus.entity.FreeBoard;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

    // 개별 학교 + 게시판용
    List<FreeBoard> findBySchoolAndCategoryOrderByCreatedAtDesc(String school, String category);

    // 전체 학교 + 특정 게시판용 (예: 통합 자유게시판)
    List<FreeBoard> findByCategoryOrderByCreatedAtDesc(String category);
<<<<<<< HEAD

    // ✅ 사용자 작성 게시글 보기용
    List<FreeBoard> findByWriterOrderByCreatedAtDesc(String writer);
}
=======
    //HOT 게시뭏
    List<FreeBoard> findTop10ByLikeCountGreaterThanOrderByLikeCountDesc(int likeCount);

}
>>>>>>> branch 'main' of https://github.com/lee729882/EveryCampus.git
