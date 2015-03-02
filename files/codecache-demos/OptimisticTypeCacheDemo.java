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
 * Demo for using the optimistic type cache in combination with the optimistic type feature.
 */
public class OptimisticTypeCacheDemo {

    public static void main(String[] args) throws Exception {
        final URL lodash = new File("lodash.js").toURI().toURL();
        // Comment out this line to disable the optimistic type cache.
        System.setProperty("nashorn.typeInfo.maxFiles", "20000");

        // Evaluate the script 20 times using the optimistic type cache.
        // We create a new script engine on each iteration to disable internal class caching.
        for (int i = 0; i < 20; i++) {
            // Get a Nashorn script engine with optimistic types enabled.
            // Note that this option will only work with JDK 8u40 or later.
            final ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine("-ot=true");
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
