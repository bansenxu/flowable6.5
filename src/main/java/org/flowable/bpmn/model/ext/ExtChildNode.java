//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.flowable.bpmn.model.ext;

import java.io.Serializable;

public class ExtChildNode implements Serializable {
    private static final long serialVersionUID = 1L;
    private String resourceId;
    private ExtProperty properties;
    private String stencil;
    private String childShapes;
    private String outgoing;
    private String bounds;
    private String dockers;

    public ExtChildNode() {
    }

    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public ExtProperty getProperties() {
        return this.properties;
    }

    public void setProperties(ExtProperty properties) {
        this.properties = properties;
    }

    public String getStencil() {
        return this.stencil;
    }

    public void setStencil(String stencil) {
        this.stencil = stencil;
    }

    public String getChildShapes() {
        return this.childShapes;
    }

    public void setChildShapes(String childShapes) {
        this.childShapes = childShapes;
    }

    public String getOutgoing() {
        return this.outgoing;
    }

    public void setOutgoing(String outgoing) {
        this.outgoing = outgoing;
    }

    public String getBounds() {
        return this.bounds;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
    }

    public String getDockers() {
        return this.dockers;
    }

    public void setDockers(String dockers) {
        this.dockers = dockers;
    }
}
