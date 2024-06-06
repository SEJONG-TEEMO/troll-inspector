package sejong.teemo.riotapi.async;

import lombok.extern.slf4j.Slf4j;
import sejong.teemo.riotapi.exception.ExceptionProvider;
import sejong.teemo.riotapi.exception.FailedApiCallingException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * <p>
 *     I는 인풋 타입 O는 아웃풋 타입으로 구성된다.
 *     <br/>ex) CurrentGameParticipant (input) -> ChampionMastery (output)
 * </p>
 * @param <I> input
 * @param <O> output
 */
@Slf4j
public class AsyncCall<I, O> {

    private final List<I> lists;

    public AsyncCall(List<I> lists) {
        this.lists = lists;
    }

    public List<O> execute(int nThread, Function<I, O> function) {
        try (ExecutorService executorService = Executors.newFixedThreadPool(nThread)) {
            List<CompletableFuture<O>> futures = lists.stream()
                    .map(list -> CompletableFuture.supplyAsync(() -> function.apply(list), executorService))
                    .toList();

            return futures.stream().map(CompletableFuture::join).toList();
        } catch (Exception e) {
            log.error("call error = {}", e.getMessage());
            throw new FailedApiCallingException(ExceptionProvider.RIOT_CHAMPION_MASTERY_API_CALL_FAILED);
        }
    }
}
