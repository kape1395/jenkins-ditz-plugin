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
    public static final String STATUS_UNSTARTED = ":unstarted";
    public static final String STATUS_IN_PROGRESS = ":in_progress";
    public static final String STATUS_PAUSED = ":paused";
    public static final String STATUS_CLOSED = ":closed";

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

    public boolean isOpen() {
        return statusName != null && !statusName.equalsIgnoreCase(STATUS_CLOSED);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public String getComponentName() {
        return componentName;
    }

}
