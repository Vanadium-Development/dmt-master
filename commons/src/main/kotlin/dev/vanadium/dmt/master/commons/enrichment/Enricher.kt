package dev.vanadium.dmt.master.commons.enrichment

interface Enricher<IN, OUT> {
    fun enrich(input: IN): OUT


}