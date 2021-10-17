package com.collabsphere.springbatchdemo.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Spring will pull auto wire objects from application context
 *
 * The job parameters creates a unique job instance.
 * SpringBatch will never run a completed job instance again.
 *
 */
@Component
@EnableScheduling
public class LegoJobLauncher implements CommandLineRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Override
    public void run(String... args) throws Exception {

        JobParameters params = new JobParametersBuilder()
                .addLong("jobId",System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(job,params);
    }

   // @Scheduled(cron = "0 */1 * * * ?")
    /*
    public void perform() throws Exception{
        JobParameters params = new JobParametersBuilder()
                .addLong("jobId",System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(job,params);
    }
    */
}
