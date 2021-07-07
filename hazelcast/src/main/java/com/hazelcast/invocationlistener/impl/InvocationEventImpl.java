/*
 * Copyright (c) 2008-2021, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hazelcast.invocationlistener.impl;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.spi.impl.ClientInvocation;
import com.hazelcast.internal.nio.Connection;
import com.hazelcast.invocationlistener.InvocationEvent;
import com.hazelcast.spi.impl.operationservice.Operation;

import java.util.UUID;

public class InvocationEventImpl implements InvocationEvent {

    private final long correlationId;
    private final UUID invocationId;
    private final String operationName;
    private final Connection connection;
    private final String target;
    private final int partitionId;

    public InvocationEventImpl(ClientInvocation clientInvocation) {
        ClientMessage clientMessage = clientInvocation.getClientMessage();
        this.correlationId = clientMessage.getCorrelationId();
        this.operationName = clientMessage.getOperationName();
        this.connection = clientMessage.getConnection();
        this.target = clientInvocation.getTarget();
        this.partitionId = clientMessage.getPartitionId();
        this.invocationId = null;
    }

    public InvocationEventImpl(Operation operation) {
        this.correlationId = operation.getCallId();
        this.operationName = operation.getServiceName();
        this.connection = operation.getConnection();
        this.target = operation.getServiceName();
        this.partitionId = operation.getPartitionId();
        this.invocationId = null;
    }

    @Override
    public long getCorrelationId() {
        return correlationId;
    }

    @Override
    public String getOperationName() {
        return operationName;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public int getPartitionId() {
        return partitionId;
    }

    @Override
    public UUID getInvocationId() {
        return invocationId;
    }
}
