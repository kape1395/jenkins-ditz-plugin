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
package lt.kape1395.jenkins.ditz.model;

/**
 * Represents one issue in DITZ.
 * @author k.petrauskas
 */
public class Issue {

    /**
     * Unique ID.
     */
    private String id;

    /**
     * Bug title.
     */
    private String title;

    /**
     * String representation of issue type.
     */
    private String typeName;

    /**
     * String representation of issue status.
     */
    private String statusName;

    /**
     * String representation of issue release.
     */
    private String releaseName;

    /**
     * String representation of issue component.
     */
    private String componentName;

    /**
     * Constructor.
     * @param id Id.
     * @param title Title.
     * @param typeName Type.
     * @param statusName Status.
     * @param releaseName Release.
     * @param componentName Component.
     */
    public Issue(
            String id, String title, String typeName,
            String statusName, String releaseName, String componentName) {
        this.id = id;
        this.title = title;
        this.typeName = typeName;
        this.statusName = statusName;
        this.releaseName = releaseName;
        this.componentName = componentName;
    }

    /**
     * Constructor.
     * @param id Id.
     * @param title Title.
     * @param typeName Type.
     * @param statusName Status.
     * @param releaseName Release.
     */
    public Issue(
            String id, String title, String typeName,
            String statusName, String releaseName) {
        this.id = id;
        this.title = title;
        this.typeName = typeName;
        this.statusName = statusName;
        this.releaseName = releaseName;
    }

    /**
     * Is the issue considered open?
     * @return true, if issue is open.
     */
    public boolean isOpen() {
        return statusName != null && !statusName.equalsIgnoreCase(Status.CLOSED.toString());
    }

    /**
     * Get issue id.
     * @return Issue id.
     */
    public String getId() {
        return id;
    }

    /**
     * Get title of the issue.
     * @return title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns string representation of the issue type.
     * @return type as a string.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Returns string representation of the issue status.
     * @return status as a string.
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * Returns string representation of the release, to which the issue is assigned to.
     * @return release name.
     */
    public String getReleaseName() {
        return releaseName;
    }

    /**
     * Returns string representation of the component, issue is registered for.
     * @return component name.
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Issue statuses.
     */
    public static enum Status {
        /**
         *  Issue is unstarted. This is an initial status.
         */
        UNSTARTED(":unstarted"),
        /**
         * Issue is started to deal with.
         */
        IN_PROGRESS(":in_progress"),
        /**
         * Issue was started and then stopped.
         */
        PAUSED(":paused"),
        /**
         * Issue was closed with some disposition.
         */
        CLOSED(":closed");

        /**
         * String representation of the status.
         */
        private String name;

        /**
         * Constructor.
         * @param name String representation of the status.
         */
        private Status(String name) {
            this.name = name;
        }

        /**
         * Show it as a string.
         * @return value as it is used in yaml files.
         */
        @Override
        public String toString() {
            return name;
        }
    }

}
