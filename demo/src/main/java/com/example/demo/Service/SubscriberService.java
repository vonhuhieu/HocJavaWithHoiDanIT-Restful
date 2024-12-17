package com.example.demo.Service;

import com.example.demo.Domain.Skill;
import com.example.demo.Domain.Subscriber;
import com.example.demo.Repository.SkillRepository;
import com.example.demo.Repository.SubscriberRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Util.Error.ExistsByData;
import com.example.demo.Util.Error.IDInvalidException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository, UserRepository userRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    public Subscriber createSubscriber(Subscriber subscriber){
        boolean checkExistsByEmail = this.userRepository.existsByEmail(subscriber.getEmail());
        if (checkExistsByEmail){
            throw new ExistsByData("Email already exists");
        }
        List<Long> idSkills = new ArrayList<>();
        for (Skill skill : subscriber.getSkills()){
            idSkills.add(skill.getId());
        }
        Subscriber newSubscriber = new Subscriber();
        newSubscriber.setEmail(subscriber.getEmail());
        newSubscriber.setName(subscriber.getName());
        newSubscriber.setSkills(this.skillRepository.findByIdIn(idSkills));
        this.subscriberRepository.save(newSubscriber);
        return newSubscriber;
    }

    public Subscriber updateSubscriber(Subscriber subscriber){
        Optional<Subscriber> checkExistsBySubscriber = this.subscriberRepository.findById(subscriber.getId());
        if (checkExistsBySubscriber.isEmpty()){
            throw new IDInvalidException("No exists subscriber whose id = " + subscriber.getId());
        }
        Subscriber currentSubscriber = checkExistsBySubscriber.get();
        List<Long> idSkills = new ArrayList<>();
        for (Skill skill : subscriber.getSkills()){
            idSkills.add(skill.getId());
        }
        currentSubscriber.setSkills(this.skillRepository.findByIdIn(idSkills));
        this.subscriberRepository.save(currentSubscriber);
        return currentSubscriber;
    }
}
