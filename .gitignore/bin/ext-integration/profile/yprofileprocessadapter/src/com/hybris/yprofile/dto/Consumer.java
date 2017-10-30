package com.hybris.yprofile.dto;

/**
 * Transfer object for correct yaas format.
 */
public class Consumer {

    private String ref;
    private String type;

    public Consumer(String ref, String type) {
        this.ref = ref;
        this.type = type;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Consumer{");
        sb.append("ref='").append(ref).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
