//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.flowable.bpmn.model.ext;

import java.io.Serializable;
import java.util.List;

public class ExtModelEditor implements Serializable {
    private static final long serialVersionUID = 1L;
    private String modelId;
    private List<ExtChildNode> childShapes;
    private String bounds;
    private String properties;
    private String stencil;
    private String stencilset;

    public ExtModelEditor() {
    }

    public String getModelId() {
        return this.modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public List<ExtChildNode> getChildShapes() {
        return this.childShapes;
    }

    public void setChildShapes(List<ExtChildNode> childShapes) {
        this.childShapes = childShapes;
    }

    public String getBounds() {
        return this.bounds;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
    }

    public String getProperties() {
        return this.properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getStencil() {
        return this.stencil;
    }

    public void setStencil(String stencil) {
        this.stencil = stencil;
    }

    public String getStencilset() {
        return this.stencilset;
    }

    public void setStencilset(String stencilset) {
        this.stencilset = stencilset;
    }
}
