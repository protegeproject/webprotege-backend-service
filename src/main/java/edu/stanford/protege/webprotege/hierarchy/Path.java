package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class Path<N> {

    private static final Path<?> EMPTY_PATH = new Path();

    private static final transient Transform<?, ?> IDENTITY_TRANSFORM = element -> element;

    private List<N> path = new ArrayList<>();


    @SuppressWarnings("unchecked")
    public static <N> Transform<N, N> getIdentityTransform() {
        return (Transform<N, N>) IDENTITY_TRANSFORM;
    }

    /**
     * For serialization purposes only
     */
    private Path() {
    }

    @JsonCreator
    public Path(List<N> path) {
        this.path = new ArrayList<>(path);
    }

    @SafeVarargs
    public static <N> Path<N> asPath(N... elements) {
        return new Path<>(Arrays.asList(elements));
    }

    @SuppressWarnings("unchecked")
    public static <N> Path<N> emptyPath() {
        return (Path<N>) EMPTY_PATH;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return path.isEmpty();
    }

    @JsonValue
    public List<N> asList() {
        return new ArrayList<>(path);
    }

    @JsonIgnore
    public int getLength() {
        return path.size();
    }

    public int size() {
        return path.size();
    }

    public N get(int index) {
        return path.get(index);
    }

    public boolean contains(N element) {
        return path.contains(element);
    }

    @Nonnull
    public Path<N> pathByAppending(@Nonnull N element) {
        List<N> p = new ArrayList<>(path);
        p.add(checkNotNull(element));
        return new Path<>(p);
    }

    public <T> Path<T> transform(Transform<N, T> transform) {
        List<T> result = new ArrayList<>(path.size());
        for(N element : path) {
            result.add(transform.transform(element));
        }
        return new Path<>(result);
    }

    @Nonnull
    public Iterator<N> iterator() {
        return path.iterator();
    }

    @JsonIgnore
    public Optional<N> getFirst() {
        if(path.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(path.get(0));
    }

    @JsonIgnore
    public Optional<N> getLast() {
        if(path.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(path.get(path.size() - 1));
    }

    @JsonIgnore
    public Optional<N> getLastPredecessor() {
        if(path.size() < 2) {
            return Optional.empty();
        }
        return Optional.of(path.get(path.size() - 2));
    }

    public Path<N> reverse() {
        return new Path<>(Lists.reverse(path));
    }

    @Override
    public int hashCode() {
        return "Path".hashCode() + path.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof Path)) {
            return false;
        }
        Path other = (Path) o;
        return this.path.equals(other.path);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Path")
                          .addValue(path)
                          .toString();
    }

    public interface Transform<N, T> {
        T transform(N element);
    }
}
