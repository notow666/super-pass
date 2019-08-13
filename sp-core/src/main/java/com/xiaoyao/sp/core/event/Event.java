package com.xiaoyao.sp.core.event;

import com.xiaoyao.sp.core.commons.Request;

public interface Event {
    EventType getType();

    Request getSource();
}
