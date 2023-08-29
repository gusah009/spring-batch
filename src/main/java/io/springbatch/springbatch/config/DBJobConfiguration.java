package io.springbatch.springbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DBJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("dbJob")
                .start(step1())
                .next(step2())
                .next(stepForExecutionContext1())
                .next(stepForExecutionContext2())
                .next(stepForExecutionContext3())
                .next(stepForExecutionContext4())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
//                    System.out.println("date param: " + jobParameters.getDate("date"));
                    System.out.println("seq param: " + jobParameters.getLong("seq"));
                    System.out.println("age param: " + jobParameters.getDouble("age"));

                    System.out.println("step1 was executed");

                    System.out.println(chunkContext.getStepContext().getJobParameters());
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 was executed");
//                    throw new RuntimeException("step2 failed!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step stepForExecutionContext1() {
        return stepBuilderFactory.get("stepForExecutionContext1")
                .tasklet((contribution, chunkContext) -> {
                    ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
                    ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();

                    String jobName = contribution.getStepExecution().getJobExecution().getJobInstance().getJobName();
                    String stepName = contribution.getStepExecution().getStepName();

                    if (jobExecutionContext.get("jobName") == null) {
                        jobExecutionContext.put("jobName", jobName);
                    }

                    if (stepExecutionContext.get("stepName") == null) {
                        stepExecutionContext.put("stepName", stepName);
                    }

                    System.out.println("jobName : " + jobExecutionContext.get("jobName"));
                    System.out.println("stepName : " + stepExecutionContext.get("stepName"));

                    System.out.println("stepForExecutionContext1 was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step stepForExecutionContext2() {
        return stepBuilderFactory.get("stepForExecutionContext2")
                .tasklet((contribution, chunkContext) -> {
                    ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
                    ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();

                    System.out.println("jobName : " + jobExecutionContext.get("jobName"));
                    System.out.println("stepName : " + stepExecutionContext.get("stepName"));

                    System.out.println("stepForExecutionContext2 was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step stepForExecutionContext3() {
        return stepBuilderFactory.get("stepForExecutionContext3")
                .tasklet((contribution, chunkContext) -> {
                    ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();

                    if (jobExecutionContext.get("name") == null) {
                        jobExecutionContext.put("name", "momo");
                        throw new RuntimeException("step3 was failed");
                    }
                    System.out.println("stepForExecutionContext3 was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step stepForExecutionContext4() {
        return stepBuilderFactory.get("stepForExecutionContext4")
                .tasklet((contribution, chunkContext) -> {
                    ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();

                    System.out.println("[step4] name : " + jobExecutionContext.get("name"));

                    System.out.println("stepForExecutionContext4 was executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
