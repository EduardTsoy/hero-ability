package com.github.eduardtsoy.heroability.dto;

import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.impl.EntityImpl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BaseDTO extends EntityImpl {

    @Nonnull
    public List<Link> safeLinks() {
        if (getLinks() == null) {
            setLinks(new ArrayList<>());
        }
        return getLinks();
    }
}
