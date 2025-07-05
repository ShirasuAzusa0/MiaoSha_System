package ben.miaoshasystem.repository;

import ben.miaoshasystem.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tags, Integer> {
    @Query(value = """
    SELECT t.tagId, t.tagName, t.goodId
    FROM tags t
    INNER JOIN (
        SELECT MIN(tagId) AS id FROM tags GROUP BY tagName
    ) t2 ON t.tagId = t2.id
""", nativeQuery = true)
    List<Tags> findAll();
}
