package org.broadinstitute.hellbender.tools.genomicsdb;

import org.broadinstitute.hellbender.tools.walkers.genotyper.GenotypeCalculationArgumentCollection;

import java.nio.file.Path;

/**
 * Encapsulates the GenomicsDB-specific options relevant to the FeatureDataSource
 */
public final class GenomicsDBOptions {
    final private Path reference;
    final private boolean callGenotypes;
    final private int maxAlternateAlleles;
    final private int maxGenotypeCount;
    final private boolean useVCFCodec;

    public GenomicsDBOptions() {
        this(null, new GenomicsDBArgumentCollection());
    }

    public GenomicsDBOptions(final Path reference) {
        this(reference, new GenomicsDBArgumentCollection());
    }

    public GenomicsDBOptions(final Path reference, GenomicsDBArgumentCollection genomicsdbArgs) {
        this.reference = reference;
        this.callGenotypes = genomicsdbArgs.callGenotypes;
        this.maxAlternateAlleles = genomicsdbArgs.maxAlternateAlleles;
        this.maxGenotypeCount = genomicsdbArgs.maxGenotypeCount;
        this.useVCFCodec = genomicsdbArgs.useVCFCodec;
    }

    public GenomicsDBOptions(final Path reference, GenomicsDBBaseArgumentCollection genomicsdbArgs, GenotypeCalculationArgumentCollection genotypeCalcArgs) {
        this.reference = reference;
        this.callGenotypes = genomicsdbArgs.callGenotypes;
        this.maxAlternateAlleles = genotypeCalcArgs.MAX_ALTERNATE_ALLELES;
        this.maxGenotypeCount = genotypeCalcArgs.MAX_GENOTYPE_COUNT;
        this.useVCFCodec = genomicsdbArgs.useVCFCodec;
    }

    public Path getReference() {
        return reference;
    }

    public boolean doCallGenotypes() {
        return callGenotypes;
    }

    public int getMaxAlternateAlleles() {
        return maxAlternateAlleles;
    }

    public int getMaxGenotypeCount() {
        return maxGenotypeCount;
    }

    public boolean useVCFCodec() {
        return useVCFCodec;
    }
}
