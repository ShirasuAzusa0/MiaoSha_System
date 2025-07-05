package ben.miaoshasystem.controller;

import ben.miaoshasystem.entity.Goods;
import ben.miaoshasystem.entity.Tags;
import ben.miaoshasystem.entity.vo.response.GoodDetailsVO;
import ben.miaoshasystem.entity.vo.response.GoodListElementVO;
import ben.miaoshasystem.service.GoodService;
import ben.miaoshasystem.util.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodController {

    @Autowired
    private GoodService goodService;

    @Resource
    JwtUtils jwtUtils;

    @GetMapping("/{goodId}")
    public ResponseEntity<?> getGoodData(@PathVariable("goodId") int goodId) {
        Goods good = goodService.getGoodById(goodId);
        if (good == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> tags = good.getTags().stream()
                .map(Tags::getTagName)
                .toList();

        GoodDetailsVO vo = new GoodDetailsVO(
                good.getGoodId(),
                good.getGoodName(),
                good.getQuantity(),
                good.getDescription(),
                good.getImage(),
                good.getPrice(),
                good.getContent(),
                tags
        );
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取商品信息成功",
                        "data", vo
                )
        );
    }

    @GetMapping("/list/{tag}")
    public ResponseEntity<?> getGoodsList(@PathVariable("tag") String tag, int choice, int limit) {
        List<Goods> goods = goodService.getLimitedGoods(tag, choice, limit);

        List<GoodListElementVO> vos = goods.stream()
                .map(g -> new GoodListElementVO(
                        g.getGoodId(),
                        g.getGoodName(),
                        g.getDescription(),
                        g.getPrice(),
                        g.getImage(),
                        g.getQuantity()
                )).toList();

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取商品列表成功",
                        "goods", vos
                )
        );
    }
}
