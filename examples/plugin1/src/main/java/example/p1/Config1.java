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

import static dev.c0ps.cli.AssertArgs.assertFor;

import com.google.inject.Provides;
import com.google.inject.Singleton;

import dev.c0ps.cli.AssertArgs;
import dev.c0ps.cli.InjectorConfig;
import dev.c0ps.cli.InjectorConfigBase;
import example.p1.utils.Bar;
import example.p1.utils.Foo;
import example.p1.utils.FooBar;

@InjectorConfig
public class Config1 extends InjectorConfigBase {

    private Args1 args;

    public Config1(Args1 args) {
        this.args = args;
    }

    @Provides
    @Singleton
    public Args1 bindArgs1() {
        return args;
    }

    @Provides
    @Singleton
    public Foo bindFoo() {
        assertFor(args) //
                .notNull(args -> args.foo, "must provide a foo") //
                .that(args -> args.foo.length() > 3, "foo must be at least 3 characters");
        return new Foo(args.foo);
    }

    @Provides
    @Singleton
    public Bar bindBar() {
        AssertArgs.notNull(args, args -> args.bar, "must provide a bar");
        AssertArgs.that(args.bar, bar -> bar > 0, "bar must be set to a value greater 0");
        return new Bar(args.bar);
    }

    @Provides
    @Singleton
    public FooBar bindFooBar(Foo foo, Bar bar) {
        return new FooBar(foo, bar);
    }
}