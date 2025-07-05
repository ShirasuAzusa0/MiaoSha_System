package ben.miaoshasystem.controller;

import ben.miaoshasystem.entity.Tags;
import ben.miaoshasystem.entity.vo.response.TagListElementVO;
import ben.miaoshasystem.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tag-list")
    public ResponseEntity<?> getTagList() {
        List<Tags> tagList = tagService.getAllTags();

        List<TagListElementVO> vos = tagList.stream()
                .map(t -> new TagListElementVO(
                        t.getTagName()
                )).toList();

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取标签列表成功",
                        "List", vos
                )
        );
    }
}
