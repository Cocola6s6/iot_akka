package com.akka.test.controller;

import com.akka.test.actor.RuleEngineSysContext;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Validated
@RestController
@RequestMapping("/akka")
@RequiredArgsConstructor
public class AkkaController {

    private final RuleEngineSysContext ruleEngineSysContext;


    @GetMapping("/get")
    public Response get() {
        ruleEngineSysContext.tellRuleNode(null,  null, null);
        return Response.buildSuccess();
    }
}