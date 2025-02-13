package edu.stanford.protege.webprotege.shortform;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-11
 */
public class EntityBasedSimilarity extends SimilarityBase {

    @Inject
    public EntityBasedSimilarity() {
    }

    @Override
    protected double score(BasicStats stats, double freq, double docLen) {
        return freq * stats.getBoost();
    }

    @Override
    public String toString() {
        return EntityBasedSimilarity.class.getSimpleName();
    }
}
