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

import javax.inject.Inject;

import dev.c0ps.cli.Plugin;
import example.p1.utils.Bar;
import example.p1.utils.Foo;

public class Plugin1 implements Plugin {

    private Args1 args;
    private Foo foo;
    private Bar bar;

    @Inject
    public Plugin1(Args1 args, Foo foo, Bar bar) {
        this.args = args;
        this.foo = foo;
        this.bar = bar;
    }

    @Override
    public void run() {
        System.out.printf("You can directly access %s.\n", args.foo);
        System.out.printf("Also bound instances get injected, like %s or %s!", foo.getAsUpperCase(), bar.getBar());
    }
}