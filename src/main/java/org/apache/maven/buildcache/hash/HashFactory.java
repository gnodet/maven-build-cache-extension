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
package org.apache.maven.buildcache.hash;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import net.openhft.hashing.LongHashFunction;

/**
 * HashFactory
 */
public enum HashFactory {
    SHA1(new SHA("SHA-1")),
    SHA256(new SHA("SHA-256")),
    SHA384(new SHA("SHA-384")),
    SHA512(new SHA("SHA-512")),
    XX(new Zah("XX", LongHashFunction.xx(), false)),
    XXMM(new Zah("XXMM", LongHashFunction.xx(), true)),
    METRO(new Zah("METRO", LongHashFunction.metro(), false)),
    METRO_MM(new Zah("METRO+MM", LongHashFunction.metro(), true));

    private static final Map<String, HashFactory> LOOKUP = new HashMap<>();

    static {
        for (HashFactory factory : HashFactory.values()) {
            LOOKUP.put(factory.getAlgorithm(), factory);
        }
    }

    public static HashFactory of(String algorithm) throws NoSuchAlgorithmException {
        final HashFactory factory = LOOKUP.get(algorithm);
        if (factory == null) {
            throw new NoSuchAlgorithmException(algorithm);
        }
        return factory;
    }

    private final Hash.Factory factory;

    HashFactory(Hash.Factory factory) {
        this.factory = factory;
    }

    public String getAlgorithm() {
        return factory.getAlgorithm();
    }

    public HashAlgorithm createAlgorithm() {
        return new HashAlgorithm(factory.algorithm());
    }

    public HashChecksum createChecksum(int count) {
        return new HashChecksum(factory.algorithm(), factory.checksum(count));
    }
}
