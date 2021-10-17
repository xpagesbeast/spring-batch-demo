package com.collabsphere.springbatchdemo.controller;

import com.collabsphere.springbatchdemo.launcher.LegoJobLauncher;
import com.collabsphere.springbatchdemo.model.ui.RunJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    LegoJobLauncher legoJobLauncher;

    @PostMapping("/lego-parts")
    public @ResponseBody String runJob(@RequestBody RunJob runJob) throws Exception {
        switch(runJob.getBatchJobName()){
            case "LEGOPARTS": legoJobLauncher.run();
            break;
        };
        return runJob.getBatchJobName() + " ran.";
    }

    @GetMapping("/lego-parts/{batchJobName}")
    public @ResponseBody String runJob(@PathVariable String batchJobName) throws Exception {
        switch(batchJobName){
            case "LEGOPARTS": legoJobLauncher.run();
                break;
        };
        return batchJobName + " ran.";
    }
}
