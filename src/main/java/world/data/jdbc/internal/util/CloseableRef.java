/*
 * dw-jdbc
 * Copyright 2017 data.world, Inc.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * This product includes software developed at data.world, Inc.(http://www.data.world/).
 */
package world.data.jdbc.internal.util;

/**
 * Helper for tracking an object to be closed via try-with-resources.
 */
public final class CloseableRef implements AutoCloseable {
    private AutoCloseable closeable;

    public CloseableRef(AutoCloseable obj) {
        this.closeable = obj;
    }

    public <T extends AutoCloseable> T set(T obj) {
        this.closeable = obj;
        return obj;
    }

    public <T> T detach(T obj) {
        this.closeable = null;
        return obj;
    }

    @Override
    public void close() throws Exception {
        if (closeable != null) {
            closeable.close();
            closeable = null;
        }
    }
}
