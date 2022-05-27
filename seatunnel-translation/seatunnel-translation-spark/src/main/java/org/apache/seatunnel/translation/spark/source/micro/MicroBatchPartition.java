/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.translation.spark.source.micro;

import org.apache.seatunnel.api.source.SeaTunnelSource;
import org.apache.seatunnel.api.source.SupportCoordinate;
import org.apache.seatunnel.api.table.type.SeaTunnelRow;

import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.sources.v2.reader.InputPartition;
import org.apache.spark.sql.sources.v2.reader.InputPartitionReader;
import org.apache.spark.sql.types.StructType;

public class MicroBatchPartition implements InputPartition<InternalRow> {
    protected final SeaTunnelSource<SeaTunnelRow, ?, ?> source;
    protected final Integer parallelism;
    protected final Integer subtaskId;
    protected final StructType rowType;
    protected final Integer checkpointId;
    protected final Integer checkpointInterval;
    protected final String checkpointPath;
    protected final String hdfsRoot;
    protected final String hdfsUser;

    public MicroBatchPartition(SeaTunnelSource<SeaTunnelRow, ?, ?> source,
                               Integer parallelism,
                               Integer subtaskId,
                               StructType rowType,
                               Integer checkpointId,
                               Integer checkpointInterval,
                               String checkpointPath,
                               String hdfsRoot,
                               String hdfsUser) {
        this.source = source;
        this.parallelism = parallelism;
        this.subtaskId = subtaskId;
        this.rowType = rowType;
        this.checkpointId = checkpointId;
        this.checkpointInterval = checkpointInterval;
        this.checkpointPath = checkpointPath;
        this.hdfsRoot = hdfsRoot;
        this.hdfsUser = hdfsUser;
    }

    @Override
    public InputPartitionReader<InternalRow> createPartitionReader() {
        if (source instanceof SupportCoordinate) {
            return new CoordinatedMicroBatchPartitionReader(source, parallelism, subtaskId, rowType, checkpointId, checkpointInterval, checkpointPath, hdfsRoot, hdfsUser);
        } else {
            return new ParallelMicroBatchPartitionReader(source, parallelism, subtaskId, rowType, checkpointId, checkpointInterval, checkpointPath, hdfsRoot, hdfsUser);
        }
    }
}
