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

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.net.URL;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.URLReader;

/**
 * Example for using the Nashorn engine without any caching involved
 */
public class NoCacheDemo {

    public static void main(String[] args) throws Exception {
        final URL lodash = new File("lodash.js").toURI().toURL();

        // Evaluate the script 20 times, using a new script engine for each iteration.
        for (int i = 0; i < 20; i++) {
            // Create a Nashorn script engine with default options and evaluate the script.
            final ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();
            evaluate(engine, lodash);
        }
    }

    private static Object evaluate(ScriptEngine engine, URL url) throws ScriptException {
        final long start = System.currentTimeMillis();
        try {
            return engine.eval(new URLReader(url));
        } finally {
            System.out.println("Evaluated " + url + " in " + (System.currentTimeMillis() - start) + " milliseconds");
        }
    }

}
