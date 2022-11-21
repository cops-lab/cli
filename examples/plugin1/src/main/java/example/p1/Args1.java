/*
 * Copyright 2022 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.p1;

import com.beust.jcommander.Parameter;

public class Args1 {

    @Parameter(names = "--foo", arity = 1, description = "some foo for P1")
    public String foo;

    @Parameter(names = "--bar", arity = 1, description = "some bar for P1")
    public Integer bar; // Integer allows to detect non-specification, use int to have default values

}