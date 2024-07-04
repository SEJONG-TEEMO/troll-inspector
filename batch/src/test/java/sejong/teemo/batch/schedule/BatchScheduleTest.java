package sejong.teemo.batch.schedule;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.batch.application.schedule.BatchSchedule;
import sejong.teemo.batch.container.TestContainer;

@SpringBootTest
class BatchScheduleTest extends TestContainer {

    @Autowired
    private BatchSchedule batchSchedule;

    @Test
    @Disabled
    void 배치_통합_테스트() {
        // given

        // when
        batchSchedule.schedulingBatchUserInfo();

        // then

    }
}