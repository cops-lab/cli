/*
 * Copyright 2021 Delft University of Technology
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
package dev.c0ps.cli;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static dev.c0ps.test.TestLoggerUtils.getFormattedLogs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import com.google.inject.Provides;

import dev.c0ps.test.TestLoggerUtils;
import other.OtherConfig;

public class RunnerTest {

    private static final String BASE_PKG = RunnerTest.class.getPackageName();

    private Runner sut;

    @BeforeEach
    public void setup() {
        sut = new Runner(BASE_PKG);

        TestPlugin.wasCalled = false;
        TestLoggerUtils.clearLog();
    }

    @Test
    public void pluginCanBeStarted() {
        assertFalse(TestPlugin.wasCalled);
        sut.run(new String[] { "--plugin", TestPlugin.class.getName() });
        assertTrue(TestPlugin.wasCalled);
    }

    @Test
    public void providedPackagesAreLookedUp() {
        sut = new Runner(OtherConfig.class.getPackageName());
        sut.run(new String[] { "--plugin", MultiConfigPlugin.class.getName() });
        // test is successful when plugin can be provisioned
    }

    @Test
    public void logSettingDefaultIsSet() {
        var logSettings = mock(ILogSettings.class);
        sut = new Runner(BASE_PKG, logSettings);
        sut.run(new String[] { "--plugin", TestPlugin.class.getName() });
        verify(logSettings).setLogLevel(LogLevel.INFO);
    }

    @Test
    public void logSettingCanBeChanged() {
        var logSettings = mock(ILogSettings.class);
        sut = new Runner(BASE_PKG, logSettings);
        sut.run(new String[] { "--plugin", TestPlugin.class.getName(), "--logLevel", "ERROR" });
        verify(logSettings).setLogLevel(LogLevel.ERROR);
    }

    @Test
    public void basicInfoReported() {
        sut.run(new String[] { "--plugin", TestPlugin.class.getName() });
        var logs = getFormattedLogs(Runner.class);
        assertEquals(2, logs.size());
        var msg = String.format("INFO Starting plugin %s ...", TestPlugin.class.getName());
        assertTrue(logs.contains(msg));
        assertTrue(logs.get(1).startsWith("INFO Max memory: "));
    }

    @Test
    public void missingPluginPrintsUsage() throws Exception {
        var out = SystemLambda.tapSystemOut(() -> {
            catchSystemExit(() -> {
                sut.run(new String[] {});
            });
        });
        assertTrue(out.contains("Insufficient startup arguments"));
        assertTrue(out.contains("no plugin defined"));
        assertTrue(out.contains("The *subset* of related arguments"));
        assertTrue(out.contains("--plugin"));
    }

    @Test
    public void handleThrowables() throws Exception {
        catchSystemExit(() -> {
            sut.run(new String[] { "--plugin", ThrowingPlugin.class.getName() });
        });
        var logs = getFormattedLogs(Runner.class);
        assertTrue(logs.contains("ERROR Throwable caught in main loader class, shutting down VM ..."));
    }

    @Test
    public void handleAssertArgErrorsRun() throws Exception {
        catchSystemExit(() -> {
            sut.run(new String[] { "--plugin", ArgsErrorRunPlugin.class.getName() });
        });
        var logs = getFormattedLogs(Runner.class);
        assertEquals(2, logs.size());
    }

    @Test
    public void handleAssertArgsErrorsInit() throws Exception {
        catchSystemExit(() -> {
            sut.run(new String[] { "--plugin", ArgsErrorInitPlugin.class.getName() });
        });
        var logs = getFormattedLogs(Runner.class);
        assertEquals(2, logs.size());
    }

    public static class TestPlugin implements Plugin {

        public static boolean wasCalled = false;

        @Override
        public void run() {
            wasCalled = true;
        }
    }

    public static class MultiConfigPlugin implements Plugin {

        @Inject
        public MultiConfigPlugin(RunnerArgs ra, ILogSettings ls) {
            System.out.println();
        }

        @Override
        public void run() {}
    }

    public static class ThrowingPlugin implements Plugin {

        @Override
        public void run() {
            throw new RuntimeException("RTE");
        }
    }

    @InjectorConfig
    public static class ArgsErrorInitConfig extends InjectorConfigBase {
        @Provides
        public IInjectorConfig provideSomethingButFail() {
            throw new AssertArgsError();
        }
    }

    public static class ArgsErrorInitPlugin implements Plugin {

        @Inject
        public ArgsErrorInitPlugin(IInjectorConfig cfg) {}

        @Override
        public void run() {}
    }

    public static class ArgsErrorRunPlugin implements Plugin {

        @Override
        public void run() {
            throw new AssertArgsError();
        }
    }
}