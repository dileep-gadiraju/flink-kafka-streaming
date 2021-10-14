package com.dg.boilerplate.metrics

import com.dg.boilerplate.core.KafkaMsg
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.{Counter, Histogram}
import org.apache.flink.runtime.metrics.DescriptiveStatisticsHistogram

class FlinkMetricsExposingMapFunction extends RichMapFunction[KafkaMsg, KafkaMsg] {
  @transient private var eventCounter: Counter = _
  @transient private var valueHistogram: Histogram = _

  override def open(parameters: Configuration): Unit = {
    eventCounter = getRuntimeContext().getMetricGroup().counter("events")
    valueHistogram =
      getRuntimeContext()
        .getMetricGroup()
        .histogram("value_histogram", new DescriptiveStatisticsHistogram(10000));
  }

  override def map(in: KafkaMsg): KafkaMsg = {
    eventCounter.inc()
    valueHistogram.update(eventCounter.getCount)
    in
  }
}