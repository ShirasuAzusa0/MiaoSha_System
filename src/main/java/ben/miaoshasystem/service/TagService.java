package ben.miaoshasystem.service;

import ben.miaoshasystem.entity.Tags;
import ben.miaoshasystem.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tags> getAllTags() {
        return tagRepository.findAll();
    }

}
