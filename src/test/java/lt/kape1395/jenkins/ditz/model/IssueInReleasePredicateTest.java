/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package lt.kape1395.jenkins.ditz.model;

import org.apache.commons.collections.Predicate;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test cases for {@link IssueInReleasePredicate}.
 * @author k.petrauskas
 */
public class IssueInReleasePredicateTest {

    /**
     * Check all combinations.
     */
    @Test
    public void testEvaluate() {
        Predicate r0 = new IssueInReleasePredicate();
        Predicate r1 = new IssueInReleasePredicate(new Release("r1", "a"));
        
        Issue i0a = new Issue("a", "b", "c", "d", null);
        Issue i0b = new Issue("a", "b", "c", "d", "");
        Issue i1 = new Issue("a", "b", "c", "d", "r1");
        Issue i2 = new Issue("a", "b", "c", "d", "r2");
        
        assertThat(r0.evaluate(null), is(false));
        assertThat(r0.evaluate(getClass()), is(false));
        assertThat(r0.evaluate(i0a), is(true));
        assertThat(r0.evaluate(i0b), is(true));
        assertThat(r0.evaluate(i1), is(false));
        assertThat(r0.evaluate(i2), is(false));
        
        assertThat(r1.evaluate(null), is(false));
        assertThat(r1.evaluate(getClass()), is(false));
        assertThat(r1.evaluate(i0a), is(false));
        assertThat(r1.evaluate(i0b), is(false));
        assertThat(r1.evaluate(i1), is(true));
        assertThat(r1.evaluate(i2), is(false));
    }
}
