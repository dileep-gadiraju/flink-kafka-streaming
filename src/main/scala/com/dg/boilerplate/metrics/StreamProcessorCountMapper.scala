package com.dg.boilerplate.metrics

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.{Counter, Metric}

class StreamProcessorCountMapper extends RichMapFunction[String,String] {
  @transient private var counter: Counter = _

  override def open(parameters: Configuration): Unit = {
    counter = getRuntimeContext()
      .getMetricGroup()
      .counter("ProcessCount")
  }

  override def map(value: String): String = {
    counter.inc()
    value
  }
}