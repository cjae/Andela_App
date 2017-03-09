package com.esod.andelaapp.event;

import com.esod.andelaapp.model.Developer;

/**
 * Created by Jedidiah on 09/03/2017.
 */

public class DeveloperItemClickEvent {

    public final Developer developer;

    public DeveloperItemClickEvent(Developer developer) {
        this.developer = developer;
    }
}
