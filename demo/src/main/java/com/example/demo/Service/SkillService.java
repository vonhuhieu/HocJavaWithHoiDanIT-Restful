package com.example.demo.Service;

import com.example.demo.Domain.DTO.Response.ResultPaginationDTO;
import com.example.demo.Domain.Skill;
import com.example.demo.Repository.SkillRepository;
import com.example.demo.Util.Error.ExistsByData;
import com.example.demo.Util.Error.IDInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService {
    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill createSkill(Skill skill){
        boolean checkSkillExists = this.skillRepository.existsByName(skill.getName());
        if (checkSkillExists){
            throw new ExistsByData("Skill already exists.Please try again");
        }
        Skill newSkill = new Skill();
        newSkill.setName(skill.getName());
        this.skillRepository.save(newSkill);
        return newSkill;
    }

    public Skill updateSkill(Skill skill){
        Optional<Skill> checkSkillID = this.skillRepository.findById(skill.getId());
        if (!checkSkillID.isPresent()){
            throw new IDInvalidException("Please check ID's skill");
        }
        boolean checkSkillExists = this.skillRepository.existsByName(skill.getName());
        if (checkSkillExists && skill.getId() != checkSkillID.get().getId()){
            throw new ExistsByData("Skill already exists. Please try again");
        }
        Skill currentSkill = checkSkillID.get();
        currentSkill.setName(skill.getName());
        this.skillRepository.save(currentSkill);
        return currentSkill;
    }

    public ResultPaginationDTO fetchListSkills(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        // lấy từ pageable vì đây là 2 thông số mà frontend truyền lên
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageSkill.getTotalPages());
        meta.setTotal(pageSkill.getTotalElements());
        result.setMeta(meta);
        result.setResult(pageSkill.getContent());
        return result;
    }
}
