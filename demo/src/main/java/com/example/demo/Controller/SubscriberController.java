package com.example.demo.Controller;

import com.example.demo.Domain.RestResponse;
import com.example.demo.Domain.Subscriber;
import com.example.demo.Service.SubscriberService;
import com.example.demo.Util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;
    private final ResponseUtil responseUtil;

    public SubscriberController(SubscriberService subscriberService, ResponseUtil responseUtil) {
        this.subscriberService = subscriberService;
        this.responseUtil = responseUtil;
    }

    @PostMapping("/subscribers")
    public ResponseEntity<RestResponse<Object>> createSubscriber(@Valid @RequestBody Subscriber postManSubscriber){
        return this.responseUtil.buildCreateResponse("Create a new subscriber successfully", this.subscriberService.createSubscriber(postManSubscriber));
    }

    @PutMapping("/subscribers")
    public ResponseEntity<RestResponse<Object>> updateSubscriber(@RequestBody Subscriber postManSubscriber){
        return this.responseUtil.buildSuccessResponse("Update the subscriber successfully", this.subscriberService.updateSubscriber(postManSubscriber));
    }
}
