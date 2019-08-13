package com.xiaoyao.sp.core.event;

import com.xiaoyao.sp.core.commons.Request;

public class SPEvent implements Event {

    private EventType type;
    private Request source;

    public SPEvent() {
    }

    public SPEvent(EventType type, Request source) {
        this.type = type;
        this.source = source;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public void setSource(Request source) {
        this.source = source;
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public Request getSource() {
        return source;
    }
}
