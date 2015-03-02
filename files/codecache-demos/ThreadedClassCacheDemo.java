/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.File;
import java.net.URL;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.URLReader;

/**
 * Example for using the Nashorn internal class cache with multiple threads.
 */
public class ThreadedClassCacheDemo {

    public static void main(String[] args) throws Exception {
        // Get a Nashorn script engine with default options
        final ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();
        final URL lodash = new File("lodash.js").toURI().toURL();

        // Evaluate the script 20 times, using the same script engine but a new thread and global bindings
        // for each iteration. This allows the compiled script to be reused with JDK 8u20 and later.
        for (int i = 0; i < 20; i++) {
            runInThread(engine, lodash);
        }
    }

    private static void runInThread(final ScriptEngine engine, final URL url) {
        new Thread() {
            @Override
            public void run() {
                final Bindings b = engine.createBindings();
                final ScriptContext context = new SimpleScriptContext();
                context.setBindings(b, ScriptContext.ENGINE_SCOPE);

                final long start = System.currentTimeMillis();
                try {
                    engine.eval(new URLReader(url), context);
                } catch (final ScriptException e) {
                    System.err.println("Error evaluating script: " + e);
                }
                System.out.println("Evaluated " + url + " in " + (System.currentTimeMillis() - start) + " milliseconds");
            }
        }.run();
    }

}
