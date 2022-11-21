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
package example.p2;

import javax.inject.Inject;

import dev.c0ps.cli.Plugin;
import example.p1.utils.Bar;

public class Plugin2 implements Plugin {

    private Args2 args;
    private Bar bar;

    @Inject
    public Plugin2(Args2 args, Bar bar) {
        this.args = args;
        this.bar = bar;

    }

    @Override
    public void run() {
        System.out.printf("Hello, %s!\n", args.name);
        System.out.printf("Bar is %s.\n", bar.getBar());
    }
}