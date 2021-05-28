/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
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
import com.hazelcast.invocationlistener.InvocationEvent;

public class InvocationEventImpl implements InvocationEvent {

    private final long correlationId;
    private final String operationName;

    public InvocationEventImpl(ClientMessage clientMessage) {
        this.correlationId = clientMessage.getCorrelationId();
        this.operationName = clientMessage.getOperationName();
    }

    @Override
    public long getCorrelationId() {
        return correlationId;
    }

    @Override
    public String getOperationName() {
        return operationName;
    }
}
