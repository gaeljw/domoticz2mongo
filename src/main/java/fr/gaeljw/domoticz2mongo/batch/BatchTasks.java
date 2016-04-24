package fr.gaeljw.domoticz2mongo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTasks.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importTemperaturesJob;

    @Scheduled(fixedDelayString = "${domoticz.query.interval}")
    public void importTemperatures() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        LOGGER.info("Running importTemperaturesJob");
        jobLauncher.run(importTemperaturesJob, new JobParameters());
    }

}
