package com.example.demo.Service;

import com.example.demo.Domain.Company;
import com.example.demo.Domain.DTO.Response.Company.CompanyDTO;
import com.example.demo.Domain.DTO.Response.Job.JobDTO;
import com.example.demo.Domain.DTO.Response.Pagination.ResultPaginationDTO;
import com.example.demo.Domain.Job;
import com.example.demo.Domain.Skill;
import com.example.demo.Repository.CompanyRepository;
import com.example.demo.Repository.JobRepository;
import com.example.demo.Repository.SkillRepository;
import com.example.demo.Util.Error.ExistsByData;
import com.example.demo.Util.Error.IDInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    public JobDTO updateJob(Job job){
        Optional<Job> fetchJobById = this.jobRepository.findById(job.getId());
        if (fetchJobById.isEmpty()){
            throw new IDInvalidException("No exist Job whose id = " + job.getId());
        }
        boolean checkJobExists = this.jobRepository.existsByName(job.getName());
        if (checkJobExists){
            throw new ExistsByData("Job already exists");
        }
        Job currentJob = fetchJobById.get();
        currentJob.setName(job.getName());
        currentJob.setLocation(job.getLocation());
        currentJob.setSalary(job.getSalary());
        currentJob.setQuantity(job.getQuantity());
        currentJob.setLevel(job.getLevel());
        currentJob.setDescription(job.getDescription());
        currentJob.setStartDate(job.getStartDate());
        currentJob.setEndDate(job.getEndDate());
        currentJob.setActive(job.isActive());
        currentJob.setSkills(job.getSkills());
        currentJob.setCompany(job.getCompany());
        this.jobRepository.save(currentJob);
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(currentJob.getId());
        jobDTO.setName(currentJob.getName());
        jobDTO.setLocation(currentJob.getLocation());
        jobDTO.setSalary(currentJob.getSalary());
        jobDTO.setQuantity(currentJob.getQuantity());
        jobDTO.setQuantity(currentJob.getQuantity());
        jobDTO.setLevel(currentJob.getLevel());
        jobDTO.setDescription(currentJob.getDescription());
        jobDTO.setStartDate(currentJob.getStartDate());
        jobDTO.setEndDate(currentJob.getEndDate());
        jobDTO.setActive(currentJob.isActive());
        List<Long> idSkills = new ArrayList<>();
        for (Skill skill : currentJob.getSkills()){
            idSkills.add(skill.getId());
        }
        List<Skill> listSkillsFromInput = this.skillRepository.findByIdIn(idSkills);
        List<String> listSkills = new ArrayList<>();
        for (Skill skill : listSkillsFromInput){
            listSkills.add(skill.getName());
        }
        jobDTO.setSkills(listSkills);
        Optional<Company> company = this.companyRepository.findById(currentJob.getCompany().getId());
        if (!company.isPresent()){
            throw new IDInvalidException("Please check company's id");
        }
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.get().getId());
        companyDTO.setName(company.get().getName());
        jobDTO.setCompanyDTO(companyDTO);
        jobDTO.setCreatedAt(currentJob.getCreatedAt());
        jobDTO.setCreatedBy(currentJob.getCreatedBy());
        jobDTO.setUpdatedAt(currentJob.getUpdatedAt());
        jobDTO.setUpdatedBy(currentJob.getUpdatedBy());
        return jobDTO;
    }

    public void deleteJob(long id){
        Optional<Job> currentJob = this.jobRepository.findById(id);
        if (currentJob.isEmpty()){
            throw new IDInvalidException("No exists job whose id = " + id);
        }
        this.jobRepository.deleteById(currentJob.get().getId());
    }

    public JobDTO fetchJobById(long id){
        Optional<Job> fetchJobById = this.jobRepository.findById(id);
        if (fetchJobById.isEmpty()){
            throw new IDInvalidException("No exists job whose id = " + id);
        }
        Job currentJob = fetchJobById.get();
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(currentJob.getId());
        jobDTO.setName(currentJob.getName());
        jobDTO.setLocation(currentJob.getLocation());
        jobDTO.setSalary(currentJob.getSalary());
        jobDTO.setQuantity(currentJob.getQuantity());
        jobDTO.setQuantity(currentJob.getQuantity());
        jobDTO.setLevel(currentJob.getLevel());
        jobDTO.setDescription(currentJob.getDescription());
        jobDTO.setStartDate(currentJob.getStartDate());
        jobDTO.setEndDate(currentJob.getEndDate());
        jobDTO.setActive(currentJob.isActive());
        List<Long> idSkills = new ArrayList<>();
        for (Skill skill : currentJob.getSkills()){
            idSkills.add(skill.getId());
        }
        List<Skill> listSkillsFromInput = this.skillRepository.findByIdIn(idSkills);
        List<String> listSkills = new ArrayList<>();
        for (Skill skill : listSkillsFromInput){
            listSkills.add(skill.getName());
        }
        jobDTO.setSkills(listSkills);
        Optional<Company> company = this.companyRepository.findById(currentJob.getCompany().getId());
        if (!company.isPresent()){
            throw new IDInvalidException("Please check company's id");
        }
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.get().getId());
        companyDTO.setName(company.get().getName());
        jobDTO.setCompanyDTO(companyDTO);
        jobDTO.setCreatedAt(currentJob.getCreatedAt());
        jobDTO.setCreatedBy(currentJob.getCreatedBy());
        jobDTO.setUpdatedAt(currentJob.getUpdatedAt());
        jobDTO.setUpdatedBy(currentJob.getUpdatedBy());
        return jobDTO;
    }

    public ResultPaginationDTO fetchListJobs(Specification<Job> specification, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        // lấy từ pageable vì đây là 2 thông số mà frontend truyền lên
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageJob.getTotalPages());
        meta.setTotal(pageJob.getTotalElements());
        result.setMeta(meta);
        List<JobDTO> listJobs = new ArrayList<>();
        for (Job job : pageJob.getContent()){
            JobDTO jobDTO = new JobDTO();
            jobDTO.setId(job.getId());
            jobDTO.setName(job.getName());
            jobDTO.setLocation(job.getLocation());
            jobDTO.setSalary(job.getSalary());
            jobDTO.setQuantity(job.getQuantity());
            jobDTO.setQuantity(job.getQuantity());
            jobDTO.setLevel(job.getLevel());
            jobDTO.setDescription(job.getDescription());
            jobDTO.setStartDate(job.getStartDate());
            jobDTO.setEndDate(job.getEndDate());
            jobDTO.setActive(job.isActive());
            List<Long> idSkills = new ArrayList<>();
            for (Skill skill : job.getSkills()){
                idSkills.add(skill.getId());
            }
            List<Skill> listSkillsFromInput = this.skillRepository.findByIdIn(idSkills);
            List<String> listSkills = new ArrayList<>();
            for (Skill skill : listSkillsFromInput){
                listSkills.add(skill.getName());
            }
            jobDTO.setSkills(listSkills);
            Optional<Company> company = this.companyRepository.findById(job.getCompany().getId());
            if (!company.isPresent()){
                throw new IDInvalidException("Please check company's id");
            }
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setId(company.get().getId());
            companyDTO.setName(company.get().getName());
            jobDTO.setCompanyDTO(companyDTO);
            jobDTO.setCreatedAt(job.getCreatedAt());
            jobDTO.setCreatedBy(job.getCreatedBy());
            jobDTO.setUpdatedAt(job.getUpdatedAt());
            jobDTO.setUpdatedBy(job.getUpdatedBy());
            listJobs.add(jobDTO);
        }
        result.setResult(listJobs);
        return result;
    }
}
