package sejong.teemo.batch.job;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.transaction.PlatformTransactionManager;
import sejong.teemo.batch.dto.LeagueEntryDto;
import sejong.teemo.batch.entity.UserInfo;
import sejong.teemo.batch.exception.FailedApiCallingException;
import sejong.teemo.batch.exception.TooManyApiCallingException;
import sejong.teemo.batch.item.process.LeagueItemProcess;
import sejong.teemo.batch.item.reader.LeagueItemReader;
import sejong.teemo.batch.item.writer.LeagueItemWriter;
import sejong.teemo.batch.repository.JdbcRepository;
import sejong.teemo.batch.service.BatchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

@Configuration
@RequiredArgsConstructor
public class LeagueJob {

    private final PlatformTransactionManager tm;
    private final BatchService batchService;
    private final JdbcRepository jdbcRepository;
    private final Bucket bucket;

    private static final String TIER_JOB = "tierJob";
    private static final String STEP = "I";

    private static final int CHUNK_SIZE = 10;

    @Bean
    public Job leagueInfoJob(JobRepository jobRepository) {
        return new JobBuilder(TIER_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(leagueInfoStepOne(jobRepository))
                .build();
    }

    @Bean
    @JobScope
    public Step leagueInfoStepOne(JobRepository jobRepository) {
        return new StepBuilder(TIER_JOB + STEP, jobRepository)
                .<List<LeagueEntryDto>, List<UserInfo>>chunk(CHUNK_SIZE, tm)
                .reader(leagueInfoItemReader(null, null))
                .processor(leagueInfoProcessor())
                .writer(leagueInfoItemWriter())
                .faultTolerant()
                .retryPolicy(retryPolicy())
                .backOffPolicy(backOffPolicy())
                .skip(FailedApiCallingException.class)
                .skip(IllegalArgumentException.class)
                .skipLimit(Integer.MAX_VALUE)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<List<LeagueEntryDto>> leagueInfoItemReader(@Value("#{jobParameters['tier']}") String tier,
                                                                 @Value("#{jobParameters['division']}") String division) {
        return new LeagueItemReader(batchService, tier, division);
    }

    @Bean
    @StepScope
    public ItemProcessor<List<LeagueEntryDto>, List<UserInfo>> leagueInfoProcessor() {
        return new LeagueItemProcess(batchService, bucket);
    }

    @Bean
    @StepScope
    public ItemWriter<List<UserInfo>> leagueInfoItemWriter() {
        return new LeagueItemWriter(jdbcRepository);
    }

    @Bean
    public RetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(TooManyApiCallingException.class, true);
        return new SimpleRetryPolicy(3, retryableExceptions);
    }

    @Bean
    public BackOffPolicy backOffPolicy() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(60000);
        backOffPolicy.setMultiplier(2);
        backOffPolicy.setMaxInterval(300000);

        return backOffPolicy;
    }
}
