package sejong.teemo.riotapi.common.async;

import sejong.teemo.riotapi.dto.LeagueEntryDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class AsyncCallRiotApi<T> {

    private final int startPage;
    private final int endPage;

    public AsyncCallRiotApi(int startPage, int endPage) {
        this.startPage = startPage;
        this.endPage = endPage;
    }

    public List<List<T>> execute(int nThread, Function<Integer, List<T>> function) {

        try (ExecutorService executorService = Executors.newFixedThreadPool(nThread)) {
            List<CompletableFuture<List<T>>> futures = IntStream.rangeClosed(startPage, endPage)
                    .mapToObj(page -> CompletableFuture.supplyAsync(() -> function.apply(page), executorService))
                    .toList();

            return futures.stream().map(CompletableFuture::join).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
