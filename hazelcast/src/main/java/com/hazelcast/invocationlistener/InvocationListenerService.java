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
package com.hazelcast.invocationlistener;

import com.hazelcast.invocationlistener.impl.InvocationEventImpl;

import java.util.List;

public interface InvocationListenerService {
    void registerListener(InvocationListener listener);
    List<InvocationListener> getListeners();

    default void invoke(InvocationEvent invocation) {
        getListeners().forEach(listener -> listener.invoke(invocation));
    }

    default void complete(InvocationEvent invocation) {
        getListeners().forEach(listener -> listener.complete(invocation));
    }

    default void completeExceptionally(InvocationEvent invocation, Throwable t) {
        getListeners().forEach(listener -> listener.completeExceptionally(invocation, t));
    }
}
