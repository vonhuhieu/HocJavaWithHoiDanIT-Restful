package com.example.demo.Service;

import com.example.demo.Domain.Company;
import com.example.demo.Domain.DTO.Response.CompanyDTO;
import com.example.demo.Domain.DTO.Response.JobDTO;
import com.example.demo.Domain.Job;
import com.example.demo.Domain.Skill;
import com.example.demo.Repository.CompanyRepository;
import com.example.demo.Repository.JobRepository;
import com.example.demo.Repository.SkillRepository;
import com.example.demo.Util.Error.ExistsByData;
import com.example.demo.Util.Error.IDInvalidException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private JobRepository jobRepository;
    private SkillRepository skillRepository;
    private CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public JobDTO createJob(Job job){
        boolean checkJobExists = this.jobRepository.existsByName(job.getName());
        if (checkJobExists){
            throw new ExistsByData("Job already exists");
        }
        Job newJob = new Job();
        newJob.setName(job.getName());
        newJob.setLocation(job.getLocation());
        newJob.setSalary(job.getSalary());
        newJob.setQuantity(job.getQuantity());
        newJob.setLevel(job.getLevel());
        newJob.setDescription(job.getDescription());
        newJob.setStartDate(job.getStartDate());
        newJob.setEndDate(job.getEndDate());
        newJob.setActive(job.isActive());
        newJob.setSkills(job.getSkills());
        newJob.setCompany(job.getCompany());
        this.jobRepository.save(newJob);
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(newJob.getId());
        jobDTO.setName(newJob.getName());
        jobDTO.setLocation(newJob.getLocation());
        jobDTO.setSalary(newJob.getSalary());
        jobDTO.setQuantity(newJob.getQuantity());
        jobDTO.setQuantity(newJob.getQuantity());
        jobDTO.setLevel(newJob.getLevel());
        jobDTO.setDescription(newJob.getDescription());
        jobDTO.setStartDate(newJob.getStartDate());
        jobDTO.setEndDate(newJob.getEndDate());
        jobDTO.setActive(newJob.isActive());
        List<Long> idSkills = new ArrayList<>();
        for (Skill skill : newJob.getSkills()){
            idSkills.add(skill.getId());
        }
        List<Skill> listSkillsFromInput = this.skillRepository.findByIdIn(idSkills);
        List<String> listSkills = new ArrayList<>();
        for (Skill skill : listSkillsFromInput){
            listSkills.add(skill.getName());
        }
        jobDTO.setSkills(listSkills);
        Optional<Company> company = this.companyRepository.findById(newJob.getCompany().getId());
        if (!company.isPresent()){
            throw new IDInvalidException("Please check company's id");
        }
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.get().getId());
        companyDTO.setName(company.get().getName());
        jobDTO.setCompanyDTO(companyDTO);
        jobDTO.setCreatedAt(newJob.getCreatedAt());
        jobDTO.setCreatedBy(newJob.getCreatedBy());
        jobDTO.setUpdatedAt(newJob.getUpdatedAt());
        jobDTO.setUpdatedBy(newJob.getUpdatedBy());
        return jobDTO;
    }
}
