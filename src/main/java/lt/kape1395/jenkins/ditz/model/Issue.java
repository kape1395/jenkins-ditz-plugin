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
     * Indicates status change, occurred in this build.
     */
    private StatusChange statusChange;

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
        this.statusChange = StatusChange.UNCHANGED;
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
        this(id, title, typeName, statusName, releaseName, null);
    }

    /**
     * Constructor.
     * @param id Id.
     * @param title Title.
     * @param typeName Type.
     * @param status Status.
     * @param releaseName Release.
     */
    public Issue(
            String id, String title, String typeName,
            Status status, String releaseName) {
        this(id, title, typeName, status.toString(), releaseName);
    }

    /**
     * Returns the issue as human-readable string.
     * @return Issue as a string.
     */
    @Override
    public String toString() {
        return "Issue"
                + "(id=" + id
                + " status=" + statusName
                + " release=" + releaseName
                + " component=" + componentName
                + " statusChange=" + statusChange
                + " title=" + title
                + ")";
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
     * Returns a status change recorded for a particular build.
     * @return status change indicator.
     */
    public StatusChange getStatusChange() {
        return statusChange;
    }

    /**
     * Set new status change indicator.
     * @param statusChange new status change value.
     */
    public void setStatusChange(StatusChange statusChange) {
        this.statusChange = statusChange;
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

    /**
     *  Lists status changes, that can be recorded for a particular build.
     */
    public static enum StatusChange {
        /**
         * Issue was created in this build.
         */
        NEW,
        /**
         * Issue was closed in this build.
         */
        CLOSED,
        /**
         * Issue status is unchanged but additional
         * information was modified.
         * This is unused currently.
         */
        MODIFIED,
        /**
         * Issue is unchanged.
         */
        UNCHANGED;
    }

}
