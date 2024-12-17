package com.example.demo.Repository;

import com.example.demo.Domain.Job;
import com.example.demo.Domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    boolean existsByName(String name);
    List<Job> findBySkillsIn(List<Skill> skills);
}
