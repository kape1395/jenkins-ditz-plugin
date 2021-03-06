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
package lt.kape1395.jenkins.ditz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Logger;

import lt.kape1395.jenkins.ditz.model.Component;
import lt.kape1395.jenkins.ditz.model.Issue;
import lt.kape1395.jenkins.ditz.model.IssueStats;
import lt.kape1395.jenkins.ditz.model.Project;
import lt.kape1395.jenkins.ditz.model.Release;

import com.thoughtworks.xstream.XStream;

/**
 * Does some persistence of ditz project to a XML document.
 *
 * @author k.petrauskas
 */
public class XStreamDataSerializer implements DitzProjectDAO {
    /**
     * Logger for debugging.
     */
    private static Logger log = Logger.getLogger(XStreamDataSerializer.class.getName());
    /**
     * File to deal with. This is an XML file, with xstream serialized objects.
     */
    private File file;
    /**
     * XStream to be used for serialization / deserialization.
     */
    private XStream xstream;

    /**
     * Constructor.
     * @param file File to read from or store to.
     */
    public XStreamDataSerializer(File file) {
        this(file, null);
    }

    /**
     * Constructor.
     * @param file File to read from or store to.
     * @param xstream Preconfigured XStream.
     */
    public XStreamDataSerializer(File file, XStream xstream) {
        this.file = file;
        if (xstream == null) {
            this.xstream = createConfiguredXStream();
        } else {
            this.xstream = xstream;
        }
        log.fine("XStreamDataSerializer(file=" + file + ", ...)");
    }

    /**
     * Creates configured xstream.
     * @return xstream instance.
     */
    protected XStream createConfiguredXStream() {
        XStream x = new XStream();
        x.setClassLoader(Project.class.getClassLoader());
        x.alias("project", Project.class);
        x.alias("release", Release.class);
        x.alias("component", Component.class);
        x.alias("issue", Issue.class);
        x.alias("issueStats", IssueStats.class);
        x.setMode(XStream.XPATH_RELATIVE_REFERENCES);
        x.omitField(Issue.class, "open");
        x.omitField(IssueStats.class, "stats");
        return x;
    }

    /**
     * {@inheritDoc}
     */
    public Project loadProject() throws Exception {
        FileInputStream inputStream = new FileInputStream(file);
        Project project;
        try {
            project = (Project) xstream.fromXML(inputStream);
        } finally {
            inputStream.close();
        }
        return project;
    }

    /**
     * {@inheritDoc}
     */
    public void saveProject(Project project) throws Exception {
        FileOutputStream outputStream = new FileOutputStream(file);
        try {
            xstream.toXML(project, outputStream);
            outputStream.flush();
        } finally {
            outputStream.close();
        }
    }

}
