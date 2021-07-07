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

package com.hazelcast.spi.impl.operationservice.impl;

import com.hazelcast.cluster.Address;
import com.hazelcast.invocationlistener.InvocationListenerService;
import com.hazelcast.invocationlistener.impl.InvocationEventImpl;
import com.hazelcast.spi.impl.operationservice.InvocationBuilder;
import com.hazelcast.spi.impl.operationservice.Operation;

/**
 * An {@link InvocationBuilder} that is tied to the {@link OperationServiceImpl}.
 */
class InvocationBuilderImpl extends InvocationBuilder {

    private final Invocation.Context context;
    private final InvocationListenerService invocationListenerService;

    InvocationBuilderImpl(Invocation.Context context, String serviceName, Operation op,
                          int partitionId, InvocationListenerService invocationListenerService) {
        this(context, serviceName, op, partitionId, null, invocationListenerService);
    }

    InvocationBuilderImpl(Invocation.Context context, String serviceName, Operation op,
                          Address target, InvocationListenerService invocationListenerService) {
        this(context, serviceName, op, Operation.GENERIC_PARTITION_ID, target, invocationListenerService);
    }

    private InvocationBuilderImpl(Invocation.Context context, String serviceName, Operation op,
                                  int partitionId, Address target, InvocationListenerService invocationListenerService) {
        super(serviceName, op, partitionId, target);
        this.context = context;
        this.invocationListenerService = invocationListenerService;
    }

    @Override
    public InvocationFuture invoke() {
        try {
            op.setServiceName(serviceName);
            InvocationFuture invocationFuture;
            if (target == null) {
                op.setPartitionId(partitionId).setReplicaIndex(replicaIndex);
                invocationFuture = new PartitionInvocation(
                        context, op, doneCallback, tryCount, tryPauseMillis, callTimeout, resultDeserialized,
                        failOnIndeterminateOperationState, connectionManager).invoke();
            } else {
                invocationFuture = new TargetInvocation(
                        context, op, target, doneCallback, tryCount, tryPauseMillis,
                        callTimeout, resultDeserialized, connectionManager).invoke();
            }
            invocationListenerService.complete(new InvocationEventImpl(op));
            return invocationFuture;
        } catch (Exception e) {
            invocationListenerService.completeExceptionally(new InvocationEventImpl(op), e);
            throw e;
        }
    }
}
