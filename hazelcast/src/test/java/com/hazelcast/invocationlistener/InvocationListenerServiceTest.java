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
package com.hazelcast.invocationlistener;

import com.hazelcast.client.test.TestHazelcastFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.test.HazelcastParallelClassRunner;
import com.hazelcast.test.annotation.ParallelJVMTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(HazelcastParallelClassRunner.class)
@Category({QuickTest.class, ParallelJVMTest.class})
public class InvocationListenerServiceTest {

    private final TestHazelcastFactory hazelcastFactory = new TestHazelcastFactory();
    private HazelcastInstance member;
    private HazelcastInstance client;

    @Before
    public void setup() {
        member = hazelcastFactory.newHazelcastInstance();
        client = hazelcastFactory.newHazelcastClient();
    }

    @After
    public void tearDown() {
        hazelcastFactory.terminateAll();
    }

    @Test
    public void testInterceptorInvokeAndCompleteForClientInstance() {
        InvocationListener invocationListener = mock(InvocationListener.class);
        client.getInvocationListenerService().registerListener(invocationListener);
        IMap<Object, Object> name = client.getMap("Name");
        name.put("1", "1");
        verify(invocationListener).invoke(any());
        verify(invocationListener).complete(any());
    }

    @Test
    public void testInterceptorInvokeAndCompleteForEmbeddedInstance() {
        InvocationListener invocationListener = mock(InvocationListener.class);
        client.getInvocationListenerService().registerListener(invocationListener);
        IMap<Object, Object> name = member.getMap("Name");
        name.put("2", "2");
        verify(invocationListener).invoke(any());
        verify(invocationListener).complete(any());
    }
}
