package edu.stanford.protege.webprotege;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static java.util.Objects.requireNonNull;

public class BatchCollector<T> implements Collector<T, List<T>, List<T>> {

    private final int batchSize;
    private final Consumer<List<T>> batchProcessor;


    /**
     * Constructs the batch collector
     *
     * @param batchSize the batch size after which the batchProcessor should be called
     * @param batchProcessor the batch processor which accepts batches of records to process
     */
    BatchCollector(int batchSize, Consumer<List<T>> batchProcessor) {
        this.batchSize = batchSize;
        this.batchProcessor = requireNonNull(batchProcessor);
    }

    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }

    public BiConsumer<List<T>, T> accumulator() {
        return (ts, t) -> {
            ts.add(t);
            if (ts.size() >= batchSize) {
                batchProcessor.accept(ts);
                ts.clear();
            }
        };
    }

    public BinaryOperator<List<T>> combiner() {
        return (ts, ots) -> {
            // process each parallel list without checking for batch size
            // avoids adding all elements of one to another
            // can be modified if a strict batching mode is required
            batchProcessor.accept(ts);
            batchProcessor.accept(ots);
            return Collections.emptyList();
        };
    }

    public Function<List<T>, List<T>> finisher() {
        return ts -> {
            batchProcessor.accept(ts);
            return Collections.emptyList();
        };
    }

    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}