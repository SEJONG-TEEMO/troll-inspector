package sejong.teemo.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import sejong.teemo.batch.entity.TempUserInfo;
import sejong.teemo.batch.entity.UserInfo;
import sejong.teemo.batch.repository.mapper.TempUserInfoRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MigrationJob {

    private final PlatformTransactionManager tm;
    private final DataSource dataSource;

    private static final String MIGRATION_JOB_NAME = "migrationJob";
    private static final String STEP = "step";

    private static final int CHUNK_SIZE = 100;

    @Bean
    public Job migrateDataJob(JobRepository jobRepository) {
        return new JobBuilder(MIGRATION_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(migrateDataStep(jobRepository))
                .build();
    }

    @Bean
    @JobScope
    public Step migrateDataStep(JobRepository jobRepository) {
        return new StepBuilder(MIGRATION_JOB_NAME + STEP, jobRepository)
                .<TempUserInfo, UserInfo>chunk(CHUNK_SIZE, tm)
                .reader(jdbcPagingItemReader())
                .processor(itemProcessor())
                .writer(jdbcBatchItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<TempUserInfo> jdbcPagingItemReader() {

        Map<String, Order> params = new HashMap<>(1);
        params.put("league_point", Order.ASCENDING);

        return new JdbcPagingItemReaderBuilder<TempUserInfo>()
                .name("jdbcPagingItemReader")
                .dataSource(dataSource)
                .rowMapper(new TempUserInfoRowMapper())
                .pageSize(CHUNK_SIZE)
                .fetchSize(CHUNK_SIZE)
                .selectClause("*")
                .fromClause("tmp_user_info")
                .sortKeys(params)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<TempUserInfo, UserInfo> itemProcessor() {
        return TempUserInfo::convert;
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<UserInfo> jdbcBatchItemWriter() {

        String sql = "insert into user_info (game_name, tag_line, puuid, summoner_id, queue_type, tier, `rank`, wins, losses, league_point, account_id, profile_icon_id, revision_data, summoner_level) " +
                "VALUES (:gameName, :tagLine, :puuid, :summonerId, :queueType, :tier, :rank, :wins, :losses, :leaguePoint, :accountId, :profileIconId, :revisionData, :summonerLevel) " +
                "ON DUPLICATE KEY UPDATE " +
                "game_name = VALUES(game_name), " +
                "tag_line = VALUES(tag_line), " +
                "puuid = VALUES(puuid), " +
                "summoner_id = VALUES(summoner_id), " +
                "queue_type = VALUES(queue_type), " +
                "tier = VALUES(tier), " +
                "`rank` = VALUES(`rank`), " +
                "wins = VALUES(wins), " +
                "losses = VALUES(losses), " +
                "league_point = VALUES(league_point), " +
                "account_id = VALUES(account_id), " +
                "profile_icon_id = VALUES(profile_icon_id), " +
                "revision_data = VALUES(revision_data), " +
                "summoner_level = VALUES(summoner_level)";

        JdbcBatchItemWriter<UserInfo> itemWriter = new JdbcBatchItemWriterBuilder<UserInfo>()
                .sql(sql)
                .beanMapped()
                .dataSource(dataSource)
                .build();

        itemWriter.afterPropertiesSet();
        return itemWriter;
    }
}
