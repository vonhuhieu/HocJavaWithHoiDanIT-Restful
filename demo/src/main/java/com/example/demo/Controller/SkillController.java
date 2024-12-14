package com.example.demo.Controller;

import com.example.demo.Domain.DTO.Response.ResultPaginationDTO;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Domain.Skill;
import com.example.demo.Service.SkillService;
import com.example.demo.Util.ResponseUtil;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private SkillService skillService;
    private ResponseUtil responseUtil;

    public SkillController(ResponseUtil responseUtil, SkillService skillService) {
        this.responseUtil = responseUtil;
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    public ResponseEntity<RestResponse<Object>> createSkill(@Valid @RequestBody Skill postManSkill){
        Skill newSkill = this.skillService.createSkill(postManSkill);
        return this.responseUtil.buildCreateResponse("create a new skillsuccessfully", newSkill);
    }

    @PutMapping("/skills")
    public ResponseEntity<RestResponse<Object>> updateSkill(@Valid @RequestBody Skill postManSkill){
        Skill updateSkill = this.skillService.updateSkill(postManSkill);
        return this.responseUtil.buildSuccessResponse("update the skill successfully", updateSkill);
    }

    @GetMapping("/skills")
    public ResponseEntity<RestResponse<Object>> fetchListSkills(@Filter Specification<Skill> specification, Pageable pageable) {
        ResultPaginationDTO listSkills = this.skillService.fetchListSkills(specification, pageable);
        return this.responseUtil.buildSuccessResponse("Get list skills successfully", listSkills);
    }
}
